package com.thalesgroup.optet.devenv.usdl;

/* Copyright (C) 2012 Jorge Cardoso. All rights reserved.
 * 
 * This program and the accompanying materials are made available under
 * the terms of the Common Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * $Id: LinkedUSDLExample.java,v 0.1 2012/11/11 20:02:36 jorge_cardoso Exp $
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

public class USDLGenerator {

	public USDLGenerator() {
		super();
		// TODO Auto-generated constructor stub
		usdlInstanceModel = ModelFactory.createDefaultModel();
		 formatter = new SimpleDateFormat("dd-MM-yyyy");
	}

	public final static String USDL = "http://www.linked-usdl.org/ns/usdl-core#";
	public final static String RDF  = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public final static String OWL  = "http://www.w3.org/2002/07/owl#";
	public final static String DC   = "http://purl.org/dc/elements/1.1/";
	public final static String XSD  = "http://www.w3.org/2001/XMLSchema#";
	public final static String VANN = "http://purl.org/vocab/vann/";
	public final static String FOAF = "http://xmlns.com/foaf/0.1/";
	public final static String USDK = "http://www.linked-usdl.org/ns/usdl#";
	public final static String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
	public final static String GR   = "http://purl.org/goodrelations/v1#";
	public final static String SKOS = "http://www.w3.org/2004/02/skos/core#";
	public final static String ORG  = "http://www.w3.org/ns/org#";
	public final static String PRICE= "http://www.linked-usdl.org/ns/usdl-pricing#";
	public final static String LEGAL= "http://www.linked-usdl.org/ns/usdl-legal#";
	public final static String DEI  = "http://dei.uc.pt/rdf/dei#";
	public final static String DCTERMS = "http://purl.org/dc/terms/";
	public final static String OPTETMKP =  "http://optet.eu/ns/TWMarketplace#";

	public final static String USDL_Core_Schema_File = "./usdl-core.ttl";
	public final static String USDL_Price_Schema_File = "./usdl-price.ttl";
	public final static String USDL_Instance_File = "./Service_01.ttl";

	private boolean available = true;
	private boolean availablemain = true;


	private Model usdlInstanceModel;
	private SimpleDateFormat formatter;
	public static void main (String args[]) {
		USDLGenerator example = new USDLGenerator();		
		BusinessEntity be = new BusinessEntity();
		be.setID("id");
		be.setLegalForm("legal");

Service s = new Service();
s.setTitle("title");

ServiceOffering so = new ServiceOffering();
so.setTitle("title");
TechnicalDescription td = new TechnicalDescription();
td.setLicense("BSD");
s.setTechnicalDescription(td);
PricePlan pp = new PricePlan();
pp.setTitle("pptitle");
PricePlan pp2 = new PricePlan();
pp2.setTitle("pp2title");

PriceComponent pc = new PriceComponent();
pc.setTitle("pc");
PriceComponent pc2 = new PriceComponent();
pc2.setTitle("pc2");
pp.getComponentMap().put(pc2.getTitle(), pc2);
pp.getComponentMap().put(pc.getTitle(), pc);

so.getPricePlanMap().put(pp.getTitle(), pp);
so.getPricePlanMap().put(pp2.getTitle(), pp2);

		
		example.addBuisinessEntity(be);
		example.addService(s);
		example.addServiceOffering(so);
		
		example.displayUSDL(System.out, "RDF/XML");
		

				File file = new File("d:/temp/usdlfile.usdl");
				try {
					FileOutputStream  fop = new FileOutputStream(file);
					// Display the usdl service instance just created
					example.displayUSDL(fop, "RDF/XML");
					example.displayUSDL(System.out, "OWL");
					//example.loadUDSL("d:/temp/usdlfile.usdl");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		try {
			example.loadBusinessEntityFromUDSL("d:/temp/usdlfile.usdl");
			example.loadServiceFromUDSL("d:/temp/usdlfile.usdl");
			 ServiceOffering so1 = example.loadServiceOfferingFromUDSL("d:/temp/usdlfile.usdl");
			System.out.println("size" + so1.getPricePlanMap().size());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	public BusinessEntity loadBusinessEntityFromUDSL(String file) throws ParseException{


		BusinessEntity be = new BusinessEntity();
		Model m = FileManager.get().loadModel(file);
		Property rdfType = m.createProperty(RDF, "type");

		
 
		/*
		 * find bussiness
		 */
		{
			Property responseInfo = m.createProperty(GR, "BusinessEntity");
			ResIterator iter = m.listResourcesWithProperty(rdfType, responseInfo);
			//There should only be one - take the first
			Resource  infoResource = null;
			while (iter.hasNext()) {
				infoResource = iter.next();
				System.out.println("BusinessEntity : "+infoResource.toString());
				
				Statement legalName = infoResource.getProperty(m.createProperty(GR+"legalName"));	
				if (legalName!=null){
					System.out.println("BusinessEntity legalName: "+legalName.getString());
					be.setLegalName(legalName.getString());
				}
				Statement legalForm = infoResource.getProperty(m.createProperty(USDL+"legalForm"));	
				if (legalForm!=null){
					System.out.println("BusinessEntity legalForm: "+legalForm.getString());
					be.setLegalForm(legalForm.getString());
				}
				Statement description = infoResource.getProperty(m.createProperty(DCTERMS+"description"));		
				if (description!=null){
					System.out.println("BusinessEntity description: "+description.getString());
					be.setDescription(description.getString());
				}
				Statement homepage = infoResource.getProperty(m.createProperty(FOAF+"homepage"));		
				if (homepage!=null){
					System.out.println("BusinessEntity homepage: "+homepage.getResource().toString());
					be.setUrl(homepage.getResource().toString());
				}
			}
		}
		return be;
		
		
		
		

	}


	
	public Service loadServiceFromUDSL(String file) throws ParseException{

		Service service = new Service();
		Model m = FileManager.get().loadModel(file);

		Property rdfType = m.createProperty(RDF, "type");
		List<String> technicalDesciption = new ArrayList<>();
		
 		/*
		 * find service
		 */
		{
			Property responseInfo = m.createProperty(USDL, "Service");
			ResIterator iter = m.listResourcesWithProperty(rdfType, responseInfo);
			Resource  infoResource = null;
			//There should only be one - take the first
			while (iter.hasNext()) {
				infoResource = iter.next();
				System.out.println("Service  : " + infoResource.toString());

				Statement description = infoResource.getProperty(m.createProperty(DCTERMS+"description"));		
				if (description!=null){
					System.out.println("Service description : " + description.getString());
					service.setDescription(description.getString());
				}
				Statement title = infoResource.getProperty(m.createProperty(DCTERMS+"title"));		
				if (title!=null){
					System.out.println("Service title : " + title.getString());
					service.setTitle(title.getString());
				}
				Statement _abstract = infoResource.getProperty(m.createProperty(DCTERMS+"abstract"));		
				if (_abstract!=null){
					System.out.println("Service abstract : " + _abstract.getString());
					service.setAbstractDes(_abstract.getString());
				}
				Statement modified = infoResource.getProperty(m.createProperty(DCTERMS+"modified"));		
				if (modified!=null){
					System.out.println("Service modified : " +  modified.getString());
					try{
						service.setModified(formatter.parse(modified.getString()));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				Statement created = infoResource.getProperty(m.createProperty(DCTERMS+"created"));		
				if (created!=null){
					System.out.println("Service created : " + created.getString());
					try{
						service.setCreated(formatter.parse(created.getString()));
					}catch(Exception e){

					}
				}
				Statement homepage = infoResource.getProperty(m.createProperty(FOAF+"page"));		
				if (homepage!=null){
					System.out.println("Service page : " + homepage.getResource().toString());
					service.setUrl(homepage.getResource().toString());
				}
				//				Statement hasClassification = infoResource.getProperty(m.createProperty(USDL+"hasClassification"));		
				//				if (hasClassification!=null){
				//					System.out.println(hasClassification.getString());
				//					service.setHasClassification(hasClassification.getString());
				//				}
				Statement hasOPTETCertificate = infoResource.getProperty(m.createProperty(OPTETMKP+"hasOPTETCertificate"));		
				if (hasOPTETCertificate!=null){
					System.out.println("Service hasOPTETCertificate : " + hasOPTETCertificate.getResource().toString());
					service.setCertificateURL(hasOPTETCertificate.getResource().toString());
				}

				Statement hasTechnicalDescription = infoResource.getProperty(m.createProperty(OPTETMKP+"hasTechnicalDescription"));		
				if (hasTechnicalDescription!=null){
					System.out.println("Service hasTechnicalDescription : " + hasTechnicalDescription.getResource().toString());
					technicalDesciption.add(hasTechnicalDescription.getResource().toString());
				}

			}
		}


		/*
		 * find TechnicalDescription
		 */

		{
			Property responseInfo = m.createProperty(OPTETMKP, "TechnicalDescription");
			ResIterator iter = m.listResourcesWithProperty(rdfType, responseInfo);

			Resource  infoResource = null;
			//There should only be one - take the first
			while (iter.hasNext()) {
				infoResource = iter.next();
				System.out.println("TechnicalDescription  : " + infoResource.toString());
				
				if (technicalDesciption.contains(infoResource.toString())){

					TechnicalDescription td = new TechnicalDescription();
					Statement License = infoResource.getProperty(m.createProperty(OPTETMKP+"License"));		
					if (License!=null){
						System.out.println("TechnicalDescription License : " + License.getString());
						td.setLicense(License.getString());
					}
					Statement ExecutionPlatform = infoResource.getProperty(m.createProperty(OPTETMKP+"ExecutionPlatform"));		
					if (ExecutionPlatform!=null){
						System.out.println("TechnicalDescription ExecutionPlatform : " + ExecutionPlatform.getString());
						td.setExecutionPlatform(ExecutionPlatform.getString());
					}
					Statement ProgrammingLanguage = infoResource.getProperty(m.createProperty(OPTETMKP+"ProgrammingLanguage"));		
					if (ProgrammingLanguage!=null){
						System.out.println("TechnicalDescription ProgrammingLanguage : " + ProgrammingLanguage.getString());
						td.setProgrammingLanguage(ProgrammingLanguage.getString());
					}
					Statement SoftwareDependency = infoResource.getProperty(m.createProperty(OPTETMKP+"SoftwareDependency"));		
					if (SoftwareDependency!=null){
						System.out.println("TechnicalDescription SoftwareDependency : " + SoftwareDependency.getString());
						td.setSoftwareDependency(SoftwareDependency.getString());
					}
					Statement HardwareRequirement = infoResource.getProperty(m.createProperty(OPTETMKP+"HardwareRequirement"));		
					if (HardwareRequirement!=null){
						System.out.println("TechnicalDescription HardwareRequirement : " + HardwareRequirement.getString());
						td.setHardwareRequirement(HardwareRequirement.getString());
					}
					Statement versionNumber = infoResource.getProperty(m.createProperty(OPTETMKP+"versionNumber"));		
					if (versionNumber!=null){
						System.out.println("TechnicalDescription versionNumber : " + versionNumber.getString());
						td.setVersionNumber(versionNumber.getString());
					}
					
					service.setTechnicalDescription(td);
				}
			}
		}
		return service;
	}


	
	
	public ServiceOffering loadServiceOfferingFromUDSL(String file) throws ParseException{

		ServiceOffering so = new ServiceOffering();
		Model m = FileManager.get().loadModel(file);
		Property rdfType = m.createProperty(RDF, "type");
		List<String> pricePlan = new ArrayList<>();
		Map<String, String> priceComponent = new HashMap<String, String>();
		
		/*
		 * find ServiceOffering
		 */
		{
			Property responseInfo = m.createProperty(USDL, "ServiceOffering");
			ResIterator iter = m.listResourcesWithProperty(rdfType, responseInfo);

			Resource  infoResource = null;
			//There should only be one - take the first
			while (iter.hasNext()) {
				infoResource = iter.next();
				System.out.println("ServiceOffering  : " + infoResource.toString());



				Statement description = infoResource.getProperty(m.createProperty(DCTERMS+"description"));		
				if (description!=null){
					System.out.println("ServiceOffering description : " + description.getString());
					so.setDescription(description.getString());
				}
				Statement title = infoResource.getProperty(m.createProperty(DCTERMS+"title"));		
				if (title!=null){
					System.out.println(title.getString());
					so.setTitle(title.getString());
				}

				Statement validFrom = infoResource.getProperty(m.createProperty(USDL+"validFrom"));		
				if (validFrom!=null){
					System.out.println("ServiceOffering validFrom : " + validFrom.getString());
					try{
						so.setValidFrom(formatter.parse(validFrom.getString()));
					}catch(Exception e){

					}
				}
				Statement validThrough = infoResource.getProperty(m.createProperty(USDL+"validThrough"));		
				if (validThrough!=null){
					System.out.println("ServiceOffering validThrough : " + validThrough.getString());
					try{
						so.setValidThrough(formatter.parse(validThrough.getString()));
					}catch(Exception e){

					}
				}
				
				StmtIterator propList = infoResource.listProperties(m.createProperty(USDL+"hasPricePlan"));
				List<Statement> statList = propList.toList();
				for (Iterator iterator = statList.iterator(); iterator
						.hasNext();) {
					Statement hasPricePlan = (Statement) iterator.next();
					if (hasPricePlan!=null){
						System.out.println("ServiceOffering hasPricePlan : " + hasPricePlan.getResource().toString());
						pricePlan.add(hasPricePlan.getResource().toString());
					}	
				}
				
				
//				Statement hasPricePlan = infoResource.getProperty(m.createProperty(USDL+"hasPricePlan"));		
//				if (hasPricePlan!=null){
//					System.out.println("ServiceOffering hasPricePlan : " + hasPricePlan.getResource().toString());
//					pricePlan.add(hasPricePlan.getResource().toString());
//				}				
			}
		}

		/*
		 * find PricePlan
		 */
		{
			Property responseInfo = m.createProperty(PRICE, "PricePlan");
			ResIterator iter = m.listResourcesWithProperty(rdfType, responseInfo);

			Resource  infoResource = null;
			//There should only be one - take the first
			while (iter.hasNext()) {
				infoResource = iter.next();
				System.out.println("PricePlan  : "+infoResource.toString());

				if (pricePlan.contains(infoResource.toString())){

					PricePlan pp = new PricePlan();
					Statement description = infoResource.getProperty(m.createProperty(DCTERMS+"description"));		
					if (description!=null){
						System.out.println("PricePlan description : " + description.getString());
						pp.setDescription(description.getString());
					}
					Statement title = infoResource.getProperty(m.createProperty(DCTERMS+"title"));		
					if (title!=null){
						System.out.println("PricePlan title : " + title.getString());
						pp.setTitle(title.getString());
					}
					
					so.getPricePlanMap().put(pp.getTitle(), pp);
					
					
					StmtIterator propList = infoResource.listProperties(m.createProperty(PRICE+"hasPriceComponent"));
					List<Statement> statList = propList.toList();
					for (Iterator iterator = statList.iterator(); iterator
							.hasNext();) {
						Statement hasPriceComponent = (Statement) iterator.next();
						if (hasPriceComponent!=null){
							System.out.println("ServiceOffering hasPriceComponent : " + hasPriceComponent.getResource().toString());
							priceComponent.put(hasPriceComponent.getResource().toString(), pp.getTitle());
						}	
					}
					
//					Statement hasPriceComponent = infoResource.getProperty(m.createProperty(PRICE+"hasPriceComponent"));		
//					if (hasPriceComponent!=null){
//						System.out.println("PricePlan hasPriceComponent : " + hasPriceComponent.getResource().toString());
//						priceComponent.put(hasPriceComponent.getResource().toString(), pp.getTitle());
//					}
//					
//					so.getPricePlanMap().put(pp.getTitle(), pp);
				}
			}
		}
		/*
		 * find PriceComponent
		 */
		{
			Property responseInfo = m.createProperty(PRICE, "PriceComponent");
			ResIterator iter = m.listResourcesWithProperty(rdfType, responseInfo);

			Resource  infoResource = null;
			//There should only be one - take the first
			while (iter.hasNext()) {
				infoResource = iter.next();
				System.out.println("PriceComponent  : "+infoResource.toString());

				if (priceComponent.containsKey(infoResource.toString())){

					PriceComponent pc = new PriceComponent();
					Statement description = infoResource.getProperty(m.createProperty(DCTERMS+"description"));		
					if (description!=null){
						System.out.println("PriceComponent description : " + description.getString());
						pc.setDescription(description.getString());
					}
					Statement title = infoResource.getProperty(m.createProperty(DCTERMS+"title"));		
					if (title!=null){
						System.out.println("PriceComponent title : " + title.getString());
						pc.setTitle(title.getString());
					}
					
					Statement hasCurrencyValue = infoResource.getProperty(m.createProperty(GR+"hasCurrencyValue"));		
					if (hasCurrencyValue!=null){
						System.out.println("PriceComponent hasCurrencyValue : " + hasCurrencyValue.getString());
						pc.setCurrencyValue(hasCurrencyValue.getString());
					}

					Statement hasCurrency = infoResource.getProperty(m.createProperty(GR+"hasCurrency"));		
					if (hasCurrency!=null){
						System.out.println("PriceComponent hasCurrency : " + hasCurrency.getString());
						pc.setCurrency(hasCurrency.getString());
					}

					Statement hasUnitOfMeasurement = infoResource.getProperty(m.createProperty(GR+"hasUnitOfMeasurement"));		
					if (hasUnitOfMeasurement!=null){
						System.out.println("PriceComponent hasUnitOfMeasurement : " + hasUnitOfMeasurement.getString());
						pc.setUnitOfMeasure(hasUnitOfMeasurement.getString());
					}

					so.getPricePlanMap().get(priceComponent.get(infoResource.toString())).getComponentMap().put(pc.getTitle(), pc);
				}
			}
		}
		return so;
	}


	
	public void displayUSDL(OutputStream stream , String lang){
		
		usdlInstanceModel.setNsPrefix("usdl" , "http://www.linked-usdl.org/ns/usdl-core#");
		usdlInstanceModel.setNsPrefix("rdf"  , "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		usdlInstanceModel.setNsPrefix("owl"  , "http://www.w3.org/2002/07/owl#");
		usdlInstanceModel.setNsPrefix("dc"   , "http://purl.org/dc/elements/1.1/");
		usdlInstanceModel.setNsPrefix("xsd"  , "http://www.w3.org/2001/XMLSchema#");
		usdlInstanceModel.setNsPrefix("vann" , "http://purl.org/vocab/vann/");
		usdlInstanceModel.setNsPrefix("foaf" , "http://xmlns.com/foaf/0.1/");
		usdlInstanceModel.setNsPrefix("usdk" , "http://www.linked-usdl.org/ns/usdl#");
		usdlInstanceModel.setNsPrefix("rdfs" , "http://www.w3.org/2000/01/rdf-schema#");
		usdlInstanceModel.setNsPrefix("gr"   , "http://purl.org/goodrelations/v1#");
		usdlInstanceModel.setNsPrefix("skos" , "http://www.w3.org/2004/02/skos/core#");
		usdlInstanceModel.setNsPrefix("org"  , "http://www.w3.org/ns/org#");
		usdlInstanceModel.setNsPrefix("price", "http://www.linked-usdl.org/ns/usdl-pricing#");
		usdlInstanceModel.setNsPrefix("legal", "http://www.linked-usdl.org/ns/usdl-legal#");
		usdlInstanceModel.setNsPrefix("dei"  , "http://dei.uc.pt/rdf/dei#");
		usdlInstanceModel.setNsPrefix("dcterms" , "http://purl.org/dc/terms/");
		usdlInstanceModel.setNsPrefix("optetmkp" ,  "http://optet.eu/ns/TWMarketplace#");
		if (lang.endsWith("RDF/XML")){
			usdlInstanceModel.write(stream, "RDF/XML", null);   
		}else if (lang.endsWith("OWL")){
			OWLOntology  ont= ModelJenaToOwlConvert("RDF/XML");
			RDFXMLOntologyFormat xmlFormat = new RDFXMLOntologyFormat();
			try {
				ont.getOWLOntologyManager().saveOntology(ont, xmlFormat,	
						stream);
			} catch (OWLOntologyStorageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}

	/**
	 * This function converts an ontology object from OWLapi to Jena
	 *
	 * @param owlmodel {An OWLOntology object}
	 * @param format {RDF/XML or TURTLE}
	 * @return {An OntModel that is a Jena object}
	 */
	public synchronized OntModel ModelOwlToJenaConvert(OWLOntology owlmodel,String format){

		while (availablemain == false) {
			try {wait();} catch (InterruptedException e) {System.err.println("ModelOwlToJenaConvert::: "+e);}
		}

		availablemain=false;
		OWLOntologyID id;

		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			OWLOntologyManager owlmanager = owlmodel.getOWLOntologyManager();

			format = format.trim();

			if(format.equals("TURTLE")||format.equals("RDF/XML")){

				if(format.equals("TURTLE"))
					owlmanager.setOntologyFormat(owlmodel, new TurtleOntologyFormat());
				if(format.equals("RDF/XML"))
					owlmanager.setOntologyFormat(owlmodel,new RDFXMLOntologyFormat());

				OWLOntologyFormat owlformat = owlmanager.getOntologyFormat(owlmodel);

				owlmanager.saveOntology(owlmodel, owlformat, out);

				OntModel jenamodel = ModelFactory.createOntologyModel();
				id = owlmodel.getOntologyID();
				jenamodel.read(new ByteArrayInputStream(out.toByteArray()),id.toString().replace("<","").replace(">",""),format);

				availablemain = true;
				notifyAll();
				return jenamodel;
			}else{
				System.err.println("The only format supported is RDF/XML or TURTLE. Please check the format!");

				availablemain = true;
				notifyAll();
				return null;
			}}catch (OWLOntologyStorageException eos){
				System.err.print("ModelOwlToJenaConvert::: ");
				eos.printStackTrace();
				return null;
			}
	}

	/**
	 * This function converts an ontology object from Jena to OWLapi
	 *
	 * @param jenamodel {An OntModel object}
	 * @param format {only in "RDF/XML"}
	 * @return {An OWLOntology  that is an owl object}
	 */
	public synchronized OWLOntology ModelJenaToOwlConvert(String format){

		while (availablemain == false) {
			try {wait();} catch (InterruptedException e) {System.err.println("ModelJenaToOwlConvert::: "+e);}
		}

		availablemain=false;

		try{

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			if(!format.equals("RDF/XML")){
				System.err.println("The only format supported is RDF/XML. Please check the format!");

				availablemain = true;
				notifyAll();
				return null;
			}else{

				usdlInstanceModel.write(out,format);

				OWLOntologyManager owlmanager = OWLManager.createOWLOntologyManager();

				OWLOntology owlmodel = owlmanager.loadOntologyFromOntologyDocument(new ByteArrayInputStream(out.toByteArray()));

				availablemain = true;
				notifyAll();
				return owlmodel;
			}}catch (OWLOntologyCreationException eoc){
				System.err.print("ModelJenaToOwlConvert::: ");
				eoc.printStackTrace();
				return null;
			}
	}


	public void addBuisinessEntity(BusinessEntity be){
		Resource bussineddEntity 
		= usdlInstanceModel.createResource("http://BuisinessEntity" +  be.getID());
		bussineddEntity.addProperty(usdlInstanceModel.createProperty(RDF+"type"), usdlInstanceModel.createResource(GR+"BusinessEntity"));
		if (be.getLegalName()!=null){
			bussineddEntity.addProperty(usdlInstanceModel.createProperty(GR+"legalName"), usdlInstanceModel.createLiteral(be.getLegalName()));
		}
		if (be.getLegalForm()!=null){
			bussineddEntity.addProperty(usdlInstanceModel.createProperty(USDL+"legalForm"), usdlInstanceModel.createLiteral(be.getLegalForm()));
		}
		if (be.getDescription()!=null){
			bussineddEntity.addProperty(usdlInstanceModel.createProperty(DCTERMS+"description"), usdlInstanceModel.createLiteral(be.getDescription()));
		}
		if (be.getUrl()!=null){
			bussineddEntity.addProperty(usdlInstanceModel.createProperty(FOAF+"homepage"), usdlInstanceModel.createResource(be.getUrl()));
		}
	}

	public void addPriceComponent(PriceComponent pc){
		Resource priceComponent 
		= usdlInstanceModel.createResource("http://PriceComponent" + pc.getID()).
		addProperty(usdlInstanceModel.createProperty(RDF+"type"), usdlInstanceModel.createResource(PRICE+"PriceComponent"));

		if (pc.getDescription()!=null){
			priceComponent.addProperty(usdlInstanceModel.createProperty(DCTERMS+"description"), usdlInstanceModel.createLiteral(pc.getDescription()));
		}
		if (pc.getCurrencyValue()!=null){
			priceComponent.addProperty(usdlInstanceModel.createProperty(GR+"hasCurrencyValue"), usdlInstanceModel.createLiteral(pc.getCurrencyValue()));
		}
		if (pc.getCurrency()!=null){
			priceComponent.addProperty(usdlInstanceModel.createProperty(GR+"hasCurrency"), usdlInstanceModel.createLiteral(pc.getCurrency()));
		}
		if (pc.getUnitOfMeasure()!=null){
			priceComponent.addProperty(usdlInstanceModel.createProperty(GR+"hasUnitOfMeasurement"), usdlInstanceModel.createLiteral(pc.getUnitOfMeasure()));
		}
		if (pc.getTitle()!=null){
			priceComponent.addProperty(usdlInstanceModel.createProperty(DCTERMS+"title"), usdlInstanceModel.createLiteral(pc.getTitle())); 
		}

	}

	public void addPricePlan(PricePlan pp){
		Resource privePlan 
		= usdlInstanceModel.createResource("http://PricePlan" + pp.getID()).
		addProperty(usdlInstanceModel.createProperty(RDF+"type"), usdlInstanceModel.createResource(PRICE+"PricePlan"));

		if (pp.getDescription()!=null){
			privePlan.addProperty(usdlInstanceModel.createProperty(DCTERMS+"description"), usdlInstanceModel.createLiteral(pp.getDescription()));
		}
		if (pp.getTitle()!=null){
			privePlan.addProperty(usdlInstanceModel.createProperty(DCTERMS+"title"), usdlInstanceModel.createLiteral(pp.getTitle())); 
		}

		for (Iterator iterator = pp.getComponentMap().values().iterator(); iterator.hasNext();) {
			PriceComponent type = (PriceComponent) iterator.next();
			privePlan.addProperty(usdlInstanceModel.createProperty(PRICE+"hasPriceComponent"), usdlInstanceModel.createResource("http://PriceComponent" + type.getID()));
			addPriceComponent(type);
		}
	}

	public void addServiceOffering(ServiceOffering so){

		Resource serviceOffering 
		= usdlInstanceModel.createResource("http://ServiceOffering" + so.getID()).
		addProperty(usdlInstanceModel.createProperty(RDF+"type"), usdlInstanceModel.createResource(USDL+"ServiceOffering"));
		if (so.getDescription()!=null){
			serviceOffering.addProperty(usdlInstanceModel.createProperty(DCTERMS+"description"), usdlInstanceModel.createLiteral(so.getDescription()));
		}
		if (so.getTitle()!=null){
			serviceOffering.addProperty(usdlInstanceModel.createProperty(DCTERMS+"title"), usdlInstanceModel.createLiteral(so.getTitle()));
		}
		if (so.getValidFrom()!=null){
			serviceOffering.addProperty(usdlInstanceModel.createProperty(USDL+"validFrom"), usdlInstanceModel.createLiteral(formatter.format(so.getValidFrom())));
		}
		if (so.getValidThrough()!=null){
			serviceOffering.addProperty(usdlInstanceModel.createProperty(USDL+"validThrough"), usdlInstanceModel.createLiteral(formatter.format(so.getValidThrough())));
		}


		for (Iterator iterator = so.getPricePlanMap().values().iterator(); iterator.hasNext();) {
			PricePlan type = (PricePlan) iterator.next();
			serviceOffering.addProperty(usdlInstanceModel.createProperty(USDL+"hasPricePlan"), usdlInstanceModel.createResource("http://PricePlan" + type.getID()));
			addPricePlan(type);
		}
	}

	public void addServiceDescription(ServiceDescription sd){

		Resource serviceDescription 
		= usdlInstanceModel.createResource("http://ServiceDescription" + sd.getID()).
		addProperty(usdlInstanceModel.createProperty(RDF+"type"), usdlInstanceModel.createResource(USDL+"ServiceDescription"));
		if (sd.getDescription()!=null){
			serviceDescription.addProperty(usdlInstanceModel.createProperty(DCTERMS+"description"), usdlInstanceModel.createLiteral(sd.getDescription()));
		}
		if (sd.getTitle()!=null){
			serviceDescription.addProperty(usdlInstanceModel.createProperty(DCTERMS+"title"), usdlInstanceModel.createLiteral(sd.getTitle()));
		}
		if (sd.getModified()!=null){
			serviceDescription.addProperty(usdlInstanceModel.createProperty(USDL+"modified"), usdlInstanceModel.createLiteral(sd.getModified().toString()));
		}
		if (sd.getCreated()!=null){
			serviceDescription.addProperty(usdlInstanceModel.createProperty(USDL+"created"), usdlInstanceModel.createLiteral(sd.getCreated().toString()));
		}
		if (sd.getCreator()!=null){
			serviceDescription.addProperty(usdlInstanceModel.createProperty(USDL+"creator"), usdlInstanceModel.createLiteral(sd.getCreator()));
		}
	}

	public void addTechnicalDescription(TechnicalDescription td){
		Resource technicalDescription 
		= usdlInstanceModel.createResource("http://TechnicalDescription" + td.getID()).
		addProperty(usdlInstanceModel.createProperty(RDF+"type"), usdlInstanceModel.createResource(OPTETMKP+"TechnicalDescription"));

		if (td.getLicense()!=null){
			technicalDescription.addProperty(usdlInstanceModel.createProperty(OPTETMKP+"License"), usdlInstanceModel.createLiteral(td.getLicense()));
		}
		if (td.getExecutionPlatform()!=null){
			technicalDescription.addProperty(usdlInstanceModel.createProperty(OPTETMKP+"ExecutionPlatform"), usdlInstanceModel.createLiteral(td.getExecutionPlatform()));
		}
		if (td.getProgrammingLanguage()!=null){
			technicalDescription.addProperty(usdlInstanceModel.createProperty(OPTETMKP+"ProgrammingLanguage"), usdlInstanceModel.createLiteral(td.getProgrammingLanguage()));
		}
		if (td.getSoftwareDependency()!=null){
			technicalDescription.addProperty(usdlInstanceModel.createProperty(OPTETMKP+"SoftwareDependency"), usdlInstanceModel.createLiteral(td.getSoftwareDependency()));
		}
		if (td.getHardwareRequirement()!=null){
			technicalDescription.addProperty(usdlInstanceModel.createProperty(OPTETMKP+"HardwareRequirement"), usdlInstanceModel.createLiteral(td.getHardwareRequirement()));
		}
		if (td.getVersionNumber()!=null){
			technicalDescription.addProperty(usdlInstanceModel.createProperty(OPTETMKP+"versionNumber"), usdlInstanceModel.createLiteral(td.getVersionNumber()));
		}

	}

	public void addService(Service s){
		//usdlInstanceModel.setNsPrefix("usdl", "http://www.linked-usdl.org/ns/usdl-core#");
		Resource service  
		= usdlInstanceModel.createResource("http://Service" + s.getID());
		service.addProperty(usdlInstanceModel.createProperty(RDF+"type"), usdlInstanceModel.createResource("http://www.linked-usdl.org/ns/usdl-core#Service"));
		



		if (s.getDescription()!=null){
			service.addProperty(usdlInstanceModel.createProperty(DCTERMS+"description"), usdlInstanceModel.createLiteral(s.getDescription()));
		}
		if (s.getTitle()!=null){
			service.addProperty(usdlInstanceModel.createProperty(DCTERMS+"title"), usdlInstanceModel.createLiteral(s.getTitle()));
		}
		if (s.getAbstractDes()!=null){
			service.addProperty(usdlInstanceModel.createProperty(DCTERMS+"abstract"), usdlInstanceModel.createLiteral(s.getAbstractDes()));
		}
		if (s.getModified()!=null){
			service.addProperty(usdlInstanceModel.createProperty(DCTERMS+"modified"), usdlInstanceModel.createLiteral(formatter.format(s.getModified())));
		}
		if (s.getCreated()!=null){
			service.addProperty(usdlInstanceModel.createProperty(DCTERMS+"created"), usdlInstanceModel.createLiteral(formatter.format(s.getCreated())));
		}
		if (s.getHasClassification()!=null){
			service.addProperty(usdlInstanceModel.createProperty(USDL+"hasClassification"), usdlInstanceModel.createResource(s.getHasClassification()));
		}
		if (s.getUrl()!=null){
			service.addProperty(usdlInstanceModel.createProperty(FOAF+"page"), usdlInstanceModel.createResource(s.getUrl()));
		}
		if (s.getCertificateURL()!=null){
			service.addProperty(usdlInstanceModel.createProperty(OPTETMKP+"hasOPTETCertificate"), usdlInstanceModel.createResource(s.getCertificateURL()));
		}
		if (s.getTechnicalDescription()!=null){
			addTechnicalDescription(s.getTechnicalDescription());
			service.addProperty(usdlInstanceModel.createProperty(OPTETMKP+"hasTechnicalDescription"), usdlInstanceModel.createResource("http://TechnicalDescription" + s.getTechnicalDescription().getID())); 		
		}
	}

	public void displayUSDLmodel(String lang) {
		// write it to standard out
		usdlInstanceModel.write(System.out, lang, USDL);            
	}
}
