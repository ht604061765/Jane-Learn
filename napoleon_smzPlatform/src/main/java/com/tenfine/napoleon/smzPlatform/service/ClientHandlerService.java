package com.tenfine.napoleon.smzPlatform.service;

import com.tenfine.napoleon.smzPlatform.client.ClientHandler;
import com.tenfine.napoleon.user.dao.po.Device;

public interface ClientHandlerService {
	
	/**
	 * 【841】上传采集资料成功
	 */
	public void onSyncOutUserSuccess(byte[] content);
	
	/**
	 * 【841】同步外部人员信息失败
	 */
	public void onSyncOutUserFail(String msg);
	
	/**
	 * 【825】上传考勤成功
	 */
	public void onUploadAttendanceSuccess(String deviceNo, String session, byte[] content);
	
	/**
	 * 【823】登录失败(啥也没做)
	 */
	public void onLoginFail(String deviceNo, String msg);

	/**
	 * 【823】登录成功
	 */
	public void onLoginSuccess(Device device, ClientHandler handler);

	/**
	 * 【824】获取项目成员成功
	 */
	public void onDeviceInfoSuccess(String deviceNo, byte[] content);
	
	/**
	 * 【832】上传采集资料成功
	 */
	public void onUploadUserSuccess(String deviceNo, String session, byte[] content);
}
