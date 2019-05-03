package com.xfrj.login.shiro;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

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
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.xfrj.core.shiro.redis.ShiroRedisCacheManager;
import com.xfrj.login.shiro.token.TokenLoginFilter;
import com.xfrj.login.shiro.token.UserLoginRealm;

/**
 * Shiro 配置类 </br>
 * 1、启用shrio cache，cache使用redis来管理session </br>
 * 2、使用UserLoginRealm控制登录 </br>
 * 3、移动端采用token、配合TokenLoginFilter控制访问权限 </br>
 * 4、WEB端登录采用session/cookie，配合SessionLoginFilter控制访问权限
 */
@Configuration
public class ShiroLoginConfiguration {
	//https://www.cnblogs.com/sunshine-2015/p/5515429.html
	
	@Autowired
	private RedisConnectionFactory redisConnectionFactory;
	
	/**
	 * shrio登录验证规则器
	 * 
	 * @return UserLoginRealm bean
	 */
	@Bean
	public UserLoginRealm loginShiroRealm() {
		return new UserLoginRealm();
	}

	/**
	 * shiro拦截过滤器工厂，配置拦截规则
	 * 
	 * @return ShiroFilterFactoryBean bean
	 */
	@Bean
	public ShiroFilterFactoryBean loginShirofactory() {
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		factoryBean.setSecurityManager(loginSecurityManager());
//		factoryBean.setLoginUrl("/app/login"); // 登陆接口
		factoryBean.setUnauthorizedUrl("/unauthorized"); // 无权限指定接口
		
		// 访问路径权限过滤器Map
		Map<String, Filter> filterMap = new LinkedHashMap<String, Filter>(); 
		filterMap.put("sysFilter", new TokenLoginFilter()); // web访问过滤器 TODO
		filterMap.put("tokenFilter", new TokenLoginFilter()); // app访问过滤器
		factoryBean.setFilters(filterMap);
		
		// 访问路径权限Map（注意是有序Map） 参考 DefaultFilterChainManager
		Map<String, String> ruleMap = new LinkedHashMap<String, String>(); 
		// 无需权限路径
		ruleMap.put("/unauthorized/**", FilterChainEnum.ANON.getValue()); // 无权限
		// 静态资源
		ruleMap.put("/static/**", FilterChainEnum.ANON.getValue());
		// web端
		ruleMap.put("/syslogin", FilterChainEnum.ANON.getValue()); // web登陆
		ruleMap.put("/sys/**", "sysFilter"); // web其他访问路径需要经过session
		// 移动端
		ruleMap.put("/app/login", FilterChainEnum.ANON.getValue()); // app登陆
		ruleMap.put("/app/register", FilterChainEnum.ANON.getValue()); // app注册
		ruleMap.put("/app/**", "tokenFilter"); // app其他路径访问需要经过token
		
		ruleMap.put("/**", FilterChainEnum.AUTHC.getValue()); // 其他接口一律需要登录
		factoryBean.setFilterChainDefinitionMap(ruleMap);
		return factoryBean;
	}

	/**
	 * shiro权限管理器，注入Realm验证规则
	 * 
	 * @return SecurityManager bean
	 */
	@Bean
	public SecurityManager loginSecurityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRememberMeManager(loginCookieRememberMeManager());
		// 配置 缓存管理类 cacheManager，需要在setRealm 和 setSessionManage操作前面执行，
		securityManager.setCacheManager(loginSessionRedisCacheManager());
		securityManager.setRealm(loginShiroRealm());
		securityManager.setSessionManager(loginWebSessionManager());
		return securityManager;
	}

	/**
	 * RememberMe cookie管理器
	 * 
	 * @return
	 */
	@Bean
	public CookieRememberMeManager loginCookieRememberMeManager() {
		// rememberMe cookie 即 浏览器中的"记住我"
		CookieRememberMeManager rememberManager = new CookieRememberMeManager();

		// 这里的Cookie的默认名称是 CookieRememberMeManager.DEFAULT_REMEMBER_ME_COOKIE_NAME
		SimpleCookie rememberCookie = new SimpleCookie(CookieRememberMeManager.DEFAULT_REMEMBER_ME_COOKIE_NAME);
		// 是否只在taps情况下传输
		rememberCookie.setSecure(false);
		// 设置 cookie 的过期时间，单位为秒，这里为一天
		rememberCookie.setMaxAge(2592000);

		rememberManager.setCookie(rememberCookie);
		// rememberMe cookie 加密的密钥 TODO
		// cookieRememberMeManager.setCipherKey(Base64.decode("ZWvohmPdUsAWT3=KpPqda"));
		return rememberManager;
	}

	/**
	 * session redis缓存操作对象
	 * 
	 * @return
	 */
	@Bean
	public ShiroRedisCacheManager loginSessionRedisCacheManager() {
		RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<String, Serializable>();
		redisTemplate.setEnableTransactionSupport(false);
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		/**
		 * shrio cache将 session存储在redis的 String数据类型中：
		 * 1.1、[key:sessionId][value: org.apache.shiro.session.mgt.SimpleSession]
		 * 1.2、value的序列化方式 valueSerializer
		 * 1.2.1、如果使用 GenericJackson2JsonRedisSerializer/GenericFastJsonRedisSerializer 序列化模式时：
		 * 在反序列化时，由于SimpleSession.attributes数据类型Map<Object,Object> 会导致反序列化失败， 故采用默认序列化：JdkSerializationRedisSerializer
		 */
		redisTemplate.setKeySerializer(new StringRedisSerializer()); // String序列化
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer()); // 默认jdk序列化
		redisTemplate.setHashKeySerializer(redisTemplate.getKeySerializer());
		redisTemplate.setHashValueSerializer(redisTemplate.getValueSerializer());
		redisTemplate.afterPropertiesSet();
		return new ShiroRedisCacheManager(redisTemplate);
	}

	/**
	 * session管理器
	 * 
	 * @return
	 */
	@Bean
	public DefaultWebSessionManager loginWebSessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		// 设置session超时时间，单位为毫秒
		sessionManager.setGlobalSessionTimeout(1800000);
		// 设置cookie
		sessionManager.setSessionIdCookie(new SimpleCookie("xfrj.session.id"));
		// session操作，默认EnterpriseCacheSessionDAO，可自己实现
		sessionManager.setSessionDAO(new EnterpriseCacheSessionDAO());
		return sessionManager;
	}

	/**
	 * 配置启用shiro权限控制注解bean，启用shiro相关注解（如：@RequiresRoles）
	 * 
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor loginAuthorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(loginSecurityManager()); // 注入SecurityManager
		return advisor;
	}
}