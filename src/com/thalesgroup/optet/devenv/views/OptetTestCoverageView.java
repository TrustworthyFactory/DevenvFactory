package com.thalesgroup.optet.devenv.views;


import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import com.thalesgroup.optet.common.views.OptetMetricModelProvider;
import com.thalesgroup.optet.common.views.OptetTestCoverageModel;
import com.thalesgroup.optet.common.views.OptetTestCoverageModelProvider;

/**
 * The test coverage view for optet
 * Contains all the test coverage entry find
 * 
 * @author F. Motte
 *
 */
public class OptetTestCoverageView extends ViewPart {
	// the tableViewer 
	private TableViewer viewer;

	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {

		createViewer(parent);
	}

	
	/**
	 * createViewer
	 * @param parent
	 */
	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, viewer);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ArrayContentProvider());
		// Get the content for the viewer, setInput will call getElements in the
		// contentProvider
		//viewer.setInput(OptetMetricModelProvider.INSTANCE.getAudit());
		viewer.setInput(OptetTestCoverageModelProvider.INSTANCE.getTestCoverage());
		// Make the selection available to other views
		getSite().setSelectionProvider(viewer);
		// Set the sorter for the table

		// Layout the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}

	public TableViewer getViewer() {
		return viewer;
	}

	// This will create the columns for the table
	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = {"Categories", "Total", "Covered", "Pourcentage"};
		int[] bounds = { 200, 200, 200, 200 };

		// First column is for the Categories
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				OptetTestCoverageModel p = (OptetTestCoverageModel) element;
				return p.getName();
			}
		});

		// Second column is for the total
		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				OptetTestCoverageModel p = (OptetTestCoverageModel) element;
				return String.valueOf(p.getTotal());
			}
		});

		// Now the cvered
		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				OptetTestCoverageModel p = (OptetTestCoverageModel) element;
				return String.valueOf(p.getCovered());
			}
		});

		// now the percentage
		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				OptetTestCoverageModel p = (OptetTestCoverageModel) element;
				return String.valueOf(p.getPourcentage());
			}
		});

	}
	
	
	/**
	 * create the column into the table
	 * 
	 * @param title the title of the column
	 * @param bound the size of the column
	 * @param colNumber the number of the column
	 * @return
	 */
	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
		viewer.setInput(OptetTestCoverageModelProvider.INSTANCE.getTestCoverage());
		viewer.refresh();
	}
	
	
	/**
	 * Refresh the TableView
	 */
	public void refresh() {
		viewer.setInput(OptetTestCoverageModelProvider.INSTANCE.getTestCoverage());
		viewer.refresh();
	}
} 