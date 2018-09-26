package com.moqbus.app.logic.sysman;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.moqbus.app.common.constant.StatusConst;
import com.moqbus.app.common.helper.NumericHelper;
import com.moqbus.app.db.bean.BranchEntity;
import com.moqbus.app.db.bean.BranchUserEntity;
import com.moqbus.app.db.bean.DeviceEntity;
import com.moqbus.app.db.bean.UserEntity;
import com.moqbus.app.db.dao.BranchUserDao;
import com.moqbus.app.db.dao.DeviceDao;
import com.moqbus.app.db.dao.UserDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.LogicCommon;
import com.moqbus.app.logic.share.annotation.Action;
import com.moqbus.app.logic.sysman.param.DeviceDelLogicParam;
import com.moqbus.app.logic.sysman.param.UserDelLogicParam;
import com.moqbus.app.logic.sysman.param.UserGetLogicParam;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="sysman.device.del")
public class DeviceDelLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		DeviceDelLogicParam myParam = (DeviceDelLogicParam)logicParam;
		
		DeviceDao dao = new DeviceDao(em);
		DeviceEntity device = dao.findById(Integer.valueOf(myParam.getDeviceId()));
		
		if (device == null) {

			res.add("status", -11)
				.add("msg", "device not exists. ");
			
			return false;
		}
		
		dao.delete(device);
		
		
		res.add("status", 1)
			.add("msg", "sysman.device.del ok. ")
			.add("deviceId", device.getId());
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		DeviceDelLogicParam myParam = (DeviceDelLogicParam)logicParam;
		
		if (StringUtils.isEmpty(myParam.getDeviceId())) {
			res.add("status", -3)
				.add("msg", "need deviceId.");
			
			return false;
		}

		if (!NumericHelper.isInteger(myParam.getDeviceId())) {
			res.add("status", -3)
				.add("msg", "deviceId need integer.");
			
			return false;
		}
		
		return true;
	}

}
