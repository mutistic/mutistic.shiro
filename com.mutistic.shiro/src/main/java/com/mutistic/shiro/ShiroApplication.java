package com.mutistic.shiro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan(basePackages = {"com.mutistic.shiro.*.mapper"})
public class ShiroApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(ShiroApplication.class, args);
//		System.out.println(ctx.getBean("serializableRedisTemplate"));
	}

}
