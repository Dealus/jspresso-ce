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
package org.jspresso.framework.util.gate;

/**
 * An always closed gate.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class ClosedGate extends AbstractGate {

  /**
   * Singleton instance <code>INSTANCE</code>.
   */
  public static final ClosedGate INSTANCE = new ClosedGate();

  private ClosedGate() {
    // Private constructor.
  }

  /**
   * Always false.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isOpen() {
    return false;
  }

}
