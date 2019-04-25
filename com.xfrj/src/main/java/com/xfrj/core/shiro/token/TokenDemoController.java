package com.xfrj.core.shiro.token;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xfrj.core.controller.BaseController;
import com.xfrj.core.utils.ValidateUtil;
import com.xfrj.user.model.UserEntity;

@RestController
@RequestMapping("/token")
public class TokenDemoController extends BaseController {

	/**
	 * APP登录
	 * 
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

		Session session = subject.getSession(true);
		// 主机
		System.out.println("host:" + session.getHost());
		// session超时时间
		session.setTimeout(1500000);
		System.out.println("timeout:" + session.getTimeout());
		// 属性参数值
		session.setAttribute("name", subject.getPrincipal());
		System.out.println("name:" + session.getAttribute("name"));
		session.removeAttribute("name");
		System.out.println("attributekeys:" + session.getAttributeKeys());
		// id
		System.out.println("id:" + session.getId());
		// 访问时间(创建session的时间和最后访问session的时间)
		System.out.println("lastAccessTime:" + session.getLastAccessTime());
		session.touch();// 更新会话访问时间
		System.out.println("StartTimestamp:" + session.getStartTimestamp());

		return success(subject.getPrincipal(), "APP登录成功");
	}

	@GetMapping("/app/getMessage")
	public Object getMessage() {
		return success("token验证成功，可以获得该接口的信息！");
	}

	@GetMapping("/app/loginout")
	public Object loginout() {
		SecurityUtils.getSubject().logout();
		return success("已成功退出！");
	}
}
