package com.mutistic.shiro.login.dto;

import com.mutistic.shiro.user.model.UserEntity;

public class UserParams extends UserEntity {
	private String regiestMobile;
	private String regiestCode;
	private Boolean isRememberme;
	
	public Boolean getIsRememberme() {
		return isRememberme;
	}
	public void setIsRememberme(Boolean isRememberme) {
		this.isRememberme = isRememberme;
	}
	public String getRegiestMobile() {
		return regiestMobile;
	}
	public void setRegiestMobile(String regiestMobile) {
		this.regiestMobile = regiestMobile;
	}
	public String getRegiestCode() {
		return regiestCode;
	}
	public void setRegiestCode(String regiestCode) {
		this.regiestCode = regiestCode;
	}
	
}
