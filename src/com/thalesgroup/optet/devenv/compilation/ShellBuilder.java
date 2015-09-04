package com.thalesgroup.optet.devenv.compilation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 *		OPTET Factory
 *
 *	Class ShellBuilder 1.0 1 sept. 2014
 *
 *	Copyright (c) 2013 Thales Communications & Security SAS
 *	4, Avenue des Louvresses - 92230 Gennevilliers 
 *	All rights reserved
 *
 */

/**
 * @author F. Motte
 *
 */
public class ShellBuilder {



	class AfficheurFlux implements Runnable {

		private final InputStream inputStream;
		private String type;

		AfficheurFlux(InputStream inputStream, String type) {
			this.inputStream = inputStream;
			this.type = type;
		}

		private BufferedReader getBufferedReader(InputStream is) {
			return new BufferedReader(new InputStreamReader(is));
		}

		@Override
		public void run() {
			BufferedReader br = getBufferedReader(inputStream);
			String ligne = "";
			try {
				while ((ligne = br.readLine()) != null) {
					System.out.println("ligne " + type+ " " + ligne);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void build (IProject project, File launcher, String option){

		System.out.println(executeCommand(project, launcher.getAbsolutePath(), option, null, null)); 
	}

	public void build (IProject project, String launcher, String option, File output){

		System.out.println(executeCommand(project, launcher, option, output,null)); 
	}

	public void build (IProject project, String launcher, String option, File output, Map<String, String> env){

		System.out.println(executeCommand(project, launcher, option, output,env)); 
	}


	private String executeCommand(IProject project, String file, String command, File output, Map<String, String> env) {

		try {
			//System.out.println(project.getRawLocation().toOSString());
			//System.out.println("path  " + new File(project.getRawLocation().toOSString()));

			List<String> args = new ArrayList<String>();
			args.add (file); // command name
			String[] commandArray = command.split(" ");
			for (int i = 0; i < commandArray.length; i++) {
				System.out.println("add " + commandArray[i]);
				args.add (commandArray[i]); // optional args added as separate list items
			}


			ProcessBuilder pb = new ProcessBuilder(args);
			
			
			if (env!=null){
				for (Map.Entry<String, String> entry : env.entrySet())
				{
				    System.out.println(entry.getKey() + "/" + entry.getValue());
				    pb.environment().put(entry.getKey(), entry.getValue());
				}
			}
			
			
			System.out.println(project.getLocationURI());


			//pb.directory(new File(project.getRawLocation().toOSString()));
			pb.directory(new File(project.getLocationURI()));
			pb.redirectOutput(output);

			Process p = pb.start();
			AfficheurFlux fluxSortie = new AfficheurFlux(p.getInputStream(), "output");
			AfficheurFlux fluxErreur = new AfficheurFlux(p.getErrorStream(), "error");
			new Thread(fluxSortie).start();
			new Thread(fluxErreur).start();
			p.waitFor();
			System.out.println("it's the end");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}
}
