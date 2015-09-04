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
 * OptetPreferencePage the optet preference page in order to 
 * configure functionnality
 * 
 * @author F. Motte
 *
 */
public class OptetPreferencePage 
extends FieldEditorPreferencePage
implements IWorkbenchPreferencePage{

	
	/**
	 * constructor
	 */
	public OptetPreferencePage() {
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
		
		addField( new FileFieldEditor(PreferenceConstants.jarsigner_PATH_DELIVERY, "jarsigner path", getFieldEditorParent()));
		

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
