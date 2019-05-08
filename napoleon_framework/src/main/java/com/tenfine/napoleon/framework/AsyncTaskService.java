package com.tenfine.napoleon.framework;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 用spring boot的线程池异步执行线程
 * @author Hunter
 */
@Service("AsyncTaskService")
public class AsyncTaskService {
	
	/**
	 * 用spring boot的线程池异步执行线程
	 */
	@Async
	public void executeAsyncTask(Thread thread) {
		thread.run();
	}
}
