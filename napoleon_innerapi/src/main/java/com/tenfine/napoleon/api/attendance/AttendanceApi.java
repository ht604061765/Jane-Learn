package com.tenfine.napoleon.api.attendance;

import java.util.List;
import java.util.Map;

public interface AttendanceApi {
	
	/**
	 * 创建用户组
	 */
	void createUserGroup(int projectIdInt, String platformId);
	
	/**
	 * 因为未在实名制入职也可以打卡
	 * 人员入职后需要将没有workerNo的考勤数据，补充上workerNo
	 */
	void entryPerson(String idNo, String deviceNo, int workerNoInt);
	
	/**
	 * 发出考勤记录时修改发送时间
	 */
	void submitAttendanceList(List<Map<String, Object>> sendList);
	
	/**
	 * 考勤记录上传成功
	 */
	void uploadedRecord(String attendanceId);
	
	/**
	 * 获取设备下的3分钟前的未同步的考勤记录
	 */
	List<Map<String, Object>>getSubmitAttendance(String deviceNo);
}
