/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view.descriptor.basic;

import java.util.List;

import org.jspresso.framework.model.descriptor.ICollectionDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;

/**
 * This view descriptor serves 2 purposes :
 * <ul>
 * <li>configure complex, component based views : refine <i>columns</i> of table
 * views and <i>fields</i> of component (form) views.</li>
 * <li>display a single property as an autonomous view, i.e. not as a table
 * column or a form field.</li>
 * </ul>
 * The second usage might be a little bit unusual, but here is a use-case
 * scenario : display a text area which maps a text property that contains XML
 * content. This text area must be displayed in a split pane and provide actions
 * to interact directly with the FS (save content to a file, load content from a
 * file, ...). In that case, defining a property view alone on the text property
 * of the owning component might be a good solution.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicPropertyViewDescriptor extends BasicViewDescriptor implements
    IPropertyViewDescriptor {

  private List<String> renderedChildProperties;
  private Integer      width;
  private String       labelBackground;
  private String       labelFont;
  private String       labelForeground;

  /**
   * {@inheritDoc}
   */
  public List<String> getRenderedChildProperties() {
    if (renderedChildProperties != null) {
      return renderedChildProperties;
    }
    // IPropertyDescriptor childPropertyDescriptor =
    // ((IComponentDescriptorProvider<?>) getModelDescriptor())
    // .getComponentDescriptor().getPropertyDescriptor(getName());
    IModelDescriptor propertyDescriptor = getModelDescriptor();
    if (propertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
      return ((ICollectionDescriptor<?>) ((ICollectionPropertyDescriptor<?>) propertyDescriptor)
          .getCollectionDescriptor()).getElementDescriptor()
          .getRenderedProperties();
    } else if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
      return ((IReferencePropertyDescriptor<?>) propertyDescriptor)
          .getReferencedDescriptor().getRenderedProperties();
    }
    return null;
  }

  /**
   * Whenever the property decriptor backing the view is not scalar, this
   * property allows to override which of the referenced component fields should
   * be displayed :
   * <ul>
   * <li>as columns when the rendered property is a collection property</li>
   * <li>as fields when the rendered property is a reference property</li>
   * </ul>
   * The property must be configured with a <code>List</code> containing the
   * property names to render for the child element(s).
   * <p>
   * A <code>null</code> value (default), means that the non-scalar property
   * will be rendered using default rendered properties as specified in its
   * referenced model descriptor.
   * <p>
   * Please note that this is quite unusual to embed non-scalar properties
   * directly in a property view. Although permitted, you won't have as much
   * flexibility in the content layouting as you would have when using composite
   * views; so the latter is by far recommended.
   * 
   * @param renderedChildProperties
   *          the renderedChildProperties to set.
   */
  public void setRenderedChildProperties(List<String> renderedChildProperties) {
    this.renderedChildProperties = renderedChildProperties;
  }

  /**
   * Gets the width.
   * 
   * @return the width.
   */
  public int getWidth() {
    if (width != null) {
      return width.intValue();
    }
    return 1;
  }

  /**
   * When the property has to be displayed in a grid-like layout (e.g. in a
   * component view), this property defines the umber of grid columns the
   * corresponding UI component will span.
   * <p>
   * Default value is <code>null</code>, meaning use default span of 1.
   * 
   * @param width
   *          the width to set.
   */
  public void setWidth(Integer width) {
    this.width = width;
  }

  /**
   * Gets the labelBackground.
   * 
   * @return the labelBackground.
   */
  public String getLabelBackground() {
    return labelBackground;
  }

  /**
   * When the property has to be labelled (e.g. in a component view), this
   * property defines the background color of the corresponding label. It might
   * differ from the field component one. The color must be defined using its
   * string hexadecimal representation (<i>0xrgba</i> encoded).
   * <p>
   * Default value is <code>null</code>, meaning use UI default.
   * 
   * @param labelBackground
   *          the labelBackground to set.
   */
  public void setLabelBackground(String labelBackground) {
    this.labelBackground = labelBackground;
  }

  /**
   * Gets the labelFont.
   * 
   * @return the labelFont.
   */
  public String getLabelFont() {
    return labelFont;
  }

  /**
   * When the property has to be labelled (e.g. in a component view), this
   * property defines the font of the corresponding label. It might differ from
   * the field component one. The font must be string encoded using the pattern
   * <b>&quot;[name];[style];[size]&quot;</b> :
   * <ul>
   * <li><b>[name]</b> is the name of the font, e.g. <i>arial</i>.</li>
   * <li><b>[style]</b> is PLAIN, BOLD, ITALIC or a union of BOLD and ITALIC
   * combined with the '|' character, e.g. <i>BOLD|ITALIC</i>.</li>
   * <li><b>[size]</b> is the size of the font, e.g. <i>10</i>.</li>
   * </ul>
   * Any of the above pattern section can be left empty, thus falling back to
   * the component default.
   * <p>
   * Default value is <code>null</code>, meaning use default component font.
   * 
   * @param labelFont
   *          the labelFont to set.
   */
  public void setLabelFont(String labelFont) {
    this.labelFont = labelFont;
  }

  /**
   * Gets the labelForeground.
   * 
   * @return the labelForeground.
   */
  public String getLabelForeground() {
    return labelForeground;
  }

  /**
   * When the property has to be labelled (e.g. in a component view), this
   * property defines the foregroud color of the corresponding label. It might
   * differ from the field component one. The color must be defined using its
   * string hexadecimal representation (<i>0xrgba</i> encoded).
   * <p>
   * Default value is <code>null</code>, meaning use UI default.
   * 
   * @param labelForeground
   *          the labelForeground to set.
   */
  public void setLabelForeground(String labelForeground) {
    this.labelForeground = labelForeground;
  }
}
