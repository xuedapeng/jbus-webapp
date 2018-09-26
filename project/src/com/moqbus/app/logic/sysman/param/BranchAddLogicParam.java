package com.moqbus.app.logic.sysman.param;

import com.moqbus.app.logic.BaseZLogicParam;

public class BranchAddLogicParam extends BaseZLogicParam {

	public BranchAddLogicParam() {
		
	}
	
	private String branchName;
	private String description;
	
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
