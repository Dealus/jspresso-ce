/**
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 */


package org.jspresso.framework.application.frontend.command.remote {

    import org.jspresso.framework.gui.remote.RComponent;
		
    [RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteWorkspaceDisplayCommand")]
    public class RemoteWorkspaceDisplayCommand extends RemoteCommand {

        private var _workspaceName:String;
        private var _workspaceView:RComponent;

        public function RemoteWorkspaceDisplayCommand() {
          //default constructor.
        }

        public function set workspaceName(value:String):void {
            _workspaceName = value;
        }
        public function get workspaceName():String {
            return _workspaceName;
        }

        public function set workspaceView(value:RComponent):void {
            _workspaceView = value;
        }
        public function get workspaceView():RComponent {
            return _workspaceView;
        }
    }
}