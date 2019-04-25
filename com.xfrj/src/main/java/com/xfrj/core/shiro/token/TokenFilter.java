package com.xfrj.core.shiro.token;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;


/**
 * Token 过滤器
 */
public class TokenFilter extends BasicHttpAuthenticationFilter {
	// http://shiro.apache.org/static/1.3.2/apidocs/org/apache/shiro/web/filter/authc/package-summary.html

	// 身份拦截器
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String token = httpServletRequest.getHeader(TokenUtil.TOKEN);
		if (token == null || token.trim().length() == 0) {
			throw new AccountException("请先登陆！");
		}

		TokenUtil.verifyToken(token.trim());
		return true;
	}

	// @Override
	// protected boolean executeLogin(ServletRequest request, ServletResponse
	// response) throws Exception {
	// getSubject(request, response).login(TokenUtil.request2Token(request));
	// return true;
	// }

	// @Override
	// protected boolean executeLogin(ServletRequest request, ServletResponse
	// response) throws Exception {
	// HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	// String token = httpServletRequest.getHeader(ShiroContant.TOKEN);
	// // 提交给realm进行登入，如果错误他会抛出异常并被捕获
	// getSubject(request, response).login(TokenUtil.decryptToken(token));
	// // 如果没有抛出异常则代表登入成功，返回true
	// return true;
	// }
}
