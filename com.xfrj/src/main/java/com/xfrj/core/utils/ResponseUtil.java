package com.xfrj.core.utils;

import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 响应数据工具类
 */
public class ResponseUtil {
	private static final Logger log = LoggerFactory.getLogger(ResponseUtil.class);
	/**
	 * 返回200
	 * @param data 返回数据
	 * @return
	 */
	public static ModelMap success(Object data) {
		return success(data, HttpStatus.OK.getReasonPhrase());
	}
	/**
	 * 返回200
	 * @param data 返回数据
	 * @param msg 返回msg
	 * @return
	 */
	public static ModelMap success(Object data, String msg) {
		return message(data, msg, HttpStatus.OK);
	}
	/**
	 * 返回404
	 * @param msg 返回msg
	 * @return
	 */
	public static ModelMap warn(String msg) {
		return message(msg, HttpStatus.NOT_FOUND);
	}
	/**
	 * 返回HttpStatus
	 * @param msg 返回msg
	 * @param status 返回HttpStatus状态码
	 * @return
	 */
	public static ModelMap message(String msg, HttpStatus status) {
		return message(null, msg, status);
	}
	/**
	 * 返回HttpStatus
	 * @param data 返回数据
	 * @param msg 返回msg
	 * @param status 返回HttpStatus状态码
	 * @return
	 */
	public static ModelMap message(Object data, String msg, HttpStatus status) {
		ModelMap model = new ModelMap();
		model.put("timestamp", new Date());
		// data格式： data:[{},{}] or data:{}
//		model.put("data", JSONObject.toJSON(data));
		// data格式： data:[{},{}]
		if(data == null) {
			model.put("data", new ArrayList<Object>());
		} else if(DataTypeUtil.isCollection(data)) {
			model.put("data", JSONObject.toJSON(data));
		} else {
			model.put("data", JSONObject.toJSON(new ArrayList<Object>() {
				{
					add(data);
				}
			}));
		}
		model.put("status", status.value());
		model.put("message", msg == null ? status.getReasonPhrase() : msg);
		model.put("timemillis", System.currentTimeMillis());
		log.info("response===> " + JSON.toJSONString(model));
		return model;
	}
	
}
