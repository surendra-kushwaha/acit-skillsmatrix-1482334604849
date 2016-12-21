package com.acit.multiskilling.exception;


public class MultiSkillException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8879126149855934809L;
	
	private int errorCode;
	private String errorMessage;
	private String errorField;
	public MultiSkillException(){
		
	}
	
	public MultiSkillException(int errorCode){
		this.errorCode=errorCode;
	}
	
	public MultiSkillException(int errorCode ,String errorMessage){
		this.errorCode=errorCode;
		this.errorMessage=errorMessage;
		
	}
	
	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * @return the errorField
	 */
	public String getErrorField() {
		return errorField;
	}
	/**
	 * @param errorField the errorField to set
	 */
	public void setErrorField(String errorField) {
		this.errorField = errorField;
	}
	

}