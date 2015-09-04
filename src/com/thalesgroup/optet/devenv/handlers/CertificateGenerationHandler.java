package com.thalesgroup.optet.devenv.handlers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.internal.Perspective;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.osgi.framework.Bundle;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.hp.hpl.jena.util.FileUtils;
import com.thalesgroup.dtwc.DTWC;
import com.thalesgroup.dtwc.impl.CertificateWrapper;
import com.thalesgroup.optet.common.exception.ProjectNotFoundException;
import com.thalesgroup.optet.devenv.REST.RestClient;
import com.thalesgroup.optet.devenv.compilation.CompilationView;
import com.thalesgroup.optet.devenv.compilation.SelectCompilationOutputView;
import com.thalesgroup.optet.devenv.crypto.CryptoPreferences;
import com.thalesgroup.optet.devenv.crypto.SignFile;
import com.thalesgroup.optet.devenv.datamodel.Attribute;
import com.thalesgroup.optet.devenv.datamodel.Component;
import com.thalesgroup.optet.devenv.datamodel.Control;
import com.thalesgroup.optet.devenv.datamodel.Element;
import com.thalesgroup.optet.devenv.datamodel.InternalSystemDM;
import com.thalesgroup.optet.devenv.datamodel.Metric;
import com.thalesgroup.optet.devenv.datamodel.Stakeholder;
import com.thalesgroup.optet.devenv.datamodel.TWAttribute;
import com.thalesgroup.optet.devenv.datamodel.TWProperty;
import com.thalesgroup.optet.devenv.datamodel.TWPropertySpecification;
import com.thalesgroup.optet.devenv.datamodel.Threat;
import com.thalesgroup.optet.devenv.jena.CertificatInterfaceImpl;
import com.thalesgroup.optet.devenv.preferences.PreferenceConstants;
import com.thalesgroup.optet.devenv.preferences.PreferenceInitializer;
import com.thalesgroup.optet.devenv.utils.TWProfileUtil;
import com.thalesgroup.optet.securerepository.SVNWrapper;
import com.thalesgroup.optet.securerepository.impl.SVNWrapperImpl;
import com.thalesgroup.optet.twmanagement.impl.SettingsModuleImpl;
import com.thalesgroup.optet.devenv.Activator;
/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class CertificateGenerationHandler extends AbstractHandler {

	private InternalSystemDM data;

	private CertificateWrapper cw;
	private IFile dtwcFile;
	private DTWC dtwc;



	// the crypto preference define into the preference apge
	private CryptoPreferences pref = null;
	private SignFile signature;

	/**
	 * The constructor.
	 */
	public CertificateGenerationHandler() {
		data = InternalSystemDM.getInternalSystemDM();
		this.pref = new CryptoPreferences();
		signature = new SignFile();
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event)
			throws ExecutionException
			{

		if (data.getCurrentProject() == null)
		{
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow
					().getShell(), "Project Selection", "Please, select a project before");
			return false;
		}

		//start the compilation
		CompilationView view = new CompilationView(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.DIALOG_TRIM);
		Boolean resultCompilation = (Boolean) view.open();

		String compilationFile="";
		//selected the output file
		if (resultCompilation){
			SelectCompilationOutputView selectView = new SelectCompilationOutputView(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.DIALOG_TRIM);
			compilationFile = (String) selectView.open();
			System.out.println("compilation result " + compilationFile);
		}

		dtwcFile = data.getCurrentProject().getFile("/Optet/dtwc.ttl");
		cw = new com.thalesgroup.dtwc.impl.CertificateWrapper();

		if (!dtwcFile.exists()){			
			Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
			URL fileURL = bundle.getEntry("resources/dtwc.ttl");
			InputStream in;
			try {
				in = new FileInputStream(FileLocator.toFileURL(fileURL).getPath());
				dtwcFile.create(in, false, null);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			data.getCurrentProject().getName();
			dtwc = cw.createCertificate(data.getCurrentProject().getName(), dtwcFile.getRawLocation().toString());
			try {
				cw.printCertificate(dtwcFile.getRawLocationURI().toString());
			} catch (OWLOntologyStorageException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			dtwc = cw.loadCertificate(data.getCurrentProject().getName(),dtwcFile.getRawLocation().toString());
		}

		InternalSystemDM data = InternalSystemDM.getInternalSystemDM();
		IProject project = data.getCurrentProject();

		Properties prop = new Properties();
		String projectID = null;

		try {
			prop.load(project.getFile("/Optet/Optet.properties").getContents());
			projectID = prop.getProperty("svn.project.selected");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//SVNWrapper svn = new SVNWrapperImpl();
		if (project != null){
//			SettingsModuleImpl.getInstance()
//			.setTWProfile(projectID,svn.getTWProfile(projectID));
			SettingsModuleImpl.getInstance()
			.setTWProfile(projectID,TWProfileUtil.getLocalTWProfile(project));
		}

		String certificatePath = pref.getCertificatePath();
		File f = new File(certificatePath);
		try {

			System.out.println(f.toURI());

			FileOutputStream output = new FileOutputStream(certificatePath);
			cw.printXMLCertificate(f.toURI().toString());
		} catch (OWLOntologyStorageException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			signature.signXMLData(certificatePath);
		} catch (TransformerFactoryConfigurationError
				| Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CertificatInterfaceImpl c = new CertificatInterfaceImpl(projectID);

		String currentDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String CertificateName = "certificate_"+ project.getName() +"_"+ currentDate +".xml" ;
		IFolder folder = project.getFolder(new Path("/Optet/" + currentDate));
		try {
			folder.create(IResource.NONE, true, null);
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}




		IFile file = project.getFile("/Optet/" +currentDate + "/" +  CertificateName);
		IFile fileCompile = copyToDir(compilationFile, project.getFile("/Optet/" +currentDate + "/").getRawLocation().toOSString());
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try{
			  //do what you want to do before sleeping
			  Thread.currentThread().sleep(2000);//sleep for 1000 ms
			  //do what you want to do after sleeptig
			}
			catch(InterruptedException ie){
			//If this thread was intrrupted by nother thread
			}
		InputStream in;
		try {
			in = new FileInputStream(certificatePath);
			file.delete(true, null);
			file.create(in, true, null);

			List<IFile> certificatToCommit = new ArrayList<>();
			if (fileCompile!=null && fileCompile.exists()){
				System.out.println("compilation file " + fileCompile.getRawLocation());
				certificatToCommit.add(fileCompile);
			}
			certificatToCommit.add(file);
			SVNWrapper svn = new SVNWrapperImpl();
			svn.setFilesToSVN(projectID, certificatToCommit);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RestClient client = new RestClient();
		try {
			String URL = Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.CERTIFICAT_URL, "", null);
			System.out.println("publish " + file.getRawLocation().makeAbsolute().toFile().getAbsolutePath() + " to " + URL);
			client.publishCertificate(URL, file.getRawLocation().makeAbsolute().toFile());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		int result = 0;
		if (resultCompilation){
			MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow
					().getShell(), "Certificate generation", null,
					"The certificate is generated to " + f.toURI(), MessageDialog.INFORMATION, new String[] { "OK" }, 0);
			result = dialog.open();
			System.out.println(result);
			IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow
					().getActivePage().findView("com.thalesgroup.optet.devenv.views.DashboardView");
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(part); 
		}

		return result;
			}

	/**
	 * @param osString
	 * @param osString2
	 */
	private IFile copyToDir(String osString, String osString2) {
		// TODO Auto-generated method stub
		InputStream in = null;
		OutputStream out = null;

		try{

			File afile =new File(osString);
			System.out.println("copy " + osString + " to " + osString2 +"/"+ afile.getName()) ;

			File destFile = new File(osString2 +"/"+ afile.getName());
			Path path = new Path(destFile.getAbsolutePath());

			if(afile.renameTo(destFile)){
				System.out.println("File is moved successful!");
				IPath location = Path.fromOSString(destFile.getAbsolutePath());
				return ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(location);
			}else{
				System.out.println("File is failed to move!");
				return null;
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
}
