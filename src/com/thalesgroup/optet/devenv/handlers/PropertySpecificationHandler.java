/*
 *		OPTET Factory
 *
 *	Class PropertyDefinitionHandler 1.0 28 oct. 2013
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;


import com.thalesgroup.optet.devenv.propertyspecification.PropertySpecificationView;

/**
 * @author F. Motte
 *
 */
public class PropertySpecificationHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public PropertySpecificationHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		PropertySpecificationView box = new PropertySpecificationView(window.getShell(),SWT.DIALOG_TRIM);
		box.open();
		return null;
	}
}