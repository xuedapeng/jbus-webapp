package com.moqbus.app.logic.sysman.param;

import com.moqbus.app.logic.BaseZLogicParam;

public class PrivilegeGetLogicParam extends BaseZLogicParam {

	public PrivilegeGetLogicParam() {
		
	}
	
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
