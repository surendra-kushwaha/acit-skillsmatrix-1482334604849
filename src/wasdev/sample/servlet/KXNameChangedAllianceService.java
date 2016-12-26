package wasdev.sample.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;



/**
 * Servlet implementation class KXService
 */
@WebServlet("/NameChangedAllianceService")
public class KXNameChangedAllianceService extends HttpServlet {
	long serialVersionUID = 1L;
	String protcol = "https";
	int portNumber=443;
	String domain="ts.accenture.com";
	String pathEvents="/sites/Accenture%20Innovation%20Center%20for%20IBM%20Technologies/ACIT%20Microsite/_api/web/lists/getbytitle%28%27upcoming%20events%27%29/items%28%29";
	
	String pathAnnouncements="/sites/Accenture%20Innovation%20Center%20for%20IBM%20Technologies/ACIT%20Microsite/_vti_bin/ListData.svc/ACITAnnouncements";
	String queryEvents="$top=10&$orderby=EventDate%20desc";
	String queryAnnouncements="$top=10&$orderby=Modified%20desc";
	String sharePointURL="https://ts.accenture.com/sites/Accenture%20Innovation%20Center%20for%20IBM%20Technologies/_vti_bin//ListData.svc/ACITSkillsMatrix";   
   @Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	    InputStream Io=null;
	    OutputStream out=null;
	    String flag = request.getParameter("event");
		try {
			
			Io = HttpClient.get(sharePointURL);
			response.setContentType("application/json");
			response.addHeader("Access-Control-Allow-Origin","*");
			response.addHeader("Access-Control-Allow-Methods","GET,POST");
			response.addHeader("Access-Control-Allow-Headers","Origin,X-Requested-With,Content-Type,Accept");
			out = response.getOutputStream();
			Runtime runtime = Runtime.getRuntime();
			String kxresponse = new String(IOUtils.toString(Io,"UTF-8"));
			InputStream instream = IOUtils.toInputStream(kxresponse);
			IOUtils.copyLarge(instream, out);
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			try{
				out.close();
				Io.close();
			}catch(Exception e){
				
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
