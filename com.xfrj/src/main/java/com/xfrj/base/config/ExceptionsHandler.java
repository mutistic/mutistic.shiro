package com.xfrj.base.config;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AccountException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.xfrj.base.utils.ResponseUtil;

@ControllerAdvice
public class ExceptionsHandler {

	@ExceptionHandler(ShiroException.class)
	public Object handleShiro(HttpServletRequest request, Throwable ex) {
	    return ResponseUtil.warn("您没有权限访问！");
	}
	
	@ExceptionHandler(AccountException.class)
	public Object handleAccount(HttpServletRequest request, Throwable ex) {
	    return ResponseUtil.warn(ex.getMessage());
	}
	
}