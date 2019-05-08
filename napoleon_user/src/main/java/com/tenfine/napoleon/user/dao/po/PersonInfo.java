package com.tenfine.napoleon.user.dao.po;

import com.tenfine.napoleon.framework.annotation.Entity;
import com.tenfine.napoleon.framework.bean.BasePO;

@Entity
public class PersonInfo extends BasePO{

	private static final long serialVersionUID = 2763434103737607585L;
	
	private String name; //昵称
	private String idNo; //身份证号码
	private String sex; //性别
	private String nation; //民族
	private String birthday; //生日
	private String address; //籍贯
	private String provide; //签发机关
	private String period; //有效日期
	private String collectUser; //采集人
	private String collectDevice; //采集设备
	private String notes; //开发人员备注
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProvide() {
		return provide;
	}
	public void setProvide(String provide) {
		this.provide = provide;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getCollectUser() {
		return collectUser;
	}
	public void setCollectUser(String collectUser) {
		this.collectUser = collectUser;
	}
	public String getCollectDevice() {
		return collectDevice;
	}
	public void setCollectDevice(String collectDevice) {
		this.collectDevice = collectDevice;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

	
}
