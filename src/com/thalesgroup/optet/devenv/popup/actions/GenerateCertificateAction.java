package com.thalesgroup.optet.devenv.popup.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

//import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.thalesgroup.optet.common.data.OptetDataModel;
import com.thalesgroup.optet.devenv.Activator;
import com.thalesgroup.optet.devenv.datamodel.InternalSystemDM;
import com.thalesgroup.optet.devenv.utils.TWProfileUtil;
import com.thalesgroup.optet.securerepository.SVNWrapper;
import com.thalesgroup.optet.securerepository.impl.SVNWrapperImpl;
import com.thalesgroup.optet.twmanagement.impl.SettingsModuleImpl;


//import eu.optet.audittools.checkstyle.CheckstyleUtil;
//import eu.optet.audittools.codepro.CodeproUtil;
//import eu.optet.audittools.findbugs.FindbugsUtil;
//import eu.optet.audittools.pmd.PMDUtils;


/**
 * GenerateCertificateAction generate the certiicate of the process
 * running the audit and the metric
 * 
 * @author F. Motte
 *
 */
public class GenerateCertificateAction implements IObjectActionDelegate {

	// the current selection
	private ISelection selection;

	//the current project
	private IProject project;

	/**
	 * Constructor for Action1.
	 */
	public GenerateCertificateAction() {
		super();
	}


	/**
	 * return the list of file content into the resource
	 * @param files list of file to return
	 * @param resource the selection 
	 * @param extension return the file only with this extention
	 * @throws CoreException
	 */
	private static void listFiles(List files, IResource resource, String extension) throws CoreException {
		// the resource is a folder
		if (resource instanceof IFolder) {
			IResource[] res = ((IFolder)resource).members();
			for (int i = 0; i < res.length; i++) {
				listFiles(files, res[i], extension);
			}
		}
		else if (resource instanceof IFile) {
			if (((IFile)resource).getFileExtension().endsWith(extension)){
				files.add(resource);
			}
		}
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {

		try
		{
//			ListSelectionDialog dlg = new ListSelectionDialog(
//					PlatformUI.getWorkbench().getActiveWorkbenchWindow
//					().getShell(),
//					ResourcesPlugin.getWorkspace().getRoot(), 
//					new BaseWorkbenchContentProvider(),
//					new WorkbenchLabelProvider(),
//					"Select the Project:");
//			dlg.setTitle("Project Selection");
//			dlg.open();
//			Object[] selectionResult = dlg.getResult();
//			for (int i = 0; i < selectionResult.length; i++) {
//				if (selectionResult[i] instanceof IProject) {
					InternalSystemDM data = InternalSystemDM.getInternalSystemDM();
//					IProject project = (IProject)selectionResult[i];
					data.setCurrentProject(project);

					Properties prop = new Properties();
					String projectID = null;

					try {
						prop.load(project.getFile("/Optet/Optet.properties").getContents());
						projectID = prop.getProperty("svn.project.selected");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (CoreException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

//					SVNWrapper svn = new SVNWrapperImpl();
					if (project != null){
//						SettingsModuleImpl.getInstance()
//						.setTWProfile(projectID,svn.getTWProfile(projectID));
						SettingsModuleImpl.getInstance()
						.setTWProfile(projectID,TWProfileUtil.getLocalTWProfile(project));					}
//				}
//			}



			PlatformUI.getWorkbench().getActiveWorkbenchWindow
			().getActivePage().showView("com.thalesgroup.optet.devenv.views.DashboardView");
		} catch (PartInitException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// save the current selection
		this.selection = selection;

		// save the current project coming from the selection 
		if(selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection)selection).getFirstElement();


			if (element instanceof IResource) {
				project= ((IResource)element).getProject();
			} else if (element instanceof PackageFragmentRootContainer) {
				IJavaProject jProject = 
						((PackageFragmentRootContainer)element).getJavaProject();
				project = jProject.getProject();
			} else if (element instanceof IJavaElement) {
				IJavaProject jProject= ((IJavaElement)element).getJavaProject();
				project = jProject.getProject();
			}
		} 
	}

}
