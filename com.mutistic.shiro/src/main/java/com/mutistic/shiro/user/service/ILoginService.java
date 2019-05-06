package com.mutistic.shiro.user.service;

import com.mutistic.shiro.user.model.UserEntity;

public interface ILoginService {

	String getRole(String username);

	UserEntity queryUser(String username);

	UserEntity register(UserEntity entity);

	UserEntity queryUser(String username, Integer authorType);

}
