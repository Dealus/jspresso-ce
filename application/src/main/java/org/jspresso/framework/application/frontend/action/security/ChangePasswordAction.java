/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.security;

import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.security.AbstractChangePasswordAction;
import org.jspresso.framework.application.frontend.action.std.EditComponentAction;
import org.jspresso.framework.view.descriptor.basic.BasicComponentViewDescriptor;

/**
 * This is the frontend action to initiate the logged-in user password change.
 * It will install a form view with a custom component designed to host :
 * <ol>
 * <li>the current password</li>
 * <li>the new password</li>
 * <li>the confirmation for the new password</li>
 * </ol>
 * This action must be combined (setting <code>okAction</code>) with a concrete
 * subclass of backend <code>AbstractChangePasswordAction</code> that performs
 * the actual password change depending on the authentication backend. Jspresso
 * offers two concrete implementations :
 * <ul>
 * <li><code>LdapChangePasswordAction</code> for LDAP based authentication
 * backend</li>
 * <li><code>DatabaseChangePasswordAction</code> for JDBC based authentication
 * backend</li>
 * </ul>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ChangePasswordAction<E, F, G> extends EditComponentAction<E, F, G> {

  /**
   * Constructs a new <code>ChangePasswordAction</code> instance.
   */
  public ChangePasswordAction() {
    BasicComponentViewDescriptor viewDescriptor = new BasicComponentViewDescriptor();
    viewDescriptor
        .setModelDescriptor(AbstractChangePasswordAction.PASSWD_CHANGE_DESCRIPTOR);
    setViewDescriptor(viewDescriptor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    setActionParameter(new HashMap<String, Object>(), context);
    return super.execute(actionHandler, context);
  }
}
