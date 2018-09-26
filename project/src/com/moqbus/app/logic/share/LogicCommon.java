package com.moqbus.app.logic.share;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.moqbus.app.common.constant.StatusConst;
import com.moqbus.app.db.bean.BranchEntity;
import com.moqbus.app.db.bean.BranchUserEntity;
import com.moqbus.app.db.bean.UserEntity;
import com.moqbus.app.db.dao.BranchDao;
import com.moqbus.app.db.dao.BranchUserDao;
import com.moqbus.app.db.dao.UserDao;

public class LogicCommon {
	
	public static boolean isBranchAdmin(Integer userId, EntityManager em) {

		BranchUserEntity branchUser = new BranchUserDao(em).findByUserId(userId);
		boolean isBranchAdmin = false;
		
		if (branchUser != null && branchUser.getRole().equals(StatusConst.BRANCH_ROLE_ADMIN)) {
			isBranchAdmin = true;
		}
		
		return isBranchAdmin;
	}
	
	public static Integer getBranchRole(Integer userId, EntityManager em) {

		BranchUserEntity branchUser = new BranchUserDao(em).findByUserId(userId);
		
		if (branchUser != null) {
			return branchUser.getRole();
		}
		
		return StatusConst.BRANCH_ROLE_COMMON;
	}
	
	public static List<UserEntity> getUsersOfBranch(Integer branchId, EntityManager em) {
		
		List<BranchUserEntity> branchUserList = new BranchUserDao(em).findByBranchId(branchId);
		
		List<Integer> ids = branchUserList.stream()
				.map(bu->bu.getUserId())
				.collect(Collectors.toList());
		
		List<UserEntity> userList = new  ArrayList<UserEntity>();
		if (ids.size() > 0) {
			userList = new UserDao(em).findByIds(ids);
		}
		
		return userList;
	}
	
	public static BranchEntity getBranchOfUser(Integer userId, EntityManager em) {
		
		BranchUserEntity branchUser = new BranchUserDao(em).findByUserId(userId);
		
		if (branchUser == null) {
			return null;
		}
		
		BranchEntity branch = new BranchDao(em).findById(branchUser.getBranchId());
		
		return branch;
	}
}
