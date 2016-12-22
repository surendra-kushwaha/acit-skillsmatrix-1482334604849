package com.acit.myalliance.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.TimerTask;

import org.apache.commons.io.IOUtils;
import org.apache.http.auth.NTCredentials;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.acit.myalliance.sharepoint.client.HttpClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ibm.websphere.objectgrid.ObjectMap;
/**
 * 
 * @author 
 */
// Create a class extends with TimerTask
public class ScheduledTask extends TimerTask {
	String sharePointURL = Utility.getProperties("activeAllianceURL");
	//log.info("Active Alliance service details");
	String jsonPrettyPrintString = null;
	InputStream inputStream = null;
	
	String kxResponse = null;
	String mapName = CacheUtil.dataServiceMapName;
	Date now; // to display current time
	ObjectMap map = null;
	String username;
	String password;
	
	public ScheduledTask(){
		
	}
	
	public ScheduledTask(String sharePointURL,ObjectMap map, String username, String password){
		this.sharePointURL=sharePointURL;		
		this.map=map;
		this.username=username;	
		this.password=password;	
	}
	public String getKxResponse() {
		return kxResponse;
	}

	public void setKxResponse(String kxResponse) {
		this.kxResponse = kxResponse;
	}
	// Add your task here
	public void run() {
		//now = new Date(); // initialize date
		//System.out.println("Time is :" + now); // Display current time
		try{
		//System.out.println("credential"+ntCredentials.toString()+ntCredentials.getPassword());	
		inputStream = AuthenticationUtil.getSharePointData(sharePointURL);
		kxResponse = new String(IOUtils.toString(inputStream, "UTF-8"));
		//setKxResponse(kxResponse);
		//System.out.println("Scheduler username"+ntCredentials.getUserName());
		//kxResponse = AuthenticationUtil.getSharePointData(username, password, sharePointURL);
		InputStream instream = IOUtils.toInputStream(kxResponse);
		try {
			JSONObject xmlJSONObj = XML.toJSONObject(IOUtils.toString(instream));
			JsonArray contactListArray = new JsonArray();
			inputStream = convertToJSON(sharePointURL, inputStream, xmlJSONObj, contactListArray,
					username,password);
			jsonPrettyPrintString = contactListArray.toString();

		} catch (JSONException je) {
			//log.error(je.toString());
		}

		if (CacheUtil.ogSession != null && CacheUtil.ogSession.getMap(mapName) != null) {
			map = CacheUtil.ogSession.getMap(mapName);
			int timeToLive = Integer.parseInt(Utility.getProperties("timeToLive"));
			//log.info("time to live: " + timeToLive);
			map.setTimeToLive(timeToLive);
			map.upsert("activeAlliance", jsonPrettyPrintString);
			CacheUtil.mapActiveValClear = false;
		}
		}catch(Exception e){
			
		}
	}
	/*
	 * Convert XML to JSON
	 */
	private InputStream convertToJSON(String sharepointURL, InputStream inputStream, JSONObject xmlJSONObj,
			JsonArray contactListArray, String username, String password) throws JSONException, Exception, IOException {
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
						String TEPRSUrl = sharepointURL + "(" + Id + ")" + "/TEPRelationshipLead";
						//inputStream = AuthenticationUtil.getSharePointData(username, password, sharePointURL);
						kxResponse = new String(IOUtils.toString(inputStream, "UTF-8"));
						//System.out.println("Scheduler username"+ntCredentials.getUserName());
						//kxResponse = AuthenticationUtil.getSharePointData(username, password, sharePointURL);
						instream = IOUtils.toInputStream(kxResponse);
						xmlJSONObj = XML.toJSONObject(IOUtils.toString(instream));

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
}
