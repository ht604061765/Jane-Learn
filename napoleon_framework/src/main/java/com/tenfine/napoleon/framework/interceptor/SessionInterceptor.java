package com.tenfine.napoleon.framework.interceptor;

import com.tenfine.napoleon.framework.SessionConstant;
import com.tenfine.napoleon.framework.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionInterceptor implements HandlerInterceptor{
    private static final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

	
    // 在请求处理之前进行调用（Controller方法调用之前）
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String currentUri = request.getRequestURI();
        
    	logger.info("【Url Match】：" + currentUri);
    	
        // 登录不做拦截（登录页面和登录请求和注册请求）
        String[] loginUris = {"/login", "/loginSubmit", "/regSubmit", "/test", "/error"};
        for (int i = 0; i < loginUris.length; i++) {
        	if(currentUri.equals(loginUris[i])){
        		return true;
        	}
		}
        if(currentUri.contains("/api")){
    		return true;
    	}
        // 验证session是否存在
        Object obj = SessionUtil.getInstance().getAttribute(SessionConstant.USER_NAME, request.getSession());
        if (obj == null) {
             response.sendRedirect("/login");
             // 解决在框架中跳转登录页的问题
//            response.setContentType("text/html; charset=UTF-8");
//            PrintWriter out = response.getWriter();
//            out.println("<html>");
//            out.println("<script>");
//            // out.println("alert('操作超时，请重新登录！');");
//            out.println("window.open('/login');");
//            out.println("</script>");
//            out.println("</html>");
            return false;
        }
        return true;
    }

    // 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    // 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
