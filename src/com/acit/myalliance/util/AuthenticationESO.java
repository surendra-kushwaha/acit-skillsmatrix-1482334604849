package com.acit.myalliance.util;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class AuthenticationESO {	
	private static final Log LOGGER = LogFactory.getLog(AuthenticationESO.class);
	public static void main(String[] a){
		try{
			boolean authFlag=authenticateUser("A06614.myalliance@accenture.com","uM3oI2xX5a9Ve0Lm47b1");
			//LOGGER.info(authFlag);
			System.out.println("Authentication:"+authFlag);
		}catch(Exception e){
			LOGGER.error(e.getMessage());
		}
	}
		
	public static boolean authenticateUser(String username,String password)throws Exception {
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
		boolean authFlag=false;
		try {
			HttpResponse samlResponse = postRequest(
					new URI("https://federation-sts.accenture.com/adfs/services/trust/2005/usernamemixed"),
					onlineCRMAuthSOAPEnvelope);
			String samlResponseVal = EntityUtils.toString(samlResponse.getEntity());
			System.out.println("SAML Token Response::: " + samlResponseVal);
			
			Pattern p = Pattern.compile("<t:RequestedSecurityToken>(.*?)</t:RequestedSecurityToken>");
			Matcher m = p.matcher(samlResponseVal);
			String codeGroup = null;
			if (m.find()) {
				// get the SAML token
				codeGroup = m.group(1);	
				authFlag=true;
				System.out.println("Authenticate Success");
				
			}else{
				//SAML Token not found
				authFlag=false;
				System.out.println("Authenticate Failed");
			}			
			//return samlResponse.getStatusLine().getStatusCode();
			return authFlag;

		}catch (Exception e) {
			// TODO Auto-generated catch block
			throw new Exception(e);
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

}
