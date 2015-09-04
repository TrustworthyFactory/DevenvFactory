package com.thalesgroup.optet.devenv.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;


public class InternalSystemDM {

	private static InternalSystemDM internal = new InternalSystemDM();

	private HashMap<String, Threat> threats;







	public Map<String, Metric> getMetrics() {
		return metrics;
	}



	public void setMetrics(Map<String, Metric> metrics) {
		this.metrics = metrics;
	}





	private Map<String,Metric> metrics;

	private HashMap<String, Element> comp;

	private HashMap<String, Stakeholder> stakeholders;

	private HashMap<String, Element> assets;

	private Map<String, Control> controls;

	private Map<String, TWAttribute> twAttributes;

	private Map<String, TWProperty> twProperties;

	private IProject currentProject;

	public Map<String, TWAttribute> getTwAttributes() {
		return twAttributes;
	}



	public void setTwAttributes(Map<String, TWAttribute> twAttributes) {
		this.twAttributes = twAttributes;
	}



	public Map<String, TWProperty> getTwProperties() {
		return twProperties;
	}



	public void setTwProperties(Map<String, TWProperty> twProperties) {
		this.twProperties = twProperties;
	}



	public Map<String, TWPropertySpecification> getTwPropertySpecifications() {
		return twPropertySpecifications;
	}



	public void setTwPropertySpecifications(
			Map<String, TWPropertySpecification> twPropertySpecifications) {
		this.twPropertySpecifications = twPropertySpecifications;
	}





	private Map<String, TWPropertySpecification> twPropertySpecifications;


	public static InternalSystemDM getInternalSystemDM(){
		return internal;
	}



	private InternalSystemDM() {
		super();


		stakeholders = new HashMap<String, Stakeholder>();
		assets = new HashMap<String, Element>(); 
		comp = new  HashMap<String, Element>(); 

		Component root = new Component("Root", "Root", false);
		currentComponent = root;
		comp.put("Root", root);

		threats = exampleThread();
	}












	public HashMap<String, Threat> getThreats() {
		return threats;
	}



	public void setThreats(HashMap<String, Threat> threats) {
		this.threats = threats;
	}



	public void setComp(Map<String, Element> comp) {
		this.comp = (HashMap<String, Element>) comp;
	}






	public Map<String, Element> getComp() {
		return (Map<String, Element>) comp.clone();
		//return new HashMap<String, Element>(comp);
	}



	public Map<String, Element> getAssets() {
		return assets;
	}



	public void setAssets(Map<String, Element> assets) {
		this.assets = (HashMap<String, Element>) assets;


		System.out.println("set assets " + assets.size());
	}




	public void setStakeholders(Map<String, Stakeholder> stakeholders) {
		this.stakeholders = (HashMap<String, Stakeholder>) stakeholders;
	}





	public Map<String, Stakeholder> getStakeholders() {
		return (Map<String, Stakeholder>) stakeholders.clone();
	}





	private Component currentComponent;


	//	public void loadModel(){
	//		stakeholders = new HashMap<String, Stakeholder>();
	//		assets = new HashMap<String, Element>(); 
	//		comp = new ArrayList<>();
	////		comp = exampleComponent();	
	////		threats = exampleThread();
	////		metrics = exampleMetric();
	//		loadStakeholder();
	//	}

	private void loadStakeholder(){
		for (int i = 0; i < comp.size(); i++) {
			findStakeholderIntoComponent (comp.get(i));
		}	
	}

	private void findStakeholderIntoComponent(Element element){
		System.out.println(" element " + element);
		if (element instanceof Component)
		{
			System.out.println("is component");
			Set<String> childrenKey = ((Component)element).getChildren();
			for (Iterator iterator = childrenKey.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				if (((Component)element).getElement(string) instanceof Component){
					System.out.println("is component " + ((Component)element).getElement(string));
					findStakeholderIntoComponent (((Component)element).getElement(string));
				}else if (((Component)element).getElement(string) instanceof Stakeholder){

					stakeholders.put(((Stakeholder)(((Component)element).getElement(string))).ID,(Stakeholder)(((Component)element).getElement(string)));
				}
			}
		}
	}



