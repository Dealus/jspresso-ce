/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.ArrayList;
import java.util.List;

import com.d2s.framework.model.component.IQueryComponent;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import com.d2s.framework.view.descriptor.IComponentViewDescriptor;
import com.d2s.framework.view.descriptor.ILovViewDescriptorFactory;
import com.d2s.framework.view.descriptor.ISubViewDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * A default implementation for lov view factories.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicLovViewDescriptorFactory implements ILovViewDescriptorFactory {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public IViewDescriptor createLovViewDescriptor(
      IReferencePropertyDescriptor entityRefDescriptor) {
    BasicSplitViewDescriptor lovViewDescriptor = new BasicSplitViewDescriptor();
    lovViewDescriptor.setMasterDetail(true);
    lovViewDescriptor
        .setLeftTopViewDescriptor(createQueryComponentViewDescriptor(entityRefDescriptor
            .getComponentDescriptor()));
    lovViewDescriptor
        .setRightBottomViewDescriptor(createResultViewDescriptor(entityRefDescriptor
            .getComponentDescriptor()));
    return lovViewDescriptor;

  }

  private IViewDescriptor createQueryComponentViewDescriptor(
      IComponentDescriptor<?> entityDescriptor) {
    BasicComponentViewDescriptor queryComponentViewDescriptor = new BasicComponentViewDescriptor();
    queryComponentViewDescriptor.setModelDescriptor(entityDescriptor);
    queryComponentViewDescriptor.setName("queryEntity");
    queryComponentViewDescriptor
        .setLabelsPosition(IComponentViewDescriptor.ASIDE);
    queryComponentViewDescriptor.setColumnCount(2);

    List<String> queryProperties = new ArrayList<String>();
    for (String queryProperty : entityDescriptor.getQueryableProperties()) {
      IPropertyDescriptor propertyDescriptor = entityDescriptor
          .getPropertyDescriptor(queryProperty);
      if (propertyDescriptor.isQueryable()) {
        queryProperties.add(queryProperty);
      }
    }
    List<ISubViewDescriptor> queryPropertyViewDescriptors = new ArrayList<ISubViewDescriptor>();
    for (String renderedProperty : queryProperties) {
      BasicSubviewDescriptor propertyDescriptor = new BasicSubviewDescriptor();
      propertyDescriptor.setName(renderedProperty);
      queryPropertyViewDescriptors.add(propertyDescriptor);
    }
    queryComponentViewDescriptor
        .setPropertyViewDescriptors(queryPropertyViewDescriptors);

    return queryComponentViewDescriptor;
  }

  private IViewDescriptor createResultViewDescriptor(
      IComponentDescriptor<Object> entityDescriptor) {
    BasicTableViewDescriptor resultViewDescriptor = new BasicTableViewDescriptor();

    BasicCollectionDescriptor<Object> queriedEntitiesListDescriptor = new BasicCollectionDescriptor<Object>();
    queriedEntitiesListDescriptor
        .setCollectionInterface(List.class);
    queriedEntitiesListDescriptor.setElementDescriptor(entityDescriptor);

    BasicCollectionPropertyDescriptor<Object> queriedEntitiesDescriptor = new BasicCollectionPropertyDescriptor<Object>();
    queriedEntitiesDescriptor
        .setReferencedDescriptor(queriedEntitiesListDescriptor);
    queriedEntitiesDescriptor.setName(IQueryComponent.QUERIED_COMPONENTS);

    resultViewDescriptor.setName("queriedEntities.table");
    resultViewDescriptor.setModelDescriptor(queriedEntitiesDescriptor);
    resultViewDescriptor.setReadOnly(true);
    return resultViewDescriptor;
  }
}
