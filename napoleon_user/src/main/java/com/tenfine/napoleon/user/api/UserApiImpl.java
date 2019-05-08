package com.tenfine.napoleon.user.api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tenfine.napoleon.api.user.UserApi;
import com.tenfine.napoleon.framework.bean.POCondition;
import com.tenfine.napoleon.framework.util.DateUtil;
import com.tenfine.napoleon.framework.util.IDUtil;
import com.tenfine.napoleon.framework.util.MapUtil;
import com.tenfine.napoleon.user.dao.UserDao;
import com.tenfine.napoleon.user.dao.po.PersonInfo;
import com.tenfine.napoleon.user.dao.po.PersonState;
import com.tenfine.napoleon.user.dao.po.ProjectPerson;
import com.tenfine.napoleon.user.dao.po.ProjectPersonOut;
import com.tenfine.napoleon.user.dao.po.Device;
import com.tenfine.napoleon.user.service.UserService;



@Service("UserApi")
public class UserApiImpl implements UserApi{

	private final UserDao dao;
	@Autowired
	public UserApiImpl(UserDao dao, UserService userService) {
		this.dao = dao;
	}

	@Override
	public List<Map<String, Object>> getPlatformDevice(String platformId) {
//		POCondition condition = new POCondition();
//		condition.addEQ("platformId", platformId);
//		List<Device> deviceList = dao.findPoList(Device.class, condition);
		List<Device> deviceList = dao.findPoList(Device.class);
		List<Map<String, Object>> rtnData = MapUtil.beansToMaps(deviceList);
		return rtnData;
	}

	@Override
	public Map<String, Object> getUserByDeviceNo(String deviceNO) {
		POCondition condition = new POCondition();
		condition.addEQ("deviceNo", deviceNO);
		List<Device> userList = dao.findPoList(Device.class, condition);
		if(userList.size() == 1) {
			Device user = userList.get(0);
			return MapUtil.beanToMap(user);
		}
		return null;
	}

	@Override
	public void quitPerson(int patformProjectId, int patformWorkerId) {
		POCondition condition = new POCondition();
		condition.addEQ("platformProId", patformProjectId);
		condition.addEQ("platformWorkerId", patformWorkerId);
		List<ProjectPerson> personList = dao.findPoList(ProjectPerson.class, condition);
		if(personList.size() == 1) {
			ProjectPerson personMapping = personList.get(0);
			personMapping.setState("2");
			dao.updatePo(personMapping);
		}
	}

	@Override
	public void entryPerson(String idNo, int projectId, String projectName, String deviceNo, int workerId, String workerName) {
		Device device = getDevice(deviceNo);
		POCondition condition = new POCondition();
		condition.addEQ("idNo", idNo);
		List<PersonInfo> personList = dao.findPoList(PersonInfo.class, condition);
		if(personList.size() == 0) { 
			//不存在人员基本信息
			POCondition condition2 = new POCondition();
			condition2.addEQ("idNo", idNo);
			condition2.addEQ("platformProId", projectId);
			List<ProjectPersonOut> projectPersonOutList = dao.findPoList(ProjectPersonOut.class, condition2);
			if(projectPersonOutList.size() == 0) {
				ProjectPersonOut projectPersonOut = new ProjectPersonOut();
				projectPersonOut.setId(IDUtil.getUUID());
				projectPersonOut.setCreateTime(DateUtil.getCurrentDatetime());
				projectPersonOut.setIdNo(idNo);
				projectPersonOut.setPlatformId(device.getPlatformId());
				projectPersonOut.setPlatformProId(String.valueOf(projectId));
				projectPersonOut.setPlatformProName(device.getPlatformProName());
				projectPersonOut.setPlatformWorkerId(String.valueOf(workerId));
				projectPersonOut.setPlatformWorkerName(workerName);
				projectPersonOut.setState("1");
				dao.addPo(projectPersonOut);
			}
		}else if(personList.size() == 1) { //有这个人得话就把他的映射关系变为入职
			POCondition condition1 = new POCondition();
			condition1.addEQ("idNo", idNo);
			condition1.addEQ("platformProId", projectId);
			List<ProjectPerson> projectPersonList = dao.findPoList(ProjectPerson.class, condition1);
			if(projectPersonList.size() == 1) {
				// 同步人员数据
				ProjectPerson projectPerson = projectPersonList.get(0);
				projectPerson.setState("1");
				projectPerson.setPlatformWorkerId(String.valueOf(workerId));
				projectPerson.setPlatformWorkerName(workerName);
				dao.updatePo(projectPerson);
			}
		}
		
	}

