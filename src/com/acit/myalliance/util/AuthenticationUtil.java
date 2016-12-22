package com.acit.myalliance.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class AuthenticationUtil {
	
	static String fedAUth = null;
	
	public static int authenticateUser(String username,String password)throws Exception {
		HttpResponse responseSP = null;
		System.out.println(" username:"+(username));
		System.out.println(" pwd:"+(password));
		System.out.println("html username:"+stringToHtmlString(username));
		System.out.println("html pwd:"+stringToHtmlString(password));
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
			
			
			
			String serviceURL = "https://ts.accenture.com/sites/AlliancesWizard/myAlliance/Pages/default.aspx";
			String serviceTokenRequestFormat = String.format(SERVICE_TOKEN_REQUEST, codeGroup, serviceURL);
			HttpResponse serviceTokenResponse = postRequest(new URI("https://login.microsoftonline.com/extSTS.srf"),
					serviceTokenRequestFormat);
			String serviceTokenResponseVal = EntityUtils.toString(serviceTokenResponse.getEntity());
			System.out.println("Service token response::: " + serviceTokenResponseVal);
			p = Pattern.compile("<wsse:BinarySecurityToken Id=\"Compact0\">(.*?)</wsse:BinarySecurityToken>");
			m = p.matcher(serviceTokenResponseVal);
			String binaryToken = null;
			if (m.find()) {
				// get the binary token
				binaryToken = m.group(1);				
			}

			
			
			String cookiesURL = "https://login.microsoftonline.com/login.srf?wa=wsignin1.0&wreply=https://ts.accenture.com/sites/AlliancesWizard/myAlliance/Pages/default.aspx/_forms/default.aspx?wa-wsignin1.0";
			System.out.println("Cookies Service URL:::" + cookiesURL);
			LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();			
			
			CookieStore cookieStore = new BasicCookieStore();
	        HttpClientContext cookiesContext = HttpClientContext.create();
	        cookiesContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
	       
			CloseableHttpClient httpclient = HttpClients.custom().setRedirectStrategy(redirectStrategy).build();		
			
			
			HttpGet getFedAuthRequest = new HttpGet("https://ts.accenture.com/sites/AlliancesWizard/myAlliance/_vti_bin/idcrl.svc");
			getFedAuthRequest.addHeader("Authorization","BPOSIDCRL "+binaryToken);
			HttpResponse responseFedAuth = httpclient.execute(getFedAuthRequest);
			
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
			return responseFedAuth.getStatusLine().getStatusCode();

		}catch (Exception e) {
			// TODO Auto-generated catch block
			throw new Exception(e);
		} /*
			 * catch (ParserConfigurationException e) { // TODO Auto-generated
			 * catch block e.printStackTrace(); } catch (SAXException e) { //
			 * TODO Auto-generated catch block e.printStackTrace(); }
			 */
		//return responseSP.getEntity().getContent();
	}
		
	public static InputStream getSharePointData(String sharePointUrl)throws Exception {
		HttpResponse responseSP = null;
		//Invoking service
		//Invoking service
		CloseableHttpClient httpclientSP = HttpClients.createDefault();
		//HttpGet getSPRequest = new HttpGet("https://ts.accenture.com/sites/AlliancesWizard/myAlliance/_vti_bin/ListData.svc/ActiveAlliances(20)/TEPRelationshipLead");
		HttpGet getSPRequest = new HttpGet(sharePointUrl);
		getSPRequest.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		//getSPRequest.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
		getSPRequest.addHeader("Cookie", fedAUth);
		
		responseSP = httpclientSP.execute(getSPRequest);
		Thread.sleep(1000);
		System.out.println("Response Code SP:: " 
                + responseSP.getStatusLine().getStatusCode());
		//spResponseVal = EntityUtils.toString(responseSP.getEntity());
		//System.out.println("SP Service response::: " + spResponseVal);
		if (responseSP.getStatusLine().getStatusCode() != 200) {
			String requestStatusLine = responseSP.getStatusLine() + " "
					+ responseSP.getStatusLine().getStatusCode();
			System.out.println( "HttpClient:" + responseSP.toString());
			throw new Exception(requestStatusLine + " ");
		} else {
			return responseSP.getEntity().getContent();
		}
	}
	
	public static InputStream getSharePointTEPData(int id)throws Exception {
		HttpResponse responseSP = null;
		//Invoking service
		CloseableHttpClient httpclientSP = HttpClients.createDefault();
		HttpGet getSPRequest = new HttpGet("https://ts.accenture.com/sites/AlliancesWizard/myAlliance/_vti_bin/ListData.svc/ActiveAlliances("+id+")/TEPRelationshipLead");
		getSPRequest.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		//getSPRequest.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
		getSPRequest.addHeader("Cookie", fedAUth);
		
		responseSP = httpclientSP.execute(getSPRequest);
		if (responseSP.getStatusLine().getStatusCode() != 200) {
			String requestStatusLine = responseSP.getStatusLine() + " "
					+ responseSP.getStatusLine().getStatusCode();
			System.out.println( "HttpClient:" + responseSP.toString());
			throw new Exception(requestStatusLine + " ");
		} else {
			return responseSP.getEntity().getContent();
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
	
	public static final String stringToHtmlString(String s){
	       StringBuffer sb = new StringBuffer();
	       int n = s.length();
	       for (int i = 0; i < n; i++) {
	          char c = s.charAt(i);
	          switch (c) {
	             case '<': sb.append("&lt;"); break;
	             case '>': sb.append("&gt;"); break;
	             case '&': sb.append("&amp;"); break;
	             case '"': sb.append("&quot;"); break;
	             default:  sb.append(c); break;
	          }
	       }
	       return sb.toString();
	    }
	

}
