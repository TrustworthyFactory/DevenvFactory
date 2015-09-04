package com.thalesgroup.optet.devenv.preferences;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.eclipse.core.runtime.Platform;

import com.thalesgroup.optet.devenv.Activator;

/**
 * Verifies a signed jar file given an array of truted CA certs
 *
 * @author Andrew Harrison
 * @version $Revision: 148 $
 * @created Apr 11, 2007: 11:02:26 PM
 * @date $Date: 2007-04-12 13:31:48 +0100 (Thu, 12 Apr 2007) $ modified by $Author: scmabh $
 * @todo Put your notes here...
 */


public class JarVerifier {

	public boolean verifyJar(String path)
	{
		boolean result = false;
		try {
			String jarsigner = Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.jarsigner_PATH_DELIVERY, "", null);

			System.out.println(jarsigner + " -verify " + path);
			Process p = Runtime.getRuntime().exec(jarsigner + " -verify " + path);
			p.waitFor();
			InputStream is = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null)
			{
				System.out.println(line);
				if(line.contains("jar verified")){
					return true;
				}else if (line.contains("jar is unsigned")){
					return true;
				}else{
					return false;
				}
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}
}