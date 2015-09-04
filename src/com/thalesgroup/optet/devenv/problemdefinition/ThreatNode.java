package com.thalesgroup.optet.devenv.problemdefinition;

import java.util.ArrayList;
import java.util.List;

class ThreatNode {
	private String ID;
	private String type;
	private List<ThreatNode> children = new ArrayList<ThreatNode>();
	private ThreatNode parent;

	public ThreatNode(String iD, String type) {
		super();
		ID = iD;
		this.type = type;
	}

	public List<ThreatNode> getChildren() {
		return children;
	}

	public void addChild(ThreatNode child) {
		children.add(child);
		child.setParent(this);
	}

	public ThreatNode getParent() {
		return parent;
	}

	public void setParent(ThreatNode parent) {
		this.parent = parent;
	}

	public String getData() {
		return ID;
	}

	@Override
	public String toString() {
		return ID;
	}
}
