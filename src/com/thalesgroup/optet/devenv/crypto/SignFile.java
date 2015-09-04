package com.thalesgroup.optet.devenv.crypto;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Collections;

import javax.xml.crypto.dsig.*;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

//import sun.misc.BASE64Encoder;
import org.apache.commons.codec.binary.Base64;
import com.thalesgroup.optet.devenv.Activator;


import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;

import javax.xml.crypto.*;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dom.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.*;
import javax.xml.crypto.dsig.spec.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;

import org.w3c.dom.*;
/**
 * Generate file signature
 * @author F. Motte
 *
 */
public class SignFile {

	// the algothism used for the signature
	private final String algo = "SHA256withRSA";

	// the keystore instance
	private final String keystoreInstance = "JKS";

	// the crypto preference define into the preference apge
	private CryptoPreferences pref = null;

	// the private key of the certificate
	private PrivateKey privateKey = null;

	/**
	 * Constructor
	 */
	public SignFile() {
		super();

		XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance();


		this.pref = new CryptoPreferences();

		Activator.getDefault().logInfo("alias" + pref.getAlias());
		Activator.getDefault().logInfo("password" + String.valueOf(pref.getPassword()));
		Activator.getDefault().logInfo("keystore path" + pref.getKeystorePath());
		Activator.getDefault().logInfo("keystore pasword" +  String.valueOf(pref.getKeystorePassword()));

		try {
			KeyStore keystore=KeyStore.getInstance(keystoreInstance);
			keystore.load(new FileInputStream(pref.getKeystorePath()),pref.getKeystorePassword());
			KeyPair pair = getPrivateKey(keystore, pref.getAlias(), pref.getPassword());
			privateKey = pair.getPrivate();

		} catch (NoSuchAlgorithmException | CertificateException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void signXMLData(String inputFile) throws TransformerConfigurationException, TransformerFactoryConfigurationError, Exception, IllegalAccessException, ClassNotFoundException{
		// Instantiate the document to be signed
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
		Document doc = dbFactory
				.newDocumentBuilder()
				.parse(new FileInputStream(inputFile));

		// prepare signature factory
		String providerName = System.getProperty(
				"jsr105Provider", 
				"org.jcp.xml.dsig.internal.dom.XMLDSigRI"
				);

		final XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance(
				"DOM",
				(Provider) Class.forName(providerName).newInstance()
				);

		Node nodeToSign = null;
		Node sigParent = null;
		String referenceURI = null;
		XPathExpression expr = null; 
		NodeList nodes;
		List transforms = null;

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		sigParent = doc.getDocumentElement();
		referenceURI = ""; // Empty string means whole document
		transforms = Collections.singletonList(
				sigFactory.newTransform(
						Transform.ENVELOPED, 
						(TransformParameterSpec) null
						)
				);
		// Retrieve signing key
		KeyStore keyStore = KeyStore.getInstance(keystoreInstance);
		keyStore.load(
				new FileInputStream(pref.getKeystorePath()),  
				String.valueOf(pref.getPassword()).toCharArray()
				);

		PrivateKey privateKey = (PrivateKey) keyStore.getKey(
				pref.getAlias(),
				String.valueOf(pref.getKeystorePassword()).toCharArray()
				);

		X509Certificate cert = (X509Certificate) keyStore.getCertificate(pref.getAlias());
		PublicKey publicKey = cert.getPublicKey();

		// Create a Reference to the enveloped document
		Reference ref = sigFactory.newReference(
				referenceURI,
				sigFactory.newDigestMethod(DigestMethod.SHA256, null),
				transforms,
				null, 
				null
				);

		// Create the SignedInfo
		SignedInfo signedInfo = sigFactory.newSignedInfo(
				sigFactory.newCanonicalizationMethod(
						CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS, 
						(C14NMethodParameterSpec) null
						), 
						sigFactory.newSignatureMethod(
								SignatureMethod.RSA_SHA1, 
								null
								),
								Collections.singletonList(ref)
				);

		// Create a KeyValue containing the RSA PublicKey 
		KeyInfoFactory keyInfoFactory = sigFactory.getKeyInfoFactory();
		KeyValue keyValue = keyInfoFactory.newKeyValue(publicKey);

		// Create a KeyInfo and add the KeyValue to it
		KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(keyValue));

		// Create a DOMSignContext and specify the RSA PrivateKey and
		// location of the resulting XMLSignature's parent element
		DOMSignContext dsc = new DOMSignContext(
				privateKey, 
				sigParent
				);

		// Create the XMLSignature (but don't sign it yet)
		XMLSignature signature = sigFactory.newXMLSignature(signedInfo, keyInfo);

		// Marshal, generate (and sign) the enveloped signature
		signature.sign(dsc);

		// output the resulting document
		OutputStream os;
		os = new FileOutputStream(inputFile);

		Transformer trans = TransformerFactory.newInstance()
				.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));
	}

	/**
	 * Sign the date 
	 * @param data
	 * @return the signature of the data
	 * @throws NoSuchAlgorithmException
	 * @throws SignatureException
	 * @throws InvalidKeyException
	 */
	public String signData (String data) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException{


		Signature signature;
		signature = Signature.getInstance(algo);
		signature.initSign(privateKey);
		signature.update(data.getBytes());

		byte[] dataSignature = signature.sign(); // Get signature
		return new String(Base64.encodeBase64(dataSignature));
	}

	/**
	 * return the public and private key from the keystore
	 * @param keystore 
	 * @param alias
	 * @param password
	 * @return
	 */
	private KeyPair getPrivateKey(KeyStore keystore, String alias, char[] password) {
		try {
			Key key=keystore.getKey(alias,password);
			if(key instanceof PrivateKey) {
				Certificate cert=keystore.getCertificate(alias);
				cert.getPublicKey().getAlgorithm();
				PublicKey publicKey=cert.getPublicKey();
				return new KeyPair(publicKey,(PrivateKey)key);
			}
		} catch (UnrecoverableKeyException e) {
		} catch (NoSuchAlgorithmException e) {
		} catch (KeyStoreException e) {
		}
		return null;
	}




}
