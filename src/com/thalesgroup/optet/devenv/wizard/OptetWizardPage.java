package com.thalesgroup.optet.devenv.wizard;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Set;
import javax.xml.bind.JAXBException;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.SWT;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.events.*;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.jface.viewers.*;

import com.thalesgroup.optet.common.exception.ProjectNotFoundException;
import com.thalesgroup.optet.common.jaxb.TWProfile.TWProfile;
import com.thalesgroup.optet.securerepository.SVNWrapper;
import com.thalesgroup.optet.securerepository.impl.SVNWrapperImpl;
import com.thalesgroup.optet.securerepository.impl.SvnEntry;
import com.thalesgroup.optet.securerepository.views.SvnEntryListLabelProvider;
import com.thalesgroup.optet.securerepository.views.SvnEntryTreeContentProvider;
import com.thalesgroup.optet.twmanagement.SettingsModule;
import com.thalesgroup.optet.twmanagement.impl.SettingsModuleImpl;

/**
 * The "New" wizard page allows setting the container for
 * the new file as well as the file name. The page
 * will only accept file name without the extension OR
 * with the extension that matches the expected one .
 */

public class OptetWizardPage extends WizardPage {

	protected Object result;
	private Text text;


	private SVNWrapper svn;
	private SvnEntry selectedSvnentry = null;
	private TreeViewer treeViewer;
	private Button btnCheckoutTheSource;
	/**
	 * Constructor for SampleNewWizardPage.
	 * @param pageName
	 */
	public OptetWizardPage() {
		super("wizardPage");
		setTitle("Optet certification Wizard");
		setDescription("This wizard selects the svn repository to associate");
		svn = new SVNWrapperImpl();
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
//		GridLayout layout = new GridLayout();
//		container.setLayout(layout);
//		layout.numColumns = 2;
//		layout.verticalSpacing = 9;		
		
		ListViewer listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		list.setBounds(10, 10, 113, 280);
		Set<SvnEntry> projectList = svn.getProjectFromSVN();
		System.out.println("set size " + projectList.size());
		//		for (Iterator<String> iterator = projectList.iterator(); iterator.hasNext();) {
		//			String string = (String) iterator.next();
		//			list.add(string);
		//		}
		//		


		listViewer.setContentProvider(new IStructuredContentProvider(){

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
				// TODO Auto-generated method stub

			}

			@SuppressWarnings("unchecked")
			@Override
			public Object[] getElements(Object inputElement) {
				// TODO Auto-generated method stub
				return ((Set<String>)inputElement).toArray();
			}});

		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event)
			{
				try {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					System.out.println("Selected: " + selection.getFirstElement());
					System.out.println(selection.size());

					System.out.println(svn.getProjectDescription((SvnEntry)selection.getFirstElement()));
					text.setMessage(svn.getProjectDescription((SvnEntry)selection.getFirstElement()).toString());
					selectedSvnentry = (SvnEntry)selection.getFirstElement();
					svn.getTWProfile(selectedSvnentry);

					System.out.println("-----------------------------");
					treeViewer.setInput(svn.getFilesFromSVN(selectedSvnentry));
					dialogChanged();
				} catch (ProjectNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		listViewer.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
				// TODO Auto-generated method stub
				return ((SvnEntry)element).getAuthor();
			}

		});


		listViewer.setInput(projectList);
		
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(129, 10, 330, 137);
		text.setEditable(false);
		text.setEnabled(false);



		treeViewer = new TreeViewer(container, SWT.BORDER);

		treeViewer.setLabelProvider(
				new SvnEntryListLabelProvider());
		treeViewer.setContentProvider(
				new SvnEntryTreeContentProvider());
		treeViewer.expandAll();

		Tree tree = treeViewer.getTree();
		tree.setBounds(129, 158, 330, 132);
		
		btnCheckoutTheSource = new Button(container, SWT.CHECK);
		btnCheckoutTheSource.setBounds(171, 317, 145, 16);
		btnCheckoutTheSource.setText("Checkout the source");
		btnCheckoutTheSource.setVisible(false);
		
		dialogChanged();
		setControl(container);
	}
	
	
	private void dialogChanged() {
		SvnEntry svnEntry = getSelectedSVNEntry();

		if (svnEntry == null) {
			updateStatus("Select a SVN project");
			return;
		}

		updateStatus(null);
	}
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public SvnEntry getSelectedSVNEntry(){
		return selectedSvnentry;
	}
	
	public Boolean mustCheckoutSource(){
		//return btnCheckoutTheSource.getEnabled();
		return true;
	}
	
}