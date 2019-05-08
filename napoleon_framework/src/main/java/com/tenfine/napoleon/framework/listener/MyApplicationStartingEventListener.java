package com.tenfine.napoleon.framework.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

import com.tenfine.napoleon.framework.cache.SqlCache;

/**
 * spring boot启动开始时执行的事件
 */
public class MyApplicationStartingEventListener implements ApplicationListener<ApplicationStartingEvent> {
	private static final Logger logger = LoggerFactory.getLogger(MyApplicationStartingEventListener.class);
	
    public void onApplicationEvent(ApplicationStartingEvent event) {
        String wordLine = "=======================================================";
        System.out.println(wordLine + " 服务开始启动 " + wordLine);
        logger.info(wordLine + " logger日志打印：服务开始启动 " + wordLine);
        
        System.out.println("扫描 SQL XML 文件中 ...");
        SqlCache.init();
        System.out.println("扫描 SQL XML 文件完成");
    }
}

