package com.thalesgroup.optet.devenv.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.thalesgroup.optet.common.views.OptetAuditConst;
import com.thalesgroup.optet.common.views.OptetAuditModel;

/**
 * OptetAuditViewSorter the OptetAuditViewSorter permet to sort tha table view in order to
 * classify the audit following different criteria like tool, filename, message, etc...
 * 
 * @author F. Motte
 *
 */
public class OptetAuditViewSorter extends ViewerSorter {
	// indicate if the research is ascending or descending
	private static final int ASCENDING = 0;

	private static final int DESCENDING = 1;

	// the column used to realise the sort
	private int column;

	// direction of the sort (ascending or descending)
	private int direction;

	
	/**
	 * Realize the sort 
	 * @param column the column for the sort
	 */
	public void doSort(int column) {
		if (column == this.column) {
			direction = 1 - direction;
		} else {
			this.column = column;
			direction = ASCENDING;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public int compare(Viewer viewer, Object e1, Object e2) {
		int rc = 0;
		OptetAuditModel p1 = (OptetAuditModel) e1;
		OptetAuditModel p2 = (OptetAuditModel) e2;

		switch (column) {
		case OptetAuditConst.COLUMN_TOOL:
			rc = p1.getTool().compareTo(p2.getTool());
			break;
		case OptetAuditConst.COLUMN_FILENAME:
			rc = p1.getFile().getFullPath().toOSString().compareTo(p2.getFile().getFullPath().toOSString());
			break;
		case OptetAuditConst.COLUMN_LINE:
			rc = p1.getLine() > p2.getLine() ? 1 : -1;
			break;
		case OptetAuditConst.COLUMN_SEVERITY:
			rc = p1.getSeverity().compareTo(p2.getSeverity());
			break;
		case OptetAuditConst.COLUMN_RULESET:
			rc = p1.getRuleset().compareTo(p2.getRuleset());
			break;
		case OptetAuditConst.COLUMN_MESSAGE:
			rc = p1.getMessage().compareTo(p2.getMessage());
			break;
		case OptetAuditConst.COLUMN_RECO:
			rc = p1.getRecommandation().compareTo(p2.getRecommandation());
			break;
		}
		if (direction == DESCENDING)
			rc = -rc;
		return rc;
	}
}
