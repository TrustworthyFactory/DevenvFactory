package com.thalesgroup.optet.devenv.datamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Element implements Cloneable{

	public String ID;

	public Component parent = null;
	
	private String type;

	
	private Collection< Threat> threads;
	
	public Collection< Threat> getThreads() {
		return threads;
	}

	public void setThreads(Collection< Threat> threads) {
		this.threads = threads;
	}

	public Element(String iD, String type) {
		super();
		threads = new ArrayList<Threat>();
		this.ID = iD;
		this.type = type;

	}

	public String getID() {
		return ID;
	}

	public String toString() {
		return ID;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	
	
}