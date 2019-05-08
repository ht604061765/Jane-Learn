//package com.tenfine.napoleon.framework.listener.task;
//
//import java.util.List;
//
//import org.quartz.CronScheduleBuilder;
//import org.quartz.CronTrigger;
//import org.quartz.JobBuilder;
//import org.quartz.JobDetail;
//import org.quartz.Scheduler;
//import org.quartz.SchedulerException;
//import org.quartz.SchedulerFactory;
//import org.quartz.TriggerBuilder;
//import org.quartz.impl.StdSchedulerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.tenfine.napoleon.framework.listener.task.po.TimeTask;
//
//public class TaskManager {
//
//	private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);
//
//	static SchedulerFactory sf = new StdSchedulerFactory();
//	static Scheduler sched;
//	private static String TaskGroup = "HUNTER";
//	
//	static {
//		try {
//			sched = sf.getScheduler();
//		} catch (SchedulerException e) {
//			logger.error("======[定时任务]创建Scheduler实例异常:" + e.getMessage());
//		}
//	}
//	
//	public synchronized static void shutdown() throws Exception {
//		sched.shutdown();
//	}
//	
//	public synchronized static void startJob(List<TimeTask> taskTimings) throws Exception {
//		sched.clear();
//		if (taskTimings.size() > 0) {
//			for (int i = 0; i < taskTimings.size(); i++) {
//				TimeTask task = (TimeTask) taskTimings.get(i);
//				String isopen = task.getIsopen();
//				if (isopen.equals("1")) {
//					Class jobcls = Class.forName(task.getImp()); 	//找到任务class
//				JobDetail job = JobBuilder							//把任务class转换成job
//							.newJob(jobcls)
//							.withIdentity(task.getCode(), TaskGroup)
//							.build(); 								
//				
//				CronTrigger trigger = TriggerBuilder				//定义触发规则
//						.newTrigger()
//						.withIdentity(task.getCode() + "_Trigger",TaskGroup)
//						.withSchedule(CronScheduleBuilder.cronSchedule(task.getRule()))
//						.build();
//				
//				sched.scheduleJob(job, trigger); 					//把规则和任务转换成一个调度
//				}
//			}
//			sched.start();
//		}else {
//			logger.info("+++++++++定时任务列表为空，没有要执行的定时任务+++++++++++");
//		}
//		
//	}
//	
//}
