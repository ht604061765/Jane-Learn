package com.tenfine.napoleon.framework.util;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionUtil {
    private static final Logger logger = LoggerFactory.getLogger(SessionUtil.class);

    // 创建实例对象
    private static SessionUtil instance = new SessionUtil();

    public static SessionUtil getInstance() {
        if (instance == null)
            instance = new SessionUtil();
        return instance;
    }

    // 使session失效
    public void invalidate(HttpSession session) {
        session.invalidate();
    }

    /**
     * 在session中放入属性值
     *
     * @param key     引用SessionConstant中定义的常量，例如userId，userName，orgId，orgName
     * @param value   值
     * @param session session对象
     */
    public void setAttribute(String key, Object value, HttpSession session) {
        session.setAttribute(key, value);
    }

    /**
     * 从session中获取属性值
     *
     * @param key     引用SessionConstant中定义的常量，例如userId，userName，orgId，orgName
     * @param session 值
     * @return session对象
     */
    public Object getAttribute(String key, HttpSession session) {
        Object obj = session.getAttribute(key);
        if (obj != null)
            return obj;
        else
            return null;
    }
}
