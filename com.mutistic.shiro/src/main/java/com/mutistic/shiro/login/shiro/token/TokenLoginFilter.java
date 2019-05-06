package com.mutistic.shiro.login.shiro.token;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import com.mutistic.shiro.login.shiro.UserDto;


/**
 * Token 过滤器
 */
public class TokenLoginFilter extends BasicHttpAuthenticationFilter {
	// http://shiro.apache.org/static/1.3.2/apidocs/org/apache/shiro/web/filter/authc/package-summary.html
	// https://www.cnblogs.com/q95265/p/6928081.html

	// 身份拦截器
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String token = httpServletRequest.getHeader(TokenUtil.TOKEN_HEAD);
		TokenUtil.notNull(token, "没有访问权限，请先登录！");
		TokenUtil.validateToken(token);
		
		UserDto user = TokenUtil.getTokenInfo(token);
		TokenUtil.notNull(user, "token已过期，请重新登陆！");
		if(user.getExpirationTime().compareTo(System.currentTimeMillis()) == -1) {
			TokenUtil.deleteTokenInfo(token);
			TokenUtil.exception("token已过期，请重新登陆！");
		}
		
		return true;
	}
}
