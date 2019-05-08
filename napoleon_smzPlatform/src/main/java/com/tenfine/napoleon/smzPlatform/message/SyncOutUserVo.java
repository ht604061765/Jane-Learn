package com.tenfine.napoleon.smzPlatform.message;

public class SyncOutUserVo {

	private String userName; //用户名称
	private String idNumber; //身份证号
	private Integer nation; //名族
	private String sex; //性别
	private String idCardAddress; //身份证地址
	private String birthday; //生日
	private String certificationAuthority; //发证机关
	private String validity; //有效期
	private Integer projectId; //项目编号
	private Integer teamId; //班组编号
	private Integer workTypeId; //工种类别编号
	private Integer collectionImgLong; //采集照片长度
	private byte[] collectionImg; //采集照片
	private Integer idCardImgLong; //身份证照片长度
	private byte[] idCardImg; //身份证照片
	private Integer infraRedImgLong; //红外照片长度
	private byte[] infraRedImg; //红外照片
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public Integer getNation() {
		return nation;
	}
	public void setNation(Integer nation) {
		this.nation = nation;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIdCardAddress() {
		return idCardAddress;
	}
	public void setIdCardAddress(String idCardAddress) {
		this.idCardAddress = idCardAddress;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getCertificationAuthority() {
		return certificationAuthority;
	}
	public void setCertificationAuthority(String certificationAuthority) {
		this.certificationAuthority = certificationAuthority;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public Integer getTeamId() {
		return teamId;
	}
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	public Integer getWorkTypeId() {
		return workTypeId;
	}
	public void setWorkTypeId(Integer workTypeId) {
		this.workTypeId = workTypeId;
	}
	public Integer getCollectionImgLong() {
		return collectionImgLong;
	}
	public void setCollectionImgLong(Integer collectionImgLong) {
		this.collectionImgLong = collectionImgLong;
	}
	public byte[] getCollectionImg() {
		return collectionImg;
	}
	public void setCollectionImg(byte[] collectionImg) {
		this.collectionImg = collectionImg;
	}
	public Integer getIdCardImgLong() {
		return idCardImgLong;
	}
	public void setIdCardImgLong(Integer idCardImgLong) {
		this.idCardImgLong = idCardImgLong;
	}
	public byte[] getIdCardImg() {
		return idCardImg;
	}
	public void setIdCardImg(byte[] idCardImg) {
		this.idCardImg = idCardImg;
	}
	public Integer getInfraRedImgLong() {
		return infraRedImgLong;
	}
	public void setInfraRedImgLong(Integer infraRedImgLong) {
		this.infraRedImgLong = infraRedImgLong;
	}
	public byte[] getInfraRedImg() {
		return infraRedImg;
	}
	public void setInfraRedImg(byte[] infraRedImg) {
		this.infraRedImg = infraRedImg;
	}

	


}
