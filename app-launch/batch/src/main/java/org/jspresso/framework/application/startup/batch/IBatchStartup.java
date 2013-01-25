/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.startup.batch;

import org.jspresso.framework.application.startup.IStartup;

/**
 * Batch startup interface supporting command line arguments.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IBatchStartup extends IStartup {

  /**
   * Parses command line arguments.
   * 
   * @param args
   *          command line arguments.
   * @return true if parsing was complete and succesfull.
   */
  boolean parseCmdLine(String[] args);
}
