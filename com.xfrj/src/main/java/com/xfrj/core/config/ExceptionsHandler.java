package com.xfrj.core.config;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AccountException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xfrj.core.utils.ResponseUtil;

/**
 * 统一异常处理器
 */
@ControllerAdvice
public class ExceptionsHandler {

	@ExceptionHandler({ AccountException.class, IllegalArgumentException.class})
	@ResponseBody
	public Object handleBusiness(HttpServletRequest request, Throwable ex) {
		return ResponseUtil.warn(ex.getMessage());
	}
	
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public Object handleRuntime(HttpServletRequest request, Throwable ex) {
		return ResponseUtil.warn("系统出现异常，请联系管理员！");
	}

	@ExceptionHandler(ShiroException.class)
	@ResponseBody
	public Object handleShiro(HttpServletRequest request, Throwable ex) {
		return ResponseUtil.warn("您没有权限访问！");
	}
	
//	@ExceptionHandler(AccountException.class)
//	@ResponseBody
//	public Object handleAccount(HttpServletRequest request, Throwable ex) {
//		return ResponseUtil.warn(ex.getMessage());
//	}
//	
//	 @ExceptionHandler(IllegalArgumentException.class)
//	 @ResponseBody
//	 public Object handleIllegal(HttpServletRequest request, Throwable ex) {
//	 	return ResponseUtil.warn(ex.getMessage());
//	 }
//	
//	 @ExceptionHandler(RuntimeException.class)
//	 @ResponseBody
//	 public Object handleRuntime(HttpServletRequest request, Throwable ex) {
//	 	return ResponseUtil.warn(ex.getMessage());
//	 }

}