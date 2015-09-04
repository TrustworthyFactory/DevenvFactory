package com.thalesgroup.optet.devenv.systemdesc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.atlas.lib.ArrayUtils;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.internal.ole.win32.COMObject;

import com.thalesgroup.dtwc.Component;
import com.thalesgroup.dtwc.DTWC;
import com.thalesgroup.dtwc.impl.CertificateWrapper;
import com.thalesgroup.dtwc.impl.ComponentImpl;




public class ComponentTreeContentProvider
extends ArrayContentProvider
implements ITreeContentProvider {

	private DTWC dtwc;

	/**
	 * @param dtwc
	 */
	public ComponentTreeContentProvider(DTWC dtwc) {
		// TODO Auto-generated constructor stub
		this.dtwc = dtwc;
	}

	public Object[] getChildren(Object parentElement) {
		Component component = (Component) parentElement;
		System.out.println("getChildren : " + component.getId());
		
		List<Object> allList = new ArrayList<Object>();
		allList.addAll(component.getStakeholderList());
		allList.addAll(component.getComponentModel().getAttributeList());
		allList.addAll(component.getComponentModel().getComponentList());
		return allList.toArray();
	}

	public Object getParent(Object element) {
		Component component = (Component) element;
		System.out.println("getParent : " + component.getId());
		return getParent(component);
	}

	public boolean hasChildren(Object element) {
		int length = 0;
		if (element instanceof Component) {
			Component component = (Component) element;
			length = component.getComponentModel().getComponentList().size();
			length = length + component.getComponentModel().getAttributeList().size();
			length = length + component.getStakeholderList().size();
		}

		return length > 0;
	}

	private Component getParent(Component comp){
		Component parent = null;
		
		Collection<ComponentImpl> compList = dtwc.getSystemDescription().getComponentList();
		
		for (Iterator iterator = compList.iterator(); iterator.hasNext();) {
			ComponentImpl componentImpl = (ComponentImpl) iterator.next();
			parent = findComp(comp, componentImpl);
			if (parent!=null)
				break;
		}		
		return parent;		
	}
	
	private Component findComp(Component wantedComp, Component comp){
		Component parent = null;
		
		Collection<ComponentImpl> compList = comp.getComponentModel().getComponentList();
		
		for (Iterator iterator = compList.iterator(); iterator.hasNext();) {
			ComponentImpl componentImpl = (ComponentImpl) iterator.next();
			if (componentImpl.getId().equals(wantedComp.getId())){
				parent = componentImpl;
				break;
			} else {
				parent = findComp(wantedComp, componentImpl);
				if (parent != null){
					break;
				}
			}
		}
		
		
		return parent;		
	}
	
	
}