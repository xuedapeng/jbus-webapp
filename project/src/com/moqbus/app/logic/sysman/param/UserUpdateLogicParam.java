package com.moqbus.app.logic.sysman.param;

import com.moqbus.app.logic.BaseZLogicParam;

public class UserUpdateLogicParam extends BaseZLogicParam {

	public UserUpdateLogicParam() {
		
	}
	
	private String userId;
	private String account;
	private String password;
	private String nickName;
	private String mobile;
	private String email;
	private String sysAdmin;
	private String branchId;
	private String branchRole;
	private String status;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getSysAdmin() {
		return sysAdmin;
	}
	public void setSysAdmin(String sysAdmin) {
		this.sysAdmin = sysAdmin;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getBranchRole() {
		return branchRole;
	}
	public void setBranchRole(String branchRole) {
		this.branchRole = branchRole;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

}
