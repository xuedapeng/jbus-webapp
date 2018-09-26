package com.moqbus.app.logic.sysman.param;

import com.moqbus.app.logic.BaseZLogicParam;

public class DeviceDelLogicParam extends BaseZLogicParam {

	public DeviceDelLogicParam() {
		
	}
	
	private String deviceId;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
