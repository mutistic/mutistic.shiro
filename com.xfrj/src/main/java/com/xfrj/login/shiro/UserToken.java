package com.xfrj.login.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

@SuppressWarnings("serial")
public class UserToken extends UsernamePasswordToken {

	private Integer authorType;

	public Integer getAuthorType() {
		return authorType;
	}

	public void setAuthorType(Integer authorType) {
		this.authorType = authorType;
	}
	
	public UserToken(final String username, final String password, Integer authorType) {
		super(username, password, false, null);
		this.authorType = authorType;
    }
	
}
