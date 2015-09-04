/*
 *		OPTET Factory
 *
 *	Class ServiceOffering 1.0 7 août 2014
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.usdl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author F. Motte
 *
 */
public class ServiceOffering extends common{

	private Date validFrom;
	private Date validThrough;
	private String description;
	private String title;
	private Map<String, PricePlan> pricePlanList = new HashMap<String, PricePlan>();
	public Date getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}
	public Date getValidThrough() {
		return validThrough;
	}
	public void setValidThrough(Date validThrough) {
		this.validThrough = validThrough;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Map<String, PricePlan> getPricePlanMap() {
		return pricePlanList;
	}
	public void setPricePlanMap(Map<String, PricePlan> pricePlanList) {
		this.pricePlanList = pricePlanList;
	}
}
