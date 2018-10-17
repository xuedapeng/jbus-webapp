package com.moqbus.app.common.helper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class NumericHelper {
	public static boolean isInteger(String str) {  
		  
		if (StringUtils.isEmpty(str)) {
			  return false;
		}
		  
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
		return pattern.matcher(str).matches();  
	}
	
	 public static String formatDouble2(double d) {
		 DecimalFormat df = new DecimalFormat("0.00");

	        
	     return df.format(d);
    }
}
