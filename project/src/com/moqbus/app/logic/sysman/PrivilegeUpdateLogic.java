package com.moqbus.app.logic.sysman;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.moqbus.app.common.constant.StatusConst;
import com.moqbus.app.common.helper.NumericHelper;
import com.moqbus.app.db.bean.PrivilegeEntity;
import com.moqbus.app.db.dao.PrivilegeDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.annotation.Action;
import com.moqbus.app.logic.sysman.param.PrivilegeUpdateLogicParam;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="sysman.privilege.user.update")
public class PrivilegeUpdateLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		PrivilegeUpdateLogicParam myParam = (PrivilegeUpdateLogicParam)logicParam;
		
		Integer userId = Integer.valueOf(myParam.getUserId());
		List<Map<String, String>> privileges = myParam.getPrivileges();
		
		PrivilegeDao dao = new PrivilegeDao(em);
		
		// 清空
		List<PrivilegeEntity> listDel = dao.findBySubject(StatusConst.SUBJECT_TYPE_USER, userId);
		listDel.forEach((E)->{
			dao.delete(E);
		});
		
		privileges.forEach((E)->{
			Integer branchId = Integer.valueOf(E.get("branchId"));
			Integer privilege = Integer.valueOf(E.get("privilege"));
			
			PrivilegeEntity pe = new PrivilegeEntity();
			pe.setSubjectType(StatusConst.SUBJECT_TYPE_USER);
			pe.setSubjectId(userId);
			pe.setObjectType(StatusConst.OBJECT_TYPE_BRANCH);
			pe.setObjectId(branchId);
			pe.setPrivilege(privilege);
			dao.save(pe);
		});
		
		res.add("status", 1)
			.add("msg", "sysman.privilege.user.update ok. ");
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		PrivilegeUpdateLogicParam myParam = (PrivilegeUpdateLogicParam)logicParam;
		
		List<Map<String, String>> privileges = myParam.getPrivileges();
		
		if (StringUtils.isEmpty(myParam.getUserId())
				|| privileges == null) {
			res.add("status", -3)
				.add("msg", "need userId/privileges.");
			
			return false;
		}

		if (!NumericHelper.isInteger(myParam.getUserId())) {
			res.add("status", -3)
				.add("msg", "userId need integer.");
			
			return false;
		}
		
		
		if (privileges != null) {
			for (Map<String, String> item: privileges) {
				if (item.get("branchId") == null 
						|| item.get("privilege") == null) {

					res.add("status", -3)
						.add("msg", "privileges format error.");
					return false;
				}
			}
		}
		
		return true;
	}

}
