package com.tenfine.napoleon.attendance.service.impl;

import com.tenfine.napoleon.attendance.dao.AttendanceDao;
import com.tenfine.napoleon.attendance.dao.po.Apk;
import com.tenfine.napoleon.attendance.dao.po.Attendance;
import com.tenfine.napoleon.attendance.service.AttendanceService;
import com.tenfine.napoleon.framework.WebSocketServer;
import com.tenfine.napoleon.framework.bean.BaseService;
import com.tenfine.napoleon.framework.bean.POCondition;
import com.tenfine.napoleon.framework.bean.Pager;
import com.tenfine.napoleon.framework.util.AddressUtil;
import com.tenfine.napoleon.framework.util.DateUtil;
import com.tenfine.napoleon.framework.util.IDUtil;
import com.tenfine.napoleon.framework.util.JsonUtil;
import com.tenfine.napoleon.user.dao.po.Device;
import com.tenfine.napoleon.user.dao.po.ProjectPerson;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("MemberService")
public class AttendanceServiceImpl extends BaseService implements AttendanceService{

	private final AttendanceDao dao;
	
	@Autowired
	public AttendanceServiceImpl(AttendanceDao dao) {
		this.dao = dao;
	}
	
    /**
     * 获取当天的考勤数据的经纬度
     */
    @Override
    public List<Attendance> getAttendanceList(String begin, String end, List<Device> proDevices) {
    	POCondition condition = new POCondition();
    	if (proDevices.size() > 0){
    		String[] deviceNos = new String[proDevices.size()];
    		for (int i=0, n=proDevices.size(); i<n; i++){
				deviceNos[i] = proDevices.get(i).getDeviceNo();
			}
			condition.addIn("deviceNo", deviceNos);
		}
		condition.addGE("createTime",begin);
    	// 如果开始时间与结束时间相等，则只加入开始时间
    	if (!begin.equals(end)){
			condition.addLE("createTime",end);
		}
    	condition.addOrderDesc("createTime");
        List<Attendance> attendanceList = dao.findPoList(Attendance.class, condition);
        if (null != attendanceList && attendanceList.size() > 0) {
            return attendanceList;
        }
        return null;
    }


	@Override
	public String addAttendance(Device device, String idNo, String name, String matchScore, String longitude, String latitude, String position, String convert) {
		String workerNo = "0";
		if(device.getPlatformProId() != null) { //如果用户已经绑定项目
			POCondition condition = new POCondition();
			condition.addEQ("platformId", device.getPlatformId());
			condition.addEQ("platformProId", device.getPlatformProId());
			condition.addEQ("idNo", idNo);
			List<ProjectPerson> projectPersonList = dao.findPoList(ProjectPerson.class, condition);
			
			if(projectPersonList.size() == 1) {
				ProjectPerson projectPerson = projectPersonList.get(0);
				if(projectPerson.getPlatformWorkerId() != null) {
					workerNo = projectPerson.getPlatformWorkerId();
				}
			}
		}
		// 如果需要转换地图位置
		if("1".equals(convert)) {
			double jingduD = Double.parseDouble(longitude);
        	double weiduD = Double.parseDouble(latitude);
        	double[] address = AddressUtil.wgs84tobd09(jingduD, weiduD);
        	longitude = String.valueOf(address[0]);
        	latitude = String.valueOf(address[1]);
		}
		Attendance attendance = new Attendance();
		attendance.setId(IDUtil.getUUID());
		attendance.setCreateTime(DateUtil.getCurrentDatetime());
		attendance.setDeviceNo(device.getDeviceNo());
		attendance.setPersonIdNo(idNo);
		attendance.setPersonName(name);
		attendance.setState("0");
		attendance.setTimeStamp(String.valueOf(System.currentTimeMillis()));
		attendance.setMatchScore(matchScore);
		attendance.setLongitude(longitude);
		attendance.setLatitude(latitude);
		attendance.setAttendancePlace(position);
		attendance.setPushTime("0");
		attendance.setWorkerNo(workerNo);
		dao.addPo(attendance);
		try {
			WebSocketServer.sendInfo(JsonUtil.toJSON(attendance),null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return attendance.getTimeStamp();
	}

	@Override
	public Pager<Attendance> getAttendancePage(int pageSize, int pageNo, String searchKey) {
		POCondition condition = new POCondition();
		if(!"".equals(searchKey)) {
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("personName");
			fields.add("personIdNo");
			condition.addOrLike(fields, searchKey);
		}
		condition.addOrderDesc("createTime");
		Pager<Attendance> attendancePage = dao.pagePo(Attendance.class, condition, pageNo, pageSize);
		return attendancePage;
	}


	@Override
	public Attendance getAttendanceDetail(String id) {
		Attendance attendance = dao.findPo(Attendance.class, id);
		return attendance;
	}
	
	@Override
	public Pager<Apk> getApkPage(int pageSize, int pageNo, String searchKey) {
		POCondition condition = new POCondition();
		if(!"".equals(searchKey)) {
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("name");
			fields.add("summary");
			condition.addOrLike(fields, searchKey);
		}
		condition.addOrderDesc("createTime");
		Pager<Apk> ApkPage = dao.pagePo(Apk.class, condition, pageNo, pageSize);
		return ApkPage;
	}


	@Override
	public void addApk(String fileName, String createTime, long fileSize) {
		Apk apk = new Apk();
		apk.setId(IDUtil.getUUID());
		apk.setCreateTime(createTime);
		apk.setFileSize(String.valueOf(fileSize));
		apk.setName(fileName);
		apk.setState("0");
		apk.setSummary("");
		apk.setVersion("");
		apk.setNotes("");
		dao.addPo(apk);
		
	}


	@Override
	public Apk getApkDetail(String apkId) {
		Apk apk = dao.findPo(Apk.class, apkId);
		return apk;
	}


	@Override
	public boolean checkHasVersion(String version) {
	    POCondition condition = new POCondition();
	    condition.addEQ("version", version);
	    List<Apk> userList = dao.findPoList(Apk.class, condition);
	    if (CollectionUtils.isEmpty(userList)) {
	        return false;
        }
        return true;
	}


	@Override
	public Apk updateApk(Apk apk) {
		Apk oldApkInfo = dao.findPo(Apk.class, apk.getId());
		oldApkInfo.setSummary(apk.getSummary());
		oldApkInfo.setVersion(apk.getVersion());
		if("on".equals(apk.getState())) {
			List<Apk> apkList = dao.findPoList(Apk.class);
			for (int i = 0; i < apkList.size(); i++) {
				apkList.get(i).setState("0");
			}
			dao.updatePoBatch(apkList);
			oldApkInfo.setState("1");
		}
		dao.updatePo(oldApkInfo);
		return oldApkInfo;
	}


	@Override
	public List<Apk> getValidApk() {
		POCondition condition = new POCondition();
		condition.addEQ("state", "1");
		List<Apk> apkList = dao.findPoList(Apk.class, condition);
		if(apkList.size() == 1) {
			return apkList;
		}
		return null;
	}


}
