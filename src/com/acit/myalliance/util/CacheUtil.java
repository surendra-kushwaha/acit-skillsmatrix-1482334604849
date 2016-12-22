package com.acit.myalliance.util;

import org.apache.log4j.Logger;

import com.acit.myalliance.service.NameChangedAllianceService;
import com.ibm.websphere.objectgrid.ClientClusterContext;
import com.ibm.websphere.objectgrid.ObjectGrid;
import com.ibm.websphere.objectgrid.ObjectGridManager;
import com.ibm.websphere.objectgrid.ObjectGridManagerFactory;
import com.ibm.websphere.objectgrid.Session;
import com.ibm.websphere.objectgrid.security.config.ClientSecurityConfiguration;
import com.ibm.websphere.objectgrid.security.config.ClientSecurityConfigurationFactory;
import com.ibm.websphere.objectgrid.security.plugins.builtins.UserPasswordCredentialGenerator;

public class CacheUtil {

	static Logger log = Logger.getLogger(CacheUtil.class.getName());
	public static Session ogSession;
	public static ObjectGrid ivObjectGrid;
	public static String mapName = null;
	public static boolean mapActiveValClear = true;
	public static boolean mapExpValClear = true;
	public static boolean mapRenameValClear = true;
	public static boolean connectedGrid = false;
	public final static String dataServiceMapName = "myalliance.LUT.P";

	public static void connectToGrid(String username, String password, String endpoint, String gridName) {
		try {
			// String mapType="mymap.LUT.P";
			ObjectGridManager ogm = ObjectGridManagerFactory.getObjectGridManager();
			ClientSecurityConfiguration csc = null;
			csc = ClientSecurityConfigurationFactory.getClientSecurityConfiguration();
			csc.setCredentialGenerator(new UserPasswordCredentialGenerator(username, password));
			csc.setSecurityEnabled(true);
			//
			// ogm.getObjectGrids();
			ClientClusterContext ccc = null;
			try {
				ccc = ogm.connect(endpoint, csc, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				connectedGrid = true;
				log.warn("grid is already connected!!");
			}
			ObjectGrid clientGrid = ogm.getObjectGrid(ccc, gridName);
			ogSession = clientGrid.getSession();			
			ivObjectGrid = clientGrid;

		} catch (Exception e) {
			log.error("Failed to connect to grid!" + e.getMessage());
		}
	}

}
