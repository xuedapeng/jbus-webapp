package com.moqbus.app.logic.sysman;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.moqbus.app.common.constant.StatusConst;
import com.moqbus.app.common.helper.NumericHelper;
import com.moqbus.app.db.bean.BranchEntity;
import com.moqbus.app.db.bean.BranchUserEntity;
import com.moqbus.app.db.bean.UserEntity;
import com.moqbus.app.db.dao.BranchUserDao;
import com.moqbus.app.db.dao.UserDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.LogicCommon;
import com.moqbus.app.logic.share.annotation.Action;
import com.moqbus.app.logic.sysman.param.UserDelLogicParam;
import com.moqbus.app.logic.sysman.param.UserGetLogicParam;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="sysman.user.del")
public class UserDelLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		UserDelLogicParam myParam = (UserDelLogicParam)logicParam;
		
		UserDao userDao = new UserDao(em);
		UserEntity user = userDao.findById(Integer.valueOf(myParam.getUserId()));
		
		if (user == null) {

			res.add("status", -11)
				.add("msg", "user not exists. ");
			
			return false;
		}
		
		userDao.delete(user);
		
		// 部门关系
		BranchUserDao buDao = new BranchUserDao(em);
		BranchUserEntity branchUser = buDao.findByUserId(user.getId());
		if (branchUser != null) {
			buDao.delete(branchUser);
		}
		
		res.add("status", 1)
			.add("msg", "sysman.user.del ok. ")
			.add("userId", user.getId());
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		UserDelLogicParam myParam = (UserDelLogicParam)logicParam;
		
		if (StringUtils.isEmpty(myParam.getUserId())) {
			res.add("status", -3)
				.add("msg", "need userId.");
			
			return false;
		}

		if (!NumericHelper.isInteger(myParam.getUserId())) {
			res.add("status", -3)
				.add("msg", "userId need integer.");
			
			return false;
		}
		
		return true;
	}

}
