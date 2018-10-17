package com.moqbus.app.service.mqtt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.moqbus.app.common.constant.JbusConst;
import com.moqbus.app.common.helper.HexHelper;
import com.moqbus.app.common.helper.JsonHelper;
import com.moqbus.app.common.utils.CrcTool;
import com.moqbus.app.common.utils.GateTool;
import com.moqbus.app.db.bean.DeviceEntity;
import com.moqbus.app.db.dao.DeviceDao;
import com.moqbus.app.service.DataSaverService;

import fw.jbiz.ext.json.ZGsonObject;
import fw.jbiz.ext.websocket.ZWsHandlerManager;
import fw.jbiz.logic.ZDbProcessor;
import fw.jbiz.logic.interfaces.IResponseObject;

public class ControlProxy {

	static Logger log = Logger.getLogger(ControlProxy.class);

	
	static boolean serviceRunning = false;
	
	// deviceSn, List<wsSessionId>
	static Map<String, List<String>> _deviceSn2WsSessionIdsMap = new ConcurrentHashMap<String, List<String>>();
	static List<CmdCallback> _cmdCallbackList = new ArrayList<CmdCallback>();
	static List<String> _allDeviceSnList = new ArrayList<String>();
	
	public static void cmdOpen(String deviceSn, Integer height) {

		log.info(String.format("cmdOpen:deviceSn=%s, height=%d", deviceSn, height));
		
		byte[] _cmd = GateCmd.SET_HEIGHT(height);
		String _topic = JbusConst.TOPIC_PREFIX_CMD + deviceSn;
		Date _sendTime = new Date();
		MqttProxy.publish(_topic, _cmd);
		
		// 设置回调
		_cmdCallbackList.add(
				new CmdCallback() {
					
					@Override
					public Long expireTime() {
						return _sendTime.getTime() + 60000; // 60s
					}

					@Override
					public boolean condition(String topic, byte[] data) {
						
						return GateTool.getDeviceSnFromTopic(topic)
									.equals(GateTool.getDeviceSnFromTopic(_topic)) 
								&& Arrays.equals(_cmd, data);
						
					}

					@Override
					public void execute(String topic, byte[] data) {
						
						MqttProxy.publish(JbusConst.TOPIC_PREFIX_CMD + deviceSn, GateCmd.MOVE_TO_HEIGHT());
						
					}
				}
		);
	}

	public static void cmdStop(String deviceSn) {
		
		// 紧急停止

		byte[] _cmd = GateCmd.STOP();
		String _topic = JbusConst.TOPIC_PREFIX_CMD + deviceSn;
		MqttProxy.publish(_topic, _cmd);
		
		log.info(String.format("cmdStop:deviceSn=%s", deviceSn));
	}

	public static void cmdQueryStatus(String deviceSn) {

		// get info
		MqttProxy.publish(JbusConst.TOPIC_PREFIX_CMD + deviceSn, GateCmd.GET_INFO());
		
		// 订阅在线状态和数据
		MqttProxy.subscribe(JbusConst.TOPIC_PREFIX_STS + deviceSn);
		MqttProxy.subscribe(JbusConst.TOPIC_PREFIX_DAT + deviceSn);

	}
	
	public static void receiveDat(String topic, byte[] data) {

		log.info(
				String.format("receive: topic=%s, data=[%s]", 
						topic, 
						HexHelper.bytesToHexString(data)));
		
		String topicType = topic.substring(0, JbusConst.TOPIC_PREFIX_DAT.length());
		String deviceSn = topic.substring(JbusConst.TOPIC_PREFIX_DAT.length());
		
		IResponseObject response = new ZGsonObject();
		try {
			response.add("topicType", topicType)
				.add("deviceSn", deviceSn)	
				.add("data", 
						topicType.equals(JbusConst.TOPIC_PREFIX_STS)
							? JsonHelper.json2map(new String(data, "UTF-8"))
							: GateData.decodeData(data)
					);
		} catch (UnsupportedEncodingException e) {
			log.error("", e);
			return;
		}
			
		if (!topicType.equals(JbusConst.TOPIC_PREFIX_STS)) {
			response.add("origin", HexHelper.bytesToHexString(data));
		}
			
		// 回调
		List<CmdCallback> tobeRemovedList = new ArrayList<CmdCallback>();
		List<CmdCallback> tobeRunList = new ArrayList<CmdCallback>();
		for (int i=0; i<_cmdCallbackList.size(); i++ ) {

			log.info("before: _cmdCallbackList.size=" + _cmdCallbackList.size());
			
			CmdCallback cb= _cmdCallbackList.get(i);
			
			if (cb.expireTime() < new Date().getTime()) {
				
				tobeRemovedList.add(cb);
				
			} else {
			
				if(cb.condition(topic, data)) {
					tobeRunList.add(cb);
					if (cb.once()) {
						tobeRemovedList.add(cb);
					}
				}
			}

			tobeRunList.forEach((E)->{
				E.execute(topic, data);
			});
			
			tobeRemovedList.forEach((E)->{
				_cmdCallbackList.remove(E);
			});
			
			tobeRemovedList.clear();
			tobeRunList.clear();
			
			log.info("after: _cmdCallbackList.size=" + _cmdCallbackList.size());
		}
			
		// 保存数据
		DataSaverService.save(JsonHelper.json2map(response.toString()));
		// 推送前端
		List<String> sessionIdList = _deviceSn2WsSessionIdsMap.get(deviceSn);
		if (sessionIdList != null) {
			ZWsHandlerManager.send(response, sessionIdList);
		}
	}
	
