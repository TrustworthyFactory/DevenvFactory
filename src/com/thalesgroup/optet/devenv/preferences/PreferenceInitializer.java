package com.thalesgroup.optet.devenv.preferences;



import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.osgi.framework.Bundle;

import com.thalesgroup.optet.devenv.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {

		
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.ALIAS_DELIVERY, "optet");
		store.setDefault(PreferenceConstants.KEYSTORE_PW_DELIVERY, "password");
		store.setDefault(PreferenceConstants.KEY_PW_DELIVERY, "password");
		store.setDefault(PreferenceConstants.KEYSTORE_PATH_DELIVERY, System.getenv("USERPROFILE") + "\\.keystore");
		store.setDefault(PreferenceConstants.ALIAS, "optet");
		store.setDefault(PreferenceConstants.KEYSTORE_PW, "password");
		store.setDefault(PreferenceConstants.KEY_PW, "password");
		store.setDefault(PreferenceConstants.KEYSTORE_PATH, System.getenv("USERPROFILE") + "\\.keystore");
		store.setDefault(PreferenceConstants.CERTIFICAT_URL, "https://localhost:8443/JAXRS-FileUpload/rest/files/uploadCertificate");
		store.setDefault(PreferenceConstants.MARKETPLACE_URL, "https://localhost:8443/JAXRS-FileUpload/rest/files/uploadProduct");
		store.setDefault(PreferenceConstants.CERTIFICAT_PATH, System.getenv("USERPROFILE") + "\\certificat.xml");
		store.setDefault(PreferenceConstants.jarsigner_PATH_DELIVERY, "C:/Program Files/Java/jdk1.7.0/bin/jarsigner.exe");


		store.setDefault(PreferenceConstants.METRICTOOL_HOST, "130.206.83.245");
		store.setDefault(PreferenceConstants.METRICTOOL_PORT, "8080");
		store.setDefault(PreferenceConstants.PROXYURL, "proxy.theresis.thales");
		store.setDefault(PreferenceConstants.PROXYPORT, "80");
		store.setDefault(PreferenceConstants.PROXYLOGIN, "password");
		store.setDefault(PreferenceConstants.PROXYPASSWORD, "password");

	
	
	}

}
