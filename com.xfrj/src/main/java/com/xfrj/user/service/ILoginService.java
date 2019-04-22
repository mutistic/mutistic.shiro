package com.xfrj.user.service;

import com.xfrj.user.model.UserEntity;

public interface ILoginService {

	String getRole(String username);

	UserEntity queryUser(String username);

}
