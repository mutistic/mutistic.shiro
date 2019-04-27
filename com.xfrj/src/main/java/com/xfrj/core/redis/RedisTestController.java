package com.xfrj.core.redis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xfrj.core.controller.BaseController;
import com.xfrj.user.model.UserEntity;

@RestController
@RequestMapping("/redisTest")
public class RedisTestController extends BaseController {

	@PostMapping("/applogin")
	public Object login(@RequestBody UserEntity entity) {
		return success(RedisUtil.set(entity.getUsername(), entity, 10l, false), "APP登录成功");
	}
	
	@GetMapping("/app/getMessage")
	public Object getMessage() {
		UserEntity suer = RedisUtil.get("userentity", UserEntity.class);
		return success(suer);
	}
}
