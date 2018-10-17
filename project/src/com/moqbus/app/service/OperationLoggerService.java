package com.moqbus.app.service;

import java.util.Date;


import com.moqbus.app.db.bean.OperationEntity;
import com.moqbus.app.logic.share.LogicCommon;
import com.moqbus.app.wshandler.param.GateHandlerParam;

import fw.jbiz.db.ZDao;

public class OperationLoggerService {

	public static void save(String action, String secretId, String deviceSn, String cmdParam) {
		
		OperationEntity op = new OperationEntity();
		op.setUserId(LogicCommon.getUserIdBySecretId(secretId));
		op.setDeviceId(LogicCommon.getDeviceIdBySn(deviceSn));
		op.setDeviceSn(deviceSn);
		op.setCmdType(action);
		op.setCmdParam(cmdParam);
		op.setOperationTime(new Date());
		
		ZDao.saveAsy(op);
		
		
	}
	
}
