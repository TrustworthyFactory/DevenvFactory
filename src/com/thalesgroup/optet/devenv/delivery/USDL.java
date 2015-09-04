/*
 *		OPTET Factory
 *
 *	Class USDL 1.0 15 mai 2014
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.delivery;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;


import com.thalesgroup.optet.common.exception.ProjectNotFoundException;
import com.thalesgroup.optet.devenv.Activator;
import com.thalesgroup.optet.devenv.datamodel.InternalSystemDM;
import com.thalesgroup.optet.devenv.usdl.BusinessEntity;
import com.thalesgroup.optet.devenv.usdl.DateTimeUtil;
import com.thalesgroup.optet.devenv.usdl.PriceComponent;
import com.thalesgroup.optet.devenv.usdl.PricePlan;
import com.thalesgroup.optet.devenv.usdl.Service;
import com.thalesgroup.optet.devenv.usdl.ServiceOffering;
import com.thalesgroup.optet.devenv.usdl.TechnicalDescription;
import com.thalesgroup.optet.devenv.usdl.USDLGenerator;
import com.thalesgroup.optet.securerepository.SVNWrapper;
import com.thalesgroup.optet.securerepository.impl.SVNWrapperImpl;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.internal.Workbench;
import org.osgi.framework.Bundle;

/**
 * @author F. Motte
 *
 */
public class USDL extends Dialog {

	protected Object result;
	protected Shell shlUsdlEditor;
	private Text legalNameField;
	private Text legalformField;
	private Text bussinessDesc;
	private Text businnessURL;
	private Text serviceTitleField;
	private Text serviceDescfield;
	private Text serviceAbsctractField;
	private Text serviceHomapageField;
	private Text serviceCertifField;
	private Text tdLicenseField;
	private Text tdexecplatField;
	private Text tdLanguageField;
	private Text tdsoftDepField;
	private Text tdhardreqField;
	private Text tdVersionField;
	private Text sotitleField;
	private Text soTitleDesc;
	private Text soPricePlanDescField;
	private Text soPriceComponentDesc;
	private Text soPriceField;

	private DateTime serviceCreationDate;
	private DateTime serviceModifDate;

	private BusinessEntity be;
	private Service service;
	private ServiceOffering so;

