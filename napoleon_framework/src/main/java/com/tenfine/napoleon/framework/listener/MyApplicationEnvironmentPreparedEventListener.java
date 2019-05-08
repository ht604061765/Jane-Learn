package com.tenfine.napoleon.framework.listener;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * 配置环境事件监听
 * spring boot对应environment已经准备完毕，但此时上下文context还没有创建
 */
public class MyApplicationEnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        String wordLine = "=======================================================";
        System.out.println(wordLine + " 环境配置完毕 " + wordLine);
    }
}

