package com.acit.multiskilling.util;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.json.JSONObject;

public class Main {
	static String fedAUth = null;
	public static void main(String[] args) throws Exception {

		String urlStr = "https://ts.accenture.com/sites/Accenture%20Innovation%20Center%20for%20IBM%20Technologies/_vti_bin//ListData.svc/ACITSkillsMatrix";
		String domain = "dir"; // May also be referred as realm
		String userName = "surendra.kushwaha@accenture.com";
		String password = "Dec@2016";		

		String responseText = getAuthenticatedResponse(urlStr, domain, userName, password);

	    System.out.println("response: " + responseText);
	}

	public static String getAuthenticatedResponse(final String urlStr, final String domain, final String username, final String password) throws IOException {

	    StringBuilder response = new StringBuilder();

		Authenticator.setDefault(new Authenticator() {
	        @Override
	        public PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication("surendra.kushwaha@accenture.com", "Dec@2016".toCharArray());
	        }
	    });
		
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
			HttpGet getActiveAllianceRequest = new HttpGet(urlStr);
			//HttpGet getActiveAllianceRequest = new HttpGet("https://ts.accenture.com/sites/AlliancesWizard/myAlliance/_vti_bin//ListData.svc/IndexOfAllianceNameChanges");
			//HttpGet getActiveAllianceRequest = new HttpGet("https://ts.accenture.com/sites/Accenture%20Innovation%20Center%20for%20IBM%20Technologies/_vti_bin//ListData.svc/ACITSkillsMatrix");
			getActiveAllianceRequest.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			getActiveAllianceRequest.addHeader("Cookie", fedAUth);
			HttpResponse response1 = httpclientSP.execute(getActiveAllianceRequest);			
			sharePointData=IOUtils.toString(response1.getEntity().getContent());	
	    return sharePointData;
	}catch(Exception e){
		System.out.println(e);
		return "";
	}
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
