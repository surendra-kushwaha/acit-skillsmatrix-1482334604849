<%@page import="com.ibm.json.java.*,java.util.Collection, java.util.Iterator, java.math.BigDecimal"%>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.*"%>
        <%@ page import="com.acit.multiskilling.model.SkillsInfo"%>
            <!DOCTYPE html>
            <% 
            String err="";
            String updateSucc = (String)request.getAttribute("updateFlag") == null ? "" : (String)request.getAttribute("updateFlag");
			
            String userName="";
            
            if(request.getSession().getAttribute("userName")!=null){
            	userName =request.getSession().getAttribute("userName").toString();
            }else{
            	//response.sendRedirect("LogoutController");
            }
            String uploadFlag="";
            if(request.getAttribute("uploadFlag")!=null){
            	uploadFlag=(String)request.getAttribute("uploadFlag");
            	//request.getRequestDispatcher("LogoutController").forward(request,response);
            }
            
            String enterprizeId = request.getParameter("enterprizeId") == null ? "" : request.getParameter("enterprizeId");
            String skill = request.getParameter("skill") == null ? "" : request.getParameter("skill");
            String workLocation = request.getParameter("workLocation") == null ? "" : request.getParameter("workLocation");
            String certification = request.getParameter("certification") == null ? "" : request.getParameter("certification");
			%>
                <html>
                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                    <title>ACIT Skills and Certification Dashboard</title>
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
                                        <h4 class="proj-name">ACIT Skills and Certification Dashboard</h4>
                                    </div>
                                    <div class="col col-md-4 col-sm-4 col-xs-4">
                                    	<span>Accenture Center for IBM Technologies</span><br>
                                    	<span>Smarter Selling, Smarter Delivery.</span>
                                    	<span class="proj-name loggedin-label"></span>                               	
                                    </div>
                                </div>
                            </div>
                        </header>
						<div class="container">
                                	<img src="images/sub_title.png" />
                                </div>
						<div class="container">  
              <div id="formHome" width="116%" style="width:116%">
            <form method="POST" action='SearchSkillController' id="searchForm" name="frmSearchForm" onsubmit="return lStorage()">
                <div class="top-search-items row">
                    <div class="col col-md-4">
                        <div><span class="form-lables">Enterprise ID</span>
                            <span><input type="text" name="enterprizeId"  value="<%=enterprizeId%>"  /></span>
                        </div>
                        <div class="form-group clr-both"><span class="form-lables">Skill<span class="mant-symbol"></span></span>                       
                        	<span><input type="text" name="skill" value="<%=skill%>" /></span>
                        </div>
                                       
                    </div>                    
                    <div class="col col-md-4">                                                                 
                      <div class="form-group clr-both"><span class="form-lables" style="width:105px;">Country<span class="mant-symbol"></span></span>
	                        <select id="workLocation" name="workLocation">
	                            	<option value="" selected>Select Country</option>	                            	
	                                <option <%= (workLocation.equals("India")?"selected='selected'":"") %> value="India">India</option>
	                                <option  <%= (workLocation.equals("China")?"selected='selected'":"") %> value="China">China</option>
	                                <option <%= (workLocation.equals("Finland")?"selected='selected'":"") %> value="Finland">Finland</option>
	                                <option <%= (workLocation.equals("Maxico")?"selected='selected'":"") %> value="Maxico">Maxico</option>
	                                <option <%= (workLocation.equals("Spain")?"selected='selected'":"") %> value="Spain">Spain</option>
	                                <option <%= (workLocation.equals("UK")?"selected='selected'":"") %> value="UK">UK</option>
	                                <option <%= (workLocation.equals("Norway")?"selected='selected'":"") %> value="Norway">Norway</option>
	                                <option <%= (workLocation.equals("US")?"selected='selected'":"") %> value="US">US</option>
	                        </select>
                       </div>
                       <div class="form-group clr-both"><span class="form-lables" style="width:105px;">Certification</span>
	                        <span><input type="text" name="certification"  value="<%=certification%>" /></span>
                       </div>                               	
                    </div>
                    
                <div class="search-btns row">
                		<div class="col col-md-6" style="text-align: center">
                            <input type="submit" value="Search" id="searchbtn" class="btn btns" onkeydown = "if (event.keyCode == 13)
                        document.getElementById('searchbtn').click()"/>
                        </div>
                        <div class="col col-md-6" style="text-align: left">
                        	 <input type="reset" value="Reset"  class="btn btns resetbtn" id="reset" onkeydown = "if (event.keyCode == 13)
                        document.getElementById('reset').click()"/>							
                        </div>
                        
                 </div>
                <div><b>Note</b></div>
    				<div><b>*Expert Skill</b> : The person has development experience on the skill.</div>
    				<div><b>**Supplementary Skill</b> : The person is familiar with this skill and can get cross trained so as to be able to develop simple applications within couple of weeks</div>      
    			</div>     
            </form>
			<form method="post" id="downloadForm">
            <h3 class="form-title">Skill Details</h3>
             <button class="dwn-btns" id="expExcel">Export All</button>
            <hr size="4" color="gray" />
            <table id="skillDetails" style="width:97%">
                <thead>
                    <tr>
                        <th style="width:9%;">Enterprise ID</th>                                                                     
                        <th style="width:7%;">Team</th>
                        <th style="width:7%;">Country</th>
                        <th style="width:12%;">*Expert Skills</th>
                        <th style="width:12%;">**Supplementary Skills</th>
                        <th style="width:15%;">Certifications Obtained</th>
                        <th style="width:15%;">Certifications Planned-FY17</th>
                        <th style="width:10%;">Point of Contact</th> 
                        <th style="width:10%;">Comments</th>                
                    </tr>
                </thead>
   <tbody>
