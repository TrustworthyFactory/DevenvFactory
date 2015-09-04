/*
 *		OPTET Factory
 *
 *	Class BusinessEntity 1.0 7 août 2014
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.usdl;

/**
 * @author F. Motte
 *
 */
public class BusinessEntity extends common{

	private String LegalName = "";
	private String LegalForm = "";
	private String description = "";
	private String url  ="http://nothing";
	public String getLegalName() {
		return LegalName;
	}
	public void setLegalName(String legalName) {
		LegalName = legalName;
	}
	public String getLegalForm() {
		return LegalForm;
	}
	public void setLegalForm(String legalForm) {
		LegalForm = legalForm;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
