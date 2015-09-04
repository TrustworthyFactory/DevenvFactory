package com.thalesgroup.optet.devenv.evidencesview;

import java.util.ArrayList;
import java.util.List;

class EvidenceNode {
	private String ID;

	private String name;
	private List<EvidenceNode> children = new ArrayList<EvidenceNode>();
	private EvidenceNode parent;

	public EvidenceNode(String iD, String name) {
		super();
		ID = iD;
		this.name = name;
	}

	public List<EvidenceNode> getChildren() {
		return children;
	}

	public void addChild(EvidenceNode child) {
		children.add(child);
		child.setParent(this);
	}

	public EvidenceNode getParent() {
		return parent;
	}

	public void setParent(EvidenceNode parent) {
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
