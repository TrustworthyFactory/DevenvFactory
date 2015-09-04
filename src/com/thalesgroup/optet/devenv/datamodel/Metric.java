/*
 *		OPTET Factory
 *
 *	Class Metric 1.0 28 oct. 2013
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
public class Metric {

	private String name;
	private String category;
	private String value;
	private String method;
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public Metric(String name, String category) {
		super();
		this.name = name;
		this.category = category;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		System.out.println("SetValue  " + name + " " + value);
		this.value = value;
	}


	public String getMethod() {
		return method;
	}


	public void setMethod(String method) {
		System.out.println("setMethod  " + name + " " + method);

		this.method = method;
	}
	
	
	
	
}
