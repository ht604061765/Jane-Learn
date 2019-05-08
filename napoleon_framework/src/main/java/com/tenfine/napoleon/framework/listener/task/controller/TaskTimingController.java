//package com.tenfine.napoleon.framework.listener.task.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.tenfine.napoleon.framework.bean.BaseController;
//import com.tenfine.napoleon.framework.bean.Result;
//import com.tenfine.napoleon.framework.listener.task.service.TaskTimingService;
//
//@Controller
//public class TaskTimingController extends BaseController{
//
//	private final TaskTimingService taskTimingService;
//	
//	@Autowired
//	public TaskTimingController(TaskTimingService taskTimingService) {
//		this.taskTimingService = taskTimingService;
//	}
//	
//	@RequestMapping(value = "addTimingTask")
//	@ResponseBody
//	public Result<String> addTask(){
//		taskTimingService.insertTaskTimingTest();
//		return Result.success("成功添加了一条定时任务！");
//	}
//	
//}
