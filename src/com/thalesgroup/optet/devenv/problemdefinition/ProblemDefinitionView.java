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

package com.thalesgroup.optet.devenv.problemdefinition;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.osgi.framework.Bundle;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

//import com.thalesgroup.optet.devenv.datamodel.Attribute;
//import com.thalesgroup.optet.devenv.datamodel.Component;
//import com.thalesgroup.optet.devenv.datamodel.Element;
//import com.thalesgroup.optet.devenv.datamodel.InternalSystemDM;
//import com.thalesgroup.optet.devenv.datamodel.Threat;
import com.thalesgroup.dtwc.Asset;
import com.thalesgroup.dtwc.Attribute;
import com.thalesgroup.dtwc.DTWC;
import com.thalesgroup.dtwc.TWProblemDefinition;
import com.thalesgroup.dtwc.Threat;
import com.thalesgroup.dtwc.impl.AssetImpl;
import com.thalesgroup.dtwc.impl.CertificateWrapper;
import com.thalesgroup.dtwc.impl.TWProblemDefinitionImpl;
import com.thalesgroup.optet.devenv.Activator;
import com.thalesgroup.optet.devenv.datamodel.InternalSystemDM;
import com.thalesgroup.optet.devenv.systemdesc.ComponentListLabelProvider;
import com.thalesgroup.optet.devenv.systemdesc.ComponentTreeContentProvider;

/**
 * @author F. Motte
 *
 */
public class ProblemDefinitionView extends Dialog {

	protected Object result;
	protected Shell shlTwProblemDefinition;
	private Table table;
	private InternalSystemDM data;
	private Asset currentAssetSelected = null;
	private Collection<AssetImpl> assets; 

	//private Map<String, ISelection> problemsForAsset;

	private Map<String, Collection<com.thalesgroup.optet.devenv.datamodel.Threat>> checkedElementMap;

	private CertificateWrapper cw;
	private IFile dtwcFile;
	private DTWC dtwc;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ProblemDefinitionView(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
		data = InternalSystemDM.getInternalSystemDM();
		checkedElementMap = new HashMap<>();


	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		//		ErrorDialog errorDialog = new ErrorDialog(getParent(), "Project Selection", "Please, select a project before", null, IStatus.ERROR);
		//		errorDialog.open();

		if (data.getCurrentProject() == null)
		{
			MessageDialog.openError(getParent(), "Project Selection", "Please, select a project before");
			return false;
		}


		dtwcFile = data.getCurrentProject().getFile("/Optet/dtwc.ttl");
		cw = new com.thalesgroup.dtwc.impl.CertificateWrapper();

		if (!dtwcFile.exists()){			
			Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
			URL fileURL = bundle.getEntry("resources/dtwc.ttl");
			InputStream in;
			try {
				in = new FileInputStream(FileLocator.toFileURL(fileURL).getPath());
				dtwcFile.create(in, false, null);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			data.getCurrentProject().getName();
			dtwc = cw.createCertificate(data.getCurrentProject().getName(), dtwcFile.getRawLocation().toString());
			try {
				cw.printCertificate(dtwcFile.getRawLocationURI().toString());
			} catch (OWLOntologyStorageException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			dtwc = cw.loadCertificate(data.getCurrentProject().getName(),dtwcFile.getRawLocation().toString());
		}
		assets = dtwc.getSystemDescription().getAssetList();
		for (Iterator iterator = assets.iterator(); iterator.hasNext();) {
			AssetImpl asset = (AssetImpl) iterator.next();
			checkedElementMap.put(asset.getId(), new ArrayList<com.thalesgroup.optet.devenv.datamodel.Threat>());
		}


		Collection<TWProblemDefinitionImpl> probDef = dtwc.getTWProblemDefinitions();
		for (Iterator iterator = probDef.iterator(); iterator.hasNext();) {
			TWProblemDefinitionImpl twProblemDefinitionImpl = (TWProblemDefinitionImpl) iterator
					.next();
			System.out.println("add asset " + twProblemDefinitionImpl.getAsset().getId());
			System.out.println("twProblemDefinitionImpl" + twProblemDefinitionImpl.getThreat().getId());
			System.out.println(data);
			System.out.println(data.getThreats());
			System.out.println(data.getThreats().get(twProblemDefinitionImpl.getThreat().getId()));
			System.out.println(" with threat " + data.getThreats().get(twProblemDefinitionImpl.getThreat().getId()));
			checkedElementMap.get(twProblemDefinitionImpl.getAsset().getId()).add(data.getThreats().get(twProblemDefinitionImpl.getThreat().getId()));
		}

		//		 
		//		for(Entry<String, Element> entry : assets.entrySet()) {
		//			System.out.println("read threat from asset");
		//			Collection<Threat> threatColl = entry.getValue().getThreads();
		//			checkedElementMap.put(entry.getValue().ID, threatColl);
		//		}


		createContents();
		shlTwProblemDefinition.open();
		shlTwProblemDefinition.layout();
		Display display = getParent().getDisplay();
		while (!shlTwProblemDefinition.isDisposed()) {
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
		shlTwProblemDefinition = new Shell(getParent(), getStyle());
		shlTwProblemDefinition.setSize(445, 406);
		shlTwProblemDefinition.setText("TW Problem Definition");

		Button cancelButton = new Button(shlTwProblemDefinition, SWT.NONE);
		cancelButton.setBounds(292, 341, 68, 23);
		cancelButton.setText("Cancel");

		Button validateButton = new Button(shlTwProblemDefinition, SWT.NONE);
		validateButton.setBounds(366, 341, 68, 23);
		validateButton.setText("Validate");


		validateButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event){				
				try {


					//					Collection<TWProblemDefinitionImpl> twpdList = dtwc.getTWProblemDefinitions();
					//					for (Iterator iterator = twpdList.iterator(); iterator
					//							.hasNext();) {
					//						TWProblemDefinitionImpl twProblemDefinitionImpl = (TWProblemDefinitionImpl) iterator
					//								.next();
					//						dtwc.deleteTWProblemDefinition(twProblemDefinitionImpl.getId());
					//					}


					Collection<String> twpdList = dtwc.getTWProblemDefinitionsKey();


					cw.printCertificate("file:///D:/temp/test.ttl");
					dtwc.getTWProblemDefinitions().clear();

					for (Entry<String, Collection<com.thalesgroup.optet.devenv.datamodel.Threat>> entry : checkedElementMap.entrySet()) {
						System.out.println("Key : " + entry.getKey() + " Value : ");

						for (Iterator iterator = entry.getValue().iterator(); iterator
								.hasNext();) {
							com.thalesgroup.optet.devenv.datamodel.Threat threat = (com.thalesgroup.optet.devenv.datamodel.Threat) iterator.next();
							System.out.println("threat : " + threat.getId() + " " + threat.getCategory());

							TWProblemDefinition twpd = dtwc.addTWProblemDefinition(entry.getKey() + "-" + threat.getId());
							Asset asset = twpd.addAsset(entry.getKey());
							asset.setId(entry.getKey());
							Threat threat_p = twpd.addThreat(threat.getId());
							threat_p.setId(threat.getId());
							threat_p.setType(threat.getType());							
						}
					}

					System.out.println("print cert " + dtwcFile.getRawLocationURI().toString());

					cw.printCertificate(dtwcFile.getRawLocationURI().toString());
				} catch (OWLOntologyStorageException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				shlTwProblemDefinition.close();
			}
		});
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {

				shlTwProblemDefinition.close();
			}
		});

