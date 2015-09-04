/*
 *		OPTET Factory
 *
 *	Class TWProperty 1.0 26 nov. 2013
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.datamodel;

/**
 * @author F. Motte
 *
 */
public class TWProperty {

	private String name;
	private String assetID;
	private String metricID;
	private String TWAttribute;
	public String getAssetID() {
		return assetID;
	}
	public void setAssetID(String assetID) {
		this.assetID = assetID;
	}
	public String getMetricID() {
		return metricID;
	}
	public void setMetricID(String metricID) {
		this.metricID = metricID;
	}
	public String getTWAttribute() {
		return TWAttribute;
	}
	public void setTWAttribute(String tWAttribute) {
		TWAttribute = tWAttribute;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TWProperty(String name, String assetID, String metricID,
			String tWAttribute) {
		super();
		this.name = name;
		this.assetID = assetID;
		this.metricID = metricID;
		TWAttribute = tWAttribute;
	}
	
	
}
