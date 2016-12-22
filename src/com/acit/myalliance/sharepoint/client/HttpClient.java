package com.acit.myalliance.sharepoint.client;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.DefaultCookieSpecProvider;
import org.apache.http.impl.cookie.RFC6265CookieSpecProvider;
import org.apache.http.message.BasicHeader;

import com.acit.myalliance.util.Utility;

public class HttpClient {
	private static final Logger LOGGER = Logger.getLogger(HttpClient.class
			.getName());
	
	
	public static InputStream get(String url, NTCredentials ntCredentials)
			throws Exception {
		try {
			HttpGet request = new HttpGet(url);
			
			final CredentialsProvider cprovider = new BasicCredentialsProvider();
			cprovider.setCredentials(AuthScope.ANY, ntCredentials);

			PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader
					.getDefault();
			final Registry<CookieSpecProvider> registry = RegistryBuilder
					.<CookieSpecProvider> create()
					.register(CookieSpecs.DEFAULT,
							new DefaultCookieSpecProvider(publicSuffixMatcher))
					.register(CookieSpecs.STANDARD,
							new RFC6265CookieSpecProvider(publicSuffixMatcher))
					.build();

			final RequestConfig requestConfig = RequestConfig.custom()
					.setCookieSpec("easy").setAuthenticationEnabled(true).setRelativeRedirectsAllowed(true).build();
			
			Collection<Header> defaultHeaders = new ArrayList<Header>();
			final Header header = new BasicHeader (HttpHeaders.CONTENT_TYPE, "text/xml");
			Header header1 = new BasicHeader ("Keep-Alive","300");
			Header header2 = new BasicHeader ("Connection","Keep-Alive");
			defaultHeaders.add(header);
			defaultHeaders.add(header1);
			defaultHeaders.add(header2);
			
			final Registry<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder
					.<AuthSchemeProvider> create()
					.register(AuthSchemes.NTLM, new JCIFSNTLMSchemeFactory())
					.register(AuthSchemes.BASIC, new BasicSchemeFactory())
					.register(AuthSchemes.DIGEST, new DigestSchemeFactory())
					.register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory())
					.register(AuthSchemes.KERBEROS, new KerberosSchemeFactory())
					.build();
			final CloseableHttpClient client = HttpClients.custom().setDefaultHeaders(defaultHeaders)
					.setDefaultAuthSchemeRegistry(authSchemeRegistry)
					.setDefaultCredentialsProvider(cprovider)
					.setDefaultCookieSpecRegistry(registry)
					.setDefaultRequestConfig(requestConfig).build();
			
			final HttpResponse response = client.execute(request);
			//response.setContentType("application/json");
			response.addHeader("Access-Control-Allow-Origin","*");
			response.addHeader("Access-Control-Allow-Methods","GET,POST");
			response.addHeader("Access-Control-Allow-Headers","Origin,X-Requested-With,Content-Type,Accept");
			if (response.getStatusLine().getStatusCode() != 200) {
				String requestStatusLine = response.getStatusLine() + " "
						+ response.getStatusLine().getStatusCode();
				LOGGER.log(Level.WARNING, "HttpClient:" + response.toString());
				throw new Exception(requestStatusLine + " "
						+url);
			} else {
				return response.getEntity().getContent();
			}

		} catch (Exception ex) {
			LOGGER.severe(ex.toString());
			throw new Exception(ex);
		}
	}		
}
