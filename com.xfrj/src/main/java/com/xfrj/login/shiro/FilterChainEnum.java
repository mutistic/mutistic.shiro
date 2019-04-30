package com.xfrj.login.shiro;

/**
 * 权限枚举
 */
public enum FilterChainEnum {
	/** anon：开放权限 */
	ANON("anon"),
	/** authc：登陆权限 */
	AUTHC("authc"),
	/** logout：注销 */
	LOGOUT("logout"),
	/** authcBasic：需要httpBasic认证 */
	AUTHC_BASIC("authcBasic"),
	/** user：必须存在用户 */
	USER("user"),
	/** ssl：安全的URL请求，协议为 https */
	SSL("ssl"),
	/** port：请求URL端口必须是8080时*/
	PORT_8080("port[8080]"),
	;
	
	private String value;
	public String getValue() {
		return this.value;
	}
	
	FilterChainEnum(String value){
		this.value = value;
	}
}
