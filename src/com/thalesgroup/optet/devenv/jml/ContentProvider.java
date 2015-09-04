package com.thalesgroup.optet.devenv.jml;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
 
class ContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return ((ThreatNode) parentElement).getChildren().toArray();
	}

	@Override
	public Object getParent(Object element) {
		return ((ThreatNode) element).getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		return !((ThreatNode) element).getChildren().isEmpty();
	}

}
