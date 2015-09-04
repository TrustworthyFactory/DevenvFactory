/*
 *		OPTET Factory
 *
 *	Class CommitSourceAction 1.0 5 nov. 2013
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.popup.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import org.eclipse.ui.dialogs.ResourceSelectionDialog;

import com.thalesgroup.optet.common.exception.ProjectNotFoundException;
import com.thalesgroup.optet.securerepository.SVNWrapper;
import com.thalesgroup.optet.securerepository.impl.SVNWrapperImpl;

/**
 * @author F. Motte
 *
 */
public class CommitSourceAction implements IObjectActionDelegate {

	// the current selection
	private Shell shell;

	private ISelection selection = null; 

	private IProject currentProject = null;


	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction arg0) {

		ResourceSelectionDialog dialog = new ResourceSelectionDialog(shell,
				currentProject,
				"Select ressource:");
		dialog.setTitle("commit resources to secure repository");
		dialog.open();
		Object[] fileToCommit = dialog.getResult();
		List<IFile> commitedList = new ArrayList<>();
		IProject currentProject = null;
		for (int i = 0; i < fileToCommit.length; i++) {
			if (fileToCommit[i] instanceof IFile) {
				IFile fileToAdd = (IFile) fileToCommit[i];
				commitedList.add(fileToAdd);
				currentProject = fileToAdd.getProject();
				System.out.println(fileToAdd);
			}
		}

		Properties properties = new Properties();
		String projectID = null;
		try {
			properties.load(currentProject.getFile("/Optet/Optet.properties").getContents());
			projectID = properties.getProperty("svn.project.selected");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		SVNWrapper svn = new SVNWrapperImpl();
		try {
			if (projectID != null){
				svn.setFilesToSVN(projectID, commitedList);
			}
		} catch (ProjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MessageBox dialog2 = 
				  new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
				dialog2.setText("Commit source");
				dialog2.setMessage("The commit is finished");
				dialog2.open(); 
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// save the current selection
		this.selection = selection;
		System.out.println("selectionchange");
		// save the current project coming from the selection 
		if(selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection)selection).getFirstElement();
			System.out.println(element);
			if (element instanceof IResource) {
				System.out.println("is a resource");
				currentProject= ((IResource)element).getProject();
				System.out.println("project name "+ currentProject.getName());
			} else if (element instanceof IProject) {
				System.out.println("is a poject");
				currentProject= (IProject)element;
			}else if (element instanceof IFolder) {
				System.out.println("is a folder");
				currentProject= ((IFolder)element).getProject();
			} else if (element instanceof PackageFragmentRootContainer) {
				System.out.println("is a package");
				IJavaProject jProject = 
						((PackageFragmentRootContainer)element).getJavaProject();
				currentProject = jProject.getProject();
			} else if (element instanceof IJavaElement) {
				System.out.println("is a java");
				IJavaProject jProject= ((IJavaElement)element).getJavaProject();
				currentProject = jProject.getProject();
			}
		} 

	}



	/* (non-Javadoc)
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();

	}

}
