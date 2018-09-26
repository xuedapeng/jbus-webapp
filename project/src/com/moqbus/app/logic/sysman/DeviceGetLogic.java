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
import com.moqbus.app.db.dao.BranchDao;
import com.moqbus.app.db.dao.BranchUserDao;
import com.moqbus.app.db.dao.DeviceDao;
import com.moqbus.app.db.dao.UserDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.LogicCommon;
import com.moqbus.app.logic.share.annotation.Action;
import com.moqbus.app.logic.sysman.param.DeviceGetLogicParam;
import com.moqbus.app.logic.sysman.param.UserGetLogicParam;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="sysman.device.detail")
public class DeviceGetLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		DeviceGetLogicParam myParam = (DeviceGetLogicParam)logicParam;
		
		DeviceEntity device = new DeviceDao(em).findById(Integer.valueOf(myParam.getDeviceId()));
		
		if (device == null) {

			res.add("status", -11)
				.add("msg", "device not exists. ");
			
			return false;
		}
		
		BranchEntity branch = new BranchDao(em).findById(device.getBranchId());
		
		res.add("status", 1)
			.add("msg", "sysman.device.detail ok. ")
			.add("deviceId", device.getId())
			.add("deviceSn", device.getDeviceSn())
			.add("deviceName", device.getDeviceName())
			.add("branchId", device.getBranchId())
			.add("location", device.getLocation())
			.add("latitude", device.getLatitude())
			.add("longitude", device.getLongitude())
			.add("branchName", branch!=null?branch.getBranchName():"")
			.add("description", device.getDescription());
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		DeviceGetLogicParam myParam = (DeviceGetLogicParam)logicParam;
		
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
