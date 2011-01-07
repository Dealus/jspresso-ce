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

    import org.jspresso.framework.gui.remote.RAction;
		
    [RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteFileCommand")]
    public class RemoteFileCommand extends RemoteCommand {

        private var _cancelCallbackAction:RAction;
        private var _fileFilter:Object;
        private var _fileUrl:String;

        public function RemoteFileCommand() {
          //default constructor.
        }

        public function set cancelCallbackAction(value:RAction):void {
            _cancelCallbackAction = value;
        }
        public function get cancelCallbackAction():RAction {
            return _cancelCallbackAction;
        }

        public function set fileFilter(value:Object):void {
            _fileFilter = value;
        }
        public function get fileFilter():Object {
            return _fileFilter;
        }

        public function set fileUrl(value:String):void {
            _fileUrl = value;
        }
        public function get fileUrl():String {
            return _fileUrl;
        }
    }
}