package com.mutistic.shiro.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;

import com.mutistic.shiro.core.utils.ResponseUtil;

public class BaseController {

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
