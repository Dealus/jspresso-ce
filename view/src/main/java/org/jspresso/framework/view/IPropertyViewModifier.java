package org.jspresso.framework.view;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.security.ISecurityHandler;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;

public interface IPropertyViewModifier {
	public void completePropertyView(IComponentDescriptor<?> sourceComponentDescriptor, IPropertyViewDescriptor propertyViewDescriptor, ISecurityHandler securityHandler);
}
