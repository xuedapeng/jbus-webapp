package com.moqbus.app.logic.sysman;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.moqbus.app.common.constant.StatusConst;
import com.moqbus.app.common.helper.GuidHelper;
import com.moqbus.app.common.helper.NumericHelper;
import com.moqbus.app.common.helper.ValidateHelper;
import com.moqbus.app.common.utils.Md5SaltTool;
import com.moqbus.app.db.bean.BranchEntity;
import com.moqbus.app.db.bean.BranchUserEntity;
import com.moqbus.app.db.bean.UserEntity;
import com.moqbus.app.db.dao.BranchDao;
import com.moqbus.app.db.dao.BranchUserDao;
import com.moqbus.app.db.dao.UserDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.annotation.Action;
import com.moqbus.app.logic.sysman.param.UserAddLogicParam;
import com.moqbus.app.logic.sysman.param.UserUpdateLogicParam;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="sysman.user.update")
public class UserUpdateLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		UserUpdateLogicParam myParam = (UserUpdateLogicParam)logicParam;
		
		UserDao userDao = new UserDao(em);
		UserEntity user = userDao.findById(Integer.valueOf(myParam.getUserId()));
		if (user == null) {

			res.add("status", -11)
				.add("msg", "user not exists. ");
			
			return false;
		}
		
		user.setAccount(myParam.getAccount());
		if (!StringUtils.isEmpty(myParam.getPassword())) {
			user.setPassword(Md5SaltTool.getEncryptedPwd(myParam.getPassword()));
		}
		user.setNickName(myParam.getNickName());
		user.setStatus(Integer.valueOf(myParam.getStatus()));
		
		userDao.save(user);
		
		// 加入部门
		BranchUserDao buDao = new BranchUserDao(em);
		BranchUserEntity branchUser = buDao.findByUserId(user.getId());
		if (branchUser == null) {
			branchUser = new BranchUserEntity();
		}

		branchUser.setBranchId(Integer.valueOf(myParam.getBranchId()));
		branchUser.setUserId(user.getId());
		branchUser.setRole(Integer.valueOf(myParam.getBranchRole()));
		
		buDao.save(branchUser);
		
		res.add("status", 1)
			.add("msg", "sysman.user.update ok. ")
			.add("userId", user.getId());
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		UserUpdateLogicParam myParam = (UserUpdateLogicParam)logicParam;
		
		String notEmptyResult = ValidateHelper.notEmptyCheck(
				"userId", myParam.getUserId(),
				"account", myParam.getAccount(),
				"nickName", myParam.getNickName(),
				"branchId", myParam.getBranchId(),
				"branchRole", myParam.getBranchRole(),
				"status", myParam.getStatus()
				);
		if (!notEmptyResult.isEmpty()) {
		
			res.add("status", -3)
				.add("msg", "need " + notEmptyResult);
			
			return false;
		}

		if (!NumericHelper.isInteger(myParam.getBranchId())
				|| !NumericHelper.isInteger(myParam.getBranchRole()) ) {
			
			res.add("status", -3)
				.add("msg", "branchId/branchRole need integer.");
			
			return false;
		}
		
		// 重复check
		UserEntity user = new UserDao(em).findByAccount(myParam.getAccount());
		if (user != null && !user.getId().equals(Integer.valueOf(myParam.getUserId()))) {

			res.add("status", -11)
				.add("msg", "account exists.");
			
			return false;
		}
		
		// branchId 存在
		BranchEntity branch = new BranchDao(em).findById(Integer.valueOf(myParam.getBranchId()));
		if (branch == null) {

			res.add("status", -12)
				.add("msg", "branchId not exists.");
			
			return false;
		}
		
		return true;
	}

}
