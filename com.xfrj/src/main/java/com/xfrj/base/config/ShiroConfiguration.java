package com.xfrj.base.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.ShiroException;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 
// https://www.cnblogs.com/HowieYuan/p/9259638.html
@Configuration
public class ShiroConfiguration {

	// 将自己的验证方式加入容器
	@Bean
	public UserRealm shiroRealm() {
		return new UserRealm();
	}

	// 权限管理，配置主要是Realm的管理认证
	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(shiroRealm());
		return securityManager;
	}

	// Filter工厂，设置对应的过滤条件和跳转条件
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// setLoginUrl 如果不设置值，默认会自动寻找Web工程根目录下的"/login.jsp"页面 或 "/login" 映射
		shiroFilterFactoryBean.setLoginUrl("/notLogin");
		// 设置无权限时跳转的 url;
		shiroFilterFactoryBean.setUnauthorizedUrl("/notRole");
		// 设置接口拦截规则
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap());
		System.out.println("Shiro拦截器工厂类注入成功");
		return shiroFilterFactoryBean;
	}

	/**
	 * @description 配置访问拦截器 
	 * @author mutisitic
	 * @return
	 */
	private Map<String, String> filterMap() {
		// 设置拦截器
		Map<String, String> filterRuleMap = new LinkedHashMap<>();
		 // 访问 /unauthorized/** 不通过JWTFilter
	    filterRuleMap.put("/unauthorized/**", "anon");
		// 用户，需要角色权限 “user”
		filterRuleMap.put("/user/**", "roles[user]");
		// 管理员，需要角色权限 “admin”
		filterRuleMap.put("/sys/**", "roles[sys]");
		// 游客，开发权限
		filterRuleMap.put("/guest/**", "anon");
		// 开放登陆接口
		filterRuleMap.put("/app.login", "anon");
		
		// 其余接口一律拦截
		// 主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
		filterRuleMap.put("/**", "authc");
		return filterRuleMap;
	}

	// 加入注解的使用，不加入这个注解不生效
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}
}