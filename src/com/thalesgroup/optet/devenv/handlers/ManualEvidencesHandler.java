package com.thalesgroup.optet.devenv.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.thalesgroup.optet.common.data.OptetDataModel;
import com.thalesgroup.optet.devenv.evidencesview.EvidencesView;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ManualEvidencesHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public ManualEvidencesHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		OptetDataModel.getInstance().cleanAudit();
		OptetDataModel.getInstance().cleanMetric();
		EvidencesView box = new EvidencesView(window.getShell(),SWT.DIALOG_TRIM, false);
		box.open();
		return null;
	}
}