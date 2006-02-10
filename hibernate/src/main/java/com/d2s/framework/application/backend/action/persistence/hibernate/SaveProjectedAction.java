/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.util.Map;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.view.action.IActionHandler;
import com.d2s.framework.view.projection.BeanModule;

/**
 * Saves the projected object(s) in a transaction.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SaveProjectedAction extends AbstractHibernateAction {

  /**
   * Saves the projected object(s) in a transaction.
   * <p>
   * {@inheritDoc}
   */
  public Map<String, Object> execute(@SuppressWarnings("unused")
  IActionHandler actionHandler) {
    getTransactionTemplate().execute(new TransactionCallback() {

      public Object doInTransaction(@SuppressWarnings("unused")
      TransactionStatus status) {
        ICompositeValueConnector projectionConnector = getProjectionConnector();
        BeanModule projection = (BeanModule) projectionConnector
            .getConnectorValue();
        if (projection.getModuleObjects() != null) {
          for (Object entity : projection.getModuleObjects()) {
            saveEntity((IEntity) entity);
          }
        } else if (projection.getModuleObject() != null) {
          saveEntity((IEntity) projection.getModuleObject());
        }
        return null;
      }
    });
    return null;
  }

  /**
   * Saves an entity in hibernate.
   * 
   * @param entity
   *          the entity to save.
   */
  private void saveEntity(final IEntity entity) {
    getHibernateTemplate().execute(new HibernateCallback() {

      public Object doInHibernate(Session session) {
        IEntity mergedEntity = mergeInHibernate(entity, session);
        session.saveOrUpdate(mergedEntity);
        return null;
      }
    });
  }
}
