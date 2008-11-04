/**
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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


package org.jspresso.framework.gui.remote {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import org.jspresso.framework.util.remote.RemoteServerPeer;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.gui.remote.RAction")]
    public class RAction extends RemoteServerPeer {

        private var _description:String;
        private var _enabled:Boolean;
        private var _icon:RIcon;
        private var _mnemonicAsString:String;
        private var _name:String;

        public function set description(value:String):void {
            _description = value;
        }
        public function get description():String {
            return _description;
        }

        public function set enabled(value:Boolean):void {
            _enabled = value;
        }
        public function get enabled():Boolean {
            return _enabled;
        }

        public function set icon(value:RIcon):void {
            _icon = value;
        }
        public function get icon():RIcon {
            return _icon;
        }

        public function set mnemonicAsString(value:String):void {
            _mnemonicAsString = value;
        }
        public function get mnemonicAsString():String {
            return _mnemonicAsString;
        }

        public function set name(value:String):void {
            _name = value;
        }
        public function get name():String {
            return _name;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _description = input.readObject() as String;
            _enabled = input.readObject() as Boolean;
            _icon = input.readObject() as RIcon;
            _mnemonicAsString = input.readObject() as String;
            _name = input.readObject() as String;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_description);
            output.writeObject(_enabled);
            output.writeObject(_icon);
            output.writeObject(_mnemonicAsString);
            output.writeObject(_name);
        }
    }
}