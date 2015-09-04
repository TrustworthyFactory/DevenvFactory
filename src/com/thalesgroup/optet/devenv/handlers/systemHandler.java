package com.thalesgroup.optet.devenv.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import com.thalesgroup.optet.devenv.systemdesc.SystemDialogBox;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class systemHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public systemHandler() {
		System.out.println("create systemHandler");
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		SystemDialogBox box = new SystemDialogBox(window.getShell(),SWT.DIALOG_TRIM);
		box.open();
		return null;
	}
}