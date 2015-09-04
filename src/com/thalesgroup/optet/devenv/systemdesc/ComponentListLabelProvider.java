package com.thalesgroup.optet.devenv.systemdesc;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;



import com.thalesgroup.dtwc.Asset;
import com.thalesgroup.dtwc.Attribute;
import com.thalesgroup.dtwc.Component;
import com.thalesgroup.dtwc.Stakeholder;
import com.thalesgroup.optet.devenv.Activator;

public class ComponentListLabelProvider extends LabelProvider {

	public Image getImage(Object element) {
		System.out.println("---------> getimage");
		ImageDescriptor descriptor = null;
		if (element instanceof Component) {
			System.out.println("component");
			descriptor = Activator.getImageDescriptor("icons/icon_component.png");
		} else if (element instanceof Attribute) {
			System.out.println("Attribute");
			descriptor = Activator.getImageDescriptor("icons/icon_attribute.gif");
		}else if (element instanceof Stakeholder) {
			System.out.println("Stakeholder");
			descriptor = Activator.getImageDescriptor("icons/stakeholder_analysis_icon.jpg");
		}else if (element instanceof Asset) {
			System.out.println("asset");
			descriptor = Activator.getImageDescriptor("icons/icon_component.png");
		} 

		return descriptor.createImage();
	}

	public String getText(Object element) {
		String text = "";
		if (element instanceof Component) {
			Component comp = (Component) element;
			text = comp.getId();
		} else if (element instanceof Attribute) {
			Attribute attr = (Attribute) element;
			text = attr.getId();
		}else if (element instanceof Stakeholder) {
			Stakeholder stake = (Stakeholder) element;
			text = stake.getId();
		} else if (element instanceof Asset) {
			Asset asset = (Asset) element;
			text = asset.getId();
		} 

		System.out.println("--------->  getText" + text);
		return text ;
	}
}