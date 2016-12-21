<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>-->
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
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
                                    <div class="col col-md-12 col-sm-12 col-xs-12">
                                    	<img class="acc-login" src="images/logo.png" >
                                    </div>
                                </div>
                            </div>
                        </header>
                        <div class="container">
                                	<img src="images/sub_title.png" />
                                </div>
						
						<div class="container">
    	
   				<div class="login-container row">
   				<div class="col col-md-6"><img src="images/login_image.png">
   				</div>
   				<div class="col col-md-6" style="padding-top: 25px;">
<%
String inValidUser=null;
if(request.getAttribute("loginMassage")!=null){
	inValidUser = (String)request.getAttribute("loginMassage");
	System.out.println("message recieved::"+inValidUser);
%>	<div class="login-error"><%=inValidUser%></div>
<%} %>

<%
String sessionExpired=null;
if(request.getAttribute("LoginError")!=null){
	sessionExpired = (String)request.getAttribute("LoginError");
	System.out.println("message recieved::"+sessionExpired);
%>	<div class="login-error"><%=sessionExpired%></div>
<%} %>
<%
if(session!=null){
	session.invalidate();
}
 %>
   				<form method="POST" action="LoginController" name="loginForm" class="form-horizontal" style="margin-top: 0; padding: 25px 10% 0 10%;">
  <div class="form-group">
    <label for="inputEmail3" class="col-sm-3 control-label">User Name</label>
    <div class="col-sm-9">
      <input type="text" class="form-control" id="username" name="userName" placeholder="User Name" style="width: 100%;">
    </div>
  </div>
  <div class="form-group">
    <label for="inputPassword3" class="col-sm-3 control-label">Password</label>
    <div class="col-sm-9">
      <input type="password" class="form-control" id="password" name="password" placeholder="Password">
    </div>
  </div>
  <div class="form-group">
    <div class="col-sm-offset-3 col-sm-9">
      <div class="checkbox">
        <label>
          <input type="checkbox" id="remDetails"> Remember my Login details
        </label>
      </div>
    </div>
  </div>
  <div class="form-group">
  <div class="col-sm-offset-3" style="padding-left:18px">
  	 <input type="button" value="reset" class="btn btns resetbtn" style="margin-right: 10px;"/>
  	 <button type="submit" class="btn btns">Login</button>
  	 </div>
  </div>
</form>

        <!-- <form method="POST" action='LoginController' name="loginForm" class="form-inline">                       
            User Name : <input type="text" name="userName"
                               value="" /> <br /><br /><br />
             Password : 
                <input type="password" name="password"
                               value="" /> <br />  <br /><br />        
                       <input type="checkbox">Remember my Login details
                <br /> <br /> <br />
             <input type="button" value="reset" class="btn btns resetbtn"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="submit" value="Submit" class="btn btns"/>
        </form>-->
        </div></div>
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
    <script>
   					var errormsgs="";
   					var updateErr = "";
    			 </script>
     <script src="js/jquery-1.11.3.min.js"></script>
                <script src="js/bootstrap.min.js"></script>
                <script src="js/jquery.dataTables.js"></script>
                <script src="js/dataTables.tableTools.js"></script>
                <script src="js/jquery.dataTables.columnFilter.js"></script>
                <script src="js/main.js"></script>
</html>