package com.tenfine.napoleon.smzPlatform.client;

import com.tenfine.napoleon.api.user.UserApi;
import com.tenfine.napoleon.framework.util.*;
import com.tenfine.napoleon.smzPlatform.dao.po.Project;
import com.tenfine.napoleon.smzPlatform.message.SendMessageBo;
import com.tenfine.napoleon.smzPlatform.message.UploadUserVo;
import com.tenfine.napoleon.smzPlatform.service.PlatformService;
import com.tenfine.napoleon.smzPlatform.service.impl.ClientHandlerServiceImpl;
import com.tenfine.napoleon.smzPlatform.socket.SocketDecoder;
import com.tenfine.napoleon.smzPlatform.socket.SocketEncoder;
import com.tenfine.napoleon.user.dao.po.Device;
import com.tenfine.napoleon.user.service.UserService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseClient extends Thread{
	
    private static final Logger logger = LoggerFactory.getLogger(BaseClient.class);
    	
	private static PlatformService platformService;
	private static UserService userService;

	/**
	 * user模块变为公用模块，这个变量要被清除，请注意尽量少用
	 */
	private static UserApi userApi;
	
	/**
	 * Spring Boot 注解注入是非线程安全得，所以在线程内部，不能使用注解注入得方式
	 * 从应用上下文中获取 Hunter 190118
	 */
	public BaseClient() {
		this.platformService = (PlatformService) SpringUtil.getBean("PlatformService");
		this.userService = (UserService) SpringUtil.getBean("UserService");
		this.userApi = (UserApi) SpringUtil.getBean("UserApi");
	}
	
	@Override
	public void run() {
		super.run();
		while (true) {
			// 是首次进入或者每隔60秒重新登录一次
			if (sleepSecond % 60 == 0 || sleepSecond == 0) {
				// 获取对应平台下得所有设备
//				List<Map<String, Object>> deviceList = userApi.getPlatformDevice("platformId"); //写死平台Id条件，查询不做过滤，默认获取全部设备
				//所有设备都要模拟连接
				List<Device> deviceList = userService.getAllDevice();
				if(deviceList != null) {
					for(int i = 0, n = deviceList.size(); i < n; i++) {
						// 获取设备常量
						Device device = deviceList.get(i);
						String deviceNo = device.getDeviceNo();
						// 初始化会话参数
						String session = IDUtil.getUUID16();
						// 查看设备是否存在连接
						ClientHandler handler = CacheManager.getHandlerByDevice(deviceNo);
						Channel channel;
						if(handler == null) {
							// 如果设备不存在对应的接收器缓存，则发起连接
							channel = connectSocket(device, session);
							// 链接完毕，用新的Channel发送登录指令
							login(channel, deviceNo, session);
						}else {
							channel = handler.channel;
							if (!channel.isOpen() || !channel.isWritable()){
								CacheManager.removeHandler(deviceNo);
								channel = connectSocket(device, session);
							}
							// 链接既存，用旧的Channel发送登录指令
							login(channel, deviceNo, session);
						}
					}	
				}
				
			}
			// 每6秒遍历一次所有需要提交的人员
			if (sleepSecond % (20) == 0) {
				submitCollectUser(); //【832】指令启动，上传采集资料
//				syncOutUser(); //【841】指令启动方式，同步非本平台采集人员信息
			}
			// 每执行一次睡眠十秒
			sleepThread(5);
		}
	}
	
	
//	/**
//	 * 同步非本平台采集人员信息【841】
//	 * @param customerId
//	 */
//	public static void syncOutUser() {
//		LoggerUtil.getLogger().info("【841】【SEND】开始");
//		String session = IDUtil.getUUID16();
//		String deviceNo = "CMD841-CHANNEL"; // 841专属通道设备号
//		Channel channel = connectScoket(deviceNo, session);
//		
//		int sendCount = 0; // 发送次数，每次遍历发送十次请求
//		int installCount = 0; // 消息组装人员身份证号数量，每条消息组装十个
//		byte[] content = new byte[0]; // 组装消息实体
//
//		List<Project> projectList = platformService.getAllProject(); // 遍历所有项目
//		if(projectList.size() > 0) {
//			outControl: for (int i = 0, n = projectList.size(); i < n; i++) {
//				String platformProId = projectList.get(i).getPlatformProjectId();
////				List<Map<String, Object>> outUserList = userApi.getOutUserList(platformProId);
//				List<ProjectPersonOut> outUserList = userService.getOutUserList(platformProId);
//				if(outUserList.size() > 0) {
//					for (int j = 0, k = outUserList.size(); j < k; j++) {
//						String idNo = outUserList.get(j).getIdNo();
//						userApi.changeSyncTime(idNo); //加了异步
//						content = ByteUtil.concatAll(content, idNo.getBytes());
//						installCount ++;
//						if(installCount >= 10) {
//							// 当一条消息已经组装了十个身份证号的时候，需要写出去，并将installCount清零，sendCount加一
//							SendMessageBo newMsg = MessageUtil.createGetOutUserMsg(Integer.valueOf(platformProId), installCount, content, session);
//							channel.writeAndFlush(newMsg);
//							content = new byte[0]; // content初始化
//							sendCount ++;
//							if(sendCount >= 10) {
//								// 如果已经发送了十条出去，则终止循环
//								break outControl;
//							}
//							LoggerUtil.getLogger().info("【841】【SEND】发送一条841，项目=" + platformProId + "身份证数量=" + installCount);
//							installCount = 0;
//						}
//					}
//					// 项目下所有的人不足十个，那就发送已有数据
//					SendMessageBo newMsg = MessageUtil.createGetOutUserMsg(Integer.valueOf(platformProId), installCount, content, session);
//					channel.writeAndFlush(newMsg);
//					content = new byte[0]; // content初始化
//					sendCount ++;
//					if(sendCount >= 10) {
//						// 如果已经发送了十条出去，则终循环
//						break outControl;
//					}
//					LoggerUtil.getLogger().info("【841】【SEND】发送一条841，项目=" + platformProId + "身份证数量=" + installCount);
//					installCount = 0;
//				}
//			}
//		}
//	}
	
	/**
	 * 上传采集资料【832】
	 */
	public void submitCollectUser() {
		logger.info("【832】【send】开始");
		// 获取平台下的所有项目
		List<Project> projectList = platformService.getAllProject();
		// 记录有提交人员的项目
		List<Project> subProjects = new ArrayList<>();
		//每个项目每次提交全部人
		for (Project project : projectList) {
			//找到需要提交得那个人Id
			List<String> ids = userApi.getUploadUserIds(project.getPlatformProjectId());
			if(ids.size() > 0) {
				// 加入到同步项目列表
				subProjects.add(project);
				for (int i = 0; i < ids.size(); i++) {
					String uploadIdNo = ids.get(i);
					//是一个personInfo对象
					Map<String, Object> personInfo = userApi.getUserByIdNo(uploadIdNo);
					if(personInfo != null) {
						//找到一个项目下有效得连接
						Channel channel = CacheManager.getProjectChannel(project.getPlatformProjectId());
						if(channel == null) {
							logger.info("【832】【SEND】项目下没有有效的channel连接，project=" + project.getName());
							continue;
						}
						String idCardPath = PathStatic.getCollectPath(uploadIdNo) + "cardFront.jpg";
						String collectionPath = checkCollectionPath(uploadIdNo);
						if(collectionPath == null) {
							logger.info("【832】【SEND】未找到人员得采集照片，idNo=" + uploadIdNo);
							continue;
						}
						UploadUserVo uploadUser = new UploadUserVo();
						uploadUser.setIdNo(uploadIdNo);
						uploadUser.setName((String) personInfo.get("name"));
						// 1：男，0：女
						uploadUser.setSex("男".equals(personInfo.get("sex")) ? 1 : 0);
						uploadUser.setPlatformTeamId(-1);
						uploadUser.setNation((String) personInfo.get("nation"));
						uploadUser.setBirthday((String) personInfo.get("birthday"));
						uploadUser.setAddress((String) personInfo.get("address"));
						uploadUser.setProvide((String) personInfo.get("provide"));
						uploadUser.setValidity((String) personInfo.get("period"));
						uploadUser.setIdCardImgPath(idCardPath);
						uploadUser.setCollectPath(collectionPath);
						//16位随机会话Id
						String session = IDUtil.getUUID16();
						SendMessageBo newMsg = MessageUtil.createUploadUserMsg(uploadUser, session);
						channel.writeAndFlush(newMsg);

						Map<String, Object> paramMap = new HashMap<>();
						paramMap.put("sendTime", System.currentTimeMillis());
						paramMap.put("idNo", uploadIdNo);
						paramMap.put("proId", project.getPlatformProjectId());
						// 将此次会话内容存入缓存
						CacheManager.getSessionCache_832().put(session, paramMap);

					}
				}
			}
		}
		if (subProjects.size() > 0){
			platformService.changeUploadTimeBatch(subProjects);
		}

	}
	
	private static String checkCollectionPath(String idNo) {
		String basePath = PathStatic.getCollectPath(idNo);
		File learnImgDir = new File(basePath + "RGB.jpg");
		if(learnImgDir.isFile()) {
			return learnImgDir.getPath();
		}else {
			return null;
		}
	}
	
	private static EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
	
	private static Channel connectSocket(Device device, String session) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				// 内容编解码处理
				pipeline.addLast("encode", new SocketEncoder());
				pipeline.addLast("decode", new SocketDecoder());
				
				ClientHandler clientHandler = new ClientHandler(device, session);
				pipeline.addLast("handler", clientHandler);
				clientHandler.setHandlerService(new ClientHandlerServiceImpl());
			}
		});
		// 连接实名制服务端
		boolean isConnect = false;
		Channel channel = null;
		while (!isConnect){
			try {
				channel =  bootstrap.connect(PlatfromProperties.getSocketServer(), PlatfromProperties.getSocketPort()).sync().channel();
				isConnect = true;
			} catch (Exception e) {
				logger.error("实名制平台链接失败，socketServer=" + PlatfromProperties.getSocketServer() + ",socketPort=" + PlatfromProperties.getSocketPort() +
						"，30秒后尝试重新链接");
				e.printStackTrace();
				sleepThread(30);
			}
		}
		return channel;
	}
	
	/**
	 * 设备登录方法
	 */
	public void login(Channel channel, String deviceNo, String session) {
		String thsn = "C7A72EED-976C-415C-83B4-F36DE630";
		byte[] thsnB = thsn.getBytes();
		String deviceNoString = deviceNo + "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";
		byte[] deviceNoByte = deviceNoString.getBytes();
		byte[] xor = { 0x00 };
		byte[] content = ByteUtil.concatAll(thsnB, deviceNoByte, xor);
		SendMessageBo msg = MessageUtil.CreateMsg(ByteUtil.xor(content), CMD.login.getCode(), session, true, 2);
		channel.writeAndFlush(msg);
	}
	
	/**
	 * 睡眠秒数
	 */
	private static int sleepSecond = 0;

	/**
	 * 线程睡眠方法
	 * @param second
	 */
	private static void sleepThread(Integer second) {
		try {
			BaseClient.sleep(second * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sleepSecond += second;
	}
}
