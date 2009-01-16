/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.controller.remote;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.CallbackHandler;

import org.jspresso.framework.application.frontend.command.remote.CommandException;
import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand;
import org.jspresso.framework.application.frontend.controller.AbstractFrontendController;
import org.jspresso.framework.binding.IConfigurableConnectorFactory;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.remote.RemoteConnectorFactory;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.event.ISelectable;
import org.jspresso.framework.util.remote.IRemotePeer;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
import org.jspresso.framework.view.IActionFactory;
import org.jspresso.framework.view.IViewFactory;
import org.jspresso.framework.view.remote.RemoteActionFactory;

/**
 * Default implementation of a remote frontend controller. This implementation
 * is usable "as-is".
 * <p>
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
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultRemoteController extends
    AbstractFrontendController<RComponent, RIcon, RAction> implements
    IRemoteCommandHandler, IRemotePeerRegistry {

  private IRemotePeerRegistry remotePeerRegistry;
  private List<RemoteCommand> commandQueue;
  private boolean             commandRegistrationEnabled;

  /**
   * Constructs a new <code>DefaultRemoteController</code> instance.
   */
  public DefaultRemoteController() {
    commandQueue = new ArrayList<RemoteCommand>();
    commandRegistrationEnabled = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected CallbackHandler createLoginCallbackHandler() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void displayWorkspace(
      @SuppressWarnings("unused") String workspaceName) {
    // NO-OP as of now
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkAccess(@SuppressWarnings("unused") ISecurable securable) {
    // Empty implementation for testing.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<RemoteCommand> handleCommands(List<RemoteCommand> commands) {
    try {
      commandRegistrationEnabled = true;
      commandQueue.clear();
      if (commands != null) {
        for (RemoteCommand command : commands) {
          handleCommand(command);
        }
      }
    } finally {
      commandRegistrationEnabled = false;
    }
    return commandQueue;
  }

  /**
   * Handles a single command.
   * 
   * @param command
   *          the command to handle.
   */
  protected void handleCommand(RemoteCommand command) {
    IRemotePeer targetPeer = getRegistered(command.getTargetPeerGuid());
    if (targetPeer == null) {
      throw new CommandException("Target remote peer could not be retrieved");
    }
    if (command instanceof RemoteValueCommand) {
      ((IValueConnector) targetPeer)
          .setConnectorValue(((RemoteValueCommand) command).getValue());
    } else if (command instanceof RemoteSelectionCommand) {
      ((ISelectable) targetPeer).setSelectedIndices(
          ((RemoteSelectionCommand) command).getSelectedIndices(),
          ((RemoteSelectionCommand) command).getLeadingIndex());
    } else {
      throw new CommandException("Unsupported command type : "
          + command.getClass().getSimpleName());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerCommand(RemoteCommand command) {
    if (commandRegistrationEnabled) {
      commandQueue.add(command);
    }
  }

  /**
   * {@inheritDoc}
   */
  public IRemotePeer getRegistered(String guid) {
    return remotePeerRegistry.getRegistered(guid);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isRegistered(String guid) {
    return remotePeerRegistry.isRegistered(guid);
  }

  /**
   * {@inheritDoc}
   */
  public void register(IRemotePeer remotePeer) {
    remotePeerRegistry.register(remotePeer);
  }

  /**
   * {@inheritDoc}
   */
  public void unregister(String guid) {
    remotePeerRegistry.unregister(guid);
  }

  /**
   * Sets the remotePeerRegistry.
   * 
   * @param remotePeerRegistry
   *          the remotePeerRegistry to set.
   */
  public void setRemotePeerRegistry(IRemotePeerRegistry remotePeerRegistry) {
    this.remotePeerRegistry = remotePeerRegistry;
  }

  /**
   * Updates the view factory with the remote peer registry.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setViewFactory(
      IViewFactory<RComponent, RIcon, RAction> viewFactory) {
    IActionFactory<RAction, RComponent> actionFactory = viewFactory
        .getActionFactory();
    if (actionFactory instanceof RemoteActionFactory) {
      ((RemoteActionFactory) actionFactory).setRemoteCommandHandler(this);
      ((RemoteActionFactory) actionFactory).setRemotePeerRegistry(this);
    }
    IConfigurableConnectorFactory connectorFactory = viewFactory
        .getConnectorFactory();
    if (connectorFactory instanceof RemoteConnectorFactory) {
      ((RemoteConnectorFactory) connectorFactory).setRemoteCommandHandler(this);
      ((RemoteConnectorFactory) connectorFactory).setRemotePeerRegistry(this);
    }
    super.setViewFactory(viewFactory);
  }

}
