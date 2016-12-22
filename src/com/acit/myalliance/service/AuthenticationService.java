package com.acit.myalliance.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import com.acit.myalliance.sharepoint.client.HttpClient;
import com.acit.myalliance.util.AuthenticationUtil;
import com.acit.myalliance.util.Utility;




/**
 * Servlet implementation class ExpiredAllianceService
 */
@WebServlet("/AuthenticationService")
public class AuthenticationService extends HttpServlet {
	final static long serialVersionUID = 1L;
	static{
    	BasicConfigurator.configure();
    }
    static Logger log = Logger.getLogger(AuthenticationService.class.getName());
   @Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    InputStream Io=null;
	    OutputStream out=null;
		try {
			log.info("Authenticate User");
			
			response.setContentType("application/json");
			response.addHeader("Access-Control-Allow-Origin","*");
			response.addHeader("Access-Control-Allow-Methods","GET,POST");
			response.addHeader("Access-Control-Allow-Headers","Origin,X-Requested-With,Content-Type,Accept");
				
			
			//log.info("Expired alliance Details");
			String serviceUser = Utility.getProperties("NtUserName");
			String servicePwd = Utility.getProperties("NtPassword");
			String workStation = Utility.getProperties("NtWorkStation");
			String domain = Utility.getProperties("NtDomain");
			log.info("Authenticate User usrename"+serviceUser);
			//username=3ju5HiWoAERMfjByOjZCtj2pU7XlC87IlqTRNyS1xBg%3D&tokenid=zJqhOBFwwkCIUsTpg6BQawdLUfXUnsoQjKkMJNfrtbo%3D
			//String uname=Utility.decrypt("xxIzahJDpHvaL4rW9AGRj%2Bw%2Fg0KHYChH%2Fib8gwy%2BO1ZRJbGH3dV%2B%2B4BdTG5%2FzhKVxKZuiwGXe3JYjVM3MmaDJw%3D%3D");
			//String pwd=Utility.decrypt("qwCdU7NgJCbzdgPMmg5jooLT4tzAScVv2M%2F9YLei6jQ%3D");
			//System.out.println("uname###:"+uname);
			//System.out.println("pwd###:"+pwd);
			
			//String uname1=Utility.decrypt("3ju5HiWoAERMfjByOjZCtj2pU7XlC87IlqTRNyS1xBg%3D");
			//String pwd1=Utility.decrypt("zJqhOBFwwkCIUsTpg6BQawdLUfXUnsoQjKkMJNfrtbo%3D");
			//System.out.println("uname###$$:"+uname1);
			//System.out.println("pwd###$$:"+pwd1);
			//inputStream = HttpClient.get(sharePointURL, ntCredentials);
			
			System.out.println(System.getenv("GENERIC_USERID"));
			System.out.println(System.getenv("PASSWORD"));
			
			Map env = System.getenv();
			String genericid = (String) env.get("GENERIC_USERID");
			String genericpwd= (String) env.get("PASSWORD");
			
			
			System.out.println("user defined env data::"+genericid);
			System.out.println("user defined env data::"+genericpwd);
			
			int responseCode = AuthenticationUtil.authenticateUser(serviceUser, servicePwd);
			System.out.println("response code"+responseCode);
			if(responseCode==200){
				response.getWriter().append("User authenticated");
			}else{
				response.getWriter().append("User does not authenticated");
			}
			//out.write("user authenticated");
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}finally
		{
			try{
				out.close();
				Io.close();
			}catch(Exception e){
				log.error(e.getMessage());
			}
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
}
