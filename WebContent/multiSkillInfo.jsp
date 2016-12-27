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
                                        <h4 class="proj-name">Skills Matrix</h4>
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
			<ul class="nav nav-tabs">
		        <li class="active"><a href="multiSkilling.jsp">Search</a></li>
		    </ul>   
              <div id="formHome">
              <%if(!uploadFlag.equalsIgnoreCase("YES")){ %>
            <form method="POST" action='SearchSkillController' id="searchForm" name="frmSearchForm" onsubmit="return lStorage()">
                <div class="top-search-items row">
                    <div class="col col-md-4">
                        <div><span class="form-lables">Enterprise ID</span>
                            <span><input type="text" name="enterprizeId" /></span>
                        </div>
                        <div class="form-group clr-both"><span class="form-lables">Expert Skill<span class="mant-symbol"></span></span>                       
                        	<!--<select id="skillRole" name="skillRole">
                            	<option value="" selected>Select Skill</option>
                                <option value="Integration Developer">Integration Developer</option>
                                <option value="Integration Architect">Integration Architect</option>
                                <option value="PaaS Developer">PaaS Developer</option>                               
                        	</select>-->
                        	<span><input type="text" name="ExpertSkill" /></span>
                        </div>                       
                    </div>                    
                    <div class="col col-md-4">                                            
                      <div class="form-group clr-both"><span class="form-lables">Supporting Skill<span class="mant-symbol"></span></span>
	                        <!--<select id="clear" name="clear">
	                            <option value="" selected>Select Skill</option>
                                <option value="Integration Developer">Integration Developer</option>
                                <option value="Integration Architect">Integration Architect</option>
                                <option value="PaaS Developer">PaaS Developer</option> 	                                
	                        </select>-->
	                        <span><input type="text" name="SupSkill" /></span>
                       </div>
                      
                      <div class="form-group clr-both"><span class="form-lables">Work Location<span class="mant-symbol"></span></span>
	                        <select id="workLocation" name="workLocation">
	                            	<option value="" selected>Select Work Location</option>
	                                <option value="Bangalore">Bangalore</option>
	                                <option value="Chennai">Chennai</option>
	                                <option value="Delhi">Delhi</option>
	                                <option value="Gurgaon">Gurgaon</option>
	                                <option value="Hyderabad">Hyderabad</option>
	                                <option value="Kolkata">Kolkata</option>
	                                <option value="Mumbai">Mumbai</option>
	                                <option value="Pune">Pune</option>
	                                <option value="Onshore">Onshore</option>
	                        </select>
                       </div>                    	
                    </div>
                <!--<div class="search-btns row">                       
                        <div class="col col-md-6" style="text-align: left;width: 41%">
                        <br/>
                            <input type="submit" value="Search" id="searchbtn" class="btn btns" />
                        </div>
                </div>  -->
                
                <div class="search-btns row">
                        <div class="col col-md-6" style="text-align: center">
                        	<!--<button type="button" class="btn btns">Search</button>-->
                        	 <input type="button" value="Reset"  class="btn btns resetbtn" id="reset">							
                        </div>
                        <div class="col col-md-6" style="text-align: left">
                            <input type="submit" value="Search" id="searchbtn" class="btn btns" />
                        </div>
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
                        <th style="width:9%;">Employee Name</th>                                                
                        <th style="width:7%;">Team</th>
                        <th style="width:7%;">Country</th>
                        <th style="width:12%;">Expert Skills</th>
                        <th style="width:12%;">Supporting Skills</th>
                        <th style="width:15%;">Certifications Obtained</th>
                        <th style="width:15%;">Certifications Planned-FY17</th>
                        <th style="width:10%;">Point of Contact</th> 
                        <th style="width:10%;">Comments</th>                
                    </tr>
                </thead>
                <tfoot>
		<tr>
			<th class="clum-hide" style="width:4%;"></th>
			<th style="width:11%;">Enterprize ID</th>
			<th style="width:11%;">Employee Name</th>
			<th style="width:14%;">Team</th>
			<th style="width:10%;">Country</th>
			<th style="width:10%;">Expert Skill</th>
			<th style="width:11%;">Supporting Skills</th>
			<th style="width:11%;">Certifications Obtained</th>
			<th style="width:7%;">Certifications Planned-FY17</th>
			<th style="width:10%;">Point of Contact</th>			
			<th class="clum-hide" style="width:7%;"></th>
		</tr>
	</tfoot>
   <tbody>
<%
   List skillList=null;
   if(request.getAttribute("skillList")!=null){
	   skillList = (List)request.getAttribute("skillList");
	   session.setAttribute("exportList",skillList);
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
        </div>
        
               
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