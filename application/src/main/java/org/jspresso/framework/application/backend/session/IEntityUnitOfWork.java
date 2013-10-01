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
package org.jspresso.framework.application.backend.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;

/**
 * This interface is implemented by unit of works on entities. A unit of work
 * is a structure which will generally synchronize with a transaction to keep
 * track of entity creations, updates, deletions and which is able to either
 * commit the changes or rollback them upon work completion. A unit of work is
 * reusable.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntityUnitOfWork extends IEntityLifecycleHandler {

  /**
   * Registers an entity as being updated.
   * 
   * @param entity
   *          the entity to register.
   */
  void addUpdatedEntity(IEntity entity);

  /**
   * Begins a new unit of work.
   */
  void begin();

  /**
   * Begins a nested unit of work.
   */
  void beginNested();

  /**
   * Begins a nested unit of work.
   */
  boolean hasNested();

  /**
   * Clears the dirty state of the entity in this unit of work.
   * 
   * @param flushedEntity
   *          the entity that was flushed and cleaned.
   */
  void clearDirtyState(IEntity flushedEntity);

  /**
   * Commits the unit of work. It should clear it state and be ready for another
   * work.
   */
  void commit();

  /**
   * Gets the entitiesRegisteredForDeletion.
   * 
   * @return the entitiesRegisteredForDeletion.
   */
  Collection<IEntity> getEntitiesRegisteredForDeletion();

  /**
   * Gets the entitiesRegisteredForUpdate.
   * 
   * @return the entitiesRegisteredForUpdate.
   */
  Collection<IEntity> getEntitiesRegisteredForUpdate();

  /**
   * Gets a map of entities already part of the unit of work. Entities are first
   * keyed by their contract, then their id.
   * 
   * @return a map of entities already part of the unit of work
   */
  Map<Class<? extends IEntity>, Map<Serializable, IEntity>> getRegisteredEntities();

  /**
   * Gets the entitiesToMergeBack.
   * 
   * @return the entitiesToMergeBack.
   */
  Collection<IEntity> getUpdatedEntities();

  /**
   * Tests whether this unit of work is currently in use.
   * 
   * @return true if the unit of work is active.
   */
  boolean isActive();

  /**
   * Tests whether the passed entity already updated in the current unit of work and waits
   * for commit.
   * 
   * @param entity
   *          the entity to test.
   * @return true if the passed entity already updated in the current unit of
   *         work and waits for commit.
   */
  boolean isUpdated(IEntity entity);

  /**
   * Registers an entity in the unit of work.
   * 
   * @param entity
   *          the entity to register.
   * @param initialChangedProperties
   *          the map of dirty properties the entity has before entering the
   *          unit of work along with their original values.
   */
  void register(IEntity entity, Map<String, Object> initialChangedProperties);

  /**
   * Rollbacks the unit of work. It should clear it state, restore the entity
   * states to the one before the unit of work beginning and be ready for another
   * work.
   */
  void rollback();

  /**
   * Clears the UOW.
   */
  void clear();

  /**
   * Gets a previously registered entity in the unit of work.
   * 
   * @param entityContract
   *          the entity contract.
   * @param entityId
   *          the identifier of the looked-up entity.
   * @return the registered entity or null.
   */
  IEntity getRegisteredEntity(Class<? extends IEntity> entityContract,
      Serializable entityId);
}
