package com.tenfine.napoleon.smzPlatform.client;

/**
 * @author Hunter
 */
public enum CMD {
	/**
	 * 心跳指令
	 * 用来周期性的验证socket有效性，未启用
	 */
	heartbeat							(65535,"心跳"),

	/**
	 * 登录指令
	 * 平台模拟设备登录至实名制平台
	 */
	login								(823,"登录"),

	/**
	 * 获取项目白名单
	 * 用于获取设备所处的项目的人员名单
	 */
	get_device_info						(824,"获取项目人员名单"),

	/**
	 * 提交考勤数据
	 * 提交考勤数据到实名制平台
	 */
	upload_attendance					(825,"上传不带文件考勤数据"),

	/**
	 * 提交考勤数据
	 * 提交带考勤图片的考勤数据到实名制平台
	 */
	upload_attendance_file				(828,"上传带文件考勤数据"),

	/**
	 * 提交考勤数据
	 * 提交带考勤图片和考勤地点的考勤数据到实名制平台
	 */
	upload_attendance_file_address		(842,"上传带文件和地址考勤数据"),

	/**
	 * 上传采集资料
	 * 提交人员采集资料到实名制平台
	 */
	upload_user							(832,"上传采集资料"),

	/**
	 * 获取设备安装信息
	 * 获取设备的绑定及安装信息，未启用
	 */
	get_device_bind						(838,"获取设备安装位置"),

	/**
	 * 同步非本平台采集的人员的学习资料，未启用
	 */
	get_user_info						(841, "外部人员学习资料");
	
	private Integer code;
	private String name;

	CMD(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
