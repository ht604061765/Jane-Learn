package com.tenfine.napoleon.smzPlatform.message;

public class UploadUserVo {

	private String idNo;
	private String name;
	private int sex;
	private int platformTeamId;
	private String nation;
	private String birthday;
	private String address; //地址
	private String provide; //签发机关
	private String validity;// 有效期
	private String idCardImgPath;
	private String collectPath;
	
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getPlatformTeamId() {
		return platformTeamId;
	}
	public void setPlatformTeamId(int platformTeamId) {
		this.platformTeamId = platformTeamId;
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
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public String getIdCardImgPath() {
		return idCardImgPath;
	}
	public void setIdCardImgPath(String idCardImgPath) {
		this.idCardImgPath = idCardImgPath;
	}
	public String getCollectPath() {
		return collectPath;
	}
	public void setCollectPath(String collectPath) {
		this.collectPath = collectPath;
	}
	
	
}