	private Device getDevice(String deviceNO) {
		POCondition condition = new POCondition();
		condition.addEQ("deviceNo", deviceNO);
		List<Device> userList = dao.findPoList(Device.class, condition);
		if(userList.size() == 1) {
			return userList.get(0);
		}
		return null;
	}

	@Override
	public Map<String, Object> getUserByToken(String token) {
		POCondition condition = new POCondition();
		condition.addEQ("token", token);
		List<Device> userList = dao.findPoList(Device.class, condition);
		if(userList.size() == 1) {
			return MapUtil.beanToMap(userList.get(0));
		}
		return null;
	}

	@Override
	public void addOutPerson(Map<String, Object> outUserInfo) {
		//外部人员信息
		ProjectPersonOut outUser = null;
		POCondition condition = new POCondition();
		condition.addEQ("idNo", outUserInfo.get("idNumber"));
		condition.addEQ("platformProId", outUserInfo.get("projectId"));
		List<ProjectPersonOut> outUserList = dao.findPoList(ProjectPersonOut.class, condition);
		if(outUserList.size() == 1) {
			outUser = outUserList.get(0);
		}else {
			return;
		}
		//加入personInfo表
		PersonInfo personInfo = addPsersonInfo(outUserInfo, outUser);
		//加入personState表
		addPersonState(personInfo);
		//加入projectPerson表
		addProjectPerson(personInfo,outUser);
		//修改外部人员状态为已同步
		outUser.setState("1");
		dao.updatePo(outUser);		
	}
	
	private void addProjectPerson(PersonInfo personInfo, ProjectPersonOut outUser) {
		POCondition condition = new POCondition();
		condition.addEQ("idNo", outUser.getIdNo());
		condition.addEQ("platformProId", outUser.getPlatformProId());
		List<ProjectPerson> projectPersonList = dao.findPoList(ProjectPerson.class, condition);
		if(projectPersonList.size() == 0) {
			ProjectPerson projectPerson = new ProjectPerson();
			projectPerson.setId(IDUtil.getUUID());
			projectPerson.setCreateTime(DateUtil.getCurrentDatetime());
			projectPerson.setIdNo(personInfo.getIdNo());
			projectPerson.setPlatformId(outUser.getPlatformId());
			projectPerson.setPlatformProId(outUser.getPlatformProId());
			projectPerson.setPlatformProName(outUser.getPlatformProName());
			projectPerson.setPlatformWorkerName(personInfo.getName());
			projectPerson.setState("1"); //1：入职，2：离职
			dao.addPo(projectPerson);
		}else {
			ProjectPerson projectPerson = projectPersonList.get(0);
			projectPerson.setState("1"); //1：入职，2：离职
			dao.updatePo(projectPerson);
		}
	}
	
	
	private PersonInfo addPsersonInfo(Map<String, Object> outUserInfo, ProjectPersonOut outUser) {
		POCondition condition = new POCondition();
		condition.addEQ("idNo", outUserInfo.get("idNumber"));
		List<PersonInfo> personList = dao.findPoList(PersonInfo.class, condition);
		if(personList.size() == 0) {
			PersonInfo personInfo = new PersonInfo();
			personInfo.setId(IDUtil.getUUID());
			personInfo.setCreateTime(DateUtil.getCurrentDatetime());
			personInfo.setName((String) outUserInfo.get("userName"));
			personInfo.setIdNo((String) outUserInfo.get("idNumber"));
			personInfo.setSex((String) outUserInfo.get("sex"));
			personInfo.setNation((String) outUserInfo.get("nation"));
			personInfo.setBirthday((String) outUserInfo.get("birthday"));
			personInfo.setAddress((String) outUserInfo.get("idCardAddress"));
			personInfo.setProvide((String) outUserInfo.get("certificationAuthority"));
			personInfo.setPeriod((String) outUserInfo.get("validity"));
			personInfo.setCollectUser("outUser");
			personInfo.setCollectDevice("outDevice");
			dao.addPo(personInfo);
			return personInfo;
		}
		return personList.get(0);
	}
	
	private void addPersonState(PersonInfo personInfo) {
		POCondition condition = new POCondition();
		condition.addEQ("personId", personInfo.getId());
		List<PersonState> personStateList = dao.findPoList(PersonState.class, condition);
		if(personStateList.size() == 0) {
			PersonState personState = new PersonState();
			personState.setId(IDUtil.getUUID());
			personState.setCreateTime(DateUtil.getCurrentDatetime());
			personState.setPersonId(personInfo.getId());
			personState.setPersonName(personInfo.getName());
			personState.setState("1");
			dao.addPo(personState);
		}
	}

