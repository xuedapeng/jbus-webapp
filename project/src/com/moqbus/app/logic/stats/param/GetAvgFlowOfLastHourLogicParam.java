package com.moqbus.app.logic.stats.param;

import com.moqbus.app.logic.BaseZLogicParam;

public class GetAvgFlowOfLastHourLogicParam extends BaseZLogicParam {

	public GetAvgFlowOfLastHourLogicParam() {
		
	}

	private String deviceSn;

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}
}
