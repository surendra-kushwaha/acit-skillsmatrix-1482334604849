package com.acit.myalliance.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import com.acit.myalliance.sharepoint.client.HttpClient;
import com.acit.myalliance.util.AuthenticationUtil;
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
			
			String outStr=loadCache(username,tokenid);
			System.out.println("String sharedata + "+outStr);
			response.getWriter().append(outStr);
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
	

public String loadCache(String username,String password)throws Exception {
	String jsonPrettyPrintString = null;
	JSONObject xmlJSONObj=null;
	String kxActiveResponse=null;
	String sharePointData="";
	HttpResponse responseSP = null;
	String SOAP_ENV_TOKEN_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
			+ "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:saml=\"urn:oasis:names:tc:SAML:1.0:assertion\" xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:wssc=\"http://schemas.xmlsoap.org/ws/2005/02/sc\" xmlns:wst=\"http://schemas.xmlsoap.org/ws/2005/02/trust\">\r\n"
			+ "     <s:Header>\r\n"
			+ "          <wsa:Action s:mustUnderstand=\"1\">http://schemas.xmlsoap.org/ws/2005/02/trust/RST/Issue</wsa:Action>\r\n"
			+ "          <wsa:To s:mustUnderstand=\"1\">https://federation-sts.accenture.com/adfs/services/trust/2005/usernamemixed</wsa:To>\r\n"
			+ "          <wsa:MessageID>urn:uuid:%s</wsa:MessageID>\r\n"
			+ "          <ps:AuthInfo xmlns:ps=\"http://schemas.microsoft.com/Passport/SoapServices/PPCRL\" Id=\"PPAuthInfo\">\r\n"
			+ "               <ps:HostingApp>Managed IDCRL</ps:HostingApp>\r\n"
			+ "               <ps:BinaryVersion>6</ps:BinaryVersion>\r\n"
			+ "               <ps:UIVersion>1</ps:UIVersion>\r\n" + "               <ps:Cookies/>\r\n"
			+ "               <ps:RequestParams>AQAAAAIAAABsYwQAAAAxMDMz</ps:RequestParams>\r\n"
			+ "          </ps:AuthInfo>\r\n" + "          <wsse:Security>\r\n"
			+ "               <wsse:UsernameToken wsu:Id=\"user\">\r\n"
			+ "                    <wsse:Username>%s</wsse:Username>\r\n"
			+ "                    <wsse:Password>%s</wsse:Password>\r\n"
			+ "               </wsse:UsernameToken>\r\n" + "               <wsu:Timestamp Id=\"Timestamp\">\r\n"
			+ "                    <wsu:Created>%s</wsu:Created>\r\n"
			+ "                    <wsu:Expires>%s</wsu:Expires>\r\n" + "               </wsu:Timestamp>\r\n"
			+ "          </wsse:Security>\r\n" + "     </s:Header>\r\n" + "     <s:Body>\r\n"
			+ "          <wst:RequestSecurityToken Id=\"RST0\">\r\n"
			+ "               <wst:RequestType>http://schemas.xmlsoap.org/ws/2005/02/trust/Issue</wst:RequestType>\r\n"
			+ "               <wsp:AppliesTo>\r\n" + "                    <wsa:EndpointReference>\r\n"
			+ "                         <wsa:Address>urn:federation:MicrosoftOnline</wsa:Address>\r\n"
			+ "                    </wsa:EndpointReference>\r\n" + "               </wsp:AppliesTo>\r\n"
			+ "               <wst:KeyType>http://schemas.xmlsoap.org/ws/2005/05/identity/NoProofKey</wst:KeyType>\r\n"
			+ "          </wst:RequestSecurityToken>\r\n" + "     </s:Body>\r\n" + "</s:Envelope>\r\n";

	String SERVICE_TOKEN_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
			+ "<S:Envelope xmlns:S=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:wst=\"http://schemas.xmlsoap.org/ws/2005/02/trust\">\r\n"
			+ "  <S:Header>\r\n"
			+ "    <wsa:Action S:mustUnderstand=\"1\">http://schemas.xmlsoap.org/ws/2005/02/trust/RST/Issue</wsa:Action>\r\n"
			+ "    <wsa:To S:mustUnderstand=\"1\">https://login.microsoftonline.com/rst2.srf</wsa:To>\r\n"
			+ "    <ps:AuthInfo xmlns:ps=\"http://schemas.microsoft.com/LiveID/SoapServices/v1\" Id=\"PPAuthInfo\">\r\n"
			+ "      <ps:BinaryVersion>5</ps:BinaryVersion>\r\n"
			+ "      <ps:HostingApp>Managed IDCRL</ps:HostingApp>\r\n" + "    </ps:AuthInfo>\r\n"
			+ "    <wsse:Security>%s</wsse:Security>\r\n" + "  </S:Header>\r\n" + "  <S:Body>\r\n"
			+ "    <wst:RequestSecurityToken xmlns:wst=\"http://schemas.xmlsoap.org/ws/2005/02/trust\" Id=\"RST0\">\r\n"
			+ "      <wst:RequestType>http://schemas.xmlsoap.org/ws/2005/02/trust/Issue</wst:RequestType>\r\n"
			+ "      <wsp:AppliesTo>\r\n" + "        <wsa:EndpointReference>\r\n"
			+ "          <wsa:Address>%s</wsa:Address>\r\n" + "        </wsa:EndpointReference>\r\n"
			+ "      </wsp:AppliesTo>\r\n" + "       <wsp:PolicyReference URI=\"MBI\"/>\r\n"
			+ "    </wst:RequestSecurityToken>\r\n" + "  </S:Body>\r\n" + "</S:Envelope>\r\n";

	System.out.println("hi1");
	String paramMessageId = UUID.randomUUID().toString();
	TimeZone gmtTZ = TimeZone.getTimeZone("GMT");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	formatter.setTimeZone(gmtTZ);
	Calendar calendar = Calendar.getInstance(gmtTZ);
	Date timestampRequest = calendar.getTime();
	calendar.add(Calendar.MINUTE, 5);
	Date timestampExpiryRequest = calendar.getTime();
	String paramTimestampRequest = formatter.format(timestampRequest);
	String paramTimestampExpiryRequest = formatter.format(timestampExpiryRequest);
	String paramTokenId = "uuid-" + UUID.randomUUID().toString() + "-1";
	String onlineCRMAuthSOAPEnvelope = String.format(SOAP_ENV_TOKEN_REQUEST, paramMessageId,
			username, password, paramTimestampRequest, paramTimestampExpiryRequest,
			paramTokenId

	);
	System.out.println("hi2");
	try {
		HttpResponse samlResponse = postRequest(
				new URI("https://federation-sts.accenture.com/adfs/services/trust/2005/usernamemixed"),
				onlineCRMAuthSOAPEnvelope);
		String samlResponseVal = EntityUtils.toString(samlResponse.getEntity());
		//System.out.println("SAML Token Response::: " + samlResponseVal);
		
		Pattern p = Pattern.compile("<t:RequestedSecurityToken>(.*?)</t:RequestedSecurityToken>");
		Matcher m = p.matcher(samlResponseVal);
		String codeGroup = null;
		if (m.find()) {
			// get the SAML token
			codeGroup = m.group(1);	
			
		}else{
			//SAML Token not found
			//Authentication Failed
		}
		
		System.out.println("hi3");
		
		String serviceURL = "https://ts.accenture.com/sites/Accenture%20Innovation%20Center%20for%20IBM%20Technologies/Pages/default.aspx";
		String serviceTokenRequestFormat = String.format(SERVICE_TOKEN_REQUEST, codeGroup, serviceURL);
		HttpResponse serviceTokenResponse = postRequest(new URI("https://login.microsoftonline.com/extSTS.srf"),
				serviceTokenRequestFormat);
		String serviceTokenResponseVal = EntityUtils.toString(serviceTokenResponse.getEntity());
		//System.out.println("Service token response::: " + serviceTokenResponseVal);
		p = Pattern.compile("<wsse:BinarySecurityToken Id=\"Compact0\">(.*?)</wsse:BinarySecurityToken>");
		m = p.matcher(serviceTokenResponseVal);
		String binaryToken = null;
		if (m.find()) {
			// get the binary token
			binaryToken = m.group(1);				
		}

		System.out.println("hi4");
		
		String cookiesURL = "https://login.microsoftonline.com/login.srf?wa=wsignin1.0&wreply=https://ts.accenture.com/sites/Accenture%20Innovation%20Center%20for%20IBM%20Technologies/Lists/ACIT%20Skills%20Matrix/ACITSkillsMatrix.aspx?wa-wsignin1.0";
		System.out.println("Cookies Service URL:::" + cookiesURL);
		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();			
		
		CookieStore cookieStore = new BasicCookieStore();
        HttpClientContext cookiesContext = HttpClientContext.create();
        cookiesContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
       
		CloseableHttpClient httpclient = HttpClients.custom().setRedirectStrategy(redirectStrategy).build();		
		System.out.println("hi5");
		
		HttpGet getFedAuthRequest = new HttpGet("https://ts.accenture.com/sites/Accenture%20Innovation%20Center%20for%20IBM%20Technologies/_vti_bin/idcrl.svc");
		getFedAuthRequest.addHeader("Authorization","BPOSIDCRL "+binaryToken);
		HttpResponse responseFedAuth = httpclient.execute(getFedAuthRequest);
		System.out.println("hi6");
		Header[] headers = responseFedAuth.getAllHeaders();
		//fedAUth = null;
		for (Header header : headers) {
			System.out.println("Key : " + header.getName() + " ,Value : " + header.getValue());
			if("Set-Cookie".equalsIgnoreCase(header.getName())){
				String cookieVal = header.getValue();
				fedAUth = cookieVal.substring(cookieVal.indexOf("SPOIDCRL="), cookieVal.indexOf(';'));
				System.out.println("FedAuth Value::"+fedAUth);
			}

		}
		System.out.println("hi7");
		HttpResponse activeAllianceResponse=null;
		//Invoking service
		CloseableHttpClient httpclientSP = HttpClients.createDefault();
		//Invoking active alliance service	
		System.out.println("hi8");
		//HttpGet getActiveAllianceRequest = new HttpGet(Utility.getProperties("activeAllianceURL"));
		HttpGet getActiveAllianceRequest = new HttpGet("https://ts.accenture.com/sites/Accenture%20Innovation%20Center%20for%20IBM%20Technologies/_vti_bin//ListData.svc/ACITSkillsMatrix");
		getActiveAllianceRequest.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		getActiveAllianceRequest.addHeader("Cookie", fedAUth);
		HttpResponse response = httpclientSP.execute(getActiveAllianceRequest);
		
		 sharePointData=IOUtils.toString(response.getEntity().getContent());
		try {
			//xmlJSONObj = XML.toJSONObject(IOUtils.toString(inputStream1));
			//xmlJSONObj = XML.toJSONObject(total.toString());
			System.out.println("xmlJSONObj:A:::"+xmlJSONObj);
			//JSONObject xmlJSONObj = XML.toJSONObject(kxActiveResponse);
			JsonArray contactListArray = new JsonArray();
			System.out.println("hi11");
			String sharePointURL=Utility.getProperties("activeAllianceURL");
			//InputStream inputStream = convertToJSON(sharePointURL, inputStream1, xmlJSONObj, contactListArray);
			//jsonPrettyPrintString = contactListArray.toString();
			System.out.println("hi12");
			//jsonPrettyPrintString = new String(IOUtils.toString(inputStream, "UTF-8"));

		} catch (Exception je) {
			//LOG.error(je.toString());
		}
		System.out.println("Active alliance loaded to cache");
				
		//return responseFedAuth.getStatusLine().getStatusCode();

	}catch (Exception e) {
		System.out.println("Exception in loadcahce method"+e);
		// TODO Auto-generated catch block
		//throw new Exception(e);
	} /*
		 * catch (ParserConfigurationException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } catch (SAXException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */
	//return jsonPrettyPrintString;
	return sharePointData;
}

/*
 * Convert XML to JSON
 */
private static InputStream convertToJSON(String sharepointURL, InputStream inputStream, JSONObject xmlJSONObj,
		JsonArray contactListArray) throws JSONException, Exception, IOException {
	System.out.println("convert to json method::");
	String kxResponse;
	InputStream instream;
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
	return inputStream;
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
