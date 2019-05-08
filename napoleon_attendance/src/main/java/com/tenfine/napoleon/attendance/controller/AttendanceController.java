package com.tenfine.napoleon.attendance.controller;

import com.tenfine.napoleon.attendance.dao.po.Apk;
import com.tenfine.napoleon.attendance.dao.po.Attendance;
import com.tenfine.napoleon.attendance.service.AttendanceService;
import com.tenfine.napoleon.faceutils.AttendanceRecognition;
import com.tenfine.napoleon.faceutils.CollectRecognition;
import com.tenfine.napoleon.framework.bean.BaseController;
import com.tenfine.napoleon.framework.bean.Pager;
import com.tenfine.napoleon.framework.bean.Result;
import com.tenfine.napoleon.framework.util.*;
import com.tenfine.napoleon.user.dao.po.Device;
import com.tenfine.napoleon.user.dao.po.PersonInfo;
import com.tenfine.napoleon.user.dao.po.PersonVip;
import com.tenfine.napoleon.user.dao.po.ProjectPerson;
import com.tenfine.napoleon.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Controller
public class AttendanceController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);
    
    private final AttendanceService attendanceService;
    private final UserService userService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService, UserService userService) {
        this.attendanceService = attendanceService;
        this.userService = userService;
    }
    
	@RequestMapping("/editApkSubmit")
    @ResponseBody
    public Result addDeviceSubmit(Apk apk) {
	    if (attendanceService.checkHasVersion(apk.getVersion())) {
	        return Result.error("设备版本号已存在");
        }
	    Apk newApk = attendanceService.updateApk(apk);
		return Result.success(newApk);
    }
    
    @RequestMapping("/uploadApk")
    @ResponseBody
    public Result<String> uploadApk(HttpServletResponse response, MultipartFile packageFile){
    	if (packageFile.isEmpty()) {
            return Result.error("上传失败，请选择文件");
        }
    	String fileName = packageFile.getOriginalFilename();
    	String createTime = DateUtil.getCurrentDatetime();
    	boolean isSave = FileUtil.saveApkFile(packageFile, createTime);
    	if(isSave) {
    		// 如果文件保存成功，则写入数据
    		attendanceService.addApk(fileName, createTime, packageFile.getSize());
    		
    	}else {
    		return Result.error("文件写入失败");
    	}
		return Result.success("APK包上传成功");
    }
    
    @RequestMapping("/api/downloadVipImg")
    public void downloadVipImg(HttpServletResponse response, String syncTime) throws Exception { //syncTime: yyyy-MM-dd HH:mm:ss
    	// 理论上来讲这里应该是做校验的，后续再优化吧
//    	Date syncDate = DateUtil.parseDatetime(syncTime);
//    	Long timeStamp = syncDate.getTime();
//    	Result<String> checkToken = checkToken(token, timeStamp, random);
    	Result<String> checkToken = Result.success();
    	if(checkToken.isSuccess()) {
    		List<PersonVip> vipList = userService.getVipFace(syncTime);
    		if(vipList.size() > 0) {
				// 文件下载类型--二进制文件
    			response.setContentType("application/octet-stream");
				// 设置文件名
                response.addHeader("Content-Disposition", "attachment;fileName=VipFace.zip");
    			
    			List<String> paths = new ArrayList<>();
    			for (int i = 0; i < vipList.size(); i++) {
    				PersonVip vip = vipList.get(i);
	    			String facePath = PathStatic.getVipPath(vip.getPersonNo());
	    			paths.add(facePath);
				}
    			OutputStream out  = response.getOutputStream();
    			FileUtil.toZipReturn(out, paths);
    		}
    	}
	} 
    
    @RequestMapping("/api/downloadApk")
    public void downloadApk(HttpServletResponse response, String token, long timeStamp, String random) throws IOException {
    	Result<String> checkToken = checkToken(token, timeStamp, random);
    	if(!checkToken.isSuccess()) {
    		return;
    	}
    	List<Apk> apkList = attendanceService.getValidApk();
    	if(apkList.size() ==1) {
    		Apk apk = apkList.get(0);
        	String YYYYMMDDHHMMSS = apk.getCreateTime().replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
        	String filePath = PathStatic.getApkFilePath() + YYYYMMDDHHMMSS + "/" + apk.getName();
    		File file = new File(filePath);
    		if (file.exists()) {
    			response.reset();
				// 文件下载类型--二进制文件
            	response.setContentType("application/octet-stream");
            	// 设置文件名
                response.addHeader("Content-Disposition", "attachment;fileName=" + apk.getName());
                // 对面需要文件大小
                byte[] fileByte = ByteUtil.file2byte(file);
                response.addIntHeader("fileSize", fileByte.length);
                
                FileInputStream in = new FileInputStream(file);
                OutputStream os = response.getOutputStream();
                byte[] b = new byte[1024];
                while (in.read(b) != -1) {
                    os.write(b);
                }
                in.close();
                os.flush();
                os.close();
            }else {
            	logger.error("对应的文件不存在，filePath=" + filePath);
            }
    	}else {
        	logger.error("没有有效的Apk包");
    	}
    }
    
    private Result<String> checkToken(String token, long timeStamp, String random){
        String ticket = "76EC56823FFD90E56C3BFB7ACEED84DE";
        String[] tokenObj = new String[3];
    	tokenObj[0] = String.valueOf(timeStamp);
    	tokenObj[1] = random;
    	tokenObj[2] = ticket;
    	String encodeString = Arrays.toString(tokenObj);
    	String myToken = EncryptUtil.md5Encode(encodeString);
    	if(myToken.equals(token)) {
    		return Result.success("校验通过");
    	}
		return Result.error("校验不通过");
    }
    
    /**
     * 设备更新
     */
    @RequestMapping("/api/deviceUpdate")
    @ResponseBody
    public Result deviceUpdate(HttpServletRequest request, String token, long timeStamp, String random) {
    	Result<String> checkToken = checkToken(token, timeStamp, random);
    	if(!checkToken.isSuccess()) {
    		return checkToken;
    	}
    	List<Apk> apkList = attendanceService.getValidApk();
    	if(apkList.size() ==1) {
    		Apk apk = apkList.get(0);
    		String yyyyMmDdHhMmSs = apk.getCreateTime().replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
    		String filePath = PathStatic.getApkFilePath() + yyyyMmDdHhMmSs + "/" + apk.getName();
    		File file = new File(filePath);
    		if (file.exists()) {
    	    	Map<String, Object> rtnMap = new HashMap<>();
	        	rtnMap.put("curVersion", apk.getVersion());
	        	rtnMap.put("downloadHref", "192.168.41.10:10100/downloadApk");
	        	return Result.success(rtnMap);
            }else {
            	return Result.error("对应的Apk文件不存在，filePath=" + filePath);
            }
    	}else {
        	return Result.error("获取Apk对象出错");
    	}
    }
    
    @RequestMapping("/attendanceMap")
    public String attendanceMap() {
        return "attendanceMap";
    }

	/**
	 * 地图用到得
	 * @return
	 */
    @RequestMapping("/attendanceList")
    @ResponseBody
    public Result<?> attendanceList(String startDate, String endDate, String proId) {
        List<Device> proDevices = new ArrayList<>();
        if (!"0".equals(proId)){
            proDevices = userService.getProDevice(proId);
        }
        List<Attendance> attendanceList = attendanceService.getAttendanceList(startDate, endDate, proDevices);
        return Result.success(attendanceList);
    }

    
    @RequestMapping("/getAttendanceList")
	@ResponseBody
    public String getAttendanceList(HttpServletRequest request, int pageSize, int offset ,String searchKey) {
		int pageNo = offset/pageSize + 1;
		Pager<Attendance> attendancePage = attendanceService.getAttendancePage(pageSize, pageNo, searchKey);
		List<Attendance> attendanceList = attendancePage.getRecordList();
		Map<String, Object> rtnData = new HashMap<>();
		rtnData.put("total", attendancePage.getTotalRecord());
		rtnData.put("rows", attendanceList);
		return JsonUtil.toJSON(rtnData);
    }
    
    @RequestMapping("/getApkList")
	@ResponseBody
    public String getApkList(HttpServletRequest request, int pageSize, int offset ,String searchKey) {
		int pageNo = offset/pageSize + 1;
		Pager<Apk> apkPage = attendanceService.getApkPage(pageSize, pageNo, searchKey);
		List<Apk> apkList = apkPage.getRecordList();
		Map<String, Object> rtnData = new HashMap<>();
		rtnData.put("total", apkPage.getTotalRecord());
		rtnData.put("rows", apkList);
		return JsonUtil.toJSON(rtnData);
    }
    
    @RequestMapping("/api/collectVip")
	@ResponseBody
    public Result<String> collectVip(MultipartFile file, String token, long timeStamp, String random) throws Exception {
		if(file.isEmpty()) {
			return Result.error("图片文件为空");
		}
		Result<String> checkToken = checkToken(token, timeStamp, random);
    	if(!checkToken.isSuccess()) {
    		return Result.error("token校验失败");
    	}
    	//构造业务参数
    	String uploadTime = DateUtil.formatDatetime(timeStamp); // 上传时间,YYYY-MM-DD HH:MM:SS
    	String personNo = DateUtil.conversionTimestamp(String.valueOf(timeStamp)); // 注册到百度的人员编号,YYYYMMDDHHMMSS
    	String groupId = "vip_" + PlatfromProperties.getId(); //vip_hengqin
    	Result<String> collectResult = null;
    	// 人脸检测及注册
		collectResult = CollectRecognition.VipCollect(groupId, personNo, file);
        if(collectResult.isSuccess()) {
            userService.addVipPerson(uploadTime, personNo);
            FileUtil.saveVipFile(personNo, file); 
        }
		return collectResult;
	}
    
    
	@RequestMapping("/api/getCollect")
	@ResponseBody
    public Result getCollect(String ver, String token, int collectType, MultipartFile file, String idNo) throws IOException {
		if(file.isEmpty() && idNo.isEmpty()) {
			return Result.error("参数为空，请检查后重试，文件长度=" + file.getSize() +",idNo=" + idNo);
		}
        List<Device> deviceList = userService.getDeviceByToken(token);
		if(deviceList.size() != 1) {
			return Result.error("token错误，未获取到正确的用户");
		}
		Device device = deviceList.get(0);
		// 校验设备状态
		Result check = userService.checkDeviceState(device);
		if(!check.isSuccess()){
			return check;
		}
        if (collectType == 1) { // 第一次上传，身份证正面
        	Result collectResult = CollectRecognition.cerfNumFront(file.getBytes());
        	if(collectResult.isSuccess()) {
        		Map<String, String> rtnMap = (Map<String, String>) collectResult.getData();
            	idNo = rtnMap.get("idNo");
            	FileUtil.saveCollectFile(1, idNo, file);
        	}
            return collectResult;
        } else if (collectType == 2) { // 第二次上传，身份证反面
        	Result collectResult =CollectRecognition.cerfNumBack(file.getBytes(), idNo);
        	if(collectResult.isSuccess()) {
            	FileUtil.saveCollectFile(2, idNo, file);
        	}
            return collectResult;
        } else if (collectType == 3){ // 第三次上传，采集图片
        	//进行业务操作
        	String groupId = device.getPlatformId() + "_" + device.getPlatformProId();
        	// 如果图片分辨率不是640 * 480，等比例放大，缩小图片的分辨率
            File resizeFile = CollectRecognition.resizeImage(file.getBytes(), idNo);
        	// 调用百度识别，拿到返回结果
        	Result collectResult = CollectRecognition.workCollect(groupId, idNo, resizeFile);
        	if(collectResult.isSuccess()) {
	            try {
	        		PersonInfo personInfo = (PersonInfo) collectResult.getData();
	            	//采集新增人员
	        		Result<String> addDataResult = userService.addCollectPerson(device, personInfo);
	        		if(!addDataResult.isSuccess()) {
	        			return addDataResult;
	        		}
	                FileUtil.saveResizeFile(idNo, resizeFile); // 保存修改图片分辨率后的采集图片
				} catch (Exception e) {
					logger.error("采集数据写入出错");
					e.printStackTrace();
					return Result.error("采集数据写入出错");
				} finally {
		        	resizeFile.delete(); // 把转换分辨率的临时图片删除
				}
        	}
            return collectResult;
        } else {
            return Result.error("采集类型参数：collectType错误");
        }
	}

    @RequestMapping("/api/getAttendance")
    @ResponseBody
    public Result getAttendance(String ver, String token, MultipartFile file, String longitude, String latitude, String convert) throws IOException {
    	//longitude：经度，latitude：纬度
    	List<Device> deviceList = userService.getDeviceByToken(token);
		if(deviceList.size() != 1) {
			return Result.error("token错误，未获取到正确的用户对象");
		}
		Device device = deviceList.get(0);
		// 校验设备状态
		Result check = userService.checkDeviceState(device);
		if(!check.isSuccess()){
			return check;
		}
    	String groupId = device.getPlatformId() + "_" + device.getPlatformProId();
		Result addendanceResult = AttendanceRecognition.workerAttendance(file.getBytes(), groupId);
		if(addendanceResult.isSuccess()) {
			// 百度算法反馈
			Map<String, Object> attendanceRtn = (Map<String, Object>) addendanceResult.getData();
			String idNo = (String) attendanceRtn.get("idNo");
			
			ProjectPerson projectPerson = userService.getProjectPerson(device.getPlatformProId(), idNo);
			if(projectPerson != null) {
				if("2".equals(projectPerson.getState())) {
					return Result.error("人员离职，不能考勤");
				}
			}
			
			String matchScore = (String) attendanceRtn.get("score");
			String position = (String) attendanceRtn.get("position");
			// 人员详细信息
			PersonInfo personInfo = userService.getPersonByIdNo(idNo);
			// 添加考勤数据
			String timeStamp = attendanceService.addAttendance(device, idNo, personInfo.getName(), matchScore, longitude, latitude, position, convert);
			// 根据考勤时间戳存考勤图片
			FileUtil.saveAttendanceFile(timeStamp, idNo, file);
			// 在返回值中加入参数：personName
			attendanceRtn.put("personName", personInfo.getName());
			Result<Map<String, Object>> rtnResult = Result.success(attendanceRtn);
			return rtnResult;
		}
		
        return addendanceResult;
    }

    /* ============================== 视图 ============================== */
    
    /**
     * 考勤详情模态框
     */
	@RequestMapping("/modelAttendanceDetails")
    public ModelAndView modelAttendanceDetails(HttpServletRequest request, String attendanceId) {
		ModelAndView mv = new ModelAndView("modelAttendanceDetails");
		Attendance attendance = attendanceService.getAttendanceDetail(attendanceId);
		mv.addObject("attendance", attendance);
		return mv;
    }
	
    /**
     * 考勤详情模态框
     */
	@RequestMapping("/modelApkDetails")
    public ModelAndView modelApkDetails(HttpServletRequest request, String apkId) {
		ModelAndView mv = new ModelAndView("modelApkDetails");
		Apk apk = attendanceService.getApkDetail(apkId);
		mv.addObject("apk", apk);
		return mv;
    }
	
	
    @RequestMapping("/apkListPage")
    public String apkListPage() {
        return "apkListPage";
    }
	
    @RequestMapping("/attendanceListPage")
    public String attendanceListPage() {
        return "attendanceListPage";
    }

    @RequestMapping("/mapSocket")
    public String mapSocket() {

        return "mapSocket";
    }

}
