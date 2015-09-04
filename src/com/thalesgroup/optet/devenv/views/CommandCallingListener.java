package com.thalesgroup.optet.devenv.views;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

public class CommandCallingListener
  implements SelectionListener
{
  DashboardView view;
  String commandID;
  String featureName;

  public CommandCallingListener(DashboardView view, String commandID, String featureName)
  {
    this.view = view;
    this.commandID = commandID;
    this.featureName = featureName;
  }

  public void widgetDefaultSelected(SelectionEvent arg0)
  {
  }

  public void widgetSelected(SelectionEvent arg0)
  {
//    IHandlerService handlerService;
//    if (this.view.getPartOfLastSelection() != null) {
//      handlerService = (IHandlerService)this.view.getPartOfLastSelection().getSite().getService(IHandlerService.class);
//    }
//    else
//      handlerService = (IHandlerService)this.view.getSite().getService(IHandlerService.class);

      final IHandlerService handlerService = (IHandlerService) PlatformUI
    		  .getWorkbench().getService(IHandlerService.class); 
      
       
    try
    {

      handlerService.executeCommand(this.commandID, null);
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (NotDefinedException e) {
      MessageDialog.openInformation(this.view.getSite().getShell(), 
        "Command not defined.", 
        "This command was not available.");
      e.printStackTrace();
    } catch (NotEnabledException localNotEnabledException) {
      MessageDialog.openError(this.view.getSite().getShell(), 
        "Command not enabled.", 
        "The command is not enabled for the current selection. Please select a valid target.");
    } catch (NotHandledException e) {
      MessageDialog.openError(this.view.getSite().getShell(), 
        "Not a valid selection.", 
        "The command could not be performed on the current selection. Please select a valid target.");
      e.printStackTrace();
    }
  }
}