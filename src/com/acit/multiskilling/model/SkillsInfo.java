package com.acit.multiskilling.model;

import java.io.InputStream;

public class SkillsInfo {
	
	String enterpriseId;
	String employeeName;
	String expertSkills;
	String supSkills;
	String country;
	String certificationObtained;
	String certificationPlanned;
	//boolean isDocAvailable;
	String mentorEntId;
	//boolean isCertificateAvailable;
	String workLocation;
	String teamName;
	String pointOfContact;
	String updateDate;
	String comments;
	
	/**
	 * @return the enterprizeId
	 */
	public String getEnterpriseId() {
		return enterpriseId;
	}

	/**
	 * @param enterprizeId the enterprizeId to set
	 */
	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getExpertSkills() {
		return expertSkills;
	}

	public void setExpertSkills(String expertSkills) {
		this.expertSkills = expertSkills;
	}

	public String getSupSkills() {
		return supSkills;
	}

	public void setSupSkills(String supSkills) {
		this.supSkills = supSkills;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCertificationObtained() {
		return certificationObtained;
	}

	public void setCertificationObtained(String certificationObtained) {
		this.certificationObtained = certificationObtained;
	}

	public String getCertificationPlanned() {
		return certificationPlanned;
	}

	public void setCertificationPlanned(String certificationPlanned) {
		this.certificationPlanned = certificationPlanned;
	}

	public String getMentorEntId() {
		return mentorEntId;
	}

	public void setMentorEntId(String mentorEntId) {
		this.mentorEntId = mentorEntId;
	}

	public String getWorkLocation() {
		return workLocation;
	}

	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getPointOfContact() {
		return pointOfContact;
	}

	public void setPointOfContact(String pointOfContact) {
		this.pointOfContact = pointOfContact;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	

}
