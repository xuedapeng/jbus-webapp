package com.moqbus.app.service.mqtt;

import com.moqbus.app.common.helper.HexHelper;
import com.moqbus.app.common.utils.CrcTool;

public class GateCmd {


	final static String PRE_CMD_SET_HEIGHT = "01 06 11 C2";
	final static String PRE_CMD_MOVE_TO_HEIGHT = "01 05 08 00 FF 00";
	final static String PRE_CMD_GET_INFO = "01 03 11 2C 00 12";
	final static String PRE_CMD_STOP = "01 05 08 11 FF 00";
	
	public static byte[] SET_HEIGHT(int height) {

		String cmdStr = PRE_CMD_SET_HEIGHT + HexHelper.intTo2BytesStr(height);
		byte[] cmd = HexHelper.hexStringToBytes(cmdStr); 

		return CrcTool.appendModbusCRC16(cmd);
	}

	public static byte[] MOVE_TO_HEIGHT() {

		String cmdStr = PRE_CMD_MOVE_TO_HEIGHT;
		byte[] cmd = HexHelper.hexStringToBytes(cmdStr); 

		return CrcTool.appendModbusCRC16(cmd);
	}
	
	public static byte[] GET_INFO() {

		String cmdStr = PRE_CMD_GET_INFO;
		byte[] cmd = HexHelper.hexStringToBytes(cmdStr); 

		return CrcTool.appendModbusCRC16(cmd);
	}
	
	public static byte[] STOP() {

		String cmdStr = PRE_CMD_STOP;
		byte[] cmd = HexHelper.hexStringToBytes(cmdStr); 

		return CrcTool.appendModbusCRC16(cmd);
	}

	
}
