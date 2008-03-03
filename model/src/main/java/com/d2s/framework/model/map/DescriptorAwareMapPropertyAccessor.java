/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.map;

import java.lang.reflect.InvocationTargetException;

import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.model.descriptor.IModelDescriptorAware;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.util.accessor.map.MapPropertyAccessor;

/**
 * A map property accessor that receives a model descriptor to handle the model
 * integrity.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DescriptorAwareMapPropertyAccessor extends MapPropertyAccessor
    implements IModelDescriptorAware {

  private IModelDescriptor modelDescriptor;

  /**
   * Constructs a new <code>DescriptorAwareMapPropertyAccessor</code>
   * instance.
   * 
   * @param property
   *            the property to create the accessor for.
   */
  public DescriptorAwareMapPropertyAccessor(String property) {
    super(property);
  }

  /**
   * {@inheritDoc}
   */
  public void setModelDescriptor(IModelDescriptor modelDescriptor) {
    this.modelDescriptor = modelDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    Object oldValue = getValue(target);
    if (getModelDescriptor() != null) {
      getModelDescriptor().preprocessSetter(target, oldValue, value);
    }
    super.setValue(target, value);
    if (getModelDescriptor() != null) {
      getModelDescriptor().postprocessSetter(target, oldValue, value);
    }
  }

  /**
   * Gets the modelDescriptor.
   * 
   * @return the modelDescriptor.
   */
  protected IPropertyDescriptor getModelDescriptor() {
    return (IPropertyDescriptor) modelDescriptor;
  }
}
