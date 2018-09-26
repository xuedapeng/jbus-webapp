package com.moqbus.app.logic.sysman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.moqbus.app.common.constant.StatusConst;
import com.moqbus.app.common.helper.NumericHelper;
import com.moqbus.app.db.bean.BranchUserEntity;
import com.moqbus.app.db.bean.PrivilegeEntity;
import com.moqbus.app.db.bean.UserEntity;
import com.moqbus.app.db.dao.BranchUserDao;
import com.moqbus.app.db.dao.PrivilegeDao;
import com.moqbus.app.db.dao.UserDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.LogicCommon;
import com.moqbus.app.logic.share.annotation.Action;
import com.moqbus.app.logic.sysman.param.PrivilegeGetLogicParam;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="sysman.privilege.user.get")
public class PrivilegeGetLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		PrivilegeGetLogicParam myParam = (PrivilegeGetLogicParam)logicParam;
		
		List<PrivilegeEntity> privilegeList = 
				new PrivilegeDao(em).findBySubject(
						StatusConst.SUBJECT_TYPE_USER, Integer.valueOf(myParam.getUserId()));
		
		List<Map<String, Integer>> resultList = new ArrayList<Map<String, Integer>>();
		
		privilegeList.forEach((E)->{
			Map<String, Integer> pMap = new HashMap<String, Integer>();
			pMap.put("subjectType", E.getSubjectType());
			pMap.put("subjectId", E.getSubjectId());
			pMap.put("objectType", E.getObjectType());
			pMap.put("objectId", E.getObjectId());
			pMap.put("privilege", E.getPrivilege());
			
			resultList.add(pMap);
		});
		
		res.add("status", 1)
			.add("msg", "sysman.privilege.user.get ok. ")
			.add("result", resultList);
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		PrivilegeGetLogicParam myParam = (PrivilegeGetLogicParam)logicParam;
		
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
