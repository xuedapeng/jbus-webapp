package com.moqbus.app.logic.sysman;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.moqbus.app.common.constant.StatusConst;
import com.moqbus.app.common.helper.GuidHelper;
import com.moqbus.app.common.helper.NumericHelper;
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

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="sysman.user.add")
public class UserAddLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		UserAddLogicParam myParam = (UserAddLogicParam)logicParam;
		
		UserEntity user = new UserEntity();
		user.setAccount(myParam.getAccount());
		user.setPassword(Md5SaltTool.getEncryptedPwd(myParam.getPassword()));
		user.setNickName(myParam.getNickName());
		user.setMobile(myParam.getMobile());
		user.setEmail(myParam.getEmail());
		user.setRole(
				"yes".equals(myParam.getSysAdmin())
				? StatusConst.USER_ROLE_SYS_ADMIN
				: StatusConst.USER_ROLE_COMMON);
		user.setStatus(StatusConst.STATUS_NORMAL);
		user.setSecretId(GuidHelper.genUUID());
		user.setSecretKey(GuidHelper.genUUID());
		
		new UserDao(em).save(user);
		
		// 加入部门
		BranchUserEntity branchUser = new BranchUserEntity();
		branchUser.setBranchId(Integer.valueOf(myParam.getBranchId()));
		branchUser.setUserId(user.getId());
		branchUser.setRole(Integer.valueOf(myParam.getBranchRole()));
		
		new BranchUserDao(em).save(branchUser);
		
		res.add("status", 1)
			.add("msg", "sysman.user.add ok. ")
			.add("userId", user.getId());
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		UserAddLogicParam myParam = (UserAddLogicParam)logicParam;
		
		if (StringUtils.isEmpty(myParam.getAccount())
				|| StringUtils.isEmpty(myParam.getPassword())
				|| StringUtils.isEmpty(myParam.getNickName())
				|| StringUtils.isEmpty(myParam.getBranchId())
				) {
			
			res.add("status", -3)
				.add("msg", "need account/password/nickName/branchId.");
			
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
		if (user != null) {

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
