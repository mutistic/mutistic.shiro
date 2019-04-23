package com.xfrj.user.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xfrj.core.controller.BaseController;
import com.xfrj.core.utils.ValidateUtil;
import com.xfrj.user.model.UserEntity;

/**
 * 登录Controller
 */
@RestController
public class LoginController extends BaseController{

	/**
	 * APP登录
	 * @param entity
	 * @return
	 */
	@PostMapping("/applogin")
	public Object login(@RequestBody UserEntity entity) {
		ValidateUtil.notNull(entity, "登录信息");
		ValidateUtil.notNull(entity.getUserName(), "用户名");
		ValidateUtil.notNull(entity.getPassword(), "密码");
		
		// 从SecurityUtils里边创建一个 subject
		Subject subject = SecurityUtils.getSubject();
		// 在认证提交前准备 token（令牌）
		UsernamePasswordToken token = new UsernamePasswordToken(entity.getUserName(), entity.getPassword());
		// 执行认证登陆
		subject.login(token);
		return success(subject.getPrincipal(), "APP登录成功");
	}
}