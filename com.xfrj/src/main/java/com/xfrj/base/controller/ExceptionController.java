package com.xfrj.base.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController extends BaseController {

	@GetMapping("/notLogin")
	public Object notLogin() {
		return warn("您还没登陆！");
	}
	@GetMapping("/notRole")
	public Object notRole() {
		return warn("您没有访问权限！");
	}
	@GetMapping("/logout")
	public Object logout() {
		SecurityUtils.getSubject().logout(); //注销
		return message("您已成功退出！", HttpStatus.OK);
	}
}
