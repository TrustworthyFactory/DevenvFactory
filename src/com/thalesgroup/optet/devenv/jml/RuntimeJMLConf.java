package com.thalesgroup.optet.devenv.jml;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.sun.xml.internal.ws.util.Pool.Marshaller;
import com.thalesgroup.dtwc.Asset;
import com.thalesgroup.dtwc.TWProblemDefinition;
import com.thalesgroup.dtwc.Threat;
import com.thalesgroup.optet.common.data.DataModel;
import com.thalesgroup.optet.common.data.OptetDataModel;
import com.thalesgroup.optet.devenv.datamodel.InternalSystemDM;
import com.thalesgroup.optet.devenv.jml.jaxb.JMLFiles;
import com.thalesgroup.optet.devenv.jml.jaxb.JMLFiles.JMLFile;

/*
 *		OPTET Factory
 *
 *	Class test 1.0 3 mars 2015
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

/**
 * @author F. Motte
 *
 */
public class RuntimeJMLConf extends Dialog {

	protected Object result;
	protected Shell shell;
	private Table table;

	private java.util.List<String> metrics = new ArrayList<>();
	private Map<String, IFile> files; 

	private Map<String, Collection<String>> checkedElementMap;

	private String currentfileSelected;


	private IFile jmlFile;

	private IJavaProject currentProject;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RuntimeJMLConf(Shell parent, int style, IJavaProject project) {
		super(parent, style);
		currentProject = project;
		checkedElementMap = new HashMap<>();

		jmlFile = project.getProject().getFile("/Optet/JMLRuntimeConf.xml");
		if (jmlFile.exists()){	

			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(JMLFiles.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				JMLFiles files = (JMLFiles) jaxbUnmarshaller.unmarshal(jmlFile.getRawLocation().toFile());	
				java.util.List<JMLFile> filesList = files.getJMLFile();

				for (Iterator iterator = filesList.iterator(); iterator
						.hasNext();) {
					JMLFile jmlFile = (JMLFile) iterator.next();
					checkedElementMap.put(jmlFile.getName(), jmlFile.getJMLmetric());
				}
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



		}

		setText("JML runtime configuration");

		try {
			files = new HashMap<String, IFile>();
			
			if (JUnitCore.findTestTypes(project, null).length != 0) {
				ArrayList<String> names=new ArrayList<String>();
				IType[] types=JUnitCore.findTestTypes(project,null);
				for (  IType type : types) {
					names.add(type.getFullyQualifiedName());
					
					
					files.put(type.getPath().toOSString(), null);
				}
			}
			
			//files = members(project.getProject(), "java");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DataModel data = OptetDataModel.getInstance();
		Set<String> listAttr = data.getAllTWAttributeName();
		for (Iterator iterator = listAttr.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			Set<String> listEvidence = data.getEvidenceForTWAttribute(string);
			for (Iterator iterator2 = listEvidence.iterator(); iterator2
					.hasNext();) {
				String string2 = (String) iterator2.next();
				if (!metrics.contains(string2)){
					metrics.add(string2);
					System.out.println("add " + string2);
				}
			}
		}

	}



	private Map<String, IFile> members(IContainer container,String extension) throws CoreException {
		Map<String, IFile> output=new HashMap<String, IFile>();
		if (container != null && container.isAccessible()) {
			IResource[] children=container.members();
			if (children != null) {
				for (int i=0; i < children.length; ++i) {
					IResource resource=children[i];
					if (resource instanceof IFile && extension.equals(((IFile)resource).getFileExtension())) {
						output.put(((IFile)resource).getFullPath().toString(), (IFile)resource);
					}
					else         if (resource instanceof IContainer) {
						output.putAll(members((IContainer)resource,extension));
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
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
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
		shell = new Shell(getParent(), getStyle());
		shell.setSize(1000, 350);
		shell.setText(getText());


		Button cancelButton = new Button(shell, SWT.NONE);
		cancelButton.setBounds(840, 280, 68, 23);
		cancelButton.setText("Cancel");

		Button validateButton = new Button(shell, SWT.NONE);
		validateButton.setBounds(915, 280, 68, 23);
		validateButton.setText("Validate");


		validateButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event){				

				JAXBContext jaxbContext;
				try {
					
					//OptetDataModel.getInstance().cleanMetric();
										
					jaxbContext = JAXBContext.newInstance(JMLFiles.class);
					javax.xml.bind.Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

					JMLFiles jmlFiles = new JMLFiles();
					for(Entry<String, Collection<String>> entry : checkedElementMap.entrySet()) {
						String key = entry.getKey();
					

						JMLFile file = new JMLFile();
						file.setName(key);

						Collection<String> values = entry.getValue();
						for (Iterator iterator = values.iterator(); iterator
								.hasNext();) {
							String string = (String) iterator.next();
							file.getJMLmetric().add(string);
						}												
						jmlFiles.getJMLFile().add(file);

					}



					jaxbMarshaller.marshal(jmlFiles, jmlFile.getRawLocation().toFile());






				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				shell.close();
			}
		});
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {

				shell.close();
			}
		});

		final CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer.newCheckList(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table = checkboxTableViewer.getTable();
		table.setBounds(500, 10, 480, 250);		
		table.setEnabled(false);
		checkboxTableViewer.setContentProvider(new ModelContentProvider());
		checkboxTableViewer.setInput(metrics);

		ListViewer listViewer = new ListViewer(shell, SWT.BORDER | SWT.V_SCROLL  | SWT.H_SCROLL| SWT.SINGLE);
		List list = listViewer.getList();
		list.setBounds(10, 10, 480, 250);
		list.setItems(files.keySet().toArray(new String[files.keySet().size()]));


		checkboxTableViewer.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				// TODO Auto-generated method stub

				System.out.println("selectionChanged tableViewer_1.getCheckedElements() " + checkboxTableViewer.getCheckedElements().length);

				java.util.List<String> list = new ArrayList<String>();
				for (int i = 0; i <  checkboxTableViewer.getCheckedElements().length; i++) {
					if (checkboxTableViewer.getCheckedElements()[i] instanceof String) {
						System.out.println("is instance");
						String evidence = (String) checkboxTableViewer.getCheckedElements()[i];
						list.add(evidence);
					}
					else System.out.println("is not instance");
				}

				checkedElementMap.put(currentfileSelected, list);
			}

		});

		listViewer.getList( ).addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}

			public void widgetSelected( SelectionEvent e )
			{
				org.eclipse.swt.widgets.List list = (org.eclipse.swt.widgets.List) e.getSource( );
				String[] strs = list.getSelection( );
				if ( strs != null && strs.length > 0 )
				{
					for ( int i = 0; i < strs.length; i++ )
					{
						System.out.println("string " +  strs[i] );
						currentfileSelected = strs[i];
						table.setEnabled(true);
						System.out.println("currentfileSelected " + currentfileSelected);
						checkboxTableViewer.setAllChecked(false);
						if (checkedElementMap.containsKey(currentfileSelected)){                			
							checkboxTableViewer.setCheckedElements(checkedElementMap.get(currentfileSelected).toArray());
						}
					}
				}
			}
		} );
	}
}
