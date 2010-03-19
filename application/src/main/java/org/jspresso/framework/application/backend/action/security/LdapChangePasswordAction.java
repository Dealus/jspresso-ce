/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.jspresso.framework.action.ActionBusinessException;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.ldap.LdapConstants;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DirContextSource;

/**
 * Concrete backend implementation of a change password action where password is
 * stored in an LDAP directory. The user DN to use to connect to the LDAP
 * directory is the one stored in the user principal from the login process.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class LdapChangePasswordAction extends AbstractChangePasswordAction {

  private String ldapUrl;

  /**
   * Configures the LDAP url (e.g. <i>http://localhost:389</i>) of the LDAP
   * directory. The user must be authorized to change its own password in the
   * LDAP backend.
   * 
   * @param ldapUrl
   *          the ldapUrl to set.
   */
  public void setLdapUrl(String ldapUrl) {
    this.ldapUrl = ldapUrl;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean changePassword(UserPrincipal userPrincipal,
      String currentPassword, String newPassword) {

    String userDn = (String) userPrincipal
        .getCustomProperty(UserPrincipal.USERDN_PROPERTY);

    DirContextSource contextSource = new DirContextSource();
    contextSource.setUrl(ldapUrl);
    contextSource.setUserDn(userDn);
    contextSource.setPassword(new String(currentPassword));
    LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
    try {
      contextSource.afterPropertiesSet();
      ldapTemplate.afterPropertiesSet();
    } catch (Exception ex) {
      throw new ActionException(ex);
    }
    List<ModificationItem> mods = new ArrayList<ModificationItem>();
    try {
      mods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
          new BasicAttribute(LdapConstants.PASSWORD_ATTIBUTE,
              digestAndEncode(newPassword.toCharArray()))));
      ldapTemplate.modifyAttributes(userDn, mods
          .toArray(new ModificationItem[0]));
    } catch (NoSuchAlgorithmException ex) {
      throw new ActionException(ex);
    } catch (UnsupportedEncodingException ex) {
      throw new ActionException(ex);
    } catch (IOException ex) {
      throw new ActionException(ex);
    } catch (AuthenticationException ex) {
      throw new ActionBusinessException("Current password is not valid.",
          "password.current.invalid");
    }
    return true;
  }

  /**
   * Returns a prefix to use before storing a password. An example usage is to
   * prefix the password hash with the type of hash, e.g. {MD5}.
   * 
   * @return a prefix to use before storing a password.
   */
  @Override
  protected String getPasswordStorePrefix() {
    if (getDigestAlgorithm() != null) {
      return "{" + getDigestAlgorithm() + "}";
    }
    return super.getPasswordStorePrefix();
  }
}
