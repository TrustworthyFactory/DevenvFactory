/*
 *		OPTET Factory
 *
 *	Class ProblemDefinition 1.0 28 oct. 2013
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.jml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


/**
 * @author F. Motte
 *
 */
public class JMLStaticSelectionView extends Dialog {

	protected Object result;
	protected Shell shlJmlEscDefinition;
	private org.eclipse.swt.widgets.List table;
	//private InternalSystemDM data;
	private IFile currentFileSelected = null;
	private List<IFile> files; 

	//private Map<String, ISelection> problemsForAsset;

	private Map<String, Collection<IFile>> checkedElementMap;

	//	private CertificateWrapper cw;
	//	private IFile dtwcFile;
	//	private DTWC dtwc;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public JMLStaticSelectionView(Shell parent, int style, IProject project) {
		super(parent, style);
		setText("SWT Dialog");
		//data = InternalSystemDM.getInternalSystemDM();
		checkedElementMap = new HashMap<>();

		try {
			files = members(project, "java");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Iterator iterator = files.iterator(); iterator.hasNext();) {
			IFile iFile = (IFile) iterator.next();
			System.out.println("file " + iFile.getFullPath());
		}
		
	}


	private List<IFile> members(IContainer container,String extension) throws CoreException {
		List<IFile> output=new ArrayList<IFile>();
		if (container != null && container.isAccessible()) {
			IResource[] children=container.members();
			if (children != null) {
				for (int i=0; i < children.length; ++i) {
					IResource resource=children[i];
					if (resource instanceof IFile && extension.equals(((IFile)resource).getFileExtension())) {
						output.add((IFile)resource);
					}
					else         if (resource instanceof IContainer) {
						output.addAll(members((IContainer)resource,extension));
					}
				}
			}
		}
		return output;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		//		ErrorDialog errorDialog = new ErrorDialog(getParent(), "Project Selection", "Please, select a project before", null, IStatus.ERROR);
		//		errorDialog.open();



		//		Collection<TWProblemDefinitionImpl> probDef = dtwc.getTWProblemDefinitions();
		//		for (Iterator iterator = probDef.iterator(); iterator.hasNext();) {
		//			TWProblemDefinitionImpl twProblemDefinitionImpl = (TWProblemDefinitionImpl) iterator
		//					.next();
		//			System.out.println("add asset " + twProblemDefinitionImpl.getAsset().getId());
		//			System.out.println("twProblemDefinitionImpl" + twProblemDefinitionImpl.getThreat().getId());
		//			System.out.println(data);
		//			System.out.println(data.getThreats());
		//			System.out.println(data.getThreats().get(twProblemDefinitionImpl.getThreat().getId()));
		//			System.out.println(" with threat " + data.getThreats().get(twProblemDefinitionImpl.getThreat().getId()));
		//			checkedElementMap.get(twProblemDefinitionImpl.getAsset().getId()).add(data.getThreats().get(twProblemDefinitionImpl.getThreat().getId()));
		//		}

		//		 
		//		for(Entry<String, Element> entry : assets.entrySet()) {
		//			System.out.println("read threat from asset");
		//			Collection<Threat> threatColl = entry.getValue().getThreads();
		//			checkedElementMap.put(entry.getValue().ID, threatColl);
		//		}


		createContents();
		shlJmlEscDefinition.open();
		shlJmlEscDefinition.layout();
		Display display = getParent().getDisplay();
		while (!shlJmlEscDefinition.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlJmlEscDefinition = new Shell(getParent(), getStyle());
		shlJmlEscDefinition.setSize(445, 406);
		shlJmlEscDefinition.setText("JML ESC");

		Button cancelButton = new Button(shlJmlEscDefinition, SWT.NONE);
		cancelButton.setBounds(292, 341, 68, 23);
		cancelButton.setText("Cancel");

		Button validateButton = new Button(shlJmlEscDefinition, SWT.NONE);
		validateButton.setBounds(366, 341, 68, 23);
		validateButton.setText("Validate");


		validateButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event){				


				//					Collection<TWProblemDefinitionImpl> twpdList = dtwc.getTWProblemDefinitions();
				//					for (Iterator iterator = twpdList.iterator(); iterator
				//							.hasNext();) {
				//						TWProblemDefinitionImpl twProblemDefinitionImpl = (TWProblemDefinitionImpl) iterator
				//								.next();
				//						dtwc.deleteTWProblemDefinition(twProblemDefinitionImpl.getId());
				//					}


				//Collection<String> twpdList = dtwc.getTWProblemDefinitionsKey();


				for (Entry<String, Collection<IFile>> entry : checkedElementMap.entrySet()) {
					System.out.println("Key : " + entry.getKey() + " Value : ");

					for (Iterator iterator = entry.getValue().iterator(); iterator
							.hasNext();) {
						//							com.thalesgroup.optet.devenv.datamodel.Threat threat = (com.thalesgroup.optet.devenv.datamodel.Threat) iterator.next();
						//							System.out.println("threat : " + threat.getId() + " " + threat.getCategory());

						//							TWProblemDefinition twpd = dtwc.addTWProblemDefinition(entry.getKey() + "-" + threat.getId());
						//							Asset asset = twpd.addAsset(entry.getKey());
						//							asset.setId(entry.getKey());
						//							Threat threat_p = twpd.addThreat(threat.getId());
						//							threat_p.setId(threat.getId());
						//							threat_p.setType(threat.getType());							
					}
				}



				shlJmlEscDefinition.close();
			}
		});
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {

				shlJmlEscDefinition.close();
			}
		});

		Label lblAssets = new Label(shlJmlEscDefinition, SWT.NONE);
		lblAssets.setBounds(10, 10, 49, 13);
		lblAssets.setText("Files");

		ListViewer tableViewer = new ListViewer(shlJmlEscDefinition, SWT.BORDER | SWT.FULL_SELECTION );
		table = tableViewer.getList();
		table.setBounds(10, 29, 165, 296);
		ContentProvider cp = new ContentProvider();

		final CheckboxTableViewer tableViewer_1 = new CheckboxTableViewer(shlJmlEscDefinition);
		Table table_1 = tableViewer_1.getTable();
		table_1.setBounds(181, 29, 248, 296);
		table_1.setEnabled(false);


