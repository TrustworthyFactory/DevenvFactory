package com.thalesgroup.optet.devenv;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.thalesgroup.optet.devenv"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	// Controls debugging of the plugin 
	public static boolean DEBUG;

	public ILog log;


	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		System.out.println("start plugin " + PLUGIN_ID) ;
		super.start(context);
		plugin = this;
		Bundle bundle = Platform.getBundle(Platform.PI_RUNTIME);
		log = Platform.getLog(bundle);
		
		registerEventHandler(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * Log an exception.
	 *
	 * @param e
	 *            the exception
	 * @param message
	 *            message describing how/why the exception occurred
	 */
	public void logException(Throwable e, String message) {
		logMessage(IStatus.ERROR, message, e);
	}

	/**
	 * Log an error.
	 *
	 * @param message
	 *            error message
	 */
	public void logError(String message) {
		logMessage(IStatus.ERROR, message, null);
	}

	/**
	 * Log a warning.
	 *
	 * @param message
	 *            warning message
	 */
	public void logWarning(String message) {
		logMessage(IStatus.WARNING, message, null);
	}

	/**
	 * Log an informational message.
	 *
	 * @param message
	 *            the informational message
	 */
	public void logInfo(String message) {
		logMessage(IStatus.INFO, message, null);
	}

	/**
	 * Log message 
	 * 
	 * @param severity	the severity of the message
	 * @param message	the message
	 * @param e 		the exception
	 */
	private void logMessage(int severity, String message, Throwable e) {
		if (DEBUG) {
			String what = (severity == IStatus.ERROR) ? (e != null ? "Exception" : "Error") : "Warning";
			System.out.println(what + " in Optet plugin: " + message);
			if (e != null) {
				e.printStackTrace();
			}
		}
		IStatus status = createStatus(severity, message, e);
		//Activator.getDefault().getLog().log(status);
		log.log(status);
	}
	
	/**
	 * create the status of the message 
	 * 
	 * @param severity the severity of the message
	 * @param message the message
	 * @param e the exception
	 * @return the created status
	 */
	private IStatus createStatus(int severity, String message, Throwable e) {
		return new Status(severity, Activator.PLUGIN_ID, 0, message, e);
	}
	
	private void registerEventHandler(BundleContext ctx) {
		EventHandler handler = new OptetEventHandler();

		System.out.println("start dev comm");

		Dictionary<String,String> properties = new Hashtable<String, String>();
		properties.put(EventConstants.EVENT_TOPIC, "viewdisplay/*");
		ctx.registerService(EventHandler.class, handler, properties);
		System.out.println("start dev comm");
	}
}
