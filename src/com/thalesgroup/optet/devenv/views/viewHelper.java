/*
 *		OPTET Factory
 *
 *	Class viewHelper 1.0 26 août 2014
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.views;

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;



/**
 * @author F. Motte
 *
 */
public class viewHelper {

	private static viewHelper view= new viewHelper();
	
	private viewHelper() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static viewHelper GetInstance(){
		return view;
		
	}
	
	public void display(){
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow
			().getActivePage().showView("com.thalesgroup.optet.devenv.views.OptetTestCoverageView");
			PlatformUI.getWorkbench().getActiveWorkbenchWindow
			().getActivePage().showView("com.thalesgroup.optet.devenv.views.OptetAuditView");
			PlatformUI.getWorkbench().getActiveWorkbenchWindow
			().getActivePage().showView("com.thalesgroup.optet.devenv.views.OptetMetricView");
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
