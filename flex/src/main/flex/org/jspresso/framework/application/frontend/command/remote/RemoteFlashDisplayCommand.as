/**
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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

    [RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteFlashDisplayCommand")]
    public class RemoteFlashDisplayCommand extends RemoteAbstractDialogCommand {

        private var _swfUrl:String;
        private var _paramNames:Array;
        private var _paramValues:Array;

        public function RemoteFlashDisplayCommand() {
          //default constructor.
        }

        public function set swfUrl(value:String):void {
            _swfUrl = value;
        }
        public function get swfUrl():String {
            return _swfUrl;
        }

        public function set paramNames(value:Array):void {
            _paramNames = value;
        }
        public function get paramNames():Array {
            return _paramNames;
        }

        public function set paramValues(value:Array):void {
            _paramValues = value;
        }
        public function get paramValues():Array {
            return _paramValues;
        }

    }
}