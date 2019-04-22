package com.xfrj.base.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BaseController {

	private static final Logger log = LoggerFactory.getLogger(BaseController.class);

	// httpstatus https://www.cnblogs.com/DeasonGuan/articles/Hanami.html
	public ResponseEntity<ModelMap> success(Object data) {
		return success(data, HttpStatus.OK.getReasonPhrase());
	}
	
	public ResponseEntity<ModelMap> success(Object data, String msg) {
		return message(JSONObject.toJSON(data), HttpStatus.OK, null);
	}

	public ResponseEntity<ModelMap> warn(String msg) {
		return message(msg, HttpStatus.NOT_FOUND);
	}
	
	public ResponseEntity<ModelMap> message(String msg, HttpStatus status) {
		return message(null, status, msg);
	}
	
	public ResponseEntity<ModelMap> token(String token) {
		ModelMap mode = new ModelMap();
		mode.put("code", HttpStatus.OK.value());
		mode.put("msg", HttpStatus.OK.getReasonPhrase());
		mode.put("timestamp", System.currentTimeMillis());
		mode.put("token", token);
		log.info("response===> " + JSON.toJSONString(mode));
		return ResponseEntity.ok(mode);
	}
	
	public ResponseEntity<ModelMap> message(Object data, HttpStatus status, String msg) {
		ModelMap mode = new ModelMap();
		if(data != null) {
			mode.put("data", data);
		}
		mode.put("code", status.value());
		mode.put("msg", msg == null ? status.getReasonPhrase() : msg);
		mode.put("timestamp", System.currentTimeMillis());
		log.info("response===> " + JSON.toJSONString(mode));
		return ResponseEntity.ok(mode);
	}
}
