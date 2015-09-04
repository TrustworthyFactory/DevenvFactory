package com.thalesgroup.optet.devenv.jml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jmlspecs.openjml.Main;
import org.osgi.framework.Bundle;

import com.thalesgroup.optet.common.data.OptetDataModel;
import com.thalesgroup.optet.devenv.Activator;
import com.thalesgroup.optet.devenv.compilation.ShellBuilder;


public class ESCExec {

	void run(IJavaProject project, String file, Collection<String> metrics){


		try {

			ShellBuilder shell = new ShellBuilder();

			Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
			//			URL openJMLFileURL = bundle.getEntry("jmlExec/openjml.jar");
			//			URL cvc4FileUrl = bundle.getEntry("jmlExec/cvc4-1.4-win32-opt.exe");
			//			
			//			
			//			URL entry = bundle.getEntry("/");
			//			String path = org.eclipse.core.runtime.FileLocator.toFileURL(entry).getPath(); 
			//			
			//			System.out.println("path " + path);
			//			
			//			
			//			System.out.println("openjml path " + org.eclipse.core.runtime.FileLocator.toFileURL(bundle.getEntry("jmlExec/openjml.jar")).toURI().getRawPath());
			//			System.out.println("cvc4 path " + org.eclipse.core.runtime.FileLocator.toFileURL(bundle.getEntry("jmlExec/cvc4-1.4-win32-opt.exe")).getPath());
			//			//get object which represents the workspace  
			IWorkspace workspace = ResourcesPlugin.getWorkspace();  
			//
			//			//get location of workspace (java.io.File)  
			//File workspaceDirectory = workspace.getRoot().getLocation().toPortableString());
			//					
			//System.out.println("workspaceDirectory " + workspaceDirectory.getAbsolutePath());
						System.out.println("Project.getLocationURI().getRawPath() " + project.getProject().getLocationURI().getRawPath());
						System.out.println("Project.getFullPath() " + project.getProject().getFullPath().toFile().getAbsolutePath());
						System.out.println("Project.getRawLocation() " + project.getProject().getRawLocation());
			//
			System.out.println("file " + workspace.getRoot().getLocation().toPortableString()+file);
			//			
			//			System.out.println("fileURL " + openJMLFileURL.toURI().getRawPath());
			//			System.out.println("fileURL " + openJMLFileURL.getPath());
			//			System.out.println("fileURL " + openJMLFileURL.toExternalForm());





			String openJMLPath = org.eclipse.core.runtime.FileLocator.toFileURL(bundle.getEntry("jmlExec/openjml.jar")).getPath().replaceFirst("/", "");
			String cvc4Path = org.eclipse.core.runtime.FileLocator.toFileURL(bundle.getEntry("jmlExec/cvc4-1.4-win32-opt.exe")).getPath().replaceFirst("/", "");

			String option = "-jar "
					+ openJMLPath
					+ "  -esc  -prover  cvc4 -exec "
					+ cvc4Path
					+ " -noPurityCheck ";


			ArrayList sourceFolders = new ArrayList();
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IClasspathEntry[] entries = project.getResolvedClasspath(true);
			String classpath = null;
			for (int i = 0; i < entries.length; i++) {
				IClasspathEntry entry2 = entries[i];
				if (entry2.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					IPath path2 = entry2.getPath();
					IFolder sourceFolder = root.getFolder(path2);
					sourceFolders.add(sourceFolder);
					System.out.println("source folder  " + sourceFolder.getRawLocation().toPortableString());
					if (classpath!=null){
						classpath = classpath + ";"+sourceFolder.getRawLocation().toPortableString();
					}
					else{
						classpath = sourceFolder.getRawLocation().toPortableString();
					}
				}
			}	
			if (classpath!=null)
			{
				option = option + " -sourcepath "+ classpath;
			}				
			option = option + " " +  workspace.getRoot().getLocation().toPortableString()+file;

			System.out.println("option :  " + option);

			
			
			File outputFile = new File(project.getProject().getLocationURI().getRawPath()+"/Optet/"+ file.replace("/", "-") + ".jmlres");
			System.out.println(outputFile.exists());
			System.out.println(outputFile.getAbsolutePath());
			
			System.out.println("project path " + project.getProject().getLocationURI().getRawPath());
			//shell.build(project, "java", "-jar D:/works/security2011/optet/WP4/sandboxKepler/workspace5/com.thalesgroup.optet.devenv/jmlExec/openjml.jar -esc  -prover  cvc4 -exec D:/works/security2011/optet/WP4/sandboxKepler/openJML/cvc4-1.4-win32-opt.exe  -noPurityCheck -sourcepath D:/works/security2011/optet/WP4/sandboxKepler/openJML/TestJML/src D:/works/security2011/optet/WP4/sandboxKepler/openJML/TestJML/src/fr/amossys/optet/example/sensitivedata/SensitiveData.java", outputFile);
			//shell.build(project.getProject(), "java", "-jar D:/works/security2011/optet/WP4/sandboxKepler/workspace5/com.thalesgroup.optet.devenv/jmlExec/openjml.jar", outputFile);
			//shell.build(project.getProject(), "java", "-jar D:/works/security2011/optet/WP4/sandboxKepler/workspace5/com.thalesgroup.optet.devenv/jmlExec/openjml.jar  -esc  -prover  cvc4 -exec D:/works/security2011/optet/WP4/sandboxKepler/workspace5/com.thalesgroup.optet.devenv/jmlExec/cvc4-1.4-win32-opt.exe -noPurityCheck  -classpath D:/works/security2011/optet/WP4/sandboxKepler/runtime-New_configuration/test/src/main/java  D:/works/security2011/optet/WP4/sandboxKepler/runtime-New_configuration/test/src/main/java/fr/amossys/optet/example/sensitivedata/DataManager.java", outputFile);

			OptetDataModel.getInstance().addJMLProgress("analyse " + file);
			PlatformUI.getWorkbench().getActiveWorkbenchWindow
			().getActivePage().showView("com.thalesgroup.optet.analysis.openjml.JMLProgressView").setFocus();


			shell.build(project.getProject(), "java", option, outputFile);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
