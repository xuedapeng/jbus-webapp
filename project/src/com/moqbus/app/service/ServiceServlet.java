package com.moqbus.app.service;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.moqbus.app.service.mqtt.MqttProxy;


public class ServiceServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ServiceServlet.class);

	@Override
	public void init() {

		MqttProxy.init();
		
		
	}
}
