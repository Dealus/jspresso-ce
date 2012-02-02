/**
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
  import org.jspresso.framework.gui.remote.RAction;
  import org.jspresso.framework.gui.remote.RActionList;

		
    [RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand")]
    public class RemoteInitCommand extends RemoteCommand {

        private var _actions:Array;
        private var _secondaryActions:Array;
        private var _helpActions:Array;
        private var _navigationActions:Array;
        private var _workspaceActions:RActionList;
        private var _workspaceNames:Array;
        private var _exitAction:RAction;

        public function RemoteInitCommand() {
          //default constructor.
        }

        public function set actions(value:Array):void {
            _actions = value;
        }
        public function get actions():Array {
            return _actions;
        }

        public function set helpActions(value:Array):void {
            _helpActions = value;
        }
        public function get helpActions():Array {
            return _helpActions;
        }

        public function set workspaceActions(value:RActionList):void {
            _workspaceActions = value;
        }
        public function get workspaceActions():RActionList {
            return _workspaceActions;
        }

        public function set workspaceNames(value:Array):void {
          _workspaceNames = value;
        }
        public function get workspaceNames():Array {
          return _workspaceNames;
        }

        public function get navigationActions():Array {
          return _navigationActions;
        }
        public function set navigationActions(value:Array):void {
          _navigationActions = value;
        }

        public function get exitAction():RAction {
          return _exitAction;
        }
        public function set exitAction(value:RAction):void {
          _exitAction = value;
        }

        public function get secondaryActions():Array
        {
          return _secondaryActions;
        }

        public function set secondaryActions(value:Array):void
        {
          _secondaryActions = value;
        }


    }
}