//		String[] COLUMN_NAMES = new String[] { "FOO", "BAR" };
//		int[] COLUMN_WIDTHS = new int[] { 300, 200 };
//		String[] COLUMNS_PROPERTIES = new String[] { "foo_prop", "bar_prop" };
//		for (int i = 0; i < COLUMN_NAMES.length; i++) {
//			TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
//			tableColumn.setText(COLUMN_NAMES[i]);
//			tableColumn.setWidth(COLUMN_WIDTHS[i]);
//		}

		tableViewer_1.setContentProvider(new ModelContentProvider());

		//tableViewer.setColumnProperties(COLUMNS_PROPERTIES);
		//tableViewer_1.setInput(data.getThreats().values());
		tableViewer_1.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				// TODO Auto-generated method stub

				System.out.println("selectionChanged tableViewer_1.getCheckedElements() " + tableViewer_1.getCheckedElements().length);

				for (int i = 0; i <  tableViewer_1.getCheckedElements().length; i++) {
					//					if (tableViewer_1.getCheckedElements()[i] instanceof com.thalesgroup.optet.devenv.datamodel.Threat) {
					//						System.out.println("is instance");
					//						com.thalesgroup.optet.devenv.datamodel.Threat thread = (com.thalesgroup.optet.devenv.datamodel.Threat) tableViewer_1.getCheckedElements()[i];
					//						list.add(thread);
					//					}
					//					else System.out.println("is not instance");
				}

				//checkedElementMap.put(currentFileSelected.getId(), list);
			}

		});

		// specific

//				tableViewer.setLabelProvider(
//						new ComponentListLabelProvider());
				tableViewer.setContentProvider(
						new ArrayContentProvider());
				tableViewer.setInput(files.toArray());
		//		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
		//			public void selectionChanged(SelectionChangedEvent event)
		//			{
		//				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		//				System.out.println("selectionChanged: " + selection.getFirstElement());
		//				currentFileSelected =  (Asset) selection.getFirstElement();
		//
		//				tableViewer_1.setCheckedElements(checkedElementMap.get(currentFileSelected.getId()).toArray());
		//				tableViewer_1.getTable().setEnabled(true);
		//				if (checkedElementMap.containsKey(currentFileSelected.getId())){
		//					tableViewer_1.setCheckedElements(checkedElementMap.get(currentFileSelected.getId()).toArray());
		//				} else {
		//					tableViewer_1.setCheckedElements(new Object[0]);
		//				}
		//
		//
		//
		//			}
		//		}
		//				);

	}



}