	//	public List<Element> getModel(){
	//		return comp;
	//	}

	public static String[] componentCategories () {
		return new String[] {"Root",
				"Host",
				"Human",
				"LogicalAsset",
				"Organisation",
				"WiredNetwork",
		"WirelessNetwork"};
	}

	public static HashMap<String, Threat> exampleThread() {
		HashMap<String, Threat> threads = new HashMap<>();

		threads.put("Destruction",new Threat("Destruction", "OPTET_Undefined", "Exploit"));
		threads.put("Unrestricted_access",new Threat("Unrestricted_access", "OPTET_Undefined", "Exploit"));
		threads.put("Theft",new Threat("Theft", "OPTET_Undefined", "Exploit"));
		threads.put("Alteration",new Threat("Alteration", "OPTET_Undefined", "Exploit"));
		threads.put("Insider_attack",new Threat("Insider_attack", "OPTET_Undefined", "Exploit"));
		threads.put("Message_interruption",new Threat("Message_interruption", "OPTET_Undefined", "Communication"));
		threads.put("Message_injection",new Threat("Message_injection", "OPTET_Undefined", "Communication"));
		threads.put("Message_tampering",new Threat("Message_tampering", "OPTET_Undefined", "Communication"));
		threads.put("Communication_Eavesdropping",new Threat("Communication_Eavesdropping", "OPTET_Undefined", "Internal"));
		threads.put("Software_bug",new Threat("Software_bug", "OPTET_Undefined", "Internal"));
		threads.put("Malfunction",new Threat("Malfunction", "OPTET_Undefined", "Internal"));
		threads.put("Interference",new Threat("Interference", "OPTET_Undefined", "External"));
		threads.put("Impersonation",new Threat("Impersonation", "OPTET_Undefined", "External"));
		threads.put("Deception",new Threat("Deception", "OPTET_Undefined", "External"));
		threads.put("Under-provisioning",new Threat("Under-provisioning", "OPTET_Undefined", "Focus"));
		threads.put("Over-use",new Threat("Over-use", "OPTET_Undefined", "Focus"));
		threads.put("Over-commitment",new Threat("Over-commitment", "OPTET_Undefined", "Focus"));		
		threads.put("Remote_known_exploit",new Threat("Remote_known_exploit", "OPTET_Undefined", "Exploit"));
		threads.put("Remote_zero_day_exploit",new Threat("Remote_zero_day_exploit", "OPTET_Undefined", "Exploit"));
		threads.put("Other_action",new Threat("Other_action", "OPTET_Undefined", "Other"));		



		threads.put("UnderprovisionedService",new Threat("UnderprovisionedService", "OPTET_Undefined", "Exploit"));
		threads.put("ServiceSoftwareError",new Threat("ServiceSoftwareError", "OPTET_Undefined", "Exploit"));
		threads.put("UnderperformingHost",new Threat("UnderperformingHost", "OPTET_Undefined", "Exploit"));
		threads.put("UnauthorisedServiceAccess",new Threat("UnauthorisedServiceAccess", "OPTET_Undefined", "Exploit"));
		threads.put("InaccurateClient",new Threat("InaccurateClient", "OPTET_Undefined", "Exploit"));
		threads.put("ServiceOverload",new Threat("ServiceOverload", "OPTET_Undefined", "Exploit"));
		threads.put("ExternalMessageInterruption",new Threat("ExternalMessageInterruption", "OPTET_Undefined", "Exploit"));
		threads.put("SoftwareError",new Threat("SoftwareError", "OPTET_Undefined", "Exploit"));
		threads.put("IndiscreetHost",new Threat("IndiscreetHost", "OPTET_Undefined", "Exploit"));
		threads.put("ServiceImpersonation",new Threat("ServiceImpersonation", "OPTET_Undefined", "Exploit"));
		threads.put("UnavailableHost",new Threat("UnavailableHost", "OPTET_Undefined", "Exploit"));
		threads.put("InaccurateHost",new Threat("InaccurateHost", "OPTET_Undefined", "Exploit"));
		threads.put("ServiceUnderperformance",new Threat("ServiceUnderperformance", "OPTET_Undefined", "Exploit"));
		threads.put("ClientImpersonation",new Threat("ClientImpersonation", "OPTET_Undefined", "Exploit"));
		threads.put("UnauthenticClient",new Threat("UnauthenticClient", "OPTET_Undefined", "Exploit"));
		threads.put("UnauthorisedCommunications",new Threat("UnauthorisedCommunications", "OPTET_Undefined", "Exploit"));




		return threads;		
	}

