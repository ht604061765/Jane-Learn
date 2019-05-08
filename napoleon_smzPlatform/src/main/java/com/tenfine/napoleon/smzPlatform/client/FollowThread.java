package com.tenfine.napoleon.smzPlatform.client;

import com.tenfine.napoleon.api.attendance.AttendanceApi;
import com.tenfine.napoleon.api.user.UserApi;
import com.tenfine.napoleon.framework.util.IDUtil;
import com.tenfine.napoleon.framework.util.PathStatic;
import com.tenfine.napoleon.framework.util.PlatfromProperties;
import com.tenfine.napoleon.framework.util.SpringUtil;
import com.tenfine.napoleon.smzPlatform.dao.po.Project;
import com.tenfine.napoleon.smzPlatform.message.SendMessageBo;
import com.tenfine.napoleon.smzPlatform.service.PlatformService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FollowThread extends Thread{
	
    private static final Logger logger = LoggerFactory.getLogger(FollowThread.class);
	
	private String deviceNo;

	private static PlatformService platformService;
	private static UserApi userApi;
	private static AttendanceApi attendanceApi;

	public FollowThread(String deviceNo) {
		this.deviceNo = deviceNo;
		this.platformService = (PlatformService) SpringUtil.getBean("PlatformService");
		this.userApi = (UserApi) SpringUtil.getBean("UserApi");
		this.attendanceApi = (AttendanceApi) SpringUtil.getBean("AttendanceApi");
	}
	
	@Override
	public void run() {

		while (true) {
			ClientHandler clientHandler = CacheManager.getHandlerByDevice(deviceNo);
			if(clientHandler == null) {
				break;
			}
			if(!clientHandler.isRunning) {
				logger.info("登录失败，设备业务线程暂停一分钟");
				threadSleep(60);
				break;
			}
			// 每60秒获取设备所处项目人员名单，指令【824】
			if (sleepSecond % (30) == 0) {
				Channel channel = clientHandler.channel;
				if(channel.isWritable()) {
					String lastTime = "20000101010101";
					Map<String, Object> userInfo = userApi.getUserByDeviceNo(deviceNo);
					String platformProId = (String) userInfo.get("platformProId");
					String platformId = (String) userInfo.get("platformId");
					if(platformProId != null) {
						Project project = platformService.getPlatformProject(platformProId, platformId);
						if(project != null) {
							String syncTime = project.getSyncTime();
							lastTime = syncTime.replace("-", "").replaceAll(":", "").replaceAll(" ", "");
						}
					}
					String session = IDUtil.getUUID16();
					SendMessageBo deviceInfoMsg = MessageUtil.createGetDeviceInfoMsg(deviceNo, session, lastTime);
					channel.writeAndFlush(deviceInfoMsg);
					logger.info("【824】【SEND】deviceNo=" + deviceNo + "，同步时间=" + lastTime);
				}
			}
			// 每20秒提交考勤记录
			if (sleepSecond % (20) == 0) {
				String attendCmd = PlatfromProperties.getAttendanceCmd();
				if(attendCmd.equals(String.valueOf(CMD.upload_attendance_file_address.getCode()))) {
					logger.info("【842】【SEND】:开始");
					uploadAttendsBy842(clientHandler);
				}else if (attendCmd.equals(String.valueOf(CMD.upload_attendance.getCode()))) {
					logger.info("【825】【SEND】:开始");
					uploadAttendsBy825(clientHandler);
				}
			}
			threadSleep(10);
		}
	}
	
	/**
	 * 用825指令提交考勤数据
	 */
	private void uploadAttendsBy825(ClientHandler clientHandler) {
		List<Map<String, Object>> attends = attendanceApi.getSubmitAttendance(deviceNo);
		if(attends == null) {
			return;
		}
		if(attends.size() > 200) {
			attends = attends.subList(0, 200);
		}
		Channel channel = clientHandler.channel;
		if(channel.isWritable()) {
			//批量修改考勤记录推送时间
			attendanceApi.submitAttendanceList(attends);
			//所有符合条件的考勤记录，可以上传
			String session = IDUtil.getUUID16();
			SendMessageBo newMsg = MessageUtil.createUploadAttendanceMsg(session, attends);
			channel.writeAndFlush(newMsg);
			logger.info("【825上传考勤记录】【SEND】:设备编号：" + clientHandler.device.getDeviceNo() + ",提交数据条数=" + attends.size());
			// 整理缓存
			CacheManager.getSessionCache_attend().put(session, attends);
		}else {
//			LoggerUtil.getLogger().info("【825】【SEND】:设备通道无效，等待下次提交，deviceNo=" + clientHandler.deviceNo);
		}
		
	}
	
	/**
	 * 用842指令提交考勤数据
	 */
	private void uploadAttendsBy842(ClientHandler clientHandler) {
		Channel channel = clientHandler.channel;
		if(channel.isWritable()) {
			List<Map<String, Object>> attends = attendanceApi.getSubmitAttendance(deviceNo);
			if(attends == null) {
				return;
			}
			if(attends.size() > 200) {
				attends = attends.subList(0, 200);
			}
			//组装最终要发送得记录列表
			List<Map<String, Object>> sendList = new ArrayList<>();
			for(int i=0; i<attends.size(); i++) {
				Map<String, Object> attendance = attends.get(i);
				String idNo = (String) attendance.get("personIdNo");
				String timeStamp = (String) attendance.get("timeStamp");
				String attendanceFilePath = PathStatic.getAttendancePath(idNo) + idNo + "/" + timeStamp + "attend.jpg";
				File attendanceFile = new File(attendanceFilePath);
				if(attendanceFile.isFile()) {
					sendList.add(attendance);
				}else {
					logger.error("【842】【SEND】:未获取到考勤图片，idNo=" + idNo + ",timeStamp=" + timeStamp);
				}
			}
			//批量修改考勤记录推送时间
			attendanceApi.submitAttendanceList(sendList);
			//生成会话
			String session = IDUtil.getUUID16();
			SendMessageBo newMsg = MessageUtil.createUploadAttendanceWithFileAddressMsg(session, sendList);
			channel.writeAndFlush(newMsg);
			// 整理缓存
			CacheManager.getSessionCache_attend().put(session, sendList);
		}else {
//			LoggerUtil.getLogger().info("【842】【SEND】:设备通道无效，等待下次提交，deviceNo=" + clientHandler.deviceNo);
		}
	}
	
	
	public static void main(String[] args) {
//		for (int i = 0; i < 4; i++) {
//			System.out.println(i);
//			i = 10;
//		}
		long a1 = 300;
		long a2 = 400;
		System.out.println(a1>a2);
	}
	
	//睡眠秒数
	private int sleepSecond = 0; 
	// 线程睡眠方法
	private void threadSleep(Integer second) {
		try {
			sleepSecond += second;
			FollowThread.sleep(second * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
