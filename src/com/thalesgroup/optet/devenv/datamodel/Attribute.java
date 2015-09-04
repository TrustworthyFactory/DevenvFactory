package com.thalesgroup.optet.devenv.datamodel;

public class Attribute   extends Element {

	
	private Boolean inTOE = false;
	
	/**
	 * @param iD
	 * @param type
	 * @param inTOE
	 */
	public Attribute(String iD, String type, Boolean inTOE) {
		super(iD, type);
		this.inTOE = inTOE;
	}

	public Boolean getInTOE() {
		return inTOE;
	}

	public void setInTOE(Boolean inTOE) {
		this.inTOE = inTOE;
	}
	
}
