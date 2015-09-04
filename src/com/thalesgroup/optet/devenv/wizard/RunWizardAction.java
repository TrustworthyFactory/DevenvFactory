/*
 * Created on Feb 26, 2005
 */
package com.thalesgroup.optet.devenv.wizard;

import javax.swing.JOptionPane;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

public class RunWizardAction extends Action implements IWorkbenchWindowActionDelegate {

    /** Called when the action is created. */ 
	public void init(IWorkbenchWindow window) {
	}

	/** Called when the action is discarded. */ 
	public void dispose() {
	}

	/** Called when the action is executed. */ 
	public void run(IAction action) {
		System.out.println("zzzzzzzzzzzzzzzzzzz   run");
		OptetWizard wizard= new OptetWizard();
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		WizardDialog dialog= new WizardDialog(shell, wizard);
		dialog.create();
		dialog.open();
	}

	/** Called when objects in the editor are selected or deselected. */ 
	public void selectionChanged(IAction action, ISelection selection) {
	}
}
