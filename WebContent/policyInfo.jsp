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
            String formNo = request.getParameter("formNo") == null ? "" : request.getParameter("formNo");
            String description = request.getParameter("description") == null ? "" : request.getParameter("description");
            String formType = request.getParameter("formType") == null ? "" : request.getParameter("formType");
            String LOB = request.getParameter("LOB") == null ? "" : request.getParameter("LOB");
            String err="";
            String updateSucc = (String)request.getAttribute("updateFlag") == null ? "" : (String)request.getAttribute("updateFlag");
			
            String userName="";
            
            /*if(request.getSession().getAttribute("userName")!=null){
            	userName =request.getSession().getAttribute("userName").toString();
            }else{
            	response.sendRedirect("LogoutController");
            }*/
            
            /*if(request.getSession().getAttribute("userName")==null){
            	request.getRequestDispatcher("LogoutController").forward(request,response);
            }*/
			%>
                <html>
                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
                    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                    <title>Form Details</title>
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
                                        <h4 class="proj-name">Forms Explorer</h4>
                                    </div>
                                    <div class="col col-md-4 col-sm-4 col-xs-4">
                                    	<span class="proj-name loggedin-label">Logged in : <b></b></span>                               	
                                    	<a href="#" class="logouts">Logout</a>
                                    </div>
                                </div>
                            </div>
                        </header>
						<div class="container">
                                	<img src="images/sub_title.png" />
                                </div>
						<div class="container">
<div class="success-msg2">Form Successfully Updated.</div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="policyInfo.jsp">Search Forms</a></li>
    </ul>
              <div id="formHome">
            <form method="POST" action='SearchSkillController' id="searchForm" name="frmSearchForm" onsubmit="return lStorage()">
                <div class="top-search-items row">
                    <div class="col col-md-4">
                        <div><span class="label">Form Number</span>
                            <span><input type="text" name="formNo" /></span>
                        </div>
                        <div><span class="label">Form Type</span>
                            <select name="formType" id="formTypes">
                            	<option value="">All Form Types</option>
                                <option value="Schedule">Schedule</option>
                                <option value="Policy">Policy</option>
                                <option value="Declarations">Declarations</option>
                                <option value="Other">Other </option>
                                <option value="Endorsement">Endorsement</option>
                                <option value="Coverage">Coverage</option>
                                <option value="PolicyWriting">PolicyWriting</option>
                                <option value="PolicyWriting Support Forms">PolicyWriting Support Forms</option>
                            </select>
                        </div>
                        <div><span class="label">Business Type</span>
                            <select id="businessType" name="businessType">
                            	<option value="" selected>All Business Types</option>
                                <option value="Commercial">Commercial</option>
                                <option value="Personal">Personal</option>
                                <option value="Speciality">Speciality</option>
                            </select>
                        </div>
                    </div>
                    <div class="col col-md-4">
                        <div style="margin-bottom: 0"><!-- <span class="label">Mandatory Forms</span>
                        	<input type=radio name="mandatory" value="Y" checked>&nbsp;Yes
                        	<input type=radio name="mandatory" value="N">&nbsp;No-->
                        	<span class="label">Mandatory Forms</span>
                        	<select name="mandatory" id="manditems">
                        		<option value="">All Forms</option>
                        		<option value="Y">Yes</option>
                        		<option value="N">No</option>
                        	</select>
                        </div>
                        <div><span class="label">LOB</span>
                            <select id="lob" name="lob">
                            </select>
                        </div>
                        <div><span class="label">Source</span>
                            <select name="source" id="souritems">
                                <option value="">All Sources</option>
                                <option value="ISO">ISO</option>
                                <option value="Custom">Custom</option>
                            </select>
                        </div>
                    </div>
                    <div class="col col-md-4">
                    	<div>
                    		<span class="label" style="width: 22%; !important">Multi-State</span>
                            <select name="sele-multi" id="sele-multi" onchange="multiSele1()">
                                <option value="">All</option>
                                <option value="Y">Yes</option>
                                <option value="N">No</option>
                            </select>
                    	</div>
                        <div class="selections">
                            <span class="label" style="width: 22%; !important">State</span>
                            <span>
