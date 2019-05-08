package com.tenfine.napoleon.framework.util;

public class PlatfromProperties{
	
	private static String id; //平台Id
	private static String name; //平台名称
	private static String socketServer; //socket连接用得服务地址
	private static int socketPort; //socket连接用得服务端口
	private static String attendanceCmd; //考勤指令
	
	public static String getId() {
		return id;
	}
	public static void setId(String id) {
		PlatfromProperties.id = id;
	}
	public static String getName() {
		return name;
	}
	public static void setName(String name) {
		PlatfromProperties.name = name;
	}
	public static String getSocketServer() {
		return socketServer;
	}
	public static void setSocketServer(String socketServer) {
		PlatfromProperties.socketServer = socketServer;
	}
	public static int getSocketPort() {
		return socketPort;
	}
	public static void setSocketPort(int socketPort) {
		PlatfromProperties.socketPort = socketPort;
	}
	public static String getAttendanceCmd() {
		return attendanceCmd;
	}
	public static void setAttendanceCmd(String attendanceCmd) {
		PlatfromProperties.attendanceCmd = attendanceCmd;
	}
	
}
