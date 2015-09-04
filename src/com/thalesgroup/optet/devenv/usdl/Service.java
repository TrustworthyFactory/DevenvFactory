/*
 *		OPTET Factory
 *
 *	Class Service 1.0 7 août 2014
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.usdl;

import java.util.Date;

/**
 * @author F. Motte
 *
 */
public class Service extends common{
	private Date modified;
	private Date created;
	private String title;
	private String description;
	private String abstractDes;
	private String url;
	private String certificateURL;
	private TechnicalDescription TechnicalDescription = new TechnicalDescription();
	private String hasClassification;
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		System.out.println("********************setcreated = " + created);
		this.created = created;
	}
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
	public String getAbstractDes() {
		return abstractDes;
	}
	public void setAbstractDes(String abstractDes) {
		this.abstractDes = abstractDes;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCertificateURL() {
		return certificateURL;
	}
	public void setCertificateURL(String certificateURL) {
		this.certificateURL = certificateURL;
	}
	public TechnicalDescription getTechnicalDescription() {
		return TechnicalDescription;
	}
	public void setTechnicalDescription(TechnicalDescription technicalDescription) {
		TechnicalDescription = technicalDescription;
	}
	public String getHasClassification() {
		return hasClassification;
	}
	public void setHasClassification(String hasClassification) {
		this.hasClassification = hasClassification;
	}
	
	
}
