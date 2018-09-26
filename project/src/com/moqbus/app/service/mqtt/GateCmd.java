package com.moqbus.app.service.mqtt;

import com.moqbus.app.common.helper.HexHelper;
import com.moqbus.app.common.utils.CrcTool;

public class GateCmd {


	final static String PRE_SET_HEIGHT = "01 06 11 0A";
	final static String PRE_MOVE_TO_HEIGHT = "01 06 08 00";
	final static String PRE_GET_HEIGHT = "01 03 11 98";
	final static String PRE_GET_FLOW = "01 03 10 04";
	
	public static byte[] SET_HEIGHT(int height) {

		String cmdStr = PRE_SET_HEIGHT + HexHelper.intTo2BytesStr(height);
		byte[] cmd = HexHelper.hexStringToBytes(cmdStr); 

		return CrcTool.appendModbusCRC16(cmd);
	}

	public static byte[] MOVE_TO_HEIGHT() {

		String cmdStr = PRE_MOVE_TO_HEIGHT + HexHelper.intTo2BytesStr(1);
		byte[] cmd = HexHelper.hexStringToBytes(cmdStr); 

		return CrcTool.appendModbusCRC16(cmd);
	}
	
	public static byte[] GET_HEIGHT() {

		String cmdStr = PRE_GET_HEIGHT + HexHelper.intTo2BytesStr(2);
		byte[] cmd = HexHelper.hexStringToBytes(cmdStr); 

		return CrcTool.appendModbusCRC16(cmd);
	}

	public static byte[] GET_FLOW() {

		String cmdStr = PRE_GET_FLOW + HexHelper.intTo2BytesStr(2);
		byte[] cmd = HexHelper.hexStringToBytes(cmdStr); 

		return CrcTool.appendModbusCRC16(cmd);
	}
	
}
