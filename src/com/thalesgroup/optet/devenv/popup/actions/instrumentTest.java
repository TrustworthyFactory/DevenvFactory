package com.thalesgroup.optet.devenv.popup.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.thalesgroup.optet.devenv.Activator;
import com.thalesgroup.optet.twmanagement.OrchestrationInterface;
import com.thalesgroup.optet.twmanagement.impl.OrchestrationImpl;

//import com.instantiations.assist.eclipse.coverage.model.CodeCoverageReportManager;
//import com.instantiations.assist.eclipse.coverage.util.ProjectUtilities;

/**
 * instrument Test rune the JUnit test of the project
 * @author F. Motte
 *
 */
public class instrumentTest extends Action
implements IWorkbenchWindowActionDelegate, IActionDelegate
{
	// the current selection
	private ISelection selection;

	// the current project
	private IProject project = null;

	/**
	 * constructor
	 */
	public instrumentTest()
	{
		setEnabled(true);
	}


	/**
	 * constructor
	 * @param label
	 */
	public instrumentTest(String label)
	{
		super(label);
		setEnabled(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window)
	{
	}

	/**
	 * @param action
	 */
	public void init(IAction action)
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
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

	/**
	 * @param action
	 * @param event
	 */
	public void runWithEvent(IAction action, Event event)
	{
		run(action);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action)
	{
		if (this.project == null)
			return;

		Activator.getDefault().logInfo("start runtest");

		try {
		OrchestrationInterface orchestration  = OrchestrationImpl.getInstance();
		orchestration.runtimeAnalyse(project);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow
			().getActivePage().showView("com.thalesgroup.optet.devenv.views.OptetTestCoverageView");
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		// set the current project has "code coverage"
//		ProjectUtilities.setHasCodeCoverageNature(project, true);
//		TestRunRecorder listener = new TestRunRecorder();
//		JUnitCore.addTestRunListener(listener);
//
//
//		// add listener 
//		CodeCoverageReportManager manager = CodeCoverageReportManager.getInstance();
//		OptetCoverageReportManagerListener lis = new OptetCoverageReportManagerListener(project, listener);
//		manager.addCoverageReportManagerListener(lis);
//
//		// launch the unit test
//		JUnitLaunchShortcut shortcut = new JUnitLaunchShortcut();
//		shortcut.launch(selection, ILaunchManager.RUN_MODE);


	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose()
	{
	}


}