package com.mutistic.shiro.core.shiro.token;

import java.io.IOException;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.alibaba.fastjson.JSONObject;
import com.mutistic.shiro.core.security.SecurityUtil;
import com.mutistic.shiro.core.shiro.UserSession;

public class TokenUtil {

	public final static String TOKEN = "token";
	public final static String TOKEN_SPLIT = "###";
	
	/**
	 * 过期时间
	 * 	默认：7*24*60*60*1000 = 604800000
	 */
	private final static Long EXPIRATION_TIME = 604800000l;
	
	public static String encryptToken(String data, Long currentTimeMillis) {
		String token = data + TOKEN_SPLIT + (currentTimeMillis + EXPIRATION_TIME);
		return SecurityUtil.encryptDES(token);
	}

	public static UserSession decryptToken(String token) {
		String data = SecurityUtil.decryptDES(token);
		String[] array = data.split(TOKEN_SPLIT);
		UserSession userToken = new UserSession();
		userToken.setId(Long.valueOf(array[0]));
		userToken.setExpirationTime(Long.valueOf(array[1]));
		return userToken;
	}

	public static void verifyToken(String token) {
		UserSession session = null;
		try {
			session = decryptToken(token);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccountException("token无效，请重新登陆！", e);
		}

		Subject subject = SecurityUtils.getSubject();
		UserSession user = (UserSession) subject.getPrincipal();
		if (user == null) {
			throw new AccountException("您还未登陆，请先登陆！");
		}
		if (!session.getId().equals(user.getId())) {
			throw new AccountException("token错误，请重新登陆！");
		}
		if (session.getExpirationTime().compareTo(user.getLastLoginTime()) < 0) {
			throw new AccountException("token已过期，请重新登陆！");
		}
	}

	public static UsernamePasswordToken request2Token(ServletRequest request) {
		try {
			UserSession body = JSONObject.parseObject(request.getInputStream(), UserSession.class, null);
			if (body == null) {
				throw new ShiroException("非登陆接口");
			}
			return body2Token(body);
		} catch (IOException e) {
			throw new AccountException("token解析失败", e);
		}
	}

	public static UsernamePasswordToken body2Token(UserSession user) {
		UsernamePasswordToken token = new UsernamePasswordToken();
		token.setUsername(user.getUsername());
		token.setPassword(user.getPassword().toCharArray());
		return token;
	}

	public static void main(String[] args) {
		System.out.println(TokenUtil.encryptToken("1026448466399707138", System.currentTimeMillis()));
	}

}
