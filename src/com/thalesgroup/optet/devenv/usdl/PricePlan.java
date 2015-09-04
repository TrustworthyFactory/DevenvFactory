/*
 *		OPTET Factory
 *
 *	Class PricePlan 1.0 7 août 2014
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.usdl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author F. Motte
 *
 */
public class PricePlan extends common{

	private String title;
	private String description;
	private Map<String,PriceComponent> componentList = new HashMap<String, PriceComponent>();
	
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Map<String, PriceComponent> getComponentMap() {
		return componentList;
	}
	public void setComponentMap(Map<String, PriceComponent> componentList) {
		this.componentList = componentList;
	}
	
	
	
}
