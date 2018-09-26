package com.moqbus.app.service.mqtt;

import java.util.HashMap;
import java.util.Map;

import com.moqbus.app.common.helper.HexHelper;

public class GateData {
	
	public static Map<String, String> decodeData(byte[] data, String cmdPre) {
		
		Map<String, String> rsMap = new HashMap<String, String>();
		
		if (GateCmd.PRE_GET_HEIGHT.equals(cmdPre)) {
			rsMap.put("type","GET_HEIGHT");
			parseHeight(data, rsMap);
		}
		
		if (GateCmd.PRE_GET_FLOW.equals(cmdPre)) {
			rsMap.put("type","GET_FLOW");
			parseFlow(data, rsMap);
		}
		
		return rsMap;
		
	}

	private static void parseFlow(byte[] data, Map<String, String> rsMap) {

		byte[] bHeight = {data[3], data[4]};
		rsMap.put("flow", String.valueOf(HexHelper.byte2ToInt(bHeight)));
	}
	
	private static void parseHeight(byte[] data, Map<String, String> rsMap) {

		byte[] bHeight = {data[3], data[4]};
		rsMap.put("height", String.valueOf(HexHelper.byte2ToInt(bHeight)));
	}
}
