package com.thalesgroup.optet.devenv;

import org.eclipse.ui.IActionFilter;

public class ArtifactToActionFilterAdapter implements IActionFilter {

	private static final Object MYOBJECT_TYPE = "objectType";

	private static ArtifactToActionFilterAdapter INSTANCE = new ArtifactToActionFilterAdapter();

	private ArtifactToActionFilterAdapter() {}

	@Override
	public boolean testAttribute(Object target, String name, String value) {
		
		
		System.out.println("*******************test attribute " + name + "   " + value);
//		if (target instanceof MyObject) {
//			MyObject obj = (MyObject) target;
//
//			if(MYOBJECT_TYPE.equals(name)) {
//				return value.equals(obj.getType());
//			}
//		}
return true;
		//return false;
	}

	public static ArtifactToActionFilterAdapter getInstance() {
		return INSTANCE;
	}
}