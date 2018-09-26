package com.moqbus.app.logic.sysman.param;

import com.moqbus.app.logic.BaseZLogicParam;

public class UserAddLogicParam extends BaseZLogicParam {

	public UserAddLogicParam() {
		
	}
	
	private String account;
	private String password;
	private String nickName;
	private String mobile;
	private String email;
	private String sysAdmin;
	private String branchId;
	private String branchRole;
	
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getSysAdmin() {
		return sysAdmin;
	}
	public void setSysAdmin(String sysAdmin) {
		this.sysAdmin = sysAdmin;
	}
	public String getBranchRole() {
		return branchRole;
	}
	public void setBranchRole(String branchRole) {
		this.branchRole = branchRole;
	}
	
	

}
