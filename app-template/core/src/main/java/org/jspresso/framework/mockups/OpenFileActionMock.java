/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.mockups;

import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.file.IFileOpenCallback;

/**
 * A mockup for SaveFileAction.
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
public class OpenFileActionMock<E, F, G> extends FrontendAction<E, F, G> {

  private IFileOpenCallback fileOpenCallback;

  /**
   * Sets the fileOpenCallback.
   * 
   * @param fileOpenCallback
   *          the fileOpenCallback to set.
   */
  public void setFileOpenCallback(IFileOpenCallback fileOpenCallback) {
    this.fileOpenCallback = fileOpenCallback;
  }

  /**
   * Gets the fileOpenCallback.
   * 
   * @return the fileOpenCallback.
   */
  public IFileOpenCallback getFileOpenCallback() {
    return fileOpenCallback;
  }

}