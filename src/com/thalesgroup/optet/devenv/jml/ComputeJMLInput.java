package com.thalesgroup.optet.devenv.jml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.thalesgroup.optet.common.data.OptetDataModel;
import com.thalesgroup.optet.devenv.jml.jaxb.JMLFiles;
import com.thalesgroup.optet.devenv.jml.jaxb.JMLFiles.JMLFile;

public class ComputeJMLInput {
	private IFile jmlFile;
	private IJavaProject currentProject;
	private Map<String, Collection<String>> checkedElementMap;
	private Map<String, Boolean> metricsResult;
	private Shell shell;


	public ComputeJMLInput(IJavaProject currentProject, Shell shell) {
		super();
		this.currentProject = currentProject;
		this.shell = shell;
		checkedElementMap = new HashMap<>();
		metricsResult = new HashMap<String, Boolean>();
		jmlFile = currentProject.getProject().getFile("/Optet/JMLConf.xml");

	}

	public void run(){

		if (shell!=null){
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION |SWT.OK | SWT.CANCEL);
			messageBox.setMessage("Start JML computation, please wait ...");
			int rc = messageBox.open();


			switch (rc) {
			case SWT.OK:
				System.out.println("SWT.OK");
				break;
			case SWT.CANCEL:
				System.out.println("SWT.CANCEL");
				break;
			case SWT.YES:
				System.out.println("SWT.YES");
				break;
			case SWT.NO:
				System.out.println("SWT.NO");
				break;
			case SWT.RETRY:
				System.out.println("SWT.RETRY");
				break;
			case SWT.ABORT:
				System.out.println("SWT.ABORT");
				break;
			case SWT.IGNORE:
				System.out.println("SWT.IGNORE");
				break;
			}
		}
		JAXBContext jaxbContext;
		try {



			jaxbContext = JAXBContext.newInstance(JMLFiles.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			JMLFiles files = (JMLFiles) jaxbUnmarshaller.unmarshal(jmlFile.getRawLocation().toFile());	
			java.util.List<JMLFile> filesList = files.getJMLFile();

			ESCExec e = new ESCExec();

			for (Iterator iterator = filesList.iterator(); iterator
					.hasNext();) {
				JMLFile jmlFile = (JMLFile) iterator.next();
				e.run(currentProject,jmlFile.getName(), jmlFile.getJMLmetric());
			}

			compute();

			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow
				().getActivePage().showView("com.thalesgroup.optet.devenv.views.OptetMetricView");
			} catch (PartInitException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private void compute(){


		jmlFile = currentProject.getProject().getFile("/Optet/JMLConf.xml");
		if (jmlFile.exists()){	

			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(JMLFiles.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				JMLFiles files = (JMLFiles) jaxbUnmarshaller.unmarshal(jmlFile.getRawLocation().toFile());	
				java.util.List<JMLFile> filesList = files.getJMLFile();

				for (Iterator iterator = filesList.iterator(); iterator
						.hasNext();) {
					JMLFile jmlFile = (JMLFile) iterator.next();
					checkedElementMap.put(jmlFile.getName(), jmlFile.getJMLmetric());

					//convert file to filename
					File outputFile = new File(currentProject.getProject().getLocationURI().getRawPath()+"/Optet/"+ jmlFile.getName().replace("/", "-") + ".jmlres");
					// find file if present
					// at the end, the metric OK are Ok, the other (false or not found) are false
					if (outputFile.exists()){

						//check the content
						BufferedReader in;
						try {
							in = new BufferedReader(new FileReader(outputFile));
							String line;
							while ((line = in.readLine()) != null)
							{
								if (!line.contains("warning") && !line.contains("error")){
									List<String> metrics = jmlFile.getJMLmetric();
									for (Iterator iterator2 = metrics.iterator(); iterator2
											.hasNext();) {
										String string = (String) iterator2.next();
										if (metricsResult.containsKey(string))
										{
											//nothing
										}else{
											metricsResult.put(string, true);
										}
									}
								}
								else{
									List<String> metrics = jmlFile.getJMLmetric();
									for (Iterator iterator2 = metrics.iterator(); iterator2
											.hasNext();) {
										String string = (String) iterator2.next();
										metricsResult.put(string, false);
									}  
								}
							}
							in.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// if the content is ok, set the metric associate to true exception if the metric was false previously
					}else{
						List<String> metrics = jmlFile.getJMLmetric();
						for (Iterator iterator2 = metrics.iterator(); iterator2
								.hasNext();) {
							String string = (String) iterator2.next();
							metricsResult.put(string, false);
						}
					}
					// add to datamodel


					for (Map.Entry<String, Boolean> entry : metricsResult.entrySet()) {
						System.out.println("Key : " + entry.getKey() + " Value : "
								+ entry.getValue());
						if (entry.getValue()){
							OptetDataModel.getInstance().addMetrics(entry.getKey(), "1","Evidence","");
						}else{
							OptetDataModel.getInstance().addMetrics(entry.getKey(), "0","Evidence","");							
						}
					}




				}
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}



	}
}