package com.thalesgroup.optet.devenv.popup.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.thalesgroup.optet.common.data.OptetDataModel;
import com.thalesgroup.optet.devenv.Activator;
import com.thalesgroup.optet.devenv.utils.TWProfileUtil;
import com.thalesgroup.optet.securerepository.SVNWrapper;
import com.thalesgroup.optet.securerepository.impl.SVNWrapperImpl;
import com.thalesgroup.optet.twmanagement.Evidence;
import com.thalesgroup.optet.twmanagement.OrchestrationInterface;
import com.thalesgroup.optet.twmanagement.impl.OrchestrationImpl;
import com.thalesgroup.optet.twmanagement.impl.SettingsModuleImpl;

//import com.instantiations.assist.eclipse.coverage.model.CodeCoverageReport;
//import com.instantiations.assist.eclipse.coverage.model.CodeCoverageReportManager;
//import com.instantiations.assist.eclipse.coverage.model.PackageCodeCoverage;
//import com.instantiations.assist.eclipse.coverage.model.ProjectCodeCoverage;
//
//
//import eu.optet.audittools.codepro.CodeproUtil;
//import eu.optet.audittools.jdepends.JdependUtil;



/**
 * MetricsAction generate metric on the code
 * 
 * @author F. Motte
 *
 */
public class MetricsAction implements IObjectActionDelegate {

	// the current selection
	private ISelection selection;

	// the current project
    private IProject project;
    
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
	 * Constructor for Action1.
	 */
	public MetricsAction() {
		super();
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
		
		
		if (this.project == null)
			return;

		
		List<IFile> fileList = new ArrayList<IFile>();
		List<?> files = (selection instanceof IStructuredSelection) ? ((IStructuredSelection)selection).toList() : Collections.EMPTY_LIST;

		// construct the list of file which must be analysed
		for (Iterator<?> i = files.iterator(); i.hasNext();)
		{
			IResource resource = (IResource) i.next();
			if (resource instanceof IFile) {
				fileList.add(((IFile)resource));
			}
			if (resource instanceof IFolder) {
				try {
					listFiles(fileList, resource,"java");
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}


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
		
//		SVNWrapper svn = new SVNWrapperImpl();
		if (project != null){
//			SettingsModuleImpl.getInstance()
//				.setTWProfile(projectID,svn.getTWProfile(projectID));
			SettingsModuleImpl.getInstance()
			.setTWProfile(projectID,TWProfileUtil.getLocalTWProfile(project));		}



		// clean the previous audit
		Activator.getDefault().logInfo("start audit");
		OptetDataModel.getInstance().cleanAudit();


		OrchestrationInterface orchestration  = OrchestrationImpl.getInstance();

		OptetDataModel.getInstance().cleanMetric();
//		OptetDataModel.getInstance().cleanExpectedMetric();
//		
//		// load expected value
//		List<Evidence> evidences = SettingsModuleImpl.getInstance().getEvidences(projectID);
//		for (Iterator iterator = evidences.iterator(); iterator.hasNext();) {
//			Evidence evidence = (Evidence) iterator.next();
//			Map<String, com.thalesgroup.optet.twmanagement.Metric> metricMap = evidence.getMetrics();
//			
//			
//			for (Map.Entry<String, com.thalesgroup.optet.twmanagement.Metric> entry : metricMap.entrySet()) {
//				System.out.println("add expected value  : " + entry.getKey() + " Value : "
//					+ entry.getValue().getMetricExpectedValue());
//				OptetDataModel.getInstance().addExpectedMetric(entry.getKey(), entry.getValue().getMetricExpectedValue());
//			}
//		}

		
		
		orchestration.staticAndRuntimeAnalyse(project, "dev");
		OptetDataModel.getInstance().computeRulesMetric();


		Activator.getDefault().logInfo("start runtest M");

		//orchestration.runtimeAnalyse(project);
		
		//OptetDataModel.getInstance().computeMetric();
		
		System.out.println("ici job");
//				TrivialJob job = new TrivialJob("myjob");
//				job.schedule();
//				job.run(null);
//				try {
//					job.join();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}


	     
	     
//		try {
//			PlatformUI.getWorkbench().getActiveWorkbenchWindow
//			().getActivePage().showView("com.thalesgroup.optet.devenv.views.OptetTestCoverageView");
//			PlatformUI.getWorkbench().getActiveWorkbenchWindow
//			().getActivePage().showView("com.thalesgroup.optet.devenv.views.OptetAuditView");
//			PlatformUI.getWorkbench().getActiveWorkbenchWindow
//			().getActivePage().showView("com.thalesgroup.optet.devenv.views.OptetMetricView");
//		} catch (PartInitException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
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
