package com.xfrj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan(basePackages = {"com.xfrj.*.mapper"})
public class XFRJApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(XFRJApplication.class, args);
//		System.out.println(ctx.getBean("serializableRedisTemplate"));
	}

}
