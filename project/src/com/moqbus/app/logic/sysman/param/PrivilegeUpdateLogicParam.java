package com.moqbus.app.logic.sysman.param;

import java.util.List;
import java.util.Map;

import com.moqbus.app.logic.BaseZLogicParam;

public class PrivilegeUpdateLogicParam extends BaseZLogicParam {

	public PrivilegeUpdateLogicParam() {
		
	}
	
	private String userId;
	private List<Map<String, String>> privileges; // [{"branchId":1, "privilege":2}, {"branchId":2, "privilege":1}]

	public List<Map<String, String>> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<Map<String, String>> privileges) {
		this.privileges = privileges;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