	@Override
	public String getUploadUserIdNo(String platformProId, String platformId) {
		long minuteBefore = new Date().getTime() - 1*60*1000;
		String minuteBeforeDateTime = DateUtil.formatDatetime(minuteBefore);
		POCondition condition = new POCondition();
		condition.addEQ("platformProId", platformProId); //这个项目
//		condition.addEQ("platformId", platformId); //这个平台
		condition.addEQ("uploaded","0"); //未提交的
//		condition.addLT("uploadTime", minuteBeforeDateTime); //多少分钟以前的数据
		condition.addOrderAsc("uploadTime"); // 按照推送时间升序
		List<ProjectPerson> projectPersonList = dao.findPoList(ProjectPerson.class, condition);
		if(projectPersonList.size() > 0 && projectPersonList != null) {
			return projectPersonList.get(0).getIdNo();
		}
		return null;
	}

	@Override
	public Map<String, Object> getUserByIdNo(String idNo) {
		POCondition condition = new POCondition();
		condition.addEQ("idNo", idNo);
		List<PersonInfo> personInfoList = dao.findPoList(PersonInfo.class, condition);
		if(personInfoList.size() == 1) {
			return MapUtil.beanToMap(personInfoList.get(0));
		}
		return null;
	}

	@Override
	public void uploadedUser(String idNo, String platformProId) {
		POCondition condition = new POCondition();
		condition.addEQ("platformProId", platformProId);
		condition.addEQ("idNo", idNo);
		List<ProjectPerson> projectPersonList = dao.findPoList(ProjectPerson.class, condition);
		if(projectPersonList.size() == 1) {
			ProjectPerson projectPerson = projectPersonList.get(0);
			projectPerson.setUploadTime(DateUtil.getCurrentDatetime());
			projectPerson.setUploaded("1");
			dao.updatePo(projectPerson);
		}
	}

	@Override
	public void changeUploadTime(String idNo, String platformProId) {
		POCondition condition = new POCondition();
		condition.addEQ("platformProId", platformProId);
		condition.addEQ("idNo", idNo);
		List<ProjectPerson> projectPersonList = dao.findPoList(ProjectPerson.class, condition);
		if(projectPersonList.size() == 1) {
			ProjectPerson projectPerson = projectPersonList.get(0);
			projectPerson.setUploadTime(DateUtil.getCurrentDatetime());
			dao.updatePo(projectPerson);
		}
		
	}

	@Override
	public void stopDevice(String userId) {
		Device device = dao.findPo(Device.class, userId);
		device.setState("0");
		dao.updatePo(device);
	}

	@Override
	public void checkDeviceState(String deviceId, int platformProId, String projectName) {
		Device device = dao.findPo(Device.class, deviceId);
		String oldProId = device.getPlatformProId();
		if(oldProId == null || "" == oldProId) {
			device.setState("1");
			device.setPlatformProId(String.valueOf(platformProId));
			device.setPlatformProName(projectName);
			dao.updatePo(device);
		}else { //判断新旧proId是否相同
			if(oldProId.equals(String.valueOf(platformProId))) {
				// 启用
				device.setState("1");
				dao.updatePo(device);
			}else {
				//用户换绑
				device.setState("2");
				dao.updatePo(device);
			}
		}
	}

	@Override
	public List<String> getUploadUserIds(String platformProId) {
		POCondition condition = new POCondition();
		//这个项目
		condition.addEQ("platformProId", platformProId);
		//未提交的
		condition.addEQ("uploaded","0");
		List<ProjectPerson> projectPersonList = dao.findPoList(ProjectPerson.class, condition);
		List<String> ids = new ArrayList<String>();
		if(projectPersonList.size() > 0) {
			for (int i = 0, n=projectPersonList.size(); i < n; i++) {
				ids.add(projectPersonList.get(i).getIdNo());
			}
		}

		return ids;
	}

	@Override
	@Async
	public void changeSyncTime(String idNo) {
		POCondition condition = new POCondition();
		condition.addEQ("idNo", idNo);
		List<ProjectPersonOut> outPersonList = dao.findPoList(ProjectPersonOut.class, condition);
		if(outPersonList.size() == 1) {
			ProjectPersonOut outPerson = outPersonList.get(0);
			outPerson.setSyncTime(DateUtil.getCurrentDatetime());
			dao.updatePo(outPerson);
		}
		
	}

}