<select name="state"  class="selector-multiple" multiple id="seleState" size=5>
</select>
</span>                        </div>
<div id="searchedItems" class="searched-items searched-items1">
                        	<ul></ul></div>
                    </div>
                </div>
                <div class="search-btns row">
                        <div class="col col-md-6">
                        	<!--<button type="button" class="btn btns">Search</button>-->
                        	 <a href="#" class="btn btns resetbtn">Reset</a>
							
                        </div>
                        <div class="col col-md-6" style="text-align: right;width: 41%">
                            <input type="submit" value="Search" id="searchbtn" class="btn btns" />
                        </div>
                    </div>
            </form>

			<form method="post" id="downloadForm">
            <h3 class="form-title">Form Details</h3>
            <button class="dwn-btns" id="btnDoc" disabled>Download Doc</button>
      		<button class="dwn-btns" id="btnPdf" disabled>Download Pdf</button>
            <button class="dwn-btns" id="expExcel" disabled>Export To Excel</button>
            <hr size="4" color="gray" />
            <table id="policyDetails">
                <thead>
                    <tr>
                         <th style="width:3%;" class="no-sort">
                            <input type="checkbox" name="formSel" id="selecctall" disabled style="margin-left: 2px;"/>
                            <input type="checkbox" name="formSel" id="selecctall1" style="margin-left: 2px;"/>
                        </th>
                        <th style="width:11%;">Form No<img src="images/sort_desc.png"></th>
                        <th style="width:14%;">Description<img src="images/sort_desc.png"></th>
                        <th style="width:7%;">LOB<img src="images/sort_desc.png"></th>
                        <th style="width:9%;">Form Type<img src="images/sort_desc.png"></th>
                        <th style="width:15%;">State<img src="images/sort_desc.png"></th>
                        <th style="width:9%;">Mandatory<img src="images/sort_desc.png"></th>
                        <th style="width:9%;">Portfolio<img src="images/sort_desc.png"></th>
                        <th style="width:9%;">Source<img src="images/sort_desc.png"></th>
                        <th style="width:7%;" class="no-sort">View PDF</th>
                        <th style="width:7%;" class="no-sort">View Doc</th>

                    </tr>
                </thead>
                <tfoot>
		<tr>
			<th class="clum-hide" style="width:3%;"></th>
			<th style="width:11%;">Form No
			<span class="filter_column filter_text"><input type="text" class="text_filter form-control search_init" value="Form No" rel="1"></span>
			</th>
			<th style="width:14%;" rowspan="1" colspan="1"><span class="filter_column filter_text"><input type="text" class="search_init text_filter form-control" value="Description" rel="2"></span></th>
			<th style="width:7%;">LOB</th>
			<th style="width:9%;">Form Type</th>
			<th style="width:15%;">State</th>
			<th style="width:9%;">Mandatory</th>
			<th style="width:9%;">Portfolio</th>
			<th style="width:9%;">Source</th>
			<th class="clum-hide" style="width:7%;"></th>
			<th class="clum-hide" style="width:7%;"></th>
		</tr>
	</tfoot>
   <!-- <tbody>
   <tr data-target="#myModal" data-toggle="modal" data-formno="sdsaa1" data-desc="sdsaa" data-btype="btPer" data-lob="TR" data-ftype="Schedule" data-mand="N" data-state="AZ,AR" data-prof="FR,GL" data-source="ISO"><td><input class="checkboxs" type="checkbox"></td>
   	<td>sdsaa1</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   	<td class="titles">sdsaa</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   </tr>
    <tr data-target="#myModal" data-toggle="modal" ><td><input class="checkboxs" type="checkbox"></td>
   	<td>sdsaa2</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   	<td class="titles">sdsaa</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   </tr>
    <tr data-target="#myModal" data-toggle="modal" ><td><input class="checkboxs" type="checkbox"></td>
   	<td>sdsaa3</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   	<td class="titles">sdsaa</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   	<td>sdsaa</td>
   </tr>
   </tbody>-->
   <tbody>
