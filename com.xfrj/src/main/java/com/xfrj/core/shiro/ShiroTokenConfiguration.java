package com.xfrj.core.shiro;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 
// https://www.cnblogs.com/HowieYuan/p/9259638.html
// http://shiro.apache.org/static/1.3.2/apidocs/overview-summary.html
/**
 * Shiro Token 配置类
 */
@Configuration
public class ShiroTokenConfiguration {
	
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

	@Bean
	public ShiroFilterFactoryBean factory(SecurityManager securityManager) {
	    ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
	   
	    // 设置自定义Filter
	    Map<String, Filter> filterMap = new HashMap<>();
	    filterMap.put("token", new TokenFilter()); // token filter
	    factoryBean.setFilters(filterMap);
	    factoryBean.setSecurityManager(securityManager);
	    
	    // 设置访问路径
	    Map<String, String> filterRuleMap = new HashMap<>();
	    filterRuleMap.put("/applogin", "anon"); // app登陆
	    filterRuleMap.put("/unauthorized/**", "anon");
	    filterRuleMap.put("/app/**", "token");
	    factoryBean.setFilterChainDefinitionMap(filterRuleMap);
	    return factoryBean;
	}

	// 加入注解的使用，不加入这个注解不生效
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}
}