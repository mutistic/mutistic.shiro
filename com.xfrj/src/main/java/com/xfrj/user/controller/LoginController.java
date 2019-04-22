package com.xfrj.user.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xfrj.base.controller.BaseController;
import com.xfrj.user.model.UserEntity;

@RestController
public class LoginController extends BaseController{

	/**
	 * 登陆
	 * @param username 用户名
	 * @param password 密码
	 */
	@PostMapping("/app.login")
	public Object login(@RequestBody UserEntity entity) {
		// 从SecurityUtils里边创建一个 subject
		Subject subject = SecurityUtils.getSubject();
		// 在认证提交前准备 token（令牌）
		UsernamePasswordToken token = new UsernamePasswordToken(entity.getUserName(), entity.getPassword());
		// 执行认证登陆
		subject.login(token);
		ModelMap model = success(null, "APP登录成功");
		model.put("token", "token");
		return model;
		
		// 根据权限，指定返回数据
//		String role = "user";
//		if ("user".equals(role)) {
//			ModelMap model = success(null, "APP登录成功");
//			model.put("token", "token");
//			return model;
//		}
//		
//		return warn("您没有访问权限！");
	}
}