package com.xfrj.core.error;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xfrj.core.controller.BaseController;

@RestController
public class CustomErrorController extends BaseController {

	@GetMapping("/notLogin")
	public Object notLogin() {
		return warn("您还没登陆！");
	}
	@GetMapping("/notRole")
	public Object notRole() {
		return warn("您没有访问权限！");
	}
	@GetMapping("/404")
	public Object error404() {
		return warn("资源不存在！");
	}
	@GetMapping("/500")
	public Object error500() {
		return warn("系统出现异常，请联系管理员！");
	}
}
