/*
 *		OPTET Factory
 *
 *	Class PropertySpecification 1.0 28 oct. 2013
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.propertyspecification;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.List;

//import com.thalesgroup.optet.devenv.datamodel.Control;
//import com.thalesgroup.optet.devenv.datamodel.Element;
import com.thalesgroup.dtwc.Asset;
import com.thalesgroup.dtwc.Context;
import com.thalesgroup.dtwc.Control;
import com.thalesgroup.dtwc.DTWC;
import com.thalesgroup.dtwc.Metric;
import com.thalesgroup.dtwc.TWAttribute;
import com.thalesgroup.dtwc.TWProperty;
import com.thalesgroup.dtwc.TWPropertySpecification;
import com.thalesgroup.dtwc.impl.AssetImpl;
import com.thalesgroup.dtwc.impl.CertificateWrapper;
import com.thalesgroup.dtwc.impl.TWPropertySpecificationImpl;
import com.thalesgroup.optet.devenv.datamodel.InternalSystemDM;
//import com.thalesgroup.optet.devenv.datamodel.Metric;
//import com.thalesgroup.optet.devenv.datamodel.TWAttribute;
//import com.thalesgroup.optet.devenv.datamodel.TWProperty;
//import com.thalesgroup.optet.devenv.datamodel.TWPropertySpecification;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import com.thalesgroup.optet.devenv.Activator;

import org.osgi.framework.Bundle;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 * @author F. Motte
 *
 */
public class PropertySpecificationView extends Dialog {

	protected Object result;
	protected Shell shlPropertyspecification;
	private Text twPropertyID;
	private Text controlID;
	private InternalSystemDM data;


	private ComboViewer twAttributeComboViewer;
	//private ComboViewer metricComboViewer;
	private ComboViewer assetComboViewer; 
	private List twPropSpecList;

	private CertificateWrapper cw;
	private IFile dtwcFile;
	private DTWC dtwc;
	
	
	private Map<String, Control> controls;
	private Map<String, Metric> metrics;
	private Map<String, TWAttribute> twAttributes;
	private Map<String, TWProperty> twProperties;
	private Map<String, TWPropertySpecification> twPropertySpecifications;

	//String[] ITEMS_TWATTRIBUTE = {"Dependability","Complexity","Security"};
	String[] ITEMS_TWATTRIBUTE = {"Security","Accountability","Auditability/Tracability","Confidentiality","Integrity","Safety","Non-Repudiation","Openness","Reusability","Change Cycle/Stability","Completeness","Compliance with standards","Compliance with regulations","Compliance with User expectations","Cost","Privacy","Data Integrity","Data Reliability","Data Timeless","Data Validity","Accuracy","Availability","Flexibility/Robustness","Reliability","Scalability","Maintainability","Throughput","Response Time","FunctionalCorrectness","Composability","Software Complexity"};

//	String[] ITEMS_PERFORMANCE = { "MinimalOverhead" };
//	String[] ITEMS_SECURITY = { "SecurityMetric" };
//	String[] ITEMS_COMPLEXITY = { "SoftwareComplexityMetric" };
//	String[] ITEMS_MAINTAINABILITY = { "MaintainabilityMetric"};
//	String[] ITEMS_RELIABILITY = { "ReliabilityMetric" };
//	String[] ITEMS_AVAILABILITY = { "AvailabilityMetric"};
//
//	String[] ITEMS_ACCOUNTABILITY = {"AccountabilityMetric" };
//	String[] ITEMS_AUDITABILITY_TRACEABILITY = {"Auditability_TraceabilityMetric" };
//	String[] ITEMS_CONFIDENTIALITY = {"ConfidentialityMetric" };
//	String[] ITEMS_INTEGRITY = {"IntegrityMetric" };
//	String[] ITEMS_SAFETY = {"SafetyMetric" };
//	String[] ITEMS_NON_REPUDIATION = {"Non-repudiationMetric" };
//	String[] ITEMS_OPENNESS = {"OpennessMetric" };
//	String[] ITEMS_REUSABILITY = {"ReusabilityMetric" };
//	String[] ITEMS_CHANGECYCLE_STABILITY = {"ChangeCycle_StabilityMetric" };
//	String[] ITEMS_COMPLETENESS = {"CompletenessMetric" };
//	String[] ITEMS_COMPIANCEWITHSTANDARDS = {"ComplianceWithStandardsMetric" };
//	String[] ITEMS_COMPLIANCEWITHREGULATIONs = {"ComplianceWithRegulationsMetric" };
//	String[] ITEMS_COMPLIANCEWITHUSEREXPECTATIONS = {"ComplianceWithUserExpectationsMetric" };
//	String[] ITEMS_COST = {"CostMetric" };
//	String[] ITEMS_DATAINTEGRITY = {"DataIntegrityMetric" };
//	String[] ITEMS_DATARELIABILITY = {"DataReliabilityMetric" };
//	String[] ITEMS_DATATIMELESS = {"DataTimelessMetric" };
//	String[] ITEMS_DATAVALIDITY = {"DataValidityMetric" };
//	String[] ITEMS_ACCURACY = {"AccuracyMetric" };
//	String[] ITEMS_FLEXIBILITY_ROBUSTNESS = {"Flexibility_RobustnessMetric" };
//	String[] ITEMS_SCALABILITY = {"ScalabilityMetric" };
//	String[] ITEMS_THROUGHPUT = {"ThroughputMetric" };
//	String[] ITEMS_RESPONSETIME = {"ResponseTimeMetric" };
//	String[] ITEMS_FUNCTIONALCORRESCTNESS = {"FunctionalCorrectnessMetric" };
//	String[] ITEMS_COMPOSABILITY = {"ComposabilityMetric" };
//	String[] ITEMS_PRIVACY = {"PrivacyMetric" };
//	
	
	
	
