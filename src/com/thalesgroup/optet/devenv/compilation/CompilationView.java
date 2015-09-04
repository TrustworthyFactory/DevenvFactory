/*
 *		OPTET Factory
 *
 *	Class CompilationView 1.0 1 sept. 2014
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.compilation;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.internal.Workbench;

import com.thalesgroup.optet.devenv.datamodel.InternalSystemDM;

/**
 * @author F. Motte
 *
 */
public class CompilationView extends Dialog {

	protected boolean result;
	protected Shell shlCompilation;
	private Text text;
	private Text text_1;
	private InternalSystemDM data;

	private Button btnMaven; 
	private Button btnAnt;
	private Button btnShell;

	private File buildFile = null;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public CompilationView(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
		data = InternalSystemDM.getInternalSystemDM();
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlCompilation.open();
		shlCompilation.layout();
		Display display = getParent().getDisplay();
		while (!shlCompilation.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	public void displayFiles(String[] files) {
		for (int i = 0; files != null && i < files.length; i++) {
			text.setText(files[i]);
			text.setEditable(true);
		}
	}
	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlCompilation = new Shell(getParent(), getStyle());
		shlCompilation.setSize(450, 300);
		shlCompilation.setText("Compilation");

		Composite composite = new Composite(shlCompilation, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		composite.setBounds(10, 10, 424, 248);

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setBounds(346, 215, 68, 23);
		btnNewButton.setText("start");
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				System.out.println("start");
				result = true;

				if (btnMaven.getSelection()){
					System.out.println("Build maven with file "+ buildFile + " with option " +text_1.getText() );
					MavenBuilder mvn = new MavenBuilder();
					mvn.build(data.getCurrentProject(), buildFile, text_1.getText());
				}else if (btnAnt.getSelection()){
					System.out.println("Build ant with file "+ buildFile + " with option " +text_1.getText() );
					AntBuilder ant = new AntBuilder();
					ant.build(data.getCurrentProject(), buildFile, text_1.getText());
				}else if (btnShell.getSelection()){
					System.out.println(" with option " +text_1.getText());

					System.out.println("Build shell with file "+ buildFile );
					System.out.println("Build shell with file "+ buildFile + " with option " +text_1.getText() );
					ShellBuilder shell = new ShellBuilder();
					shell.build(data.getCurrentProject(), buildFile, text_1.getText());
				}
				shlCompilation.close();
			}
		});

		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton_1.setBounds(261, 215, 68, 23);
		btnNewButton_1.setText("cancel");
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				result = false;
				shlCompilation.close();
			}
		});

		btnMaven = new Button(composite, SWT.RADIO);
		btnMaven.setBounds(10, 10, 83, 16);
		btnMaven.setText("Maven");

		btnAnt = new Button(composite, SWT.RADIO);
		btnAnt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnAnt.setText("Ant");
		btnAnt.setBounds(179, 10, 83, 16);

		btnShell = new Button(composite, SWT.RADIO);
		btnShell.setText("Shell");
		btnShell.setBounds(331, 10, 83, 16);

		text = new Text(composite, SWT.BORDER);
		text.setBounds(99, 62, 230, 19);

		Label lblCompilationFile = new Label(composite, SWT.NONE);
		lblCompilationFile.setBounds(10, 65, 83, 13);
		lblCompilationFile.setText("Compilation file");

		Button btnBrowse = new Button(composite, SWT.NONE);
		btnBrowse.setBounds(346, 58, 68, 23);
		btnBrowse.setText("Browse");
		btnBrowse.addSelectionListener(new SelectionAdapter() {


			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shlCompilation, SWT.NULL);
				String path = dialog.open();
				if (path != null) {

					File file = new File(path);
					if (file.isFile()){
						
						displayFiles(new String[] { file.toString()});
						IWorkspace workspace= ResourcesPlugin.getWorkspace();    
						IPath location= Path.fromOSString(file.getAbsolutePath()); 
						//buildFile = workspace.getRoot().getFileForLocation(location);
						buildFile = file;
					}
					else
						displayFiles(file.list());

				}
			}
		});  

		Label lblTargetoption = new Label(composite, SWT.NONE);
		lblTargetoption.setBounds(10, 106, 83, 13);
		lblTargetoption.setText("Target/Option");

		text_1 = new Text(composite, SWT.BORDER);
		text_1.setBounds(99, 100, 230, 19);


	}

	public  IProject getCurrentProject(){    
		ISelectionService selectionService =     
				Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();    

		ISelection selection = selectionService.getSelection();    

		IProject project = null;    
		if(selection instanceof IStructuredSelection) {    
			Object element = ((IStructuredSelection)selection).getFirstElement();    

			if (element instanceof IResource) {    
				project= ((IResource)element).getProject();    
			} else if (element instanceof PackageFragmentRootContainer) {    
				IJavaProject jProject =     
						((PackageFragmentRootContainer)element).getJavaProject();    
				project = jProject.getProject();    
			} else if (element instanceof IJavaElement) {    
				IJavaProject jProject= ((IJavaElement)element).getJavaProject();    
				project = jProject.getProject();    
			}    
		}     
		return project;    
	}


}
