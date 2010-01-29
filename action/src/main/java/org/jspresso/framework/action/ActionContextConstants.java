/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.action;

/**
 * Well-known action context keys.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class ActionContextConstants {

  /**
   * ACTION_COMMAND context key.
   */
  public static final String ACTION_COMMAND   = "ACTION_COMMAND";

  /**
   * ACTION_PARAM context key.
   */
  public static final String ACTION_PARAM     = "ACTION_PARAM";

  /**
   * ACTION_WIDGET context key.
   */
  public static final String ACTION_WIDGET    = "ACTION_WIDGET";

  /**
   * FRONT_CONTROLLER context key.
   */
  public static final String FRONT_CONTROLLER = "FRONT_CONTROLLER";

  /**
   * BACK_CONTROLLER context key.
   */
  public static final String BACK_CONTROLLER  = "BACK_CONTROLLER";

  /**
   * MODEL_DESCRIPTOR context key.
   */
  public static final String MODEL_DESCRIPTOR = "MODEL_DESCRIPTOR";

  /**
   * MODULE context key.
   */
  public static final String MODULE           = "MODULE";

  /**
   * SELECTED_INDICES context key.
   */
  public static final String SELECTED_INDICES = "SELECTED_INDICES";

  /**
   * SOURCE_COMPONENT context key.
   */
  public static final String SOURCE_COMPONENT = "SOURCE_COMPONENT";

  /**
   * VIEW_CONNECTOR context key.
   */
  public static final String VIEW_CONNECTOR   = "VIEW_CONNECTOR";

  /**
   * SELECTED_MODEL context key.
   */
  public static final String SELECTED_MODEL   = "SELECTED_MODEL";

  private ActionContextConstants() {
    // to prevent this class from being instanciated.
  }
}
