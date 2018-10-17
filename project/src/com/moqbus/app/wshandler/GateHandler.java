package com.moqbus.app.wshandler;

import javax.websocket.CloseReason;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.moqbus.app.common.helper.JsonHelper;
import com.moqbus.app.service.OperationLoggerService;
import com.moqbus.app.service.mqtt.ControlProxy;
import com.moqbus.app.wshandler.GateHandler;
import com.moqbus.app.wshandler.base.BaseHandler;
import com.moqbus.app.wshandler.param.GateHandlerParam;

import fw.jbiz.common.helper.BeanHelper;
import fw.jbiz.ext.json.ZGsonObject;
import fw.jbiz.ext.websocket.ZWsHandlerManager;
import fw.jbiz.ext.websocket.ZWsHandlerParam;
import fw.jbiz.ext.websocket.annotation.WsHandler;
import fw.jbiz.logic.interfaces.IResponseObject;

@WsHandler(path="gate")
public class GateHandler extends BaseHandler {

	static String CMD_OPEN = "open";
	static String CMD_STOP = "stop";
	
	static Logger logger = Logger.getLogger(GateHandler.class);
	
	String _deviceSn = null;
	String _secretId = null;
	
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

		if (CMD_STOP.equals(myParam.getCmd())) {
			ControlProxy.cmdStop(_deviceSn);
		}
		
		// 保存操作日志
		OperationLoggerService.save(myParam.getCmd(), _secretId, _deviceSn, getCmdParam(myParam));
		
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
		_secretId = myParam.getSecretId();
		
		ControlProxy.regWsClient(getSession().getId(), _deviceSn);

		// 保存操作日志
		OperationLoggerService.save("sign_in", _secretId, _deviceSn, "");
		
		response.add("status", 1);
		response.add("msg", "sign in OK");
	}

	@Override
	public void onSignOut(ZWsHandlerParam handlerParam, IResponseObject response) {

		logger.info(BeanHelper.dumpBean(handlerParam));

		ControlProxy.removeWsClient(getSession().getId());

		// 保存操作日志
		OperationLoggerService.save("sign_out", _secretId, _deviceSn, "");
		
		response.add("status", 1);
		response.add("msg", "sign out OK");
	}

	@Override
	public void onClose(CloseReason closeReason) {
		logger.info(closeReason.toString());
		ControlProxy.removeWsClient(getSession().getId());

		// 保存操作日志
		OperationLoggerService.save("close", _secretId, _deviceSn, closeReason.toString());
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


	private static String getCmdParam(GateHandlerParam param) {
		IResponseObject res = new ZGsonObject();
		res.add("height", param.getHeight());
		
		return res.toString();
	}


}
