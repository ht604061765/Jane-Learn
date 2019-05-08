package com.tenfine.napoleon.framework.listener;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
public class SessionListener implements HttpSessionListener {
    // 创建session
    public void sessionCreated(HttpSessionEvent se) {

    }

    // 销毁session
    public void sessionDestroyed(HttpSessionEvent se) {

    }
}

