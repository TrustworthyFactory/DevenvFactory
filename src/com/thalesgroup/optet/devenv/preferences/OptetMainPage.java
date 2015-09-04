package com.thalesgroup.optet.devenv.preferences;


import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.thalesgroup.optet.devenv.handlers.systemHandler;

public class OptetMainPage 
extends PreferencePage
implements IWorkbenchPreferencePage {
	public OptetMainPage() {
	}

	@Override
	public void init(IWorkbench arg0) {
		// TODO Auto-generated method stub

	}


	protected Control createContents(Composite parent) {
		//Composite composite = new Composite(parent, SWT.BORDER);


		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		parent.setLayout(gridLayout);

		// Create a label with an image
		Image image = new Image(parent.getDisplay(),  getClass()
				.getResourceAsStream("/icons/logo-optet.png"));
		Label imageLabel = new Label(parent, SWT.CENTER);
		imageLabel.setImage(image);
		//imageLabel.setLayoutData(getLayoutDataWithHorizontalSpan(1));

		// Create a horizontal separator
		Label separator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR|SWT.CENTER);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Create a label
		new Label(parent, SWT.CENTER).setText("The OPTET project is co-funded by the European Commission within the Seventh Framework Program ");
		new Label(parent, SWT.CENTER).setText("(FP7-ICT-2011-8, Project No.  317631. Duration: November 2012 – October 2015. Web: http://www.optet.eu/");

		Button button = new Button(parent, SWT.LEFT);
		button.setText("Check consistency");
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub


				//				org.apache.commons.codec.digest.DigestUtils.sha(data)
				final Collection<File> all = new ArrayList<File>();





				boolean concistancy = true;
				Map<String, Boolean> jarMap = new HashMap<>();
				try {
					File eclipseHome = new File(new URL(System.getProperty("eclipse.home.location")+"plugins/").toURI());
					findFilesRecursively(eclipseHome , all, ".jar");
					JarVerifier signer = new JarVerifier();
					for (File file : all) {
						System.out.println(file.getName());
						boolean res = signer.verifyJar(file.getAbsolutePath());
						if (res == false){
							System.out.println("result false");
							concistancy = false;
						}
						jarMap.put(file.getAbsolutePath(), res);
					}
				} catch (MalformedURLException | URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				if (concistancy == true){
					// check consistancy
					MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow
							().getShell(), "Consistency", null,
							"The platform is consistent ", MessageDialog.INFORMATION, new String[] { "OK" }, 0);
					dialog.open();
				}
				else
				{
					MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow
							().getShell(), "Consistency", null,
							"The platform is not consistent ", MessageDialog.INFORMATION, new String[] { "OK" }, 0);
					dialog.open();

				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		return parent;
	}

	//Methode qui permet de parser un repertoire et de retrouver ses fichiers
	private static void findFilesRecursively(File file, Collection<File> all, final String extension) {
		//Liste des fichiers correspondant a l'extension souhaitee
		final File[] children = file.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return f.getName().endsWith(extension) ;
			}}
				);
		if (children != null) {
			System.out.println("children not null");
			//Pour chaque fichier recupere, on appelle a nouveau la methode
			for (File child : children) {
				all.add(child);
				findFilesRecursively(child, all, extension);
			}
		}else{
			System.out.println("children null");
		}

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
