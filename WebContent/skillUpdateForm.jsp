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
                    <link href="css/bootstrap.css" rel="stylesheet">
                    <link rel="stylesheet" href="css/main.css">
                     <link rel="stylesheet" href="css/jquery-ui.css">
  <script src="js/jquery-1.11.3.min.js"></script>
  <script src="js/jquery-1.11.3.ui.js"></script>
                    <script>
					  $(function() {
					    //$( "#datepicker" ).datepicker({  maxDate: 0 });
						  $( "#datepicker" ).datepicker({
							  dateFormat: 'dd-M-yy',
							  maxDate: 0,
							  inline: true,
							  showOtherMonths: true,
							  dayNamesMin: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']
							  //numberOfMonths: 2
							});
					  });
				    </script>
                    
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
			<ul class="nav nav-tabs">
		        <li class="active"><a href="SearchSkillController">Search</a></li>
		        <li class="active"><a href="skillUpdateForm.jsp">Update</a></li>
		    </ul>   
              <div id="formHome">
              <%if(!uploadFlag.equalsIgnoreCase("YES")){ %>
            <form method="POST" action='UpdateSkillController' id="searchForm" name="frmSearchForm" onsubmit="return lStorage()">
                <div class="top-search-items row">
                    <div class="col col-md-4">
                        <div><span class="form-lables">Enterprise ID</span>
                            <span><input type="text" name="enterprizeId" /></span>
                        </div>
                                             
                    </div>                    
                    <div class="col col-md-4">                                            
                      
                      
                                         	
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
                        	 							
                        </div>
                        <div class="col col-md-6" style="text-align: left">
                            <input type="submit" value="Search" id="searchbtn" class="btn btns" />
                        </div>
                 </div>
                    
            </form>
            <%} else if(uploadFlag.equalsIgnoreCase("YES")){ %>
            	<div style="font-weight: bold;">If the information updated is not correct, Please contact administrator.</div>
            <%} %>
            <h3 class="form-title">Update Certification Details</h3>
            
   
