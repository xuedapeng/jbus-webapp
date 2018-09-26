package com.moqbus.app.wshandler;

import javax.websocket.CloseReason;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.moqbus.app.common.helper.JsonHelper;
import com.moqbus.app.service.mqtt.ControlProxy;
import com.moqbus.app.wshandler.GateHandler;
import com.moqbus.app.wshandler.base.BaseHandler;
import com.moqbus.app.wshandler.param.GateHandlerParam;

import fw.jbiz.common.helper.BeanHelper;
import fw.jbiz.ext.websocket.ZWsHandlerManager;
import fw.jbiz.ext.websocket.ZWsHandlerParam;
import fw.jbiz.ext.websocket.annotation.WsHandler;
import fw.jbiz.logic.interfaces.IResponseObject;

@WsHandler(path="gate")
public class GateHandler extends BaseHandler {

	static String CMD_OPEN = "open";
	static String CMD_QUERY = "query";
	
	static Logger logger = Logger.getLogger(GateHandler.class);
	
	String _deviceSn = null;
	
	@Override
	public boolean validate(ZWsHandlerParam handlerParam, IResponseObject response) {

		GateHandlerParam myParam = (GateHandlerParam)handlerParam;
		if (CMD_OPEN.equals(myParam.getCmd())) {
			if (myParam.getHeight() == null) {
				response.add("status", -3)
					.add("msg", "need height.");
				return false;
			}
		}
		
		logger.info(BeanHelper.dumpBean(handlerParam));
		return true;
	}

	@Override
	public void onMessage(ZWsHandlerParam handlerParam, IResponseObject response) {

		logger.info(BeanHelper.dumpBean(handlerParam));
		
		GateHandlerParam myParam = (GateHandlerParam)handlerParam;

		if (CMD_OPEN.equals(myParam.getCmd())) {
			 ControlProxy.cmdOpen(_deviceSn, myParam.getHeight());
		}

		if (CMD_QUERY.equals(myParam.getCmd())) {
			ControlProxy.cmdQueryStatus(_deviceSn);
		}
		
		response.add("status",1)
				.add("msg", "ok.")
				.add("originParam", JsonHelper.json2map(handlerParam.toJson()));
		
		respond(response);
	}

	@Override
	public void onSignIn(ZWsHandlerParam handlerParam, IResponseObject response) {

		logger.info(BeanHelper.dumpBean(handlerParam));

		GateHandlerParam myParam = (GateHandlerParam)handlerParam;
		_deviceSn = myParam.getDeviceSn();
		
		ControlProxy.regWsClient(getSession().getId(), _deviceSn);
		
		response.add("status", 1);
		response.add("msg", "sign in OK");
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

		return new GateHandlerParam();
	}

	@Override
	public boolean auth(ZWsHandlerParam handlerParam, IResponseObject response) {
		if (!super.auth(handlerParam, response)) {
			return false;
		}

		GateHandlerParam myParam = (GateHandlerParam)handlerParam;
		// todo:权限校验 userId:deviceSn
		if (StringUtils.isEmpty(myParam.getDeviceSn())) {
			response.add("status", -3)
					.add("msg", "need deviceSn.");
			return false;
		}
		
		return true;
	}



}
