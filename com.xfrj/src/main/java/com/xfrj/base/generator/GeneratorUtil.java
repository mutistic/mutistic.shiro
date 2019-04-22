package com.xfrj.base.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

public class GeneratorUtil {

	private final static String XML_FILE_URL = Thread.class.getResource("/")
			+ "com/xfrj/base/generator/GeneratorConfig.xml";

	public static void main(String[] args) throws Exception {
		try {
			List<String> warnings = new ArrayList<String>();
			File configFile =  new File(XML_FILE_URL.replace("file:/", ""));
			ConfigurationParser cp = new ConfigurationParser(warnings);
			Configuration config = cp.parseConfiguration(configFile);
			DefaultShellCallback callback = new DefaultShellCallback(true);
			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
			myBatisGenerator.generate(null);
		} catch (Exception e) {
			System.err.println("文件生成失败！");
			e.printStackTrace();
		}
		System.out.println("文件生成成功！");
	}

}
