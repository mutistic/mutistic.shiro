package com.xfrj.core.contants;

import java.util.HashSet;
import java.util.Set;

/**
 * 权限常量
 */
@SuppressWarnings("serial")
public class AuthorizationContant {
	/** 普通用户 */
	public final static String AUTHOR_APP = "app";
	/** 管理员 */
	public final static String AUTHOR_SYS = "sys";
	/** 普通用户权限集合 */
	public static Set<String> AUTHOR_APP_SET = new HashSet<String>() {
		{
			add(AUTHOR_APP);
		}
	};
	/** 管理员权限集合 */
	public static Set<String> AUTHOR_SYS_SET = new HashSet<String>() {
		{
			add(AUTHOR_APP);
			add(AUTHOR_SYS);
		}
	};
	
}
