package com.acit.multiskilling.controller.login;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/LogoutController")  
public class LogoutController extends HttpServlet { 
	private static String LOGIN_PAGE = "/login.jsp";
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       doPost(request,response);
    }
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	session.removeAttribute("userName");
    	String userName = (String) session.getAttribute("userName");
    	if (null == userName) {
    	   request.setAttribute("LoginError", "Session has expired.  Please login.");
    	   RequestDispatcher rd = request.getRequestDispatcher(LOGIN_PAGE);
    	   rd.forward(request, response);
    	}
    	session.invalidate();
    }
}
