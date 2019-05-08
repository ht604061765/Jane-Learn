package com.tenfine.napoleon.smzPlatform.message;

import java.io.Serializable;

public class ReceiveMessageBo implements Serializable {
	private static final long serialVersionUID = 1L;

	private byte[] content;// 16位md5密钥
	private String session = "";
	private int cmd = 0;
	private byte flag;

	public boolean success() {
		return flag == 0x00;
	}

	public ReceiveMessageBo(byte[] content, String session, int cmd, byte flag) {
		super();
		this.content = content;
		this.session = session;
		this.cmd = cmd;
		this.flag = flag;
	}

	public static ReceiveMessageBo createMsg(byte[] content, String session, int cmd, byte flag) {
		return new ReceiveMessageBo(content, session, cmd, flag);
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

}
