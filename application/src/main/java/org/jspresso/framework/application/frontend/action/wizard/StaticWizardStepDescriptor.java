/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.wizard;

import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.util.descriptor.DefaultDescriptor;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.descriptor.IViewDescriptor;


/**
 * A static wizard step.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class StaticWizardStepDescriptor implements IWizardStepDescriptor {

  private DefaultDescriptor     descriptor;
  private String                nextLabelKey;
  private IWizardStepDescriptor nextStepDescriptor;

  private IAction               onEnterAction;
  private IAction               onLeaveAction;

  private String                previousLabelKey;
  private IWizardStepDescriptor previousStepDescriptor;

  private IViewDescriptor       viewDescriptor;

  /**
   * Constructs a new <code>StaticWizardStepDescriptor</code> instance.
   */
  public StaticWizardStepDescriptor() {
    descriptor = new DefaultDescriptor();
  }

  /**
   * {@inheritDoc}
   */
  public boolean canFinish(Map<String, Object> context) {
    return getNextStepDescriptor(context) == null;
  }

  /**
   * {@inheritDoc}
   */
  public String getDescription() {
    return descriptor.getDescription();
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    if (getDescription() != null) {
      return translationProvider.getTranslation(getDescription(), locale);
    }
    return getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    return translationProvider.getTranslation(getName(), locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return descriptor.getName();
  }

  /**
   * Gets the nextLabelKey.
   * 
   * @return the nextLabelKey.
   */
  public String getNextLabelKey() {
    return nextLabelKey;
  }

  /**
   * {@inheritDoc}
   */
  public IWizardStepDescriptor getNextStepDescriptor(
      @SuppressWarnings("unused")
      Map<String, Object> context) {
    return nextStepDescriptor;
  }

  /**
   * Gets the onEnterAction.
   * 
   * @return the onEnterAction.
   */
  public IAction getOnEnterAction() {
    return onEnterAction;
  }

  /**
   * Gets the onLeaveAction.
   * 
   * @return the onLeaveAction.
   */
  public IAction getOnLeaveAction() {
    return onLeaveAction;
  }

  /**
   * Gets the previousLabelKey.
   * 
   * @return the previousLabelKey.
   */
  public String getPreviousLabelKey() {
    return previousLabelKey;
  }

  /**
   * {@inheritDoc}
   */
  public IWizardStepDescriptor getPreviousStepDescriptor(
      @SuppressWarnings("unused")
      Map<String, Object> context) {
    return previousStepDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getViewDescriptor() {
    return viewDescriptor;
  }

  /**
   * @param description
   *            the description to set.
   * @see org.jspresso.framework.util.descriptor.DefaultDescriptor#setDescription(java.lang.String)
   */
  public void setDescription(String description) {
    descriptor.setDescription(description);
  }

  /**
   * @param name
   *            the name to set.
   * @see org.jspresso.framework.util.descriptor.DefaultDescriptor#setName(java.lang.String)
   */
  public void setName(String name) {
    descriptor.setName(name);
  }

  /**
   * Sets the nextLabelKey.
   * 
   * @param nextLabelKey
   *            the nextLabelKey to set.
   */
  public void setNextLabelKey(String nextLabelKey) {
    this.nextLabelKey = nextLabelKey;
  }

  /**
   * Sets the nextStepDescriptor.
   * 
   * @param nextStepDescriptor
   *            the nextStepDescriptor to set.
   */
  public void setNextStepDescriptor(IWizardStepDescriptor nextStepDescriptor) {
    this.nextStepDescriptor = nextStepDescriptor;
    if (nextStepDescriptor instanceof StaticWizardStepDescriptor) {
      ((StaticWizardStepDescriptor) nextStepDescriptor).previousStepDescriptor = this;
    }
  }

  /**
   * Sets the onEnterAction.
   * 
   * @param onEnterAction
   *            the onEnterAction to set.
   */
  public void setOnEnterAction(IAction onEnterAction) {
    this.onEnterAction = onEnterAction;
  }

  /**
   * Sets the onLeaveAction.
   * 
   * @param onLeaveAction
   *            the onLeaveAction to set.
   */
  public void setOnLeaveAction(IAction onLeaveAction) {
    this.onLeaveAction = onLeaveAction;
  }

  /**
   * Sets the previousLabelKey.
   * 
   * @param previousLabelKey
   *            the previousLabelKey to set.
   */
  public void setPreviousLabelKey(String previousLabelKey) {
    this.previousLabelKey = previousLabelKey;
  }

  /**
   * Sets the viewDescriptor.
   * 
   * @param viewDescriptor
   *            the viewDescriptor to set.
   */
  public void setViewDescriptor(IViewDescriptor viewDescriptor) {
    this.viewDescriptor = viewDescriptor;
  }

}
