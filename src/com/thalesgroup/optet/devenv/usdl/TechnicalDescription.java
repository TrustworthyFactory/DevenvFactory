/*
 *		OPTET Factory
 *
 *	Class TechnicalDescription 1.0 7 août 2014
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
public class TechnicalDescription extends common{

	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getExecutionPlatform() {
		return ExecutionPlatform;
	}
	public void setExecutionPlatform(String executionPlatform) {
		ExecutionPlatform = executionPlatform;
	}
	public String getProgrammingLanguage() {
		return ProgrammingLanguage;
	}
	public void setProgrammingLanguage(String programmingLanguage) {
		ProgrammingLanguage = programmingLanguage;
	}
	public String getSoftwareDependency() {
		return SoftwareDependency;
	}
	public void setSoftwareDependency(String softwareDependency) {
		SoftwareDependency = softwareDependency;
	}
	public String getHardwareRequirement() {
		return HardwareRequirement;
	}
	public void setHardwareRequirement(String hardwareRequirement) {
		HardwareRequirement = hardwareRequirement;
	}
	public String getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	private String license;
	private String ExecutionPlatform;
	private String ProgrammingLanguage;
	private String SoftwareDependency;
	private String HardwareRequirement;
	private String versionNumber;
	
	
	
}
