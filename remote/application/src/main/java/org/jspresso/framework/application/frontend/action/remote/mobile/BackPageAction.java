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
package org.jspresso.framework.application.frontend.action.remote.mobile;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.remote.AbstractRemoteAction;
import org.jspresso.framework.application.frontend.command.remote.RemoteEditCommand;
import org.jspresso.framework.application.frontend.command.remote.mobile.RemoteBackCommand;
import org.jspresso.framework.gui.remote.RComponent;

/**
 * Triggers back navigation of current page.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class BackPageAction extends AbstractRemoteAction {

  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    RemoteBackCommand backCommand = new RemoteBackCommand();
    backCommand.setTargetPeerGuid(((RComponent) getView(context).getPeer()).getGuid());
    registerCommand(backCommand, context);
    return super.execute(actionHandler, context);
  }
}
