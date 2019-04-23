package com.xfrj.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xfrj.core.controller.BaseController;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

	@GetMapping("/getMessage")
	public Object submitLogin() {
		return success("您拥有获得该接口的信息的权限！");
	}
}
