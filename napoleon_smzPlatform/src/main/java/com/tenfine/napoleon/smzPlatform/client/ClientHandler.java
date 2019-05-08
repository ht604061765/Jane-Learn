package com.tenfine.napoleon.smzPlatform.client;

import com.tenfine.napoleon.framework.util.SpringUtil;
import com.tenfine.napoleon.smzPlatform.message.ReceiveMessageBo;
import com.tenfine.napoleon.smzPlatform.service.ClientHandlerService;
import com.tenfine.napoleon.user.dao.po.Device;
import com.tenfine.napoleon.user.service.UserService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 指令接收器
 * @author Hunter
 */
public class ClientHandler extends SimpleChannelInboundHandler<ReceiveMessageBo> {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    public Channel channel;
    public String session = "";
    public Device device;
    private ClientHandlerService handlerService;
    private static UserService userService;

    /**
     * handler是否有效
     */
    public boolean isRunning = false;

    public ClientHandler(Device device, String session) {
        this.device = device;
        this.session = session;
        this.userService = (UserService) SpringUtil.getBean("UserService");

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ReceiveMessageBo recMsg) throws Exception {
        if (recMsg.success()) {
            if (recMsg.getCmd() == CMD.login.getCode()) {
                //【823】：登录成功，改变运行状态，将接收器放入缓存
                isRunning = true;
                channel = ctx.channel();
                if (device.getPlatformProId() == null || device.getPlatformProId() == "") {
                    Device curDevice = userService.getDeviceByDeviceNo(device.getDeviceNo());
                    if (curDevice.getPlatformProId() != null || curDevice.getPlatformProId() != "") {
                        device.setPlatformProId(curDevice.getPlatformProId());
                        device.setPlatformProName(curDevice.getPlatformProName());
                    }
                }
                handlerService.onLoginSuccess(device, this);
            } else if (recMsg.getCmd() == CMD.upload_attendance.getCode()) {
                //【825】：考勤数据上传成功
                handlerService.onUploadAttendanceSuccess(device.getDeviceNo(), recMsg.getSession(), recMsg.getContent());
            } else if (recMsg.getCmd() == CMD.upload_attendance_file_address.getCode()) {
                //【842】：带文件和定位得考勤数据上传成功
                handlerService.onUploadAttendanceSuccess(device.getDeviceNo(), recMsg.getSession(), recMsg.getContent());
            } else if (recMsg.getCmd() == CMD.get_device_info.getCode()) {
                //【824】：获取项目人员名单成功
                handlerService.onDeviceInfoSuccess(device.getDeviceNo(), recMsg.getContent());
            } else if (recMsg.getCmd() == CMD.upload_user.getCode()) {
                //【832】：上传采集资料成功
                handlerService.onUploadUserSuccess(device.getDeviceNo(), recMsg.getSession(), recMsg.getContent());
            } else if (recMsg.getCmd() == CMD.get_user_info.getCode()) {
//				//【841】：外部人员学习资料获取成功
                handlerService.onSyncOutUserSuccess(recMsg.getContent());
            } else if (recMsg.getCmd() == CMD.heartbeat.getCode()) {
//				//【65535】：收到心跳包反馈
                CacheManager.getHeartbeatCache().put(device.getDeviceNo(), System.currentTimeMillis());
            }

        } else {// 返回结果为失败
            if (recMsg.getCmd() == CMD.login.getCode()) {
                //【823】：登录失败
                isRunning = false;
                // 虽然登录失败，但是通道是连接着的
                channel = ctx.channel();
                // 将处理器丢入缓存
                CacheManager.putHandler(device, this);
                logger.error("【823】【REC】登录失败，deviceNo=" + device.getDeviceNo() + ",msg=" + new String(recMsg.getContent()));
            } else if (recMsg.getCmd() == CMD.upload_attendance_file.getCode()) {
                //【825】：考勤数据上传失败
                logger.error("【828】【REC】带文件考勤数据上传失败");
            } else if (recMsg.getCmd() == CMD.get_device_info.getCode()) {
                //【824】：获取项目白名单失败
                logger.error("【824】【REC】获取项目白名单失败");

            } else if (recMsg.getCmd() == CMD.upload_user.getCode()) {
                //【832】：上传采集资料成功
                logger.error("【832】【REC】上传采集资料失败");

            } else if (recMsg.getCmd() == CMD.get_user_info.getCode()) {
                //【832】：上传采集资料成功
                logger.error("【841】【REC】同步外部采集人员失败");
                handlerService.onSyncOutUserFail(new String(recMsg.getContent()));
            }

        }
    }

    public void setHandlerService(ClientHandlerService handlerService) {
        this.handlerService = handlerService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    /**
     * 设备业务交互控制
     * true:登录成功，可以交互，false:登录失败，停止交互
     *
     * @return
     */
    public boolean isRunning() {
        return isRunning;
    }
}
