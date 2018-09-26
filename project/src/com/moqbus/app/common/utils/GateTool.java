package com.moqbus.app.common.utils;

import com.moqbus.app.common.constant.JbusConst;

public class GateTool {

	public static String getDeviceSnFromTopic(String topic) {
		return topic.substring(JbusConst.TOPIC_PREFIX_LEN);
	}
}
