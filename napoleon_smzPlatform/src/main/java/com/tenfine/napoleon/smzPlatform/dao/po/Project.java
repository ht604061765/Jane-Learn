package com.tenfine.napoleon.smzPlatform.dao.po;

import com.tenfine.napoleon.framework.annotation.Entity;
import com.tenfine.napoleon.framework.bean.BasePO;

@Entity
public class Project extends BasePO{

	private static final long serialVersionUID = 8963260435317930360L;
	
	private String name; //项目名称
	private String short_name; //项目简称
	private String platformId; //所属平台Id
	private String platformProjectId; //对应得平台项目得Id
	private String areaId; //地区Id
	private String address; //地址
	private String state; //状态
	private String syncTime; //同步时间
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShort_name() {
		return short_name;
	}
	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}
	public String getPlatformId() {
		return platformId;
	}
	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}
	public String getPlatformProjectId() {
		return platformProjectId;
	}
	public void setPlatformProjectId(String platformProjectId) {
		this.platformProjectId = platformProjectId;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
