package com.acit.myalliance.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.auth.NTCredentials;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.json.JSONArray;
//import org.apache.wink.json4j.JSONArray;
//import org.apache.wink.json4j.JSONObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.acit.myalliance.sharepoint.client.HttpClient;
import com.acit.myalliance.util.AuthenticationUtil;
import com.acit.myalliance.util.CacheUtil;
import com.acit.myalliance.util.Utility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ibm.websphere.objectgrid.ObjectMap;

@WebServlet("/AllianceService")
public class ActiveAllianceService extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4434772083274536386L;
	// long serialVersionUID = 1L;
	private String concurentThreadError = "More than single thread trying to use same Session concurrently";
	static {
		BasicConfigurator.configure();
	}
	static Logger log = Logger.getLogger(ActiveAllianceService.class.getName());

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sharePointURL = Utility.getProperties("activeAllianceURL");
		log.info("Active Alliance service details");
		String jsonPrettyPrintString = null;
		InputStream inputStream = null;
		OutputStream out = null;
		String username = null;
		String password = null;
		String endpoint = null;
		String gridName = null;
		String kxResponse = null;
		String mapName = CacheUtil.dataServiceMapName;

		try {
			ObjectMap map = null;
			Object value = null;

			if (CacheUtil.ogSession != null) {
				map = CacheUtil.ogSession.getMap(mapName);
				try {
					synchronized(map){
						value = map.get("activeAlliance");
					}
										
				} catch (Exception e) {
					log.error("Exception Message: "+e.getMessage());					
				}
				
				if (value == null) {
					log.warn("Active alliance details not available in cache.");
					CacheUtil.mapActiveValClear = true;
				}
			}

			if (CacheUtil.ogSession == null || CacheUtil.mapActiveValClear) {

				Map env = System.getenv();
				String vcap = (String) env.get("VCAP_SERVICES");

				if (vcap == null) {
					log.info("No VCAP_SERVICES found");
				} else {
					try {
						if (CacheUtil.ivObjectGrid == null) {
							JSONObject obj = new JSONObject(vcap);
							String[] names = JSONObject.getNames(obj);
							if (names != null) {
								for (String name : names) {
									if (name.startsWith("DataCache")) {
										JSONArray val = obj.getJSONArray(name);
										JSONObject serviceAttr = val.getJSONObject(0);
										JSONObject credentials = serviceAttr.getJSONObject("credentials");
										username = credentials.getString("username");
										password = credentials.getString("password");
										endpoint = credentials.getString("catalogEndPoint");
										gridName = credentials.getString("gridName");
										mapName = CacheUtil.dataServiceMapName;
										break;
									}
								}
							}
							CacheUtil.connectToGrid(username, password, endpoint, gridName);
						}
						String serviceUser = Utility.getProperties("NtUserName");
						String servicePwd = Utility.getProperties("NtPassword");
						String workStation = Utility.getProperties("NtWorkStation");
						String domain = Utility.getProperties("NtDomain");

						NTCredentials ntCredentials = new NTCredentials(serviceUser, servicePwd, workStation, domain);

						//inputStream = HttpClient.get(sharePointURL, ntCredentials);
						inputStream = AuthenticationUtil.getSharePointData(sharePointURL);
						kxResponse = new String(IOUtils.toString(inputStream, "UTF-8"));
						InputStream instream = IOUtils.toInputStream(kxResponse);
						try {
							JSONObject xmlJSONObj = XML.toJSONObject(IOUtils.toString(instream));
							JsonArray contactListArray = new JsonArray();
							inputStream = convertToJSON(sharePointURL, inputStream, xmlJSONObj, contactListArray,
									serviceUser, servicePwd);
							jsonPrettyPrintString = contactListArray.toString();

						} catch (JSONException je) {
							log.error(je.toString());
							je.printStackTrace();
						}

						if (CacheUtil.ogSession != null && CacheUtil.ogSession.getMap(mapName) != null) {
							map = CacheUtil.ogSession.getMap(mapName);
							int timeToLive = Integer.parseInt(Utility.getProperties("timeToLive"));
							log.info("time to live: " + timeToLive);

							map.setTimeToLive(timeToLive);
							map.upsert("activeAlliance", jsonPrettyPrintString);

							CacheUtil.mapActiveValClear = false;
						}

					} catch (Exception e) {
						log.error(e.toString());
						e.printStackTrace();

					}
				}

			} else {

				// value = map.get("activeAlliance");
				log.info("Returning the details from Cache.");
				if (value != null) {
					jsonPrettyPrintString = value.toString();
				}
			}

			response.setContentType("application/json");
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.addHeader("Access-Control-Allow-Methods", "GET,POST");
			response.addHeader("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept");
			out = response.getOutputStream();
			InputStream instream = IOUtils.toInputStream(jsonPrettyPrintString);
			IOUtils.copyLarge(instream, out);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/*
	 * Convert XML to JSON
	 */
	private InputStream convertToJSON(String sharepointURL, InputStream inputStream, JSONObject xmlJSONObj,
			JsonArray contactListArray, String serviceUser, String servicePwd) throws JSONException, Exception, IOException {
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
							//System.out.println("d:Id@@"+Id);
						}
						//System.out.println("d:Id###"+Id);
						// TEP Start
						//String TEPRSUrl = sharepointURL + "(" + Id + ")" + "/TEPRelationshipLead";
						//String TEPRSUrl = sharepointURL + "(6)" + "/TEPRelationshipLead";
						//inputStream = HttpClient.get(TEPRSUrl, ntCredentials);
						inputStream = AuthenticationUtil.getSharePointTEPData(Id);
						
						//System.out.println("TEPRS ID@@"+Id);
						//System.out.println("serviceUser@@"+serviceUser);
						//System.out.println("servicePwd@@"+servicePwd);
						
						kxResponse = new String(IOUtils.toString(inputStream, "UTF-8"));
						instream = IOUtils.toInputStream(kxResponse);
						xmlJSONObj = XML.toJSONObject(IOUtils.toString(instream));

						log.info("Get TEP Relationship lead name and email");
						JSONArray jsonTEParray;
						jsonTEParray = xmlJSONObj.getJSONObject("feed").optJSONArray("entry");
						//System.out.println("jsonTEParray@@"+jsonTEParray);
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
