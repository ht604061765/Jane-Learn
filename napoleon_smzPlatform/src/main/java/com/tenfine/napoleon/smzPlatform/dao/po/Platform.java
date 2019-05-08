package com.tenfine.napoleon.smzPlatform.dao.po;

import com.tenfine.napoleon.framework.annotation.Entity;
import com.tenfine.napoleon.framework.bean.BasePO;

@Entity
public class Platform extends BasePO{

	private static final long serialVersionUID = 8236468206231591741L;
	
	private String name; //用户昵称
	private String secret; //平台得密钥
	private String socketServer; //socket连接用得服务地址
	private String socketPort; //socket连接用得服务端口
	private String state; //状态
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getSocketServer() {
		return socketServer;
	}
	public void setSocketServer(String socketServer) {
		this.socketServer = socketServer;
	}

	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSocketPort() {
		return socketPort;
	}
	public void setSocketPort(String socketPort) {
		this.socketPort = socketPort;
	}




}