	private USDLGenerator usdlGen;
	private IFile usdlfile;
	private DateTime soValidFrom;
	private DateTime soValidThrough;
	private Combo soPricePlanTitleCombo;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public USDL(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");

		usdlGen = new USDLGenerator();	



	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {




		createContents();
		init();
		shlUsdlEditor.open();
		shlUsdlEditor.layout();
		Display display = getParent().getDisplay();
		while (!shlUsdlEditor.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}


	public  IProject getCurrentProject(){    
		ISelectionService selectionService =     
				Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();    

		ISelection selection = selectionService.getSelection();    

		IProject project = null;    
		if(selection instanceof IStructuredSelection) {    
			Object element = ((IStructuredSelection)selection).getFirstElement();    

			if (element instanceof IResource) {    
				project= ((IResource)element).getProject();    
			} else if (element instanceof PackageFragmentRootContainer) {    
				IJavaProject jProject =     
						((PackageFragmentRootContainer)element).getJavaProject();    
				project = jProject.getProject();    
			} else if (element instanceof IJavaElement) {    
				IJavaProject jProject= ((IJavaElement)element).getJavaProject();    
				project = jProject.getProject();    
			}    
		}     
		return project;    
	}

	private boolean init(){


		if (getCurrentProject() == null)
		{
			MessageDialog.openError(getParent(), "Project Selection", "Please, select a project before");
			return false;
		}


		System.out.println("getcurrent"  +getCurrentProject().getFullPath());
		usdlfile = getCurrentProject().getFile("/Optet/product.usdl");

		if (usdlfile.exists()){			

			System.out.println("usdl path file " + usdlfile.getLocation().toString());
			System.out.println("usdl path file " + usdlfile.getLocationURI().toString());
			try {
				be = usdlGen.loadBusinessEntityFromUDSL(usdlfile.getLocationURI().toString());
				if (be!=null){
					if (be.getLegalName()!=null)
						legalNameField.setText(be.getLegalName());
					if (be.getLegalForm()!=null)
						legalformField.setText(be.getLegalForm());
					if (be.getDescription()!=null)
						bussinessDesc.setText(be.getDescription());
					if (be.getUrl()!=null)
						businnessURL.setText(be.getUrl());
				}

				service = usdlGen.loadServiceFromUDSL(usdlfile.getLocationURI().toString());

				if (service!=null){
					if (service.getTitle()!=null)
						serviceTitleField.setText(service.getTitle());
					if (service.getDescription()!=null)
						serviceDescfield.setText(service.getDescription());
					if (service.getAbstractDes()!=null)
						serviceAbsctractField.setText(service.getAbstractDes());
					if (service.getUrl()!=null)
						serviceHomapageField.setText(service.getUrl());
					if (service.getCertificateURL()!=null)
						serviceCertifField.setText(service.getCertificateURL());
					if (service.getCreated()!=null){
						Calendar cal = Calendar.getInstance();
						cal.setTime(service.getCreated());
						serviceCreationDate.setDate(cal.get(Calendar.YEAR),  cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
					}
					if (service.getModified()!=null){
						Calendar cal = Calendar.getInstance();
						cal.setTime(service.getModified());
						serviceModifDate.setDate(cal.get(Calendar.YEAR),  cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
					}

					TechnicalDescription td = service.getTechnicalDescription();

					if (td!=null){
						if (td.getLicense()!=null)
							tdLicenseField.setText(td.getLicense());
						if (td.getExecutionPlatform()!=null)
							tdexecplatField.setText(td.getExecutionPlatform());
						if (td.getProgrammingLanguage()!=null)
							tdLanguageField.setText(td.getProgrammingLanguage());
						if (td.getSoftwareDependency()!=null)
							tdsoftDepField.setText(td.getSoftwareDependency());
						if (td.getHardwareRequirement()!=null)
							tdhardreqField.setText(td.getHardwareRequirement());
						if (td.getVersionNumber()!=null)
							tdVersionField.setText(td.getVersionNumber());
					}
				}
				so = usdlGen.loadServiceOfferingFromUDSL(usdlfile.getLocationURI().toString());

				if (so!=null){
					if (so.getTitle()!=null)
						sotitleField.setText(so.getTitle());
					if (so.getDescription()!=null)
						soTitleDesc.setText(so.getDescription());
					if (so.getValidFrom()!=null){
						Calendar cal = Calendar.getInstance();
						cal.setTime(so.getValidFrom());
						soValidFrom.setDate(cal.get(Calendar.YEAR),  cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
					}
					if (so.getValidThrough()!=null){
						Calendar cal = Calendar.getInstance();
						cal.setTime(so.getValidThrough());
						soValidThrough.setDate(cal.get(Calendar.YEAR),  cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
					}

					Set cles = so.getPricePlanMap().keySet();
					Iterator it = cles.iterator();
					while (it.hasNext()){
						String cle = (String) it.next(); 
						soPricePlanTitleCombo.add(cle);
						System.out.println("size" + so.getPricePlanMap().get(cle).getComponentMap().size());
					}

				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{

			be = new BusinessEntity();
			service = new Service();
			so = new ServiceOffering();
		}

		return true;
	}


	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlUsdlEditor = new Shell(getParent(), getStyle());
		shlUsdlEditor.setSize(450, 581);
		shlUsdlEditor.setText("USDL editor");

		Button btnCancel = new Button(shlUsdlEditor, SWT.NONE);
		btnCancel.setBounds(117, 516, 68, 23);
		btnCancel.setText("Cancel");

		Button btnValidate = new Button(shlUsdlEditor, SWT.NONE);
		btnValidate.setBounds(191, 516, 68, 23);
		btnValidate.setText("Validate");

		TabFolder tabFolder = new TabFolder(shlUsdlEditor, SWT.NONE);
		tabFolder.setBounds(10, 10, 424, 500);

		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("BusinessEntity");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(composite);

		Label lblLegalName = new Label(composite, SWT.NONE);
		lblLegalName.setBounds(10, 10, 103, 13);
		lblLegalName.setText("Legal Name");

		Label lblLegalForm = new Label(composite, SWT.NONE);
		lblLegalForm.setText("Legal Form");
		lblLegalForm.setBounds(10, 40, 103, 13);

		Label lblDescription = new Label(composite, SWT.NONE);
		lblDescription.setText("Description");
		lblDescription.setBounds(10, 73, 103, 13);

		Label lblHomepage = new Label(composite, SWT.NONE);
		lblHomepage.setText("Homepage");
		lblHomepage.setBounds(10, 181, 103, 13);

		legalNameField = new Text(composite, SWT.BORDER);
		legalNameField.setBounds(119, 10, 287, 19);

		legalformField = new Text(composite, SWT.BORDER);
		legalformField.setBounds(119, 40, 287, 19);

		bussinessDesc = new Text(composite, SWT.BORDER);
		bussinessDesc.setBounds(119, 70, 287, 100);

		businnessURL = new Text(composite, SWT.BORDER);
		businnessURL.setBounds(119, 181, 287, 19);

		TabItem tbtmNewItem_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("Service");

		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem_1.setControl(composite_1);

		Label lblTitle = new Label(composite_1, SWT.NONE);
		lblTitle.setText("Title");
		lblTitle.setBounds(10, 10, 103, 13);

		serviceTitleField = new Text(composite_1, SWT.BORDER);
		serviceTitleField.setBounds(119, 7, 287, 19);


		serviceCreationDate = new DateTime(composite_1, SWT.DATE | SWT.DROP_DOWN);
		serviceCreationDate.setBounds(56, 287, 103, 19);

		Label label = new Label(composite_1, SWT.NONE);
		label.setText("Description");
		label.setBounds(10, 38, 103, 13);

		serviceDescfield = new Text(composite_1, SWT.BORDER);
		serviceDescfield.setBounds(119, 35, 287, 55);

		Label lblAbstract = new Label(composite_1, SWT.NONE);
		lblAbstract.setText("Abstract");
		lblAbstract.setBounds(10, 103, 103, 13);

		serviceAbsctractField = new Text(composite_1, SWT.BORDER);
		serviceAbsctractField.setBounds(119, 100, 287, 55);

		Label label_1 = new Label(composite_1, SWT.NONE);
		label_1.setText("Homepage");
		label_1.setBounds(10, 164, 103, 13);

		serviceHomapageField = new Text(composite_1, SWT.BORDER);
		serviceHomapageField.setBounds(119, 164, 287, 19);

		Label lblOptetCertificate = new Label(composite_1, SWT.NONE);
		lblOptetCertificate.setText("Optet Certificate");
		lblOptetCertificate.setBounds(10, 193, 103, 13);

		serviceCertifField = new Text(composite_1, SWT.BORDER);
		serviceCertifField.setBounds(119, 193, 287, 19);

		serviceModifDate = new DateTime(composite_1, SWT.DROP_DOWN);
		serviceModifDate.setBounds(225, 287, 103, 19);

		Label lblCreationDate = new Label(composite_1, SWT.NONE);
		lblCreationDate.setText("Creation date");
		lblCreationDate.setBounds(56, 268, 103, 13);

		Label lblModificationDate = new Label(composite_1, SWT.NONE);
		lblModificationDate.setText("Modification date");
		lblModificationDate.setBounds(225, 268, 103, 13);

		TabItem tbtmNewItem_2 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_2.setText("TechnicalDescription");

		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem_2.setControl(composite_2);

		Label lblLicense = new Label(composite_2, SWT.NONE);
		lblLicense.setText("License");
		lblLicense.setBounds(10, 10, 103, 13);

		tdLicenseField = new Text(composite_2, SWT.BORDER);
		tdLicenseField.setBounds(134, 10, 272, 19);

		Label lblExecutionPlateform = new Label(composite_2, SWT.NONE);
		lblExecutionPlateform.setText("Execution plateform");
		lblExecutionPlateform.setBounds(10, 45, 103, 13);

		tdexecplatField = new Text(composite_2, SWT.BORDER);
		tdexecplatField.setBounds(134, 45, 272, 19);

		Label lblProgrammingLanguage = new Label(composite_2, SWT.NONE);
		lblProgrammingLanguage.setText("Programming Language");
		lblProgrammingLanguage.setBounds(10, 81, 118, 13);

		tdLanguageField = new Text(composite_2, SWT.BORDER);
		tdLanguageField.setBounds(134, 81, 272, 19);

		Label lblSoftwareDependencies = new Label(composite_2, SWT.NONE);
		lblSoftwareDependencies.setText("Software dependencies");
		lblSoftwareDependencies.setBounds(10, 115, 118, 13);

		tdsoftDepField = new Text(composite_2, SWT.BORDER);
		tdsoftDepField.setBounds(134, 115, 272, 19);

		Label lblHardwareRequirements = new Label(composite_2, SWT.NONE);
		lblHardwareRequirements.setText("Hardware Requirements");
		lblHardwareRequirements.setBounds(10, 147, 118, 13);

		tdhardreqField = new Text(composite_2, SWT.BORDER);
		tdhardreqField.setBounds(134, 147, 272, 19);

		Label lblVersionNumber = new Label(composite_2, SWT.NONE);
		lblVersionNumber.setText("Version number");
		lblVersionNumber.setBounds(10, 178, 103, 13);

		tdVersionField = new Text(composite_2, SWT.BORDER);
		tdVersionField.setBounds(134, 178, 272, 19);

		TabItem tbtmServiceoffering = new TabItem(tabFolder, SWT.NONE);
		tbtmServiceoffering.setText("ServiceOffering");

		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmServiceoffering.setControl(composite_3);

		Label label_2 = new Label(composite_3, SWT.NONE);
		label_2.setText("Title");
		label_2.setBounds(10, 13, 103, 13);

		sotitleField = new Text(composite_3, SWT.BORDER);
		sotitleField.setBounds(119, 10, 287, 19);

		soTitleDesc = new Text(composite_3, SWT.BORDER);
		soTitleDesc.setBounds(119, 38, 287, 55);

		Label label_3 = new Label(composite_3, SWT.NONE);
		label_3.setText("Description");
		label_3.setBounds(10, 41, 103, 13);

		Label lblValidFrom = new Label(composite_3, SWT.NONE);
		lblValidFrom.setText("Valid from");
		lblValidFrom.setBounds(10, 99, 103, 13);

		soValidFrom = new DateTime(composite_3, SWT.DROP_DOWN);
		soValidFrom.setBounds(10, 118, 103, 19);

		Label lblValidThrough = new Label(composite_3, SWT.NONE);
		lblValidThrough.setText("Valid through");
		lblValidThrough.setBounds(179, 99, 103, 13);

		soValidThrough = new DateTime(composite_3, SWT.DROP_DOWN);
		soValidThrough.setBounds(179, 118, 103, 19);

		Group grpPriceplan = new Group(composite_3, SWT.NONE);
		grpPriceplan.setText("PricePlan");
		grpPriceplan.setBounds(10, 142, 396, 138);

		soPricePlanTitleCombo = new Combo(grpPriceplan, SWT.NONE);
		soPricePlanTitleCombo.setBounds(110, 23, 276, 21);

		Label label_4 = new Label(grpPriceplan, SWT.NONE);
		label_4.setText("Title");
		label_4.setBounds(10, 23, 78, 13);

		soPricePlanDescField = new Text(grpPriceplan, SWT.BORDER);
		soPricePlanDescField.setBounds(110, 50, 276, 51);

		Label label_5 = new Label(grpPriceplan, SWT.NONE);
		label_5.setText("Description");
		label_5.setBounds(10, 53, 78, 13);

		Button btnRemovePricePlan = new Button(grpPriceplan, SWT.NONE);
		btnRemovePricePlan.setBounds(318, 107, 68, 23);
		btnRemovePricePlan.setText("Remove");

		Button btnNewPricePlan = new Button(grpPriceplan, SWT.NONE);
		btnNewPricePlan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewPricePlan.setText("New");
		btnNewPricePlan.setBounds(241, 107, 68, 23);

		Group grpPricecomponent = new Group(composite_3, SWT.NONE);
		grpPricecomponent.setText("PriceComponent");
		grpPricecomponent.setBounds(10, 286, 396, 178);

		Label label_6 = new Label(grpPricecomponent, SWT.NONE);
		label_6.setText("Title");
		label_6.setBounds(10, 22, 78, 13);

		final Combo soPricecomponentCombo = new Combo(grpPricecomponent, SWT.NONE);
		soPricecomponentCombo.setBounds(110, 22, 276, 21);

		soPriceComponentDesc = new Text(grpPricecomponent, SWT.BORDER);
		soPriceComponentDesc.setBounds(110, 49, 276, 51);

		Label label_7 = new Label(grpPricecomponent, SWT.NONE);
		label_7.setText("Description");
		label_7.setBounds(10, 52, 78, 13);

		Button btnnewPriceComponent = new Button(grpPricecomponent, SWT.NONE);
		btnnewPriceComponent.setText("New");
		btnnewPriceComponent.setBounds(241, 145, 68, 23);

		Button btnRemovePriceComponent = new Button(grpPricecomponent, SWT.NONE);
		btnRemovePriceComponent.setText("Remove");
		btnRemovePriceComponent.setBounds(318, 145, 68, 23);

		final Combo soPriceDeviseCombo = new Combo(grpPricecomponent, SWT.NONE);
		soPriceDeviseCombo.setBounds(331, 106, 55, 21);
		String[] ITEMS = { "EUR", "USD"};
		soPriceDeviseCombo.setItems(ITEMS);

		Label lblPrice = new Label(grpPricecomponent, SWT.NONE);
		lblPrice.setText("Price");
		lblPrice.setBounds(10, 109, 78, 13);

		soPriceField = new Text(grpPricecomponent, SWT.BORDER);
		soPriceField.setBounds(110, 106, 203, 19);

		Button btnSaveToSVN = new Button(shlUsdlEditor, SWT.NONE);
		btnSaveToSVN.setBounds(265, 516, 169, 23);
		btnSaveToSVN.setText("Save to the secure repository");

		/************************************************/


		legalNameField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				be.setLegalName(((Text)arg0.getSource()).getText());
			}
		});

		legalformField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				be.setLegalForm(((Text)arg0.getSource()).getText());
			}
		});

		bussinessDesc.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				be.setDescription(((Text)arg0.getSource()).getText());
			}
		});

