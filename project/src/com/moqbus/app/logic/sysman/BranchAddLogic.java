package com.moqbus.app.logic.sysman;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.moqbus.app.db.bean.BranchEntity;
import com.moqbus.app.db.dao.BranchDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.annotation.Action;
import com.moqbus.app.logic.sysman.param.BranchAddLogicParam;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="sysman.branch.add")
public class BranchAddLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		BranchAddLogicParam myParam = (BranchAddLogicParam)logicParam;
		
		BranchEntity branch = new BranchEntity();
		branch.setBranchName(myParam.getBranchName());
		branch.setDescription(myParam.getDescription());
		new BranchDao(em).save(branch);
		
		res.add("status", 1)
			.add("msg", "sysman.branch.add ok. ")
			.add("branchId", branch.getId());
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		BranchAddLogicParam myParam = (BranchAddLogicParam)logicParam;
		
		if (StringUtils.isEmpty(myParam.getBranchName())) {
			res.add("status", -3)
				.add("msg", "need branchName.");
			
			return false;
		}
		
		return true;
	}

}