<%
   List policyList=null;
   if(request.getAttribute("skillList")!=null){
	   policyList = (List)request.getAttribute("skillList");
	   session.setAttribute("exportList",policyList);
   Iterator itr=policyList.iterator();
   while(itr.hasNext()){
   SkillInfo policyInfo=(SkillInfo)itr.next();
   %>

      <tr>
      	 <td> <%=policyInfo.getEnterprizeId() %></td>
            <td class="state-titles"><%=policyInfo.getEmployeeName() %></td>
            
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
                        
                          <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
      <h4>Edit Form - <span id="fromnos"></span></h4>
      <hr size="4" color="gray" />
        <form method="post" action="editFormController" enctype="multipart/form-data" name="addForm" onsubmit="return showLoad()">
                <div class="row">
                <div class="col col-md-6">
                        <div class="form-group clr-both"><span class="form-lables">Description<span class="mant-symbol">*</span></span>
                        	 <input type="hidden" name="EditFormNo" id="EditFormNo"/>
                            <textarea name="description" maxlength="500" class="data-desc" required></textarea>
                        </div>
                        <div class="form-group clr-both"><span class="form-lables">Business Type<span class="mant-symbol">*</span></span>
                        <select id="businessType" class="data-btype" name="businessType" required>
                                <option value="Commercial">Commercial</option>
                                <option value="Personal">Personal</option>
                                <option value="Speciality">Speciality</option>
                            </select>
                        </div>
                        <div class="form-group"><span class="form-lables">LOB<span class="mant-symbol">*</span></span>
                            <select class="lob data-lob" name="lob" id="lob1" required></select>
                        </div>
                        <div class="form-group"><span class="form-lables">Form Type<span class="mant-symbol">*</span></span>
                        <select name="formType" required class="data-formT" id="formT">
                                <option value="Schedule">Schedule</option>
                                <option value="Policy">Policy</option>
                                <option value="Declarations">Declarations</option>
                                <option value="Other">Other</option>
                                <option value="Endorsement">Endorsement</option>
                                <option value="Coverage">Coverage</option>
                                <option value="PolicyWriting">PolicyWriting</option>
                                <option value="PolicyWriting Support Forms">PolicyWriting Support Forms</option>
                            </select>
                    </div>
                   
                </div>
                <div class="col col-md-6">
                                     <div class="form-group"><span class="form-lables">Mandatory<span class="mant-symbol">*</span></span>
                        <input type=radio name="mandatory" value="Y">&nbsp;Yes
                        	<input type=radio name="mandatory" value="N">&nbsp;No
                    </div>
                    <div class="form-group"><span class="form-lables">State<span class="mant-symbol">*</span></span>
                        <select name="state" class="selector-multiple data-state" id="datState" multiple size=5 required>
						<!-- <option value="AL">Alabama</option>
						<option value="AK">Alaska</option>
						<option value="AZ">Arizona</option>
						<option value="AR">Arkansas</option>
						<option value="CA">California</option>
						<option value="CO">Colorado</option>
						<option value="CT">Connecticut</option>
						<option value="DE">Delaware</option>
						<option value="FL">Florida</option>
						<option value="GA">Georgia</option>
						<option value="HI">Hawaii</option>
						<option value="ID">Idaho</option>
						<option value="IL">Illinois</option>
						<option value="IN">Indiana</option>
						<option value="IA">Iowa</option>
						<option value="KS">Kansas</option>
						<option value="KY">Kentucky</option>
						<option value="LA">Louisiana</option>
						<option value="ME">Maine</option>-->
						</select>

                    </div>
                    <div class="form-group"><span class="form-lables">Portfolio</span>
                        <select multiple  name="portfolio" class="data-prof" id="datPort">
                        	<option value="CA">CA - COMMERCIAL AUTO</option>
                        	<option value="CF">CF - COMMERCIAL PROPERTY</option>
                        	<option value="CP">CM - COMMERCIAL INLAND MARINE</option>
                        	<option value="CR">CR - CRIME AND FIDELITY</option>
                        	<option value="EB">EB - EQUIPMENT BREAKDOWN</option>
                        	<option value="EP">EP - EMPLOYMENT-RELATED PRACTICES</option>
                        	<option value="FR">FR - FARM</option>
                        	<option value="GL">GL - GENERAL LIABILTIY</option>
                        	<option value="OP">OP - CAPITAL ASSETS</option>
                        	<option value="PR">PR - MEDICAL PROFOSSIONAL LIABILTY</option>
                        </select>
                    </div>
                    <div class="form-group"><span class="form-lables">Source<span class="mant-symbol">*</span></span>
                        <select name="source" required class="data-source" id="selcSource">
                                <option value="ISO">ISO</option>
                                <option value="Custom">Custom</option>
                            </select>
                    </div>
                    <div class="form-group clr-both">
                    <!-- <input type="file" id="hideAddPdf" class="hide addPdf"/>
                    <input type="file" id="hideAddDoc" class="hide addDoc"/> -->
                    <span class="form-lables" style="height: 80px">Upload Files<br>(pdf or doc/docx)</span>
                        <!--<input type="text" style="width: 44%;"/>
                        <a href="#" class="btn btns small-btns hiddenElement" id="addPdf">Add PDF</a>  -->
                        <input type="text" style="width: 44%; float: left" class="pdfFile">
                        <a href="#" class="btn btns small-btns hiddenElement" id="addPdf">Add PDF</a> 
                        <!-- <input type="file" id="pdfFile" name="pdfFile" class="addPdf addMultiples hidden"/>-->
                        <input type="file" id="pdfFile" name="pdfFile" accept=".pdf" class="addPdf addMultiples add-either hidden">
                        <br><br>
                        <input type="text" style="width: 44%; float: left" class="docFile">
                            <a href="#" class="btn btns small-btns hiddenElement" id="addDoc">Add DOC/DOCX</a>
                            <!-- <input type="file" id="docFile" name="docFile" class="addDoc addMultiples hidden"/>-->
                         <input type="file" id="docFile" name="docFile" accept=".doc, .docx" class="addDoc addMultiples add-either hidden">
                         <div class="show-error">Either PDF or Doc should be uploaded respectively.</div>
                        </div>
                </div>
                </div>
                <div class="row mTop20">
                    <div class="col col-md-3" style="text-align: center">
                        <button class="btn btns resetdrp" data-dismiss="modal" style="padding: 4px;">Cancel</button>
                    </div>
                    <div class="col col-md-3" style="text-align: left">
                        <input type="submit" class="btn btns" value="Update"></button>
                    </div>
                </div>                
            </div><!--multi end-->
            </form>
      </div>
    </div>
  </div>
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
                <script src="js/main-forms.js"></script>
                </html>