	String[] ITEMS_CONTROLS = {"Control"};
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public PropertySpecificationView(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
		data = InternalSystemDM.getInternalSystemDM();
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();

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

		
		Collection<AssetImpl> elements = dtwc.getSystemDescription().getAssetList();

		for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
			AssetImpl element = (AssetImpl) iterator.next();
			assetComboViewer.add(element.getId());
		}
		
		Collection<TWPropertySpecificationImpl> twpsCol = dtwc.getTWPropertySpecifications();
		for (Iterator iterator = twpsCol.iterator(); iterator.hasNext();) {
			TWPropertySpecificationImpl twPropertySpecificationImpl = (TWPropertySpecificationImpl) iterator
					.next();
			twPropSpecList.add(twPropertySpecificationImpl.getId());
		}
		
		
		controls = new HashMap<>();
		metrics = new HashMap<>();
		twAttributes = new HashMap<>();
		twProperties = new HashMap<>();
		twPropertySpecifications = new HashMap<>();

		shlPropertyspecification.open();
		shlPropertyspecification.layout();
		Display display = getParent().getDisplay();
		while (!shlPropertyspecification.isDisposed()) {
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
		shlPropertyspecification = new Shell(getParent(), getStyle());
		shlPropertyspecification.setSize(685, 327);
		shlPropertyspecification.setText("TW Property Specification");

		Group grpTwproperties = new Group(shlPropertyspecification, SWT.NONE);
		grpTwproperties.setText("TWProperty");
		grpTwproperties.setBounds(10, 0, 330, 155);

		Label lblId = new Label(grpTwproperties, SWT.NONE);
		lblId.setBounds(10, 30, 49, 13);
		lblId.setText("ID");

		Label lblAsset = new Label(grpTwproperties, SWT.NONE);
		lblAsset.setBounds(10, 60, 49, 13);
		lblAsset.setText("Asset");

		Label lblTwattribute = new Label(grpTwproperties, SWT.NONE);
		lblTwattribute.setBounds(10, 90, 70, 13);
		lblTwattribute.setText("TWAttribute");

//		Label lblMetric = new Label(grpTwproperties, SWT.NONE);
//		lblMetric.setBounds(10, 120, 49, 13);
//		lblMetric.setText("Context");

		twPropertyID = new Text(grpTwproperties, SWT.BORDER);
		twPropertyID.setBounds(100, 24, 200, 19);

		assetComboViewer = new ComboViewer(grpTwproperties, SWT.NONE);
		Combo combo = assetComboViewer.getCombo();
		combo.setBounds(100, 52, 200, 21);

		twAttributeComboViewer = new ComboViewer(grpTwproperties, SWT.NONE);
		twAttributeComboViewer.add(ITEMS_TWATTRIBUTE);
		Combo combo_1 = twAttributeComboViewer.getCombo();
		combo_1.setBounds(100, 82, 200, 21);
		twAttributeComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				String selectedObject = (String) selection.getFirstElement();
				System.out.println("selectedObject " + selectedObject );
//				metricComboViewer.getCombo().removeAll();
//				if (selectedObject.equals("SoftwareComplexity")){
//					metricComboViewer.add(ITEMS_COMPLEXITY);
//				}else if (selectedObject.equals("Security")){
//					metricComboViewer.add(ITEMS_SECURITY);
//				}else if (selectedObject.equals("Maintainability")){
//					metricComboViewer.add(ITEMS_MAINTAINABILITY);
//				}else if (selectedObject.equals("Reliability")){
//					metricComboViewer.add(ITEMS_RELIABILITY);
//				}else if (selectedObject.equals("Availability")){
//					metricComboViewer.add(ITEMS_AVAILABILITY);
//				}else if (selectedObject.equals("Accountability")){
//					metricComboViewer.add(ITEMS_ACCOUNTABILITY);
//				}else if (selectedObject.equals("Auditability_Traceability")){
//					metricComboViewer.add(ITEMS_AUDITABILITY_TRACEABILITY);
//				}else if (selectedObject.equals("Confidentiality")){
//					metricComboViewer.add(ITEMS_CONFIDENTIALITY);
//				}else if (selectedObject.equals("Integrity")){
//					metricComboViewer.add(ITEMS_INTEGRITY);
//				}else if (selectedObject.equals("Safety")){
//					metricComboViewer.add(ITEMS_SAFETY);
//				}else if (selectedObject.equals("Non-repudiation")){
//					metricComboViewer.add(ITEMS_NON_REPUDIATION);
//				}else if (selectedObject.equals("Openness")){
//					metricComboViewer.add(ITEMS_OPENNESS);
//				}else if (selectedObject.equals("Reusability")){
//					metricComboViewer.add(ITEMS_REUSABILITY);
//				}else if (selectedObject.equals("ChangeCycle_Stability")){
//					metricComboViewer.add(ITEMS_CHANGECYCLE_STABILITY);
//				}else if (selectedObject.equals("Completeness")){
//					metricComboViewer.add(ITEMS_COMPLETENESS);
//				}else if (selectedObject.equals("ComplianceWithStandards")){
//					metricComboViewer.add(ITEMS_COMPIANCEWITHSTANDARDS);
//				}else if (selectedObject.equals("ComplianceWithRegulations")){
//					metricComboViewer.add(ITEMS_COMPLIANCEWITHREGULATIONs);
//				}else if (selectedObject.equals("ComplianceWithUserExpectations")){
//					metricComboViewer.add(ITEMS_COMPLIANCEWITHUSEREXPECTATIONS);
//				}else if (selectedObject.equals("Cost")){
//					metricComboViewer.add(ITEMS_COST);
//				}else if (selectedObject.equals("DataIntegrity")){
//					metricComboViewer.add(ITEMS_DATAINTEGRITY);
//				}else if (selectedObject.equals("DataReliability")){
//					metricComboViewer.add(ITEMS_DATARELIABILITY);
//				}else if (selectedObject.equals("DataTimeless")){
//					metricComboViewer.add(ITEMS_DATATIMELESS);
//				}else if (selectedObject.equals("DataValidity")){
//					metricComboViewer.add(ITEMS_DATAVALIDITY);
//				}else if (selectedObject.equals("Accuracy")){
//					metricComboViewer.add(ITEMS_ACCURACY);
//				}else if (selectedObject.equals("Flexibility_Robustness")){
//					metricComboViewer.add(ITEMS_FLEXIBILITY_ROBUSTNESS);
//				}else if (selectedObject.equals("Scalability")){
//					metricComboViewer.add(ITEMS_SCALABILITY);
//				}else if (selectedObject.equals("Throughput")){
//					metricComboViewer.add(ITEMS_THROUGHPUT);
//				}else if (selectedObject.equals("ResponseTime")){
//					metricComboViewer.add(ITEMS_RESPONSETIME);
//				}else if (selectedObject.equals("FunctionalCorrectness")){
//					metricComboViewer.add(ITEMS_FUNCTIONALCORRESCTNESS);
//				}else if (selectedObject.equals("Composability")){
//					metricComboViewer.add(ITEMS_COMPOSABILITY);
//				}else if (selectedObject.equals("Privacy")){
//					metricComboViewer.add(ITEMS_PRIVACY);
//				}  
			}
		});