<%
   List skillList=null;
   if(request.getAttribute("skillList")!=null){
	   skillList = (List)request.getAttribute("skillList");
	   session.setAttribute("exportList",skillList);
   //Iterator itr=skillList.iterator();
  // while(itr.hasNext()){
   SkillInfo skillInfo=(SkillInfo)skillList.get(0);
   %>
        </div>
        <div id="uploadForms">
    	<div class="selectFileTypes row">
                <div class="radioContain col col-md-4" style="width: 30%;padding: 0;text-align:center;">Update Certification Data</div>               
            </div>
            <div class="loadImg"><div class="load-img"><img src="images/adding.gif"></div></div>
            <div class="singleUpload hide formsTb">
            <div class="row">
            <form method="post" action="uploadCertificate" enctype="multipart/form-data" onsubmit="return showLoad1()">
            	<div class="form-group col-md-6"></div>
                
            </div><!-- singleend -->
            <div class="row mTop30">
                    <div class="col-md-offset-1 col-md-2" style="width: 11%; margin-right: 10px;">
                        <a href="javascript: void(0)" class="btn btns resetbtn tab3" id="reset">Reset</a>
                    </div>
                    <div class="col col-md-6">
                        <input type="submit" value="Update" class="btn btns"></button>
                    </div>
                    </div>
                </div>
               </form> 
            <div class="multipleUpload formsTb">
            <form method="post" action="uploadCertificate" enctype="multipart/form-data" id="addForm" name="addForm" onsubmit="return showLoad()">
                <div class="row">
                <div class="col col-md-5">
                 
                		<div class="form-group" style="float: left; width: 100%">
                        	<span class="form-lables">Enterprise Id<span class="mant-symbol"></span></span>
                            <input type="text" name="enterprizeId" id="enterprizeId" value="<%=skillInfo.getEnterprizeId() %>"  style="background-color:#EBEBE4;border:1px solid #ABADB3;" readonly />
                            <input type="hidden" name="adminRole" id="adminRole" value="adminRole"/>
                        </div>
                    	<div class="form-group" style="float: left; width: 100%">
                        	<span class="form-lables">Employee Id<span class="mant-symbol"></span></span>
                            <input type="text" name="employeeId" id="employeeId" value="<%=skillInfo.getEmployeeId() %>"  style="background-color:#EBEBE4;border:1px solid #ABADB3;" readonly />
                        </div>
                        <div class="form-group clr-both"><span class="form-lables">Certification Name<span class="mant-symbol">*</span></span>
                            <select id="certificateName" name="certificateName" required>
	                            	<option value="IBM Certified Application Developer - Cloud Platform v1" selected>IBM Certified Application Developer - Cloud Platform v1</option>	                               
	                        </select>
                        </div>
                        <div class="form-group clr-both"><span class="form-lables"></span></span>                           
                        </div>                                                                                                                                                             
                </div>
                <div class="col col-md-5">
                    
                    <div id="searchedItems" class="searched-items searched-items1">
                        	<ul></ul></div>                                       
                    <div class="form-group"><span class="form-lables">Role<span class="mant-symbol"></span></span>
                        <input type="text" name="skillRole" maxlength="200" id="skillRole" value="<%=skillInfo.getSkillRole() %>"  style="background-color:#EBEBE4;border:1px solid #ABADB3;" readonly />                  	
                        </div>
                        <div class="form-group clr-both"><span class="form-lables"></span></span>                           
                        </div>
                        <div class="form-group"><span class="form-lables">Work Location<span class="mant-symbol"></span></span>
	                       <input type="text" name="workLocation" maxlength="200" id="workLocation" value="<%=skillInfo.getWorkLocation() %>" style="background-color:#EBEBE4;border:1px solid #ABADB3;" readonly />
	                        <!--<select id="workLocation" name="workLocation" required>
	                            	<option value="" selected>Select Work Location</option>
	                                <option value="Bangalore">Bangalore</option>
	                                <option value="Chennai">Chennai</option>
	                                <option value="Delhi">Delhi</option>
	                                <option value="Gurgaon">Gurgaon</option>
	                                <option value="Hydrabad">Hydrabad</option>
	                                <option value="Kolkata">Kolkata</option>
	                                <option value="Mumbai">Mumbai</option>
	                                <option value="Pune">Pune</option>
	                                <option value="Onshore">Onshore</option>
	                        </select>  -->
                       </div>                                       
                </div>
                <hr size="4" color="gray" />
                
                <div class="col col-md-5">
                 		                			                    
	                    <div class="form-group" style="float: left; width: 100%">                			
                        	<div>Overall Score (%)<span class="mant-symbol">*</span></div>
                            <div>
                            	<span> <input type="text" name="score" maxlength="3" id="formScore" class="form-valid" required /></span>
                            </div> 
                        </div>
	                    
	                    <br />
	                    <div class="show-score-error">Please enter score between 0 to 100</div>
	                    	<div class="form-group clr-both"><span class="form-lables"></span></span>                           
	                    </div>
                 		
                		<div class="form-group" style="float: left; width: 100%">                			
                        	<div>Section 1:Hosting Cloud Apps<span class="mant-symbol">*</span></div>
                            <div>
                            	<span><input type="text" name="Score1" maxlength="3" id="Score1" class="form-valid" required /></span>
                            </div> 
                        </div>
                    	
                        <div class="form-group" style="float: left; width: 100%"><div>Section 3:Implementing Cloud Ready Apps<span class="mant-symbol">*</span></div>
                            <div>
                            	<span><input type="text"   name="Score3" maxlength="3" id="Score3" class="form-valid" required /></span> 
                            </div>
                        </div>
                                              
                        
                        <div class="form-group" style="float: left; width: 100%"><div>Section 5:Using DevOps Services & Tools<span class="mant-symbol">*</span></div>
	                        <div>
                            	<span><input type="text" name="Score5" maxlength="3" id="Score5" class="form-valid" required /></span> 
                            </div>
                       </div>
                                               	                     
	                    <div class="form-group clr-both" style="float: left; width: 100%">
	                        <div class="" style="height: 20px">Upload Certificate<span class="mant-symbol">*</span>(pdf)</div>
	                        <input type="text" class="pdfFile" id="pdfFileText" required readonly/>
	                        <a href="#" class="btn btns small-btns hiddenElement" id="addPdf">Add PDF</a>                       
	                        <input type="file" id="pdfFile" name="pdfFile" accept=".pdf" class="addPdf addMultiples add-either hidden"/>
	                        <br />                     
	                         <div class="show-error">Please select certificate to upload.</div>
	                    </div>
                                                                                                                               
                </div>
                <div class="col col-md-5">                  
                    <div id="searchedItems" class="searched-items searched-items1">
                        	<ul></ul></div>                                       
                                                                                                                                                               
                     <div class="form-group" style="float: left; width: 100%">
                        	<div>Certification Date<span class="mant-symbol">*</span></div>
                            <div>
                            	 <input type="text" name="datepicker" id="datepicker" class="form-valid" required  />
                            </div>
                     </div>                    
                    
                    <div class="form-group" style="float: left; width: 100%">
                        	<div>Section 2:Planning Cloud Apps<span class="mant-symbol">*</span></div>
                            <div>
                            	<span><input type="text"  name="Score2" maxlength="3" id="Score2" class="form-valid" required /></span> 
                            </div>
                     </div>
                    
                    <div class="form-group" style="float: left; width: 100%"><div>Section 4:Enhancing Cloud Apps<span class="mant-symbol">*</span></div>
                        	<div>
                            	<span><input type="text"  name="Score4" maxlength="3" id="Score4" class="form-valid" required /></span> 
                            </div>
                     </div>
                     
                     <div class="form-group" style="float: left; width: 100%">
                        <div>Section 6:Using Data Services<span class="mant-symbol">*</span></div>
                            <div>
                            	<span><input type="text"  name="Score6" maxlength="3" id="Score6" class="form-valid" required /></span>
                            </div>
                    </div>
                    
                    <div class="form-group" style="float: left; width: 100%">
                          <div class="show-score1-error">Please enter each section score between 0 to 100</div> 
	                </div>
                                      	
                </div>                
                </div>
                <div class="row mTop20" style="margin:20px;">
                    <div class="col col-md-3" style="text-align: center">
                        <input type="button" value="Reset" class="btn btns resetbtn" id="addReset">
                    </div>
                    <div class="col col-md-3" style="text-align: left">
                        <input type="submit" class="btn btns" value="Update">
                    </div>
                </div>                
            </div><!--multi end-->
            </form>
      <%//}
   } %>       
    </div>
    <div>Legends:</div>
    <div>Section 1: Hosting Cloud Applications</div>
    <div>Section 2: Planning Cloud Applications</div>
    <div>Section 3: Implementing Cloud Ready Applications</div>
    <div>Section 4: Enhancing Cloud Applications Using Managed Services</div>
    <div>Section 5: Using DevOps Services & Tools to Manage Cloud Applications</div>
    <div>Section 6: Using Data Services</div>
               
    </div>   
  
						
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
                <script src="js/jquery.dataTables.js"></script>
                <script src="js/jquery.dataTables.columnFilter.js"></script> 
                <script src="js/main.js"></script>
                </html>