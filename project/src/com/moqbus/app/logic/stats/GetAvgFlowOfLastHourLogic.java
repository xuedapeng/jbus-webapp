package com.moqbus.app.logic.stats;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.moqbus.app.logic.BaseZLogic;
import com.moqbus.app.logic.share.annotation.Action;
import com.moqbus.app.logic.stats.param.GetAvgFlowOfLastHourLogicParam;
import com.moqbus.app.service.StatsService;

import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.logic.ZLogicParam;

@Action(method="stats.avgflow.lasthour")
public class GetAvgFlowOfLastHourLogic extends BaseZLogic {

	@Override
	protected boolean execute(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		GetAvgFlowOfLastHourLogicParam myParam = (GetAvgFlowOfLastHourLogicParam)logicParam;
		
		Date fromTime = new Date(new Date().getTime() - 60*60*1000); // 1小时前～
		
		List<Object> resultList = StatsService.queryAvgFlow(myParam.getDeviceSn(), fromTime);
		
		res.add("status", 1)
			.add("msg", "stats.avgflow.lasthour ok. ")
			.add("result", resultList);
		
		return true;
	}

	@Override
	protected boolean validate(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em) throws Exception {

		GetAvgFlowOfLastHourLogicParam myParam = (GetAvgFlowOfLastHourLogicParam)logicParam;
		
		if (StringUtils.isEmpty(myParam.getDeviceSn())) {
			res.add("status", -3)
				.add("msg", "need deviceSn.");
			
			return false;
		}
		
		return true;
	}

}
