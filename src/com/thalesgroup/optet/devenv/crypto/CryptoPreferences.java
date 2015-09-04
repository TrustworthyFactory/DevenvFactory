package com.thalesgroup.optet.devenv.crypto;

import org.eclipse.core.runtime.Platform;

import com.thalesgroup.optet.devenv.Activator;
import com.thalesgroup.optet.devenv.preferences.PreferenceConstants;
/**
 * CryptoPreference return the user preference from the preference page 
 * @author F. Motte
 *
 */
public class CryptoPreferences {
	/**
	 * return the certificate path
	 * @return the certificate path
	 */
	public String getCertificatePath () {
		return Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.CERTIFICAT_PATH, "", null);
	}
	
	/**
	 * return the certificate URL
	 * @return the certificate URL
	 */
	public String getCertificateURL () {
		return Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.CERTIFICAT_URL, "", null);
	}
	
	/**
	 * return the alias define into the keystore
	 * @return the alias
	 */
	public String getAlias () {
		return Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.ALIAS, "", null);
	}

	/**
	 * Return the password of the certificate
	 * @return the password
	 */
	public char[] getPassword () {
		return Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.KEY_PW, "", null).toCharArray();
	}
	
	/**
	 * return the keystore path of the user preference
	 * @return keystore path
	 */
	public String getKeystorePath () {
		return Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.KEYSTORE_PATH, "", null);
	}

	/**
	 * return the keystore password
	 * @return keystore password
	 */
	public char[] getKeystorePassword () {
		return Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.KEYSTORE_PW, "", null).toCharArray();
	}


}