	public static void regWsClient(String wsSessionId, String deviceSn) {
		if (!_deviceSn2WsSessionIdsMap.containsKey(deviceSn)) {
			_deviceSn2WsSessionIdsMap.put(deviceSn, new ArrayList<String>());
		}
		if (!_deviceSn2WsSessionIdsMap.get(deviceSn).contains(wsSessionId)) {
			_deviceSn2WsSessionIdsMap.get(deviceSn).add(wsSessionId);
		}
		
		// 立即查询状态数据
		cmdQueryStatus(deviceSn);
		// 订阅在线状态和数据
//		MqttProxy.subscribe(JbusConst.TOPIC_PREFIX_STS + deviceSn);
//		MqttProxy.subscribe(JbusConst.TOPIC_PREFIX_DAT + deviceSn);
		
//		if (!serviceRunning) {
//			serviceRunning = true;
//			queryStatusService();
//		}
		
	}

	public static void removeWsClient(String wsSessionId) {
		_deviceSn2WsSessionIdsMap.forEach((K,V)->{
			if (V.contains(wsSessionId)) {
				V.remove(wsSessionId);
			}
			if (V.isEmpty()) {
				_deviceSn2WsSessionIdsMap.remove(K);
			}
		});
	}
	
	// 轮询设备数据与状态
	public static void queryAndSubscribeService() {
		
		int count = 0;
		Date lastTime = new Date();
		while(true) {
			log.info("count="+count);
			try {
//				Thread.sleep(10000); // 每10秒查询一次最新信息 
				for (int i = 0; i<10; i++) {
					Thread.sleep(1000);
					Date nowDate = new Date();
					if (nowDate.getTime()/1000 - lastTime.getTime()/1000 >= 10) {
						lastTime = nowDate;
						break;
					}
				}
				
			} catch (InterruptedException e) {
				log.error("", e);
			}
			

			log.info("count%10==0:"+(count%10==0));
			if(count%10 == 0) {
				log.info("refreshDeviceSnList start");
				refreshDeviceSnList();
			}
			
			for (String deviceSn: _allDeviceSnList) {
				log.info("cmdQueryStatus:" + deviceSn);
				try {
					cmdQueryStatus(deviceSn);
				} catch(Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			
			if (count > 1000) {
				count = 0;
			} else {
				count++;
			}
		}
	}
	
	private static void refreshDeviceSnList() {
		new ZDbProcessor() {

			@Override
			public void execute(EntityManager em) {
				DeviceDao dao = new DeviceDao(em);
				_allDeviceSnList = dao.findAll().stream().map(DeviceEntity::getDeviceSn).collect(Collectors.toList());
				
				_allDeviceSnList.clear();
				List<DeviceEntity> list = dao.findAll();

				for (DeviceEntity device: list) {
					_allDeviceSnList.add(device.getDeviceSn());
				}
				
				log.info("_allDeviceSnList.size="+_allDeviceSnList.size());
			}
		}.run();
	}
	
	public static void reSubscribe() {

//		_deviceSn2WsSessionIdsMap.forEach((K, V) -> {
//
//			MqttProxy.subscribe(JbusConst.TOPIC_PREFIX_STS + K);
//			MqttProxy.subscribe(JbusConst.TOPIC_PREFIX_DAT + K);
//		});
	}
	
}