//		metricComboViewer = new ComboViewer(grpTwproperties, SWT.NONE);
//		Combo combo_2 = metricComboViewer.getCombo();
//		combo_2.setBounds(100, 112, 200, 21);

		Group grpControls = new Group(shlPropertyspecification, SWT.NONE);
		grpControls.setText("Controls");
		grpControls.setBounds(10, 161, 330, 95);

		Label lblId_1 = new Label(grpControls, SWT.NONE);
		lblId_1.setBounds(10, 30, 49, 13);
		lblId_1.setText("ID");

		Label lblType = new Label(grpControls, SWT.NONE);
		lblType.setBounds(10, 60, 49, 13);
		lblType.setText("Type");

		controlID = new Text(grpControls, SWT.BORDER);
		controlID.setBounds(100, 24, 200, 19);

		final ComboViewer controlComboViewer = new ComboViewer(grpControls, SWT.NONE);
		controlComboViewer.add(ITEMS_CONTROLS);
		Combo combo_3 = controlComboViewer.getCombo();
		combo_3.setBounds(100, 52, 200, 21);
		Button btnNewButton = new Button(shlPropertyspecification, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton.setBounds(605, 262, 68, 23);
		btnNewButton.setText("Validate");
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				InternalSystemDM data = InternalSystemDM.getInternalSystemDM();

//				data.setControls(controls);
//				data.setTwAttributes(twAttributes);
//				data.setTwProperties(twProperties);
//				data.setTwPropertySpecifications(twPropertySpecifications);
//				data.setMetrics(metrics);
				try {
					cw.printCertificate(dtwcFile.getRawLocationURI().toString());
				} catch (OWLOntologyStorageException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				shlPropertyspecification.close();
			}
		});

		Button btnNewButton_1 = new Button(shlPropertyspecification, SWT.NONE);
		btnNewButton_1.setBounds(531, 262, 68, 23);
		btnNewButton_1.setText("Cancel");

		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shlPropertyspecification.close();
			}
		});
		Button btnAdd = new Button(shlPropertyspecification, SWT.NONE);
		btnAdd.setBounds(346, 115, 68, 23);
		btnAdd.setText("Add >>");
		btnAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {

				if (!twPropertyID.getText().equals("") && !controlID.getText().equals("")
						&& !assetComboViewer.getSelection().isEmpty()
						&& !twAttributeComboViewer.getSelection().isEmpty()
//						&& !metricComboViewer.getSelection().isEmpty()
						&& !controlComboViewer.getSelection().isEmpty())
				{
					String _asset = (String)((IStructuredSelection)assetComboViewer.getSelection()).getFirstElement();
					String _twAttribute = (String)((IStructuredSelection)twAttributeComboViewer.getSelection()).getFirstElement();
					_twAttribute = _twAttribute.replaceAll(" ", "_");
//					String _metric = (String)((IStructuredSelection)metricComboViewer.getSelection()).getFirstElement();
					String _control = (String)((IStructuredSelection)controlComboViewer.getSelection()).getFirstElement();
					
					System.out.println("twPropertyID" + twPropertyID.getText());
					System.out.println("assetComboViewer" + _asset);
					System.out.println("twAttributeComboViewer" + _twAttribute);
//					System.out.println("metricComboViewer" + _metric);
					System.out.println("controlID" + controlID.getText());
					System.out.println("controlComboViewer" + _control);

					System.out.println("Add ");


					TWPropertySpecification twps = dtwc.addTWPropertySpecification(twPropertyID.getText() + "-" + controlID.getText());					
					twps.setId(twPropertyID.getText() + "-" + controlID.getText());
					Control control = twps.addControl(controlID.getText());
					control.setId(controlID.getText());
					control.setType(_control);
					TWProperty twp = twps.addTWProperty(twPropertyID.getText());
					Asset asset = twp.addAsset(_asset);
//					Context context = twp.addContext(_metric);
//					context.setId(_metric);
					TWAttribute twa = twp.addTWAttribute(_twAttribute);
					twa.setId(_twAttribute);
					
					twPropSpecList.add(twps.getId());
					
//					Control control = new Control(controlID.getText(), _control);
//					controls.put(control.getName(), control);
//   
//					TWAttribute twAttribute = new TWAttribute( _twAttribute, _twAttribute);
//					twAttributes.put(twAttribute.getName(), twAttribute);
//
//					Metric metric = new Metric(_metric, _twAttribute);
//					metrics.put(_metric, metric);
//
//					TWProperty twProperty = new TWProperty(twPropertyID.getText(),
//							_asset,
//							_metric,
//							_twAttribute);
//
//					twProperties.put(twProperty.getName(), twProperty);
//
//					TWPropertySpecification twPropSpec = new TWPropertySpecification(twProperty.getName(), control.getName());
//					twPropertySpecifications.put(twPropSpec.getName(), twPropSpec);
//					twPropSpecList.add(twPropSpec.getName());
				}
			}
		});

		Button btnDelete = new Button(shlPropertyspecification, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] listToRemove = twPropSpecList.getSelection();
				for (int i = 0; i < listToRemove.length; i++) {
					
					dtwc.deleteTWPropertySpecification(listToRemove[i]);
					
//					twPropertySpecifications.remove(listToRemove[i]);
					twPropSpecList.remove(listToRemove[i]);
				}
			}
		});
		btnDelete.setBounds(346, 144, 68, 23);
		btnDelete.setText("delete");



		Group grpTwPropertySpecification = new Group(shlPropertyspecification, SWT.NONE);
		grpTwPropertySpecification.setText("TW Property Specification");
		grpTwPropertySpecification.setBounds(427, 0, 246, 256);

		twPropSpecList = new List(grpTwPropertySpecification, SWT.BORDER);
		twPropSpecList.setBounds(10, 20, 226, 226);




		// specific

	}
}
