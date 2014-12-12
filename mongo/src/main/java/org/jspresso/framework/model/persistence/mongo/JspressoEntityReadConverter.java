/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.persistence.mongo;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;

/**
 * Custom converter for Jspresso entities.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class JspressoEntityReadConverter
    implements ConditionalGenericConverter, ApplicationListener<ContextRefreshedEvent> {

  private IEntityFactory                entityFactory;
  private IComponentCollectionFactory   collectionFactory;
  private MongoTemplate                 mongo;
  private JspressoMappingMongoConverter converter;

  /**
   * Convert object.
   *
   * @param source      the generic source
   * @param sourceType      the source type
   * @param targetType      the target type
   * @return the object
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
    return convertEntity((DBObject) source, (Class<? extends IEntity>) targetType.getType());
  }

  @SuppressWarnings("unchecked")
  private IEntity convertEntity(DBObject source, Class<? extends IEntity> entityType) {
    Serializable id = (Serializable) source.get("_id");
    IComponentDescriptor<? extends IEntity> entityDescriptor = (IComponentDescriptor<? extends IEntity>)
        getEntityFactory()
        .getComponentDescriptor(entityType);
    IEntity entity = getBackendController().getUnitOfWorkOrRegisteredEntity(entityType, id);
    if (entity == null) {
      entity = getEntityFactory().createEntityInstance(entityType, id);
      completeComponent(source, entityDescriptor, entity);
    }
    return entity;
  }


  @SuppressWarnings("unchecked")
  private IEntity convertEntity(Serializable id, Class<IEntity> entityType) {
    IEntity entity = getBackendController().getUnitOfWorkOrRegisteredEntity(entityType, id);
    if (entity == null) {
      entity = createProxyEntity(id, entityType);
    }
    return entity;
  }

  @SuppressWarnings("unchecked")
  private Object convertComponent(DBObject source, Class<? extends IComponent> componentType) {
    IComponentDescriptor<? extends IComponent> componentDescriptor = (IComponentDescriptor<? extends IComponent>)
        getEntityFactory()
        .getComponentDescriptor(componentType);
    IComponent component = getEntityFactory().createComponentInstance(componentType);
    completeComponent(source, componentDescriptor, component);
    return component;
  }

  @SuppressWarnings("unchecked")
  private void completeComponent(DBObject source, IComponentDescriptor<? extends IComponent> entityDescriptor,
                                 IComponent component) {
    for (IPropertyDescriptor propertyDescriptor : entityDescriptor.getPropertyDescriptors()) {
      if (propertyDescriptor != null && !propertyDescriptor.isComputed()) {
        String propertyName = propertyDescriptor.getName();
        if (source.containsField(propertyName)) {
          Object propertyValue = source.get(propertyName);
          Class<?> targetType = null;
          if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
            if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
              targetType = ((IReferencePropertyDescriptor<?>) propertyDescriptor).getReferencedDescriptor()
                                                                                 .getModelType();
            } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
              targetType = ((ICollectionPropertyDescriptor<?>) propertyDescriptor).getCollectionDescriptor()
                                                                                  .getElementDescriptor()
                                                                                  .getModelType();
            }
          }
          if (propertyValue instanceof DBObject) {
            if (propertyValue instanceof BasicDBList) {
              if (propertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
                Class<? extends Collection<?>> collectionInterface = ((ICollectionPropertyDescriptor)
                    propertyDescriptor)
                    .getCollectionDescriptor().getCollectionInterface();
                if (IComponent.class.isAssignableFrom(targetType)) {
                  if (IEntity.class.isAssignableFrom(targetType)) {
                    Collection<Serializable> collectionProperty = getCollectionFactory().createComponentCollection(
                        collectionInterface);
                    for (Object element : (BasicDBList) propertyValue) {
                      collectionProperty.add((Serializable) element);
                    }
                    component.straightSetProperty(propertyName, createProxyCollection(collectionProperty,
                        (Class<IEntity>) targetType, collectionInterface));
                  } else {
                    Collection<Object> collectionProperty = getCollectionFactory().createComponentCollection(
                        collectionInterface);
                    for (Object element : (BasicDBList) propertyValue) {
                      if (element instanceof DBObject) {
                        collectionProperty.add(convertComponent((DBObject) element,
                            (Class<? extends IComponent>) targetType));
                      }
                    }
                    component.straightSetProperty(propertyName, collectionProperty);
                  }
                } else {
                  Collection<Object> collectionProperty = getCollectionFactory().createComponentCollection(
                      collectionInterface);
                  for (Object element : (BasicDBList) propertyValue) {
                    collectionProperty.add(element);
                  }
                  component.straightSetProperty(propertyName, collectionProperty);
                }
              } else {
                component.straightSetProperty(propertyName, propertyValue);
              }
            } else if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
              component.straightSetProperty(propertyName, convertComponent((DBObject) propertyValue,
                  (Class<? extends IComponent>) targetType));
            } else {
              Object convertedPropertyValue = getConverter().read(propertyDescriptor.getModelType(),
                  (DBObject) propertyValue);
              component.straightSetProperty(propertyName, convertedPropertyValue);
            }
          } else if (targetType != null && propertyValue instanceof Serializable) {
            component.straightSetProperty(propertyName, convertEntity((Serializable) propertyValue,
                (Class<IEntity>) targetType));
          } else {
            component.straightSetProperty(propertyName, propertyValue);
          }
        }
      }
    }
  }


  /**
   * Gets entity factory.
   *
   * @return the entity factory
   */
  protected IEntityFactory getEntityFactory() {
    return entityFactory;
  }

  /**
   * Sets entity factory.
   *
   * @param entityFactory      the entity factory
   */
  public void setEntityFactory(IEntityFactory entityFactory) {
    this.entityFactory = entityFactory;
  }

  /**
   * Matches boolean.
   *
   * @param sourceType      the source type
   * @param targetType      the target type
   * @return the boolean
   */
  @Override
  public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
    return DBObject.class.isAssignableFrom(sourceType.getType()) && IEntity.class.isAssignableFrom(
        targetType.getType());
  }

  /**
   * Gets convertible types.
   *
   * @return the convertible types
   */
  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return Collections.singleton(new ConvertiblePair(DBObject.class, IEntity.class));
  }

  /**
   * Gets collection factory.
   *
   * @return the collection factory
   */
  protected IComponentCollectionFactory getCollectionFactory() {
    return collectionFactory;
  }

  /**
   * Sets collection factory.
   *
   * @param collectionFactory      the collection factory
   */
  public void setCollectionFactory(IComponentCollectionFactory collectionFactory) {
    this.collectionFactory = collectionFactory;
  }

  /**
   * Gets mongo.
   *
   * @return the mongo
   */
  protected MongoTemplate getMongo() {
    return mongo;
  }

  /**
   * Sets mongo.
   *
   * @param mongo      the mongo
   */
  public void setMongo(MongoTemplate mongo) {
    this.mongo = mongo;
  }

  /**
   * On application event.
   *
   * @param event the event
   */
  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    setMongo(event.getApplicationContext().getBean("mongoTemplate", MongoTemplate.class));
  }

  private IEntity createProxyEntity(Serializable id, Class<IEntity> entityContract) {
    return (IEntity) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
        new Class[]{entityContract, JspressoMongoEntityProxy.class}, new JspressoMongoEntityProxyHandler(id,
            entityContract, getMongo()));
  }

  private Object createProxyCollection(Collection<Serializable> ids, Class<IEntity> entityContract,
                                       Class<? extends Collection<?>> collectionContract) {
    InvocationHandler handler;
    if (List.class.isAssignableFrom(collectionContract)) {
      handler = new JspressoMongoEntityListInvocationHandler(ids, entityContract, getMongo());
    } else {
      handler = new JspressoMongoEntitySetInvocationHandler(ids, entityContract, getMongo());
    }
    return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
        new Class[]{collectionContract, JspressoMongoProxy.class}, handler);
  }

  /**
   * Gets the getBackendController().
   *
   * @return the backendController.
   */
  protected IBackendController getBackendController() {
    return BackendControllerHolder.getCurrentBackendController();
  }

  /**
   * Gets converter.
   *
   * @return the converter
   */
  protected JspressoMappingMongoConverter getConverter() {
    return converter;
  }

  /**
   * Sets converter.
   *
   * @param converter the converter
   */
  public void setConverter(JspressoMappingMongoConverter converter) {
    this.converter = converter;
  }
}
