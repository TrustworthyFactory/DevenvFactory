package com.thalesgroup.optet.devenv.systemdesc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.List;
import org.osgi.framework.Bundle;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.thalesgroup.dtwc.impl.CertificateWrapper;
import com.thalesgroup.dtwc.Asset;
import com.thalesgroup.dtwc.Attribute;
import com.thalesgroup.dtwc.Component;
import com.thalesgroup.dtwc.DTWC;
import com.thalesgroup.dtwc.Stakeholder;
import com.thalesgroup.dtwc.impl.AssetImpl;
import com.thalesgroup.dtwc.impl.AttributeImpl;
import com.thalesgroup.dtwc.impl.ComponentImpl;
import com.thalesgroup.dtwc.impl.StakeholderImpl;
import com.thalesgroup.optet.devenv.Activator;
//import com.thalesgroup.optet.devenv.datamodel.Attribute;
//import com.thalesgroup.optet.devenv.datamodel.Component;
//import com.thalesgroup.optet.devenv.datamodel.Element;
import com.thalesgroup.optet.devenv.datamodel.InternalSystemDM;
//import com.thalesgroup.optet.devenv.datamodel.Stakeholder;

public class SystemDialogBox extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text componentIDText;
	private Text AttributeText;
	private Text StakeholderText;
	private Text AssetIDText;
	private Button ComponentTOEIs;
	private List fullStakeholderList;
	private List stakeholderList;
	private List AttributeList;
	private TreeViewer treeViewer;
	//private InternalSystemDM data;
	private Attribute currentAttributeSelected;
	private Stakeholder currentStakeholderSelected;
	private Asset currentAssetSelected;
	private Group group;
	private Combo AttributeTypeCombo;
	private Combo ComponentTypeCombo;
	private Combo StakeholderTypeCombo;
	//private Button attributeIntoTOE;
	private Text AssetTypeText;
	private TableViewer tableViewer;
	private Table table;

	private Collection<ComponentImpl> components;

	private Collection<StakeholderImpl> stakeholders;

	private Collection<AssetImpl> assets;

	private Component currentComponent;

	private CertificateWrapper cw;
	private IFile dtwcFile;
	private DTWC dtwc;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SystemDialogBox(Shell parent, int style) {
		super(parent, style);
		setText("System Description");



	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		System.out.println("Open");

		InternalSystemDM data = InternalSystemDM.getInternalSystemDM();
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




		components = dtwc.getSystemDescription().getComponentList();

		System.out.println("size" + components.size());
		stakeholders = dtwc.getSystemDescription().getStakeholderList();
		assets = dtwc.getSystemDescription().getAssetList();


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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MIN | SWT.MAX);
		shell.setSize(583, 565);
		shell.setText(getText());


		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 561, 488);

		TabItem tbtmTargetOfCertification = new TabItem(tabFolder, SWT.NONE);
		tbtmTargetOfCertification.setText("Target Of Certification");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmTargetOfCertification.setControl(composite);

		group = new Group(composite, SWT.NONE);
		group.setBounds(202, 10, 341, 442);

		Group grpDescription = new Group(group, SWT.NONE);
		grpDescription.setText("Description");
		grpDescription.setBounds(10, 10, 321, 138);


		Label componentIDLbl = new Label(grpDescription, SWT.NONE);
		componentIDLbl.setBounds(10, 22, 49, 13);
		componentIDLbl.setText("ID");

		componentIDText = new Text(grpDescription, SWT.BORDER);
		componentIDText.setEditable(false);
		componentIDText.setBounds(82, 16, 158, 19);


		Label ComponentTypeLbl = new Label(grpDescription, SWT.NONE);
		ComponentTypeLbl.setBounds(10, 52, 49, 13);
		ComponentTypeLbl.setText("Type :");


		ComponentTypeCombo = new Combo(grpDescription, SWT.NONE);
		InternalSystemDM.getInternalSystemDM();
		ComponentTypeCombo.setItems(InternalSystemDM.componentCategories());
		ComponentTypeCombo.setBounds(82, 49, 158, 19);
		ComponentTypeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				currentComponent.setType(ComponentTypeCombo.getText());
				loadComponent(currentComponent);
			}
		});

		Label componentTOELbl = new Label(grpDescription, SWT.NONE);
		componentTOELbl.setText("Is an asset");
		componentTOELbl.setBounds(10, 89, 60, 13);

		ComponentTOEIs = new Button(grpDescription, SWT.CHECK);

		ComponentTOEIs.setBounds(82, 89, 85, 16);
		ComponentTOEIs.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("set in toe " + ComponentTOEIs.getSelection());
				System.out.println("currentComponent" + currentComponent.getId());
				currentComponent.setInTargetOfEvaluation(ComponentTOEIs.getSelection());
				System.out.println(currentComponent.getIntargetOfEvaluation());
				loadComponent(currentComponent);
				
				if (ComponentTOEIs.getSelection()){
					Asset asset = dtwc.getSystemDescription().addAsset(currentComponent.getId());
					asset.setId(currentComponent.getId());
				}else{
					dtwc.getSystemDescription().removeAsset(currentComponent.getId());
				}

			}
		});
		Group grpComponentModel = new Group(group, SWT.NONE);
		grpComponentModel.setText("Component Model");
		grpComponentModel.setBounds(10, 154, 321, 278);

		AttributeList = new List(grpComponentModel, SWT.BORDER);
		AttributeList.setBounds(205, 31, 106, 124);

		Button btnAddAttribute = new Button(grpComponentModel, SWT.NONE);
		btnAddAttribute.setBounds(126, 63, 68, 23);
		btnAddAttribute.setText("Add >>");

		Button btnRemoveAttribute = new Button(grpComponentModel, SWT.NONE);
		btnRemoveAttribute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnRemoveAttribute.setText("<< remove");
		btnRemoveAttribute.setBounds(126, 92, 68, 23);

		AttributeText = new Text(grpComponentModel, SWT.BORDER);
		AttributeText.setBounds(10, 49, 100, 19);

		Label lblAttribute = new Label(grpComponentModel, SWT.NONE);
		lblAttribute.setText("Attribute");
		lblAttribute.setBounds(10, 35, 49, 13);

		stakeholderList = new List(grpComponentModel, SWT.BORDER);
		stakeholderList.setBounds(205, 161, 106, 84);

		Button btnAddStakeholder = new Button(grpComponentModel, SWT.NONE);
		btnAddStakeholder.setText("Add >>");
		btnAddStakeholder.setBounds(126, 177, 68, 23);

		Button btnRemoveStakeholder = new Button(grpComponentModel, SWT.NONE);
		btnRemoveStakeholder.setText("<< remove");
		btnRemoveStakeholder.setBounds(126, 206, 68, 23);

		StakeholderText = new Text(grpComponentModel, SWT.BORDER);
		StakeholderText.setBounds(10, 179, 100, 19);

		Label lblAssociatedStakeholder = new Label(grpComponentModel, SWT.NONE);
		lblAssociatedStakeholder.setText("Associated Stakeholder");
		lblAssociatedStakeholder.setBounds(10, 165, 112, 13);

		Label lblType = new Label(grpComponentModel, SWT.NONE);
		lblType.setText("type");
		lblType.setBounds(10, 76, 49, 13);

		AttributeTypeCombo = new Combo(grpComponentModel, SWT.NONE);
		InternalSystemDM.getInternalSystemDM();
		AttributeTypeCombo.setItems(InternalSystemDM.componentCategories());
		AttributeTypeCombo.setBounds(10, 95, 100, 21);

		Label StakeholerType = new Label(grpComponentModel, SWT.NONE);
		StakeholerType.setText("type");
		StakeholerType.setBounds(10, 205, 49, 13);

		StakeholderTypeCombo = new Combo(grpComponentModel, SWT.NONE);
		InternalSystemDM.getInternalSystemDM();
		StakeholderTypeCombo.setItems(InternalSystemDM.componentCategories());
		StakeholderTypeCombo.setBounds(10, 224, 100, 21);

		//		Label label = new Label(grpComponentModel, SWT.NONE);
		//		label.setText("Is an asset");
		//		label.setBounds(10, 122, 60, 13);

		//		attributeIntoTOE = new Button(grpComponentModel, SWT.CHECK);
		//		attributeIntoTOE.setBounds(75, 121, 85, 16);

		treeViewer = new TreeViewer(composite, SWT.BORDER);
		Tree ComponentTree = treeViewer.getTree();
		ComponentTree.setBounds(10, 37, 186, 250);

		//		TreeViewer treeViewer_1 = new TreeViewer(composite, SWT.BORDER);
		//		Tree StakeholderTree = treeViewer_1.getTree();
		//		StakeholderTree.setToolTipText("");
		//		StakeholderTree.setBounds(10, 313, 186, 139);

		fullStakeholderList = new List(composite, SWT.BORDER);
		fullStakeholderList.setBounds(10, 313, 186, 139);

		Label lblComponents = new Label(composite, SWT.NONE);
		lblComponents.setBounds(10, 18, 73, 13);
		lblComponents.setText("Components");

		Label lblStakeholders = new Label(composite, SWT.NONE);
		lblStakeholders.setBounds(10, 294, 73, 13);
		lblStakeholders.setText("Stakeholders");

		//		TabItem tbtmTargetOfEvaluation = new TabItem(tabFolder, SWT.NONE);
		//		tbtmTargetOfEvaluation.setText("Target Of Evaluation");
		//
		//		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		//		tbtmTargetOfEvaluation.setControl(composite_1);
		//
		//		tableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		//		//ListViewer listViewer = new ListViewer(composite_1, SWT.BORDER);
		//		table = tableViewer.getTable();
		//		table.setBounds(10, 33, 186, 419);

		//		Group group_1 = new Group(composite_1, SWT.NONE);
		//		group_1.setBounds(202, 10, 341, 442);

		//		Group group_2 = new Group(group_1, SWT.NONE);
		//		group_2.setText("Description");
		//		group_2.setBounds(10, 10, 321, 148);

		//		Label AssetIDLbl = new Label(group_2, SWT.NONE);
		//		AssetIDLbl.setText("ID");
		//		AssetIDLbl.setBounds(10, 22, 49, 13);
		//
		//		AssetIDText = new Text(group_2, SWT.BORDER);
		//		AssetIDText.setBounds(82, 16, 158, 19);
		//
		//		AssetTypeText = new Text(group_2, SWT.BORDER);
		//		AssetTypeText.setBounds(82, 49, 158, 19);
		//
		//		Label AssertTypeLbl = new Label(group_2, SWT.NONE);
		//		AssertTypeLbl.setText("Type");
		//		AssertTypeLbl.setBounds(10, 52, 49, 13);
		//
		//		Button btnRemoveFromTOE = new Button(group_2, SWT.NONE);
		//		btnRemoveFromTOE.addSelectionListener(new SelectionAdapter() {
		//			@Override
		//			public void widgetSelected(SelectionEvent e) {
		//				if (currentAssetSelected!=null){
		//					if (currentAssetSelected instanceof Component)
		//					{
		//						((Component) currentAssetSelected).setInTargetOfEvaluation(false);
		//						//					} else if (currentAssetSelected instanceof Attribute){
		//						//						((Attribute) currentAssetSelected).setInTOE(false);
		//					}
		//					tableViewer.remove(currentAssetSelected);
		//					AssetIDText.setText("");
		//					AssetTypeText.setText("");
		//				}
		//
		//			}
		//		});
		//		btnRemoveFromTOE.setBounds(10, 115, 150, 23);
		//		btnRemoveFromTOE.setText("Remove from asset list");
		//
		//		Label lblAssets = new Label(composite_1, SWT.NONE);
		//		lblAssets.setBounds(10, 14, 49, 13);
		//		lblAssets.setText("Assets");

		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setBounds(408, 500, 68, 23);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnCancel.setText("Cancel");

		Button btnValidate = new Button(shell, SWT.NONE);
		btnValidate.setBounds(485, 500, 68, 23);
		btnValidate.setText("Validate");

		// specific

		//		tableViewer.setLabelProvider(
		//				new ComponentListLabelProvider());
		//		tableViewer.setContentProvider(
		//				new ComponentTreeContentProvider());
		//		tableViewer.setInput(getAssets().toArray());
		//		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
		//			public void selectionChanged(SelectionChangedEvent event)
		//			{
		//				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		//				System.out.println("Selected2: " + selection.getFirstElement());
		//
		//				currentAssetSelected = (Element) selection.getFirstElement();
		//				if (selection.getFirstElement() instanceof Component)
		//				{
		//					System.out.println("component");
		//					AssetIDText.setText(((Component)(selection.getFirstElement())).ID);
		//					AssetTypeText.setText(((Component)(selection.getFirstElement())).getType());
		//
		//				} else if (selection.getFirstElement() instanceof Attribute){
		//					System.out.println("Attriute");
		//					AssetIDText.setText(((Attribute)(selection.getFirstElement())).ID);
		//					AssetTypeText.setText(((Attribute)(selection.getFirstElement())).getType());
		//
		//				}
		//
		//			}
		//		});


		treeViewer.setLabelProvider(
				new ComponentListLabelProvider());
		treeViewer.setContentProvider(
				new ComponentTreeContentProvider(dtwc));

		ArrayList<Object> input = new ArrayList<Object>(components);
		//input.addAll(assets);
		//input.addAll(stakeholders);

		treeViewer.setInput(input);

		treeViewer.expandAll();


		MenuManager menuMgr = new MenuManager("#PopupMenu"); 
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {

				Action actionAddComp = new Action() {
					public void run() {
						System.out.println("set action");
						CreateElementAreaDialog dialog = new CreateElementAreaDialog(shell);
						dialog.create();
						if (dialog.open() == Window.OK) {

							if (treeViewer.getTree().getSelectionCount() == 0){
								Component comp = dtwc.getSystemDescription().addComponent(dialog.getComponentName());
								comp.setId(dialog.getComponentName());
								comp.setType(dialog.getComponentType());
								comp.setInTargetOfEvaluation(dialog.getComponentInTOE());
								loadComponent(comp);
							} else {
								TreeItem item = treeViewer.getTree().getSelection()[0];
								if (item != null) {
									Object obj = item.getData();
									if (obj != null) {
										if (obj instanceof Component)
										{
											Component compParent = (Component)obj;
											Component comp = compParent.getComponentModel().addComponent(dialog.getComponentName());
											comp.setId(dialog.getComponentName());
											comp.setType(dialog.getComponentType());
											comp.setInTargetOfEvaluation(dialog.getComponentInTOE());
											loadComponent(comp);
										}
									}
								}
							}
						} 
					}
				};
				actionAddComp.setText("Add Component");
				manager.add(actionAddComp);

				Action actionRemoveComp = new Action() {
					public void run() {
						System.out.println("set action");

						TreeItem item = treeViewer.getTree().getSelection()[0];
						if (item != null) {
							Object obj = item.getData();
							if (obj != null) {
								if (obj instanceof Component)
								{
									//									//System.out.println("element id " + ((Component)obj).ID);
									//									((Component)obj).parent.removeElement(((Component)obj).toString());
									//									//components.remove(((Component)obj).ID);
									//									loadComponent(currentComponent);
								}
							}
						}
					}
				};
				actionRemoveComp.setText("Remove Component");
				manager.add(actionRemoveComp);
			}

		});

		Menu menu = menuMgr.createContextMenu(treeViewer.getTree());
		treeViewer.getTree().setMenu(menu);




		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event)
			{
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				//System.out.println("Selected: " + selection.getFirstElement());

				if (selection.getFirstElement() instanceof Component)
				{
					System.out.println("component");
					loadComponent((Component)selection.getFirstElement());
				} else {
					cleanComponent();
				}

			}
		});


		btnValidate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				try {
					System.out.println("print cert " + dtwcFile.getRawLocationURI().toString());
					
					cw.printCertificate(dtwcFile.getRawLocationURI().toString());
				} catch (OWLOntologyStorageException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				shell.close();
			}
		});
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {

				shell.close();
			}
		});

