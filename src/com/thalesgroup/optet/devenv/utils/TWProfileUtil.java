/*
 *		OPTET Factory
 *
 *	Class TWProfileUtil 1.0 8 oct. 2014
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */
package com.thalesgroup.optet.devenv.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.thalesgroup.optet.common.jaxb.TWProfile.TWProfile;

/**
 * @author F. Motte
 *
 */
public class TWProfileUtil {
	public static TWProfile getLocalTWProfile(IProject project ){
		
		JAXBContext jaxbContext;
		TWProfile twProfile = null;
		try {
			jaxbContext = JAXBContext.newInstance(TWProfile.class);
			System.out.println("jaxbContext.createUnmarshaller();");
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			System.out.println(" jaxbUnmarshaller.unmarshal");
			twProfile = (TWProfile) jaxbUnmarshaller.unmarshal(project.getFile("/Optet/TWProfile.xml").getContents());
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return twProfile;
	}

}
