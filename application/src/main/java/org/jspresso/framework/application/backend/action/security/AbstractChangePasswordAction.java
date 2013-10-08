/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.security;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.jspresso.framework.action.ActionBusinessException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicPasswordPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicStringPropertyDescriptor;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.lang.ObjectUtils;

/**
 * This is the base class for implementing an action that performs actual
 * modification of a logged-in user password. This implementation delegates to
 * subclasses the actual change in the concrete JAAS store. This backend action
 * expects a Map&lt;String,Object&gt; in as action parameter in the context.
 * This map must contain :
 * <p>
 * <ul>
 * <li>{@code password_current} entry containing current password. Entry
 * key can be referred to as PASSWD_CURRENT static constant.</li>
 * <li>{@code password_typed} entry containing the new password. Entry key
 * can be referred to as PASSWD_TYPED static constant.</li>
 * <li>{@code password_retyped} entry containing the new password retyped.
 * Entry key can be referred to as PASSWD_RETYPED static constant.</li>
 * </ul>
 * For the action to succeed, {@code current_password} must match the
 * logged-in user current password and {@code password_typed} and
 * {@code password_retyped} mut match between each other. The only method to
 * be implemented by concrete subclasses is :
 * <p>
 *
 * <pre>
 * protected abstract boolean changePassword(UserPrincipal userPrincipal,
 *           String currentPassword, String newPassword)
 * </pre>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractChangePasswordAction extends BackendAction {

  /**
   * {@code PASSWD_CHANGE_DESCRIPTOR} is a unique reference to the model
   * descriptor of the change password action.
   */
  public static final IComponentDescriptor<Map<String, String>> PASSWD_CHANGE_DESCRIPTOR = createPasswordChangeModel();
  /**
   * {@code TO_STRING}.
   */
  public static final String                                    TO_STRING                = "to_string";
  /**
   * {@code PASSWD_CURRENT}.
   */
  public static final String                                    PASSWD_CURRENT           = "password_current";
  /**
   * {@code PASSWD_RETYPED}.
   */
  public static final String                                    PASSWD_RETYPED           = "password_retyped";

  /**
   * {@code PASSWD_TYPED}.
   */
  public static final String                                    PASSWD_TYPED             = "password_typed";

  /**
   * {@code BASE64_ENCODING} is &quot;BASE64&quot;.
   */
  public static final String                                    BASE64_ENCODING          = "BASE64";
  /**
   * {@code BASE16_ENCODING} is &quot;HEX&quot;.
   */
  public static final String                                    BASE16_ENCODING          = "BASE16";
  /**
   * {@code HEX_ENCODING} is &quot;HEX&quot;.
   */
  public static final String                                    HEX_ENCODING             = "HEX";

  private String                                                digestAlgorithm;
  private String                                                hashEncoding;
  private boolean                                               allowEmptyPasswords      = true;

  private static IComponentDescriptor<Map<String, String>> createPasswordChangeModel() {
    BasicComponentDescriptor<Map<String, String>> passwordChangeModel = new BasicComponentDescriptor<>();
    BasicStringPropertyDescriptor toString = new BasicStringPropertyDescriptor();
    toString.setName(TO_STRING);
    BasicPasswordPropertyDescriptor currentPassword = new BasicPasswordPropertyDescriptor();
    currentPassword.setName(PASSWD_CURRENT);
    BasicPasswordPropertyDescriptor typedPassword = new BasicPasswordPropertyDescriptor();
    typedPassword.setName(PASSWD_TYPED);
    BasicPasswordPropertyDescriptor retypedPassword = new BasicPasswordPropertyDescriptor();
    retypedPassword.setName(PASSWD_RETYPED);

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<>();
    propertyDescriptors.add(toString);
    propertyDescriptors.add(currentPassword);
    propertyDescriptors.add(typedPassword);
    propertyDescriptors.add(retypedPassword);
    passwordChangeModel.setPropertyDescriptors(propertyDescriptors);
    passwordChangeModel.setToStringProperty(TO_STRING);
    passwordChangeModel.setRenderedProperties(Arrays.asList(PASSWD_CURRENT,
        PASSWD_TYPED, PASSWD_RETYPED));

    return passwordChangeModel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    Map<String, Object> actionParam = getModelConnector(
        context).getConnectorValue();
    String typedPasswd = (String) actionParam.get(PASSWD_TYPED);
    String retypedPasswd = (String) actionParam.get(PASSWD_RETYPED);
    if (!ObjectUtils.equals(typedPasswd, retypedPasswd)) {
      throw new ActionBusinessException(
          "Typed and retyped passwords are different.",
          "password.typed.retyped.different");
    }
    checkPasswordValidity(typedPasswd);
    UserPrincipal principal = getApplicationSession(context).getPrincipal();
    if (changePassword(principal, (String) actionParam.get(PASSWD_CURRENT),
        typedPasswd)) {
      setActionParameter(
          getTranslationProvider(context).getTranslation(
              "password.change.success", getLocale(context)), context);
      return super.execute(actionHandler, context);
    }
    return false;
  }

  /**
   * Gives the opportunity to check the new password validity against some
   * business rule. Buy default, it only checks that the password is not empty
   * if {@code allowEmptyPassword} is {@code false}.
   *
   * @param typedPasswd
   *          the password to check.
   */
  protected void checkPasswordValidity(String typedPasswd) {
    if (!isAllowEmptyPasswords()
        && (typedPasswd == null || typedPasswd.length() == 0)) {
      throw new ActionBusinessException("Empty passwords are not allowed.",
          "password.empty.disallowed");
    }
  }

  /**
   * Sets the digestAlgorithm to use to hash the password before storing it (MD5
   * for instance).
   * 
   * @param digestAlgorithm
   *          the digestAlgorithm to set.
   */
  public void setDigestAlgorithm(String digestAlgorithm) {
    this.digestAlgorithm = digestAlgorithm;
  }

  /**
   * Sets the hashEncoding to encode the password hash before storing it. You
   * may choose between :
   * <ul>
   * <li>{@code BASE64} for base 64 encoding.</li>
   * <li>{@code HEX} for base 16 encoding.</li>
   * </ul>
   * Default encoding is {@code BASE64}.
   *
   * @param hashEncoding
   *          the hashEncoding to set.
   */
  public void setHashEncoding(String hashEncoding) {
    this.hashEncoding = hashEncoding;
  }

  /**
   * Performs the effective password change depending on the underlying storage.
   * 
   * @param userPrincipal
   *          the connected user principal.
   * @param currentPassword
   *          the current password.
   * @param newPassword
   *          the new password.
   * @return true if password was changed successfully.
   */
  protected abstract boolean changePassword(UserPrincipal userPrincipal,
      String currentPassword, String newPassword);

  /**
   * Hashes a char array using the algorithm parametrised in the instance.
   * 
   * @param newPassword
   *          the new password to hash.
   * @return the password digest.
   * @throws NoSuchAlgorithmException
   *           when the digest algorithm is not supported.
   * @throws IOException
   *           whenever an I/O exception occurs.
   */
  protected String digestAndEncode(char... newPassword)
      throws NoSuchAlgorithmException, IOException {
    if (getDigestAlgorithm() != null) {
      MessageDigest md = MessageDigest.getInstance(getDigestAlgorithm());
      md.reset();
      md.update(new String(newPassword).getBytes("UTF-8"));

      byte[] digest = md.digest();
      return getPasswordStorePrefix() + encode(digest);
    }
    return new String(newPassword);
  }

  /**
   * Encodes the password hash based on the hash encoding parameter (either
   * Base64, Base16). Defaults to Base64.
   * 
   * @param source
   *          the byte array (hash) to encode.
   * @return the encoded string.
   */
  protected String encode(byte[] source) {
    String he = getHashEncoding();
    if (BASE64_ENCODING.equalsIgnoreCase(he)) {
      return Base64.encodeBase64String(source);
    }
    if (BASE16_ENCODING.equalsIgnoreCase(he)
        || HEX_ENCODING.equalsIgnoreCase(he)) {
      return Hex.encodeHexString(source);
    }
    // defaults to Base64
    return Base64.encodeBase64String(source);
  }

  /**
   * Gets the digestAlgorithm.
   * 
   * @return the digestAlgorithm.
   */
  protected String getDigestAlgorithm() {
    return digestAlgorithm;
  }

  /**
   * Gets the hashEncoding.
   * 
   * @return the hashEncoding.
   */
  protected String getHashEncoding() {
    return hashEncoding;
  }

  /**
   * Returns a prefix to use before storing a password. An example usage is to
   * prefix the password hash with the type of hash, e.g. {MD5}.
   * 
   * @return a prefix to use before storing a password.
   */
  protected String getPasswordStorePrefix() {
    return "";
  }

  /**
   * Gets the allowEmptyPasswords.
   * 
   * @return the allowEmptyPasswords.
   */
  public boolean isAllowEmptyPasswords() {
    return allowEmptyPasswords;
  }

  /**
   * Configures the possibility to choose an empty password.
   * <p>
   * Default value is {@code true}, i.e. allow for empty passwords.
   *
   * @param allowEmptyPasswords
   *          the allowEmptyPasswords to set.
   */
  public void setAllowEmptyPasswords(boolean allowEmptyPasswords) {
    this.allowEmptyPasswords = allowEmptyPasswords;
  }
}
