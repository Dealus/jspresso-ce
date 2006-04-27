/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.action;

import java.util.Map;

/**
 * This interface establishes the general contract of an object able to axacute
 * actions (controllers for instance). No assumption is made on wether this
 * action is to be executed (a)synchroniously, transactionnally, ...
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IActionHandler {

  /**
   * Executes an action. Implementors should delegate the execution to the
   * action itself but are free to set the context of the execution (action
   * context, synchronous or not, transactionality, ...).
   * 
   * @param action
   *          the action to be executed.
   * @param context
   *          the action execution context.
   * @see IAction#execute(IActionHandler, Map)
   */
  void execute(IAction action, Map<String, Object> context);

  /**
   * Retrieves the initial action context from the controller. This context is
   * passed to the action chain and contains application-wide context key-value
   * pairs.
   * 
   * @return the map representing the initial context provided by this
   *         controller.
   */
  Map<String, Object> getInitialActionContext();

  /**
   * Creates an empty action context.
   * 
   * @return an empty action context.
   */
  Map<String, Object> createEmptyContext();
}
