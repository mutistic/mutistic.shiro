package com.xfrj.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ResponseUtil {
	private static final Logger log = LoggerFactory.getLogger(ResponseUtil.class);

	public static ModelMap success(Object data) {
		return success(data, HttpStatus.OK.getReasonPhrase());
	}
	
	public static ModelMap success(Object data, String msg) {
		return message(data, HttpStatus.OK, msg);
	}
	
	public static ModelMap warn(String msg) {
		return message(msg, HttpStatus.NOT_FOUND);
	}
	
	public static ModelMap message(String msg, HttpStatus status) {
		return message(null, status, msg);
	}
	
	
	public static ModelMap message(Object data, HttpStatus status, String msg) {
		ModelMap model = new ModelMap();
		model.put("data", JSONObject.toJSON(data));
		model.put("code", status.value());
		model.put("msg", msg == null ? status.getReasonPhrase() : msg);
		model.put("timestamp", System.currentTimeMillis());
		log.info("response===> " + JSON.toJSONString(model));
		return model;
	}
	
}
