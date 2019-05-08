package com.tenfine.napoleon.framework.listener;

import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;

/**
 * spring boot启动异常时执行事件
 */
public class MyApplicationFailedEventListener implements ApplicationListener<ApplicationFailedEvent> {
    public void onApplicationEvent(ApplicationFailedEvent event) {
        String wordLine = "=======================================================";
        System.out.println(wordLine + " 服务启动异常 " + wordLine);

        Throwable throwable = event.getException();
        handleThrowable(throwable);
    }

    /**
     * 异常处理
     */
    private void handleThrowable(Throwable throwable) {
        throwable.printStackTrace();
    }
}

