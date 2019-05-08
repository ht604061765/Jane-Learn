///**
// * @author Hunter
// * TopSpaceMaven
// * 2018-1-2 下午3:38:54
// * TODO //
// */
//package com.tenfine.napoleon.framework.listener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.servlet.annotation.WebListener;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.context.support.WebApplicationContextUtils;
//
//import com.tenfine.napoleon.framework.listener.task.TaskManager;
//import com.tenfine.napoleon.framework.listener.task.po.TimeTask;
//import com.tenfine.napoleon.framework.listener.task.service.TaskTimingService;
//import com.tenfine.napoleon.framework.util.FixedPlatfrom;
//
//@WebListener
//public class TaskListener implements ServletContextListener{
//	private static final Logger logger = LoggerFactory.getLogger(TaskListener.class);
//
//	public void contextInitialized(ServletContextEvent sce) {
//		
//		TaskTimingService tts = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()).getBean(TaskTimingService.class);
//		List<TimeTask> taskTimings = new ArrayList();
//		// 定时任务用不到，先不要查询
////		List<TimeTask> a = tts.getTaskList();
//		//防止NPE
////		if(a != null) {
////			taskTimings = a;
////		}
//
//		try {
//			TaskManager.startJob(taskTimings);
//			logger.info("[定时任务]定时任务启动成功！");
//		} catch (Exception e) {
//			logger.error("[定时任务]定时任务启动失败！");
//			e.printStackTrace();
//		}
//	}
//	
//	public void contextDestroyed(ServletContextEvent sce) {
//		try {
//			TaskManager.shutdown();
//		} catch (Exception e) {
//			logger.error("[定时任务]定时任务销毁失败！",e);
//		}
//		logger.info("[定时任务]定时任务销毁成功！");
//		
//	}
//
//}
//
//
