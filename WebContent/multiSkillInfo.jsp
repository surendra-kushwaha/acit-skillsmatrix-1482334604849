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
                                    </div>
                                </div>
                            </div>
                        </header>
						<div class="container">
                                	<img src="images/sub_title.png" />
                                </div>
						<div class="container">  
              <div id="formHome">
              <%if(!uploadFlag.equalsIgnoreCase("YES")){ %>
            <form method="POST" action='SearchSkillController' id="searchForm" name="frmSearchForm" onsubmit="return lStorage()">
                <div class="top-search-items row">
                    <div class="col col-md-4">
                        <div><span class="form-lables">Enterprise ID</span>
                            <span><input type="text" name="enterprizeId" /></span>
                        </div>
                        <div class="form-group clr-both"><span class="form-lables">*Expert Skill<span class="mant-symbol"></span></span>                       
                        	<!--<select id="skillRole" name="skillRole">
                            	<option value="" selected>Select Skill</option>
                                <option value="Integration Developer">Integration Developer</option>
                                <option value="Integration Architect">Integration Architect</option>
                                <option value="PaaS Developer">PaaS Developer</option>                               
                        	</select>-->
                        	<span><input type="text" name="expertSkills" /></span>
                        </div>
                                       
                    </div>                    
                    <div class="col col-md-4">                                                                 
                      <div class="form-group clr-both"><span class="form-lables" style="width:105px;">Country<span class="mant-symbol"></span></span>
	                        <select id="workLocation" name="workLocation">
	                            	<option value="" selected>Select Country</option>
	                                <option value="India">India</option>
	                                <option value="China">China</option>
	                                <option value="Finland">Finland</option>
	                                <option value="Maxico">Maxico</option>
	                                <option value="Spain">Spain</option>
	                                <option value="UK">UK</option>
	                                <option value="Norway">Norway</option>
	                                <option value="US">US</option>
	                        </select>
                       </div>
                       <div class="form-group clr-both"><span class="form-lables" style="width:105px;">**Supporting Skill</span>
	                        <!--<select id="clear" name="clear">
	                            <option value="" selected>Select Skill</option>
                                <option value="Integration Developer">Integration Developer</option>
                                <option value="Integration Architect">Integration Architect</option>
                                <option value="PaaS Developer">PaaS Developer</option> 	                                
	                        </select>-->
	                        <span><input type="text" name="supSkills" /></span>
                       </div>
                       <div><span class="form-lables">Certificate</span>
                            <span><input type="text" name="certificate" /></span>
                        </div>                    	
                    </div>
                <div class="search-btns row">
                		<div class="col col-md-6" style="text-align: center">
                            <input type="submit" value="Search" id="searchbtn" class="btn btns" />
                        </div>
                        <div class="col col-md-6" style="text-align: left">
                        	 <input type="reset" value="Reset"  class="btn btns resetbtn" id="reset"/>							
                        </div>
                        
                 </div>
                <div><b>Note</b></div>
    				<div><b>*Expert Skill</b> : The person has development experience on the skill.</div>
    				<div><b>**Supporting Skill</b> : The person is familiar with this skill and can get cross trained so as to be able to develop simple applications within couple of weeks</div>      
    			</div>     
            </form>
            <%} else if(uploadFlag.equalsIgnoreCase("YES")){ %>
            	<div style="font-weight: bold;">If the information updated is not correct, Please contact administrator.</div>
            <%} %>
			<form method="post" id="downloadForm">
            <h3 class="form-title">Skill Details</h3>
             <button class="dwn-btns" id="expExcel">Export All</button>
            <hr size="4" color="gray" />
            <table id="skillDetails" style="width:100%">
                <thead>
                    <tr>
                        <th style="width:9%;">Enterprise ID</th>                                                                     
                        <th style="width:7%;">Team</th>
                        <th style="width:7%;">Country</th>
                        <th style="width:12%;">*Expert Skills</th>
                        <th style="width:12%;">**Supporting Skills</th>
                        <th style="width:15%;">Certifications Obtained</th>
                        <th style="width:15%;">Certifications Planned-FY17</th>
                        <th style="width:10%;">Point of Contact</th> 
                        <th style="width:10%;">Comments</th>                
                    </tr>
                </thead>
                <tfoot>
		<!--<tr>
			<th class="clum-hide" style="width:4%;"></th>
			<th style="width:11%;">Enterprize ID</th>
			<th style="width:11%;">Employee ID</th>
			<th style="width:14%;">Name</th>
			<th style="width:10%;">Role</th>
			<th style="width:10%;">Location</th>
			<th style="width:11%;">Certificate Name</th>
			<th style="width:11%;">Certificate Date</th>
			<th style="width:7%;">Cleared</th>
			<th style="width:10%;">Score(%)</th>			
			<th class="clum-hide" style="width:7%;"></th>
		</tr>  -->
	</tfoot>
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
            <td style="text-align:left;"><a href="mailto:<%=skillInfo.getEnterpriseId() %>@accenture.com?Subject=Re: certification" target="_top"><%=skillInfo.getEnterpriseId() %></a></td> 
            <td><%=skillInfo.getTeamName() %></td> 
            <td><%=skillInfo.getCountry() %></td> 
            <td><%=skillInfo.getExpertSkills() %></td> 
            <td><%=skillInfo.getSupSkills() %></td>  
            <td><%=skillInfo.getCertificationObtained() %></td>
            <td><%=skillInfo.getCertificationPlanned() %></td> 
            <td><a href="mailto:<%=skillInfo.getMentorEntId() %>@accenture.com?Subject=Re: certification" target="_top"><%=skillInfo.getMentorEntId()%></a></td> 
            <td>NA</td>    
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
