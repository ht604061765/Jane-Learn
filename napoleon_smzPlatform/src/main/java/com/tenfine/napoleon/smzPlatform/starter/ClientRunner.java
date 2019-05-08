//package com.tenfine.napoleon.smzPlatform.starter;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import com.tenfine.napoleon.framework.AsyncTaskService;
//import com.tenfine.napoleon.smzPlatform.client.BaseClient;
//
//@Component
//public class ClientRunner implements ApplicationRunner {
//	private static final Logger logger = LoggerFactory.getLogger(ClientRunner.class);
//
//	@Autowired
//	private AsyncTaskService asyncTaskService;
//
//	@Override
//	public void run(ApplicationArguments args) throws Exception {
//		try {
//			asyncTaskService.executeAsyncTask(new BaseClient());
//		} catch (Exception e) {
//			logger.error("主线程启动失败");
//			e.printStackTrace();
//		}
//
//	}
//
//}
//
//
