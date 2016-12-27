package com.acit.multiskilling.controller.search;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.acit.multiskilling.dao.MultiSkillDao;
import com.acit.multiskilling.model.SkillInfo;
import com.acit.multiskilling.util.Main;
import com.acit.multiskilling.util.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@WebServlet("/SearchSkillController")  
public class SearchSkillController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static String LIST_USER = "/multiSkillInfo.jsp";
    //private static String LIST_USER = "/policyInfo.jsp";
    private MultiSkillDao dao;
      
    public SearchSkillController() {
        super();
        dao = new MultiSkillDao();
    }
  
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       doPost(request,response);
    } 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//SkillInfo policyInfo = new SkillInfo();
    	try{
    	String enterprizeId=request.getParameter("enterprizeId");
    	String clreadFlag=request.getParameter("ExpertSkill");
    	String skillRole=request.getParameter("SupSkill");
    	String location=request.getParameter("workLocation");
    	SkillInfo skillInfo=new SkillInfo();
    	skillInfo.setEnterprizeId(enterprizeId);
    	skillInfo.setClear(clreadFlag);
    	skillInfo.setSkillRole(skillRole);
    	skillInfo.setWorkLocation(location);
    	
    	System.out.println("Controller entID"+enterprizeId);
    	System.out.println("Controller clear"+clreadFlag);
    	System.out.println("Controller skill role"+skillRole);
    	System.out.println("Controller work location"+location);
    	
    	
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
		//String sharePointJsonData = convertToJSON(sharePointURL, xmlJSONObj, contactListArray);
		
		List sharePointJsonData1 = convertToJSONList(sharePointURL, xmlJSONObj, contactListArray);
		System.out.println("sharePointJsonData1 ## "+sharePointJsonData1);
		
		/*Type listType = new TypeToken<List<String>>() {}.getType();
		List<String> yourList = new Gson().fromJson(sharePointJsonData, listType);
    	System.out.println("List from JSON  "+yourList);*/
       // List<SkillInfo> policyList=dao.getFormDataBySearch(skillInfo);
    	SkillInfo sInfo=new SkillInfo();
    	sInfo.setEnterprizeId("surendra.kushwaha");
    	sInfo.setEmployeeName("surendra kushwaha");
    	
    	SkillInfo sInfo1=new SkillInfo();
    	sInfo1.setEnterprizeId("sridhar.kunchala");
    	sInfo1.setEmployeeName("sridhar kunchala");
    	 List<SkillInfo> policyList=new ArrayList<SkillInfo>();
    	 policyList.add(sInfo);
    	 policyList.add(sInfo1);
        RequestDispatcher view = request.getRequestDispatcher(LIST_USER);
        request.setAttribute("skillList", policyList);
        view.forward(request, response);
    	}catch(Exception e){
    		
    	}
    }
    
    /*
     * Convert XML to JSON
     */
    private static String convertToJSON(String sharepointURL,JSONObject xmlJSONObj,
    		JsonArray contactListArray) throws JSONException, Exception, IOException {
    	System.out.println("convert to json method::");
    	String kxResponse;
    	InputStream instream;
    	InputStream inputStream;
    	JsonObject allianceJson;
    	JsonObject contactInArray;
    	JSONArray jsonarray;
    	//System.out.println("sharepointURL:"+sharepointURL);
    	jsonarray = xmlJSONObj.getJSONObject("feed").getJSONArray("entry");
    	//System.out.println("jsonarray:::"+jsonarray);
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
    	return contactListArray.toString();
    }
    
    /*
     * Convert XML to JSON
     */
    private static List<SkillInfo> convertToJSONList(String sharepointURL,JSONObject xmlJSONObj,
    		JsonArray contactListArray) throws JSONException, Exception, IOException {
    	System.out.println("convert to json method::");
    	String kxResponse;
    	InputStream instream;
    	InputStream inputStream;
    	JsonObject allianceJson;
    	JsonObject contactInArray;
    	JSONArray jsonarray;
    	List<SkillInfo> skillList=new ArrayList<SkillInfo>();
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
    		System.out.println("allianceJson   "+allianceJson);
    		
    		Gson gson = new Gson();
    		 
    		System.out.println(
    		    gson.fromJson(allianceJson, SkillInfo.class));
    		SkillInfo skillInfo=gson.fromJson(allianceJson, SkillInfo.class);
    		//contactInArray = new JsonObject();
    		//contactInArray.add("properties", allianceJson);
    		skillList.add(skillInfo);
    	}
    	return skillList;
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
