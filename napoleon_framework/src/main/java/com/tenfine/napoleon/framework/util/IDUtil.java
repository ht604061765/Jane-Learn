package com.tenfine.napoleon.framework.util;

import java.util.UUID;

public class IDUtil {

	/**
	 * 32位字符串，数据库主键生成
	 * @return
	 */
	public static String getUUID(){
		String s = UUID.randomUUID().toString(); 
        return s.replace("-", "").toLowerCase(); 
	}
	
	/**
	 * 16位字符串，会话生成
	 * 应急方案，后续需要优化
	 * @return
	 */
	public static String getUUID16(){
		String s = getUUID();
        return s.substring(15, 31);
	}
	
	/**
	 * 14位随机字符串，设备编码生成
	 * 应急方案，后续需要优化
	 * @return
	 */
	public static String getUUID14(){
		String s = getUUID();
        return s.substring(17, 31);
	}
	
	public static void main(String[] args) {
		String aaa = getUUID16();
		System.out.println(aaa);
	}
}
  
