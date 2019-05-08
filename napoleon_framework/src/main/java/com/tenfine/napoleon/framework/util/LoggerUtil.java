//package com.tenfine.napoleon.framework.util;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class LoggerUtil {
//	// 单例模式“SingleTon”
//	private volatile static LoggerUtil _SINGLETON = null; 
//	// 日志存放路径
//	private final String rootPath = PathStatic.getLoggerPath();
//
//	// 日志初始化的时候启动定时获取配置
//	private LoggerUtil(){
////		loggerConfigRunnable = new LoggerConfigRunnable();
////		loggerConfigThread = new Thread(loggerConfigRunnable);
////		loggerConfigThread.start();
//	}
//	
//// ++++++++++++++++++++++++++++++++ 初始化日志配置 ++++++++++++++++++++++++++++++++
//	/**
//	 *  日志打印配置初始化：
//	 * 	0000：不输出
//	 * 	0001：输出控制台
//	 *  0010：输出日志文件
//	 *  0011：输出控制台及日志文件
//	 **/
//	private String infoPrint = "0000"; //普通信息，不重要的用于提示的信息
//	private String warnPrint = "0011"; //警告信息，用于提示警告
//	private String errorPrint = "0011"; //错误信息，用于输出操作错误信息，
//	private String tracePrint = "0011"; //追踪数据，系统调试启用，正式上线禁用
//	
//	
//// ++++++++++++++++++++++++++++++++ 配置输出方法 ++++++++++++++++++++++++++++++++
//
//	public void trace(final Class<?> typeClass, final String content){
//		this.printLog(typeClass, content, LoggerType.TRACE);
//	}
//	
//	public void info(final Class<?> typeClass, final String content){
//		this.printLog(typeClass, content, LoggerType.INFO);
//	}
//	
//	public void info(final String content){
//		this.printLog(this.getClass(), content, LoggerType.INFO);
//	}
//	
//	public void warn(final Class<?> typeClass, final String content){
//		this.printLog(typeClass, content, LoggerType.WARN);
//	}
//	
//	public void error(final String content){
//		this.printLog(this.getClass(), content, LoggerType.ERROR);
//	}
//	
//	public void error(final Class<?> typeClass, final String content){
//		this.printLog(typeClass, content, LoggerType.ERROR);
//	}
//
//	public void error(final Class<?> typeClass, final Exception e){
//		e.printStackTrace();
//		this.printLog(typeClass, e.getMessage(), LoggerType.ERROR);
//	}
//	
//	private String getClassName(final Class<?> typeClass){
//		if (typeClass == null){
//			return this.getClass().toString();
//		}
//		return typeClass.getName();
//	}
//	
//// ++++++++++++++++++++++++++++++++ 日志输出 ++++++++++++++++++++++++++++++++
//
//	public void printLog(final Class<?> typeClass, final String content, final LoggerType LoggerType){
//		//配置项第四位为是否输出到控制台
//		if (getConfigByteValue(LoggerType, 3) == 1){
//			System.out.println(LoggerType.getMemo().concat(" : ") + DateUtil.getCurrentDatetime().concat(":").concat(content));
//		}
//		//配置项第三位为是否输出到文件
//		if (getConfigByteValue(LoggerType, 2) == 1){
//			try{
//				String lineContent = this.getClassName(typeClass).concat(DateUtil.getCurrentDatetime()).concat(":")
//						.concat(LoggerType.getMemo()).concat(content == null ? "null" : content);
//				String filePath = rootPath.concat(LoggerType.getName()).concat(DateUtil.getCurrentDate().concat(".log"));
//				writeTxt(lineContent, filePath);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//	}
//	
//	/**
//     *  写入Txt文件
//     */  
//	public void writeTxt(final String txtLineContent, final String filePath){
//        try {
//            File file = new File(filePath);
//            checkLogPath(file.getParent());
//        
//        	FileOutputStream writerStream = new FileOutputStream(file, true);
//        	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, "GBK")); // "UTF-8")); 
//        	writer.append(txtLineContent);
//        	writer.append("\r\n");
//        	writer.flush();
//        	writer.close(); 
//		} catch (IOException e) {
//			this.error("日志输出失败：".concat(e.getMessage()).concat("; txtLineContent = ").concat(txtLineContent));			
//			e.printStackTrace();			
//		}
//	}	
//	
//	/**
//	 * 配置路径判断
//	 */
//	public void checkLogPath(final String checkFilePath){
//		File file = new File(checkFilePath);
//		if (!file.exists()){
//			file.mkdirs();
//		}
//	}
//
//	// ++++++++++++++++++++++++++++++++ 定时获取文件配置 ++++++++++++++++++++++++++++++++
//	
//	// 读取配置调度
//	private Thread loggerConfigThread = null;
//	// 读取配置线程
//	private LoggerConfigRunnable loggerConfigRunnable = null;
//	
//	/**
//	 * 获取日志实例
//	 * 懒汉式，第一次调用的时候实例化，双重检查锁
//	 */
//	public synchronized static LoggerUtil getLogger(){
//		if (_SINGLETON == null){
//			synchronized (LoggerUtil.class) {
//				if (_SINGLETON == null){
//					_SINGLETON = new LoggerUtil();
//				}
//			}
//		}
//		return _SINGLETON;
//	}
//
//	/**
//	 * 日志配置线程
//	 */
//	protected class LoggerConfigRunnable  implements Runnable{
//		private Boolean isRunning = false;
//		
//		@SuppressWarnings("static-access")
//		public void run() {
//			if (!isRunning){
//				this.isRunning = true;
//				while(true){
//					loadSystemConfig();
//					try {
//						loggerConfigThread.sleep(1000 * 30);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//	}
//		
//	/**
//	 * 加载配置文件，每30秒会重新加载一次
//	 */
//	private void loadSystemConfig(){
//		try{
//			//log的配置文件路径
//			String configPath = rootPath + "/config/sysConfig.txt";
//			this.info("重新读取Logger配置信息 ， configPath = ".concat(configPath));
//	        try {
//				List<String> logConfigSettingList = readFileByLines(configPath);
//			    // 迭代输出
//		        for (String logConfigSetting : logConfigSettingList)
//		        {
//		        	if (logConfigSetting.length() == 0){
//		        		continue;
//		        	}
//		        	final String configName = logConfigSetting.substring(4);
//		        	final String configValue = logConfigSetting.substring(0, 4);
//		        	if (configName.toUpperCase().equals("INFO")){
//		        		infoPrint = configValue;
//		        	}else if (configName.toUpperCase().equals("WARN")){
//		        		warnPrint = configValue;
//		        	}else if (configName.toUpperCase().equals("ERROR")){
//		        		errorPrint = configValue;
//		        	}else if (configName.toUpperCase().equals("TRACE")){
//		        		tracePrint = configValue;
//		        	}
//		        }
//			} catch (Exception e) {
//				this.error(e.getMessage());
//			}
//		}catch(Exception e){
//			this.error(LoggerUtil.class, e);
//		}
//	}
//	
//	/**
//	 * 获取配置文件中的配置
//	 */
//	private int getConfigByteValue(final LoggerType LoggerType, final int byteIndex){
//		String configValue = "";
//		switch (LoggerType) {
//		case ERROR:
//			configValue = errorPrint;
//			break;
//		case INFO:
//			configValue = infoPrint;
//			break;
//		case TRACE:
//			configValue = tracePrint;
//			break;
//		case  WARN:
//			configValue = warnPrint;
//			break;
//		default:
//			break;
//		}
//		if (StringUtil.isEmpty(configValue)){
//			return 0;
//		}
//		final String[] configValues = configValue.split("");
//		if (configValues.length >= byteIndex + 1){
//			final String byteIndexValue = configValues[byteIndex];
//			try{
//				final int byteValue = Integer.parseInt(byteIndexValue);
//				return byteValue;
//			}catch(Exception e){
//				e.printStackTrace();
//				return -1;
//			}
//		}
//		return 0;
//	}
//	
//	/**
//	 * 按行读取配置
//	 * @param fileName：配置文件路径
//	 */
//	public List<String> readFileByLines(String fileName) {
//		List<String> dataLines = new ArrayList<String>();
//		//配置文件
//		File file = new File(fileName);
//		BufferedReader reader = null;// 创建缓存读取
//		try {
////			this.info("以行为单位，读取日志配置文件。。。");
//			reader = new BufferedReader(new FileReader(file));// 将文件放在缓存读取中
//			String tempString = null;
//			int line = 1;
//			// 一次读入一行，直到读入null为文件结束
//			while ((tempString = reader.readLine()) != null) {
//				// 显示行号
//				this.info("line " + line + ": " + tempString);
//				dataLines.add(tempString);
//				line++;
//			}
//			reader.close();
//		} catch (IOException e) {// 捕获异常
//			e.printStackTrace();
//		} finally {// 内容总执行
//			if (reader != null) {
//				try {
//					reader.close();// 关闭缓存读取
//				} catch (IOException e1) {
//				}
//			}
//		}
//		return dataLines;
//	}
//
//	public static void main(String[] args) {
//		String aaa = LoggerUtil.getLogger().getClassName(null);
//		System.out.println(aaa);
////		LoggerUtil.getLogger().info("test info log");
//	}
//}
