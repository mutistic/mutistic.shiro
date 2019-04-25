package com.xfrj.core.shiro.redis;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import cn.hutool.core.codec.Base64;

/**
 * Shiro redis 配置类
 */
@Configuration
public class ShiroRedisConfiguration {

	// https://blog.csdn.net/u010514380/article/details/82185451

	@Autowired
	private RedisTemplate<String, String> template;

	// 将自己的验证方式加入容器
	@Bean
	public UserRedisRealm redisShiroRealm() {
		return new UserRedisRealm();
	}

	// Filter工厂，设置对应的过滤条件和跳转条件
	@Bean
	public ShiroFilterFactoryBean redisShiroFilterFactoryBean() {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(redisSecurityManager());
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
		 // 设置访问路径
	    Map<String, String> filterRuleMap = new HashMap<>();
	    filterRuleMap.put("/redis/applogin", "anon"); // app登陆
	    filterRuleMap.put("/unauthorized/**", "anon");
	    filterRuleMap.put("/redis/app/**", "roles[app]");
		// 其余接口一律拦截
		// 主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
		filterRuleMap.put("/**", "authc");
		return filterRuleMap;
	}

	// 权限管理，配置主要是Realm的管理认证
	@Bean
	public SecurityManager redisSecurityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 配置 rememberMeCookie 查看源码可以知道，这里的rememberMeManager就仅仅是一个赋值，所以先执行
		securityManager.setRememberMeManager(redisRememberMeManager());
		// 配置 缓存管理类 cacheManager，这个cacheManager必须要在前面执行，因为setRealm 和
		// setSessionManage都有方法初始化了cachemanager,看下源码就知道了
		securityManager.setCacheManager(new ShiroRedisCacheManager(template));
		// 配置 SecurityManager，并注入 shiroRealm 这个跟springmvc集成很像，不多说了
		securityManager.setRealm(redisShiroRealm());
		// 配置 sessionManager
		securityManager.setSessionManager(redisSessionManager());
		return securityManager;
	}

	/**
	 * cookie管理对象
	 *
	 * @return CookieRememberMeManager
	 */
	private CookieRememberMeManager redisRememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(redisRememberMeCookie());
		// rememberMe cookie 加密的密钥
//		cookieRememberMeManager.setCipherKey(Base64.decode("ZWvohmPdUsAWT3=KpPqda")); // todo
		return cookieRememberMeManager;
	}

	/**
	 * rememberMe cookie 效果是重开浏览器后无需重新登录
	 *
	 * @return SimpleCookie
	 */
	private SimpleCookie redisRememberMeCookie() {
		// 这里的Cookie的默认名称是 CookieRememberMeManager.DEFAULT_REMEMBER_ME_COOKIE_NAME
		SimpleCookie cookie = new SimpleCookie(CookieRememberMeManager.DEFAULT_REMEMBER_ME_COOKIE_NAME);
		// 是否只在https情况下传输
		cookie.setSecure(false);
		// 设置 cookie 的过期时间，单位为秒，这里为一天
		cookie.setMaxAge(2592000); // todo
		return cookie;
	}

	/**
	 * session 管理对象
	 *
	 * @return DefaultWebSessionManager
	 */
	private DefaultWebSessionManager redisSessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		// 设置session超时时间，单位为毫秒
		sessionManager.setGlobalSessionTimeout(1800000);
		sessionManager.setSessionIdCookie(new SimpleCookie("xfrj.session.id"));
		// 网上各种说要自定义sessionDAO 其实完全不必要，shiro自己就自定义了一个，可以直接使用，还有其他的DAO，自行查看源码即可
		sessionManager.setSessionDAO(new EnterpriseCacheSessionDAO());
		return sessionManager;
	}

	// 加入注解的使用，不加入这个注解不生效
	@Bean
	public AuthorizationAttributeSourceAdvisor redisAuthorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(redisSecurityManager());
		return authorizationAttributeSourceAdvisor;
	}
}