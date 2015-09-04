/*
 *		OPTET Factory
 *
 *	Class Threads 1.0 28 oct. 2013
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
public class Threat {
	private String Id;
	private String Type;
	private String Category;
	
	
	public Threat(String id, String type, String category) {
		super();
		Id = id;
		Type = type;
		setCategory(category);
	}


	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}

	
}
