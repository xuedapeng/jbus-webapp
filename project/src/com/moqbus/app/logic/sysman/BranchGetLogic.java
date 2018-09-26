package com.moqbus.app.logic.sysman;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.moqbus.app.common.helper.NumericHelper;
import com.moqbus.app.db.bean.BranchEntity;
import com.moqbus.app.db.dao.BranchDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.annotation.Action;
import com.moqbus.app.logic.sysman.param.BranchAddLogicParam;
import com.moqbus.app.logic.sysman.param.BranchGetLogicParam;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="sysman.branch.detail")
public class BranchGetLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		BranchGetLogicParam myParam = (BranchGetLogicParam)logicParam;
		
		BranchEntity branch = new BranchDao(em).findById(Integer.valueOf(myParam.getBranchId()));
		
		if (branch == null) {

			res.add("status", -11)
				.add("msg", "branch not exists. ");
			return false;
		}
		res.add("status", 1)
			.add("msg", "sysman.branch.detail ok. ")
			.add("branchName", branch.getBranchName())
			.add("description", branch.getDescription());
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		BranchGetLogicParam myParam = (BranchGetLogicParam)logicParam;
		
		if (StringUtils.isEmpty(myParam.getBranchId())) {
			res.add("status", -3)
				.add("msg", "need branchId.");
			
			return false;
		}

		if (!NumericHelper.isInteger(myParam.getBranchId())) {
			res.add("status", -3)
				.add("msg", "branchId need integer.");
			
			return false;
		}
		
		return true;
	}

}
