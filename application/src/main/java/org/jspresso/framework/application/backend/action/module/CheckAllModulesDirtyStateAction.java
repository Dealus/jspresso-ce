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
package org.jspresso.framework.application.backend.action.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;

/**
 * This action recomputes all application modules dirty state. All the
 * workspaces are traversed as well as, for each workspace, the whole module
 * hierarchy. This action is typically triggered before a user exists the
 * application to bring up a notification of potentially lost pending changes.
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
public class CheckAllModulesDirtyStateAction<E, F, G> extends
    AbstractModuleDirtyStateAction<E, F, G> {

  /**
   * Returns all application modules that have been marked dirty.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Collection<Module> getModulesToCheck(Map<String, Object> context) {
    Collection<Module> modulesToCheck = new ArrayList<Module>();
    for (String workspaceName : getController(context).getWorkspaceNames()) {
      Workspace ws = getController(context).getWorkspace(workspaceName);
      List<Module> modules = ws.getModules();
      if (modules != null) {
        for (Module m : modules) {
          registerModule(m, modulesToCheck);
        }
      }
    }
    return modulesToCheck;
  }

  private void registerModule(Module module, Collection<Module> modulesToCheck) {
    if (module.isStarted()) {
      modulesToCheck.add(module);
    }
    List<Module> subModules = module.getSubModules();
    if (subModules != null) {
      for (Module subModule : subModules) {
        registerModule(subModule, modulesToCheck);
      }
    }
  }
}
