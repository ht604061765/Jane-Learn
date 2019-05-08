package com.tenfine.napoleon.framework.util;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 采用MD5加密
 */
public class EncryptUtil {
	private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

	/***
	 * MD5加密 生成32位md5码
	 * 
	 * @param 待加密字符串
	 * @return 返回32位md5码
	 */
	public static String md5Encode(String inStr) {
		StringBuffer hexValue = new StringBuffer();
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] byteArray = inStr.getBytes("UTF-8");
			byte[] md5Bytes = md5.digest(byteArray);

			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
		} catch (Exception e) {
			logger.error("MD5加密错误！", e);
			throw new RuntimeException(e);
		}

		return hexValue.toString();
	}

	/***
	 * SHA加密 生成40位SHA码
	 * 
	 * @param 待加密字符串
	 * @return 返回40位SHA码
	 */
	public static String shaEncode(String inStr) {
		StringBuffer hexValue = new StringBuffer();
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA");
			byte[] byteArray = inStr.getBytes("UTF-8");
			byte[] md5Bytes = sha.digest(byteArray);

			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
		} catch (Exception e) {
			logger.error("SHA加密错误！", e);
			throw new RuntimeException(e);
		}

		return hexValue.toString();
	}

	/**
	 * 测试主函数
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		String uuid = IDUtil.getUUID();
		String md5Str = md5Encode(uuid);
		System.out.println(md5Str);
	}
}
