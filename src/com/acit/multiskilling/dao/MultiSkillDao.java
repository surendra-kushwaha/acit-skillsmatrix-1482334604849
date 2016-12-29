package com.acit.multiskilling.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.acit.multiskilling.exception.MultiSkillException;
import com.acit.multiskilling.model.SkillInfo;
import com.acit.multiskilling.model.SkillsInfo;
import com.acit.multiskilling.util.DataBase;

public class MultiSkillDao {

	private Connection connection;

	public MultiSkillDao() {
		connection = DataBase.getInstance().getConnection();
		// connection = null;
	}
    //*** updatePolicy method updated by Surendra ****** //
    public boolean updateSkill(SkillInfo skillInfo) throws MultiSkillException{
    	boolean updateSuccessFlag=false;
    	PreparedStatement preparedStatement=null;
       //System.out.println("In updatePolicy - Start ");
    	try {
    		if ((connection == null) || connection.isClosed() ) {
				//con.DriverManager.getConnection(...);
				connection =  DataBase.getInstance().getConnection();
		    }
    		//List<InputStream> bindVariables = new ArrayList<InputStream>();
    		StringBuffer queryString = new StringBuffer();
			queryString.append("UPDATE MULTI_SKILLING_DATA set \"SKILL_ROLE\"=?,\"SCORE\"=?, \"CERTIFICATE_NAME\"=?,  \"CERT_UPLOAD_FLAG\"=?,  \"CERTIFICATE_EXTN\"=?, \"CERTIFICATE\"=?"
            		+", \"WORK_LOCATION\"=?,  \"CERTIFICATION_DATE\"=?,  \"CLEARED_FLAG\"=?, \"SECTION1_SCORE\"=?, \"SECTION2_SCORE\"=?, \"SECTION3_SCORE\"=?,"
            		+ " \"SECTION4_SCORE\"=?, \"SECTION5_SCORE\"=?, \"SECTION6_SCORE\"=?,\"UPLOAD_DATE\"=? where \"ENTERPRIZE_ID\"=?");

			
			
			preparedStatement = connection.prepareStatement(queryString.toString());
			//System.out.println("update query String ##"+queryString.toString());
			preparedStatement.setString(1, skillInfo.getSkillRole());
			preparedStatement.setString(2, skillInfo.getScore());
			preparedStatement.setString(3, skillInfo.getCertificateName());
            preparedStatement.setString(4, "YES");
            preparedStatement.setString(5,skillInfo.getCertificateExtn());
            preparedStatement.setBinaryStream(6, skillInfo.getCertificate(),skillInfo.getCertificate().available());
            
            preparedStatement.setString(7, skillInfo.getWorkLocation());
			preparedStatement.setString(8, skillInfo.getCertDate());
            preparedStatement.setString(9, skillInfo.getClear());
            preparedStatement.setString(10,skillInfo.getSection1Score());
            preparedStatement.setString(11, skillInfo.getSection2Score());
            preparedStatement.setString(12, skillInfo.getSection3Score());
			preparedStatement.setString(13, skillInfo.getSection4Score());
            preparedStatement.setString(14, skillInfo.getSection5Score());
            preparedStatement.setString(15,skillInfo.getSection6Score());
            
            String PATTERN="dd-MMM-YYYY";
            SimpleDateFormat dateFormat=new SimpleDateFormat();
            dateFormat.applyPattern(PATTERN);
            String uploadDate=dateFormat.format(Calendar.getInstance().getTime());
            System.out.println("uploadDate##"+uploadDate);
            preparedStatement.setString(16,uploadDate);
            
            preparedStatement.setString(17, skillInfo.getEnterprizeId());
    		/*for (int i = 0; i < bindVariables.size(); i++) {
				// variables are indexed from 1 in JDBC
    			int index=9;
    			preparedStatement.setBinaryStream(index+i + 1, bindVariables.get(i),bindVariables.get(i).available());
			}  */  		
            System.out.println("DAO addskillInfo"+skillInfo);
            System.out.println("Controller addskillInfo entID"+skillInfo.getEnterprizeId());
            System.out.println("Controller addskillInfo score"+skillInfo.getScore());
            System.out.println("Controller addskillInfo certificateName"+skillInfo.getCertificateName());
            
            
            int updateflag=preparedStatement.executeUpdate();
            if(updateflag>0){
            	System.out.println("updateFlag"+updateflag);
            	updateSuccessFlag=true;
            }
            //connection.commit();            
        }catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new MultiSkillException(e.getErrorCode());
			
		}  catch (Exception e) {
            e.printStackTrace();
        }finally{
        	close(null,preparedStatement,connection);
        }
    	return updateSuccessFlag;
    }

	public List<SkillInfo> getAllSkillData() {
		List<SkillInfo> policies = new ArrayList<SkillInfo>();
		PreparedStatement statement=null;
		ResultSet rs=null;
		try {
			if ((connection == null) || connection.isClosed() ) {
				//con.DriverManager.getConnection(...);
				connection =  DataBase.getInstance().getConnection();
		    }
			statement = connection.prepareStatement("select * from MULTI_SKILLING_DATA where \"EMPLOYEE_ROLE\"='user' and  CERT_UPLOAD_FLAG='YES' ");
			rs = statement.executeQuery();
			while (rs.next()) {
				SkillInfo skillInfo = new SkillInfo();
				skillInfo.setEmployeeName(rs.getString("EMPLOYEE_NAME"));
				skillInfo.setEnterprizeId(rs.getString("ENTERPRIZE_ID"));
				skillInfo.setEmployeeId(rs.getString("EMPLOYEE_ID"));
				skillInfo.setSkillRole(rs.getString("SKILL_ROLE"));
				skillInfo.setEmployeeRole(rs.getString("EMPLOYEE_ROLE"));
				/*if(rs.getString("CERTIFICATE_NAME")!=null){
					skillInfo.setCertificateName(rs.getString("CERTIFICATE_NAME"));
				}else{
					skillInfo.setCertificateName("");
				}
				if(rs.getString("SCORE")!=null){
					skillInfo.setScore(rs.getString("SCORE"));
				}else{
					skillInfo.setScore("");
				}*/
				
				skillInfo.setCertificateName(rs.getString("CERTIFICATE_NAME")!=null?rs.getString("CERTIFICATE_NAME"):"");
				skillInfo.setScore(rs.getString("SCORE")!=null?rs.getString("SCORE"):"");
				
				skillInfo.setWorkLocation(rs.getString("WORK_LOCATION")!=null?rs.getString("WORK_LOCATION"):"");
				skillInfo.setCertDate(rs.getString("CERTIFICATION_DATE")!=null?rs.getString("CERTIFICATION_DATE"):"");
				skillInfo.setClear(rs.getString("CLEARED_FLAG")!=null?rs.getString("CLEARED_FLAG"):"");
				skillInfo.setSection1Score(rs.getString("SECTION1_SCORE")!=null?rs.getString("SECTION1_SCORE"):"");
				skillInfo.setSection2Score(rs.getString("SECTION2_SCORE")!=null?rs.getString("SECTION2_SCORE"):"");
				skillInfo.setSection3Score(rs.getString("SECTION3_SCORE")!=null?rs.getString("SECTION3_SCORE"):"");
				skillInfo.setSection4Score(rs.getString("SECTION4_SCORE")!=null?rs.getString("SECTION4_SCORE"):"");
				skillInfo.setSection5Score(rs.getString("SECTION5_SCORE")!=null?rs.getString("SECTION5_SCORE"):"");
				skillInfo.setSection6Score(rs.getString("SECTION6_SCORE")!=null?rs.getString("SECTION6_SCORE"):"");
				skillInfo.setUploadDate(rs.getString("UPLOAD_DATE")!=null?rs.getString("UPLOAD_DATE"):"");
				//skillInfo.setScore(rs.getString("SCORE"));
				//skillInfo.setFormNo(rs.getString("FORM_NO"));
				//skillInfo.setFormType(rs.getString("FORM_TYPE"));
				//skillInfo.setPortfolio(rs.getString("PORTFOLIO"));
				//skillInfo.setMultiState(rs.getString("MULTI_STATE"));
				//skillInfo.setMandatory(rs.getString("MANDATORY"));
				//skillInfo.setSource(rs.getString("SOURCE"));
				/*
				boolean isDocAvailable = true;
				java.sql.Blob blob=null;
				//System.out.println("check source doc");
				if(rs.getBlob("CERTIFICATE")!=null){
					blob = rs.getBlob("CERTIFICATE");
					InputStream inputStream = blob.getBinaryStream();
					if (inputStream == null) {
						//System.out.println("Doc is Null");
						isDocAvailable = false;
					} else if (inputStream != null && inputStream.available() <= 0) {
						//System.out.println("inputStream.available() "+ inputStream.available());
						isDocAvailable = false;
					} else {
						isDocAvailable = true;
					}
					//System.out.println("isDocAvailable " + isDocAvailable);
					skillInfo.setDocAvailable(isDocAvailable);
					skillInfo.setDocExtention(rs.getString("CERTIFICATE_EXTN"));
					inputStream.close();
				}*/
				boolean isPdfAvailable = true;
				java.sql.Blob blobPdf=null;
				if(rs.getBlob("CERTIFICATE")!=null){
					blobPdf = rs.getBlob("CERTIFICATE");
					InputStream inputStreamPdf = blobPdf.getBinaryStream();
					if (inputStreamPdf == null) {
						//System.out.println("Pdf is Null");
						isPdfAvailable = false;
					} else if (inputStreamPdf != null
							&& inputStreamPdf.available() <= 0) {
						//System.out.println("inputStreamPdf.available() "+ inputStreamPdf.available());
						isPdfAvailable = false;
					} else {
						isPdfAvailable = true;
					}
					//System.out.println("isPdfAvailable " + isPdfAvailable);
					skillInfo.setCertificateAvailable(isPdfAvailable);
					inputStreamPdf.close();
				}

				policies.add(skillInfo);
			}
			//rs.close();
			//statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
        	close(rs,statement,connection);
        }
		return policies;
	}	

	public SkillInfo downloadDocument(String docType, String enterprizeId) {
		SkillInfo policyInfoDoc = new SkillInfo();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			if ((connection == null) || connection.isClosed() ) {
				//con.DriverManager.getConnection(...);
				connection =  DataBase.getInstance().getConnection();
		    }
			String strSqlDoc = null;
			if (docType.equalsIgnoreCase("doc")) {
				strSqlDoc = "SELECT \"CERTIFICATE\",\"CERTIFICATE_EXTN\" FROM \"MULTI_SKILLING_DATA\" WHERE \"ENTERPRIZE_ID\" =?";
				pstmt = connection.prepareStatement(strSqlDoc);
				pstmt.setString(1, enterprizeId);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					java.sql.Blob blob = rs.getBlob(1);
					//inputStream = blob.getBinaryStream();
					policyInfoDoc.setCertificate(blob.getBinaryStream());
					policyInfoDoc.setCertificateExtn(rs.getString("CERTIFICATE_EXTN"));
					//System.out.println("Blob Test##%%");
				}
				//rs.close();
				//pstmt.close();
			} else {
				strSqlDoc = "SELECT \"CERTIFICATE\" FROM \"MULTI_SKILLING_DATA\" WHERE \"ENTERPRIZE_ID\" =?";
				pstmt = connection.prepareStatement(strSqlDoc);
				pstmt.setString(1, enterprizeId);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					System.out.println("Git the blob");
					java.sql.Blob blob = rs.getBlob(1);
					//inputStream = blob.getBinaryStream();
					policyInfoDoc.setCertificate(blob.getBinaryStream());
					//System.out.println("Blob Test##%%");
				}
				rs.close();
				pstmt.close();
			}
			System.out.println("strSqlDoc and Ent ID"+strSqlDoc);
			//connection.commit();
		} catch (SQLException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}finally{
        	close(rs,pstmt,connection);
        }
		return policyInfoDoc;
	}
	
	public List<SkillsInfo> getFormDataBySearch(SkillsInfo skillDetails) {
		List<SkillsInfo> skills = new ArrayList<SkillsInfo>();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			if ((connection == null) || connection.isClosed() ) {
				//con.DriverManager.getConnection(...);
				connection =  DataBase.getInstance().getConnection();
		    }
			StringBuffer queryString = new StringBuffer();			
			//queryString.append("select * FROM \"MULTI_SKILLING_DATA\" WHERE \"EMPLOYEE_ROLE\"='user' AND \"CERT_UPLOAD_FLAG\"='YES' AND \"ENTERPRIZE_ID\" like '%"+enterprizeId+"%'");
			/*queryString.append("select * FROM \"MULTI_SKILLING_DATA\" WHERE \"EMPLOYEE_ROLE\"='user' AND \"ENTERPRIZE_ID\" like '%"+enterprizeId+"%'");
			ps = connection.prepareStatement(queryString
					.toString());
			rs = ps.executeQuery();*/
			
			
			List<String> bindVariables = new ArrayList<>();

			queryString.append("select * FROM \"MULTI_SKILLING_DATA\" where CERT_UPLOAD_FLAG='YES' ");

			if (skillDetails.getEnterprizeId() != null && !skillDetails.getEnterprizeId().equals("")) {
				queryString.append(" and \"ENTERPRIZE_ID\"=?");
				bindVariables.add(skillDetails.getEnterprizeId());
			}
			
			if (skillDetails.getExpertSkills() != null && !skillDetails.getExpertSkills().equals("")) {
				queryString.append(" and \"EXPERT_SKILLS\"=?");
				bindVariables.add(skillDetails.getExpertSkills());
			}

			if (skillDetails.getSupSkills() != null && !skillDetails.getSupSkills().equals("")) {
				queryString.append(" and \"SUPPLIMENTORY_SKILLS\"=?");
				bindVariables.add(skillDetails.getSupSkills());
			}

			if (skillDetails.getWorkLocation() != null
					&& !skillDetails.getWorkLocation().equals("")) {
				queryString.append(" and \"COUNTRY\"=?");
				bindVariables.add(skillDetails.getWorkLocation());
			}
			
			ps = connection.prepareStatement(queryString
					.toString());
			//System.out.println("queryString.toString()##"+ queryString.toString());

			for (int i = 0; i < bindVariables.size(); i++) {
				// variables are indexed from 1 in JDBC
				ps.setString(i + 1, bindVariables.get(i));
			}
			rs = ps.executeQuery();
			
			
			System.out.println("DAO Query String "+queryString.toString());
			while (rs.next()) {
				SkillsInfo skillInfo = new SkillsInfo();
				skillInfo.setEmployeeName(rs.getString("EMPLOYEE_NAME"));
				skillInfo.setEnterprizeId(rs.getString("ENTERPRIZE_ID"));
				skillInfo.setExpertSkills(rs.getString("EXPERT_SKILLS"));
				skillInfo.setSupSkills(rs.getString("SUPPLIMENTORY_SKILLS"));
				skillInfo.setCountry(rs.getString("COUNTRY"));
				skillInfo.setWorkLocation(rs.getString("WORK_LOCATION")!=null?rs.getString("WORK_LOCATION"):"");
				skillInfo.setCertificationObtained(rs.getString("CERTIFICATION_OBTAINED")!=null?rs.getString("CERTIFICATION_OBTAINED"):"");
				skillInfo.setCertificationPlanned(rs.getString("CERTIFICATION_PLANNED")!=null?rs.getString("CERTIFICATION_PLANNED"):"");
				skillInfo.setTeamName(rs.getString("TEAM_NAME")!=null?rs.getString("TEAM_NAME"):"");
				skillInfo.setMentorEntId(rs.getString("MENTOR_ENT_ID")!=null?rs.getString("MENTOR_ENT_ID"):"");
				skillInfo.setPointOfContact(rs.getString("POINT_OF_CONTACT")!=null?rs.getString("POINT_OF_CONTACT"):"");
				skillInfo.setComments(rs.getString("COMMENTS")!=null?rs.getString("COMMENTS"):"");
				
				skills.add(skillInfo);
			}
			rs.close();
			ps.close();
		} catch (SQLException ex) {
			System.out.println("SQL Error in check() -->" + ex);
		} catch (Exception ex) {
			System.out.println("Java Error in check() -->" + ex);
		}finally{
        	close(rs,ps,connection);
        }
		return skills;
	}
	
	public List<SkillInfo> getFormDataByEntId(String enterprizeId) {
		List<SkillInfo> policies = new ArrayList<SkillInfo>();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			if ((connection == null) || connection.isClosed() ) {
				//con.DriverManager.getConnection(...);
				connection =  DataBase.getInstance().getConnection();
		    }
			StringBuffer queryString = new StringBuffer();			
			queryString.append("select * FROM \"MULTI_SKILLING_DATA\" WHERE \"EMPLOYEE_ROLE\"='user' AND \"ENTERPRIZE_ID\"='"+enterprizeId+"'");
			ps = connection.prepareStatement(queryString
					.toString());
			rs = ps.executeQuery();
			System.out.println("DAO Query String "+queryString.toString());
			while (rs.next()) {
				SkillInfo skillInfo = new SkillInfo();
				skillInfo.setEmployeeName(rs.getString("EMPLOYEE_NAME"));
				skillInfo.setEnterprizeId(rs.getString("ENTERPRIZE_ID"));
				skillInfo.setEmployeeId(rs.getString("EMPLOYEE_ID"));
				skillInfo.setSkillRole(rs.getString("SKILL_ROLE"));
				skillInfo.setEmployeeRole(rs.getString("EMPLOYEE_ROLE"));
				//skillInfo.setScore(rs.getString("SCORE"));
				/*if(rs.getString("CERTIFICATE_NAME")!=null){
					skillInfo.setCertificateName(rs.getString("CERTIFICATE_NAME"));
				}else{
					skillInfo.setCertificateName("");
				}
				if(rs.getString("SCORE")!=null){
					skillInfo.setScore(rs.getString("SCORE"));
				}else{
					skillInfo.setScore("");
				}*/
				skillInfo.setCertificateName(rs.getString("CERTIFICATE_NAME")!=null?rs.getString("CERTIFICATE_NAME"):"");
				skillInfo.setScore(rs.getString("SCORE")!=null?rs.getString("SCORE"):"");
				
				skillInfo.setWorkLocation(rs.getString("WORK_LOCATION")!=null?rs.getString("WORK_LOCATION"):"");
				skillInfo.setCertDate(rs.getString("CERTIFICATION_DATE")!=null?rs.getString("CERTIFICATION_DATE"):"");
				skillInfo.setClear(rs.getString("CLEARED_FLAG")!=null?rs.getString("CLEARED_FLAG"):"");
				skillInfo.setSection1Score(rs.getString("SECTION1_SCORE")!=null?rs.getString("SECTION1_SCORE"):"");
				skillInfo.setSection2Score(rs.getString("SECTION2_SCORE")!=null?rs.getString("SECTION2_SCORE"):"");
				skillInfo.setSection3Score(rs.getString("SECTION3_SCORE")!=null?rs.getString("SECTION3_SCORE"):"");
				skillInfo.setSection4Score(rs.getString("SECTION4_SCORE")!=null?rs.getString("SECTION4_SCORE"):"");
				skillInfo.setSection5Score(rs.getString("SECTION5_SCORE")!=null?rs.getString("SECTION5_SCORE"):"");
				skillInfo.setSection6Score(rs.getString("SECTION6_SCORE")!=null?rs.getString("SECTION6_SCORE"):"");
				skillInfo.setUploadDate(rs.getString("UPLOAD_DATE")!=null?rs.getString("UPLOAD_DATE"):"");
				boolean isPdfAvailable = true;
				java.sql.Blob blobPdf=null;
				if(rs.getBlob("CERTIFICATE")!=null){
					blobPdf = rs.getBlob("CERTIFICATE");
					InputStream inputStreamPdf = blobPdf.getBinaryStream();
					if (inputStreamPdf == null) {
						//System.out.println("Pdf is Null");
						isPdfAvailable = false;
					} else if (inputStreamPdf != null
							&& inputStreamPdf.available() <= 0) {
						//System.out.println("inputStreamPdf.available() "+ inputStreamPdf.available());
						isPdfAvailable = false;
					} else {
						isPdfAvailable = true;
					}
					//System.out.println("isPdfAvailable " + isPdfAvailable);
					skillInfo.setCertificateAvailable(isPdfAvailable);
					inputStreamPdf.close();
				}
				policies.add(skillInfo);
			}
			rs.close();
			ps.close();
		} catch (SQLException ex) {
			System.out.println("SQL Error in check() -->" + ex);
		} catch (Exception ex) {
			System.out.println("Java Error in check() -->" + ex);
		}finally{
        	close(rs,ps,connection);
        }
		return policies;
	}

	/*public void uploadForm(PolicyInfo policyInfo) throws FormsExplorerException {
		PreparedStatement pstmt=null;
		try {
			PolicyInfo pInfo = policyInfo;			
				String insertStr = "INSERT INTO \"FORMS_EXPLORER_DATA\" (\"FORM_NO\",\"DESCRIPTION\",\"BUSINESS_TYPE\",\"LOB\",\"MULTI_STATE\",\"STATE\",\"FORM_TYPE\",\"MANDATORY\","
						+ "\"PORTFOLIO\",\"SOURCE\",\"SOURCE_DOC\",\"SOURCE_PDF\",\"DOC_EXTENTION\")"
						+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
				pstmt = connection.prepareStatement(insertStr);
				System.out.println("inserting to DB uploadDocuments@:");
	
				pstmt.setString(1, pInfo.getFormNo());
				pstmt.setString(2, pInfo.getDescription());
				pstmt.setString(3, pInfo.getBusinessType());
				pstmt.setString(4, pInfo.getLob());
				pstmt.setString(5, pInfo.getMultiState());
				pstmt.setString(6, pInfo.getState());
				pstmt.setString(7, pInfo.getFormType());
				pstmt.setString(8, pInfo.getMandatory());
				pstmt.setString(9, pInfo.getPortfolio());
				pstmt.setString(10, pInfo.getSource());
				InputStream docStream = pInfo.getDocStream();
				int docLength = 0;
	
				if (docStream != null) {
					docLength = docStream.available();
					// fetches input stream of the upload file for the blob column
					pstmt.setBinaryStream(11, docStream, docLength);
					//docStream.close();
					// pstmt.setObject(1, inputStream,length);
				}
	
				InputStream pdfStream = pInfo.getPdfStream();
				int pdfLength = 0;
				if (pdfStream != null) {
					pdfLength = pdfStream.available();
					// fetches input stream of the upload file for the blob column
					pstmt.setBinaryStream(12, pdfStream, pdfLength);
					//pdfStream.close();
					// pstmt.setObject(1, inputStream,length);
				}
				
				pstmt.setString(13, pInfo.getDocExtention());
				// sends the statement to the database server
				//int row = pstmt.executeUpdate();
				pstmt.executeUpdate();
				
		} catch (SQLException e) {
			
			throw new FormsExplorerException(e.getErrorCode());
			
		} catch (Exception e) {
			System.out.println(e);
		}finally{
			close(null,pstmt);
		}
		//return successFlag;
	}*/

	public boolean validateUser(String userId, String password) throws MultiSkillException  {
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			if ((connection == null) || connection.isClosed() ) {
				//con.DriverManager.getConnection(...);
				connection =  DataBase.getInstance().getConnection();
		    }
			ps = connection
					.prepareStatement("select * from MULTI_SKILLING_DATA where ENTERPRIZE_ID=? and EMPLOYEE_ID=? ");
			ps.setString(1, userId);
			ps.setString(2, password);
			rs = ps.executeQuery();
			if (rs.next()){
				return true;
			} else {
				return false;
			}
		} catch (SQLException ex) {
			System.out.println("Error in check() -->" + ex.getMessage());
			throw new MultiSkillException(ex.getErrorCode());
		}finally{
			close(rs,ps,connection);
		}
		//return false;
	}
	
	public SkillInfo getUserDetails(String userId) {
		PreparedStatement ps=null;
		ResultSet rs=null;
		SkillInfo skillInfo=new SkillInfo();
		try {
			if ((connection == null) || connection.isClosed() ) {
				//con.DriverManager.getConnection(...);
				connection =  DataBase.getInstance().getConnection();
		    }
			ps = connection
					.prepareStatement("select * from \"MULTI_SKILLING_DATA\" where \"ENTERPRIZE_ID\"=?");
			ps.setString(1, userId);
			//ps.setString(2, password);
			rs = ps.executeQuery();
			while (rs.next()){
				skillInfo.setEmployeeName(rs.getString("EMPLOYEE_NAME"));
				skillInfo.setEnterprizeId(rs.getString("ENTERPRIZE_ID"));
				skillInfo.setEmployeeId(rs.getString("EMPLOYEE_ID"));
				skillInfo.setEmployeeRole(rs.getString("EMPLOYEE_ROLE"));
				skillInfo.setSkillRole(rs.getString("SKILL_ROLE"));
				skillInfo.setCertUploadFlag(rs.getString("CERT_UPLOAD_FLAG"));
				skillInfo.setWorkLocation(rs.getString("WORK_LOCATION"));
			} 
			System.out.println("skillInfo##"+skillInfo);
		} catch (Exception ex) {
			System.out.println("Error in check() -->" + ex.getMessage());
		}finally{
			close(rs,ps,connection);
		}
		return skillInfo;
	}
	
	public void close(ResultSet rs, PreparedStatement pstmt, Connection conn){
		if (rs!=null){
			try{
				rs.close();
			}
			catch(SQLException e){
				System.out.println("The result set cannot be closed."+e);
			}
		}
		if (pstmt != null){
			try{
				pstmt.close();
			} catch (SQLException e){
				System.out.println("The prepared statement cannot be closed."+e);
			}
		}
		if(conn != null){
			try{
				conn.close();
			} catch (SQLException e){
				System.out.println("The data source connection cannot be closed."+e);
			}
		}

	}
	public void close(ResultSet rs, PreparedStatement pstmt){
		if (rs!=null){
			try{
				rs.close();
			}
			catch(SQLException e){
				System.out.println("The result set cannot be closed."+e);
			}
		}else if (pstmt != null){
			try{
				pstmt.close();
			} catch (SQLException e){
				System.out.println("The prepared statement cannot be closed."+e);
			}
		}
	}
}
