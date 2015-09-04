package com.thalesgroup.optet.devenv.wizard;


import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageTwo;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import org.eclipse.jdt.core.IJavaElement;

import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jdt.ui.actions.ShowInPackageViewAction;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;

import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;

import com.thalesgroup.optet.common.exception.ProjectNotFoundException;
import com.thalesgroup.optet.common.jaxb.TWProfile.TWProfile;
import com.thalesgroup.optet.securerepository.SVNWrapper;
import com.thalesgroup.optet.securerepository.impl.SVNWrapperImpl;
import com.thalesgroup.optet.securerepository.impl.SvnEntry;
/**
 * This is the wizard to create an optet document.
 */

public class OptetWizard extends NewElementWizard implements IExecutableExtension {

	private NewJavaProjectWizardPageOne fFirstPage;
	private NewJavaProjectWizardPageTwo fSecondPage;
	private OptetWizardPage fThirdPage;

	private IConfigurationElement fConfigElement;

	public OptetWizard() {
		this(null, null);
	}

	public OptetWizard(NewJavaProjectWizardPageOne pageOne, NewJavaProjectWizardPageTwo pageTwo) {
		setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWJPRJ);
		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
		setWindowTitle("New Optet Project"); 

		fFirstPage= pageOne;
		fSecondPage= pageTwo;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		if (fFirstPage == null)
			fFirstPage= new NewJavaProjectWizardPageOne();
		addPage(fFirstPage);

		if (fSecondPage == null)
			fSecondPage= new NewJavaProjectWizardPageTwo(fFirstPage);
		addPage(fSecondPage);

		if (fThirdPage == null)
			fThirdPage = new OptetWizardPage();
		addPage(fThirdPage);
		fFirstPage.init(getSelection(), getActivePart());
	}		

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.wizards.NewElementWizard#finishPage(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
		fSecondPage.performFinish(monitor); // use the full progress monitor
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		boolean res= super.performFinish();
		if (res) {
			final IJavaElement newElement= getCreatedElement();

			IWorkingSet[] workingSets= fFirstPage.getWorkingSets();
			if (workingSets.length > 0) {
				PlatformUI.getWorkbench().getWorkingSetManager().addToWorkingSets(newElement, workingSets);
			}

			BasicNewProjectResourceWizard.updatePerspective(fConfigElement);
			selectAndReveal(fSecondPage.getJavaProject().getProject());				

			SvnEntry svnentry = fThirdPage.getSelectedSVNEntry();
			IProject project = fSecondPage.getJavaProject().getProject();
			try {
				IFolder folder = project.getFolder("Optet");
				folder.create(true, true, null);
				IFile optetProp = folder.getFile("Optet.properties");

				Properties prop = new Properties();
				prop.setProperty("svn.project.selected", svnentry.getAuthor());
				prop.setProperty("project.type", "java");

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

			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPart activePart= getActivePart();
					if (activePart instanceof IPackagesViewPart) {
						(new ShowInPackageViewAction(activePart.getSite())).run(newElement);
					}
				}
			});
		}
		return res;
	}



	private IWorkbenchPart getActivePart() {
		IWorkbenchWindow activeWindow= getWorkbench().getActiveWorkbenchWindow();
		if (activeWindow != null) {
			IWorkbenchPage activePage= activeWindow.getActivePage();
			if (activePage != null) {
				return activePage.getActivePart();
			}
		}
		return null;
	}

	protected void handleFinishException(Shell shell, InvocationTargetException e) {
		String title= NewWizardMessages.JavaProjectWizard_op_error_title; 
		String message= NewWizardMessages.JavaProjectWizard_op_error_create_message;			 
		ExceptionHandler.handle(e, getShell(), title, message);
	}	

	/*
	 * Stores the configuration element for the wizard.  The config element will be used
	 * in <code>performFinish to set the result perspective.
	 */
	public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data) {
		fConfigElement= cfig;
	}

	/* (non-Javadoc)
	 * @see IWizard#performCancel()
	 */
	public boolean performCancel() {
		fSecondPage.performCancel();
		return super.performCancel();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.wizards.NewElementWizard#getCreatedElement()
	 */
	public IJavaElement getCreatedElement() {
		return fSecondPage.getJavaProject();
	}
}