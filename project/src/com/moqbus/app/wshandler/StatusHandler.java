package com.moqbus.app.wshandler;

import javax.websocket.CloseReason;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.moqbus.app.common.helper.JsonHelper;
import com.moqbus.app.service.mqtt.ControlProxy;
import com.moqbus.app.wshandler.StatusHandler;
import com.moqbus.app.wshandler.base.BaseHandler;
import com.moqbus.app.wshandler.param.StatusHandlerParam;

import fw.jbiz.common.helper.BeanHelper;
import fw.jbiz.ext.websocket.ZWsHandlerParam;
import fw.jbiz.ext.websocket.annotation.WsHandler;
import fw.jbiz.logic.interfaces.IResponseObject;

@WsHandler(path="status")
public class StatusHandler extends BaseHandler {
	
	static Logger logger = Logger.getLogger(StatusHandler.class);
	
	@Override
	public boolean validate(ZWsHandlerParam handlerParam, IResponseObject response) {
		
		logger.info(BeanHelper.dumpBean(handlerParam));
		return true;
	}

	@Override
	public void onMessage(ZWsHandlerParam handlerParam, IResponseObject response) {

		logger.info(BeanHelper.dumpBean(handlerParam));
		
		
		response.add("status",-11)
				.add("msg", "本通道只接收在线状态信息，不作其它用途。");
		
		respond(response);
	}

	@Override
	public void onSignIn(ZWsHandlerParam handlerParam, IResponseObject response) {

		logger.info(BeanHelper.dumpBean(handlerParam));

		StatusHandlerParam myParam = (StatusHandlerParam)handlerParam;
		
		myParam.getDeviceSnList().forEach((s)->{
			ControlProxy.regWsClient(getSession().getId(), s);
		});
		
		response.add("status", 1);
		response.add("msg", "StatusHandler: sign in OK");
	}

	@Override
	public void onSignOut(ZWsHandlerParam handlerParam, IResponseObject response) {

		logger.info(BeanHelper.dumpBean(handlerParam));

		ControlProxy.removeWsClient(getSession().getId());
		
		response.add("status", 1);
		response.add("msg", "sign out OK");
	}

	@Override
	public void onClose(CloseReason closeReason) {
		logger.info(closeReason.toString());
		ControlProxy.removeWsClient(getSession().getId());
		
	}

	@Override
	public ZWsHandlerParam getHandlerParam() {

		return new StatusHandlerParam();
	}

	@Override
	public boolean auth(ZWsHandlerParam handlerParam, IResponseObject response) {
		if (!super.auth(handlerParam, response)) {
			return false;
		}

		StatusHandlerParam myParam = (StatusHandlerParam)handlerParam;
		// todo:权限校验 userId:deviceSn
		if (myParam.getDeviceSnList() == null
				|| myParam.getDeviceSnList().isEmpty()) {
			
			response.add("status", -3)
					.add("msg", "need deviceSnList.");
			return false;
		}
		
		return true;
	}
}
