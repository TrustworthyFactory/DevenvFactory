package com.thalesgroup.optet.devenv.views;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;


public class DashboardView extends ViewPart
{
  public static final String ID = "com.thalesgroup.optet.devenv.views.DashboardView";
  private IProject project;
  private ISelectionListener selectionListener;
  private TabFolder tabFolder;
  private Label lblProject;
  private IWorkbenchPart partOfLastSelection;

  public void createPartControl(Composite parent)
  {
    parent.setLayout(new GridLayout(1, false));

    this.lblProject = new Label(parent, 0);
    this.lblProject.setLayoutData(new GridData(4, 16777216, true, false, 1, 1));
    this.lblProject.setText("Project");

    ScrolledComposite scrolledComposite = new ScrolledComposite(parent, 2816);
    scrolledComposite.setLayoutData(new GridData(4, 4, true, true, 1, 1));
    scrolledComposite.setExpandHorizontal(true);
    scrolledComposite.setExpandVertical(true);

    this.tabFolder = new TabFolder(scrolledComposite, 0);

    TabItem tbtmCreatingAnAal = new TabItem(this.tabFolder, 0);
    tbtmCreatingAnAal.setText("Certify the product");

    ApplicationView applicationView = new ApplicationView(this.tabFolder, 0);
    applicationView.createActions(this);
    tbtmCreatingAnAal.setControl(applicationView);

//    TabItem tbtmOntologyProject = new TabItem(this.tabFolder, 0);
//    tbtmOntologyProject.setText("Creating an Ontology");
//
//    OntologyView ontologyView = new OntologyView(this.tabFolder, 0);
//    ontologyView.createActions(this);
//    tbtmOntologyProject.setControl(ontologyView);

    scrolledComposite.setContent(this.tabFolder);
    scrolledComposite.setMinSize(this.tabFolder.computeSize(-1, -1));

    initializeToolBar();
    initializeMenu();

  }

  private void initializeToolBar()
  {
    getViewSite().getActionBars().getToolBarManager
      ();
  }

  private void initializeMenu()
  {
    getViewSite().getActionBars().getMenuManager
      ();
  }

  public void setFocus()
  {
  }

  public void dispose()
  {
	  
	  System.out.println("dipose the view");
 
    if (this.selectionListener != null)
      getSite().getPage().removePostSelectionListener(this.selectionListener);
    super.dispose();

  }

//  private void hookPageSelection()
//  {
//    this.selectionListener = new ProjectElementSelectionListener(this);
//    getSite().getPage().addPostSelectionListener(this.selectionListener);
//  }

  public void setSelectedElementWithContext(Object selectedElement, String selectedElementString, IProject selectedProject, IWorkbenchPart partOfLastSelection)
  {
    setLastSelectionPart(partOfLastSelection);
    setSelectedProject(selectedProject);
    setSelectedElement(selectedElement);
    setSelectedElementName(selectedElementString);
  }

  private void setLastSelectionPart(IWorkbenchPart partOfLastSelection)
  {
    this.partOfLastSelection = partOfLastSelection;
  }

  private void setSelectedElement(Object element)
  {
    setSelectedElementName(element.toString());
  }

  private void setSelectedElementName(String name) {
    if (name != null)
      this.lblProject.setText(name);
    else
      this.lblProject.setText("No element selected");
  }

  private void setSelectedProject(IProject input) {
    this.project = input;
  }

  public IProject getSelectedProject() {
    return this.project;
  }

  public IWorkbenchPart getPartOfLastSelection() {
    return this.partOfLastSelection;
  }
}