package com.tenfine.napoleon.framework.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * 上下文创建完成后执行的事件监听器
 * 此时bean还没有加载完成
 */
public class MyApplicationPreparedEventListener implements ApplicationListener<ApplicationPreparedEvent> {
	private static final Logger logger = LoggerFactory.getLogger(MyApplicationPreparedEventListener.class);

    public void onApplicationEvent(ApplicationPreparedEvent event) {
        String wordLine = "=======================================================";
        System.out.println(wordLine + " 上下文创建完成 " + wordLine);
    }
}

