package com.xfrj.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xfrj.core.utils.ValidateUtil;
import com.xfrj.user.mapper.UserEntityMapper;
import com.xfrj.user.model.UserEntity;
import com.xfrj.user.model.UserEntityExample;
import com.xfrj.user.model.UserEntityExample.Criteria;
import com.xfrj.user.service.ILoginService;


@Service
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private UserEntityMapper userEntityMapper;

	@Override
	public String getRole(String username) {
		return null;
	}

	@Override
	public UserEntity queryUser(String username) {
		UserEntityExample example = new UserEntityExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		
		List<UserEntity> list = userEntityMapper.selectByExample(example);
		if(list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public UserEntity queryUser(String username, Integer authorType) {
		UserEntityExample example = new UserEntityExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		criteria.andAuthorTypeEqualTo(authorType);
		
		List<UserEntity> list = userEntityMapper.selectByExample(example);
		if(list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	@Override
	public UserEntity register(UserEntity entity) {
		UserEntity user = queryUser(entity.getUsername());
		if(user != null) {
			ValidateUtil.exception(entity.getUsername() +"用户已存在！不能重复注册！");
		}
		
		entity.setId(System.currentTimeMillis());
		int result = userEntityMapper.insert(entity);
		if(result <= 0) {
			ValidateUtil.exception(entity.getUsername() +"用户注册失败！");
		}
		return entity;
	}

}
