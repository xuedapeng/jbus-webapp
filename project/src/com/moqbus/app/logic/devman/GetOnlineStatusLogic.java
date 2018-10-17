package com.moqbus.app.logic.devman;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.google.common.collect.ImmutableMap;
import com.moqbus.app.common.helper.JsonBuilder;
import com.moqbus.app.common.helper.JsonHelper;
import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.devman.param.GetOnlineStatusLogicParam;
import com.moqbus.app.logic.share.annotation.Action;

import fw.jbiz.common.conf.ZSystemConfig;
import fw.jbiz.common.helper.httpclient.HttpHelper;
import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="devman.onlinestatus.get")
public class GetOnlineStatusLogic extends BaseZLogic {

	final static String URL = ZSystemConfig.getProperty("jbus.cloud.url");
	final static String SECRET_ID = ZSystemConfig.getProperty("jbus.cloud.secret.id");
	final static String SECRET_KEY = ZSystemConfig.getProperty("jbus.cloud.secret.key");
	final static String METHOD = "realtime.device.online.query";
	
	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		GetOnlineStatusLogicParam myParam = (GetOnlineStatusLogicParam)logicParam;
			
		Map<String, Object> map = getOnlineStatus(myParam.getDeviceSnList());
		
		res.add("status", map.get("status"))
			.add("msg", map.get("msg"))
			.add("result", map.get("result"));
		
		return true;
		
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		GetOnlineStatusLogicParam myParam = (GetOnlineStatusLogicParam)logicParam;
		
		if (myParam.getDeviceSnList() == null 
				|| myParam.getDeviceSnList().isEmpty()) {
			

			res.add("status", -3)
				.add("msg", "need deviceSnList");
			
			return false;
		}
		
		return true;
	}
	
	public static Map<String, Object> getOnlineStatus(List<String> deviceSnList){

		String response = HttpHelper.doPost(
				URL, 
				JsonBuilder.build()
					.add("method", METHOD)
					.add("auth", new String[]{SECRET_ID, SECRET_KEY})
					.add("data", ImmutableMap.of("deviceIds", deviceSnList)
					).toString());
		
		Map<String, Object> map = JsonHelper.json2map(response);
		
		return map;
	}

}
