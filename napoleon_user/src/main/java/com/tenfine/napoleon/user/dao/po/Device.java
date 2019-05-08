package com.tenfine.napoleon.user.dao.po;

import com.tenfine.napoleon.framework.annotation.Entity;
import com.tenfine.napoleon.framework.bean.BasePO;

@Entity
public class Device extends BasePO{

	private static final long serialVersionUID = 5926367228093227156L;
	
	private String name; //设备昵称
	private String phone; //手机号码
	private String deviceNo; //设备编号
	private String account; //设备账号
	private String pwd; //密码
	private String type; //设备类型(保留字段)
	private String state; //状态，0：初始状态，1：启用，2：设备换绑
	private String notes; //开发人员备注
	private String token; //用户令牌
	private String platformProId; //用户所属项目Id
	private String platformProName; //用户所属项目名称
	private String platformId; //用户所属平台ID
	private String platformName; //用户所属平台名称
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public String getPlatformId() {
		return platformId;
	}
	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getPlatformProName() {
		return platformProName;
	}
	public void setPlatformProName(String platformProName) {
		this.platformProName = platformProName;
	}
	public String getPlatformProId() {
		return platformProId;
	}
	public void setPlatformProId(String platformProId) {
		this.platformProId = platformProId;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

}