		businnessURL.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				be.setUrl(((Text)arg0.getSource()).getText());
			}
		});

		/******************************************************/

		serviceTitleField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				service.setTitle(((Text)arg0.getSource()).getText());
			}
		});

		serviceDescfield.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				service.setDescription(((Text)arg0.getSource()).getText());
			}
		});

		serviceAbsctractField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				service.setAbstractDes(((Text)arg0.getSource()).getText());
			}
		});

		serviceHomapageField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				service.setUrl(((Text)arg0.getSource()).getText());
			}
		});

		serviceCertifField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				service.setCertificateURL(((Text)arg0.getSource()).getText());
			}
		});



		serviceCreationDate.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub

				service.setCreated(getDate((DateTime)arg0.getSource()));
			}
		});
		serviceModifDate.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub

				service.setModified(getDate(((DateTime)arg0.getSource())));
			}
		});

		/**************************************/

		tdLicenseField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				service.getTechnicalDescription().setLicense(((Text)arg0.getSource()).getText());
			}
		});

		tdexecplatField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				service.getTechnicalDescription().setExecutionPlatform(((Text)arg0.getSource()).getText());
			}
		});

		tdLanguageField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				service.getTechnicalDescription().setProgrammingLanguage(((Text)arg0.getSource()).getText());
			}
		});

		tdsoftDepField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				service.getTechnicalDescription().setSoftwareDependency(((Text)arg0.getSource()).getText());
			}
		});

		tdhardreqField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				service.getTechnicalDescription().setHardwareRequirement(((Text)arg0.getSource()).getText());
			}
		});

		tdVersionField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				service.getTechnicalDescription().setVersionNumber(((Text)arg0.getSource()).getText());
			}
		});


		/***********************************************************************/

		sotitleField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				so.setTitle(((Text)arg0.getSource()).getText());
			}
		});

		soTitleDesc.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				so.setDescription(((Text)arg0.getSource()).getText());
			}
		});

		soValidFrom.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub

				so.setValidFrom(getDate(((DateTime)arg0.getSource())));
				System.out.println("set modif " + so.getValidFrom());
			}
		});
		soValidThrough.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub

				so.setValidThrough(getDate(((DateTime)arg0.getSource())));
				System.out.println("set Through " + so.getValidThrough());
			}
		});

		soPricePlanTitleCombo.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				// TODO Auto-generated method stub
				switch (e.type) {
				case SWT.Selection:
					System.out.println("Button new pressed");
					//					PricePlan pp = new PricePlan();
					//					pp.setTitle(soPricePlanTitleCombo.getText());
					//					pp.setDescription(soPricePlanDescField.getText());
					soPricePlanDescField.setText(so.getPricePlanMap().get(soPricePlanTitleCombo.getText()).getDescription());
					//so.getPricePlanMap().get(soPricePlanTitleCombo.getText()).getComponentMap().keySet();
					soPricecomponentCombo.removeAll();
					soPriceComponentDesc.setText("");
					soPriceField.setText("");
					soPriceDeviseCombo.deselectAll();

					for (Iterator iterator = so.getPricePlanMap().get(soPricePlanTitleCombo.getText()).getComponentMap().keySet().iterator(); iterator
							.hasNext();) {
						String type = (String) iterator.next();
						soPricecomponentCombo.add(type);
					}
					break;
				}
			}

		});

		soPricePlanDescField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				if (so.getPricePlanMap().containsKey(soPricePlanTitleCombo.getText())){
					so.getPricePlanMap().get(soPricePlanTitleCombo.getText()).setDescription(((Text)arg0.getSource()).getText());
				}			}
		});	




		btnNewPricePlan.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				// TODO Auto-generated method stub
				switch (e.type) {
				case SWT.Selection:
					System.out.println("Button new pressed");
					PricePlan pp = new PricePlan();
					pp.setTitle(soPricePlanTitleCombo.getText());
					pp.setDescription(soPricePlanDescField.getText());
					so.getPricePlanMap().put(soPricePlanTitleCombo.getText(), pp);
					soPricePlanTitleCombo.add(soPricePlanTitleCombo.getText());

					soPricecomponentCombo.setText(null);
					soPriceComponentDesc.setText(null);
					soPriceDeviseCombo.setText(null);
					soPriceField.setText(null);


					break;
				}
			}
		});



		btnRemovePricePlan.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				// TODO Auto-generated method stub
				switch (e.type) {
				case SWT.Selection:
					System.out.println("Button remove pressed");
					so.getPricePlanMap().remove(soPricePlanTitleCombo.getText());
					soPricePlanTitleCombo.remove(soPricePlanTitleCombo.getText());
					soPricePlanTitleCombo.setText(null);
					soPricePlanDescField.setText(null);

					soPricecomponentCombo.setText(null);
					soPriceComponentDesc.setText(null);
					soPriceDeviseCombo.setText(null);
					soPriceField.setText(null);
					break;
				}
			}
		});

		/****************************************************/

		soPricecomponentCombo.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				// TODO Auto-generated method stub
				switch (e.type) {
				case SWT.Selection:
					System.out.println("Button new pressed");



					soPriceDeviseCombo.setText(so.getPricePlanMap().get(soPricePlanTitleCombo.getText()).getComponentMap().get(soPricecomponentCombo.getText()).getCurrency());
					soPriceComponentDesc.setText(so.getPricePlanMap().get(soPricePlanTitleCombo.getText()).getComponentMap().get(soPricecomponentCombo.getText()).getDescription());
					soPriceField.setText(so.getPricePlanMap().get(soPricePlanTitleCombo.getText()).getComponentMap().get(soPricecomponentCombo.getText()).getCurrencyValue());
					break;
				}
			}

		});

		soPriceComponentDesc.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				if (so.getPricePlanMap().get(soPricePlanTitleCombo.getText()).getComponentMap().containsKey(soPricePlanTitleCombo.getText())){
					so.getPricePlanMap().get(soPricePlanTitleCombo.getText()).getComponentMap().get(soPricePlanTitleCombo.getText()).setDescription(((Text)arg0.getSource()).getText());
				}			}
		});	


		soPriceDeviseCombo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				if (so.getPricePlanMap().get(soPricePlanTitleCombo.getText()).getComponentMap().containsKey(soPricePlanTitleCombo.getText())){
					so.getPricePlanMap().get(soPricePlanTitleCombo.getText()).getComponentMap().get(soPricePlanTitleCombo.getText()).setCurrency(((Text)arg0.getSource()).getText());
				}			}
		});	

		soPriceField.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				if (so.getPricePlanMap().get(soPricePlanTitleCombo.getText()).getComponentMap().containsKey(soPricePlanTitleCombo.getText())){
					so.getPricePlanMap().get(soPricePlanTitleCombo.getText()).getComponentMap().get(soPricePlanTitleCombo.getText()).setCurrencyValue(((Text)arg0.getSource()).getText());
				}			}
		});	


		btnnewPriceComponent.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				// TODO Auto-generated method stub
				switch (e.type) {
				case SWT.Selection:
					System.out.println("Button new pressed");
					PriceComponent pc = new PriceComponent();
					pc.setTitle(soPricecomponentCombo.getText());
					pc.setDescription(soPriceComponentDesc.getText());
					pc.setCurrency(soPriceDeviseCombo.getText());
					pc.setCurrencyValue(soPriceField.getText());

					so.getPricePlanMap().get(soPricePlanTitleCombo.getText()).getComponentMap().put(soPricecomponentCombo.getText(), pc);
					soPricecomponentCombo.add(soPricecomponentCombo.getText());
					break;
				}
			}
		});

		btnRemovePriceComponent.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				// TODO Auto-generated method stub
				switch (e.type) {
				case SWT.Selection:
					System.out.println("Button remove pressed");
					so.getPricePlanMap().get(soPricePlanTitleCombo.getText()).getComponentMap().remove(soPricecomponentCombo.getText());
					soPricecomponentCombo.remove(soPricecomponentCombo.getText());
					soPricecomponentCombo.setText(null);
					soPriceComponentDesc.setText(null);
					soPriceDeviseCombo.setText(null);
					soPriceField.setText(null);
					break;
				}
			}
		});



		/***************************************************/
		btnSaveToSVN.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				// TODO Auto-generated method stub
				switch (e.type) {
				case SWT.Selection:


					System.out.println("addBuisinessEntity");
					usdlGen.addBuisinessEntity(be);

					System.out.println("addService");

					usdlGen.addService(service);
					System.out.println("addServiceOffering");


					//					Set cles = so.getPricePlanMap().keySet();
					//					Iterator it = cles.iterator();
					//					while (it.hasNext()){
					//					   Object cle = it.next(); // tu peux typer plus finement ici
					//					   PricePlan valeur = so.getPricePlanMap().get(cle); // tu peux typer plus finement ici
					//					   valeur.getComponentMap().clear();
					//					}

					usdlGen.addServiceOffering(so);



					//create an empty InputStream
					//PipedInputStream in = new PipedInputStream();

					//create an OutputStream with the InputStream from above as input
					//PipedOutputStream out;
					//					try {
					//out = new PipedOutputStream(in);
					// Display the usdl service instance just created
					System.out.println("pipedout");
					//PipedOutputStream outstream = new PipedOutputStream();
					ByteArrayOutputStream  outstream = new ByteArrayOutputStream();
					System.out.println("display");
					usdlGen.displayUSDL(outstream, "OWL");


					try {
						//ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(outstream.toString().getBytes()));

						byte[] data = outstream.toByteArray();
						ByteArrayInputStream ois = new ByteArrayInputStream(data);
						usdlfile.setContents(ois, true, true, null);
					} catch (CoreException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					//usdlGen.displayUSDL(System.out, "OWL");
					//usdlfile.setContents(in, true, true, null);

					//					} catch (IOException e1) {
					//						// TODO Auto-generated catch block
					//						e1.printStackTrace();
					//					} catch (CoreException e1) {
					//						// TODO Auto-generated catch block
					//						e1.printStackTrace();
					//					}

					Properties prop = new Properties();
					String projectID = null;


					try {
						getCurrentProject().getFile("/Optet/Optet.properties");
						
						prop.load(getCurrentProject().getFile("/Optet/Optet.properties").getContents());
						projectID = prop.getProperty("svn.project.selected");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (CoreException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					SVNWrapper svn = new SVNWrapperImpl();
					List<IFile> certificatToCommit = new ArrayList<>();
					certificatToCommit.add(usdlfile);
					try {
						svn.setFilesToSVN(projectID, certificatToCommit);
					} catch (ProjectNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

				shlUsdlEditor.close();
			}
		});

		btnValidate.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				// TODO Auto-generated method stub
				switch (e.type) {
				case SWT.Selection:


					System.out.println("addBuisinessEntity");
					usdlGen.addBuisinessEntity(be);
					//dumpBuisinessEntity(be);
					System.out.println("addService");
					//dumpService(service);
					usdlGen.addService(service);
					//System.out.println("addServiceOffering");
					dumpServiceOffering(so);

					//					Set cles = so.getPricePlanMap().keySet();
					//					Iterator it = cles.iterator();
					//					while (it.hasNext()){
					//					   Object cle = it.next(); // tu peux typer plus finement ici
					//					   PricePlan valeur = so.getPricePlanMap().get(cle); // tu peux typer plus finement ici
					//					   valeur.getComponentMap().clear();
					//					}

					usdlGen.addServiceOffering(so);



					//create an empty InputStream
					//PipedInputStream in = new PipedInputStream();

					//create an OutputStream with the InputStream from above as input
					//PipedOutputStream out;
					//					try {
					//out = new PipedOutputStream(in);
					// Display the usdl service instance just created
					System.out.println("pipedout");
					//PipedOutputStream outstream = new PipedOutputStream();
					ByteArrayOutputStream  outstream = new ByteArrayOutputStream();
					System.out.println("display");
					usdlGen.displayUSDL(outstream, "OWL");


					try {
						//ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(outstream.toString().getBytes()));

						byte[] data = outstream.toByteArray();
						ByteArrayInputStream ois = new ByteArrayInputStream(data);
						if (!usdlfile.exists()){
							usdlfile.create(ois, false, null);
						}else{
							usdlfile.setContents(ois, true, true, null);
						}
					} catch (CoreException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					//usdlGen.displayUSDL(System.out, "OWL");
					//usdlfile.setContents(in, true, true, null);

					//					} catch (IOException e1) {
					//						// TODO Auto-generated catch block
					//						e1.printStackTrace();
					//					} catch (CoreException e1) {
					//						// TODO Auto-generated catch block
					//						e1.printStackTrace();
					//					}



				}

				shlUsdlEditor.close();
			}


			private void dumpService(Service service) {
				// TODO Auto-generated method stub
				System.out.println("service.getID() : " + service.getID());
				System.out.println("service.getTitle() : " + service.getTitle());
				System.out.println("service.getDescription() : " + service.getDescription());
				System.out.println("service.getAbstractDes() : " + service.getAbstractDes());
				System.out.println("service.getCertificateURL() : " + service.getCertificateURL());
				System.out.println("service.getHasClassification() : " + service.getHasClassification());
				System.out.println("service.getUrl() : " + service.getUrl());
				System.out.println("service.getCreated() : " + service.getCreated());
				System.out.println("service.getModified() : " + service.getModified());
				System.out.println("service.getTechnicalDescription().getID() : " + service.getTechnicalDescription().getID());
				System.out.println("service.getTechnicalDescription().getExecutionPlatform() : " + service.getTechnicalDescription().getExecutionPlatform());
				System.out.println("service.getTechnicalDescription().getHardwareRequirement() : " + service.getTechnicalDescription().getHardwareRequirement());
				System.out.println("service.getTechnicalDescription().getLicense() : " + service.getTechnicalDescription().getLicense());
				System.out.println("service.getTechnicalDescription().getProgrammingLanguage() : " + service.getTechnicalDescription().getProgrammingLanguage());
				System.out.println("service.getTechnicalDescription().getSoftwareDependency() : " + service.getTechnicalDescription().getSoftwareDependency());
				System.out.println("service.getTechnicalDescription().getVersionNumber() : " + service.getTechnicalDescription().getVersionNumber());

			}





			private void dumpBuisinessEntity(BusinessEntity be) {
				// TODO Auto-generated method stub
				System.out.println("be.getID() : " + be.getID());
				System.out.println("be.getDescription() : " + be.getDescription());
				System.out.println("be.getLegalForm() : " + be.getLegalForm());
				System.out.println("be.getLegalName() : " + be.getLegalName());
				System.out.println("be.getUrl() : " + be.getUrl());
			}





			private void dumpServiceOffering(ServiceOffering so) {
				// TODO Auto-generated method stub

				System.out.println("so.getID() :" + so.getID());
				System.out.println("so.getTitle() :" + so.getTitle());
				System.out.println("so.getDescription() :" + so.getDescription());
				System.out.println("so.getValidFrom() :" + so.getValidFrom());
				System.out.println("so.getValidThrough() :" + so.getValidThrough());


				Set cles = so.getPricePlanMap().keySet();
				Iterator it = cles.iterator();
				while (it.hasNext()){
					Object cle = it.next(); // tu peux typer plus finement ici
					PricePlan pp = so.getPricePlanMap().get(cle); // tu peux typer plus finement ici
					System.out.println("pp.getID() :" + pp.getID());
					System.out.println("pp.getTitle() :" + pp.getTitle());
					System.out.println("pp.getDescription() :" + pp.getDescription());

					Set cles2 = pp.getComponentMap().keySet();
					Iterator it2 = cles2.iterator();
					while (it2.hasNext()){
						Object cle2 = it2.next(); // tu peux typer plus finement ici
						PriceComponent pc = pp.getComponentMap().get(cle2); // tu peux typer plus finement ici
						System.out.println("pc.getID() :" + pc.getID());
						System.out.println("pc.getTitle() :" + pc.getTitle());
						System.out.println("pc.getDescription() :" + pc.getDescription());
						System.out.println("pc.getCurrency() :" + pc.getCurrency());
						System.out.println("pc.getCurrencyValue() :" + pc.getCurrencyValue());
						System.out.println("pc.getUnitOfMeasure() :" + pc.getUnitOfMeasure());
					}

				}
			}

		});




		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {

				shlUsdlEditor.close();
			}
		});
	}


	private Date getDate(DateTime dateTime) 
	{

		Calendar calendar = Calendar.getInstance();
		calendar.set(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay(), 0, 0, 0);
		Date date = calendar.getTime();
		return date;
	}
}
