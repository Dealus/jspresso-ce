/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.ulc;

import java.util.Locale;
import java.util.Map;

import javax.swing.KeyStroke;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.IActionFactory;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.IDisplayableAction;
import com.ulcjava.base.application.IAction;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.util.ULCIcon;

/**
 * An ulc action factory.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UlcActionFactory implements IActionFactory<IAction, ULCComponent> {

  private ITranslationProvider  translationProvider;
  private IIconFactory<ULCIcon> iconFactory;

  /**
   * {@inheritDoc}
   */
  public IAction createAction(IDisplayableAction action,
      IActionHandler actionHandler, IView<ULCComponent> view, Locale locale) {
    return createAction(action, actionHandler, view.getPeer(), view
        .getDescriptor().getModelDescriptor(), view.getConnector(), locale);
  }

  /**
   * {@inheritDoc}
   */
  public IAction createAction(IDisplayableAction action,
      IActionHandler actionHandler, ULCComponent sourceComponent,
      IModelDescriptor modelDescriptor, IValueConnector viewConnector,
      Locale locale) {
    return new ActionAdapter(action, actionHandler, sourceComponent,
        modelDescriptor, viewConnector, locale);
  }

  private final class ActionAdapter extends
      com.ulcjava.base.application.AbstractAction {

    private static final long                serialVersionUID = 5819377672533326496L;

    private com.d2s.framework.action.IAction action;
    private IActionHandler                   actionHandler;
    private ULCComponent                     sourceComponent;
    private IModelDescriptor                 modelDescriptor;
    private IValueConnector                  viewConnector;

    /**
     * Constructs a new <code>ActionAdapter</code> instance.
     * 
     * @param action
     * @param actionHandler
     * @param sourceComponent
     * @param modelDescriptor
     * @param viewConnector
     * @param locale
     */
    public ActionAdapter(IDisplayableAction action,
        IActionHandler actionHandler, ULCComponent sourceComponent,
        IModelDescriptor modelDescriptor, IValueConnector viewConnector,
        Locale locale) {
      this.action = action;
      this.actionHandler = actionHandler;
      this.sourceComponent = sourceComponent;
      this.modelDescriptor = modelDescriptor;
      this.viewConnector = viewConnector;
      putValue(IAction.NAME, action.getI18nName(translationProvider, locale));
      if (action.getDescription() != null) {
        putValue(IAction.SHORT_DESCRIPTION, action.getI18nDescription(
            translationProvider, locale)
            + TOOLTIP_ELLIPSIS);
      }
      putValue(IAction.SMALL_ICON, iconFactory.getIcon(
          action.getIconImageURL(), IIconFactory.TINY_ICON_SIZE));
      if (action.getMnemonicAsString() != null) {
        putValue(IAction.MNEMONIC_KEY, new Integer(KeyStroke.getKeyStroke(
            action.getMnemonicAsString()).getKeyCode()));
      }
    }

    /**
     * Triggers the action execution on the action handler. The following
     * initial action context is filled in :
     * <li> <code>ActionContextConstants.SOURCE_COMPONENT</code>
     * <li> <code>ActionContextConstants.VIEW_CONNECTOR</code>
     * <li> <code>ActionContextConstants.MODEL_CONNECTOR</code>
     * <li> <code>ActionContextConstants.MODEL_DESCRIPTOR</code>
     * <li> <code>ActionContextConstants.SELECTED_INDICES</code>
     * <li> <code>ActionContextConstants.LOCALE</code>
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      if (actionHandler != null) {
        Map<String, Object> actionContext = actionHandler.createEmptyContext();
        actionContext.put(ActionContextConstants.SOURCE_COMPONENT,
            sourceComponent);
        actionContext.put(ActionContextConstants.VIEW_CONNECTOR, viewConnector);
        if (viewConnector instanceof ICollectionConnectorProvider
            && ((ICollectionConnectorProvider) viewConnector)
                .getCollectionConnector() != null) {
          actionContext.put(ActionContextConstants.SELECTED_INDICES,
              ((ICollectionConnectorProvider) viewConnector)
                  .getCollectionConnector().getSelectedIndices());
        }
        actionContext.put(ActionContextConstants.MODEL_DESCRIPTOR,
            modelDescriptor);
        actionContext.put(ActionContextConstants.ACTION_COMMAND, e
            .getActionCommand());
        actionContext.put(ActionContextConstants.ACTION_WIDGET, e
            .getSource());
        if (action.getInitialContext() != null) {
          actionContext.putAll(action.getInitialContext());
        }
        actionHandler.execute(action, actionContext);
      }
    }

  }

  /**
   * Sets the iconFactory.
   * 
   * @param iconFactory
   *          the iconFactory to set.
   */
  public void setIconFactory(IIconFactory<ULCIcon> iconFactory) {
    this.iconFactory = iconFactory;
  }

  /**
   * Sets the translationProvider.
   * 
   * @param translationProvider
   *          the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }
}
