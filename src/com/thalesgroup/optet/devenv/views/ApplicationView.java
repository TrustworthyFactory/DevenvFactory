package com.thalesgroup.optet.devenv.views;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;


public class ApplicationView extends Composite
{
  private Button btnInitiateSD;
  private Button btnInitiateTP;
  private Button btnDefineTP;
  private Button btnManual;
  private Button btnAutomatic;
  private Button btnGenerate;
  DashboardView containingPart;

  public ApplicationView(Composite parent, int style)
  {
    super(parent, style);
    setLayout(new GridLayout(9, false));
    
    Font boldFont = new Font(parent.getDisplay(),"Arial",8, SWT.BOLD | SWT.ITALIC);
    Color backgroudColor = new Color(getDisplay(), new RGB(255, 100, 30));
	// Create a label with an image
	Image nextImage = new Image(parent.getDisplay(),  getClass()
			.getResourceAsStream("/icons/next_icon.png"));
	
	
	
    Group grpSoftDesc = new Group(this, 0);
    grpSoftDesc.setFont(boldFont);
    grpSoftDesc.setBackground(backgroudColor);
    grpSoftDesc.setLayout(new FillLayout(512));
    grpSoftDesc.setLayoutData(new GridData(16384, 16777216, false, false, 1, 2));
    grpSoftDesc.setText("Software Description");

    this.btnInitiateSD = new Button(grpSoftDesc, 0);
    this.btnInitiateSD.setText("Initiate");


    Composite composite = new Composite(this, 0);
    RowLayout rl_composite = new RowLayout(512);
    rl_composite.center = true;
    composite.setLayout(rl_composite);
    composite.setLayoutData(new GridData(16384, 16777216, false, false, 1, 2));

    Label lblAbc = new Label(composite, 0);
    lblAbc.setImage(nextImage);



    Group grpTProb = new Group(this, 0);
    grpTProb.setFont(boldFont);
    grpTProb.setBackground(backgroudColor);
    grpTProb.setLayout(new FillLayout(512));
    GridData gd_grpTProb = new GridData(16384, 16777216, false, false, 1, 2);
    gd_grpTProb.widthHint = 92;
    grpTProb.setLayoutData(gd_grpTProb);
    grpTProb.setText("Trustworthiness Problem definition");

    this.btnInitiateTP = new Button(grpTProb, 0);
    this.btnInitiateTP.setText("Initiate");

    Composite composite_1 = new Composite(this, 0);
    RowLayout rl_composite_1 = new RowLayout(512);
    rl_composite_1.center = true;
    composite_1.setLayout(rl_composite_1);
    composite_1.setLayoutData(new GridData(16384, 16777216, false, false, 1, 2));

    Label label = new Label(composite_1, 0);
    label.setImage(nextImage);



    Group grpTprop = new Group(this, 0);
    grpTprop.setFont(boldFont);
    grpTprop.setLayoutData(new GridData(16384, 16777216, false, false, 1, 2));
    grpTprop.setBackground(backgroudColor);
    grpTprop.setText("Trustworthiness Property Specification");
    grpTprop.setLayout(new FillLayout(512));

    this.btnDefineTP = new Button(grpTprop, 0);
    this.btnDefineTP.setEnabled(true);
    this.btnDefineTP.setText("Define");


    Composite composite_2 = new Composite(this, 0);
    RowLayout rl_composite_2 = new RowLayout(512);
    rl_composite_2.center = true;
    composite_2.setLayout(rl_composite_2);
    composite_2.setLayoutData(new GridData(16384, 16777216, false, false, 1, 2));

    Label label_1 = new Label(composite_2, 0);
    label_1.setImage(nextImage);

    Group grpEvidences = new Group(this, 0);
    grpEvidences.setFont(boldFont);
    grpEvidences.setBackground(backgroudColor);
    grpEvidences.setLayout(new FillLayout(512));
    GridData gd_grpEvidences = new GridData(16384, 16777216, false, false, 1, 2);
    gd_grpEvidences.widthHint = 154;
    grpEvidences.setLayoutData(gd_grpEvidences);
    grpEvidences.setText("Evidences");

    this.btnManual = new Button(grpEvidences, 0);
    this.btnManual.setText("Manual");

    this.btnAutomatic = new Button(grpEvidences, 0);
    this.btnAutomatic.setText("Automatic");

    Composite composite_3 = new Composite(this, 0);
    RowLayout rl_composite_3 = new RowLayout(512);
    rl_composite_3.center = true;
    composite_3.setLayout(rl_composite_3);
    composite_3.setLayoutData(new GridData(16384, 16777216, false, false, 1, 2));

    Label label_2 = new Label(composite_3, 0);
    label_2.setImage(nextImage);

    Group grpDTWC = new Group(this, 0);
    grpDTWC.setFont(boldFont);
    grpDTWC.setBackground(backgroudColor);
    GridData gd_grpDTWC = new GridData(16384, 16777216, false, false, 1, 2);
    gd_grpDTWC.widthHint = 152;
    grpDTWC.setLayoutData(gd_grpDTWC);
    grpDTWC.setText("DTWC");
    grpDTWC.setLayout(new FillLayout(512));

    this.btnGenerate = new Button(grpDTWC, 0);
    this.btnGenerate.setText("Create and Edit");
  }

  protected void checkSubclass()
  {
  }

  private void addCommandCallingListener(Button btn, String commandID, String featureName)
  {
    btn.addSelectionListener(new CommandCallingListener(this.containingPart, commandID, featureName));
  }

  protected void createActions(DashboardView containingPart)
  {
    this.containingPart = containingPart;

    System.out.println("********************* Create Actions");
    
    addCommandCallingListener(this.btnInitiateSD, "com.thalesgroup.optet.devenv.commands.systemCommand", "");
    addCommandCallingListener(this.btnInitiateTP, "com.thalesgroup.optet.devenv.commands.problemDefinitionCommand", "");
    addCommandCallingListener(this.btnDefineTP, "com.thalesgroup.optet.devenv.commands.propertySpecificationCommand", "");
    addCommandCallingListener(this.btnGenerate, "com.thalesgroup.optet.devenv.commands.certificateGenerationCommand", "");
    addCommandCallingListener(this.btnAutomatic, "com.thalesgroup.optet.devenv.commands.automaticEvidencesCommand", "");
    addCommandCallingListener(this.btnManual, "com.thalesgroup.optet.devenv.commands.manualEvidencesCommand", "");
  }
}