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
package org.jspresso.framework.application.backend.persistence;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * Fake transaction manager to use when the application is MongoDB only.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class FakeTransactionManager extends AbstractPlatformTransactionManager {
  private static final long serialVersionUID = 1L;

  /**
   * Fake return new object.
   *
   * @return the object
   */
  @Override
  protected Object doGetTransaction() {
    return new Object();
  }

  /**
   * Fake begin.
   *
   * @param transaction the transaction
   * @param definition the definition
   */
  @Override
  protected void doBegin(Object transaction, TransactionDefinition definition) {
    // NO-OP
  }

  /**
   * Fake commit.
   *
   * @param status the status
   */
  @Override
  protected void doCommit(DefaultTransactionStatus status) {
    // NO-OP
  }

  /**
   * Fake rollback.
   *
   * @param status the status
   */
  @Override
  protected void doRollback(DefaultTransactionStatus status) {
    // NO-OP
  }
}
