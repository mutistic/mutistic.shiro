package com.xfrj.core.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xfrj.core.controller.BaseController;
import com.xfrj.core.utils.ValidateUtil;
import com.xfrj.user.model.UserEntity;

@RestController
public class TokenDemoController extends BaseController {

	/**
	 * APP登录
	 * @param entity
	 * @return
	 */
	@PostMapping("/applogin")
	public Object login(@RequestBody UserEntity entity) {
		ValidateUtil.notNull(entity, "登录信息");
		ValidateUtil.notNull(entity.getUsername(), "用户名");
		ValidateUtil.notNull(entity.getPassword(), "密码");
		
		// 从SecurityUtils里边创建一个 subject
		Subject subject = SecurityUtils.getSubject();
		// 在认证提交前准备 token（令牌）
		UsernamePasswordToken token = new UsernamePasswordToken(entity.getUsername(), entity.getPassword());
		// 执行认证登陆
		subject.login(token);
		return success(subject.getPrincipal(), "APP登录成功");
	}
	
	@GetMapping("/app/getMessage")
    public Object getMessage() {
        return success("token验证成功，可以获得该接口的信息！");
    }
	
}
