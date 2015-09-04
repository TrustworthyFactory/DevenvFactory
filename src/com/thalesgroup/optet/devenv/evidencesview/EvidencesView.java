package com.thalesgroup.optet.devenv.evidencesview;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.jface.viewers.TreeViewer;
import org.osgi.framework.Bundle;

import com.thalesgroup.dtwc.DTWC;
import com.thalesgroup.dtwc.Evidence;
import com.thalesgroup.dtwc.Metric;
import com.thalesgroup.dtwc.MetricRuntimeCalculation;
import com.thalesgroup.dtwc.impl.CertificateWrapper;
import com.thalesgroup.dtwc.impl.EvidenceImpl;
import com.thalesgroup.dtwc.impl.MetricImpl;
import com.thalesgroup.dtwc.impl.MetricRuntimeCalculationImpl;
import com.thalesgroup.dtwc.impl.TWPropertySpecificationImpl;
import com.thalesgroup.optet.common.data.OptetDataModel;
import com.thalesgroup.optet.devenv.Activator;
import com.thalesgroup.optet.devenv.PluginHelper;
import com.thalesgroup.optet.devenv.datamodel.InternalSystemDM;
import com.thalesgroup.optet.devenv.datamodel.OPAValue;

import com.thalesgroup.optet.devenv.systemdesc.ComponentListLabelProvider;
import com.thalesgroup.optet.devenv.systemdesc.ComponentTreeContentProvider;
import com.thalesgroup.optet.twmanagement.OrchestrationInterface;
import com.thalesgroup.optet.twmanagement.impl.OrchestrationImpl;
import java.io.FileInputStream;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class EvidencesView extends Dialog {

	protected Object result;
	protected Shell shlEvidences;
	private Text valueText;
	private InternalSystemDM data;
	private Text definitionText;
	private EvidenceNode currentEvidenceNode;
	private Boolean automatic;
	private CertificateWrapper cw;
	private IFile dtwcFile;
	private DTWC dtwc;
	private Button CompResult;
	private Image imageOK;
	private Image imageNOK;

	String[] ITEMS_CALCULATION_METHOD = {"Inspection","Compute","Native"};
	private Text expectedValueText;
	private Text calDesc;
	private Map<String, com.thalesgroup.dtwc.TWAttribute> twAttributeMap ;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public EvidencesView(Shell parent, int style, Boolean automatic) {
		super(parent, style);
		setText("SWT Dialog");
		data = InternalSystemDM.getInternalSystemDM();
		this.automatic = automatic;
		imageOK = new Image(parent.getDisplay(),  getClass()
				.getResourceAsStream("/icons/OK.png"));
		imageNOK = new Image(parent.getDisplay(),  getClass()
				.getResourceAsStream("/icons/NOK.png"));

		twAttributeMap = new HashMap<>();
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		if (data.getCurrentProject() == null)
		{
			MessageDialog.openError(getParent(), "Project Selection", "Please, select a project before");
			return false;
		}

		dtwcFile = data.getCurrentProject().getFile("/Optet/dtwc.ttl");
		cw = new com.thalesgroup.dtwc.impl.CertificateWrapper();

		if (!dtwcFile.exists()){			
			PluginHelper.getInstance().logInfo("file not exist");


			Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
			URL fileURL = bundle.getEntry("resources/dtwc.ttl");
			InputStream in;
			try {
				in = new FileInputStream(FileLocator.toFileURL(fileURL).getPath());
				dtwcFile.create(in, false, null);
			} catch (FileNotFoundException e) {
				PluginHelper.getInstance().logException(e, e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				PluginHelper.getInstance().logException(e, e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				PluginHelper.getInstance().logException(e, e.getMessage());
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
			PluginHelper.getInstance().logInfo("file  exist");

			System.out.println("load certificate");
			dtwc = cw.loadCertificate(data.getCurrentProject().getName(),dtwcFile.getRawLocation().toString());


			Collection<TWPropertySpecificationImpl> twPorpSpec = dtwc.getTWPropertySpecifications();
			for (Iterator iterator = twPorpSpec.iterator(); iterator.hasNext();) {
				TWPropertySpecificationImpl twPropertySpecificationImpl = (TWPropertySpecificationImpl) iterator
						.next();
				System.out.println("load twattribute " + twPropertySpecificationImpl.getTWProperty().getTWAttribute().getId().replaceAll("_", " "));
				twAttributeMap.put(twPropertySpecificationImpl.getTWProperty().getTWAttribute().getId().replaceAll("_", " "), twPropertySpecificationImpl.getTWProperty().getTWAttribute());
			}
		}




		Properties prop = new Properties();
		String projectID = null;
		String projectType = null;
		try {
			prop.load(data.getCurrentProject().getFile("/Optet/Optet.properties").getContents());
			projectID = prop.getProperty("svn.project.selected");
			projectType = prop.getProperty("project.type");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (automatic && !projectType.equals("OPA")){
			if (dtwc!=null)
				Activator.getDefault().logInfo("dtwc not null");
			else
				Activator.getDefault().logInfo("dtwc  null");

			Collection<TWPropertySpecificationImpl> twpsCol = dtwc.getTWPropertySpecifications();
			for (Iterator iterator = twpsCol.iterator(); iterator.hasNext();) {
				TWPropertySpecificationImpl twPropertySpecificationImpl = (TWPropertySpecificationImpl) iterator
						.next();
				String twProfileID = twPropertySpecificationImpl.getTWProperty().getId();
				String twAttribute = twPropertySpecificationImpl.getTWProperty().getTWAttribute().getId();
				//String metric = twPropertySpecificationImpl.getTWProperty().getContext().getId();				

				System.out.println("twAttribute " + twAttribute );

				Evidence certEv = dtwc.getEvidence(twAttribute+"Ev");
				if (certEv == null){
					certEv = dtwc.addCertifiedEvidence(twAttribute+"Ev");
					certEv.setId(twAttribute);
				}

				Set<String> evidencesList = OptetDataModel.getInstance().getEvidenceForTWAttribute(twAttribute.replaceAll("_", " "));
				for (Iterator iterator2 = evidencesList.iterator(); iterator2
						.hasNext();) {
					String metric = (String) iterator2.next();
					String metricID = OptetDataModel.getInstance().getEvidenceID(metric);
					Metric myMetric = certEv.getMetric(metricID+"-"+twProfileID);
					if (myMetric == null){
						myMetric = certEv.addMetric(metricID+"-"+twProfileID);
					}

					myMetric.setId(metricID);
					if (OptetDataModel.getInstance().getMetric(metric)!=null){
						if (OptetDataModel.getInstance().getMetric(metric)!= null || !OptetDataModel.getInstance().getMetric(metric).equals("")){
							myMetric.setType("Compute");
							myMetric.setUnit("");
							myMetric.setValue(String.valueOf(OptetDataModel.getInstance().getMetric(metric)));
						}
						else {
							myMetric.setType("");
							myMetric.setUnit("");
							myMetric.setValue("");
						}


					}else{
						//do nothing
					}
				}
			} 
		}else {


			Collection<TWPropertySpecificationImpl> twpsCol = dtwc.getTWPropertySpecifications();
			for (Iterator iterator = twpsCol.iterator(); iterator.hasNext();) {
				TWPropertySpecificationImpl twPropertySpecificationImpl = (TWPropertySpecificationImpl) iterator
						.next();
				String twAttribute = twPropertySpecificationImpl.getTWProperty().getTWAttribute().getId();
				//String metric = twPropertySpecificationImpl.getTWProperty().getContext().getId();				
				String twProfileID = twPropertySpecificationImpl.getTWProperty().getId();

				System.out.println("twAttribute " + twAttribute  );

				Evidence certEv = dtwc.getEvidence(twAttribute+"Ev");
				if (certEv == null){
					certEv = dtwc.addCertifiedEvidence(twAttribute+"Ev");
					certEv.setId(twAttribute);
				}
				certEv.setId(twAttribute);
				Set<String> evidencesList = OptetDataModel.getInstance().getEvidenceForTWAttribute(twAttribute.replaceAll("_", " "));
				for (Iterator iterator2 = evidencesList.iterator(); iterator2
						.hasNext();) {
					String metric = (String) iterator2.next();
					String metricID = OptetDataModel.getInstance().getEvidenceID(metric);
					Metric myMetric = certEv.getMetric(metricID+"-"+twProfileID);
					if (myMetric == null){
						myMetric = certEv.addMetric(metricID+"-"+twProfileID);
					}
					myMetric.setId(metricID);

					if (projectType.equals("OPA")){
						String value = OPAValue.getValue(twAttribute.replaceAll("_", " "), metric);
						if (value!=null){
							myMetric.setType("Native");
							myMetric.setValue(value);
							OptetDataModel.getInstance().addMetrics(metric, value,"Evidence","");
						}
						else{
							myMetric.setType("");
							myMetric.setValue("");
						}
						myMetric.setUnit("");

					}else{
						myMetric.setType("");
						myMetric.setUnit("");
						myMetric.setValue("");
					}
				}
			}
		}

		createContents();
		shlEvidences.open();
		shlEvidences.layout();
		Display display = getParent().getDisplay();
		while (!shlEvidences.isDisposed()) {
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
		shlEvidences = new Shell(getParent(), getStyle());
		shlEvidences.setSize(531, 485);
		shlEvidences.setText("Evidences");

		Composite composite = new Composite(shlEvidences, SWT.NONE);
		composite.setBounds(10, 10, 508, 408);

		Group grpDescription = new Group(composite, SWT.NONE);
		grpDescription.setText("Description");
		grpDescription.setBounds(206, 0, 292, 283);

		Label lblDefinition = new Label(grpDescription, SWT.NONE);
		lblDefinition.setBounds(10, 31, 49, 13);
		lblDefinition.setText("Definition");

		Label lblValue = new Label(grpDescription, SWT.NONE);
		lblValue.setText("Value");
		lblValue.setBounds(10, 75, 49, 13);

		Label lblMethod = new Label(grpDescription, SWT.NONE);
		lblMethod.setText("Method");
		lblMethod.setBounds(10, 175, 49, 13);

		final ComboViewer computationMethod = new ComboViewer(grpDescription, SWT.NONE);
		computationMethod.getCombo().setBounds(81, 172, 93, 21);
		computationMethod.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				String selectedObject = (String) selection.getFirstElement();
				if (selectedObject!=null){
					//data.getMetrics().get(currentEvidenceNode.getData()).setMethod(selectedObject);

					Collection<EvidenceImpl> evCol = dtwc.getEvidences();
					for (Iterator iterator = evCol.iterator(); iterator
							.hasNext();) {
						EvidenceImpl evidenceImpl = (EvidenceImpl) iterator
								.next();
						System.out.println("evidenceImplevidenceImpl.getId()" + evidenceImpl.getId() + "    equals " + currentEvidenceNode.getParent().getData()) ;
						if (evidenceImpl.getId().equals(currentEvidenceNode.getParent().getData())){
							Collection<MetricImpl> metricCol = evidenceImpl.getMetricList();
							for (Iterator iterator2 = metricCol.iterator(); iterator2
									.hasNext();) {
								MetricImpl metricImpl = (MetricImpl) iterator2
										.next();
								System.out.println("" + metricImpl.getId() + "    equals " + OptetDataModel.getInstance().getEvidenceID(currentEvidenceNode.getData())) ;
								System.out.println("set type " + selectedObject);
								if (metricImpl.getId().equals(OptetDataModel.getInstance().getEvidenceID(currentEvidenceNode.getData()))){
									metricImpl.setType(selectedObject);
								}
							}
						}
					}
				}
			}
		});




		valueText = new Text(grpDescription, SWT.BORDER);
		valueText.setBounds(98, 72, 76, 19);

		valueText.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {

			}

			public void focusLost(FocusEvent e) {
				String textEvent = ((Text)e.getSource()).getText();
				if(textEvent==null)
					textEvent="";

				if (currentEvidenceNode!=null){

					Collection<EvidenceImpl> evCol = dtwc.getEvidences();
					for (Iterator iterator = evCol.iterator(); iterator
							.hasNext();) {
						EvidenceImpl evidenceImpl = (EvidenceImpl) iterator
								.next();
						System.out.println("evidenceImplevidenceImpl.getId()" + evidenceImpl.getId() + "    equals " + currentEvidenceNode.getParent().getData()) ;
						if (evidenceImpl.getId().equals(currentEvidenceNode.getParent().getData())){
							Collection<MetricImpl> metricCol = evidenceImpl.getMetricList();
							for (Iterator iterator2 = metricCol.iterator(); iterator2
									.hasNext();) {
								MetricImpl metricImpl = (MetricImpl) iterator2
										.next();
								System.out.println("metricImpl.getId()" + metricImpl.getId() + "    equals " + OptetDataModel.getInstance().getEvidenceID(currentEvidenceNode.getData())) ;

								if (metricImpl.getId().equals(OptetDataModel.getInstance().getEvidenceID(currentEvidenceNode.getData()))){
									metricImpl.setValue(textEvent);
									
									NumberFormat formatFR = NumberFormat.getInstance(Locale.FRENCH);
									NumberFormat formatUS = NumberFormat.getInstance(Locale.US);
									Double val = null;
									try {
										if (textEvent.contains(",")){
											val = formatFR.parse(textEvent).doubleValue()/100;
										}else if (textEvent.contains(".")){
											val = formatUS.parse(textEvent).doubleValue()/100;
										}else{
											val = formatFR.parse(textEvent).doubleValue()/100;
										}
											
										System.out.println("value " + val);
										OptetDataModel.getInstance().addMetrics(OptetDataModel.getInstance().getEvidenceName(metricImpl.getId()), String.valueOf(val),"Evidence","");
									} catch (ParseException ex) {
										// TODO Auto-generated catch block
										ex.printStackTrace();
									}
									
									
									
									String twAttrRes = OptetDataModel.getInstance().computeTWAttribute( currentEvidenceNode.getParent().getData());
									System.out.println("compute "+ currentEvidenceNode.getParent().getData()+ " with the following result " + twAttrRes);
									if (twAttrRes!=null){
										twAttributeMap.get(currentEvidenceNode.getParent().getData()).setValue(twAttrRes);
									}
									if (!valueText.getText().isEmpty() && !expectedValueText.getText().isEmpty()){
										if (Float.parseFloat(valueText.getText().replace(',', '.')) < Float.parseFloat(expectedValueText.getText().replace(',', '.')))
											CompResult.setImage(imageNOK);
										else
											CompResult.setImage(imageOK);
									}
									else
										CompResult.setImage(null);
								}
							}
						}
					}
				}
			}
		});


		definitionText = new Text(grpDescription, SWT.BORDER);
		definitionText.setBounds(81, 25, 201, 19);

		TreeViewer treeViewer = new TreeViewer(composite, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setBounds(10, 10, 190, 388);

		Button btnCancel = new Button(shlEvidences, SWT.NONE);
		btnCancel.setBounds(340, 424, 68, 23);
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shlEvidences.close();
			}
		});
		Button btnValidate = new Button(shlEvidences, SWT.NONE);
		btnValidate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnValidate.setText("Validate");
		btnValidate.setBounds(420, 424, 68, 23);
		btnValidate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				try {
					cw.printCertificate(dtwcFile.getRawLocationURI().toString());
				} catch (OWLOntologyStorageException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				shlEvidences.close();
			}
		});

		// specific

		computationMethod.add(ITEMS_CALCULATION_METHOD);
		computationMethod.getCombo().setEnabled(false);
		valueText.setEditable(false);
		definitionText.setEditable(false);

		Label lblExpectedValue = new Label(grpDescription, SWT.NONE);
		lblExpectedValue.setText("Expected Value");
		lblExpectedValue.setBounds(10, 125, 74, 13);

		expectedValueText = new Text(grpDescription, SWT.BORDER);
		expectedValueText.setEditable(false);
		expectedValueText.setBounds(98, 119, 76, 19);

		CompResult = new Button(grpDescription, SWT.NONE);
		CompResult.setSelection(true);
		CompResult.setBounds(198, 74, 64, 64);
		CompResult.setImage(null);

		Label lblCalculationDescritpion = new Label(grpDescription, SWT.NONE);
		lblCalculationDescritpion.setText("Calculation descritpion");
		lblCalculationDescritpion.setBounds(10, 223, 122, 13);

		calDesc = new Text(grpDescription, SWT.BORDER);
		calDesc.setBounds(137, 217, 145, 56);

		calDesc.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {

			}

			public void focusLost(FocusEvent e) {
				Text textEvent = (Text)e.getSource();

				if (currentEvidenceNode!=null){
					//data.getMetrics().get(currentEvidenceNode.getData()).setValue(textEvent.getText());

					Collection<EvidenceImpl> evCol = dtwc.getEvidences();
					for (Iterator iterator = evCol.iterator(); iterator
							.hasNext();) {
						EvidenceImpl evidenceImpl = (EvidenceImpl) iterator
								.next();
						System.out.println("evidenceImplevidenceImpl.getId()" + evidenceImpl.getId() + "    equals " + currentEvidenceNode.getParent().getData()) ;
						if (evidenceImpl.getId().equals(currentEvidenceNode.getParent().getData())){
							Collection<MetricImpl> metricCol = evidenceImpl.getMetricList();
							for (Iterator iterator2 = metricCol.iterator(); iterator2
									.hasNext();) {
								MetricImpl metricImpl = (MetricImpl) iterator2
										.next();
								System.out.println("metricImpl.getId()" + metricImpl.getId() + "    equals " + OptetDataModel.getInstance().getEvidenceID(currentEvidenceNode.getData())) ;

								if (metricImpl.getId().equals(OptetDataModel.getInstance().getEvidenceID(currentEvidenceNode.getData()))){
									MetricRuntimeCalculation mrc = metricImpl.addMetricRuntimeCalculation(metricImpl.getId()+"-mrc");
									mrc.setValue(textEvent.getText());
								}
							}
						}
					}
				}
			}


		});



		ContentProvider cp = new ContentProvider();
		treeViewer.setContentProvider(cp);
		treeViewer.setLabelProvider(new LabelProvider());


		EvidenceNode node = buildNode();
		treeViewer.setInput(node);


		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event)
			{
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				currentEvidenceNode = ((EvidenceNode)selection.getFirstElement());
				System.out.println("Selected: " + currentEvidenceNode.getData());
				definitionText.setText(selection.getFirstElement().toString());
				definitionText.setEditable(false);
				if (currentEvidenceNode.getChildren().size() !=  0){




					String twAttrRes = OptetDataModel.getInstance().computeTWAttribute(currentEvidenceNode.getData());
					System.out.println("compute "+ currentEvidenceNode.getParent().getData()+ " with the following result " + twAttrRes);
					if (twAttrRes!=null){
						twAttributeMap.get(currentEvidenceNode.getData()).setValue(twAttrRes);
					}					
					else
					{
						twAttributeMap.get(currentEvidenceNode.getData()).setValue("");
					}
					if (twAttributeMap.get(currentEvidenceNode.getData()).getValue()!=null)
					{
						valueText.setText(twAttributeMap.get(currentEvidenceNode.getData()).getValue());
						
						//Float f = Float.parseFloat(twAttributeMap.get(currentEvidenceNode.getData()).getValue().replace(',', '.'))*100;
						//valueText.setText(f.toString());
					}
					else
					{
						valueText.setText("");
					}
					calDesc.setEditable(false);
					calDesc.setText("");

					valueText.setEditable(false);
					computationMethod.getCombo().setText("");	
					computationMethod.getCombo().setEnabled(false);
					CompResult.setImage(null);

					String expectedValue = OptetDataModel.getInstance().getExpectedTWAttribute(currentEvidenceNode.getData());


					if (expectedValue!=null)
						expectedValueText.setText(expectedValue);
					else
						expectedValueText.setText("");

					if (!valueText.getText().isEmpty() && !expectedValueText.getText().isEmpty()){
						if (Float.parseFloat(valueText.getText().replace(',', '.')) < Float.parseFloat(expectedValueText.getText().replace(',', '.')))
							CompResult.setImage(imageNOK);
						else
							CompResult.setImage(imageOK);
					}
					else
						CompResult.setImage(null);
				}else{
					String res = null;
					String method = null;
					String id = null;
					String rcm = null;
					String expectedValue = null;

					Collection<EvidenceImpl> evCol = dtwc.getEvidences();
					for (Iterator iterator = evCol.iterator(); iterator
							.hasNext();) {
						EvidenceImpl evidenceImpl = (EvidenceImpl) iterator
								.next();
						System.out.println("evidenceImpl.getId()" + evidenceImpl.getId().replaceAll("_", " ") + "    equals " + currentEvidenceNode.getParent().getData()) ;
						if (evidenceImpl.getId().replaceAll("_", " ").equals(currentEvidenceNode.getParent().getData())){
							Collection<MetricImpl> metricCol = evidenceImpl.getMetricList();
							for (Iterator iterator2 = metricCol.iterator(); iterator2
									.hasNext();) {
								MetricImpl metricImpl = (MetricImpl) iterator2
										.next();
								System.out.println("metricImpl.getId()" + metricImpl.getId() + "    equals " + OptetDataModel.getInstance().getEvidenceID(currentEvidenceNode.getData())) ;

								if (metricImpl.getId().equals(OptetDataModel.getInstance().getEvidenceID(currentEvidenceNode.getData()))){
									id = metricImpl.getId();
									res = metricImpl.getValue();
									method = metricImpl.getType();
									System.out.println("id " + id + "  res " + res + " method " + method);
									expectedValue = OptetDataModel.getInstance().getExpectedMetric(OptetDataModel.getInstance().getEvidenceName(id), evidenceImpl.getId().replaceAll("_", " "));
									Collection<MetricRuntimeCalculationImpl> rcmList = metricImpl.getMetricRuntimeCalculationList();
									for (Iterator iterator3 = rcmList
											.iterator(); iterator3.hasNext();) {
										MetricRuntimeCalculationImpl metricRuntimeCalculationImpl = (MetricRuntimeCalculationImpl) iterator3
												.next();
										rcm = metricRuntimeCalculationImpl.getValue();
										System.out.println("rcm " + rcm);
									}
								}
							}
						}
					}


					System.out.println("id " + id + "  method " + method + "    res " + res) ;

					if (rcm!=null)
					{
						calDesc.setText(rcm);
					}
					else
					{
						calDesc.setText("");
					}
					calDesc.setEditable(true);
					if (method!= null)
					{
						if (method.equals("Compute") || method.equals("Native")){
							computationMethod.getCombo().setEnabled(false);
							computationMethod.getCombo().setText(method);	
							valueText.setEditable(false);
							if (res != null && !res.equals("") ){
								Float f = Float.parseFloat(res.replace(',', '.'))*100;
								valueText.setText(f.toString());

								//valueText.setText(res);
							}else{
								valueText.setText("");
							}
						}else
						{							
							computationMethod.getCombo().setEnabled(true);
							computationMethod.getCombo().setText(method);	
							valueText.setEditable(true);
							if (res != null && !res.equals("") ){
								Float f = Float.parseFloat(res.replace(',', '.'))*100;
								valueText.setText(f.toString());

								//valueText.setText(res);
							}else{
								valueText.setText("");
							}
						}	
					}else{
						computationMethod.getCombo().setEnabled(true);
						computationMethod.getCombo().setText("");	
						valueText.setEditable(true);
						valueText.setText("");
					}

					if (expectedValue!=null)
						expectedValueText.setText(expectedValue);
					else
						expectedValueText.setText("");

					if (!valueText.getText().isEmpty() && !expectedValueText.getText().isEmpty()){
						if (Float.parseFloat(valueText.getText().replace(',', '.')) < Float.parseFloat(expectedValueText.getText().replace(',', '.')))
							CompResult.setImage(imageNOK);
						else
							CompResult.setImage(imageOK);
					}
					else
						CompResult.setImage(null);

				}
			}
		});
	}

	private EvidenceNode buildNode() {


		EvidenceNode root = new EvidenceNode("root", "root");
		Map<String, EvidenceNode> categoryMap = new HashMap<String, EvidenceNode>();


		Collection<TWPropertySpecificationImpl> twpsCol = dtwc.getTWPropertySpecifications();
		for (Iterator iterator = twpsCol.iterator(); iterator.hasNext();) {
			TWPropertySpecificationImpl twPropertySpecificationImpl = (TWPropertySpecificationImpl) iterator
					.next();
			String twAttribute = twPropertySpecificationImpl.getTWProperty().getTWAttribute().getId().replaceAll("_", " ");
			
			System.out.println("buildNode : " + twAttribute);
			//String metric = twPropertySpecificationImpl.getTWProperty().getContext().getId() + "(" + twPropertySpecificationImpl.getTWProperty().getId()+")";	
			Set<String> evidencesList = OptetDataModel.getInstance().getEvidenceForTWAttribute(twAttribute);
			for (Iterator iterator2 = evidencesList.iterator(); iterator2
					.hasNext();) {
				String metric = (String) iterator2.next();
				if (categoryMap.containsKey(twAttribute)){
					System.out.println("exist");
					categoryMap.get(twAttribute).addChild(new EvidenceNode(metric, twAttribute));
				} else {
					System.out.println("not exist");
					EvidenceNode cat = new EvidenceNode(twAttribute, twAttribute);
					cat.addChild(new EvidenceNode(metric, twAttribute));
					categoryMap.put(twAttribute, cat);				
				}	
			}




		}

		for(Entry<String, EvidenceNode> entry : categoryMap.entrySet()) {
			root.addChild(entry.getValue());
		}

		return root;
	}
}
