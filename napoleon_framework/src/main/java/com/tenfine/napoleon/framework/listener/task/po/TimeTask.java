package com.tenfine.napoleon.framework.listener.task.po;

import com.tenfine.napoleon.framework.annotation.Entity;
import com.tenfine.napoleon.framework.bean.BasePO;

@Entity
public class TimeTask extends BasePO{

	private static final long serialVersionUID = -4372339493439802533L;
	
	private String code;	//任务编码
	private String name;	//任务名称
	private String rule;	//执行规则
	private String imp;		//实现类
	private String isopen;		//是否启用
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getImp() {
		return imp;
	}
	public void setImp(String imp) {
		this.imp = imp;
	}
	public String getIsopen() {
		return isopen;
	}
	public void setIsopen(String isOpen) {
		this.isopen = isOpen;
	}
}
