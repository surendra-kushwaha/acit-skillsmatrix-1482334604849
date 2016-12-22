package com.acit.myalliance.util;

public class Main{
    public static void main(String[] args) {
        System.out.println("'" + stringToHtmlString("1&May2016") + "'");
        String userid="surendra.kushwaha@accenture.com";
        if(userid.contains("@accenture.com")){
        userid=userid.substring(0,userid.lastIndexOf("@"));
        }
        System.out.println("userid ::"+userid);
        
        /*System.out.println("'" + stringToHtmlString("<") + "'");
        System.out.println("'" + stringToHtmlString(">") + "'");
        System.out.println("'" + stringToHtmlString(" ") + "'");
        System.out.println("'" + stringToHtmlString("     ") + "'");
        System.out.println("'" + stringToHtmlString("&<>abc") + "'");
        System.out.println("'" + stringToHtmlString("abc&<>") + "'");*/
    }
    public static final String stringToHtmlString(String s){
       StringBuffer sb = new StringBuffer();
       int n = s.length();
       for (int i = 0; i < n; i++) {
          char c = s.charAt(i);
          switch (c) {
             case '<': sb.append("&lt;"); break;
             case '>': sb.append("&gt;"); break;
             case '&': sb.append("&amp;"); break;
             case '"': sb.append("&quot;"); break;
             default:  sb.append(c); break;
          }
       }
       return sb.toString();
    }
}
