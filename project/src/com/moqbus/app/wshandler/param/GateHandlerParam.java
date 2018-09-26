package com.moqbus.app.wshandler.param;

import java.util.Map;

import com.moqbus.app.wshandler.base.BaseHandlerParam;

import fw.jbiz.common.helper.JsonHelper;
import fw.jbiz.ext.websocket.ZWsHandlerParam;

public class GateHandlerParam extends BaseHandlerParam {

	private String userId;
	private String apiKey;
	private String deviceSn;
	private String cmd;
	private Integer height;
	
	@Override
	public ZWsHandlerParam fromJson(String jsonMsg) {
		
		Map<String, Object> map = JsonHelper.jsonStr2Map(jsonMsg);
		userId = (String)map.get("userId");
		apiKey = (String)map.get("apiKey");
		deviceSn = (String)map.get("deviceSn");
		cmd = (String)map.get("cmd");
		String heightStr = (String)map.get("height");
		
		if (heightStr != null) {
			height = Integer.valueOf(heightStr);
		}
		
		
		
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
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
