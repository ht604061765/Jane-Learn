package com.tenfine.napoleon.smzPlatform.message;

public class SendMessageBo {
	private byte[] content; //消息内容
	private int cmd = 0; //指令
	private int version = 1; //版本
	private String session = ""; //消息会话
	private byte flag;

	public SendMessageBo(byte[] conternt, int cmd, String session, byte flag, int version) {
		super();
		this.content = conternt;
		this.cmd = cmd;
		this.session = session;
		this.flag = flag;
		this.version = version;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public byte[] getConternt() {
		return content;
	}

	public void setConternt(byte[] conternt) {
		this.content = conternt;
	}

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

}
