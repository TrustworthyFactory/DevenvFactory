/*
 *		OPTET Factory
 *
 *	Class AndroidOptetWizard 1.0 24 juil. 2014
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.wizard;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jdt.ui.actions.ShowInPackageViewAction;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageTwo;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard;
import com.thalesgroup.optet.common.exception.ProjectNotFoundException;
import com.thalesgroup.optet.common.jaxb.TWProfile.TWProfile;
import com.thalesgroup.optet.securerepository.SVNWrapper;
import com.thalesgroup.optet.securerepository.impl.SVNWrapperImpl;
import com.thalesgroup.optet.securerepository.impl.SvnEntry;

/**
 * @author F. Motte
 *
 */
public class AndroidOptetWizard extends NewProjectWizard{

	private OptetWizardPage fThirdPage;
	private IConfigurationElement fConfigElement;


	@Override
	public boolean canFinish() {
		// TODO Auto-generated method stub
		System.out.println("canFinish" +  super.canFinish());
		return  super.canFinish();
	}
	@Override
	protected List<Change> computeChanges() {
		// TODO Auto-generated method stub
		System.out.println("computeChanges");
		return super.computeChanges();
	}

	@Override
	protected List<String> getFilesToOpen() {
		// TODO Auto-generated method stub
		return super.getFilesToOpen();
	}

	@Override
	protected List<Runnable> getFinalizingActions() {
		// TODO Auto-generated method stub
		System.out.println("getFinalizingActions");
		return super.getFinalizingActions();
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		// TODO Auto-generated method stub
		System.out.println("next page " + page.getName());
		if (page.getName().equals("newTemplatePage"))
			return fThirdPage;
		return super.getNextPage(page);
	}

	@Override
	protected IProject getProject() {
		// TODO Auto-generated method stub
		return super.getProject();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		System.out.println("Perform init");
		super.init(workbench, selection);
	}

	@Override
	protected boolean performFinish(IProgressMonitor monitor)
			throws InvocationTargetException {
		// TODO Auto-generated method stub

		super.performFinish(monitor);
		
		System.out.println("Perform finish");

		SvnEntry svnentry = fThirdPage.getSelectedSVNEntry();
		IProject project = getProject();
		try {
			IFolder folder = project.getFolder("Optet");
			folder.create(true, true, null);
			IFile optetProp = folder.getFile("Optet.properties");

			Properties prop = new Properties();
			prop.setProperty("svn.project.selected", svnentry.getAuthor());
			prop.setProperty("project.type", "android");

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
			if (fThirdPage.mustCheckoutSource()){

				try {
					svn.getFileFromSVN(svnentry.getAuthor(), project);
				} catch (ProjectNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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



		return true;
	}

	public AndroidOptetWizard() {
		setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWJPRJ);
		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
		setWindowTitle("New Android Optet Project"); 
	}

	@Override
	public void addPages() {
		System.out.println("add pages");
		// TODO Auto-generated method stub
		super.addPages();
		if (fThirdPage == null)
			fThirdPage = new OptetWizardPage();
		addPage(fThirdPage);
	}

	//	/* (non-Javadoc)
	//	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
	//	 */
	//	@Override
	//	public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data) throws CoreException {
	//		fConfigElement= cfig;
	//		
	//	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	//	public boolean performFinish() {
	//		boolean res= super.performFinish();
	//		if (res) {
	//			final IJavaElement newElement= getCreatedElement();
	//
	//			IWorkingSet[] workingSets= fFirstPage.getWorkingSets();
	//			if (workingSets.length > 0) {
	//				PlatformUI.getWorkbench().getWorkingSetManager().addToWorkingSets(newElement, workingSets);
	//			}
	//
	//			BasicNewProjectResourceWizard.updatePerspective(fConfigElement);
	//			selectAndReveal(fSecondPage.getJavaProject().getProject());				
	//
	//			SvnEntry svnentry = fThirdPage.getSelectedSVNEntry();
	//			IProject project = fSecondPage.getJavaProject().getProject();
	//			try {
	//				IFolder folder = project.getFolder("Optet");
	//				folder.create(true, true, null);
	//				IFile optetProp = folder.getFile("Optet.properties");
	//				String prop = "svn.project.selected=" + svnentry.getAuthor();
	//				optetProp.create(new ByteArrayInputStream(prop.getBytes()), true, null);
	//			} catch (CoreException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			}
	//			
	//			if (fThirdPage.mustCheckoutSource()){
	//				SVNWrapper svn = new SVNWrapperImpl();
	//				try {
	//					svn.getFileFromSVN(svnentry.getAuthor(), project);
	//
	//
	//				} catch (ProjectNotFoundException e) {
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				}
	//				
	//				
	//			}
	//			
	//			
	//			System.out.println("rrrrrrrrrrrrrrrrrr " + svnentry.getAuthor());
	//			
	//			Display.getDefault().asyncExec(new Runnable() {
	//				public void run() {
	//					IWorkbenchPart activePart= getActivePart();
	//					if (activePart instanceof IPackagesViewPart) {
	//						(new ShowInPackageViewAction(activePart.getSite())).run(newElement);
	//					}
	//				}
	//			});
	//		}
	//		return res;
	//	}




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

	//	protected void handleFinishException(Shell shell, InvocationTargetException e) {
	//		String title= NewWizardMessages.JavaProjectWizard_op_error_title; 
	//		String message= NewWizardMessages.JavaProjectWizard_op_error_create_message;			 
	//		ExceptionHandler.handle(e, getShell(), title, message);
	//	}	
}
