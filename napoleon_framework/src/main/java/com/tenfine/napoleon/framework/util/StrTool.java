package com.tenfine.napoleon.framework.util;

public class StrTool {
	/**
	 * 比较两个字符串是否相同（若一个为null，另一个为空字符串，视为相同）
	 * @param string1
	 * @param string2
	 * @return
	 */
	public static boolean equals(String string1, String string2) {
		string1 = string1 == null ? "" : string1;
		string2 = string2 == null ? "" : string2;
		return string1.equals(string2);
	}
}

