package com.xfrj.login.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xfrj.core.controller.BaseController;
import com.xfrj.core.security.SecurityUtil;
import com.xfrj.core.utils.ValidateUtil;
import com.xfrj.login.dto.UserParams;
import com.xfrj.login.regiest.RegiestUtil;
import com.xfrj.login.shiro.UserDto;
import com.xfrj.login.shiro.UserToken;
import com.xfrj.login.shiro.token.TokenUtil;
import com.xfrj.user.model.UserEntity;
import com.xfrj.user.service.ILoginService;

@RestController
@RequestMapping("/sys")
public class SysLoginController extends BaseController {

	@Autowired
	private ILoginService loginService;
	
	/**
	 * 管理员登录
	 * 
	 * @param entity
	 * @return
	 */
	@PostMapping("/login")
	public Object login(@RequestBody UserParams data) {
		ValidateUtil.notNull(data, "登录信息");
		ValidateUtil.notNull(data.getUsername(), "用户名");
		ValidateUtil.notNull(data.getPassword(), "密码");

		data.setPassword(SecurityUtil.encryptPassword(data.getPassword()));
		Subject subject = SecurityUtils.getSubject();
		// 根据token执行认证登陆
		UserToken token = new UserToken(data.getUsername(), data.getPassword(), 1);
		token.setRememberMe(data.getIsRememberme() ? true : false);
		subject.login(token); 
		
		UserDto dto = (UserDto) subject.getPrincipal();
		TokenUtil.notNull(dto, "用户名/密码错误，登录失败！");
		TokenUtil.deleteLoginNum(dto.getId());
		
		dto.setAuthorType(null);
		dto.setPassword(null);
		dto.setExpirationTime(null);
		return success(dto, "管理员登录成功!");
	}
	
	/**
	 * 管理员注册
	 * 
	 * @param entity
	 * @return
	 */
	@PostMapping("/register")
	public Object register(@RequestBody UserParams data) {
		ValidateUtil.notNull(data, "注册信息");
		ValidateUtil.notNull(data.getUsername(), "用户名");
		ValidateUtil.notNull(data.getPassword(), "密码");
		ValidateUtil.notNull(data.getRegiestMobile(), "手机号");
		ValidateUtil.notNull(data.getRegiestCode(), "验证码");
		
		String code = RegiestUtil.getCodeInfo(data.getRegiestMobile());
		if(!data.getRegiestCode().equals(code)) {
			ValidateUtil.exception("验证码错误或已失效，请重新获取验证码！");
		}
		
		// 注册
		UserEntity entity = new UserEntity();
		entity.setUsername(data.getUsername());
		entity.setPassword(SecurityUtil.encryptPassword(data.getPassword()));
		entity.setAuthorType(1);
		entity = loginService.register(entity);
		RegiestUtil.deleteCode(data.getRegiestMobile());
		
		Subject subject = SecurityUtils.getSubject();
		subject.login(new UserToken(entity.getUsername(), entity.getPassword(), entity.getAuthorType()));
		UserDto dto = (UserDto) subject.getPrincipal();
		TokenUtil.deleteLoginNum(dto.getId());
		
		dto.setAuthorType(null);
		dto.setPassword(null);
		dto.setExpirationTime(null);
		return success(dto, "管理员注册登录成功!");
	}
	
	
	@GetMapping("/getMessage")
	public Object getMessage() {
		return success("您拥有管理员权限！");
	}

	@GetMapping("/loginout")
	public Object loginout() {
		UserDto dto = (UserDto) SecurityUtils.getSubject().getPrincipal();
		if(dto == null) {
			return warn("退出失败，shiro无登陆信息！");
		}
		
		SecurityUtils.getSubject().logout();
		return success("已成功退出！");
	}
}
