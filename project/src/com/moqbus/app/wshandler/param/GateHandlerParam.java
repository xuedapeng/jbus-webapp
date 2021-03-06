package com.moqbus.app.wshandler.param;

import java.util.Map;

import com.moqbus.app.wshandler.base.BaseHandlerParam;

import fw.jbiz.common.helper.JsonHelper;
import fw.jbiz.ext.websocket.ZWsHandlerParam;

public class GateHandlerParam extends BaseHandlerParam {

	private String secretId;
	private String secretKey;
	private String deviceSn;
	private String cmd;
	private Integer height;
	
	@Override
	public ZWsHandlerParam fromJson(String jsonMsg) {
		
		Map<String, Object> map = JsonHelper.jsonStr2Map(jsonMsg);
		secretId = (String)map.get("secretId");
		secretKey = (String)map.get("secretKey");
		deviceSn = (String)map.get("deviceSn");
		cmd = (String)map.get("cmd");
		String heightStr = (String)map.get("height");
		
		if (heightStr != null) {
			height = Integer.valueOf(heightStr);
		}
		
		
		
		return this;
	}
	
	

	public String getSecretId() {
		return secretId;
	}



	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}



	public String getSecretKey() {
		return secretKey;
	}



	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}



	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}
	
}
