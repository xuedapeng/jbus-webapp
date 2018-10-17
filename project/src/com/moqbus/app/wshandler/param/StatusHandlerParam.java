package com.moqbus.app.wshandler.param;

import java.util.List;
import java.util.Map;

import com.moqbus.app.wshandler.base.BaseHandlerParam;

import fw.jbiz.common.helper.JsonHelper;
import fw.jbiz.ext.websocket.ZWsHandlerParam;

public class StatusHandlerParam extends BaseHandlerParam {

	private String secretId;
	private String secretKey;
	private List<String> deviceSnList;
	
	@SuppressWarnings("unchecked")
	@Override
	public ZWsHandlerParam fromJson(String jsonMsg) {
		
		Map<String, Object> map = JsonHelper.jsonStr2Map(jsonMsg);
		secretId = (String)map.get("secretId");
		secretKey = (String)map.get("secretKey");
		deviceSnList = (List<String>)map.get("deviceSnList");
		
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
	public List<String> getDeviceSnList() {
		return deviceSnList;
	}
	public void setDeviceSnList(List<String> deviceSnList) {
		this.deviceSnList = deviceSnList;
	}
	
}
