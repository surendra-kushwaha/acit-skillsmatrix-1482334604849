package com.acit.multiskilling.controller.addskill;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.acit.multiskilling.dao.MultiSkillDao;
import com.acit.multiskilling.model.SkillInfo;
import com.acit.multiskilling.exception.MultiSkillException;
 
@WebServlet("/uploadCertificate")
@MultipartConfig(maxFileSize = 16177215)    // upload file's size up to 16MB
public class AddSkillController extends HttpServlet {
       
    private MultiSkillDao dao;
    
    public AddSkillController() {
        super();
        dao = new MultiSkillDao();
    }
    
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
         
        InputStream inputStreamDoc = null;
        InputStream inputStreamPdf = null;
        final String LIST_DATA = "/multiSkillInfo.jsp"; 
        final String SKILL_UPLOAD = "/skillAddForm.jsp";
       final String RESOURCE_SKILL="/resourceSkillInfo.jsp";
        Part docPart = request.getPart("docFile");
        Part pdfPart = request.getPart("pdfFile");
        String docExtention="";
        if (docPart != null) {
        	docExtention=getFileExtention(docPart);
            inputStreamDoc = docPart.getInputStream();
        }
        
        if (pdfPart != null) {
            inputStreamPdf = pdfPart.getInputStream();
        }
        inputStreamPdf = pdfPart.getInputStream();
        SkillInfo skillInfo=new SkillInfo();        
        try {
        	       	       	
        	
        	String enterprizeId=request.getParameter("enterprizeId");
        	skillInfo.setEnterprizeId(enterprizeId);
			skillInfo.setEmployeeName(request.getParameter("employeeName"));
			skillInfo.setSkillRole(request.getParameter("skillRole"));
			skillInfo.setScore(request.getParameter("score"));
			skillInfo.setCertificateName(request.getParameter("certificateName"));
			
			skillInfo.setWorkLocation(request.getParameter("workLocation"));
			skillInfo.setCertDate(request.getParameter("datepicker"));
			if(Integer.parseInt(request.getParameter("score"))>=66)
				skillInfo.setClear("Yes");
			else
				skillInfo.setClear("No");
			
			skillInfo.setSection1Score(request.getParameter("Score1"));
			skillInfo.setSection2Score(request.getParameter("Score2"));
			skillInfo.setSection3Score(request.getParameter("Score3"));
			skillInfo.setSection4Score(request.getParameter("Score4"));
			skillInfo.setSection5Score(request.getParameter("Score5"));
			skillInfo.setSection6Score(request.getParameter("Score6"));
        	
			skillInfo.setCertificateExtn(docExtention);
			skillInfo.setCertificate(inputStreamPdf);
			
			String employeeRole="";
			if(request.getParameter("employeeRole")!=null)
			employeeRole=request.getParameter("employeeRole");
			System.out.println("Employee Role"+employeeRole);
            //System.out.println("Controller addskillInfo skillRole"+request.getParameter("skillRole"));
            //System.out.println("Controller addskillInfo score"+request.getParameter("score"));
            //System.out.println("Controller addskillInfo certificateName"+request.getParameter("certificateName"));
			
            String forward="";
            try{
                dao.updateSkill(skillInfo);
                request.setAttribute("addFlag","addSuccess");                
        		forward=RESOURCE_SKILL;
        		if(employeeRole.equalsIgnoreCase("admin")){
        			forward=LIST_DATA;
        			//request.setAttribute("skillList", dao.getFormDataByEntId(enterprizeId));
        		}/*else{
        			forward=RESOURCE_SKILL;
        			request.setAttribute("skillList", dao.getFormDataByEntId(enterprizeId));
        		}*/
        		//request.setAttribute("uploadFlag", "YES");
        		//System.out.println("Add Controller ent ID"+enterprizeId);
        		request.setAttribute("skillList", dao.getFormDataByEntId(enterprizeId));
            }catch(MultiSkillException e){
            	forward=SKILL_UPLOAD;
            	String errorMessage=null;
            	if(e.getErrorCode()==-803){
            		//System.out.println("Controller SQL ERROR CODE"+e.getErrorCode());
            		errorMessage=getProperties("SQL_ERROR001");
            		request.setAttribute("addFlag", errorMessage);  
            		//request.setAttribute("addFlag", "addFailure"); 
            	}else{
            		//forward=BULK_UPLOAD;
            		errorMessage=getProperties("SQL_ERROR003");
            		request.setAttribute("addFlag", errorMessage);
            		//request.setAttribute("addFlag", "SystemError");
            	}
            	//String validationError=getProperties(e.getErrorCode());
            	//System.out.println("Error code"+getProperties(e.getErrorCode()));
            	//System.out.println("Error message"+getProperties(e.getErrorCode()));
            	//request.setAttribute("addFlag", validationError);
            }
        	
                    	       	
            RequestDispatcher view = request.getRequestDispatcher(forward);
            view.forward(request, response);
        	
        }  catch(Exception e) { 
        	e.printStackTrace();
        }
    }
    
    
    private String getFileExtention(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        String fileName="";
        //LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        for (String content : partHeader.split(";")) {
            if (content.trim().startsWith("filename")) {
            	fileName= content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }
    
    public String getProperties(String key){
    	ResourceBundle bundle = ResourceBundle.getBundle("resources.messages", Locale.US);
    	System.out.println("Key:"+bundle.getString(key));
    	return bundle.getString(key);		
    }
}