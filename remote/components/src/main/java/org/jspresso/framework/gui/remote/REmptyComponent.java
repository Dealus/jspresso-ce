/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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
 * A component used to fill an empty area.
 * 
 * @author Vincent Vandenschrick
 */
public class REmptyComponent extends RComponent {

  private static final long serialVersionUID = 6757171028589942114L;

  /**
   * Constructs a new {@code REmptyComponent} instance.
   *
   * @param guid
   *          the guid.
   */
  public REmptyComponent(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code REmptyComponent} instance. Only used for
   * GWT serialization support.
   */
  public REmptyComponent() {
    // For serialization support
  }
}
