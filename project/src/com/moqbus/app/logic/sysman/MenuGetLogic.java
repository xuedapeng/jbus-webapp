package com.moqbus.app.logic.sysman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;


import com.moqbus.app.db.bean.BranchEntity;
import com.moqbus.app.db.bean.UserEntity;
import com.moqbus.app.db.bean.DeviceEntity;
import com.moqbus.app.db.dao.BranchDao;
import com.moqbus.app.db.dao.DeviceDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.LogicCommon;
import com.moqbus.app.logic.share.annotation.Action;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="sysman.menu.get")
public class MenuGetLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		List<BranchEntity> branchList = new BranchDao(em).findByIds(null);
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		branchList.forEach((b)->{
			Map<String, Object> branchMap = new HashMap<String, Object>();
			branchMap.put("branchId", String.valueOf(b.getId()));
			branchMap.put("branchName", b.getBranchName());
			
			// user
			List<UserEntity> userEntityList = LogicCommon.getUsersOfBranch(b.getId(), em);
			List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
			userEntityList.forEach((ue)->{
				Map<String, String> userMap = new HashMap<String, String>();
				userMap.put("userId", String.valueOf(ue.getId()));
				userMap.put("nickName", ue.getNickName());
				userMap.put("account", ue.getAccount());
				userList.add(userMap);
			});
			
			branchMap.put("user", userList);
			
			// device
			List<DeviceEntity> deviceEntityList = new DeviceDao(em).findByBranchId(b.getId());
			List<Map<String, String>> deviceList = new ArrayList<Map<String, String>>();
			
			deviceEntityList.forEach((de)->{
				Map<String, String> deviceMap = new HashMap<String, String>();
				deviceMap.put("deviceId", String.valueOf(de.getId()));
				deviceMap.put("deviceName", de.getDeviceName());
				deviceMap.put("deviceSn", de.getDeviceSn());
				deviceList.add(deviceMap);
			});
			

			branchMap.put("device", deviceList);
			
			resultList.add(branchMap);
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
