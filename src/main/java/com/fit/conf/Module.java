package com.fit.conf;

import org.mybatis.guice.XMLMyBatisModule;

import com.google.inject.Singleton;

/**
 * @AUTO 用与注册module的，就是google guice用的
 * @FILE Module.java
 * @DATE 2017-9-28 下午8:35:38
 * @Author AIM
 */
@Singleton
public class Module extends XMLMyBatisModule {

	@Override
	protected void initialize() {
		setEnvironmentId("aim");
		setClassPathResource("conf/mybatis-config.xml");
	}
}
