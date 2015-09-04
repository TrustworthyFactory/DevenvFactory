package com.thalesgroup.optet.devenv;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.thalesgroup.optet.devenv.evidencesview.EvidencesView;

public class OptetEventHandler implements EventHandler {

	public static Display getDisplay() {
		Display display = Display.getCurrent();
		//may be null if outside the UI thread
		if (display == null)
			display = Display.getDefault();
		return display;		
	}


	public void handleEvent(final Event event) {
		System.out.println("--> new event view= " + event.getTopic() + " " + event.getProperty("view"));
		System.out.println("--> new event phase= " + event.getTopic() + " " + event.getProperty("phase"));

		try {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView((String)(event.getProperty("view")));
						if (event.getProperty("phase")!=null){
						if (event.getProperty("phase").equals("certification"))
						{
							EvidencesView box = new EvidencesView(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.DIALOG_TRIM, true);
							box.open();
						}
						}
					} catch (PartInitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};
				}
			});


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}