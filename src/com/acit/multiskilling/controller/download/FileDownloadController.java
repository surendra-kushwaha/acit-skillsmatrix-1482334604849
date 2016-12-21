package com.acit.multiskilling.controller.download;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acit.multiskilling.dao.MultiSkillDao;
import com.acit.multiskilling.model.SkillInfo;
 
@WebServlet("/downLoadFile")
@MultipartConfig(maxFileSize = 16177215)    // upload file's size up to 16MB
public class FileDownloadController extends HttpServlet {
       
    private MultiSkillDao dao;
    
    public FileDownloadController() {
        super();
        dao = new MultiSkillDao();
    }
    
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
         
        //InputStream inputStream = null;
    	SkillInfo pInfo=null;
        final int BUFFER_SIZE = 4096; 
        //response.setContentType("text/xml");
        String docType=request.getParameter("docType");
        String enterprizeId=request.getParameter("enterprizeId");
        String fileName=null;                                
        try {
        	System.out.println("EntID"+request.getParameter("enterprizeId"));
        	pInfo=dao.downloadDocument(docType,enterprizeId);
        	
        	if(docType.equalsIgnoreCase("doc")){
        		if(pInfo.getCertificateExtn().equals("docx"))
            		fileName=enterprizeId+".docx";
            	else
            		fileName=enterprizeId+".doc";
            	response.setContentType("application/msword");
            	//response.addHeader(
                //        "Content-Disposition","attachment; filename=CA00011013.doc");
            }else{
            	fileName=enterprizeId+".pdf";
            	 response.setContentType("application/pdf");
            	// response.addHeader(
                 //        "Content-Disposition","attachment; filename=CA00011013.pdf");
            }
        	
        	ServletContext context = getServletContext();
            // sets MIME type for the file download
            String mimeType = context.getMimeType(fileName);
            if (mimeType == null) {        
                mimeType = "application/octet-stream";
            }              
             
            // set content properties and header attributes for the response
            response.setContentType(mimeType);
            //response.setContentLength(fileLength);
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", fileName);
            response.setHeader(headerKey, headerValue);
            // writes the file to the client
            OutputStream outStream = response.getOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = pInfo.getCertificate().read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            pInfo.getCertificate().close();
            outStream.close(); 
        }  catch(Exception e) {           
            e.printStackTrace();
        }
    }
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	   doPost(request,response);
    }
}