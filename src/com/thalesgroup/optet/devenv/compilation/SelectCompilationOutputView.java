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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.thalesgroup.dtwc.DTWC;
import com.thalesgroup.dtwc.impl.CertificateWrapper;
import com.thalesgroup.optet.devenv.datamodel.InternalSystemDM;

/**
 * @author F. Motte
 *
 */
public class SelectCompilationOutputView extends Dialog {

	protected String result;
	protected Shell shlCompilation;
	private Text text;
	private InternalSystemDM data;

	private IFile selectedFile = null;
	private CertificateWrapper cw;
	private IFile dtwcFile;
	private DTWC dtwc;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SelectCompilationOutputView(Shell parent, int style) {
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
		shlCompilation.setText("Compilation Output selection");

		Composite composite = new Composite(shlCompilation, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		composite.setBounds(10, 10, 424, 248);

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setBounds(346, 215, 68, 23);
		btnNewButton.setText("select");
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				System.out.println("select");
				dtwcFile = data.getCurrentProject().getFile("/Optet/dtwc.ttl");
				cw = new com.thalesgroup.dtwc.impl.CertificateWrapper();

				if (dtwcFile.exists()){
					dtwc = cw.loadCertificate(data.getCurrentProject().getName(),dtwcFile.getRawLocation().toString());
					MessageDigest md;
					try {
						md = MessageDigest.getInstance("SHA");
						java.nio.file.Path path = Paths.get(selectedFile.getRawLocationURI());
						result = path.toString();
						byte[] res = md.digest(Files.readAllBytes(path));
						StringBuffer hexString = new StringBuffer();
						for (int i=0; i<res.length; i++){
							hexString.append(Integer.toHexString(0xFF & res[i]));
						}
						
						dtwc.setHash(new String(hexString));
			
							cw.printCertificate(dtwcFile.getRawLocationURI().toString());
	
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (OWLOntologyStorageException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

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
				result = "";
				shlCompilation.close();
			}
		});

		text = new Text(composite, SWT.BORDER);
		text.setBounds(99, 62, 230, 19);

		Label lblCompilationFile = new Label(composite, SWT.NONE);
		lblCompilationFile.setBounds(10, 65, 83, 13);
		lblCompilationFile.setText("Compilation output");

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
						selectedFile = workspace.getRoot().getFileForLocation(location);
					}
					else
						displayFiles(file.list());

				}
			}
		});


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
