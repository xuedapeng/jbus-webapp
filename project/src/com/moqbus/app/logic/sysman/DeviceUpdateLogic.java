package com.moqbus.app.logic.sysman;

import javax.persistence.EntityManager;


import com.moqbus.app.common.helper.ValidateHelper;
import com.moqbus.app.db.bean.BranchEntity;
import com.moqbus.app.db.bean.DeviceEntity;
import com.moqbus.app.db.dao.BranchDao;
import com.moqbus.app.db.dao.DeviceDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.annotation.Action;
import com.moqbus.app.logic.sysman.param.DeviceUpdateLogicParam;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="sysman.device.update")
public class DeviceUpdateLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		DeviceUpdateLogicParam myParam = (DeviceUpdateLogicParam)logicParam;
		
		DeviceDao dao = new DeviceDao(em);
		DeviceEntity device = dao.findById(Integer.valueOf(myParam.getDeviceId()));
		
		if (device == null) {

			res.add("status", -11)
				.add("msg", "device not exists. ");
			
			return false;
		}
		
		device.setDeviceSn(myParam.getDeviceSn());
		device.setDeviceName(myParam.getDeviceName());
		device.setLocation(myParam.getLocation());
		device.setLatitude(myParam.getLatitude());
		device.setLongitude(myParam.getLongitude());
		device.setBranchId(Integer.valueOf(myParam.getBranchId()));
		device.setDescription(myParam.getDescription());
		
		dao.save(device);
		
		res.add("status", 1)
			.add("msg", "sysman.device.update ok. ")
			.add("deviceId", device.getId());
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		DeviceUpdateLogicParam myParam = (DeviceUpdateLogicParam)logicParam;

		String notEmptyResult = ValidateHelper.notEmptyCheck(
				"deviceId", myParam.getDeviceId(),
				"deviceSn", myParam.getDeviceSn(),
				"deviceName", myParam.getDeviceName(),
				"location", myParam.getLocation(),
				"latitude", myParam.getLatitude(),
				"longitude", myParam.getLongitude(),
				"branchId", myParam.getBranchId()
				);
		if (!notEmptyResult.isEmpty()) {
		
			res.add("status", -3)
				.add("msg", "need " + notEmptyResult);
			
			return false;
		}

		String integerResult = ValidateHelper.integerCheck(
				"deviceId", myParam.getDeviceId(),
				"branchId", myParam.getBranchId()
				);
		if (!integerResult.isEmpty()) {
			
			res.add("status", -3)
				.add("msg", "need integer:" + integerResult);
			
			return false;
		}
		
		// 重复check
		DeviceEntity device = new DeviceDao(em).findByDeviceSn(myParam.getDeviceSn());
		if (device != null && !device.getId().equals(Integer.valueOf(myParam.getDeviceId()))) {

			res.add("status", -11)
				.add("msg", "deviceSn exists.");
			
			return false;
		}
		
		// branchId 存在
		BranchEntity branch = new BranchDao(em).findById(Integer.valueOf(myParam.getBranchId()));
		if (branch == null) {

			res.add("status", -12)
				.add("msg", "branchId not exists.");
			
			return false;
		}
		
		return true;
	}

}
