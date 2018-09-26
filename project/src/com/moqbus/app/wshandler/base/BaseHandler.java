package com.moqbus.app.wshandler.base;

import fw.jbiz.ext.websocket.ZWsHandler;
import fw.jbiz.ext.websocket.ZWsHandlerParam;
import fw.jbiz.logic.interfaces.IResponseObject;

public abstract class BaseHandler extends ZWsHandler {

	@Override
	public boolean auth(ZWsHandlerParam handlerParam, IResponseObject response) {

		return true;
	}
}
