package com.moqbus.app.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Sorts.*;

import com.google.common.collect.ImmutableMap;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.moqbus.app.common.constant.JbusConst;
import com.moqbus.app.common.helper.DateHelper;
import com.moqbus.app.common.helper.JsonHelper;
import com.moqbus.app.db.MongoUtil;
import com.moqbus.app.db.bean.EventEntity;
import com.moqbus.app.logic.share.LogicCommon;

import fw.jbiz.common.conf.ZSystemConfig;
import fw.jbiz.db.ZDao;
import fw.jbiz.logic.interfaces.IResponseObject;

public class DataSaverService {

	static Logger log = Logger.getLogger(DataSaverService.class);
	
	
	// response key: topicType, deviceSn, data
	@SuppressWarnings("unchecked")
	public static void save(Map<String, Object> response) {
		String topicType = (String)response.get("topicType");
		String deviceSn = (String)response.get("deviceSn");
		
		if (JbusConst.TOPIC_PREFIX_STS.equals(topicType)) {
			saveEvent((Map<String, Object>)response.get("data"));
			return;
		}
		
		if (JbusConst.TOPIC_PREFIX_DAT.equals(topicType)) {
			Map<String, Object> data = (Map<String, Object>)response.get("data");
			if ("GET_INFO".equals(data.get("type"))) {
				data.put("deviceSn", deviceSn);
				saveDeviceInfo(data);
			}
		}
	}
	
	private static void saveEvent(Map<String, Object> data) {
		String deviceSn = (String)data.get("deviceSn");
		String time = (String)data.get("time");
		String event = (String)data.get("event");
		String memo = JsonHelper.map2json(data);
		
		EventEntity eventEntity = new EventEntity();
		eventEntity.setDeviceId(LogicCommon.getDeviceIdBySn(deviceSn));
		eventEntity.setDeviceSn(deviceSn);
		eventEntity.setEvent(event);
		eventEntity.setTime(DateHelper.fromYmdhms(time));
			
		eventEntity.setMemo(memo);
		
		ZDao.saveAsy(eventEntity);
		
	}
	
	private static void saveDeviceInfo(Map<String, Object> data) {

        MongoCollection<Document> coll = MongoUtil.getCollection(StatsService._mongoDbName, StatsService._mongoColName);
        Date date = new Date();
        String time = DateHelper.toYmdhms(date);
        data.put("time", time);
        data.put("time_year", time.substring(0, 4));
        data.put("time_month", time.substring(0, 7));
        data.put("time_day", time.substring(0, 10));
        data.put("time_hour", time.substring(0, 13));
        data.put("time_minute", time.substring(0, 16));
        
        data.put("realHeight", Float.valueOf((String) data.get("realHeight")));
        data.put("expectHeight", Float.valueOf((String) data.get("expectHeight")));
        data.put("instantFlow", Float.valueOf((String) data.get("instantFlow")));
        data.put("totalFlow", Float.valueOf((String) data.get("totalFlow")));
        data.put("moveSpeed", Float.valueOf((String) data.get("moveSpeed")));
        data.put("electricCurrent", Float.valueOf((String) data.get("electricCurrent")));
        data.put("electricVoltage", Float.valueOf((String) data.get("electricVoltage")));
        data.put("runHours", Float.valueOf((String) data.get("runHours")));
        data.put("runMinutes", Float.valueOf((String) data.get("runMinutes")));
        
        
        Document doc = Document.parse(JsonHelper.map2json(data));
        coll.insertOne(doc);
        
        log.info("saveDeviceInfo" + doc.toJson());
	}
	
}
