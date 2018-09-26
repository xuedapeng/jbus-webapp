package com.moqbus.app.logic.sysman;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.moqbus.app.common.helper.NumericHelper;
import com.moqbus.app.db.bean.BranchEntity;
import com.moqbus.app.db.dao.BranchDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.annotation.Action;
import com.moqbus.app.logic.sysman.param.BranchAddLogicParam;
import com.moqbus.app.logic.sysman.param.BranchUpdateLogicParam;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="sysman.branch.update")
public class BranchUpdateLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		BranchUpdateLogicParam myParam = (BranchUpdateLogicParam)logicParam;

		BranchDao branchDao = new BranchDao(em);
		Integer branchId = Integer.valueOf(myParam.getBranchId());
		BranchEntity branch = branchDao.findById(branchId);

		if (branch == null) {

			res.add("status", -11)
				.add("msg", "branch not exists. ");
			
			return false;
		}
		
		branch.setBranchName(myParam.getBranchName());
		branch.setDescription(myParam.getDescription());
		
		new BranchDao(em).save(branch);
		
		res.add("status", 1)
			.add("msg", "sysman.branch.update ok. ")
			.add("branchId", branch.getId());
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		BranchUpdateLogicParam myParam = (BranchUpdateLogicParam)logicParam;
		
		if (StringUtils.isEmpty(myParam.getBranchId())
				|| StringUtils.isEmpty(myParam.getBranchName())) {
			res.add("status", -3)
				.add("msg", "need branchId/branchName.");
			
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
