package com.tenfine.napoleon;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.tenfine.napoleon.framework.AsyncTaskService;
import com.tenfine.napoleon.framework.listener.MyApplicationEnvironmentPreparedEventListener;
import com.tenfine.napoleon.framework.listener.MyApplicationFailedEventListener;
import com.tenfine.napoleon.framework.listener.MyApplicationPreparedEventListener;
import com.tenfine.napoleon.framework.listener.MyApplicationReadyEventListener;
import com.tenfine.napoleon.framework.listener.MyApplicationStartingEventListener;

@EnableAsync //线程池注解
@EnableTransactionManagement //事务注解
@SpringBootApplication 
@EnableCaching // 启动缓存
@ServletComponentScan
@ComponentScan({"com.tenfine.napoleon.*"})
public class Application extends SpringBootServletInitializer {
	
	@Autowired
	private static AsyncTaskService asyncTaskService;
	
    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.addListeners(
                new MyApplicationStartingEventListener(), new MyApplicationPreparedEventListener(),
                new MyApplicationEnvironmentPreparedEventListener(), new MyApplicationFailedEventListener(),
                new MyApplicationReadyEventListener());
    	springApplication.run(args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    	builder.listeners(
                new MyApplicationStartingEventListener(), new MyApplicationPreparedEventListener(),
                new MyApplicationEnvironmentPreparedEventListener(), new MyApplicationFailedEventListener(),
                new MyApplicationReadyEventListener());
    	return builder.sources(Application.class);
    }
    
    /**
     * 文件上传配置
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize("1024000KB"); //KB,MB
        //设置总上传数据总大小
        factory.setMaxRequestSize("1024000KB");
        return factory.createMultipartConfig();
    }
}