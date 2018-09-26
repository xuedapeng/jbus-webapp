package com.moqbus.app.logic.user.param;

import com.moqbus.app.logic.BaseZLogicParam;

public class LoginLogicParam extends BaseZLogicParam {

	private String account;
	private String password;
	
	public LoginLogicParam() {
		
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
