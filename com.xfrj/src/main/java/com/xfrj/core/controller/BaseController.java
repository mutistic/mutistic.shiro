package com.xfrj.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;

import com.xfrj.core.utils.ResponseUtil;

public class BaseController {
	
	
	private static final Logger log = LoggerFactory.getLogger(BaseController.class);

	public ModelMap success(Object data) {
		return ResponseUtil.success(data);
	}
	public ModelMap success(Object data, String msg) {
		return ResponseUtil.success(data, msg);
	}
	public ModelMap warn(String msg) {
		return ResponseUtil.warn(msg);
	}
	public ModelMap message(String msg, HttpStatus status) {
		return ResponseUtil.message(msg, status);
	}
	public ModelMap message(Object data, String msg, HttpStatus status) {
		return ResponseUtil.message(data, msg, status);
	}
}
