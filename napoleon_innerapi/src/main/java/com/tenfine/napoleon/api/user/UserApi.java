package com.tenfine.napoleon.api.user;

import java.util.List;
import java.util.Map;

public interface UserApi {
	/**
	 * 校验用户状态
	 */
	void checkDeviceState(String userId, int platformProId, String projectName);
	/**
	 * 修改推送时间
	 */
	void changeUploadTime(String idNo, String platformProId);
	/**
	 * 修改推送时间
	 */
	void changeSyncTime(String idNo);
	
	/**
	 * 人员采集资料上传成功
	 */
	void uploadedUser(String idNo, String platformProId);
	
	/**
	 * 通过身份证获取某个人得用户对象
	 */
	Map<String, Object> getUserByIdNo(String idNo);
	
	/**
	 * 找到项目下需要提交得那个人得idNo
	 */
	String getUploadUserIdNo(String platformProId, String platformId);
	
	/**
	 * 找到项目下所有需要提交得人的idNo
	 */
	List<String> getUploadUserIds(String platformProId);
	
	/**
	 * 添加外部人员信息（全部信息）
	 */
	void addOutPerson(Map<String, Object> userInfo);
	
	/**
	 * 根据token获取用户对象
	 */
	Map<String, Object> getUserByToken(String token);
	
	/**
	 * 获取平台设备/用户
	 */
	List<Map<String, Object>> getPlatformDevice(String platformId);
	
	/**
	 * 通过设备号获取用户对象（设备号唯一）
	 */
	Map<String, Object> getUserByDeviceNo(String deviceNo);
	
	/**
	 * 禁用账号
	 */
	void stopDevice(String userId);
	
	/**
	 * 离职人员
	 */
	void quitPerson(int patformProjectId, int patformWorkerId);
	
	/**
	 * 入职人员
	 */
	void entryPerson(String idNO, int projectId, String projectName, String deviceNo, int workerId, String workerName);
}