//		java.util.List<Stakeholder> stakeholder = new ArrayList<Stakeholder>(stakeholders);
//		for (Iterator iterator = stakeholder.iterator(); iterator.hasNext();) {
//			Stakeholder stakeholder2 = (Stakeholder) iterator.next();
//			fullStakeholderList.add(stakeholder2.toString());
//		}

		fullStakeholderList.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				int selection = fullStakeholderList.getSelectionIndex();

				System.out.println(fullStakeholderList.getItem(selection));
				Collection<StakeholderImpl> stakeList = currentComponent.getStakeholderList();
				for (Iterator iterator = stakeList.iterator(); iterator
						.hasNext();) {
					StakeholderImpl stakeholder2 = (StakeholderImpl) iterator
							.next();
					if (stakeholder2.getId().equals(fullStakeholderList.getItem(selection))){
						currentStakeholderSelected = stakeholder2;
					}

				}

				StakeholderText.setText(currentStakeholderSelected.getId());
				StakeholderTypeCombo.select(StakeholderTypeCombo.indexOf(currentStakeholderSelected.getType()));
			}
		});

		btnRemoveAttribute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (currentAttributeSelected != null){
					System.out.println("currentAttributeSelected.getId() " + currentAttributeSelected.getId());
					currentComponent.getComponentModel().removeAttribute(currentAttributeSelected.getId());
					loadComponent(currentComponent);
				}
			}
		});

		btnAddAttribute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//System.out.println("AttributeTypeCombo.getText() " + AttributeTypeCombo.getText());
				//System.out.println("AttributeText.getText() " + AttributeText.getText());
				if (!AttributeTypeCombo.getText().isEmpty() && !AttributeText.getText().isEmpty()){
					Attribute attr = currentComponent.getComponentModel().addAttribute(AttributeText.getText());
					attr.setId(AttributeText.getText());
					attr.setType(AttributeTypeCombo.getText());
					loadComponent(currentComponent);		
				}					
			}
		});


		AttributeList.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				int selection = AttributeList.getSelectionIndex();

				//System.out.println(AttributeList.getItem(selection));

				Collection<AttributeImpl> attrList = currentComponent.getComponentModel().getAttributeList();
				for (Iterator iterator2 = attrList.iterator(); iterator2
						.hasNext();) {
					AttributeImpl attr = (AttributeImpl) iterator2
							.next();
					if (attr.getId().equals(AttributeList.getItem(selection))){
						currentAttributeSelected = attr;
					}


				}

				AttributeText.setText(currentAttributeSelected.getId());
				AttributeTypeCombo.setText(currentAttributeSelected.getType());
			}
		});



		btnRemoveStakeholder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (currentStakeholderSelected != null){
					currentComponent.removeStakeholder(currentStakeholderSelected.getId());
					loadComponent(currentComponent);
					dtwc.getSystemDescription().removeStakeholder(currentStakeholderSelected.getId());
				}
			}
		});

		btnAddStakeholder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!StakeholderTypeCombo.getText().isEmpty() && !StakeholderText.getText().isEmpty()){							
					Stakeholder stakeholder = currentComponent.addStakeholder(StakeholderText.getText());
					stakeholder.setId(StakeholderText.getText());
					stakeholder.setType(StakeholderTypeCombo.getText());

					Stakeholder stake = dtwc.getSystemDescription().addStakeholder(StakeholderText.getText());
					stake.setId(StakeholderText.getText());
					stake.setType(StakeholderTypeCombo.getText());

					loadComponent(currentComponent);
				}					
			}
		});

		stakeholderList.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				int selection = stakeholderList.getSelectionIndex();

				//System.out.println(AttributeList.getItem(selection));

				Collection<StakeholderImpl> stakeList = currentComponent.getStakeholderList();
				for (Iterator iterator2 = stakeList.iterator(); iterator2
						.hasNext();) {
					StakeholderImpl stake = (StakeholderImpl) iterator2
							.next();
					if (stake.getId().equals(stakeholderList.getItem(selection))){
						currentStakeholderSelected = stake;
					}


				}
				StakeholderText.setText(currentStakeholderSelected.getId());
				StakeholderTypeCombo.select(StakeholderTypeCombo.indexOf(currentStakeholderSelected.getType()));						
			}
		});
	}

	private void clearView(){
		componentIDText.setText("");
		ComponentTOEIs.setSelection(false);
		AttributeList.removeAll();
		stakeholderList.removeAll();
		fullStakeholderList.removeAll();
		AttributeText.setText("");
		AttributeTypeCombo.setText("");
		StakeholderText.setText("");
		StakeholderTypeCombo.setText("");
		ComponentTypeCombo.setText("");
		//		attributeIntoTOE.setSelection(false);
		//		AssetIDText.setText("");
		//		AssetTypeText.setText("");
		//		table.removeAll();
	}


	private void cleanComponent(){
		group.setEnabled(false);
		clearView();
	}

	private void loadComponent(Component component){
		group.setEnabled(true);
		System.out.println("Load component : " + component.getId() + "  " + component.getType() + " " + component.getIntargetOfEvaluation());
		clearView();

		System.out.println("Load component : " + component.getId() + "  " + component.getType() + " " + component.getIntargetOfEvaluation());
		componentIDText.setText(component.getId());




		if (ComponentTypeCombo.indexOf(component.getType()) >= 0){

			System.out.println("index of " + ComponentTypeCombo.indexOf(component.getType()));
			ComponentTypeCombo.select(ComponentTypeCombo.indexOf(component.getType()));
		}
		else {
			System.out.println("type is null or empty => clean  ");
			ComponentTypeCombo.setText("");
		}
		System.out.println("component.getIntargetOfEvaluation() " + component.getIntargetOfEvaluation());
		ComponentTOEIs.setSelection(component.getIntargetOfEvaluation());

		currentComponent = component;		
		Collection<StakeholderImpl> collStake = component.getStakeholderList();
		for (Iterator iterator = collStake.iterator(); iterator
				.hasNext();) {
			StakeholderImpl stakeholderImpl = (StakeholderImpl) iterator
					.next();
			stakeholderList.add(stakeholderImpl.getId());
			fullStakeholderList.add(stakeholderImpl.getId());
		}


		Collection<AttributeImpl> collAttr = component.getComponentModel().getAttributeList();
		for (Iterator iterator = collAttr.iterator(); iterator
				.hasNext();) {
			AttributeImpl attributeImpl = (AttributeImpl) iterator
					.next();
			AttributeList.add(attributeImpl.getId());
		}

		//		Collection<Element> coll = components.values();
		//		for (Iterator iterator = coll.iterator(); iterator.hasNext();) {
		//			Element element = (Element) iterator.next();
		//			System.out.println("Element"  + element.getID());
		//		}


		treeViewer.setInput(new ArrayList<Component>(components));
		treeViewer.expandAll();


//		java.util.List<Stakeholder> stakeholder = new ArrayList<Stakeholder>(stakeholders);
//		fullStakeholderList.removeAll();
//		for (Iterator iterator = stakeholder.iterator(); iterator.hasNext();) {
//			Stakeholder stakeholder2 = (Stakeholder) iterator.next();
//			System.out.println("fullStakeholderList add" + stakeholder2.toString());
//			fullStakeholderList.add(stakeholder2.toString());
//		}

		//		tableViewer.setInput(getAssets().toArray());


	}


	//	public java.util.List<Element> getAssets() {
	//		assets.clear();
	//		loadAssets();
	//		return new ArrayList<Element>(assets.values());
	//	}
	//
	//	private void loadAssets(){
	//
	//
	//		//System.out.println("$$$$$$$$$$$$$$$$$$ find asset");
	//		for(Entry<String, Element> entry : components.entrySet())
	//		{
	//			findAssetsIntoComponent(entry.getValue());
	//		}
	//
	//
	//		for (int i = 0; i < components.size(); i++) {
	//			findAssetsIntoComponent (components.get(i));
	//		}	
	//		//System.out.println("$$$$$$$$$$$$$$$$$$ end find asset");
	//	}
	//
	//
	//	private void findAssetsIntoComponent(Element element){
	//		//System.out.println("****  element " + element);
	//		if (element instanceof Component)
	//		{
	//			//System.out.println("**** is component "  +element.toString());
	//			Set<String> childrenKey = ((Component)element).getChildren();
	//			for (Iterator iterator = childrenKey.iterator(); iterator.hasNext();) {
	//				String string = (String) iterator.next();
	//				//System.out.println("**** " + ((Component)element).getElement(string).ID);
	//				if (((Component)element).getElement(string) instanceof Component){
	//
	//
	//					if(((Component)(((Component)element).getElement(string))).getInTOE()){
	//						//System.out.println("**** component in TEO " + ((Component)(((Component)element).getElement(string))).ID);
	//						assets.put(((Component)(((Component)element).getElement(string))).ID,((Component)element).getElement(string));
	//					}
	//					findAssetsIntoComponent (((Component)element).getElement(string));
	//				}else if (((Component)element).getElement(string) instanceof Attribute){
	//					//System.out.println("**** Attribute in TOE " + ((Attribute)(((Component)element).getElement(string))).ID + " " + ((Attribute)(((Component)element).getElement(string))).getInTOE());
	//					if(((Attribute)(((Component)element).getElement(string))).getInTOE()){
	//						assets.put(((Attribute)(((Component)element).getElement(string))).ID,((Component)element).getElement(string));
	//					}
	//				}
	//			}
	//		}
	//	}

}