	public static List<Metric> exampleMetric() {
		List<Metric> metrics = new ArrayList<>();
		metrics.add(new Metric("complexity", "complexity"));
		metrics.add(new Metric("ErrorandExceptionHandling", "Reliability"));
		metrics.add(new Metric("complianceWithProgLanguage", "Reliability"));
		metrics.add(new Metric("complianceWithProgLanguage", "Maintenability"));
		metrics.add(new Metric("codeReadability", "Maintenability"));
		metrics.add(new Metric("codeOrganisation", "Maintenability"));
		metrics.add(new Metric("documentationEnbedded ", "Maintenability"));
		metrics.add(new Metric("security", "security"));




		return metrics;

	}

	public static List<Element> exampleComponent() {
		//Component root = new Component("root", "root", false);
		//		Component comp1 = new Component("refcomp1", "idcomp1", true);
		//		Component comp2 = new Component("refcomp2", "idcomp2", true);
		//		Component comp3 = new Component("refcomp3", "idcomp3", false);
		//		Component comp4 = new Component("refcomp4", "idcomp4", false);
		//		Component comp5 = new Component("refcomp5", "idcomp5", true);
		//		Component comp6 = new Component("refcomp6", "idcomp6", true);
		//
		//		Attribute attr1 = new Attribute("Attr1", "InputParameter", true);
		//		Attribute attr2 = new Attribute("Attr2", "InputParameter", true);
		//		Attribute attr3 = new Attribute("Attr3", "OutputParameter", false);
		//		Attribute attr4 = new Attribute("Attr4", "OutputParameter", false);
		//
		//		Stakeholder sh1 = new Stakeholder("sh1", "End-user");
		//		Stakeholder sh2 = new Stakeholder("sh2", "System");
		//		Stakeholder sh3 = new Stakeholder("sh3", "System");
		//		Stakeholder sh4 = new Stakeholder("sh4", "End-user");
		//
		//		comp1.addElement(sh1);
		//		comp1.addElement(attr1);
		//		comp1.addElement(attr2);
		//
		//		comp2.addElement(sh2);
		//		comp2.addElement(sh3);
		//		comp2.addElement(attr3);
		//		comp2.addElement(comp3);
		//
		//		comp4.addElement(comp5);
		//		comp4.addElement(comp6);
		//
		//		root.addElement(comp1);
		//		root.addElement(comp2);
		//		root.addElement(comp4);

		Component root = new Component("root", "root", false);
		List<Element> list = new ArrayList<>();
		list.add(root);
		return list;

	}



	public Map<String, Control> getControls() {
		return controls;
	}



	public void setControls(Map<String, Control> controls) {
		this.controls = controls;
	}



	/**
	 * @param iProject
	 */
	public void setCurrentProject(IProject iProject) {
		System.out.println("set current project : " + iProject.getName());
		this.currentProject = iProject;

	}



	public IProject getCurrentProject() {
		return currentProject;
	}
}
