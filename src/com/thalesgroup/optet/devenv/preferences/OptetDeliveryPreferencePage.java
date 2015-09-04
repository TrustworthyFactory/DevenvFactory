package com.thalesgroup.optet.devenv.preferences;


import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.thalesgroup.optet.devenv.Activator;
import com.thalesgroup.optet.devenv.preferences.PasswordFieldEditor;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

/**
 * OptetSignaturePreferencePage the optet preference page in order to 
 * configure the sigature functionnality
 * 
 * @author F. Motte
 *
 */
public class OptetDeliveryPreferencePage 
extends FieldEditorPreferencePage
implements IWorkbenchPreferencePage{

	
	/**
	 * constructor
	 */
	public OptetDeliveryPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
	
	@Override
	public void init(IWorkbench arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		
		// field for the output of the signature
		new Label(getFieldEditorParent(), SWT.NONE).setLayoutData(getLayoutDataWithHorizontalSpan(3));
		Label lblcertificat = new Label(getFieldEditorParent(), SWT.NONE);
		lblcertificat.setLayoutData(getLayoutDataWithHorizontalSpan(3));
		lblcertificat.setText("Marketplace:");
		lblcertificat.setFont(new Font(getFieldEditorParent().getDisplay(), "Arial", 10, SWT.BOLD));	
		addField( new StringFieldEditor(PreferenceConstants.MARKETPLACE_URL, "Marketplace URL", getFieldEditorParent()));
		
		// field for the key configuration
		new Label(getFieldEditorParent(), SWT.NONE).setLayoutData(getLayoutDataWithHorizontalSpan(3));
		Label lblKeytool = new Label(getFieldEditorParent(), SWT.NONE);
		lblKeytool.setLayoutData(getLayoutDataWithHorizontalSpan(3));
		lblKeytool.setText("Keytool:");
		lblKeytool.setFont(new Font(getFieldEditorParent().getDisplay(), "Arial", 10, SWT.BOLD));		
		

		
		addField( new StringFieldEditor(PreferenceConstants.ALIAS_DELIVERY, "Alias", getFieldEditorParent()));
		addField( new PasswordFieldEditor(PreferenceConstants.KEY_PW_DELIVERY, "Key password", getFieldEditorParent()));
		addField( new PasswordFieldEditor(PreferenceConstants.KEYSTORE_PW_DELIVERY, "Keystore password", getFieldEditorParent()));
		addField( new FileFieldEditor(PreferenceConstants.KEYSTORE_PATH_DELIVERY, "Keystore path", getFieldEditorParent()));
		

	}
	/**
	 * Gets the layout data with horizontal span.
	 * 
	 * @return the layout data with horizontal span
	 */
	private GridData getLayoutDataWithHorizontalSpan(int horizontalSpan) {
	    GridData btnGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
	    btnGridData.horizontalSpan = horizontalSpan;
	    return btnGridData;
	}
}
