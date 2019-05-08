package com.tenfine.napoleon.framework.util;

public class PathStatic {
	
	private static final String areaCode = PlatfromProperties.getId();

	/**
     * 获取apk保存路径
     * @return
     */
	public static String getApkFilePath() {
		String apkFilePath = "";
        String os = System.getProperty("os.name");
        if(os.contains("Win")){
            apkFilePath = "D:/test-file/apkFile/";
        }else{
        	apkFilePath = "/data0/napoleon/" + areaCode + "/server/file/apkFile/";
        }
        return apkFilePath;
    }
	
	
    /**
     * 获取临时文件路径
     * @return
     */
	public static String getTemporaryFilePath() {
        String os = System.getProperty("os.name");
        if(os.contains("Win")){
            return "D:/test-file/";
        }else{
            return "/data0/napoleon/temporaryFile/";
        }
    }


	/**
	 * 采集照片放在getCollectPath下，按照身份证号的前6位拆分3级目录，防止文件数过多
	 * 身份证照片正面为"idNo+cardFront.bmp"
	 * 身份证照片背面为"idNo+cardBack.bmp"
	 * 采集照片为"idNo+RGB.jpg"
	 * 红外照片为"idNo+IR.jpg"
	 * @return
	 */
	public static String getCollectPath(String idNo){
		String splitCatalog = idNo.substring(0, 2) + "/" + idNo.substring(2, 4) + "/" + idNo.substring(4, 6);
		String collectPath = null;
		String os = System.getProperty("os.name");  
		if(os.contains("Win")){  
			collectPath = "D:/hunter_workspace_napoleon/napoleon/collectFile/" + splitCatalog + "/" + idNo + "/" + idNo;
		}else{
			collectPath = "/data0/napoleon/" + areaCode + "/server/file/collectFile/" + splitCatalog + "/" + idNo + "/" + idNo;
		}
		return collectPath;
	}

	/**
	 * vip图片保存路径
	 * @return
	 */
	public static String getVipPath(String personNo){
		String vipPath = null;
		String os = System.getProperty("os.name");  
		if(os.contains("Win")){  
			vipPath = "D:/hunter_workspace_napoleon/napoleon/vip/" + personNo + ".jpg";
		}else{
			vipPath = "/data0/napoleon/" + areaCode + "/server/file/collectFile/vip/" + personNo + ".jpg";
		}
		return vipPath;
	}
	
	/**
	 * 考勤照片放在getAttendancePath下，按照身份证号的前6位拆分3级目录，防止文件数过多
	 * @return
	 */
	public static String getAttendancePath(String idNo){
		String splitCatalog = idNo.substring(0, 2) + "/" + idNo.substring(2, 4) + "/" + idNo.substring(4, 6) + "/";
		String attendancePath = null;
		String os = System.getProperty("os.name");  
		if(os.contains("Win")){  
			attendancePath = "D:/hunter_workspace_napoleon/napoleon/attendanceFile/" + splitCatalog;
		}else{
			attendancePath = "/data0/napoleon/" + areaCode + "/server/file/attendanceFile/" + splitCatalog;
		}
		return attendancePath;
	}
	
	public static String getLoggerPath(){
		String loggerPath = null;
		String os = System.getProperty("os.name");  
		if(os.contains("Win")){  
			loggerPath = "D:/test-log/" + areaCode + "/";
		}else{
			loggerPath = "/data0/napoleon/" + areaCode + "/server/file/log/";
		}
		return loggerPath;
	}

}
