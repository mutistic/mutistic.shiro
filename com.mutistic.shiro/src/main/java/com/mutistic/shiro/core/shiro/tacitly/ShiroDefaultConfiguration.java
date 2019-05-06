package com.mutistic.shiro.core.shiro.tacitly;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;

// 
// https://www.cnblogs.com/HowieYuan/p/9259638.html
// http://shiro.apache.org/static/1.3.2/apidocs/overview-summary.html
// http://shiro.apache.org/articles.html
/**
 * Shiro 默认配置类
 */
//@Configuration
public class ShiroDefaultConfiguration {
	
	// 将自己的验证方式加入容器
	@Bean
	public UserDefaultRealm defaultShiroRealm() {
		return new UserDefaultRealm();
	}

	// 权限管理，配置主要是Realm的管理认证
	@Bean
	public SecurityManager defaultSecurityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(defaultShiroRealm());
		return securityManager;
	}

	// Filter工厂，设置对应的过滤条件和跳转条件
	@Bean
	public ShiroFilterFactoryBean defaultShiroFilterFactoryBean() {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(defaultSecurityManager());
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
		Map<String, String> filterRuleMap = new LinkedHashMap<String, String>();
		// 无权限接口
		// 访问 /unauthorized/**
		filterRuleMap.put("/unauthorized/**", "anon");
		// 游客权限
		filterRuleMap.put("/guest/**", "anon");
		
		// 移动用户权限
		filterRuleMap.put("/app/**", "roles[app]");
		// 管理员权限
		filterRuleMap.put("/sys/**", "roles[sys]");

		// 特殊开放接口
		// app登录
		filterRuleMap.put("/applogin", "anon"); 

		// 其余接口一律拦截
		// 主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
		filterRuleMap.put("/**", "authc");
		return filterRuleMap;
	}

	// 加入注解的使用，不加入这个注解不生效
	@Bean
	public AuthorizationAttributeSourceAdvisor defaultAuthorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(defaultSecurityManager());
		return authorizationAttributeSourceAdvisor;
	}
}