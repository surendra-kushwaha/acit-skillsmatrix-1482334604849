package com.acit.multiskilling.util;

import java.security.spec.AlgorithmParameterSpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class Utility {	
	 public static void main(String a[]){
		 	//String uname=decrypt("xxIzahJDpHvaL4rW9AGRj%2Bw%2Fg0KHYChH%2Fib8gwy%2BO1ZRJbGH3dV%2B%2B4BdTG5%2FzhKVxKZuiwGXe3JYjVM3MmaDJw%3D%3D");
			//String pwd=Utility.decrypt("zJqhOBFwwkCIUsTpg6BQawdLUfXUnsoQjKkMJNfrtbo%3D");
			//System.out.println(uname);
			//System.out.println(pwd);
		 SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
	     System.out.println(dt.format(new Date()));
	     
		 System.out.println(new Date());
	 }
	 public static String getProperties(String key){
	    	ResourceBundle bundle = ResourceBundle.getBundle("resources.config", Locale.US);
	    	return bundle.getString(key);		
	 }
	 
	 public String getTestProperties(String key){
	    	ResourceBundle bundle = ResourceBundle.getBundle("resources.config", Locale.US);
	    	return bundle.getString(key);		
	 }
	 
	 public static String decrypt(final String encrypted) {
		    try {
		        SecretKey key = new SecretKeySpec(Base64.decodeBase64("u/Gu5posvwDsXUnV5Zaq4g=="), "AES");
		        AlgorithmParameterSpec iv = new IvParameterSpec(Base64.decodeBase64("5D9r9ZVzEYYgha93/aUK2w=="));
		        byte[] decodeBase64 = Base64.decodeBase64(encrypted);
		        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		        cipher.init(Cipher.DECRYPT_MODE, key, iv);
		        String hexString=new String(cipher.doFinal(decodeBase64));
		        byte[] bytes = Hex.decodeHex(hexString.toCharArray());
		        return new String(bytes, "UTF-8");
		    } catch (Exception e) {
		        throw new RuntimeException("Runtime Exception:", e);
		    }
		}
}
