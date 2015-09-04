/*
 *		OPTET Factory
 *
 *	Class TWPropertySpecification 1.0 26 nov. 2013
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
public class TWPropertySpecification {

	private String twPropertyName;
	
	private String controlName;

	public TWPropertySpecification(String twPropertyName, String controlName) {
		super();
		this.twPropertyName = twPropertyName;
		this.controlName = controlName;
	}

	public String getName(){
		return twPropertyName + "-" + controlName;
	}
	
	public String getTwPropertyName() {
		return twPropertyName;
	}

	public void setTwPropertyName(String twPropertyName) {
		this.twPropertyName = twPropertyName;
	}

	public String getControlName() {
		return controlName;
	}

	public void setControlName(String controlName) {
		this.controlName = controlName;
	}
	
	
}
