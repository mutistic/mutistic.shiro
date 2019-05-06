package com.mutistic.shiro.core.generator;

import java.sql.Types;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

public class MyJavaTypeResolver extends JavaTypeResolverDefaultImpl {
	public MyJavaTypeResolver() {
		super();
		super.typeMap.put(Types.BIT,
				new JdbcTypeInformation("BIT", new FullyQualifiedJavaType(Integer.class.getName())));
	}
}
