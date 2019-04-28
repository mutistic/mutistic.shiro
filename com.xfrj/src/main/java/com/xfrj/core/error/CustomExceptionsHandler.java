package com.xfrj.core.error;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AccountException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xfrj.core.redis.RedisCacheException;
import com.xfrj.core.utils.ResponseUtil;

/**
 * 自定义Controller统一异常处理器
 */
@ControllerAdvice
@ResponseBody
public class CustomExceptionsHandler {
	
	@ExceptionHandler({ AccountException.class, IllegalArgumentException.class, RedisCacheException.class})
	public Object handleBusiness(HttpServletRequest request, Throwable ex) {
		ex.printStackTrace();
		return ResponseUtil.warn(ex.getMessage());
	}
	
	@ExceptionHandler({Exception.class, RuntimeException.class, IOException.class})
	public Object handleRuntime(HttpServletRequest request, Throwable ex) {
		ex.printStackTrace();
		return ResponseUtil.warn("系统出现异常，请联系管理员！");
	}

	@ExceptionHandler(ShiroException.class)
	public Object handleShiro(HttpServletRequest request, Throwable ex) {
		ex.printStackTrace();
		return ResponseUtil.warn("您没有权限访问！");
	}

}