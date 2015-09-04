package com.thalesgroup.optet.devenv.systemdesc;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.thalesgroup.optet.devenv.datamodel.InternalSystemDM;

public class CreateElementAreaDialog extends TitleAreaDialog {

  private Text txtComponentName;
  private Combo ComponentTypeCombo;

  private String componentName;
  private String componentType;
  private Boolean ComponentTOEIsValue = false;

  public CreateElementAreaDialog(Shell parentShell) {
    super(parentShell);
  }

  @Override
  public void create() {
    super.create();
    setTitle("Create a new Component");
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayoutData(new GridData(GridData.FILL_BOTH));
    GridLayout layout = new GridLayout(2, false);
    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    container.setLayout(layout);

    createComponentName(container);
    createComponentType(container);
    createComponentInTOE(container);

    return area;
  }

  private void createComponentName(Composite container) {
    Label lbtComponentName = new Label(container, SWT.NONE);
    lbtComponentName.setText("Component Name");

    GridData dataComponentName = new GridData();
    dataComponentName.grabExcessHorizontalSpace = true;
    dataComponentName.horizontalAlignment = GridData.FILL;

    txtComponentName = new Text(container, SWT.BORDER);
    txtComponentName.setLayoutData(dataComponentName);
  }
  
  private void createComponentType(Composite container) {
	    Label lbtComponentType = new Label(container, SWT.NONE);
	    lbtComponentType.setText("Type");
	    
	    GridData dataComponentType = new GridData();
	    dataComponentType.grabExcessHorizontalSpace = true;
	    dataComponentType.horizontalAlignment = GridData.FILL;
	    
		ComponentTypeCombo = new Combo(container, SWT.NONE);
		InternalSystemDM.getInternalSystemDM();
		ComponentTypeCombo.setItems(InternalSystemDM.componentCategories());
		//ComponentTypeCombo.setItems(new String[] {"Root", "Host", "End-user", "Network", "BusinessProcess", "Consumer", "ApplicationServer", "DatabaseServer", "ClientSpecificResource", "ProviderSpecificResource"});
	  }

  private void createComponentInTOE(Composite container) {
	    Label lbtComponentInTOE = new Label(container, SWT.NONE);
	    lbtComponentInTOE.setText("Is an asset");
	    
	    GridData dataComponentInTOE = new GridData();
	    dataComponentInTOE.grabExcessHorizontalSpace = true;
	    dataComponentInTOE.horizontalAlignment = GridData.FILL;
	    
		final Button ComponentInTOE = new Button(container, SWT.CHECK);
		ComponentInTOE.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
		        if (ComponentInTOE.getSelection())
		        	ComponentTOEIsValue = true;
		        else
		        	ComponentTOEIsValue = false;
			}
		});
	  }



  @Override
  protected boolean isResizable() {
    return true;
  }

  // save content of the Text fields because they get disposed
  // as soon as the Dialog closes
  private void saveInput() {
    componentName = txtComponentName.getText();
    componentType = ComponentTypeCombo.getText();
    

  }

  @Override
  protected void okPressed() {
    saveInput();
    super.okPressed();
  }

  public String getComponentName() {
    return componentName;
  }

  public String getComponentType() {
    return componentType;
  }
  
  public Boolean getComponentInTOE() {
	  return ComponentTOEIsValue;
  }
} 