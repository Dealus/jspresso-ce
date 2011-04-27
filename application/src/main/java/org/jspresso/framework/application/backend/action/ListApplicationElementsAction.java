/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action;

import java.util.Map;
import java.util.Set;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.application.security.ApplicationDirectoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * This is a special frontend action to list all application metamodel elements
 * available along with their permIds. This is particularly useful to set-up the
 * base of security referential setup.
 * 
 * @version $LastChangedRevision: 3701 $
 * @author Vincent Vandenschrick
 */
public class ListApplicationElementsAction extends BackendAction implements
    ApplicationContextAware {

  private ApplicationContext applicationContext;

  /**
   * cofigures the application context this action was instanciated from.
   * <p>
   * {@inheritDoc}
   */
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    ApplicationDirectoryBuilder directoryBuilder = new ApplicationDirectoryBuilder();
    @SuppressWarnings("unchecked") Map<String, Workspace> workspaces = applicationContext
        .getBeansOfType(Workspace.class);
    for (Workspace workspace : workspaces.values()) {
      directoryBuilder.process(workspace);
    }
    Map<String, Set<String>> permIdsStore = directoryBuilder
        .toApplicationDirectory();
    for (Map.Entry<String, Set<String>> storeEntry : permIdsStore.entrySet()) {
      System.out.println("\n\n********************************************");
      System.out.println("************ " + storeEntry.getKey()
          + " *****************");
      System.out.println("********************************************");
      for (String permId : storeEntry.getValue()) {
        System.out.println(permId);
      }
    }
    return super.execute(actionHandler, context);
  }

}