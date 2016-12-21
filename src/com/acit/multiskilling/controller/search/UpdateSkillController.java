package com.acit.multiskilling.controller.search;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acit.multiskilling.dao.MultiSkillDao;
import com.acit.multiskilling.model.SkillInfo;

@WebServlet("/UpdateSkillController")  
public class UpdateSkillController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static String LIST_USER = "/skillUpdateForm.jsp";
    private MultiSkillDao dao;
      
    public UpdateSkillController() {
        super();
        dao = new MultiSkillDao();
    }
  
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       doPost(request,response);
    } 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//SkillInfo policyInfo = new SkillInfo();
    	String enterprizeId=request.getParameter("enterprizeId");
    	//String clreadFlag=request.getParameter("clear");
    	//String skillRole=request.getParameter("skillRole");
    	//String location=request.getParameter("workLocation");
    	SkillInfo skillInfo=new SkillInfo();
    	skillInfo.setEnterprizeId(enterprizeId);
    	//skillInfo.setClear(clreadFlag);
    	//skillInfo.setSkillRole(skillRole);
    	//skillInfo.setWorkLocation(location);
    	
    	System.out.println("Controller entID"+enterprizeId);
    	//System.out.println("Controller clear"+clreadFlag);
    	//System.out.println("Controller skill role"+skillRole);
    	//System.out.println("Controller work location"+location);
    	
        List<SkillInfo> policyList=dao.getFormDataBySearch(skillInfo);
        RequestDispatcher view = request.getRequestDispatcher(LIST_USER);
        request.setAttribute("skillList", policyList);
        view.forward(request, response);
    }
}
