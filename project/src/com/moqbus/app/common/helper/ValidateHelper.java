package com.moqbus.app.common.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ValidateHelper {

	// return: error names
	public static String notEmptyCheck(String...fieldNameValues) {
		
		List<String> errorNames = new ArrayList<String>();
		
		for (int i=0; i<fieldNameValues.length; i++) {
			if (i%2==0) {
				continue;
			}
			if (StringUtils.isEmpty(fieldNameValues[i])) {
				errorNames.add(fieldNameValues[i-1]);
			}
		}
		
		return errorNames.stream().reduce("", (acc, element)->acc + (acc.isEmpty()?"":",") + element);
	}
	

	// return: error names
	public static String integerCheck(String...fieldNameValues) {
		
		List<String> errorNames = new ArrayList<String>();
		
		for (int i=0; i<fieldNameValues.length; i++) {
			if (i%2==0) {
				continue;
			}
			if (!NumericHelper.isInteger(fieldNameValues[i])) {
				errorNames.add(fieldNameValues[i-1]);
			}
		}
		
		return errorNames.stream().reduce("", (acc, element)->acc + (acc.isEmpty()?"":",") + element);
	}
}
