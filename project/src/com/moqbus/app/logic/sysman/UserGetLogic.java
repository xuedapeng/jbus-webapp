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
import com.moqbus.app.logic.sysman.param.UserGetLogicParam;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="sysman.user.detail")
public class UserGetLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		UserGetLogicParam myParam = (UserGetLogicParam)logicParam;
		
		UserEntity user = new UserDao(em).findById(Integer.valueOf(myParam.getUserId()));
		
		if (user == null) {

			res.add("status", -11)
				.add("msg", "user not exists. ");
			
			return false;
		}
		
		BranchEntity branch = LogicCommon.getBranchOfUser(user.getId(), em);
		res.add("status", 1)
			.add("msg", "sysman.user.detail ok. ")
			.add("userId", user.getId())
			.add("account", user.getAccount())
			.add("nickName", user.getNickName())
			.add("mobile", user.getMobile())
			.add("email", user.getEmail())
			.add("isSysAdmin", user.getRole()==StatusConst.USER_ROLE_SYS_ADMIN?"yes":"no")
			.add("branchRole", LogicCommon.getBranchRole(user.getId(), em))
			.add("branchId", branch!=null?branch.getId():"")
			.add("branchName", branch!=null?branch.getBranchName():"")
			.add("status", user.getStatus());
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		UserGetLogicParam myParam = (UserGetLogicParam)logicParam;
		
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
