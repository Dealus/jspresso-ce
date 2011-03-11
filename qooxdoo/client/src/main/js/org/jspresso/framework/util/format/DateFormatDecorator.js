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

qx.Class.define("org.jspresso.framework.util.format.DateFormatDecorator", {
      extend : qx.util.format.DateFormat,
      implement : qx.util.format.IFormat,

      properties : {
        formatDelegates : {
          check : "Array",
          nullable : false
        },
        remoteComponent : {
          check : "org.jspresso.framework.gui.remote.RComponent",
          nullable : false
        }
      },

      members : {
        /**
         * Uses the first format delegate.
         * 
         * @param obj
         *            {var} the object to format.
         * @return {String} the formatted object as a string.
         */
        format : function(obj) {
          return this.getFormatDelegates()[0].format(obj);
        },

        /**
         * Tries to parse until one delegate succeeds.
         * 
         * @param str
         *            {String} the string to parse.
         * @return {var} the parsed object.
         */
        parse : function(str) {
          if(str == null || str.length == 0) {
            return null;
          }
          var existingDate = this.getRemoteComponent().getState().getValue();
          var parsedDate;
          for (var i = 0; i < this.getFormatDelegates().length && !parsedDate; i++) {
            try {
              parsedDate = this.getFormatDelegates()[i].parse(str);
            } catch (err) {
              //if (i == this.getFormatDelegates().length -1) {
              //  throw new Error("No delegate could parse the string "
              //      + str);
              //}
            }
          }
          if(parsedDate == null) {
            return existingDate;
          }
          if(existingDate == null) {
            return parsedDate;
          } else {
            if (this.getRemoteComponent() instanceof org.jspresso.framework.gui.remote.RDateField) {
              parsedDate = new Date(
                parsedDate.getFullYear(),
                parsedDate.getMonth(),
                parsedDate.getDate(),
                existingDate.getHours(),
                existingDate.getMinutes(),
                existingDate.getSeconds(),
                existingDate.getMilliseconds());
            } else if (this.getRemoteComponent() instanceof org.jspresso.framework.gui.remote.RTimeField) {
              parsedDate = new Date(
                existingDate.getFullYear(),
                existingDate.getMonth(),
                existingDate.getDate(),
                parsedDate.getHours(),
                parsedDate.getMinutes(),
                parsedDate.getSeconds(),
                parsedDate.getMilliseconds());
            }
            return parsedDate;
          }
        }
      }
    });
