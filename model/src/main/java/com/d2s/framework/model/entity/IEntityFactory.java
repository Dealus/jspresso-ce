/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.entity;

import java.io.Serializable;

import com.d2s.framework.model.component.IComponentFactory;
import com.d2s.framework.model.descriptor.IComponentDescriptorRegistry;

/**
 * This interface defines the contract of an entities factory.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntityFactory extends IComponentFactory, IComponentDescriptorRegistry {

  /**
   * Creates a new entity instance based on the entity descriptor. The entity
   * will be initialized with any necessary starting state.
   * 
   * @param <T>
   *            the concrete class of the created entity.
   * @param entityContract
   *            the class of the entity to create.
   * @return the entity instance.
   */
  <T extends IEntity> T createEntityInstance(Class<T> entityContract);

  /**
   * Creates a new entity instance based on the entity descriptor.
   * 
   * @param <T>
   *            the concrete class of the created entity.
   * @param entityContract
   *            the class of the entity to create.
   * @param id
   *            the identifier to set on the entity.
   * @return the entity instance.
   */
  <T extends IEntity> T createEntityInstance(Class<T> entityContract,
      Serializable id);

}
