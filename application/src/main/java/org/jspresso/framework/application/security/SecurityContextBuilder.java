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
package org.jspresso.framework.application.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.security.ISecurityContextBuilder;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Security context builder that helps building a security context.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SecurityContextBuilder implements ISecurityContextBuilder {

  private Map<String, Object>       currentSecurityContext;
  private List<Map<String, Object>> snapshots;

  /**
   * Constructs a new <code>SecurityContextBuilder</code> instance.
   */
  public SecurityContextBuilder() {
    currentSecurityContext = new HashMap<String, Object>();
    currentSecurityContext.put(SecurityContextConstants.AUTH_TYPE,
        SecurityContextConstants.AUTH_VISIBLE);
    snapshots = new ArrayList<Map<String, Object>>();
  }

  /**
   * Returns the current security context this builder works on.
   * 
   * @return the current security context this builder works on.
   */
  public Map<String, Object> getSecurityContext() {
    // return a defensive copy.
    return new HashMap<String, Object>(currentSecurityContext);
  }

  /**
   * {@inheritDoc}
   */
  public SecurityContextBuilder pushToSecurityContext(Object contextElement) {
    snapshots.add(getSecurityContext());
    if (contextElement instanceof Workspace) {
      return append((Workspace) contextElement);
    } else if (contextElement instanceof Module) {
      return append((Module) contextElement);
    } else if (contextElement instanceof ActionMap) {
      return append((ActionMap) contextElement);
    } else if (contextElement instanceof ActionList) {
      return append((ActionList) contextElement);
    } else if (contextElement instanceof IAction) {
      return append((IAction) contextElement);
    } else if (contextElement instanceof IGate) {
      return append((IGate) contextElement);
    } else if (contextElement instanceof ITreeLevelDescriptor) {
      return append((ITreeLevelDescriptor) contextElement);
    } else if (contextElement instanceof IViewDescriptor) {
      return append((IViewDescriptor) contextElement);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SecurityContextBuilder restoreLastSecurityContextSnapshot() {
    if (snapshots.size() > 0) {
      currentSecurityContext = snapshots.remove(snapshots.size() - 1);
    }
    return this;
  }

  private SecurityContextBuilder append(@SuppressWarnings("unused") IGate gate) {
    currentSecurityContext.put(SecurityContextConstants.AUTH_TYPE,
        SecurityContextConstants.AUTH_ENABLE);
    return this;
  }

  private SecurityContextBuilder append(
      @SuppressWarnings("unused") ITreeLevelDescriptor treeLevelDescriptor) {
    return this;
  }

  private SecurityContextBuilder append(
      @SuppressWarnings("unused") IViewDescriptor viewDescriptor) {
    return this;
  }

  private SecurityContextBuilder append(Module module) {
    if (module != null) {
      Module currentModule = module;
      List<String> moduleChain = new ArrayList<String>();
      while (currentModule != null) {
        if (currentModule.getPermId() != null) {
          moduleChain.add(currentModule.getPermId());
        }
        currentModule = currentModule.getParent();
      }
      Collections.reverse(moduleChain);
      currentSecurityContext.put(SecurityContextConstants.MODULE_CHAIN,
          moduleChain);
    }
    return this;
  }

  private SecurityContextBuilder append(Workspace workspace) {
    if (workspace != null) {
      currentSecurityContext.put(SecurityContextConstants.WORKSPACE,
          workspace.getPermId());
    }
    return this;
  }

  private SecurityContextBuilder append(ActionList actionList) {
    if (actionList != null) {
      currentSecurityContext.put(SecurityContextConstants.ACTION_LIST,
          actionList.getPermId());
    }
    return this;
  }

  private SecurityContextBuilder append(ActionMap actionMap) {
    if (actionMap != null) {
      currentSecurityContext.put(SecurityContextConstants.ACTION_MAP,
          actionMap.getPermId());
    }
    return this;
  }

  private SecurityContextBuilder append(IAction action) {
    if (action != null) {
      currentSecurityContext.put(SecurityContextConstants.ACTION,
          action.getPermId());
    }
    return this;
  }
}
