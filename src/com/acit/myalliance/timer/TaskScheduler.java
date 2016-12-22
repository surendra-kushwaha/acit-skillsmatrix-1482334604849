package com.acit.myalliance.timer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.acit.myalliance.util.AuthenticationUtil;
import com.acit.myalliance.util.CacheUtil;
import com.acit.myalliance.util.Utility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ibm.websphere.objectgrid.ObjectMap;
 
/**
 * Class to extend and implement custom "Cron Job" equivalent because with
 * bluemix apps, service deployer does have access to VM's cron capability
 * 
 * @author surendra
 * 
 */
public class TaskScheduler {
 
private static final Log LOG = LogFactory.getLog(TaskScheduler.class);
public String cron_name;
public String command;
public int interval;
public boolean isEnabled;
static String fedAUth = null;
/*
@Override
public void run() {
    LOG.info("Job started at:" + new Date());
    try{
    	 if (this.isEnabled) {
    	String username=Utility.getProperties("GenericUserName");
    	String tokenid=Utility.getProperties("GenericPassword");
		loadCache(username,tokenid);
    	 } else {
             this.cancel();
    	 }
		}catch(Exception e){
			System.out.println("Exception Occured"+e.getMessage());
	}
    LOG.info("Job completed at:" + new Date());
}
*/

public String runSharePoint() {
    LOG.info("Job started at:" + new Date());
    String data="";
    try{
    	 if (this.isEnabled) {
    	String username=Utility.getProperties("GenericUserName");
    	String tokenid=Utility.getProperties("GenericPassword");
		data= loadCache(username,tokenid);
    	 } else {
             //this.cancel();
    	 }
		}catch(Exception e){
			System.out.println("Exception Occured"+e.getMessage());
	}
    LOG.info("Job completed at:" + new Date());
    return data;
}
  
public void setCronName(String value) {
        cron_name = value;
}
 
public String getCronName() {
        return cron_name;
}
 
 
public void setInterval(int value) {
        interval = value;
}
 
public String getInterval() {
        return "" + interval;
}
 
public void setEnableState(boolean value) {
        isEnabled = value;
}
 
public boolean getEnableState() {
        return isEnabled;
}

public static String loadCache(String username,String password)throws Exception {
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
		
		String serviceURL = "https://ts.accenture.com/sites/AlliancesWizard/myAlliance/Pages/default.aspx";
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
		
		String cookiesURL = "https://login.microsoftonline.com/login.srf?wa=wsignin1.0&wreply=https://ts.accenture.com/sites/AlliancesWizard/myAlliance/Pages/default.aspx/_forms/default.aspx?wa-wsignin1.0";
		System.out.println("Cookies Service URL:::" + cookiesURL);
		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();			
		
		CookieStore cookieStore = new BasicCookieStore();
        HttpClientContext cookiesContext = HttpClientContext.create();
        cookiesContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
       
		CloseableHttpClient httpclient = HttpClients.custom().setRedirectStrategy(redirectStrategy).build();		
		System.out.println("hi5");
		
		HttpGet getFedAuthRequest = new HttpGet("https://ts.accenture.com/sites/AlliancesWizard/myAlliance/_vti_bin/idcrl.svc");
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
		HttpResponse nameChangedAllianceResponse=null;
		String kxActiveResponse=null;
		String kxNameChangedResponse=null;
		//Invoking service
		CloseableHttpClient httpclientSP = HttpClients.createDefault();
		//Invoking active alliance service	
		System.out.println("hi8");
		HttpGet getActiveAllianceRequest = new HttpGet(Utility.getProperties("activeAllianceURL"));
		getActiveAllianceRequest.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		getActiveAllianceRequest.addHeader("Cookie", fedAUth);
		System.out.println("hi9");
		activeAllianceResponse = httpclientSP.execute(getActiveAllianceRequest);		
		InputStream inputStream =activeAllianceResponse.getEntity().getContent();		
		kxActiveResponse = new String(IOUtils.toString(inputStream, "UTF-8"));
		InputStream instream = IOUtils.toInputStream(kxActiveResponse);
		System.out.println("hi10");
		String jsonPrettyPrintString = null;
		try {
			JSONObject xmlJSONObj = XML.toJSONObject(IOUtils.toString(instream));
			JsonArray contactListArray = new JsonArray();
			System.out.println("hi11");
			String sharePointURL=Utility.getProperties("activeAllianceURL");
			inputStream = convertToJSON(sharePointURL, inputStream, xmlJSONObj, contactListArray);
			jsonPrettyPrintString = contactListArray.toString();

		} catch (JSONException je) {
			LOG.error(je.toString());
		}
		System.out.println("Active alliance loaded to cache");
		//Invoking expired alliance service
		System.out.println("hi12");
		HttpGet getSPRequest = new HttpGet(Utility.getProperties("expiredAllianceURL"));
		getSPRequest.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		getSPRequest.addHeader("Cookie", fedAUth);
		responseSP = httpclientSP.execute(getSPRequest);		
		InputStream inputStreamExpired =responseSP.getEntity().getContent();		
		String kxResponse = new String(IOUtils.toString(inputStreamExpired, "UTF-8"));
		System.out.println("Expired alliance loaded to cache");
		System.out.println("hi13");
		//Invoking expired name change service
		HttpGet getNameChangedRequest = new HttpGet(Utility.getProperties("nameChangedAllianceURL"));
		getNameChangedRequest.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		getNameChangedRequest.addHeader("Cookie", fedAUth);
		nameChangedAllianceResponse = httpclientSP.execute(getNameChangedRequest);		
		InputStream inputStreamName =nameChangedAllianceResponse.getEntity().getContent();		
		kxNameChangedResponse = new String(IOUtils.toString(inputStreamName, "UTF-8"));
		System.out.println("Name Changed alliance loaded to cache");
		System.out.println("hi14");
		String mapName = CacheUtil.dataServiceMapName;
		System.out.println("hi15");
		String username1 = null;
		String password1= null;
		String endpoint = null;
		String gridName = null;
		if (CacheUtil.ogSession == null || CacheUtil.mapExpValClear) {
		Map env = System.getenv();
		String vcap = (String) env.get("VCAP_SERVICES");

		if (vcap == null) {
			System.out.println("No VCAP_SERVICES found");
		} else {System.out.println("VCAP_SERVICES found");
			try {
				if (CacheUtil.ivObjectGrid == null && !CacheUtil.connectedGrid) {
					System.out.println("connectGrid null");
					JSONObject obj = new JSONObject(vcap);
					String[] names = JSONObject.getNames(obj);
					if (names != null) {
						for (String name : names) {
							if (name.startsWith("DataCache")) {
								JSONArray val = obj.getJSONArray(name);
								JSONObject serviceAttr = val.getJSONObject(0);
								JSONObject credentials = serviceAttr.getJSONObject("credentials");
								username1= credentials.getString("username");
								password1= credentials.getString("password");
								endpoint = credentials.getString("catalogEndPoint");
								gridName = credentials.getString("gridName");
								mapName = CacheUtil.dataServiceMapName;
								break;
							}
						}
					}
					//CacheUtil.connectToGrid(username1, password1, endpoint, gridName);
				}

			} catch (Exception e) {
				LOG.error("Exception Message: " + e.getMessage());
				e.printStackTrace();
			}
		}

	}
		
		ObjectMap map = null;
		map = CacheUtil.ogSession.getMap(mapName);
		System.out.println("hi16");
		int timeToLive = Integer.parseInt(Utility.getProperties("timeToLive"));
		System.out.println("hi17");
		map.setTimeToLive(timeToLive);
		System.out.println("hi18");
		map.upsert("activeAlliance", jsonPrettyPrintString);
		System.out.println("hi19");
		map.upsert("expiredAlliance", kxResponse);
		map.upsert("renamedAlliance", kxNameChangedResponse);
		System.out.println("hi20");
		CacheUtil.mapExpValClear = false;
		System.out.println("hi21");
		
		
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
	return responseSP.getEntity().getContent().toString();
}

/*
 * Convert XML to JSON
 */
private static InputStream convertToJSON(String sharepointURL, InputStream inputStream, JSONObject xmlJSONObj,
		JsonArray contactListArray) throws JSONException, Exception, IOException {
	String kxResponse;
	InputStream instream;
	JsonObject allianceJson;
	JsonObject contactInArray;
	JSONArray jsonarray;
	jsonarray = xmlJSONObj.getJSONObject("feed").getJSONArray("entry");
	// Iterate through entry to get alliance details
	for (int i = 0; i < jsonarray.length(); i++) {
		JSONObject jsonobject = jsonarray.getJSONObject(i);
		allianceJson = new JsonObject();
		JSONObject propertiesJson = jsonobject.getJSONObject("content").getJSONObject("m:properties");
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
				if (ele.equalsIgnoreCase("d:Id")) {
					int Id = 0;
					if (isValidJson(propertiesJson, "d:Id")) {
						Id = propertiesJson.getJSONObject("d:Id").getInt("content");
					}
					// TEP Start
					//System.out.println("d:Id###"+Id);
					// TEP Start
					//String TEPRSUrl = sharepointURL + "(" + Id + ")" + "/TEPRelationshipLead";
					//String TEPRSUrl = sharepointURL + "(6)" + "/TEPRelationshipLead";
					//inputStream = HttpClient.get(TEPRSUrl, ntCredentials);
					//inputStream = AuthenticationUtil.getSharePointTEPData(Id);
					//System.out.println("Fetching TEP");
					
					HttpResponse responseSP = null;
					//Invoking service
					CloseableHttpClient httpclientSP = HttpClients.createDefault();
					HttpGet getSPRequest = new HttpGet("https://ts.accenture.com/sites/AlliancesWizard/myAlliance/_vti_bin/ListData.svc/ActiveAlliances("+Id+")/TEPRelationshipLead");
					getSPRequest.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
					//getSPRequest.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
					getSPRequest.addHeader("Cookie", fedAUth);
					
					responseSP = httpclientSP.execute(getSPRequest);
					
					InputStream inputstreamTep=responseSP.getEntity().getContent();
					
					
					//System.out.println("TEPRS ID@@"+Id);
					//System.out.println("serviceUser@@"+serviceUser);
					//System.out.println("servicePwd@@"+servicePwd);
					
					kxResponse = new String(IOUtils.toString(inputstreamTep, "UTF-8"));
					instream = IOUtils.toInputStream(kxResponse);
					xmlJSONObj = XML.toJSONObject(IOUtils.toString(instream));

					//log.info("Get TEP Relationship lead name and email");

					//log.info("Get TEP Relationship lead name and email");
					JSONArray jsonTEParray;
					jsonTEParray = xmlJSONObj.getJSONObject("feed").optJSONArray("entry");
					String tepRelLeadName = "", tepRelLeadEmail = "";
					if (jsonTEParray instanceof JSONArray) {
						for (int j = 0; j < jsonTEParray.length(); j++) {
							JSONObject jsonTEParray1 = jsonTEParray.optJSONObject(j);
							if (jsonTEParray1 != null) {
								JSONObject propertiesTEPJson = jsonTEParray1.getJSONObject("content")
										.getJSONObject("m:properties");
								tepRelLeadName += propertiesTEPJson.getString("d:Name") + ";";
								tepRelLeadEmail += propertiesTEPJson.getString("d:WorkEmail") + ";";
							}
						}
						allianceJson.addProperty("TEPRSLeadName", tepRelLeadName);
						allianceJson.addProperty("TEPRSLeadEmail", tepRelLeadEmail);
					} else {
						if (xmlJSONObj.getJSONObject("feed").optJSONObject("entry") != null) {
							allianceJson.addProperty("TEPRSLeadName",
									xmlJSONObj.getJSONObject("feed").getJSONObject("entry").getJSONObject("content")
											.getJSONObject("m:properties").getString("d:Name"));
							allianceJson.addProperty("TEPRSLeadEmail",
									xmlJSONObj.getJSONObject("feed").getJSONObject("entry").getJSONObject("content")
											.getJSONObject("m:properties").getString("d:WorkEmail"));
						} else {
							allianceJson.addProperty("TEPRSLeadName", "");
							allianceJson.addProperty("TEPRSLeadEmail", "");
						}
					}
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