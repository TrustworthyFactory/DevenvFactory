package com.thalesgroup.optet.devenv.handlers;

import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.thalesgroup.optet.devenv.datamodel.InternalSystemDM;
import com.thalesgroup.optet.devenv.utils.TWProfileUtil;
import com.thalesgroup.optet.securerepository.SVNWrapper;
import com.thalesgroup.optet.securerepository.impl.SVNWrapperImpl;
import com.thalesgroup.optet.twmanagement.impl.SettingsModuleImpl;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class OptetHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public OptetHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event)
			throws ExecutionException
			{
		try
		{
			ListSelectionDialog dlg = new ListSelectionDialog(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow
					().getShell(),
					ResourcesPlugin.getWorkspace().getRoot(), 
					new BaseWorkbenchContentProvider(),
					new WorkbenchLabelProvider(),
					"Select the Project:");
			dlg.setTitle("Project Selection");
			dlg.open();
			Object[] selectionResult = dlg.getResult();
			for (int i = 0; i < selectionResult.length; i++) {
				if (selectionResult[i] instanceof IProject) {
					InternalSystemDM data = InternalSystemDM.getInternalSystemDM();
					IProject project = (IProject)selectionResult[i];
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
						SettingsModuleImpl.getInstance()
						.setTWProfile(projectID,TWProfileUtil.getLocalTWProfile(project));
//						SettingsModuleImpl.getInstance()
//						.setTWProfile(projectID,svn.getTWProfile(projectID));
					}
				}
			}



			PlatformUI.getWorkbench().getActiveWorkbenchWindow
			().getActivePage().showView("com.thalesgroup.optet.devenv.views.DashboardView");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
			}
}
