package com.thalesgroup.optet.devenv.handlers;

import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.thalesgroup.optet.common.data.OptetDataModel;
import com.thalesgroup.optet.devenv.Activator;
import com.thalesgroup.optet.devenv.datamodel.InternalSystemDM;
import com.thalesgroup.optet.devenv.evidencesview.EvidencesView;
import com.thalesgroup.optet.twmanagement.OrchestrationInterface;
import com.thalesgroup.optet.twmanagement.impl.OrchestrationImpl;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class AutomaticEvidencesHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public AutomaticEvidencesHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		InternalSystemDM data = InternalSystemDM.getInternalSystemDM();
		Properties prop = new Properties();
		String projectID = null;
		String projectType = null;
		try {
			prop.load(data.getCurrentProject().getFile("/Optet/Optet.properties").getContents());
			projectID = prop.getProperty("svn.project.selected");
			projectType = prop.getProperty("project.type");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (projectType.equals("OPA")){
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
			OptetDataModel.getInstance().cleanAudit();
			OptetDataModel.getInstance().cleanMetric();
			EvidencesView box = new EvidencesView(window.getShell(),SWT.DIALOG_TRIM, false);
			box.open();
		}else{

			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
			// create dialog with ok and cancel button and info icon
			MessageBox dialog = 
					new MessageBox(window.getShell(), SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
			dialog.setText("Analysis");
			dialog.setMessage("Please, wait during the analysis");

			// open dialog and await user selection
			dialog.open(); 


			Activator.getDefault().logInfo("start audit");


			OrchestrationInterface orchestration  = OrchestrationImpl.getInstance();
			OptetDataModel.getInstance().cleanAudit();
			OptetDataModel.getInstance().cleanMetric();
			orchestration.staticAndRuntimeAnalyse(data.getCurrentProject(), "certification");
			OptetDataModel.getInstance().computeRulesMetric();
		}

		//		OptetDataModel.getInstance().cleanMetric();
		//		OptetDataModel.getInstance().computeMetric();
		//
		//		Activator.getDefault().logInfo("end audit");
		//
		//		orchestration.runtimeAnalyse(data.getCurrentProject());
		//		EvidencesView box = new EvidencesView(window.getShell(),SWT.DIALOG_TRIM, true);
		//		box.open();
		return null;
	}
}