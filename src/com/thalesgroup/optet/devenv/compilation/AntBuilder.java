package com.thalesgroup.optet.devenv.compilation;


/*
 *		OPTET Factory
 *
 *	Class AntBuilder 1.0 1 sept. 2014
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */
import java.io.File;
import org.eclipse.core.runtime.Status;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
/**
 * @author F. Motte
 *
 */
public class AntBuilder {
	public void build(IProject project,File launcher, String option){
		run(project);
		//runBuild(launcher.getRawLocation().makeAbsolute().toOSString(), option);
	}

	
	private void run(IProject project){
		final File buildFile = new File(project.getLocation().toOSString() + "/build.xml");
		

		if (buildFile.exists()) {
			Shell workbenchShell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
			Job b2Job = new Job("Deploy Building Block") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask("Deploy Building block", 3);
					AntRunner runner = new AntRunner();
					runner.setBuildFileLocation(buildFile.getAbsolutePath());
					runner.setExecutionTargets(new String[] {"dist"});
					runner.setArguments("-Dmessage=Building -verbose");
					//runner.addBuildListener("org.eclipse.ant.internal.core.ant.ProgressBuildListener");
					monitor.subTask("Uploading B2");

					try {
						runner.run(monitor);
						while(runner.isBuildRunning()) {
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								runner.stop();
								return Status.CANCEL_STATUS;
							}
						}
					} catch (CoreException e) {
						e.printStackTrace();
						monitor.setCanceled(true);
					}
					System.out.println("end of ant build");
					return Status.OK_STATUS;
				}
			};
			b2Job.setUser(true);
			b2Job.schedule();
			
			
		}
	}

	private void runBuild(String file, String option) {
		
		System.out.println("run " + file + " with option " + option);
		IProgressMonitor monitor = null;
		AntRunner runner = new AntRunner();
		runner.setBuildFileLocation(file);
		runner.setExecutionTargets(new String[] { option });
		runner.setArguments("-Dmessage=Building -verbose");
		try {
			runner.run(monitor);
			runner.getAvailableTargets();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("end build ant");
	}
}
