package com.moqbus.app.logic.sysman;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.moqbus.app.common.helper.NumericHelper;
import com.moqbus.app.db.bean.BranchEntity;
import com.moqbus.app.db.bean.BranchUserEntity;
import com.moqbus.app.db.bean.DeviceEntity;
import com.moqbus.app.db.dao.BranchDao;
import com.moqbus.app.db.dao.BranchUserDao;
import com.moqbus.app.db.dao.DeviceDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.annotation.Action;
import com.moqbus.app.logic.sysman.param.BranchAddLogicParam;
import com.moqbus.app.logic.sysman.param.BranchDelLogicParam;
import com.moqbus.app.logic.sysman.param.BranchGetLogicParam;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;
import javapns.devices.Device;

@Action(method="sysman.branch.del")
public class BranchDelLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		BranchDelLogicParam myParam = (BranchDelLogicParam)logicParam;
		
		BranchDao branchDao = new BranchDao(em);
		Integer branchId = Integer.valueOf(myParam.getBranchId());
		BranchEntity branch = branchDao.findById(branchId);

		if (branch == null) {

			res.add("status", -11)
				.add("msg", "branch not exists. ");
			
			return false;
		}
		
		BranchUserDao buDao = new BranchUserDao(em);
		List<BranchUserEntity> buList =  buDao.findByBranchId(branchId);
		
		if (!buList.isEmpty()) {

			res.add("status", -12)
				.add("msg", "管理单位下面有人员存在，不能删除。");
			
			return false;
		}
		
		DeviceDao deviceDao = new DeviceDao(em);
		List<DeviceEntity> deviceList = deviceDao.findByBranchId(branchId);
		if (!deviceList.isEmpty()) {

			res.add("status", -13)
				.add("msg", "管理单位下面有设备存在，不能删除。");
			
			return false;
		}
		
		branchDao.delete(branch);
		
		res.add("status", 1)
			.add("msg", "sysman.branch.del ok. ")
			.add("branchId", branch.getId());
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		BranchDelLogicParam myParam = (BranchDelLogicParam)logicParam;
		
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
