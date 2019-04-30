package com.xfrj.login.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xfrj.core.controller.BaseController;
import com.xfrj.core.utils.ValidateUtil;
import com.xfrj.login.shiro.UserDto;
import com.xfrj.login.shiro.token.TokenUtil;
import com.xfrj.user.model.UserEntity;

@RestController
@RequestMapping("/app")
public class LoginController extends BaseController {

	/**
	 * APP登录
	 * 
	 * @param entity
	 * @return
	 */
	@PostMapping("/login")
	public Object login(@RequestBody UserEntity entity) {
		ValidateUtil.notNull(entity, "登录信息");
		ValidateUtil.notNull(entity.getUsername(), "用户名");
		ValidateUtil.notNull(entity.getPassword(), "密码");

		Subject subject = SecurityUtils.getSubject();
		UserDto oldDto = (UserDto) SecurityUtils.getSubject().getPrincipal();
		
		// 根据token执行认证登陆
		subject.login(new UsernamePasswordToken(entity.getUsername(), entity.getPassword()));
		UserDto dto = (UserDto) subject.getPrincipal();
		// 针对未退出重新登陆操作时删除旧token
		if(oldDto != null && oldDto.getToken() != null && dto.getId().equals(oldDto.getId())) {
			TokenUtil.deleteTokenInfo(oldDto.getToken());
		}
		TokenUtil.notNull(dto, "用户名/密码错误，登录失败！");
		TokenUtil.setTokenInfo(dto);
		
		dto.setPassword(null);
		dto.setExpirationTime(null);
		return success(dto, "APP登录成功!");
	}
	
	@GetMapping("/getMessage")
	public Object getMessage() {
		return success("您拥有获得该接口的信息的权限！");
	}

	@GetMapping("/loginout")
	public Object loginout() {
		UserDto dto = (UserDto) SecurityUtils.getSubject().getPrincipal();
		if(dto == null) {
			return warn("退出失败，shiro无登陆信息！");
		}
		
		TokenUtil.deleteTokenInfo(dto.getToken());
		SecurityUtils.getSubject().logout();
		return success("已成功退出！");
	}
}