/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.printing.frontend.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.application.printing.model.IReport;
import com.d2s.framework.application.printing.model.basic.BasicReport;
import com.d2s.framework.application.printing.model.descriptor.IReportDescriptor;
import com.d2s.framework.application.printing.model.descriptor.basic.BasicReportDescriptor;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.model.IModelConnectorFactory;
import com.d2s.framework.model.descriptor.basic.BasicCollectionDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * Frontend action to select a report.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
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
public class PrintReportAction<E, F, G> extends AbstractChainedAction<E, F, G> {

  private IModelConnectorFactory  beanConnectorFactory;
  private List<IReportDescriptor> reportDescriptors;

  /**
   * Sets the reportDescriptors.
   * 
   * @param reportDescriptors
   *          the reportDescriptors to set.
   */
  public void setReportDescriptors(List<IReportDescriptor> reportDescriptors) {
    this.reportDescriptors = reportDescriptors;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    BasicCollectionDescriptor modelDescriptor = new BasicCollectionDescriptor();
    modelDescriptor.setCollectionInterface(List.class);
    modelDescriptor.setElementDescriptor(BasicReportDescriptor.INSTANCE);
    IValueConnector reportDescriptorsConnector = beanConnectorFactory
        .createModelConnector(modelDescriptor);
    reportDescriptorsConnector.setConnectorValue(createReportInstances(
        getTranslationProvider(context), getLocale(context)));
    context
        .put(ActionContextConstants.ACTION_PARAM, reportDescriptorsConnector);
    return super.execute(actionHandler, context);
  }

  private List<IReport> createReportInstances(
      ITranslationProvider translationProvider, Locale locale) {
    List<IReport> reports = new ArrayList<IReport>();
    if (reportDescriptors != null) {
      for (IReportDescriptor descriptor : reportDescriptors) {
        BasicReport report = new BasicReport();
        report.setName(descriptor.getI18nName(translationProvider, locale));
        report.setDescription(descriptor.getI18nDescription(translationProvider,
            locale));
        report.setReportDescriptor(descriptor);
        report.setContext(new HashMap<String, Object>());
        reports.add(report);
      }
    }
    return reports;
  }

  /**
   * Sets the beanConnectorFactory.
   * 
   * @param beanConnectorFactory
   *          the beanConnectorFactory to set.
   */
  public void setBeanConnectorFactory(
      IModelConnectorFactory beanConnectorFactory) {
    this.beanConnectorFactory = beanConnectorFactory;
  }
}
