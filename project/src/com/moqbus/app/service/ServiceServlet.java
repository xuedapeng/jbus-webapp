package com.moqbus.app.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.moqbus.app.db.bean.DeviceEntity;
import com.moqbus.app.db.bean.EventEntity;
import com.moqbus.app.db.dao.DeviceDao;
import com.moqbus.app.logic.devman.GetOnlineStatusLogic;
import com.moqbus.app.logic.share.LogicCommon;
import com.moqbus.app.service.mqtt.ControlProxy;
import com.moqbus.app.service.mqtt.MqttProxy;

import fw.jbiz.db.ZDao;
import fw.jbiz.logic.ZDbProcessor;


public class ServiceServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ServiceServlet.class);

	@Override
	public void init() {

		MqttProxy.init();

		// 连续发布／订阅
		new QueryAndSubscribeThread().start();
		
		// 在线状态更新
		new ZDbProcessor() {

			@Override
			public void execute(EntityManager em) {
				updateStatusOnStart(em);
			}
			
		}.run();
		
		
	}
	
	@SuppressWarnings("unchecked")
	private void updateStatusOnStart(EntityManager em) {
		DeviceDao dao = new DeviceDao(em);
		List<DeviceEntity> list = dao.findAll();
		
		List<String> deviceSnList = list.stream().map(DeviceEntity::getDeviceSn).collect(Collectors.toList());
		Map<String, Object> map = GetOnlineStatusLogic.getOnlineStatus(deviceSnList);
		
		if (Integer.valueOf((String) map.get("status")) < 0) {
			return;
		}
		
		Map<String, String> result = (Map<String, String>)map.get("result");
		list.forEach((device)->{
			EventEntity eventEntity = new EventEntity();
			eventEntity.setDeviceId(device.getId());
			eventEntity.setDeviceSn(device.getDeviceSn());
			eventEntity.setEvent(result.get(device.getDeviceSn()));
			eventEntity.setTime(new Date());
			eventEntity.setMemo("on webapp start");
			
			ZDao.saveAsy(eventEntity);
		});
	}
	
}
