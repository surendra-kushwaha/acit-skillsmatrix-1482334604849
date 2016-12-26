package com.acit.myalliance.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.acit.myalliance.util.Main;
import com.acit.myalliance.util.Utility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;




/**
 * Servlet implementation class ExpiredAllianceService
 */
@WebServlet("/SkillsService")
public class SkillsService extends HttpServlet {
	final static long serialVersionUID = 1L;
	static String fedAUth = null;
	static{
    	BasicConfigurator.configure();
    }
    static Logger log = Logger.getLogger(SkillsService.class.getName());
   @Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    InputStream Io=null;
	    OutputStream out=null;
		try {
			log.info("Authenticate User");
			
			response.setContentType("application/json");
			response.addHeader("Access-Control-Allow-Origin","*");
			response.addHeader("Access-Control-Allow-Methods","GET,POST");
			response.addHeader("Access-Control-Allow-Headers","Origin,X-Requested-With,Content-Type,Accept");
				
			
			//log.info("Expired alliance Details");
			String serviceUser = Utility.getProperties("NtUserName");
			String servicePwd = Utility.getProperties("NtPassword");
			String workStation = Utility.getProperties("NtWorkStation");
			String domain = Utility.getProperties("NtDomain");
			log.info("Authenticate User usrename"+serviceUser);
			//username=3ju5HiWoAERMfjByOjZCtj2pU7XlC87IlqTRNyS1xBg%3D&tokenid=zJqhOBFwwkCIUsTpg6BQawdLUfXUnsoQjKkMJNfrtbo%3D
			//String uname=Utility.decrypt("xxIzahJDpHvaL4rW9AGRj%2Bw%2Fg0KHYChH%2Fib8gwy%2BO1ZRJbGH3dV%2B%2B4BdTG5%2FzhKVxKZuiwGXe3JYjVM3MmaDJw%3D%3D");
			//String pwd=Utility.decrypt("qwCdU7NgJCbzdgPMmg5jooLT4tzAScVv2M%2F9YLei6jQ%3D");
			//System.out.println("uname###:"+uname);
			//System.out.println("pwd###:"+pwd);
			
			//String uname1=Utility.decrypt("3ju5HiWoAERMfjByOjZCtj2pU7XlC87IlqTRNyS1xBg%3D");
			//String pwd1=Utility.decrypt("zJqhOBFwwkCIUsTpg6BQawdLUfXUnsoQjKkMJNfrtbo%3D");
			//System.out.println("uname###$$:"+uname1);
			//System.out.println("pwd###$$:"+pwd1);
			//inputStream = HttpClient.get(sharePointURL, ntCredentials);
			
			System.out.println(System.getenv("GENERIC_USERID"));
			System.out.println(System.getenv("PASSWORD"));
			
			Map env = System.getenv();
			String genericid = (String) env.get("GENERIC_USERID");
			String genericpwd= (String) env.get("PASSWORD");
			
			
			System.out.println("user defined env data::"+genericid);
			String username=Utility.getProperties("GenericUserName");
	    	String tokenid=Utility.getProperties("GenericPassword");
	    	System.out.println("user defined env data::"+genericpwd);
			
			//String outStr=loadCache(username,tokenid);
	    	
	    	String urlStr = "https://ts.accenture.com/sites/Accenture%20Innovation%20Center%20for%20IBM%20Technologies/_vti_bin//ListData.svc/ACITSkillsMatrix";
			String domain1 = "dir"; // May also be referred as realm
			String userName = "surendra.kushwaha@accenture.com";
			String password = "Dec@2016";		
			Main amian=new Main();
			String responseText = amian.getAuthenticatedResponse(urlStr, domain1, userName, password);
			JSONObject xmlJSONObj=null;
			xmlJSONObj = XML.toJSONObject(responseText);
			System.out.println("String sharedata + "+responseText);
			
			JsonArray contactListArray = new JsonArray();
			String sharePointURL=Utility.getProperties("activeAllianceURL");
			String sharePointJsonData = convertToJSON(sharePointURL, xmlJSONObj, contactListArray);
			//jsonPrettyPrintString = contactListArray.toString();
			System.out.println("hi12");
			//String jsonPrettyPrintString = new String(IOUtils.toString(inputStream, "UTF-8"));
			
			//response.getWriter().append(xmlJSONObj.toString());
			response.getWriter().append(sharePointJsonData);
			//out.write("user authenticated");
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}finally
		{
			try{
				out.close();
				Io.close();
			}catch(Exception e){
				log.error(e.getMessage());
			}
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
	

/*
 * Convert XML to JSON
 */
private static String convertToJSON(String sharepointURL,JSONObject xmlJSONObj,
		JsonArray contactListArray) throws JSONException, Exception, IOException {
	System.out.println("convert to json method::");
	String kxResponse;
	InputStream instream;
	InputStream inputStream;
	JsonObject allianceJson;
	JsonObject contactInArray;
	JSONArray jsonarray;
	System.out.println("sharepointURL:"+sharepointURL);
	jsonarray = xmlJSONObj.getJSONObject("feed").getJSONArray("entry");
	System.out.println("jsonarray:::"+jsonarray);
	// Iterate through entry to get alliance details
	for (int i = 0; i < jsonarray.length(); i++) {
		JSONObject jsonobject = jsonarray.getJSONObject(i);
		allianceJson = new JsonObject();
		JSONObject propertiesJson = jsonobject.getJSONObject("content").getJSONObject("m:properties");
		System.out.println("propertiesJson::"+propertiesJson);
		Iterator keyIterator = propertiesJson.keys();
		while (keyIterator.hasNext()) {
			String ele = (String) keyIterator.next();
			if (!isValidJson(propertiesJson, ele)) {
				if (ele.equalsIgnoreCase("d:Version")) {
					allianceJson.addProperty(ele.substring(2),
							jsonobject.getJSONObject("content").getJSONObject("m:properties").getDouble(ele));
				} else {
					allianceJson.addProperty(ele.substring(2),
							jsonobject.getJSONObject("content").getJSONObject("m:properties").getString(ele));
				}
			} else {
				if (ele.equalsIgnoreCase("d:Version")) {
					allianceJson.addProperty(ele.substring(2), jsonobject.getJSONObject("content")
							.getJSONObject("m:properties").getJSONObject(ele).getDouble("content"));
				} else {
					allianceJson.addProperty(ele.substring(2), jsonobject.getJSONObject("content")
							.getJSONObject("m:properties").getJSONObject(ele).optString("content", ""));
				}
			}
		}
		contactInArray = new JsonObject();
		contactInArray.add("properties", allianceJson);
		contactListArray.add(contactInArray);
	}
	return contactListArray.toString();
}

private static boolean isValidJson(JSONObject jsonStr, String element) {
	boolean isValid = false;
	try {
		jsonStr.getJSONObject(element);
		isValid = true;
	} catch (JSONException je) {
		isValid = false;
	}
	return isValid;
}

public static HttpResponse postRequest(URI serviceUri, String requestMessage) throws ParseException, IOException {

	HttpResponse response = null;
	CloseableHttpClient httpclient = HttpClients.createDefault();
	HttpPost post = new HttpPost(serviceUri);
	StringEntity entity = new StringEntity(requestMessage);
	post.setHeader("Content-Type", "application/soap+xml; charset=UTF-8");
	post.setEntity(entity);
	response = httpclient.execute(post);		
	return response;		

}

public static HttpResponse getRequest(URI serviceUri, String requestMessage) throws ParseException, IOException {

	HttpResponse response = null;
	CloseableHttpClient httpclient = HttpClients.createDefault();
	HttpPost post = new HttpPost(serviceUri);
	StringEntity entity = new StringEntity(requestMessage);
	post.setHeader("Content-Type", "application/soap+xml; charset=UTF-8");
	post.setEntity(entity);
	response = httpclient.execute(post);		
	return response;		

}
	
}
