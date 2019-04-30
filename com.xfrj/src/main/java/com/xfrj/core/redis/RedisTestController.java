package com.xfrj.core.redis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xfrj.core.controller.BaseController;
import com.xfrj.user.model.UserEntity;

@RestController
@RequestMapping("/test/redis")
public class RedisTestController extends BaseController {

	@PostMapping("/applogin")
	public Object login(@RequestBody UserEntity entity) {
		RedisUtil.opsForHashExp.put("user", entity.getUsername(), entity, 100l);
		return success(null, "put success");
	}
	
	@GetMapping("/app/getMessage")
	public Object getMessage() {
//		return success(RedisUtil.opsForHash.entries("user"));
		return success(RedisUtil.opsForHashExp.expKeys("user"));
	}
	
}
