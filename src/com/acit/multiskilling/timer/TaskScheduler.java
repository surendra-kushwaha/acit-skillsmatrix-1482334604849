package com.acit.multiskilling.timer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.acit.multiskilling.dao.MultiSkillDao;
import com.acit.multiskilling.model.SkillsInfo;
import com.acit.multiskilling.model.SkillsMatrix;
import com.acit.multiskilling.util.Main;
import com.acit.multiskilling.util.Utility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
 
/**
 * Class to extend and implement custom "Cron Job" equivalent because with
 * bluemix apps, service deployer does have access to VM's cron capability
 * 
 * @author surendra
 * 
 */
public class TaskScheduler  extends TimerTask {
 
private static final Log LOG = LogFactory.getLog(TaskScheduler.class);
public String cron_name;
public String command;
public int interval;
public boolean isEnabled;
static String fedAUth = null;
private MultiSkillDao dao;

public TaskScheduler() {
    super();
    dao = new MultiSkillDao();
}

@Override
public void run() {
    LOG.info("Job started at:" + new Date());
    try{
    	 if (this.isEnabled) {
    	String username=Utility.getProperties("GenericUserName");
    	String tokenid=Utility.getProperties("GenericPassword");
    	runSharePoint();
    	 } else {
             this.cancel();
    	 }
		}catch(Exception e){
			System.out.println("Exception Occured"+e.getMessage());
	}
    LOG.info("Job completed at:" + new Date());
}


public String runSharePoint() {
    LOG.info("Job started at:" + new Date());
    String data=null;
    try{
    	 if (this.isEnabled) {
    	String username=Utility.getProperties("GenericUserName");
    	String tokenid=Utility.getProperties("GenericPassword");
		 //data=loadCache(username,tokenid);
    	String urlStr = "https://ts.accenture.com/sites/Accenture%20Innovation%20Center%20for%20IBM%20Technologies/_vti_bin//ListData.svc/ACITSkillsMatrix";
		String domain1 = "dir"; // May also be referred as realm
		String userName = "surendra.kushwaha@accenture.com";
		String password = "Dec@2016";		
		Main amian=new Main();
		String responseText = amian.getAuthenticatedResponse(urlStr, domain1, userName, password);
		JSONObject xmlJSONObj=null;
		xmlJSONObj = XML.toJSONObject(responseText);
		System.out.println("String sharedata + "+responseText);
		
		JsonArray contactListArray = new JsonArray();
		String sharePointURL=Utility.getProperties("activeAllianceURL");
		List<SkillsInfo> sharePointJsonData1 = convertToJSONList(sharePointURL, xmlJSONObj, contactListArray);
		System.out.println("sharePointJsonData1 ## "+sharePointJsonData1.toString());
		
		//Update DB with sharepoint data
		dao.updateSkill(sharePointJsonData1);
		
		
		
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

/*
 * Convert XML to JSON
 */
private static List<SkillsInfo> convertToJSONList(String sharepointURL,JSONObject xmlJSONObj,
		JsonArray contactListArray) throws JSONException, Exception, IOException {
	System.out.println("convert to json method::");
	String kxResponse;
	InputStream instream;
	InputStream inputStream;
	JsonObject allianceJson;
	JsonObject contactInArray;
	JSONArray jsonarray;
	List<SkillsInfo> skillList=new ArrayList<SkillsInfo>();
	//System.out.println("sharepointURL:"+sharepointURL);
	jsonarray = xmlJSONObj.getJSONObject("feed").getJSONArray("entry");
	//System.out.println("jsonarray:::"+jsonarray);
	// Iterate through entry to get alliance details
	for (int i = 0; i < jsonarray.length(); i++) {
		JSONObject jsonobject = jsonarray.getJSONObject(i);
		allianceJson = new JsonObject();
		JSONObject propertiesJson = jsonobject.getJSONObject("content").getJSONObject("m:properties");
		//System.out.println("propertiesJson::"+propertiesJson);
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
		
		String teamName=getTeamName(propertiesJson,sharepointURL);
		String expertSkills=getExpertSkills(propertiesJson,sharepointURL);
		String supSkills=getSupSkills(propertiesJson,sharepointURL);
		String mentorId=getMentorEntId(propertiesJson,sharepointURL);
		
		System.out.println("Team Name  "+teamName);
		System.out.println("Expert Skills "+expertSkills);
		System.out.println("Sup Skills  "+supSkills);
		System.out.println("Mentor ID  "+mentorId);
		System.out.println("EnterpriseID  "+allianceJson.get("EnterpriseId").toString());
		System.out.println("Country  "+allianceJson.get("CountryValue").toString());
		System.out.println("Certification Obtained  "+allianceJson.get("CertificationsObtained").toString());
		System.out.println("Certification Planned  "+allianceJson.get("CertificationsPlannedForTheYear").toString());
		
		System.out.println("allianceJson   "+allianceJson);
		
		SkillsInfo skillInfo=new SkillsInfo();
		skillInfo.setEnterpriseId(allianceJson.get("EnterpriseId").toString().replace("\"", ""));
		skillInfo.setTeamName(teamName);
		skillInfo.setExpertSkills(expertSkills);
		skillInfo.setSupSkills(supSkills);
		skillInfo.setMentorEntId(mentorId);
		skillInfo.setCountry(allianceJson.get("CountryValue").toString().replace("\"", ""));
		skillInfo.setCertificationObtained(allianceJson.get("CertificationsObtained").toString().replace("\"", ""));
		skillInfo.setCertificationPlanned(allianceJson.get("CertificationsPlannedForTheYear").toString().replace("\"", ""));
		/*
		Gson gson = new Gson();
		 
		System.out.println(
		    gson.fromJson(allianceJson, SkillsMatrix.class));
		SkillsMatrix skillInfo=gson.fromJson(allianceJson, SkillsMatrix.class);*/
		//contactInArray = new JsonObject();
		//contactInArray.add("properties", allianceJson);
		skillList.add(skillInfo);
	}
	return skillList;
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

public static String getTeamName(JSONObject propertiesJson,String sharepointUrl){
	System.out.println(" in side getTeam NAme");
	String teamName = "";
		try{
		int Id = 0;
		if (isValidJson(propertiesJson, "d:Id")) {
			Id = propertiesJson.getJSONObject("d:Id").getInt("content");
		}
		String teamUrl = sharepointUrl + "(" + Id + ")" + "/TeamName";		
		//String username=Utility.getProperties("GenericUserName");
		//String tokenid=Utility.getProperties("GenericPassword");
		String domain1 = "dir"; // May also be referred as realm
		String userName = "surendra.kushwaha@accenture.com";
		String password = "Dec@2016";		
		Main amian=new Main();
		String responseText = amian.getAuthenticatedResponse(teamUrl, domain1, userName, password);
		JSONObject xmlJSONObj=null;
		xmlJSONObj = XML.toJSONObject(responseText);
		System.out.println("String sharedata get Team Name+ "+responseText);		
		JSONObject jsonTEParray;
		jsonTEParray = xmlJSONObj.getJSONObject("entry");
		teamName=xmlJSONObj.getJSONObject("entry").getJSONObject("content")
					.getJSONObject("m:properties").getString("d:TeamName");
			
			System.out.println("Team Name Recieved::"+teamName);		
			
		}catch(Exception e){
		
	}
	return teamName;

}

public static String getExpertSkills(JSONObject propertiesJson,String sharepointUrl){
	System.out.println(" in side getTeam NAme");
	String expertSkills = "";
		try{
		int Id = 0;
		if (isValidJson(propertiesJson, "d:Id")) {
			Id = propertiesJson.getJSONObject("d:Id").getInt("content");
		}
		String teamUrl = sharepointUrl + "(" + Id + ")" + "/SkillsAsAnExpert";		
		//String username=Utility.getProperties("GenericUserName");
		//String tokenid=Utility.getProperties("GenericPassword");
		String domain1 = "dir"; // May also be referred as realm
		String userName = "surendra.kushwaha@accenture.com";
		String password = "Dec@2016";		
		Main amian=new Main();
		String responseText = amian.getAuthenticatedResponse(teamUrl, domain1, userName, password);
		JSONObject xmlJSONObj=null;
		xmlJSONObj = XML.toJSONObject(responseText);
		System.out.println("String sharedata get Team Name+ "+responseText);		
		/*JSONObject jsonTEParray;
		jsonTEParray = xmlJSONObj.getJSONObject("entry");
		teamName=xmlJSONObj.getJSONObject("entry").getJSONObject("content")
					.getJSONObject("m:properties").getString("d:TeamName");
			
			System.out.println("Team Name Recieved::"+teamName);*/
		
		//log.info("Get TEP Relationship lead name and email");
		JSONArray jsonTEParray;
		jsonTEParray = xmlJSONObj.getJSONObject("feed").optJSONArray("entry");
		//System.out.println("jsonTEParray@@"+jsonTEParray);
		
		if (jsonTEParray instanceof JSONArray) {
			for (int j = 0; j < jsonTEParray.length(); j++) {
				JSONObject jsonTEParray1 = jsonTEParray.optJSONObject(j);
				if (jsonTEParray1 != null) {
					JSONObject propertiesTEPJson = jsonTEParray1.getJSONObject("content")
							.getJSONObject("m:properties");
					expertSkills += propertiesTEPJson.getString("d:Value") + ";";
					//tepRelLeadEmail += propertiesTEPJson.getString("d:WorkEmail") + ";";
				}
			}
			//allianceJson.addProperty("TEPRSLeadName", tepRelLeadName);
			//allianceJson.addProperty("TEPRSLeadEmail", tepRelLeadEmail);
		} else {
			if (xmlJSONObj.getJSONObject("feed").optJSONObject("entry") != null) {
				/*allianceJson.addProperty("TEPRSLeadName",
						xmlJSONObj.getJSONObject("feed").getJSONObject("entry").getJSONObject("content")
								.getJSONObject("m:properties").getString("d:Name"));
				allianceJson.addProperty("TEPRSLeadEmail",
						xmlJSONObj.getJSONObject("feed").getJSONObject("entry").getJSONObject("content")
								.getJSONObject("m:properties").getString("d:WorkEmail"));*/
				expertSkills=xmlJSONObj.getJSONObject("feed").getJSONObject("entry").getJSONObject("content")
						.getJSONObject("m:properties").getString("d:Value");
			} else {
				//allianceJson.addProperty("TEPRSLeadName", "");
				//allianceJson.addProperty("TEPRSLeadEmail", "");
			}
		}
			
		}catch(Exception e){
		
	}
	return expertSkills;

}

public static String getSupSkills(JSONObject propertiesJson,String sharepointUrl){
	System.out.println(" in side getTeam NAme");
	String supSkills = "";
		try{
		int Id = 0;
		if (isValidJson(propertiesJson, "d:Id")) {
			Id = propertiesJson.getJSONObject("d:Id").getInt("content");
		}
		String teamUrl = sharepointUrl + "(" + Id + ")" + "/SupplimentrySkills";		
		String username=Utility.getProperties("GenericUserName");
		String tokenid=Utility.getProperties("GenericPassword");
		String domain1 = "dir"; // May also be referred as realm
		//String userName = "surendra.kushwaha@accenture.com";
		//String password = "Dec@2016";		
		Main amian=new Main();
		String responseText = amian.getAuthenticatedResponse(teamUrl, domain1, username, tokenid);
		JSONObject xmlJSONObj=null;
		xmlJSONObj = XML.toJSONObject(responseText);
		System.out.println("String sharedata get Team Name+ "+responseText);		
		/*JSONObject jsonTEParray;
		jsonTEParray = xmlJSONObj.getJSONObject("entry");
		teamName=xmlJSONObj.getJSONObject("entry").getJSONObject("content")
					.getJSONObject("m:properties").getString("d:TeamName");
			
			System.out.println("Team Name Recieved::"+teamName);*/
		
		//log.info("Get TEP Relationship lead name and email");
		JSONArray jsonTEParray;
		jsonTEParray = xmlJSONObj.getJSONObject("feed").optJSONArray("entry");
		//System.out.println("jsonTEParray@@"+jsonTEParray);
		
		if (jsonTEParray instanceof JSONArray) {
			for (int j = 0; j < jsonTEParray.length(); j++) {
				JSONObject jsonTEParray1 = jsonTEParray.optJSONObject(j);
				if (jsonTEParray1 != null) {
					JSONObject propertiesTEPJson = jsonTEParray1.getJSONObject("content")
							.getJSONObject("m:properties");
					supSkills += propertiesTEPJson.getString("d:Value") + ";";
					//tepRelLeadEmail += propertiesTEPJson.getString("d:WorkEmail") + ";";
				}
			}
			//allianceJson.addProperty("TEPRSLeadName", tepRelLeadName);
			//allianceJson.addProperty("TEPRSLeadEmail", tepRelLeadEmail);
		} else {
			if (xmlJSONObj.getJSONObject("feed").optJSONObject("entry") != null) {
				/*allianceJson.addProperty("TEPRSLeadName",
						xmlJSONObj.getJSONObject("feed").getJSONObject("entry").getJSONObject("content")
								.getJSONObject("m:properties").getString("d:Name"));
				allianceJson.addProperty("TEPRSLeadEmail",
						xmlJSONObj.getJSONObject("feed").getJSONObject("entry").getJSONObject("content")
								.getJSONObject("m:properties").getString("d:WorkEmail"));*/
				supSkills=xmlJSONObj.getJSONObject("feed").getJSONObject("entry").getJSONObject("content")
						.getJSONObject("m:properties").getString("d:Value");
			} else {
				//allianceJson.addProperty("TEPRSLeadName", "");
				//allianceJson.addProperty("TEPRSLeadEmail", "");
			}
		}
			
		}catch(Exception e){
		
	}
	return supSkills;

}

public static String getMentorEntId(JSONObject propertiesJson,String sharepointUrl){
	System.out.println(" in side getTeam NAme");
	String teamName = "";
		try{
		int Id = 0;
		if (isValidJson(propertiesJson, "d:Id")) {
			Id = propertiesJson.getJSONObject("d:Id").getInt("content");
		}
		String teamUrl = sharepointUrl + "(" + Id + ")" + "/TeamName";		
		String username=Utility.getProperties("GenericUserName");
		String tokenid=Utility.getProperties("GenericPassword");
		String domain1 = "dir"; // May also be referred as realm
		//String userName = "surendra.kushwaha@accenture.com";
		//String password = "Dec@2016";		
		Main amian=new Main();
		String responseText = amian.getAuthenticatedResponse(teamUrl, domain1, username, tokenid);
		JSONObject xmlJSONObj=null;
		xmlJSONObj = XML.toJSONObject(responseText);
		System.out.println("String sharedata get Team Name+ "+responseText);		
		JSONObject jsonTEParray;
		jsonTEParray = xmlJSONObj.getJSONObject("entry");
		teamName=xmlJSONObj.getJSONObject("entry").getJSONObject("content")
					.getJSONObject("m:properties").getString("d:WorkEmail");
			
			System.out.println("Team Name Recieved::"+teamName);		
			
		}catch(Exception e){
		
	}
	return teamName;

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
