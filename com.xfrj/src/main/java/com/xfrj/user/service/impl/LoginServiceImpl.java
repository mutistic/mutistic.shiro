package com.xfrj.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		criteria.andUserNameEqualTo(username);
		
		List<UserEntity> list = userEntityMapper.selectByExample(example);
		if(list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

}
