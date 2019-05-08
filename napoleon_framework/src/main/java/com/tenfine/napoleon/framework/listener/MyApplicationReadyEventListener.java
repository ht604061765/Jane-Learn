package com.tenfine.napoleon.framework.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import com.tenfine.napoleon.framework.util.PlatfromProperties;

/**
 * 启动完成事件
 * 环境environment准备、上下文context创建和bean加载都完成了
 */
public class MyApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
	private static final Logger logger = LoggerFactory.getLogger(MyApplicationReadyEventListener.class);
	
    public void onApplicationEvent(ApplicationReadyEvent event) {
    	String wordLine = "=======================================================";
        System.out.println(wordLine + " 服务启动完成 " + wordLine);
    }
}

