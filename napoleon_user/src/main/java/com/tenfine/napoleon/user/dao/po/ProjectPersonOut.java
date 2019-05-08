package com.tenfine.napoleon.user.dao.po;

import com.tenfine.napoleon.framework.annotation.Entity;
import com.tenfine.napoleon.framework.bean.BasePO;

@Entity
public class ProjectPersonOut extends BasePO{

	private static final long serialVersionUID = -7592571454406052996L;

	private String idNo; //身份证号码
	private String platformId; //所属平台Id
	private String platformName; //所属项目Id
	private String platformProId; //所属平台项目Id
	private String platformProName; //所属平台项目名称
	private String platformWorkerId; //平台工人Id
	private String platformWorkerName; //平台工人名称
	private String state; //同步状态，1：已同步学习资料，0：未同步学习资料
	private String syncTime; // 同步时间
	
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getPlatformId() {
		return platformId;
	}
	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public String getPlatformProId() {
		return platformProId;
	}
	public void setPlatformProId(String platformProId) {
		this.platformProId = platformProId;
	}
	public String getPlatformProName() {
		return platformProName;
	}
	public void setPlatformProName(String platformProName) {
		this.platformProName = platformProName;
	}
	public String getPlatformWorkerId() {
		return platformWorkerId;
	}
	public void setPlatformWorkerId(String platformWorkerId) {
		this.platformWorkerId = platformWorkerId;
	}
	public String getPlatformWorkerName() {
		return platformWorkerName;
	}
	public void setPlatformWorkerName(String platformWorkerName) {
		this.platformWorkerName = platformWorkerName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSyncTime() {
		return syncTime;
	}
	public void setSyncTime(String syncTime) {
		this.syncTime = syncTime;
	}
	
}
