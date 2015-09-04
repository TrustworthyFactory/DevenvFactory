package com.thalesgroup.optet.devenv.compilation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
/*
 *		OPTET Factory
 *
 *	Class MavenBuilder 1.0 1 sept. 2014
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.m2e.internal.launch.MavenLaunchDelegate;
import java.io.File;

/**
 * @author F. Motte
 *
 */
public class MavenBuilder {
	public void build(IProject project, File launcher, String option){

		
		ExecutePomAction mvn  = new ExecutePomAction();
		mvn.setInitializationData(null, null, option);
		mvn.launch(project, ILaunchManager.RUN_MODE);
		System.out.println("end of maven");

	}
}
