package com.xfrj.login.shiro;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserDto implements Serializable {
	private Long id;
	private String username;
	private String password;
	private Integer authorType;
	private String token;
	private Long lastLoginTime;
	private Long expirationTime;
	public Integer getAuthorType() {
		return authorType;
	}
	public void setAuthorType(Integer authorType) {
		this.authorType = authorType;
	}
	public Long getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public Long getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(Long expirationTime) {
		this.expirationTime = expirationTime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}
