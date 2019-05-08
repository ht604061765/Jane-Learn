package com.tenfine.napoleon.attendance.dao.po;

import com.tenfine.napoleon.framework.annotation.Entity;
import com.tenfine.napoleon.framework.bean.BasePO;

@Entity
public class Apk extends BasePO{

	private static final long serialVersionUID = -2863967015023014384L;
	
	private String name; //Apk文件名称，由上传文件确定
	private String version; //Apk版本号，由运维人员指定
	private String fileSize; //Apk文件大小
	private String state; //Apk状态，0：禁用，1：启用
	private String summary; //Apk描述
	private String notes; //开发人员备注
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
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
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}

	
}
