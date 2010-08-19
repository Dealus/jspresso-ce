/**
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

		
    [RemoteClass(alias="org.jspresso.framework.gui.remote.RComboBox")]
    public class RComboBox extends RComponent {

        private var _icons:Array;
        private var _translations:Array;
        private var _values:Array;
        private var _readOnly:Boolean;

        public function RComboBox() {
          //default constructor.
        }

        public function set icons(value:Array):void {
            _icons = value;
        }
        public function get icons():Array {
            return _icons;
        }

        public function set translations(value:Array):void {
            _translations = value;
        }
        public function get translations():Array {
            return _translations;
        }

        public function set values(value:Array):void {
            _values = value;
        }
        public function get values():Array {
            return _values;
        }

        public function get readOnly():Boolean {
          return _readOnly;
        }
        public function set readOnly(value:Boolean):void {
          _readOnly = value;
        }

    }
}