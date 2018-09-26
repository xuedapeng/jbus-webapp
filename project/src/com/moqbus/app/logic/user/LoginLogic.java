package com.moqbus.app.logic.user;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.moqbus.app.common.constant.StatusConst;
import com.moqbus.app.common.utils.Md5SaltTool;
import com.moqbus.app.db.bean.BranchUserEntity;
import com.moqbus.app.db.bean.UserEntity;
import com.moqbus.app.db.dao.BranchUserDao;
import com.moqbus.app.db.dao.UserDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.LogicCommon;
import com.moqbus.app.logic.share.annotation.Action;
import com.moqbus.app.logic.user.param.LoginLogicParam;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="user.login")
public class LoginLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		LoginLogicParam myParam = (LoginLogicParam)logicParam;
		
		String account = myParam.getAccount();
		String password = myParam.getPassword();
		
		// find db
		UserEntity user = new UserDao(em).findByAccount(account);
		
		if (user == null) {
			res.add("status", -11)
			.add("msg", "用户不存在");
			
			return false;
		}
		
		if (!Md5SaltTool.validPassword(password, user.getPassword())) {

			res.add("status", -12)
			.add("msg", "密码错误");
			
			return false;
			
		}
		
		res.add("status", 1)
			.add("msg", "user.login ok")
			.add("userId", user.getId())
			.add("secretId", user.getSecretId())
			.add("secretKey", user.getSecretKey())
			.add("account", user.getAccount())
			.add("nickName", user.getNickName())
			.add("isSysAdmin", user.getRole().equals(StatusConst.USER_ROLE_SYS_ADMIN)?"yes":"no")
			.add("isBranchAdmin", LogicCommon.isBranchAdmin(user.getId(), em)?"yes":"no");

		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		LoginLogicParam myParam = (LoginLogicParam)logicParam;
		
		String account = myParam.getAccount();
		String password = myParam.getPassword();
		
		if (StringUtils.isEmpty(account)
				|| StringUtils.isEmpty(password)) {
			
			res.add("status", -3)
				.add("msg", "参数错误");
			
			return false;
		}
		
		
		return true;
	}

	
   /*
    * 身份认证
    */
	@Override
   protected boolean auth(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em){
		return true;
	}

}
