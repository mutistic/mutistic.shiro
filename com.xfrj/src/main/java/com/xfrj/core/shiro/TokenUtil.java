package com.xfrj.core.shiro;

import java.io.IOException;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.alibaba.fastjson.JSONObject;
import com.xfrj.core.security.SecurityUtil;

public class TokenUtil {

	public static String encryptToken(String username, String password) {
		String data = username + ShiroContant.TOKEN_SPLIT + password;// + ShiroContant.TOKEN_SPLIT +
																		// System.currentTimeMillis();
		return SecurityUtil.encryptDES(data);
	}

	public static UsernamePasswordToken decryptToken(String token) {
		String data = SecurityUtil.decryptDES(token);
		String[] array = data.split(ShiroContant.TOKEN_SPLIT);

		UsernamePasswordToken userToken = new UsernamePasswordToken();
		userToken.setUsername(array[0]);
		userToken.setPassword(array[1].toCharArray());
		return userToken;
	}

	public static void verifyToken(String token) {
		UsernamePasswordToken up = null;
		try {
			up = decryptToken(token);
		} catch (Exception e) {
			throw new AccountException("token已失效，请重新登陆！");
		}

		Subject subject = SecurityUtils.getSubject();
		UserSession user = (UserSession) subject.getPrincipal();
		if (user == null) {
			throw new AccountException("请先登陆！");
		}
		if (!up.getUsername().equals(user.getUserName())) {
			throw new AccountException("token不正确，请重新登陆！");
		}
	}

	public static UsernamePasswordToken request2Token(ServletRequest request) {
		try {
			UserBody body = JSONObject.parseObject(request.getInputStream(), UserBody.class, null);
			if (body == null) {
				throw new ShiroException("非登陆接口");
			}
			return body2Token(body);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static UsernamePasswordToken body2Token(UserBody user) {
		UsernamePasswordToken token = new UsernamePasswordToken();
		token.setUsername(user.getUsername());
		token.setPassword(user.getPassword().toCharArray());
		return token;
	}

	public static void main(String[] args) {
		System.out.println(TokenUtil.encryptToken("222", "222"));
	}

}
