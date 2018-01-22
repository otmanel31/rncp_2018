package com.otmanel.firstJunit;

public class TextUtils {

	//capitalise un e chaine donc p1ere lettre en maj
	public String capitalize(String txt) {
		if (txt == null) return null ;
		if (txt.length() == 0) return "";
		//return Character.toUpperCase(txt.charAt(0)) + txt.substring(1);
		return txt.substring(0,1).toUpperCase() + txt.substring(1);
	}
	
	public String inverse(String txt) {
		if (txt == null) return null ;
		if (txt.length() == 0) return "";
		StringBuilder sb = new StringBuilder(txt.length());
		// return sb.reverse().toString();
		
		for (int i = txt.length() - 1; i>=0; i-- ) {
			sb.append(txt.charAt(i));
		}
		return sb.toString();
	}
}
