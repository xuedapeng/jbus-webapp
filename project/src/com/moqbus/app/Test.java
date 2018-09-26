package com.moqbus.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.moqbus.app.common.helper.JsonHelper;

public class Test {

	public static void main(String[] args) {
		
		Map<String, Object> bMap = new HashMap<String, Object>();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		Map<String, String> sMap = new HashMap<String, String>();
		sMap.put("a", "1");
		sMap.put("b", "2");
		list.add(sMap);
		
		sMap = new HashMap<String, String>();
		sMap.put("c", "3");
		sMap.put("d", "4");
		list.add(sMap);
		
		bMap.put("u", 1);
		bMap.put("data", list);
		
		String json = JsonHelper.map2json(bMap);
		System.out.println(json);
		
		Map<String, Object> jsonMap = JsonHelper.json2map(json);
		List data = (List) jsonMap.get("data");
		
		data.forEach((E)->System.out.println(((Map)E).get("a")));
	}
}
