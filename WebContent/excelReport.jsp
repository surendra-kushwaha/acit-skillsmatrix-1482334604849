<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.List" %>
<%@ page import="com.acit.multiskilling.model.SkillsInfo" %>
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
			<td><b>Team</b></td>
			<td><b>Country</b></td>
			<td><b>Expert Skills</b></td>
			<td><b>Supporting Skills</b></td>
			<td><b>Certifications Obtained</b></td>
			<td><b>Certifications Planned-FY17</b></td>
			<td><b>Point of Contact</b></td>
			<td><b>Comments</b></td>
		</tr>
		<%  MultiSkillDao dao= new MultiSkillDao(); 
			List<SkillsInfo> skillInfoList  = (List<SkillsInfo>)dao.getAllSkillData();
			System.out.println("skillInfoList size $$"+skillInfoList.size());
	        if (skillInfoList != null) {
    	        response.setContentType("application/vnd.ms-excel");
    	        //response.setContentType("application/vnd.openxml");
    	        //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        	    response.setHeader("Content-Disposition", "inline; filename="+ "certification_report.xls");
        	    //response.setHeader("Content-Disposition", "attachment; filename="+ "form_report.xlsx");    	         
        	}
			for(SkillsInfo skillInfo: skillInfoList){
		%>
		<tr>
			<td style="text-align:left;"><%=skillInfo.getEnterpriseId() %></td> 
            <td><%=skillInfo.getTeamName() %></td> 
            <td><%=skillInfo.getCountry() %></td> 
            <td><%=skillInfo.getExpertSkills() %></td> 
            <td><%=skillInfo.getSupSkills() %></td>  
            <td><%=skillInfo.getCertificationObtained() %></td>
            <td><%=skillInfo.getCertificationPlanned() %></td> 
            <td>NA</td> 
            <td>NA</td> 
		</tr>
		<% 
			}
		%>
	</table>
</body>
</html>