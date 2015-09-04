package com.thalesgroup.optet.devenv.views;


import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.ITextEditor;

import com.thalesgroup.optet.common.views.OptetAuditModel;
import com.thalesgroup.optet.common.views.OptetAuditModelProvider;



/**
 * The audit view for optet
 * Contains all the audit entry find by all the audit tools
 * 
 * @author F. Motte
 *
 */
public class OptetAuditView extends ViewPart {

	// the tableViewer 
	private TableViewer viewer;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		createViewer(parent);
		
		// add a new listener to open file into the text editor
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection sel = (IStructuredSelection) event.getSelection();
				// get the audit element
				OptetAuditModel audit = (OptetAuditModel) sel.getFirstElement();
				if(audit != null){
					// get the file of the audit
					IFile fileToBeOpened = audit.getFile();
					IEditorInput editorInput = new FileEditorInput(fileToBeOpened);
					IWorkbenchWindow window=PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					IWorkbenchPage page = window.getActivePage();
					// open the file into the text editor
					try {
						IEditorPart openEditor = page.openEditor(editorInput, "org.eclipse.ui.DefaultTextEditor");
						if (openEditor instanceof ITextEditor) {
							ITextEditor textEditor = (ITextEditor) openEditor ;
							IDocument document= textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
							textEditor.selectAndReveal(document.getLineOffset(audit.getLine() - 1), document.getLineLength(audit.getLine()-1));
						}

					} catch (PartInitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}



	/**
	 * Create the table view 
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
		viewer.setInput(OptetAuditModelProvider.INSTANCE.getAudit());

		// Make the selection available to other views
		getSite().setSelectionProvider(viewer);
		// Set the sorter for the table
		viewer.setSorter(new OptetAuditViewSorter());
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
		String[] titles = {"Tool", "Filename", "Line", "Severity", "Ruleset", "Message", "Recommandation" };
		int[] bounds = { 50, 400, 100, 100, 100, 400, 400 };

		// First column is for the tool name
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				OptetAuditModel p = (OptetAuditModel) element;
				return p.getTool();
			}
		}); 		

		// Second column is for the filename
		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				OptetAuditModel p = (OptetAuditModel) element;
				return p.getFile().getLocation().toOSString();
			}
		});

		// Now the line
		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				OptetAuditModel p = (OptetAuditModel) element;
				return String.valueOf(p.getLine());
			}
		});

		// Now the severity
		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				OptetAuditModel p = (OptetAuditModel) element;
				return p.getSeverity();
			}
		});
		
		// Now the ruleset
		col = createTableViewerColumn(titles[4], bounds[4], 4);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				OptetAuditModel p = (OptetAuditModel) element;
				return p.getRuleset();
			}
		});
		
		// Now the message
		col = createTableViewerColumn(titles[5], bounds[5], 5);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				OptetAuditModel p = (OptetAuditModel) element;
				return p.getMessage();
			}
		});
		
		// Now the recommendation
		col = createTableViewerColumn(titles[6], bounds[6], 6);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				OptetAuditModel p = (OptetAuditModel) element;
				return p.getRecommandation();
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
		column.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				((OptetAuditViewSorter) viewer.getSorter()).doSort(colNumber);
				viewer.refresh();
			}
		});
		return viewerColumn;
	}


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
		viewer.setInput(OptetAuditModelProvider.INSTANCE.getAudit());
		viewer.refresh();
	}

	/**
	 * Refresh the TableView
	 */
	public void refresh() {
		viewer.setInput(OptetAuditModelProvider.INSTANCE.getAudit());
		viewer.refresh();
	}
} 