package com.moqbus.app.logic.devman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.moqbus.app.common.constant.StatusConst;
import com.moqbus.app.db.bean.BranchEntity;
import com.moqbus.app.db.bean.BranchUserEntity;
import com.moqbus.app.db.bean.DeviceEntity;
import com.moqbus.app.db.bean.PrivilegeEntity;
import com.moqbus.app.db.dao.BranchDao;
import com.moqbus.app.db.dao.BranchUserDao;
import com.moqbus.app.db.dao.DeviceDao;
import com.moqbus.app.db.dao.PrivilegeDao;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.annotation.Action;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="devman.mydevice.list")
public class MyDeviceListLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		List<BranchEntity> branchList = new BranchDao(em).findByIds(null);
		BranchUserEntity branchUser = new BranchUserDao(em).findByUserId(_loginUser.getId());
		List<PrivilegeEntity> privileges = new PrivilegeDao(em).findBySubject(
				StatusConst.SUBJECT_TYPE_USER, _loginUser.getId());
		
		// branchId, privilege
		Map<Integer, Integer> pMap = privileges.stream()
				.filter(e->e.getObjectType().equals(StatusConst.OBJECT_TYPE_BRANCH))
				.collect(Collectors.toMap(PrivilegeEntity::getObjectId, PrivilegeEntity::getPrivilege));
			
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		DeviceDao deviceDao = new DeviceDao(em);
		branchList.forEach((b)->{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("branchId", b.getId());
			map.put("branchName", b.getBranchName());
			map.put("description", b.getDescription());
			
			// 部门所有设备
			if (_loginUser.getRole().equals(StatusConst.USER_ROLE_SYS_ADMIN)
					|| b.getId().equals(branchUser.getBranchId())
					|| pMap.containsKey(b.getId())) {
				
				List<DeviceEntity> deviceEntityList = deviceDao.findByBranchId(b.getId());
				List<Map<String, Object>> deviceMapList = new ArrayList<Map<String, Object>>();
				
				deviceEntityList.forEach((d)->{
					Map<String, Object> deviceMap = new HashMap<String, Object>();
					deviceMap.put("deviceId", d.getId());
					deviceMap.put("deviceName", d.getDeviceName());
					
					// 系统管理员
					if (_loginUser.getRole().equals(StatusConst.USER_ROLE_SYS_ADMIN)) {
						deviceMap.put("privilege", "ctrl");
						
					} else if  (b.getId().equals(branchUser.getBranchId())) {
						// 本部门
						deviceMap.put("privilege", 
								branchUser.getRole().equals(StatusConst.BRANCH_ROLE_COMMON)
									?"view"
									:"ctrl");
						
					} else if (pMap.containsKey(b.getId())){
						// 有授权
						deviceMap.put("privilege", 
								pMap.get(b.getId()).equals(StatusConst.PRIVILEGE_VIEW)
									?"view"
									:"ctrl");
							
					}
					
					deviceMapList.add(deviceMap);
				});
				
				if (!deviceMapList.isEmpty()) {
					map.put("deviceList", deviceMapList);
					resultList.add(map);
				}
			}
			
			
			
		});
		
		res.add("status", 1)
			.add("msg", "devman.mydevice.list ok. ")
			.add("result", resultList);
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {
		
		return true;
	}

}
