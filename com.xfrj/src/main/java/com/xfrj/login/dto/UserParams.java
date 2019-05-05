package com.xfrj.login.dto;

import com.xfrj.user.model.UserEntity;

public class UserParams extends UserEntity {
	private String regiestMobile;
	private String regiestCode;
	
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
