/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.gui.remote;

/**
 * A remote text component.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class RTextComponent extends RComponent {

  private static final long serialVersionUID = 2668241040260840359L;

  private int               maxLength = -1;

  /**
   * Constructs a new <code>RTextField</code> instance.
   * 
   * @param guid
   *          the guid.
   */
  public RTextComponent(String guid) {
    super(guid);
  }

  /**
   * Constructs a new <code>RTextComponent</code> instance. Only used for
   * serialization support.
   */
  public RTextComponent() {
    // For serialization support
  }

  /**
   * Gets the maxLength.
   * 
   * @return the maxLength.
   */
  public int getMaxLength() {
    return maxLength;
  }

  /**
   * Sets the maxLength.
   * 
   * @param maxLength
   *          the maxLength to set.
   */
  public void setMaxLength(int maxLength) {
    this.maxLength = maxLength;
  }
}
