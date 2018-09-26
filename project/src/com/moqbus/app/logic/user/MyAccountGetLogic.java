package com.moqbus.app.logic.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.moqbus.app.common.constant.StatusConst;
import com.moqbus.app.common.helper.NumericHelper;
import com.moqbus.app.common.utils.Md5SaltTool;
import com.moqbus.app.db.bean.BranchEntity;
import com.moqbus.app.db.bean.BranchUserEntity;
import com.moqbus.app.db.bean.PrivilegeEntity;
import com.moqbus.app.db.bean.UserEntity;
import com.moqbus.app.db.dao.BranchUserDao;
import com.moqbus.app.db.dao.PrivilegeDao;
import com.moqbus.app.db.dao.UserDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.LogicCommon;
import com.moqbus.app.logic.share.annotation.Action;
import com.moqbus.app.logic.user.param.LoginLogicParam;
import com.moqbus.app.logic.user.param.MyAccountGetLogicParam;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="user.myaccount.get")
public class MyAccountGetLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		MyAccountGetLogicParam myParam = (MyAccountGetLogicParam)logicParam;
		
		// 账号信息
		UserEntity user = new UserDao(em).findById(Integer.valueOf(myParam.getUserId()));
		BranchEntity branch = LogicCommon.getBranchOfUser(user.getId(), em);
		
		// 权限信息
		List<PrivilegeEntity> privilegeList = 
				new PrivilegeDao(em).findBySubject(
						StatusConst.SUBJECT_TYPE_USER, Integer.valueOf(myParam.getUserId()));

		Map<String, Integer> pResultMap = new HashMap<String, Integer>();
		privilegeList.forEach((E)->{
			if(E.getObjectType().equals(StatusConst.OBJECT_TYPE_BRANCH)) {
				pResultMap.put(String.valueOf(E.getObjectId()), E.getPrivilege());
			}
		});
		
		// 返回值
		res.add("status", 1)
			.add("msg", "user.myaccount.get ok. ")
			.add("userId", user.getId())
			.add("account", user.getAccount())
			.add("nickName", user.getNickName())
			.add("mobile", user.getMobile())
			.add("email", user.getEmail())
			.add("isSysAdmin", user.getRole()==StatusConst.USER_ROLE_SYS_ADMIN?"yes":"no")
			.add("branchId", branch!=null?branch.getId():"")
			.add("branchRole", LogicCommon.getBranchRole(user.getId(), em))
			.add("branchName", branch!=null?branch.getBranchName():"")
			.add("status", user.getStatus())
			.add("privileges", pResultMap);
		

		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		MyAccountGetLogicParam myParam = (MyAccountGetLogicParam)logicParam;

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

	
   /*
    * 身份认证
    */
	@Override
   protected boolean auth(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em){
		
		MyAccountGetLogicParam myParam = (MyAccountGetLogicParam)logicParam;
		
		if (!super.auth(logicParam, res, em)) {
			return false;
		}
		
		// 只能获取自己的账号信息
		if (!_loginUser.getId().equals(Integer.valueOf(myParam.getUserId()))) {
			res.add("status", -2)
				.add("msg", "只能获取自己的账号信息");
			return false;
		}
		
		return true;
	}

}
