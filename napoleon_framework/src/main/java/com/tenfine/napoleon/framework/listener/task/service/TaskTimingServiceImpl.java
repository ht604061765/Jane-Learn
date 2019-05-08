//package com.tenfine.napoleon.framework.listener.task.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.tenfine.napoleon.framework.bean.BaseService;
//import com.tenfine.napoleon.framework.listener.task.po.TaskTimingDao;
//import com.tenfine.napoleon.framework.listener.task.po.TimeTask;
//import com.tenfine.napoleon.framework.util.DateUtil;
//import com.tenfine.napoleon.framework.util.IDUtil;
//
//@Service("TaskTimingService")
//public class TaskTimingServiceImpl extends BaseService implements TaskTimingService{
//	
//	private final TaskTimingDao dao;
//	
//	@Autowired
//	public TaskTimingServiceImpl(TaskTimingDao dao) {
//		this.dao = dao;
//	}
//	
//	public List<TimeTask> getTaskList() {
//		List<TimeTask> taskList = dao.findPoList(TimeTask.class);
//		return taskList;
//	}
//
//	public void insertTaskTimingTest() {
//		String id = IDUtil.getUUID();
//		String code = "refreshImgJob";
//		String name = "刷新背景任务";
//		String rule = "0 0/1 8-23 * * ?";
//		String imp = "com.hunter.springboot.framework.listener.task.job.refreshImgJob";
//		String isOpen = "1";
//		
//		TimeTask taskTiming = new TimeTask();
//		taskTiming.setId(id);
//		taskTiming.setCode(code);
//		taskTiming.setName(name);
//		taskTiming.setRule(rule);
//		taskTiming.setImp(imp);
//		taskTiming.setIsopen(isOpen);
//		taskTiming.setCreateTime(DateUtil.getCurrentDatetime());
//		dao.addPo(taskTiming);
//		
//	}
//
//}
