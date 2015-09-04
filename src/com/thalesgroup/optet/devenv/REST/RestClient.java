package com.thalesgroup.optet.devenv.REST;


import java.io.File;
import java.io.FileInputStream;
//import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;  

import java.security.KeyStore;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

@SuppressWarnings("deprecation")
public class RestClient
{



	public static void main(String args[]) throws Exception
	{
		System.out.println("test");
		RestClient fileUpload = new RestClient () ;
		File file = new File("D:/temp/test/monfichier.txt") ;
		File file2 = new File("D:/temp/test/monfichier2.txt") ;
		File file3 = new File("D:/temp/test/monfichier3.txt") ;
		//Upload the file
		fileUpload.publishCertificate("https://localhost:8443/JAXRS-FileUpload/rest/files/uploadCertificate", file) ;
		fileUpload.publishProduct("https://localhost:8443/JAXRS-FileUpload/rest/files/uploadProduct", file2, file3) ;
	} 

	public void publishCertificate(String urlString, File certificate) throws Exception
	{
	    TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
	        @Override
	        public boolean isTrusted(X509Certificate[] certificate, String authType) {
	            return true;
	        }
	    };
	    SSLSocketFactory sf = new SSLSocketFactory(acceptingTrustStrategy, 
	      SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	    SchemeRegistry registry = new SchemeRegistry();
	    registry.register(new Scheme("https", 8443, sf));
		DefaultHttpClient client = new DefaultHttpClient();
		client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 8443, sf));





		//        // Trust own CA and all self-signed certs
		//        SSLContext sslcontext = SSLContexts.custom()
		//                .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
		//                .build();
		//        // Allow TLSv1 protocol only
		//        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
		//                sslcontext,
		//                new String[] { "TLSv1" },
		//                null,
		//                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		//        CloseableHttpClient client = HttpClients.custom()
		//                .setSSLSocketFactory(sslsf)
		//                .build();




		//HttpClient client = new DefaultHttpClient() ;
		HttpPost postRequest = new HttpPost (urlString) ;
		try
		{


			MultipartEntity entity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );

			/* example for adding an image part */
			FileBody fileBody = new FileBody(certificate); //image should be a String
			entity.addPart("file", fileBody); 

			//Set to request body
			postRequest.setEntity(entity) ;

			//Send request
			HttpResponse response = client.execute(postRequest) ;

			//Verify response if any
			if (response != null)
			{
				System.out.println(response.getStatusLine().getStatusCode());
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace() ;
		}
	}

	@SuppressWarnings("deprecation")
	public void publishProduct(String urlString, File USDL, File product) throws Exception
	{

	    TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
	        @Override
	        public boolean isTrusted(X509Certificate[] certificate, String authType) {
	            return true;
	        }
	    };
	    SSLSocketFactory sf = new SSLSocketFactory(acceptingTrustStrategy, 
	      SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	    SchemeRegistry registry = new SchemeRegistry();
	    registry.register(new Scheme("https", 8443, sf));
		DefaultHttpClient client = new DefaultHttpClient();
		client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 8443, sf));


		//HttpClient client = new DefaultHttpClient() ;
		HttpPost postRequest = new HttpPost (urlString) ;
		try
		{
			MultipartEntity entity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );




			/* example for adding an image part */
			FileBody fileBodyUSDL = new FileBody(USDL); //image should be a String
			FileBody fileBodyProduct = new FileBody(product); //image should be a String
			entity.addPart("USDLFile", fileBodyUSDL); 
			entity.addPart("ProductFile", fileBodyProduct); 

			//Set to request body
			postRequest.setEntity(entity) ;


			//Send request
			HttpResponse response = client.execute(postRequest) ;

			//Verify response if any
			if (response != null)
			{
				System.out.println(response.getStatusLine().getStatusCode());
				System.out.println(response.getStatusLine().toString());
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace() ;
		}
	}
}