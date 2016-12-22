package com.acit.myalliance.util;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.io.IOUtils;
import org.apache.http.auth.NTCredentials;

import com.ibm.websphere.objectgrid.ObjectMap;
/**
 * 
 * @author 
 */
// Create a class extends with TimerTask
public class ScheduledNameChangedTask extends TimerTask {
	//String sharePointURL = Utility.getProperties("activeAllianceURL");
	String sharePointURL = "https://ts.accenture.com/sites/AlliancesWizard/myAlliance/_vti_bin//ListData.svc/IndexOfAllianceNameChanges";
	InputStream inputStream = null;
	
	String kxResponse = null;
	String mapName = CacheUtil.dataServiceMapName;
	ObjectMap map = null;
	NTCredentials ntCredentials=null;
	String username;
	String password;
	String mapKey;
	
	public static void main(String a[]){
		try{
		Timer time = new Timer(); // Instantiate Timer Object
		System.out.println("main method");
		ScheduledNameChangedTask st = new ScheduledNameChangedTask("https://ts.accenture.com/sites/AlliancesWizard/myAlliance/_vti_bin//ListData.svc/IndexOfAllianceNameChanges",CacheUtil.ogSession.getMap(CacheUtil.dataServiceMapName)); // Instantiate SheduledTask class
		time.schedule(st, 0, 86400000);
		}catch(Exception e){
			
		}
	}
	
	public ScheduledNameChangedTask(String sharePointURL,ObjectMap map){
		this.sharePointURL=sharePointURL;		
		this.map=map;
	}
	
	// Add your task here
	public void run() {
		try{
			inputStream =AuthenticationUtil.getSharePointData(sharePointURL);
			kxResponse = new String(IOUtils.toString(inputStream, "UTF-8"));
			setKxResponse(kxResponse);
			//kxResponse = AuthenticationUtil.getSharePointData(username, password, sharePointURL);
			map = CacheUtil.ogSession.getMap(mapName);
			int timeToLive = Integer.parseInt(Utility.getProperties("timeToLive"));
			System.out.println(kxResponse);
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
