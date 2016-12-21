<%@page import="com.ibm.json.java.*,java.util.Collection, java.util.Iterator, java.math.BigDecimal"%>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.*"%>
        <%@ page import="com.acit.multiskilling.model.SkillInfo"%>
            <!DOCTYPE html>
            <%					 
					 String vcap = System.getenv("VCAP_APPLICATION");	
					 String instance_id = "unknown";
					 String port = "unknown";

					  if (vcap == null) {
					   System.out.println("No VCAP_SERVICES found");
					  }    
					  else {
						try {
							JSONObject obj = (JSONObject)JSON.parse(vcap);
							//System.out.println(obj.toString());
							port = ((Long)obj.get("port")).toString();
							instance_id = obj.get("instance_id").toString();

					      } catch (Exception e) {
					    	  e.printStackTrace();
					    	  
					   }
					 }		
					
					%>
            <% 
            String err="";
            String updateSucc = (String)request.getAttribute("updateFlag") == null ? "" : (String)request.getAttribute("updateFlag");
			
            String userName=null;
            
            if(request.getSession().getAttribute("userName")!=null){
            	userName =request.getSession().getAttribute("userName").toString();
            }else{
            	response.sendRedirect("LogoutController");
            }
            String uploadFlag="";
            if(request.getAttribute("uploadFlag")!=null){
            	uploadFlag=(String)request.getAttribute("uploadFlag");
            	//request.getRequestDispatcher("LogoutController").forward(request,response);
            }
			%>
                <html>
                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                    <title>Skill Details</title>
                    <link href="css/jquery.dataTables.min.css" rel="stylesheet">
                    <link href="css/dataTables.tableTools.css" rel="stylesheet">
                    <link href="css/bootstrap.css" rel="stylesheet">
                    <link rel="stylesheet" href="css/main.css">
                </head>				
                <body>
                    <div class="main-p-container">
                        <header>
                            <div class="container">
                                <div class="row top-header-row">
                                    <div class="col col-md-8 col-sm-8 col-xs-8">
                                    	<img class="acc-login" src="images/logo.png" >
                                        <h4 class="proj-name">Certification Data</h4>
                                    </div>
                                    <div class="col col-md-4 col-sm-4 col-xs-4">
                                    	<span>Accenture Center for IBM Technologies</span><br>
                                    	<span>Smarter Selling, Smarter Delivery.</span>
                                    	<span class="proj-name loggedin-label">Logged in : <b><%=userName %></b></span>                               	
                                    	<a href="login.jsp" class="logouts">Logout</a>
                                    </div>
                                </div>
                            </div>
                        </header>
						<div class="container">
                                	<img src="images/sub_title.png" />
                                </div>
						<div class="container">
			<div class="success-msg">Skill added Successfully.</div>   
              <div id="formHome">                          	
            <h3 class="form-title">Skill Details</h3>           
            <hr size="4" color="gray" />
            <table id="skillDetails" style="width:100%">
                <thead>
                    <tr>
                        <th style="width:9%;">Enterprise ID</th>
                        <th style="width:9%;">Employee ID</th>                       
                        <th style="width:9%;">Role</th>
                        <th style="width:9%;">Location</th>                        
                        <th style="width:9%;">Certification Date</th>
                        <th style="width:8%;">Overall Score</th>
                        <th style="width:7%;">Section 1</th>
                        <th style="width:7%;">Section 2</th>
                        <th style="width:7%;">Section 3</th>
                        <th style="width:7%;">Section 4</th>
                        <th style="width:7%;">Section 5</th>                      
                        <th style="width:8%;">Section 6</th>
                        <th style="width:7%;">Cleared</th>
                        <th style="width:9%;">Upload Date</th>
                        <th style="width:10%;" class="no-sort">View PDF</th>                 
                    </tr>
                </thead>
                <tfoot>		
	</tfoot>
   <tbody>
<%
   List skillList=null;
   if(request.getAttribute("skillList")!=null){
	   skillList = (List)request.getAttribute("skillList");
	   //session.setAttribute("exportList",skillList);
   Iterator itr=skillList.iterator();
   while(itr.hasNext()){
   SkillInfo skillInfo=(SkillInfo)itr.next();
   %>

      <tr>     	    
            <td style="text-align:left;"><%=skillInfo.getEnterprizeId() %></td>
            <td><%=skillInfo.getEmployeeId() %></td>
            <td><%=skillInfo.getSkillRole() %></td>
            <td><%=skillInfo.getWorkLocation() %></td>
            <td><%=skillInfo.getCertDate() %></td>
            <td><%=skillInfo.getScore() %></td>            
            <td><%=skillInfo.getSection1Score() %></td>
            <td><%=skillInfo.getSection2Score() %></td>
            <td><%=skillInfo.getSection3Score() %></td>
            <td><%=skillInfo.getSection4Score() %></td>
            <td><%=skillInfo.getSection5Score() %></td>
            <td><%=skillInfo.getSection6Score() %></td>
            <td><%=skillInfo.getClear() %></td>
            <td nowrap><%=skillInfo.getUploadDate() %></td>
            <td><%if(skillInfo.isCertificateAvailable()) {%>
            	<a style="cursor: pointer;" href="downLoadFile?docType=pdf&enterprizeId=<%=skillInfo.getEnterprizeId() %>" target="new"><img src="images/icon_PDF.png" width="21"></a>
            	<%}%>
            </td>            
       </tr>
          <%}
   } %>
  </tbody>
</table>
	<div style="padding-bottom:1px;">&nbsp;</div>
	<div style="font-weight: bold;"><span style="color: red;">Note:</span><span style="padding-left:4px;">If the information updated is not correct, Please email to <a href="mailto:soabpm.multiskill.support@accenture.com" target="_top">soabpm.multiskill.support@accenture.com</a></span></div>    
    <div>Legends:</div>
    <div>Section 1: Hosting Cloud Applications</div>
    <div>Section 2: Planning Cloud Applications</div>
    <div>Section 3: Implementing Cloud Ready Applications</div>
    <div>Section 4: Enhancing Cloud Applications Using Managed Services</div>
    <div>Section 5: Using DevOps Services & Tools to Manage Cloud Applications</div>
    <div>Section 6: Using Data Services</div>
    
    </div>     
    </div>
						
                            <footer class="navbar-fixed-bottom">
                                <div class="small container">
                                    <div class="copy-rights">
                                        Copyright &#169; 2004 - 2015. All rights reserved.
                                    </div>
                                </div>
                            </footer>
                        </div>                                                 
</div>
</body>
                <script src="js/jquery-1.11.3.min.js"></script>
                <script src="js/bootstrap.min.js"></script>           
                <script src="js/main.js"></script>
                </html>