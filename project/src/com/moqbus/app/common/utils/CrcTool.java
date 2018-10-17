package com.moqbus.app.common.utils;

import com.moqbus.app.common.helper.HexHelper;

public class CrcTool {
	/**
     * 计算CRC16校验码
     *
     * @param bytes 字节数组
     * @return {@link String} 校验码
     * @since 1.0
     */
    public static String getModbusCRC16(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        
        return HexHelper.intTo2BytesStr(CRC);
    }
    
    public static byte[] appendModbusCRC16(byte[] bytes) {
    	return HexHelper.hexStringToBytes(
    			HexHelper.bytesToHexString(bytes) + HexHelper.reverseBit(getModbusCRC16(bytes)));
    	
    }
    
    public static void main(String[] args) {
    	String s = "01 03 06 14 00 08";
    	byte[] b = HexHelper.hexStringToBytes(s);
    	System.out.println(HexHelper.bytesToHexString(appendModbusCRC16(b)));
    }

}
