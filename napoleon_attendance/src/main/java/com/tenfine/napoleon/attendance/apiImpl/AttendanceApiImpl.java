package com.tenfine.napoleon.attendance.apiImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tenfine.napoleon.api.attendance.AttendanceApi;
import com.tenfine.napoleon.attendance.dao.AttendanceDao;
import com.tenfine.napoleon.attendance.dao.po.Attendance;
import com.tenfine.napoleon.faceutils.AttendanceRecognition;
import com.tenfine.napoleon.framework.bean.POCondition;
import com.tenfine.napoleon.framework.util.DateUtil;
import com.tenfine.napoleon.framework.util.MapUtil;
import com.tenfine.napoleon.user.service.UserService;


/**
 * @author Hunter
 */
@Service("AttendanceApi")
public class AttendanceApiImpl implements AttendanceApi{

	private final AttendanceDao dao;
	
    private final UserService userService;

	@Autowired
	public AttendanceApiImpl(AttendanceDao dao, UserService userService) {
		this.dao = dao;
		this.userService = userService;
	}
	@Override
	public List<Map<String, Object>> getSubmitAttendance(String deviceNo) {
		long minuteBefore = System.currentTimeMillis() - 1*60*1000;
		POCondition condition = new POCondition();
		condition.addEQ("deviceNo", deviceNo);
		condition.addEQ("state", "0");
		condition.addGT("workerNo", "0");
		//没推送过或者是1分钟以前推送过的数据，保证推送有效性
		condition.addLT("pushTime", DateUtil.formatDatetime(minuteBefore));
		List<Attendance> attendanceList = dao.findPoList(Attendance.class, condition);
		if(attendanceList.size() > 0) {
			return MapUtil.beansToMaps(attendanceList);
		}
		return null;
	}
	
	public static void main(String[] args) {
		String nowTime = DateUtil.getCurrentDatetime();
		
	}
	@Override
	public void uploadedRecord(String attendanceId) {
		Attendance attendance = dao.findPo(Attendance.class, attendanceId);
		if(attendance != null) {
			attendance.setState("1");
			dao.updatePo(attendance);
		}
	}
	@Override
	public void submitAttendanceList(List<Map<String, Object>> sendList) {
		for (int i = 0; i < sendList.size(); i++) {
			Attendance attendance = dao.findPo(Attendance.class,(String) sendList.get(i).get("id"));
			if(attendance != null) {
				attendance.setPushTime(DateUtil.getCurrentDatetime());
				dao.updatePo(attendance);
			}
		}
	}

	@Override
	public void entryPerson(String idNo, String deviceNo, int workerNoInt) {
		POCondition condition = new POCondition();
		condition.addEQ("personIdNo", idNo);
		condition.addEQ("deviceNo", deviceNo);
		condition.addEQ("workerNo", "0");
		List<Attendance> attendanceList = dao.findPoList(Attendance.class, condition);
		if(attendanceList.size() > 0) {
			for (Attendance attendance : attendanceList){
				attendance.setWorkerNo(String.valueOf(workerNoInt));
			}
			dao.updatePoBatch(attendanceList);
		}
	}
	@Override
	public void createUserGroup(int projectIdInt, String platformId) {
		String projectId = String.valueOf(projectIdInt);
		AttendanceRecognition.createUserGroup(platformId + "_" + projectId);
	}
	

}
