package com.tenfine.napoleon.framework.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class SpringBeanUtil {
	private static ApplicationContext applicationContext;

	public static void setApplicationContext(
		ApplicationContext applicationContext) {
		SpringBeanUtil.applicationContext = applicationContext;

	}

	public static Object getBean(String name) throws BeansException {
		return applicationContext.getBean(name);
	}

}

