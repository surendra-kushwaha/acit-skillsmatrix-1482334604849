package com.acit.myalliance.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

public class Main {

	public static void main(String[] args) throws Exception {

		String urlStr = "https://ts.accenture.com/sites/Accenture%20Innovation%20Center%20for%20IBM%20Technologies/_vti_bin//ListData.svc/ACITSkillsMatrix";
		String domain = "DIR"; // May also be referred as realm
		String userName = "surendra.kushwaha@accenture.com";
		String password = "Dec@2016";		

		String responseText = getAuthenticatedResponse(urlStr, domain, userName, password);

	    System.out.println("response: " + responseText);
	}

	private static String getAuthenticatedResponse(final String urlStr, final String domain, final String userName, final String password) throws IOException {

	    StringBuilder response = new StringBuilder();

		Authenticator.setDefault(new Authenticator() {
	        @Override
	        public PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(domain + "\\" + userName, password.toCharArray());
	        }
	    });

	    URL urlRequest = new URL(urlStr);
	    HttpURLConnection conn = (HttpURLConnection) urlRequest.openConnection();
	    conn.setDoOutput(true);
	    conn.setDoInput(true);
	    conn.setRequestMethod("GET");

	    InputStream stream = conn.getInputStream();
	    BufferedReader in = new BufferedReader(new InputStreamReader(stream));
	    String str = "";
	    while ((str = in.readLine()) != null) {
	        response.append(str);
	    }
	    in.close();		

	    return response.toString();
	}

}
