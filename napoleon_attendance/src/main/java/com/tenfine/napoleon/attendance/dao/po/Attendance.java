package com.tenfine.napoleon.attendance.dao.po;

import com.tenfine.napoleon.framework.annotation.Entity;
import com.tenfine.napoleon.framework.bean.BasePO;

@Entity
public class Attendance extends BasePO{

	private static final long serialVersionUID = 4984120827520138830L;
	
	private String workerNo; //平台工人编号
	private String personIdNo; //人员身份证号
	private String personName; //人员名称
	private String deviceNo; //考勤设备号
	private String state; //推送状态，1：是，0：否
	private String timeStamp; //时间戳
	private String attendancePlace; //考勤地点
	private String pushTime; //推送次数
	private String matchScore; //匹配分数
	private String longitude; //经度
	private String latitude; //纬度
	

	public String getPersonIdNo() {
		return personIdNo;
	}
	public void setPersonIdNo(String personIdNo) {
		this.personIdNo = personIdNo;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getAttendancePlace() {
		return attendancePlace;
	}
	public void setAttendancePlace(String attendancePlace) {
		this.attendancePlace = attendancePlace;
	}

	public String getWorkerNo() {
		return workerNo;
	}
	public void setWorkerNo(String workerNo) {
		this.workerNo = workerNo;
	}
	public String getMatchScore() {
		return matchScore;
	}
	public void setMatchScore(String matchScore) {
		this.matchScore = matchScore;
	}
	public String getPushTime() {
		return pushTime;
	}
	public void setPushTime(String pushTime) {
		this.pushTime = pushTime;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

}
