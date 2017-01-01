<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.List" %>
<%@ page import="com.acit.multiskilling.model.SkillInfo" %>
<%@ page import="com.acit.multiskilling.dao.MultiSkillDao" %>
<html>
	<head>
		<!--<meta http-equiv="Content-Type" content="application/vnd.ms-excel; charset=ISO-8859-1">
		<meta http-equiv=Content-Disposition content="attachment; form_report.xlsx">  -->
		<title>Export All</title>
	</head>
<body>
	<table cellpadding="1"  cellspacing="1" border="1" bordercolor="gray">
		<tr>
			<td><b>Enterprise ID</b></td>
			<td><b>Employee ID</b></td>
			<td><b>Role</b></td>
			<td><b>Location</b></td>
			<td><b>Certificate Date</b></td>
			<td><b>Overall Score</b></td>
			<td><b>Section 1</b></td>
			<td><b>Section 2</b></td>
			<td><b>Section 3</b></td>
			<td><b>Section 4</b></td>
			<td><b>Section 5</b></td>
			<td><b>Section 6</b></td>
			<td><b>Cleared</b></td>
		</tr>
		<%  MultiSkillDao dao= new MultiSkillDao(); 
			List<SkillInfo> skillInfoList  = (List<SkillInfo>)dao.getAllSkillData();
	        if (skillInfoList != null) {
    	        response.setContentType("application/vnd.ms-excel");
    	        //response.setContentType("application/vnd.openxml");
    	        //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        	    response.setHeader("Content-Disposition", "inline; filename="+ "certification_report.xls");
        	    //response.setHeader("Content-Disposition", "attachment; filename="+ "form_report.xlsx");    	         
        	}
			for(SkillInfo skillInfo: skillInfoList){
		%>
		<tr>
			<td><%=skillInfo.getEnterprizeId() %></td>			
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
		</tr>
		<% 
			}
		%>
	</table>
</body>
</html>