<%
   List skillList=null;
   if(request.getAttribute("skillList")!=null){
	   skillList = (List)request.getAttribute("skillList");
	   session.setAttribute("exportList",skillList);
   Iterator itr=skillList.iterator();
   while(itr.hasNext()){
   SkillsInfo skillInfo=(SkillsInfo)itr.next();
   %>
      <tr>      	        	    
            <td style="text-align:left;width:9%;"><a href="mailto:<%=skillInfo.getEnterpriseId()%>@accenture.com?Subject=Re: certification" target="_top"><%=skillInfo.getEnterpriseId() %></a></td> 
            <td style="width:7%;"><%=skillInfo.getTeamName() %></td> 
            <td style="width:7%;"><%=skillInfo.getCountry() %></td> 
            <td style="width:12%;"><%=skillInfo.getExpertSkills() %></td> 
            <td style="width:12%;"><%=skillInfo.getSupSkills() %></td>  
            <td style="width:15%;"><%=skillInfo.getCertificationObtained() %></td>
            <td style="width:15%;"><%=skillInfo.getCertificationPlanned() %></td> 
            <td style="width:10%;"><a href="mailto:<%=skillInfo.getMentorEntId()%>?Subject=Re: certification" target="_top"><%=skillInfo.getMentorEntId()%></a></td> 
            <td style="width:10%;"><%=skillInfo.getComments() %></td>    
       </tr>
          <%}
   } %>
   </tbody>
</table>
        </div>           
     </form>
						
                            <footer class="navbar-fixed-bottom">
                                <div class="small container">
                                    <div class="copy-rights">
                                        Copyright &#169; 2004 - 2015. All rights reserved.
                                    </div>
                                </div>
                            </footer>
                        </div>                              
                </body>
                 <script>
   					var errormsgs="<%=err%>";
   					var updateErr = "<%=updateSucc%>";
    			 </script>
                <script src="js/jquery-1.11.3.min.js"></script>
                <script src="js/bootstrap.min.js"></script>
                <script src="js/jquery.dataTables.js"></script>
                <script src="js/dataTables.tableTools.js"></script>
                <script src="js/jquery.dataTables.columnFilter.js"></script>
                <script src="js/main.js"></script>
                </html>
