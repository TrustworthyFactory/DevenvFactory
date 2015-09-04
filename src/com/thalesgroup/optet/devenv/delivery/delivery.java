/*
 *		OPTET Factory
 *
 *	Class delivery 1.0 15 mai 2014
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

package com.thalesgroup.optet.devenv.delivery;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

import com.thalesgroup.optet.devenv.Activator;
import com.thalesgroup.optet.devenv.REST.RestClient;
import com.thalesgroup.optet.devenv.preferences.PreferenceConstants;
import org.eclipse.swt.events.SelectionAdapter;

/**
 * @author F. Motte
 *
 */
public class delivery extends Dialog {

	protected Object result;
	protected Shell shlDelivery;
	private Text text;
	private Text text_1;
	private Text text_2;
	private IFile USDL;
	private IFile delivery;
	private Text text_3;
	private Text text_4;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public delivery(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlDelivery.open();
		shlDelivery.layout();
		Display display = getParent().getDisplay();
		while (!shlDelivery.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlDelivery = new Shell(getParent(), getStyle());
		shlDelivery.setSize(529, 357);
		shlDelivery.setText("Delivery to marketplace");
		
		Label lblMarketplaceUrl = new Label(shlDelivery, SWT.NONE);
		lblMarketplaceUrl.setBounds(10, 10, 141, 13);
		lblMarketplaceUrl.setText("Marketplace URL");
		
		text = new Text(shlDelivery, SWT.BORDER);
		text.setBounds(10, 29, 326, 19);
		text.setText(Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.MARKETPLACE_URL, "", null));
		
		Label lblUserCredentials = new Label(shlDelivery, SWT.NONE);
		lblUserCredentials.setBounds(10, 67, 118, 13);
		lblUserCredentials.setText("User name");
		
		text_1 = new Text(shlDelivery, SWT.BORDER);
		text_1.setBounds(10, 86, 326, 19);
		
		Label lblUserPassword = new Label(shlDelivery, SWT.NONE);
		lblUserPassword.setBounds(10, 111, 130, 13);
		lblUserPassword.setText("User password");
		
		text_2 = new Text(shlDelivery, SWT.BORDER);
		text_2.setBounds(10, 130, 326, 19);
		
		Button btnValidate = new Button(shlDelivery, SWT.NONE);
		btnValidate.setBounds(438, 292, 68, 23);
		btnValidate.setText("Validate");
		btnValidate.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				RestClient client = new RestClient();
				try {
					String URL = Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.MARKETPLACE_URL, "", null);
					client.publishProduct(text.getText(), USDL.getRawLocation().toFile(), delivery.getRawLocation().toFile());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				shlDelivery.close();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Button btnCancel = new Button(shlDelivery, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlDelivery.close();
			}
		});
		btnCancel.setBounds(364, 292, 68, 23);
		btnCancel.setText("Cancel");
		
		Label lblUsdlFile = new Label(shlDelivery, SWT.NONE);
		lblUsdlFile.setBounds(10, 183, 49, 13);
		lblUsdlFile.setText("USDL File");
		
		text_3 = new Text(shlDelivery, SWT.BORDER);
		text_3.setBounds(10, 207, 326, 19);
		
		Label lblProductFile = new Label(shlDelivery, SWT.NONE);
		lblProductFile.setBounds(10, 232, 79, 13);
		lblProductFile.setText("Product File");
		
		text_4 = new Text(shlDelivery, SWT.BORDER);
		text_4.setBounds(10, 251, 326, 19);
		
		Button btnBrowseUSDL = new Button(shlDelivery, SWT.NONE);
		btnBrowseUSDL.setBounds(342, 207, 68, 23);
		btnBrowseUSDL.setText("Browse");
		btnBrowseUSDL.addSelectionListener(new SelectionAdapter() {


			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shlDelivery, SWT.NULL);
				String path = dialog.open();
				if (path != null) {

					File file = new File(path);
					if (file.isFile()){
						
						displayFiles(new String[] { file.toString()});
						IWorkspace workspace= ResourcesPlugin.getWorkspace();    
						IPath location= Path.fromOSString(file.getAbsolutePath()); 
						USDL = workspace.getRoot().getFileForLocation(location);
					}
					else
						displayFiles(file.list());

				}
			}
		});  
		
		Button btnBrowseProduct = new Button(shlDelivery, SWT.NONE);
		btnBrowseProduct.setText("Browse");
		btnBrowseProduct.setBounds(342, 249, 68, 23);
		btnBrowseProduct.addSelectionListener(new SelectionAdapter() {


			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shlDelivery, SWT.NULL);
				String path = dialog.open();
				if (path != null) {

					File file = new File(path);
					if (file.isFile()){
						
						displayFilesDelivery(new String[] { file.toString()});
						IWorkspace workspace= ResourcesPlugin.getWorkspace();    
						IPath location= Path.fromOSString(file.getAbsolutePath()); 
						delivery = workspace.getRoot().getFileForLocation(location);
					}
					else
						displayFilesDelivery(file.list());

				}
			}
		});  
	}
	
	public void displayFiles(String[] files) {
		for (int i = 0; files != null && i < files.length; i++) {
			text_3.setText(files[i]);
			text_3.setEditable(true);
		}
	}
	public void displayFilesDelivery(String[] files) {
		for (int i = 0; files != null && i < files.length; i++) {
			text_4.setText(files[i]);
			text_4.setEditable(true);
		}
	}
}
