/*
 *		OPTET Factory
 *
 *	Class CertificationInterfaceImpl 1.0 20 nov. 2013
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.jena;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.jena.riot.RDFDataMgr;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.thalesgroup.optet.devenv.Activator;
import com.thalesgroup.optet.devenv.crypto.CryptoPreferences;
import com.thalesgroup.optet.devenv.crypto.SignFile;

/**
 * @author F. Motte
 *
 */
public class CertificatInterfaceImpl implements  CertificatInterface
{
	Model model;

	String systemName;

	private SignFile signature;


	// the crypto preference define into the preference apge
	private CryptoPreferences pref = null;



	
	public CertificatInterfaceImpl(String systemName) {
		super();
		this.systemName = systemName;

		this.pref = new CryptoPreferences();

		signature = new SignFile();

		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		URL fileURL = bundle.getEntry("resources/dtwc.ttl");
		String file = null;
		try {
			file = FileLocator.resolve(fileURL).toURI().toString();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// TODO Auto-generated constructor stub



	}

	public String display(){
		model.write(System.out, "TURTLE");
		System.out.println("create certitifact file ");
		String certificatePath = pref.getCertificatePath();


		
		
		FileOutputStream output;
		try {
			output = new FileOutputStream(certificatePath);
			model.write(output, "RDF/XML");
			signature.signXMLData(certificatePath);
			
			output = new FileOutputStream(certificatePath.replace(".xml", ".ttl"));
			model.write(output, "TURTLE");
			return certificatePath;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}


}
