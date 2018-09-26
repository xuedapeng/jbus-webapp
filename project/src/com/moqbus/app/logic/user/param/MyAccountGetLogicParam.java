package com.moqbus.app.logic.user.param;

import com.moqbus.app.logic.BaseZLogicParam;

public class MyAccountGetLogicParam extends BaseZLogicParam {

	private String userId;
	
	public MyAccountGetLogicParam() {
		
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
