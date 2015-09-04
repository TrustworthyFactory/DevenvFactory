/*
 *		OPTET Factory
 *
 *	Class Control 1.0 26 nov. 2013
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
public class Control {

	private String name;
	private String type;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Control(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}
	
	
}
