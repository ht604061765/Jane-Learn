package com.tenfine.napoleon.framework.util;

import java.util.HashMap;
import java.util.Map;

public class NationUtils {
	private static Map<String, String> nationMap = null;
	 private final static String[] nationcode = {"01", "02", "03", "04", "05", "06", "07", "08",
	            "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
	            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
	            "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41",
	            "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52",
	            "53", "54", "55", "56", "57", "58"};
	 private final static String[] nationname = {"汉族", "蒙古族", "回族", "藏族", "维吾尔族", "苗族", "彝族", "壮族",
	            "布依族", "朝鲜族", "满族", "侗族", "瑶族", "土家族", "白族", "哈尼族", "哈萨克族", "傣族",
	            "黎族", "傈傈族", "佤族", "畲族", "高山族", "拉祜族", "水族", "东乡族", "纳西族", "景颇族",
	            "柯尔克孜族", "土族", "达斡尔族", "仫佬族", "羌族", "布朗族", "撒拉族", "毛难族", "仡佬族",
	            "锡伯族", "阿昌族", "普米族", "塔吉克族", "怒族", "乌孜别克族", "俄罗斯族", "鄂温克族", "崩龙族",
	            "保安族", "裕固族", "京族", "塔塔尔族", "独龙族", "鄂伦春族", "赫哲族", "门巴族", "珞巴族",
	            "基诺族", "其他", "外国血统"};

	 public static String getCode(String name){
		 if(nationMap == null){
			 nationMap = new HashMap<String, String>();
			 for (int i = 0; i < nationcode.length; i++) {
				String string = nationcode[i];
				String nationnames = nationname[i];
				nationMap.put(nationnames, string);
				
			}
		 }
		 if(name.indexOf("外国血统") != -1){  //存在一些民族是: 外国血统中国籍人士
			 return nationMap.get("外国血统");
		 }
		 return nationMap.get(name);
	 }
	 public static String getName(String code){
		 if(nationMap == null){
			 nationMap = new HashMap<String, String>();
			 for (int i = 0; i < nationcode.length; i++) {
				String nationCode = nationcode[i];
				String nationName = nationname[i];
				nationMap.put(nationCode, nationName);
				
			}
		 }
		 return nationMap.get(code);
	 }
}
