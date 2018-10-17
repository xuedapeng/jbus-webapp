package com.moqbus.app.service;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Sorts.ascending;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.common.collect.ImmutableMap;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.moqbus.app.common.helper.DateHelper;
import com.moqbus.app.common.helper.JsonHelper;
import com.moqbus.app.db.MongoUtil;

import fw.jbiz.common.conf.ZSystemConfig;

public class StatsService {

	public static String _mongoDbName = ZSystemConfig.getProperty("mongo.db");
	public static String _mongoColName = ZSystemConfig.getProperty("mongo.collection");
	
	
	// db.getCollection('shuizha@dezhou').aggregate([{$match:{time:{$gt:'2018-10-12 16:31:00'}}},{$group:{_id:{deviceSn:'$deviceSn',time_minute:'$time_minute'}, avgFlow:{$avg:'$instantFlow'}}}, {$sort:{_id:1}}])
	// db.getCollection('shuizha@dezhou').aggregate([{$match:{time:{$gt:'2018-10-12 16:30:00'},deviceSn:'4755B9F3'}},{$group:{_id:'$time_minute', avgFlow:{$avg:'$instantFlow'}}}, {$sort:{_id:1}}])
	
	public static List<Object>  queryAvgFlow(String deviceSn, Date fromTime) {
		MongoCollection<Document> coll = MongoUtil.getCollection(_mongoDbName, _mongoColName);
		List<Bson> pipeline = Arrays.asList(
				match(gt("time", DateHelper.toYmdhms(fromTime))), 
				match(eq("deviceSn", deviceSn)), 
				group("$time_minute", avg("avgFlow", "$instantFlow")),
				sort(ascending("_id")));
		
		AggregateIterable<Document> iterable = coll.aggregate(pipeline);
		
		final List<Object> retList = new ArrayList<Object>();
		iterable.forEach(new Block<Document>() {
            public void apply(final Document document) {
            	Map<String, String> map = ImmutableMap.of(
            			"time", (String)document.get("_id"), 
            			"avgFlow", document.get("avgFlow").toString());
            	
            	retList.add(map);
            }
        });
		
		return retList;
	}
	
	//打印聚合结果
	private static void printResult(String doing, List<Object> list) {
		System.out.println(doing);
		System.out.println(JsonHelper.list2json(list));
        
        System.out.println();
	}
	
	public static void main(String[] args) {
		printResult("MMM", queryAvgFlow("4755B9F3", DateHelper.fromYmdhms("2018-10-12 16:30:00")));
	}
	
}
