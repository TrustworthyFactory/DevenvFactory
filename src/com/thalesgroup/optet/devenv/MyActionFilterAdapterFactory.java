package com.thalesgroup.optet.devenv;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.IActionFilter;

public class MyActionFilterAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
	System.out.println("*************************** getAdapter" + adapterType.getCanonicalName());
		if(adapterType == IActionFilter.class)
			return ArtifactToActionFilterAdapter.getInstance();
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class[] getAdapterList() {
		System.out.println("*************************** getAdapterList");
		return new Class[] {IActionFilter.class};
	}
}