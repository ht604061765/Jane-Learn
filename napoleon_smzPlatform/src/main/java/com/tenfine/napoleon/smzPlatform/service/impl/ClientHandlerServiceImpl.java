package com.tenfine.napoleon.smzPlatform.service.impl;

import com.tenfine.napoleon.api.attendance.AttendanceApi;
import com.tenfine.napoleon.api.user.UserApi;
import com.tenfine.napoleon.framework.AsyncTaskService;
import com.tenfine.napoleon.framework.util.ByteUtil;
import com.tenfine.napoleon.framework.util.MapUtil;
import com.tenfine.napoleon.framework.util.SpringUtil;
import com.tenfine.napoleon.smzPlatform.client.CacheManager;
import com.tenfine.napoleon.smzPlatform.client.ClientHandler;
import com.tenfine.napoleon.smzPlatform.client.FollowThread;
import com.tenfine.napoleon.smzPlatform.message.SyncOutUserVo;
import com.tenfine.napoleon.smzPlatform.service.ClientHandlerService;
import com.tenfine.napoleon.smzPlatform.service.PlatformService;
import com.tenfine.napoleon.user.dao.po.Device;
import com.tenfine.napoleon.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientHandlerServiceImpl implements ClientHandlerService {
	
    private static final Logger logger = LoggerFactory.getLogger(ClientHandlerServiceImpl.class);

	private static AsyncTaskService asyncTaskService;
	private static UserApi userApi;
	private static PlatformService platformService;
	private static AttendanceApi attendanceApi;
	private static UserService userService;


	public ClientHandlerServiceImpl() {
		this.userApi = (UserApi) SpringUtil.getBean("UserApi");
		this.platformService = (PlatformService) SpringUtil.getBean("PlatformService");
		this.attendanceApi = (AttendanceApi) SpringUtil.getBean("AttendanceApi");
		this.userService = (UserService) SpringUtil.getBean("UserService");
		
		this.asyncTaskService = (AsyncTaskService) SpringUtil.getBean("AsyncTaskService");

	}

	@Override
	public void onLoginFail(String deviceNo, String msg) {

	}

	
	@Override
	public void onLoginSuccess(Device device, ClientHandler handler) {
		String deviceNo = device.getDeviceNo();
		// 登录成功将handler缓存起来（已经存在的链接不会重连，所以不需要清除以前的）
		CacheManager.putHandler(device, handler);
		
		// 管理后续线程：遍历所有线程，如果没有该设备线程，则启动一个新线程，注意：线程的运行与否通过handler的isRunning字段控制
		boolean isExists = false;
		Map<String, Thread> threads = CacheManager.getThreadCache();
		if(threads != null) {
			if(threads.containsKey(deviceNo)) {
				isExists = true;
			}
		}
		if(!isExists) {
			// 启动新线程
			logger.info("【823】设备登录成功，设备="+ deviceNo +"，【启动】新线程");
			FollowThread thread = new FollowThread(deviceNo);
			// 给线程设置名称
			thread.setName("设备业务线程_" + deviceNo);
			asyncTaskService.executeAsyncTask(thread);

			// 把线程加入缓存
			CacheManager.putThreadCache(deviceNo, thread);
		}
	}

	@Override
	public void onDeviceInfoSuccess(String deviceNo, byte[] content) {
		logger.info("【824】【REC】白名单获取，deviceNo=" + deviceNo);
		Device device = userService.getDeviceByDeviceNo(deviceNo);
		if(device == null) {
			logger.error("【824】【REC】设备编号未能获取到设备对象，放弃接收，deviceNo=" + deviceNo);
			return;
		}
//		Map<String, Object> user = userApi.getUserByDeviceNo(deviceNo);
		int length = content.length;
		if(length > 0){
			int index = 0;
			// 项目ID
			byte[] projectIdByte = ByteUtil.subBytes(content, index, 4);
			int projectIdInt = ByteUtil.byteTo4Int(projectIdByte);
			index += 4;
			
			//项目名称
			byte[] projectNameByte = ByteUtil.subBytes(content, index, 100);
			String projectNameStr = new String(projectNameByte).replaceAll("\0", "");
			index += 100;
			// 校验用户状态，如果设备之前是未绑定状态，要清除handler缓存
			userService.checkDeviceState(device, projectIdInt, projectNameStr);
			// 校验项目状态，返回是否是新项目
			boolean newProject = platformService.checkProject(device.getId(), deviceNo, projectIdInt, projectNameStr);
			if(newProject) { //如果是新项目,需要创建用户组
				attendanceApi.createUserGroup(projectIdInt, device.getPlatformId());
			}
			if (length > 106) {// 还有列表内容
				while (length - index > 2) {
					
					byte[] typeByte = ByteUtil.subBytes(content, index, 1);
					int typeInt = ByteUtil.byteTo1Int(typeByte);
					index += 1;
					
					byte[] workerNoByte = ByteUtil.subBytes(content, index, 4);
					int workerNoInt = ByteUtil.byteTo4Int(workerNoByte);
					index += 4;
					
					if(typeInt == 0) { //入职
						
						byte[] personNameByte = ByteUtil.subBytes(content, index, 10);
						String personNameStr = new String(personNameByte).replaceAll("\0", "");
						index += 10;
						
						byte[] idNoByte = ByteUtil.subBytes(content, index, 18);
						String idNoStr = new String(idNoByte).replaceAll("\0", "");
						index += 18;
//						LoggerUtil.getLogger().info("【824】【REC】白名单获取，人员=" + personNameStr + ",idNo=" + idNoStr);
						// 入职用户
						userApi.entryPerson(idNoStr, projectIdInt, projectNameStr, deviceNo, workerNoInt, personNameStr);
						// 给考勤记录加上workNo
						attendanceApi.entryPerson(idNoStr, deviceNo, workerNoInt);
						index += 2095; // 未解析位置长度
					}else if(typeInt == 1) { //离职
						// 离职用户
						userApi.quitPerson(projectIdInt, workerNoInt);
					}
				}
				index += 1;
			} else {
			}
		}else{
		}
	}

	public static void main(String[] args) {
//		byte content[] = {1, 0, 0, 0, -26, -75, -117, -24, -81, -107, -23, -95, -71, -25, -101, -82, 97, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 0, 0, 0, 1, 41, 0, 0, 0, 1, 61, 0, 0, 0, 1, 64, 0, 0, 0, 1, 121, 0, 0, 0, 1, -115, 0, 0, 0, 1, -15, 0, 0, 0, 0, 105, 1, 0, 0, -23, -97, -87, -26, -74, -101, 0, 0, 0, 0, 50, 50, 48, 54, 56, 49, 49, 57, 57, 51, 48, 49, 49, 57, 49, 51, 49, 56, -23, -110, -94, -25, -83, -117, -27, -73, -91, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -122};
//		int length = content.length;
//		int index = 0;
//		
//		// 项目ID
//		byte[] projectIdByte = ByteUtil.subBytes(content, index, 4);
//		int projectIdInt = ByteUtil.byteTo4Int(projectIdByte);
//		index += 4;
//		
//		//项目名称
//		byte[] projectNameByte = ByteUtil.subBytes(content, index, 100);
//		String projectNameStr = new String(projectNameByte).replaceAll("\0", "");
//		index += 100;
//		List<Map<String, Object>> personList = new ArrayList<>();
//
//		while (length - index > 2) {
//			Map<String, Object> personInfo = new HashMap<String, Object>();
//			
//			byte[] typeByte = ByteUtil.subBytes(content, index, 1);
//			int typeInt = ByteUtil.byteTo1Int(typeByte);
//			index += 1;
//			
//			byte[] grNoByte = ByteUtil.subBytes(content, index, 4);
//			int grNoInt = ByteUtil.byteTo4Int(grNoByte);
//			index += 4;
//			
//			if(typeInt == 0) { //入职
//				
//				
//				byte[] personNameByte = ByteUtil.subBytes(content, index, 10);
//				String personNameStr = new String(personNameByte).replaceAll("\0", "");
//				index += 10;
//				
//				byte[] idNoByte = ByteUtil.subBytes(content, index, 18);
//				String idNoStr = new String(idNoByte).replaceAll("\0", "");
//				index += 18;
//				
//				personInfo.put("在职", "在职");
//				personInfo.put("工人编号", grNoInt);
//				personInfo.put("工人姓名", personNameStr);
//				personInfo.put("身份证号码", idNoStr);
//				personList.add(personInfo);
//				// 入职用户
//				index += 2095; //非解析位置
//			}else if(typeInt == 1) { //离职
//				personInfo.put("在职", "离职");
//				personInfo.put("工人编号", grNoInt);
//				personInfo.put("工人姓名", "");
//				personInfo.put("身份证号码", "");
//				personList.add(personInfo);
//				userApi.quitPerson(projectIdInt, grNoInt);
//
//			}
//		}
//		
//		
//		System.out.println(personList.toString());
	}
	
	@Override
	public void onUploadUserSuccess(String deviceNo, String session, byte[] content) {
		// 开始解析收到的消息，是否成功
		byte[] success = ByteUtil.subBytes(content, 0, 1);
		int isSuccess = ByteUtil.byteTo1Int(success);
		Map<String, Object> sessionParamCache = CacheManager.getSessionCache_832();
		Map<String, Object> paramMap;
		if(sessionParamCache.containsKey(session)) {
			paramMap = (Map<String, Object>) sessionParamCache.get(session);
			
		}else {
			logger.error("【832】【REC】提交成功，但是未匹配到会话缓存,放弃本次接收内容");
			return;
		}

		if(isSuccess == 0) { //成功
			long sendTime = (long) paramMap.get("sendTime");
			String idNo = (String) paramMap.get("idNo");
			String proId = (String) paramMap.get("proId");
			long recTime = System.currentTimeMillis();
			userApi.uploadedUser(idNo, proId);
			logger.info("【832】【REC】提交成功,idNo=" + idNo + "，耗时:" + (recTime-sendTime) + "ms");
			// 用完要清除缓存
			sessionParamCache.remove(session);
		}else {
			byte[] msg = ByteUtil.subBytes(content, 1, 30);
			String msgStr = new String(msg);
			logger.error("【832】【REC】提交失败,人员=" + paramMap.get("idNo") + ",rtnMsg=" + msgStr);
		}
	}

	@Override
	public void onUploadAttendanceSuccess(String deviceNo, String session, byte[] content) {
		Map<String, List<Map<String, Object>>> uploadingAttendancesList = CacheManager.getSessionCache_attend();
		if(uploadingAttendancesList.containsKey(session)) {
			List<Map<String, Object>> attendanceList = uploadingAttendancesList.get(session);
			for(Map<String, Object> attendance : attendanceList) {
				attendanceApi.uploadedRecord((String) attendance.get("id"));
			}
			uploadingAttendancesList.remove(session);
			logger.info("【825/842】【REC】：上传考勤数据成功");
		}else {
			logger.error("【825/842】【REC】：上传考勤数据成功，但是未匹配到会话缓存，放弃本次接收");
		}

	}

	@Override
	public void onSyncOutUserSuccess(byte[] content) {
		List<SyncOutUserVo> userInfoList = null;
		try {
			userInfoList = explainContent_841(content);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		// 如果解析出了人员
		if(userInfoList.size() > 0) {
			for (int i = 0; i < userInfoList.size(); i++) {
				try {
					syncUserInfo_841(userInfoList.get(i));
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		}
		
	}

	@Override
	public void onSyncOutUserFail(String msg) {
		logger.error("【841】同步外部人员数据失败，,msg=" + msg);
	}
	
	private static List<SyncOutUserVo> explainContent_841(byte[] content) {
		// 开始解析
		int explainIndex = 0;
		
		// 数据列表大小，占2字节
		byte[] dataSizeByte = ByteUtil.subBytes(content, explainIndex, 2);
		int dataSizeInt = ByteUtil.byteTo1Int(dataSizeByte);
		explainIndex += 2;
		
		List<SyncOutUserVo> userInfoList = new ArrayList<>(dataSizeInt);
		for (int i = 0; i < dataSizeInt; i++) {
			SyncOutUserVo userInfo = new SyncOutUserVo();
			
			// 用户名称,占10字节
			byte[] userNameByte = ByteUtil.subBytes(content, explainIndex, 10);
			String userNameStr = new String(userNameByte).replaceAll("\0", "");
			userInfo.setUserName(userNameStr);
			explainIndex += 10;
			
			// 身份证号,占18字节
			byte[] idNumberByte = ByteUtil.subBytes(content, explainIndex, 18);
			String idNumberStr = new String(idNumberByte).replaceAll("\0", ""); //.replaceAll("\0", "");
			userInfo.setIdNumber(idNumberStr);
			explainIndex += 18;

			// 民族，占1字节
			byte[] nationByte = ByteUtil.subBytes(content, explainIndex, 1);
			int nationInt = ByteUtil.byteTo1Int(nationByte);
			userInfo.setNation(nationInt);
			explainIndex += 1;
			
			// 性别，占2字节
			byte[] sexByte = ByteUtil.subBytes(content, explainIndex, 2);
			String sexStr = new String(sexByte).replaceAll("\0", "");
			userInfo.setSex(sexStr);
			explainIndex += 2;
			
			// 身份证地址，占140字节
			byte[] idCardAddressByte = ByteUtil.subBytes(content, explainIndex, 140);
			String idCardAddressStr = new String(idCardAddressByte).replaceAll("\0", "");
			userInfo.setIdCardAddress(idCardAddressStr);
			explainIndex += 140;
			
			// 生日，占16字节
			byte[] birthdayByte = ByteUtil.subBytes(content, explainIndex, 16);
			String birthdayStr = new String(birthdayByte).replaceAll("\0", "");
			userInfo.setBirthday(birthdayStr);
			explainIndex += 16;
			
			// 发证机关，占60字节
			byte[] certificationAuthorityByte = ByteUtil.subBytes(content, explainIndex, 60);
			String certificationAuthorityStr = new String(certificationAuthorityByte).replaceAll("\0", "");
			userInfo.setCertificationAuthority(certificationAuthorityStr);
			explainIndex += 60;
			
			// 有效期，占64字节
			byte[] validityByte = ByteUtil.subBytes(content, explainIndex, 64);
			String validityStr = new String(validityByte).replaceAll("\0", "");
			userInfo.setValidity(validityStr);
			explainIndex += 64;
			
			// 项目编号，占4字节
			byte[] projectIdByte = ByteUtil.subBytes(content, explainIndex, 4);
			int projectIdInt = ByteUtil.byteTo1Int(projectIdByte);
			userInfo.setProjectId(projectIdInt);
			explainIndex += 4;
			
			// 班组编号，占4字节
			byte[] teamIdByte = ByteUtil.subBytes(content, explainIndex, 4);
			int teamIdInt = ByteUtil.byteTo1Int(teamIdByte);
			userInfo.setTeamId(teamIdInt);
			explainIndex += 4;
			
			// 工种编号，占4字节
			byte[] workTyepByte = ByteUtil.subBytes(content, explainIndex, 4);
			int workTyepInt = ByteUtil.byteTo1Int(workTyepByte);
			userInfo.setWorkTypeId(workTyepInt);
			explainIndex += 4;
			
			// 采集照片长度（GL），占4字节
			byte[] collectionImgLongByte = ByteUtil.subBytes(content, explainIndex, 4);
			int collectionImgLongInt = ByteUtil.byteTo4Int(collectionImgLongByte);
			userInfo.setCollectionImgLong(collectionImgLongInt);
			explainIndex += 4;

			// 采集照片，占‘GL’字节
			byte[] collectionImgByte = ByteUtil.subBytes(content, explainIndex, collectionImgLongInt);
			userInfo.setCollectionImg(collectionImgByte);
			explainIndex += collectionImgLongInt;
			
			// 身份证照片长度（pl），占4字节
			byte[] idCardImgLongByte = ByteUtil.subBytes(content, explainIndex, 4);
			int idCardImgLongInt = ByteUtil.byteTo4Int(idCardImgLongByte);
			userInfo.setIdCardImgLong(idCardImgLongInt);
			explainIndex += 4;
			
			// 身份证照片，占‘pl’字节
			byte[] idCardImgByte = ByteUtil.subBytes(content, explainIndex, idCardImgLongInt);
			userInfo.setIdCardImg(idCardImgByte);
			explainIndex += idCardImgLongInt;
			
			// 红外照片长度（HL），占4字节
			byte[] infraRedImgLongByte = ByteUtil.subBytes(content, explainIndex, 4);
			int infraRedImgLongInt = ByteUtil.byteTo4Int(infraRedImgLongByte);
			userInfo.setInfraRedImgLong(infraRedImgLongInt);
			explainIndex += 4;
			// 红外照片，占‘HL’字节
			
			byte[] infraRedImgByte = ByteUtil.subBytes(content, explainIndex, infraRedImgLongInt);
			userInfo.setInfraRedImg(infraRedImgByte);
			explainIndex += infraRedImgLongInt;

			userInfoList.add(userInfo);
		}
		logger.info("【841】【REC】解析人员数据成功，收到数据条数=" + userInfoList.size());
		return userInfoList;
	}	
	
	/**
	 *【841】指令后续操作
	 * @param userInfo
	 */
	private static void syncUserInfo_841(SyncOutUserVo userInfo) {
		userApi.addOutPerson(MapUtil.beanToMap(userInfo));
//		try {
//			// 写入人员二代人脸采集照片，需要带RGB.jpg
//			saveFile(userInfo, "collectionFaceImg");
//			// 写入人员身份证照片，
//			saveFile(userInfo, "idCardImg");
//			// 写入人员红外照片，需要带IR.jpg字样
//			saveFile(userInfo, "infraRedImg");
//		} catch (IOException e) {
//			e.printStackTrace();
//			return;
//		}
	}
}
