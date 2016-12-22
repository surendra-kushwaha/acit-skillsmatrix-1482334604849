package com.acit.myalliance.util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.Date;

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
public class ScheduledSPTask extends TimerTask {
	//String sharePointURL = Utility.getProperties("activeAllianceURL");
	String sharePointURL = null;
	InputStream inputStream = null;
	
	String kxResponse = null;
	String mapName = CacheUtil.dataServiceMapName;
	ObjectMap map = null;
	NTCredentials ntCredentials=null;
	String username;
	String password;
	String mapKey;
		
	public ScheduledSPTask(String sharePointURL,ObjectMap map, String username, String password,String mapKey){
		this.sharePointURL=sharePointURL;		
		this.map=map;
		this.username=username;	
		this.password=password;
		this.mapKey=mapKey;
	}
	
	// Add your task here
	public void run() {
		try{
			inputStream =AuthenticationUtil.getSharePointData( sharePointURL);
			kxResponse = new String(IOUtils.toString(inputStream, "UTF-8"));
			setKxResponse(kxResponse);
			//kxResponse = AuthenticationUtil.getSharePointData(username, password, sharePointURL);
			map = CacheUtil.ogSession.getMap(mapName);
			int timeToLive = Integer.parseInt(Utility.getProperties("timeToLive"));
			//map.setTimeToLive(timeToLive);
			//map.upsert("expiredAlliance", kxResponse);
			synchronized(map){
				map.setTimeToLive(timeToLive);
				map.upsert(mapKey, kxResponse);
			}
			CacheUtil.mapExpValClear = false;
		}catch(Exception e){
			System.out.println(e);
		}
	}

	public String getKxResponse() {
		return kxResponse;
	}

	public void setKxResponse(String kxResponse) {
		this.kxResponse = kxResponse;
	}
	
}
