package com.moqbus.app.logic.user.param;

import com.moqbus.app.logic.BaseZLogicParam;

public class MyPasswordUpdateLogicParam extends BaseZLogicParam {

	private String userId;
	private String oldPassword;
	private String newPassword;
	
	public MyPasswordUpdateLogicParam() {
		
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
}
