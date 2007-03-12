package com.d2s.framework.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that performs a JAAS login. The name of the JAAS configuration is
 * read from the servlet config. After succesful login, username and password
 * are put in the session. Note that, in contrast to the
 * <code>SecurityContextFilter</code>, the login module used here is supposed
 * to perform a real authentication. With JBoss, you could use the
 * <code>UsersRolesLoginModule</code> for example. <br>
 * Note that this is sample implementation, and is written only in order to demo
 * the use of the {@link SecurityContextFilter} class. In a real life
 * implementation, one would probably never hard-code names of redirect url's,
 * names of form parameters and names of session attributes.
 */
public class LoginServlet extends HttpServlet {

  private static final long  serialVersionUID     = 6326611145492998226L;

  /**
   * <code>JAAS_APPL_DEFAULT</code>="other".
   */
  public static final String JAAS_APPL_DEFAULT    = "other";
  /**
   * <code>JAAS_APPL_PARAM_NAME</code>="jaas-application".
   */
  public static final String JAAS_APPL_PARAM_NAME = "jaas-application";

  /**
   * <code>SUCCESS_DEFAULT</code>="main.html".
   */
  public static final String SUCCESS_DEFAULT      = "/main.html";
  /**
   * <code>SUCCESS_PARAM_NAME</code>="success.redirect".
   */
  public static final String SUCCESS_PARAM_NAME   = "success.redirect";

  /**
   * <code>ERROR_DEFAULT</code>="login-error.html".
   */
  public static final String ERROR_DEFAULT        = "/login-error.html";
  /**
   * <code>ERROR_PARAM_NAME</code>"error.redirect".
   */
  public static final String ERROR_PARAM_NAME     = "error.redirect";

  /**
   * The name of the JAAS application, the key for finding the JAAS module
   * configuration (e.g. in an auth.conf file). In JBoss this matches the
   * security domain name in the login-config.xml file.
   */
  private String             jaasApplicationName;

  private String             successRedirectUrl;
  private String             errorRedirectUrl;

  /**
   * Initializes the servlet. Reads the name of the JAAS configuration.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void init(ServletConfig config) {
    jaasApplicationName = config.getInitParameter(JAAS_APPL_PARAM_NAME);
    if (jaasApplicationName == null) {
      jaasApplicationName = JAAS_APPL_DEFAULT;
    }

    successRedirectUrl = config.getInitParameter(SUCCESS_PARAM_NAME);
    if (successRedirectUrl == null) {
      successRedirectUrl = SUCCESS_DEFAULT;
    }

    errorRedirectUrl = config.getInitParameter(ERROR_PARAM_NAME);
    if (errorRedirectUrl == null) {
      errorRedirectUrl = ERROR_DEFAULT;
    }
  }

  /**
   * Processes the login form. The form is supposed to have two input values:
   * username and password.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    // retrieve form parameter values from request
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    // just for demo purposes: do never login if no username is set
    if (username.trim().equals("")) {
      // redirect to the error page
      response.sendRedirect(request.getContextPath() + errorRedirectUrl);
      return;
    }

    // create a JAAS callback handler and hand it over username and password
    CallbackHandler callbackHandler = new UserPasswordHandler(username,
        password);

    // perform the JAAS login; it will callback on the callbackHandler to obtain
    // username and password
    try {
      LoginContext loginContext = new LoginContext(jaasApplicationName,
          callbackHandler);
      loginContext.login();

      request.getSession().setAttribute("SUBJECT", loginContext.getSubject());

      // redirect to the main menu page
      response.sendRedirect(request.getContextPath() + successRedirectUrl);
    } catch (LoginException le) {
      // redirect to the error page
      response.sendRedirect(request.getContextPath() + errorRedirectUrl);
    }
  }

  /**
   * Simple JAAS callback handler that provides username and password.
   */
  private static final class UserPasswordHandler implements CallbackHandler {

    private String username;
    private char[] password;

    /**
     * Constructs a callback handler.
     */
    private UserPasswordHandler(String username, String password) {
      this.username = username;
      this.password = password.toCharArray();
    }

    /**
     * Handles the JAAS callbacks.
     * <p>
     * {@inheritDoc}
     */
    public void handle(Callback[] callbacks)
        throws UnsupportedCallbackException {
      for (int i = 0; i < callbacks.length; i++) {
        if (callbacks[i] instanceof NameCallback) {
          ((NameCallback) callbacks[i]).setName(username);
        } else if (callbacks[i] instanceof PasswordCallback) {
          ((PasswordCallback) callbacks[i]).setPassword(password);
        } else {
          throw new UnsupportedCallbackException(callbacks[i]);
        }
      }
    }
  }
}
