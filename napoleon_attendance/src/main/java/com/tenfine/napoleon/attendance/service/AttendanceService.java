package com.tenfine.napoleon.attendance.service;

import com.tenfine.napoleon.attendance.dao.po.Apk;
import com.tenfine.napoleon.attendance.dao.po.Attendance;
import com.tenfine.napoleon.framework.bean.Pager;
import com.tenfine.napoleon.user.dao.po.Device;

import java.util.List;

public interface AttendanceService {
	
	/**
	 * 获取有效得apk
	 */
	List<Apk> getValidApk();
	
	/**
	 * 修改Apk信息
	 */
	Apk updateApk(Apk apk);
	
	/**
	 * 校验存在版本号
	 */
	boolean checkHasVersion(String version);
	
	/**
	 * 添加apk包记录
	 */
	void addApk(String fileName, String createTime, long fileSize);
	
	/**
	 * 考勤列表界面数据获取
	 */
	Pager<Attendance> getAttendancePage(int pageSize, int pageNo, String searchKey);
	
	/**
	 * Apk列表界面数据获取
	 */
	Pager<Apk> getApkPage(int pageSize, int pageNo, String searchKey);

	/**
	 * 新增考勤数据
	 */
	String addAttendance(Device user, String idNo, String name, String matchScore, String longitude, String latitude, String position, String convert);
	
	/**
	 * 获取考勤详细
	 */
	Attendance getAttendanceDetail(String id);
	
	/**
	 * 获取APK详细
	 */
	Apk getApkDetail(String apkId);
	
    List<Attendance> getAttendanceList(String begin, String end, List<Device> proDevices);
}
