package com.thalesgroup.optet.devenv.datamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Component extends Element {
	
	private Boolean inTOE = false;

	private Map<String, Element> children = new HashMap<String, Element>();

	
	public Component(String ID, String type, Boolean inTOE) {
		super(ID, type);
		//System.out.println("Create component " + ID + " " + type + " " + inTOE );
		this.inTOE = inTOE;
	}
	
	public  Set<String> getChildren(){
		return children.keySet();
	}
	public  Collection<Element> getChildrenValue(){
		return children.values();
	}
	
	public void addElement(Element element){
		//System.out.println("add element" + element.ID);
		children.put(element.ID, element);		
	}
	
	public void removeElement(String ID){
		//System.out.println("remove element " + ID);
		children.remove(ID);
	}
	
	public Element getElement(String ID){
		//System.out.println("get element" + ID);
		return children.get(ID);
	}
	
	public Boolean getInTOE() {
		return inTOE;
	}

	public void setInTOE(Boolean inTOE) {
		this.inTOE = inTOE;
	}
	
}