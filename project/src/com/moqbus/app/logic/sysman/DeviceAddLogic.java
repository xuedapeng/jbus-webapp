package com.moqbus.app.logic.sysman;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.moqbus.app.common.constant.StatusConst;
import com.moqbus.app.common.helper.GuidHelper;
import com.moqbus.app.common.helper.NumericHelper;
import com.moqbus.app.common.utils.Md5SaltTool;
import com.moqbus.app.db.bean.BranchEntity;
import com.moqbus.app.db.bean.BranchUserEntity;
import com.moqbus.app.db.bean.DeviceEntity;
import com.moqbus.app.db.bean.UserEntity;
import com.moqbus.app.db.dao.BranchDao;
import com.moqbus.app.db.dao.BranchUserDao;
import com.moqbus.app.db.dao.DeviceDao;
import com.moqbus.app.db.dao.UserDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.annotation.Action;
import com.moqbus.app.logic.sysman.param.DeviceAddLogicParam;
import com.moqbus.app.logic.sysman.param.UserAddLogicParam;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="sysman.device.add")
public class DeviceAddLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		DeviceAddLogicParam myParam = (DeviceAddLogicParam)logicParam;
		
		DeviceEntity device = new DeviceEntity();
		device.setDeviceSn(myParam.getDeviceSn());
		device.setDeviceName(myParam.getDeviceName());
		device.setLocation(myParam.getLocation());
		device.setLatitude(myParam.getLatitude());
		device.setLongitude(myParam.getLongitude());
		device.setBranchId(Integer.valueOf(myParam.getBranchId()));
		device.setDescription(myParam.getDescription());
		
		new DeviceDao(em).save(device);
		
		res.add("status", 1)
			.add("msg", "sysman.device.add ok. ")
			.add("deviceId", device.getId());
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		DeviceAddLogicParam myParam = (DeviceAddLogicParam)logicParam;
		
		if (StringUtils.isEmpty(myParam.getDeviceSn())
				|| StringUtils.isEmpty(myParam.getDeviceName())
				|| StringUtils.isEmpty(myParam.getLocation())
				|| StringUtils.isEmpty(myParam.getLatitude())
				|| StringUtils.isEmpty(myParam.getLongitude())
				|| StringUtils.isEmpty(myParam.getBranchId())
				) {
			
			res.add("status", -3)
				.add("msg", "need deviceSn/deviceName/branchId.");
			
			return false;
		}

		if (!NumericHelper.isInteger(myParam.getBranchId())) {
			
			res.add("status", -3)
				.add("msg", "branchId need integer.");
			
			return false;
		}
		
		// 重复check
		DeviceEntity device = new DeviceDao(em).findByDeviceSn(myParam.getDeviceSn());
		if (device != null) {

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
