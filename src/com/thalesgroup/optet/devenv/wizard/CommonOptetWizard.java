/*
 *		OPTET Factory
 *
 *	Class CommonOptetWizard 1.0 16 oct. 2014
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.wizard;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jdt.ui.actions.ShowInPackageViewAction;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import com.thalesgroup.optet.common.exception.ProjectNotFoundException;
import com.thalesgroup.optet.common.jaxb.TWProfile.TWProfile;
import com.thalesgroup.optet.securerepository.SVNWrapper;
import com.thalesgroup.optet.securerepository.impl.SVNWrapperImpl;
import com.thalesgroup.optet.securerepository.impl.SvnEntry;
import  org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

/**
 * @author F. Motte
 *
 */
public class CommonOptetWizard  extends Wizard implements IWorkbenchWizard, INewWizard{


	private OptetWizardPage fThirdPage;
	private WizardNewProjectCreationPage fMainPage;
	private IWorkbench fWorkbench;
	
	public CommonOptetWizard() {
		super();
		setNeedsProgressMonitor(true);
	}



	@Override

	public String getWindowTitle() {

		return "Export My Data";

	}



	@Override

	public void addPages() {
		
		super.addPages();
		fMainPage= new WizardNewProjectCreationPage("NewProjectCreationWizard");
		fMainPage.setTitle("New");
		fMainPage.setDescription("Create a new XY project.");
		addPage(fMainPage);
		
		if (fThirdPage == null)
			fThirdPage = new OptetWizardPage();
		addPage(fThirdPage);
	}



	@Override

	public boolean performFinish() {

		boolean res= true;
		if (res) {
			
			WorkspaceModifyOperation op= new WorkspaceModifyOperation() {
				protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
					finishPage(monitor);
				}
			};
			try {
				getContainer().run(false, true, op);
			} catch (InvocationTargetException e) {
				return false; // TODO: should open error dialog and log
			} catch  (InterruptedException e) {
				return false; // canceled
			}
			
			SvnEntry svnentry = fThirdPage.getSelectedSVNEntry();
			IProject project= fMainPage.getProjectHandle();
			try {
				IFolder folder = project.getFolder("Optet");
				folder.create(true, true, null);
				IFile optetProp = folder.getFile("Optet.properties");

				Properties prop = new Properties();
				prop.setProperty("svn.project.selected", svnentry.getAuthor());
				prop.setProperty("project.type", "other");

				FileOutputStream out = new FileOutputStream(optetProp.getRawLocation().toFile());
				prop.store(out, "Optet properties");

				SVNWrapper svn = new SVNWrapperImpl();
				TWProfile twProfile = svn.getTWProfile(svnentry.getAuthor());


				JAXBContext jaxbContext = JAXBContext.newInstance(TWProfile.class);
				System.out.println("jaxbContext.createMarshaller();");
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				System.out.println(" jaxbMarshaller.marshal");
				

				IFile twProfileFile = folder.getFile("TWProfile.xml");
				FileOutputStream outTWProfile = new FileOutputStream(twProfileFile.getRawLocation().toFile());
				jaxbMarshaller.marshal(twProfile, outTWProfile);
				System.out.println("end marshall");
				//if (fThirdPage.mustCheckoutSource()){

					try {
						System.out.println("svnentry.getAuthor()" + svnentry.getAuthor());
						svn.getFileFromSVN(svnentry.getAuthor(), project);
					} catch (ProjectNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				//}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



			System.out.println("rrrrrrrrrrrrrrrrrr " + svnentry.getAuthor());

//			Display.getDefault().asyncExec(new Runnable() {
//				public void run() {
//					IWorkbenchPart activePart= getActivePart();
//					if (activePart instanceof IPackagesViewPart) {
//						(new ShowInPackageViewAction(activePart.getSite())).run(newElement);
//					}
//				}
//			});
		}
		return res;

	}
	private void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
		if (monitor == null) {
			monitor= new NullProgressMonitor();
		}
		try {
			monitor.beginTask("Creating XY project...", 3); // 3 steps

			IProject project= fMainPage.getProjectHandle();
			IPath locationPath= fMainPage.getLocationPath();

			// create the project
			IProjectDescription desc= project.getWorkspace().newProjectDescription(project.getName());
			if (!fMainPage.useDefaults()) {
				desc.setLocation(locationPath);
			}
			project.create(desc, new SubProgressMonitor(monitor, 1));
			project.open(new SubProgressMonitor(monitor, 1));

			// TODO: configure your page / nature

			// change to the perspective specified in the plugin.xml
			BasicNewResourceWizard.selectAndReveal(project, fWorkbench.getActiveWorkbenchWindow());

		} finally {
			monitor.done();
		}
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection arg1) {
		fWorkbench= workbench;
		
	}

//	private IWorkbenchPart getActivePart() {
//		IWorkbenchWindow activeWindow= getWorkbench().getActiveWorkbenchWindow();
//		if (activeWindow != null) {
//			IWorkbenchPage activePage= activeWindow.getActivePage();
//			if (activePage != null) {
//				return activePage.getActivePart();
//			}
//		}
//		return null;
//	}



}
