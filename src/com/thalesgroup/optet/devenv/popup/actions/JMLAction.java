package com.thalesgroup.optet.devenv.popup.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.jmlspecs.openjml.Factory;
import org.jmlspecs.openjml.IAPI;
import org.jmlspecs.openjml.Main;
import org.osgi.framework.Bundle;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
//import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import org.eclipse.ui.handlers.HandlerUtil;
//import eu.optet.audittools.checkstyle.CheckstyleUtil;
//import eu.optet.audittools.codepro.CodeproUtil;
//import eu.optet.audittools.findbugs.FindbugsUtil;
//import eu.optet.audittools.pmd.PMDUtils;





















import com.thalesgroup.optet.common.data.OptetDataModel;
import com.thalesgroup.optet.devenv.Activator;
import com.thalesgroup.optet.devenv.jml.ComputeJMLInput;
import com.thalesgroup.optet.devenv.jml.JMLStaticSelectionView;
import com.thalesgroup.optet.devenv.jml.StaticJMLConf;
import com.thalesgroup.optet.devenv.problemdefinition.ProblemDefinitionView;
import com.thalesgroup.optet.devenv.utils.TWProfileUtil;
import com.thalesgroup.optet.twmanagement.OrchestrationInterface;
import com.thalesgroup.optet.twmanagement.impl.OrchestrationImpl;
import com.thalesgroup.optet.twmanagement.impl.SettingsModuleImpl;

import java.net.URLClassLoader;

/**
 * AuditsAction realise the audit of the code
 * 
 * @author F. Motte
 *
 */
public class JMLAction implements IObjectActionDelegate {

	// the current selection
	private ISelection selection;

	//the current project
	private IProject project;
	private IJavaProject javaProject;


	/** Caches the value of the window, when informed of it. */
	protected IWorkbenchWindow window;

	/** Caches the value of the shell in which the window exists. */
	protected Shell shell = null;



	/**
	 * return the list of file content into the resource
	 * @param files list of file to return
	 * @param resource the selection 
	 * @param extension return the file only with this extention
	 * @throws CoreException
	 */
	private static void listFiles(List<IFile> files, IResource resource, String extension) throws CoreException {
		// the resource is a folder
		if (resource instanceof IFolder) {
			IResource[] res = ((IFolder)resource).members();
			for (int i = 0; i < res.length; i++) {
				listFiles(files, res[i], extension);
			}
		}
		else if (resource instanceof IFile) {
			if (((IFile)resource).getFileExtension().endsWith(extension)){
				files.add((IFile) resource);
			}
		}
	}
	/**
	 * Constructor for Action1.
	 */
	public JMLAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}


	private static IClasspathEntry getContainer(IJavaProject javaProject, String containerPath) throws JavaModelException {
		IClasspathEntry[] cp = javaProject.getRawClasspath();
		for(int i = 0; i < cp.length; i++ ) {
			if(IClasspathEntry.CPE_CONTAINER == cp[i].getEntryKind()
					&& containerPath.equals(cp[i].getPath().lastSegment())) {
				return cp[i];
			}
		}
		return null;
	}



	public void run(IAction action) {

		System.out.println("start JML2");	    

		try {

			
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
			
//			SVNWrapper svn = new SVNWrapperImpl();
			if (project != null){
//				SettingsModuleImpl.getInstance()
//					.setTWProfile(projectID,svn.getTWProfile(projectID));
				SettingsModuleImpl.getInstance()
				.setTWProfile(projectID,TWProfileUtil.getLocalTWProfile(project));
			}
			
			IJavaProject jp = null;
			if (project instanceof IResource) {
				jp = JavaCore.create(((IResource) project).getProject());
			} else if (project instanceof IJavaElement) {
				jp = ((IJavaElement) project).getJavaProject();
			} else {

			}

			final IClasspathEntry[] resolvedClasspath = jp.getResolvedClasspath(true);
			for (IClasspathEntry classpathEntry : resolvedClasspath) {
				System.out.println("sssssssssssss" + classpathEntry.getPath().makeAbsolute().toFile().getCanonicalFile().toURL());
			}


			IType[] types= org.eclipse.jdt.junit.JUnitCore.findTestTypes(jp, null);
			ArrayList<String> names=new ArrayList<String>();
			for (  IType type : types) {
				names.add(type.getFullyQualifiedName());
				System.out.println("name " + type.getPath().toOSString());			
			}

			IClasspathEntry[] classpath = jp.getResolvedClasspath(true);
			for (int i = 0; i < classpath.length; i++) {
				System.out.println(classpath[i].getPath().toString());
			}

			IPackageFragmentRoot[] fragments = jp.getAllPackageFragmentRoots();

			for (int i = 0; i < fragments.length; i++) {
				if (fragments[i].getKind() == IPackageFragmentRoot.K_SOURCE){
					System.out.println("fragments[i]" + fragments[i].getPath().toOSString());

				}
			}






			System.out.println("end JML");
		} catch (Exception e) {
			e.printStackTrace();

		}


		//		JMLStaticSelectionView box = new JMLStaticSelectionView(shell,SWT.DIALOG_TRIM, project);
		//		box.open();

		
	    try {
			if (project.hasNature(JavaCore.NATURE_ID)) {
				System.out.println("it's a java project");
				javaProject = JavaCore.create(project);
			}else{
				System.out.println("it's not a java project");
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (javaProject!=null)
		{
			OptetDataModel.getInstance().cleanMetric();
			
			OrchestrationInterface orchestration  = OrchestrationImpl.getInstance();
			orchestration.staticJMLAnalyse(project);
			
//			ComputeJMLInput compute = new ComputeJMLInput(javaProject, shell);
//			compute.run();
		}
	}


	protected void getInfo(ExecutionEvent event) throws ExecutionException {
		window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		shell = window.getShell();
		selection = window.getSelectionService().getSelection();
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
				javaProject = jProject;
			} else if (element instanceof IJavaElement) {
				IJavaProject jProject= ((IJavaElement)element).getJavaProject();
				project = jProject.getProject();
				javaProject = jProject;
			}
		} 
	}

}
