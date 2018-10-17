package com.moqbus.app.service.mqtt;

import java.util.HashMap;
import java.util.Map;

import com.moqbus.app.common.helper.HexHelper;
import com.moqbus.app.common.helper.NumericHelper;

public class GateData {

	final static String PRE_DAT_GET_INFO = "01 03 24";
	
	public static Map<String, String> decodeData(byte[] data) {
		
		Map<String, String> rsMap = new HashMap<String, String>();
		
		String dataStr = HexHelper.bytesToHexString(data).toUpperCase();
		
		if (dataStr.startsWith(PRE_DAT_GET_INFO)) {
			rsMap.put("type","GET_INFO");
			parseInfo(data, rsMap);
		}

		if (dataStr.startsWith(GateCmd.PRE_CMD_SET_HEIGHT)) {
			rsMap.put("type","RTN_OK");
			rsMap.put("cmd", "CMD_SET_HEIGHT");
		}
		
		if (dataStr.startsWith(GateCmd.PRE_CMD_MOVE_TO_HEIGHT)) {
			rsMap.put("type","RTN_OK");
			rsMap.put("cmd", "CMD_MOVE_TO_HEIGHT");
		}

		if (dataStr.startsWith(GateCmd.PRE_CMD_STOP)) {
			rsMap.put("type","RTN_OK");
			rsMap.put("cmd", "CMD_STOP");
		}
		
		return rsMap;
		
	}

	
	private static void parseInfo(byte[] data, Map<String, String> rsMap) {

		byte[] realHeight = {data[3], data[4]}; // 当前闸位
		byte[] expectHeight = {data[7], data[8]}; // 设定闸位
		byte[] moveSpeed = {data[11], data[12]}; // 闸门移动速度
		byte[] electricCurrent = {data[15], data[16]}; // 输出电流
		byte[] electricVoltage = {data[19], data[20]}; // 电池电压
		byte[] runHours = {data[23], data[24]}; // 累积运行时间（小时数）
		byte[] runMinutes = {data[27], data[28]}; // 累积运行时间（分钟数）	
		byte[] instantFlow = {data[31], data[32]}; // 瞬时流量
		byte[] totalFlow = {data[35], data[36]}; // 累积流量
		
		
		rsMap.put("realHeight", NumericHelper.formatDouble2(HexHelper.byte2ToInt(realHeight)/1000.0));
		rsMap.put("expectHeight", NumericHelper.formatDouble2(HexHelper.byte2ToInt(expectHeight)/1000.0));
		rsMap.put("moveSpeed", NumericHelper.formatDouble2(HexHelper.byte2ToInt(moveSpeed)));
		rsMap.put("electricCurrent", NumericHelper.formatDouble2(HexHelper.byte2ToInt(electricCurrent)/1000.0));
		rsMap.put("electricVoltage", NumericHelper.formatDouble2(HexHelper.byte2ToInt(electricVoltage)/1000.0));
		rsMap.put("runHours", String.valueOf(HexHelper.byte2ToInt(runHours)));
		rsMap.put("runMinutes", String.valueOf(HexHelper.byte2ToInt(runMinutes)));
		rsMap.put("instantFlow", NumericHelper.formatDouble2(HexHelper.byte2ToInt(instantFlow)/1000.0));
		rsMap.put("totalFlow", NumericHelper.formatDouble2(HexHelper.byte2ToInt(totalFlow)/1000.0));
	}
}
