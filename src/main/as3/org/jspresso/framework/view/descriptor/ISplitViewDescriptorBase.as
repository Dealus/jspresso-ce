/**
 * Generated by Gas3 v1.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERRIDDEN EACH TIME YOU USE
 * THE GENERATOR. CHANGE INSTEAD THE INHERITED INTERFACE (ISplitViewDescriptor.as).
 */

package org.jspresso.framework.view.descriptor {

    public interface ISplitViewDescriptorBase extends ICompositeViewDescriptor {

        function get leftTopViewDescriptor():IViewDescriptor;

        function get orientation():EOrientation;

        function get rightBottomViewDescriptor():IViewDescriptor;
    }
}