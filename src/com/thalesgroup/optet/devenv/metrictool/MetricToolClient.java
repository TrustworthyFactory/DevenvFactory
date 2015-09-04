package com.thalesgroup.optet.devenv.metrictool;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.bindings.Scheme;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.thalesgroup.optet.devenv.Activator;
import com.thalesgroup.optet.devenv.preferences.PreferenceConstants;

@SuppressWarnings("deprecation")
public class MetricToolClient {




	private DefaultHttpClient httpclient = null;
	private HttpHost targetHost;
	private MetricToolClient mtc;
	private boolean initRealised = false; 
	
	private void init(){
		// TODO Auto-generated constructor stub

	
		String proxy_host = Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.PROXYURL, "", null);
		String proxy_port = Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.PROXYPORT, "", null);
		String metric_tool_host = Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.METRICTOOL_HOST, "", null);
		String metric_tool_port = Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.METRICTOOL_PORT, "", null);
		String login = Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.PROXYLOGIN, "", null);
		String password = Platform.getPreferencesService().getString(Activator.PLUGIN_ID, PreferenceConstants.PROXYPASSWORD, "", null);

        // Create and initialize scheme registry 
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(
                new org.apache.http.conn.scheme.Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        
        // Create an HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.
        HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, 100);
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

        
        httpclient = new DefaultHttpClient(cm, params);
//        httpclient.getParams().setParameter("http.connection.timeout", 1000);   
//        httpclient.getParams().setParameter("http.socket.timeout", 1000);   
//        httpclient.getParams().setParameter("http.protocol.version",HttpVersion.HTTP_1_1);  
        httpclient.getConnectionManager().closeIdleConnections(2000,TimeUnit.MILLISECONDS);
        
        
		httpclient.getCredentialsProvider().setCredentials(
				new AuthScope(proxy_host, Integer.parseInt(proxy_port)),
				new UsernamePasswordCredentials(login, password));

		targetHost = new HttpHost(metric_tool_host, Integer.parseInt(metric_tool_port), "http");
		HttpHost proxy = new HttpHost(proxy_host, Integer.parseInt(proxy_port));

		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		initRealised = true;
	}
	
	public MetricToolClient() {
		super();

	}



	public Map<String, String> getMetric(String id)throws IOException {
		System.out.println("call attributes for " + id);

		Map<String, String> metricsList = null;

		//MetricToolClient mtc= new MetricToolClient();
		String metricCall = callURL("/metrictoolapi/metrics/"+id);

		if (metricCall==null || metricCall.equals(""))
			return null;

		System.out.println("metricCall" + metricCall);

		JSONParser attrParser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory(){
			public List creatArrayContainer() {
				return new LinkedList();
			}

			public Map createObjectContainer() {
				return new LinkedHashMap();
			}

		};

		try{
			Map json = (Map)attrParser.parse(metricCall, containerFactory);
			ExtractMetricElement exctract = new ExtractMetricElement();
			metricsList = exctract.printRecursive(json);     

		}
		catch(ParseException pe){
			System.out.println(pe);
		}


		return metricsList;
	}


	public List<String> getMetrics(String id) throws IOException{

		System.out.println("call attributes for " + id);

		List<String> metricsList = null;

		//MetricToolClient mtc= new MetricToolClient();
		String metricCall = callURL("/metrictoolapi/attributes/"+id);

		if (metricCall==null || metricCall.equals(""))
			return null;

		System.out.println("metricCall" + metricCall);

		JSONParser attrParser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory(){
			public List creatArrayContainer() {
				return new LinkedList();
			}

			public Map createObjectContainer() {
				return new LinkedHashMap();
			}

		};

		try{
			Map json = (Map)attrParser.parse(metricCall, containerFactory);
			ExtractMetricsElement exctract = new ExtractMetricsElement();
			metricsList = exctract.printRecursive(json);     

		}
		catch(ParseException pe){
			System.out.println(pe);
		}


		return metricsList;
	}

	public Map<String, String> getAttributes() throws IOException{

		Map<String, String> attributeMap = null;
		//MetricToolClient mtc= new MetricToolClient();
		String attributeCall = callURL("/metrictoolapi/attributes");


		JSONParser attrParser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory(){
			public List creatArrayContainer() {
				return new LinkedList();
			}

			public Map createObjectContainer() {
				return new LinkedHashMap();
			}

		};

		try{
			Map json = (Map)attrParser.parse(attributeCall, containerFactory);
			ExtractAttributesElement exctract = new ExtractAttributesElement();
			attributeMap = exctract.printRecursive(json);     

		}
		catch(ParseException pe){
			System.out.println(pe);
		}
		return attributeMap;
	}





	private String callURL(String URL) throws IOException{
		String outputReturn  = ""; 

		if (!initRealised){
			init();
		}
		
		try {


			HttpGet httpget = new HttpGet(URL);
			httpget.addHeader("accept", "application/json");

			HttpResponse response = httpclient.execute(targetHost, httpget);
			HttpEntity entity = response.getEntity();

			BufferedReader br = new BufferedReader(
					new InputStreamReader((response.getEntity().getContent())));


			System.out.println("Output from Server .... \n");
			String output = "";
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				outputReturn = outputReturn + output;
			}

			try {
				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				EntityUtils.consume(response.getEntity());
			} finally {
				br.close();
			}
		} finally {
			//httpclient.getConnectionManager().shutdown();
		}
		return outputReturn;		
	}


//	public static void main(String[] args) throws Exception {
//
//
//
//		MetricToolClient mtc= new MetricToolClient();
//		String attributeCall = mtc.callURL("/metrictoolapi/attributes");
//
//
//		JSONParser attrParser = new JSONParser();
//		ContainerFactory containerFactory = new ContainerFactory(){
//			public List creatArrayContainer() {
//				return new LinkedList();
//			}
//
//			public Map createObjectContainer() {
//				return new LinkedHashMap();
//			}
//
//		};
//
//		try{
//			Map json = (Map)attrParser.parse(attributeCall, containerFactory);
//			ExtractAttributesElement exctract = new ExtractAttributesElement();
//			Map<String, String> attributeMap = exctract.printRecursive(json);     
//
//			for (Map.Entry<String, String> entry : attributeMap.entrySet()) {
//				System.out.println("Key : " + entry.getKey() + " Value : "
//						+ entry.getValue());
//
//				JSONParser metricParser = new JSONParser();
//				Map jsonMetric = (Map)attrParser.parse(attributeCall, containerFactory);
//				//				ExtractMetricElement exctractMetric = new ExtractMetricElement();
//				//				Map<String, String> metricMap = exctractMetric.printRecursive(jsonMetric);  
//			}
//		}
//		catch(ParseException pe){
//			System.out.println(pe);
//		}
//
//	}

}
