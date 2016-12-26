package wasdev.sample.servlet;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
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
import org.apache.http.client.utils.URIUtils;
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

public class HttpNameChangedAllianceClient {
	private static final Logger LOGGER = Logger.getLogger(HttpClient.class
			.getName());

	/*public static InputStream get(final String host, int port, final String scheme,
			final String path,final String query, Map<String, String> credentials)
			throws Exception {*/
	public static InputStream get()
			throws Exception {
		try {
			
			//final URI uri = URIUtils.createURI(scheme, host, port, path, query,null );
			String URL="https://ts.accenture.com/sites/AlliancesWizard/myAlliance/_vti_bin//ListData.svc/IndexOfAllianceNameChanges";//"https://ts.accenture.com/sites/AlliancesWizard/myAlliance/_vti_bin/ListData.svc";//"https://ts.accenture.com/sites/AlliancesWizard/_api/Web/Lists%28guid'edeaadb5-f131-4afe-bf04-c046186ba873'%29/items";
			HttpGet request = new HttpGet(URL);
			NTCredentials ntCredentials = new NTCredentials("acit.demo.ldap",
					"iA2aG1u0Vv34nM6o5",  "10.108.92.184","DIR");
			
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

			if (response.getStatusLine().getStatusCode() != 200) {
				String requestStatusLine = response.getStatusLine() + " "
						+ response.getStatusLine().getStatusCode();
				LOGGER.log(Level.WARNING, "HttpClient:" + response.toString());
				throw new Exception(requestStatusLine + " "
						+URL);
			} else {
				return response.getEntity().getContent();
			}

		} catch (Exception ex) {
			LOGGER.severe(ex.toString());
			;
			throw new Exception(ex);
		}
	}
	
	/*public void prepareAsyncConnection()  {
		  try {
		   
		    Protocol.registerProtocol("https",new Protocol("https",new EwsSSLProtocolSocketFactory(),443));
		    AuthPolicy.registerAuthScheme(AuthPolicy.NTLM,EwsJCIFSNTLMScheme.class);
		    client=new HttpClient(this.simpleHttpConnMng);
		    List authPrefs=new ArrayList();
		    authPrefs.add(AuthPolicy.NTLM);
		    authPrefs.add(AuthPolicy.BASIC);
		    authPrefs.add(AuthPolicy.DIGEST);
		    client.getParams().setParameter(AuthPolicy.AUTH_SCHEME_PRIORITY,authPrefs);
		    client.getState().setCredentials(AuthScope.ANY,
		    					new NTCredentials(getUserName(),getPassword(),"",
		    					getDomain()));
		    client.getHttpConnectionManager().getParams().setSoTimeout(getTimeout());
		    client.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
		    httpMethod=new GetMethod(getUrl().toString());
		    httpMethod.setFollowRedirects(isAllowAutoRedirect());
		    int status=client.executeMethod(httpMethod);
		  }
		 catch (  IOException e) {
		    client=null;
		    httpMethod=null;
		   
		  }
		}*/

}
