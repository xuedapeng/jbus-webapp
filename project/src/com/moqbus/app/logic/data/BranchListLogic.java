package com.moqbus.app.logic.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;


import com.moqbus.app.db.bean.BranchEntity;
import com.moqbus.app.db.dao.BranchDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.annotation.Action;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="sysman.branch.list")
public class BranchListLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		List<BranchEntity> branchList = new BranchDao(em).findByIds(null);
		
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		
		branchList.forEach((E)->{
			Map<String, String> map = new HashMap<String, String>();
			map.put("branchId", String.valueOf(E.getId()));
			map.put("branchName", E.getBranchName());
			map.put("description", E.getDescription());
			
			resultList.add(map);
		});
		
		res.add("status", 1)
			.add("msg", "sysman.branch.list ok. ")
			.add("result", resultList);
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		return true;
	}

}
