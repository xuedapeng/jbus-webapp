package com.moqbus.app.wshandler.base;


import fw.jbiz.common.helper.BeanHelper;
import fw.jbiz.ext.websocket.ZWsHandlerParam;

public abstract class BaseHandlerParam extends ZWsHandlerParam {

	@Override
	public ZWsHandlerParam fromJson(String jsonMsg) {

		BeanHelper.setValuesFromJson(this, jsonMsg);
		
		return this;
	}

	@Override
	public String toJson() {
		
		return "{" + BeanHelper.dumpBean(this) + "}";
	}
}
