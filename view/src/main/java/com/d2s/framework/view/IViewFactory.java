/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view;

import java.util.Locale;

import com.d2s.framework.view.action.IActionHandler;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * Factory for views.
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
 */
public interface IViewFactory<E, F> {

  /**
   * Creates a new view from a view descriptor.
   * 
   * @param viewDescriptor
   *          the view descriptor being the root of the view hierarchy to be
   *          constructed.
   * @param actionHandler
   *          the object responsible for executing the view actions (generally
   *          the frontend controller itself).
   * @param locale
   *          the locale the view must use for I18N.
   * @return the created view.
   */
  IView<E> createView(IViewDescriptor viewDescriptor,
      IActionHandler actionHandler, Locale locale);
  
  /**
   * Gets the icon factory.
   * 
   * @return the icon factory.
   */
  IIconFactory<F> getIconFactory();
}
