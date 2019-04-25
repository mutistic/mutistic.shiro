package com.xfrj.core.error;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 自定义异常错误界面
 */
@Component
public class CustomErrorPageRegistrar implements ErrorPageRegistrar {

	@Override
	public void registerErrorPages(ErrorPageRegistry registry) {
		ErrorPage[] pageArray = new ErrorPage[3];
		pageArray[0] = new ErrorPage(HttpStatus.UNAUTHORIZED, "/unauthorized");  // 配置401错误状态码，跳转的界面
		pageArray[1] = new ErrorPage(HttpStatus.NOT_FOUND, "/404");  // 配置404错误状态码，跳转的界面
		pageArray[2] = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500"); // 配置500错误状态码，跳转的界面
		registry.addErrorPages(pageArray); 
	}

}
