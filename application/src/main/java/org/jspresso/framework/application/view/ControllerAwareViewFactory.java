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
package org.jspresso.framework.application.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.IController;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.backend.action.FindAction;
import org.jspresso.framework.application.backend.action.StaticQueryComponentsAction;
import org.jspresso.framework.application.backend.session.IApplicationSessionAware;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.action.lov.LovAction;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicDescriptorDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicReferencePropertyDescriptor;
import org.jspresso.framework.util.descriptor.DefaultDescriptor;
import org.jspresso.framework.util.descriptor.IDescriptor;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.view.AbstractViewFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicReferencePropertyViewDescriptor;

/**
 * An intermediate view factory abstract class used to handle session DI on
 * gates.
 *
 * @param <E>
 *     the actual gui component type used.
 * @param <F>
 *     the actual icon type used.
 * @param <G>
 *     the actual action type used.
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public abstract class ControllerAwareViewFactory<E, F, G> extends AbstractViewFactory<E, F, G> {

  /**
   * {@inheritDoc}
   */
  @Override
  protected void applyGateDependencyInjection(IGate gate, IActionHandler actionHandler) {
    super.applyGateDependencyInjection(gate, actionHandler);
    if (gate instanceof IApplicationSessionAware && actionHandler instanceof IController) {
      ((IApplicationSessionAware) gate).setApplicationSession(((IController) actionHandler).getApplicationSession());
    }
  }

  /**
   * Create enumeration reference property view.
   *
   * @param propertyViewDescriptor
   *     the property view descriptor
   * @param actionHandler
   *     the action handler
   * @param locale
   *     the locale
   * @return the i view
   */
  @SuppressWarnings("unchecked")
  protected IView<E> createEnumerationReferencePropertyView(IPropertyViewDescriptor propertyViewDescriptor,
                                                            IActionHandler actionHandler, Locale locale) {

    IEnumerationPropertyDescriptor propertyDescriptor = (IEnumerationPropertyDescriptor) propertyViewDescriptor
        .getModelDescriptor();
    BasicReferencePropertyDescriptor<IDescriptor> enumRefPropertyDescriptor = new BasicReferencePropertyDescriptor<>();
    enumRefPropertyDescriptor.setName(propertyDescriptor.getName());
    BasicComponentDescriptor<IDescriptor> elementDescriptor = (BasicComponentDescriptor<IDescriptor>) (
        (BasicComponentDescriptor<IDescriptor>) BasicDescriptorDescriptor.INSTANCE)
        .clone();
    elementDescriptor.setI18nNameKey(propertyDescriptor.getName());
    elementDescriptor.setRenderedProperties(Arrays.asList("description"));
    BasicPropertyDescriptor descriptionDescriptor =
        (BasicPropertyDescriptor) elementDescriptor.getPropertyDescriptor("description").clone();
    descriptionDescriptor.setI18nNameKey(propertyDescriptor.getName());
    Collection<IPropertyDescriptor> pds = elementDescriptor.getPropertyDescriptors();
    pds.add(descriptionDescriptor);
    elementDescriptor.setPropertyDescriptors(pds);
    enumRefPropertyDescriptor.setReferencedDescriptor(elementDescriptor);

    BasicReferencePropertyViewDescriptor enumRefPropertyViewDescriptor = new BasicReferencePropertyViewDescriptor();
    enumRefPropertyViewDescriptor.setModelDescriptor(enumRefPropertyDescriptor);

    LovAction<E, F, G> enumLovAction = (LovAction<E, F, G>) getLovAction().clone();
    enumRefPropertyViewDescriptor.setLovAction(enumLovAction);

    FrontendAction<E, F, G> enumLovOkFrontAction = (FrontendAction<E, F, G>) enumLovAction.getOkAction().clone();
    enumLovAction.setOkAction(enumLovOkFrontAction);

    enumLovOkFrontAction.setNextAction(new BackendAction() {
      @Override
      public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
        DefaultDescriptor selectedEnumerationComponent = getActionParameter(context);
        if (selectedEnumerationComponent != null) {
          getViewConnector(context).setConnectorValue(null);
          getViewConnector(context).setConnectorValue(selectedEnumerationComponent.getName());
        } else {
          getViewConnector(context).setConnectorValue(null);
        }
        return super.execute(actionHandler, context);
      }
    });

    FrontendAction<E, F, G> enumLovFindFrontAction = (FrontendAction<E, F, G>) enumLovAction.getFindAction().clone();
    enumLovAction.setFindAction(enumLovFindFrontAction);

    FindAction enumLovFindBackAction = (FindAction) enumLovFindFrontAction.getWrappedAction(null).clone();
    enumLovFindFrontAction.setWrappedAction(enumLovFindBackAction);

    List<IDescriptor> values = new ArrayList<>();
    List<String> enumerationValues = new ArrayList<>(propertyDescriptor.getEnumerationValues());
    filterEnumerationValues(enumerationValues, propertyViewDescriptor);
    if (!propertyDescriptor.isMandatory()) {
      enumerationValues.add(0, "");
    }
    for (String enumerationValue : enumerationValues) {
      DefaultDescriptor valueComponent = new DefaultDescriptor();
      valueComponent.setName(enumerationValue);
      if (enumerationValue != null && propertyDescriptor.isTranslated()) {
        if ("".equals(enumerationValue)) {
          valueComponent.setDescription(" ");
        } else {
          valueComponent.setDescription(propertyDescriptor.getI18nValue(enumerationValue, actionHandler, locale));
        }
      } else {
        if (enumerationValue == null) {
          valueComponent.setDescription(" ");
        } else {
          valueComponent.setDescription(enumerationValue);
        }
      }
      values.add(valueComponent);
    }


    StaticQueryComponentsAction queryEnumValuesAction = new StaticQueryComponentsAction();
    queryEnumValuesAction.setComponentStore(values);
    enumLovFindBackAction.setQueryAction(queryEnumValuesAction);

    return createReferencePropertyView(enumRefPropertyViewDescriptor, actionHandler, locale);
  }
}
