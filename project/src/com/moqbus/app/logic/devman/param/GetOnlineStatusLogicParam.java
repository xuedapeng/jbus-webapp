package com.moqbus.app.logic.devman.param;

import java.util.List;

import com.moqbus.app.logic.BaseZLogicParam;

public class GetOnlineStatusLogicParam extends BaseZLogicParam {

	public GetOnlineStatusLogicParam() {
		
	}
	
	private List<String> deviceSnList;

	public List<String> getDeviceSnList() {
		return deviceSnList;
	}

	public void setDeviceSnList(List<String> deviceSnList) {
		this.deviceSnList = deviceSnList;
	}
	
}
