package com.moqbus.app.logic.user;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.moqbus.app.common.constant.StatusConst;
import com.moqbus.app.common.helper.NumericHelper;
import com.moqbus.app.common.helper.ValidateHelper;
import com.moqbus.app.common.utils.Md5SaltTool;
import com.moqbus.app.db.bean.BranchUserEntity;
import com.moqbus.app.db.bean.UserEntity;
import com.moqbus.app.db.dao.BranchUserDao;
import com.moqbus.app.db.dao.UserDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.LogicCommon;
import com.moqbus.app.logic.share.annotation.Action;
import com.moqbus.app.logic.user.param.LoginLogicParam;
import com.moqbus.app.logic.user.param.MyAccountGetLogicParam;
import com.moqbus.app.logic.user.param.MyPasswordUpdateLogicParam;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="user.mypassword.update")
public class MyPasswordUpdateLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		MyPasswordUpdateLogicParam myParam = (MyPasswordUpdateLogicParam)logicParam;
		
		Integer userId = Integer.valueOf(myParam.getUserId());
		String oldPassword = myParam.getOldPassword();
		String newPassword = myParam.getNewPassword();
		
		// find db
		UserDao dao = new UserDao(em);
		UserEntity user = dao.findById(userId);
		
		if (!Md5SaltTool.validPassword(oldPassword, user.getPassword())) {

			res.add("status", -12)
			.add("msg", "旧密码错误");
			
			return false;
			
		}
		
		// 更新密码
		user.setPassword(Md5SaltTool.getEncryptedPwd(newPassword));
		dao.save(user);
		
		res.add("status", 1)
			.add("msg", "密码修改成功！")
			.add("userId", user.getId());

		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		MyPasswordUpdateLogicParam myParam = (MyPasswordUpdateLogicParam)logicParam;

		String notEmptyResult = ValidateHelper.notEmptyCheck(
				"userId", myParam.getUserId(),
				"oldPassword", myParam.getOldPassword(),
				"newPassword", myParam.getNewPassword()
				);
		
		if (!notEmptyResult.isEmpty()) {
		
			res.add("status", -3)
				.add("msg", "need " + notEmptyResult);
			
			return false;
		}

		if (!NumericHelper.isInteger(myParam.getUserId())) {
			res.add("status", -3)
				.add("msg", "userId need integer.");
			
			return false;
		}
		
		return true;
	}

	
   /*
    * 身份认证
    */
	@Override
   protected boolean auth(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em){

		MyPasswordUpdateLogicParam myParam = (MyPasswordUpdateLogicParam)logicParam;
		
		if (!super.auth(logicParam, res, em)) {
			return false;
		}
		
		// 只能修改自己的密码
		if (!_loginUser.getId().equals(Integer.valueOf(myParam.getUserId()))) {
			res.add("status", -2)
				.add("msg", "只能修改自己的密码");
			return false;
		}
		
		return true;
	}

}