		Label lblAssets = new Label(shlTwProblemDefinition, SWT.NONE);
		lblAssets.setBounds(10, 10, 49, 13);
		lblAssets.setText("Assets");

		TableViewer tableViewer = new TableViewer(shlTwProblemDefinition, SWT.BORDER | SWT.FULL_SELECTION );
		table = tableViewer.getTable();
		table.setBounds(10, 29, 165, 296);
		ContentProvider cp = new ContentProvider();

		final CheckboxTableViewer tableViewer_1 = new CheckboxTableViewer(shlTwProblemDefinition);
		Table table_1 = tableViewer_1.getTable();
		table_1.setBounds(181, 29, 248, 296);
		table_1.setEnabled(false);


		String[] COLUMN_NAMES = new String[] { "FOO", "BAR" };
		int[] COLUMN_WIDTHS = new int[] { 300, 200 };
		String[] COLUMNS_PROPERTIES = new String[] { "foo_prop", "bar_prop" };
		for (int i = 0; i < COLUMN_NAMES.length; i++) {
			TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
			tableColumn.setText(COLUMN_NAMES[i]);
			tableColumn.setWidth(COLUMN_WIDTHS[i]);
		}

		tableViewer_1.setContentProvider(new ModelContentProvider());
		tableViewer_1.setLabelProvider(new ModelLabelProvider());
		tableViewer.setColumnProperties(COLUMNS_PROPERTIES);
		System.out.println("********************* " + data.getThreats().values().size() );
		tableViewer_1.setInput(data.getThreats().values());
		tableViewer_1.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				// TODO Auto-generated method stub

				System.out.println("selectionChanged tableViewer_1.getCheckedElements() " + tableViewer_1.getCheckedElements().length);

				List<com.thalesgroup.optet.devenv.datamodel.Threat> list = new ArrayList<com.thalesgroup.optet.devenv.datamodel.Threat>();
				for (int i = 0; i <  tableViewer_1.getCheckedElements().length; i++) {
					if (tableViewer_1.getCheckedElements()[i] instanceof com.thalesgroup.optet.devenv.datamodel.Threat) {
						System.out.println("is instance");
						com.thalesgroup.optet.devenv.datamodel.Threat thread = (com.thalesgroup.optet.devenv.datamodel.Threat) tableViewer_1.getCheckedElements()[i];
						list.add(thread);
					}
					else System.out.println("is not instance");
				}

				checkedElementMap.put(currentAssetSelected.getId(), list);
			}

		});

		// specific

		tableViewer.setLabelProvider(
				new ComponentListLabelProvider());
		tableViewer.setContentProvider(
				new ComponentTreeContentProvider(dtwc));
		tableViewer.setInput(dtwc.getSystemDescription().getAssetList().toArray());
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event)
			{
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				System.out.println("selectionChanged: " + selection.getFirstElement());
				currentAssetSelected =  (Asset) selection.getFirstElement();

				tableViewer_1.setCheckedElements(checkedElementMap.get(currentAssetSelected.getId()).toArray());
				tableViewer_1.getTable().setEnabled(true);
				if (checkedElementMap.containsKey(currentAssetSelected.getId())){
					tableViewer_1.setCheckedElements(checkedElementMap.get(currentAssetSelected.getId()).toArray());
				} else {
					tableViewer_1.setCheckedElements(new Object[0]);
				}



			}
		}
				);

	}



}
