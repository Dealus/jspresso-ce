/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.model.descriptor;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicIntegerPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicInterfaceDescriptor;
import org.jspresso.framework.util.collection.IPageable;

/**
 * PageableDescriptor singleton.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class PageableDescriptor {

  /**
   * The descriptor of the pageable interface.
   */
  public static final IComponentDescriptor<IPageable> INSTANCE = createInstance();

  private PageableDescriptor() {
    super();
    // Helper constructor
  }

  /**
   * Creates the singleton instance.
   * 
   * @return the singleton instance.
   */
  private static IComponentDescriptor<IPageable> createInstance() {
    BasicInterfaceDescriptor<IPageable> descriptor = new BasicInterfaceDescriptor<IPageable>(
        IPageable.class.getName());

    BasicIntegerPropertyDescriptor pageDesc = new BasicIntegerPropertyDescriptor();
    pageDesc.setName(IPageable.PAGE);
    pageDesc.setReadOnly(true);

    BasicIntegerPropertyDescriptor displayPageIndexDesc = new BasicIntegerPropertyDescriptor();
    displayPageIndexDesc.setName(IPageable.DISPLAY_PAGE_INDEX);

    BasicIntegerPropertyDescriptor pageCountDesc = new BasicIntegerPropertyDescriptor();
    pageCountDesc.setName(IPageable.PAGE_COUNT);
    pageCountDesc.setReadOnly(true);

    BasicIntegerPropertyDescriptor recordCountDesc = new BasicIntegerPropertyDescriptor();
    recordCountDesc.setName(IPageable.RECORD_COUNT);
    recordCountDesc.setReadOnly(true);

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();
    propertyDescriptors.add(pageDesc);
    propertyDescriptors.add(displayPageIndexDesc);
    propertyDescriptors.add(pageCountDesc);
    propertyDescriptors.add(recordCountDesc);

    descriptor.setPropertyDescriptors(propertyDescriptors);

    return descriptor;
  }

}
