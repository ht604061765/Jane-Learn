package com.tenfine.napoleon.user.service.impl;

import com.tenfine.napoleon.framework.SessionConstant;
import com.tenfine.napoleon.framework.bean.BaseService;
import com.tenfine.napoleon.framework.bean.POCondition;
import com.tenfine.napoleon.framework.bean.Pager;
import com.tenfine.napoleon.framework.bean.Result;
import com.tenfine.napoleon.framework.util.*;
import com.tenfine.napoleon.user.dao.UserDao;
import com.tenfine.napoleon.user.dao.po.*;
import com.tenfine.napoleon.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service("UserService")
public class UserServiceImpl extends BaseService implements UserService{
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


	private final UserDao dao;

	@Autowired
	public UserServiceImpl(UserDao dao) {
		this.dao = dao;
	}

	@Override
	public Device addDevice(Device device) {
	    String md5Pwd = EncryptUtil.md5Encode(device.getPwd());
		device.setId(IDUtil.getUUID());
        device.setPwd(md5Pwd);
        device.setCreateTime(DateUtil.getCurrentDatetime());
		device.setDeviceNo(IDUtil.getUUID14()); // 14位设备编码
		device.setToken(IDUtil.getUUID16()); //16位密钥
		device.setState("0"); // 初始状态
		dao.addPo(device);
		return device;
	}

	@Override
	public List<Device> getDeviceByAccount(String account) {
		POCondition condition = new POCondition();
		condition.addEQ("account", account);
		List<Device> userList = dao.findPoList(Device.class, condition);
		return userList;
	}

    @Override
    public boolean checkHasDevice(String account) {
	    POCondition condition = new POCondition();
	    condition.addEQ("account", account);
	    List<Device> userList = dao.findPoList(Device.class, condition);
	    if (CollectionUtils.isEmpty(userList)) {
	        return false;
        }
        return true;
    }

    @Override
	public List<Device> getDeviceByToken(String token) {
		POCondition condition = new POCondition();
		condition.addEQ("token", token);
		List<Device> userList = dao.findPoList(Device.class, condition);
		return userList;
	}

	@Override
	public List<ProjectPerson> getProjectPerson(String projectId) {
		POCondition condition = new POCondition();
		condition.addEQ("platformProId", projectId);
		List<ProjectPerson> personList = dao.findPoList(ProjectPerson.class, condition);
		if(personList.size() == 0) {
			return null;
		}
		return personList;
	}

	@Override
	public ProjectPerson getProjectPerson(int platformProjectId, int platformWorkerId) {
		POCondition condition = new POCondition();
		condition.addEQ("platformProId", platformProjectId);
		condition.addEQ("platformWorkerId", platformWorkerId);
		List<ProjectPerson> projectPersonList = dao.findPoList(ProjectPerson.class, condition);
		if(projectPersonList.size() != 1) {
			return null;
		}
		return projectPersonList.get(0);
	}

	@Override
	public PersonInfo getPersonByIdNo(String idNo) {
		POCondition condition = new POCondition();
		condition.addEQ("idNo", idNo);
		List<PersonInfo> personList = dao.findPoList(PersonInfo.class, condition);
		if(personList.size() != 1) {
			return null;
		}
		return personList.get(0);
	}

	@Override
	public Result<String> addCollectPerson(Device device, PersonInfo personInfo) {
		if(personInfo.getPeriod().isEmpty() || personInfo.getProvide().isEmpty()) {
			return Result.error("万分抱歉，重要参数丢失，请重新采集");
		}
		//新增personInfo表
		POCondition condition = new POCondition();
		condition.addEQ("idNo", personInfo.getIdNo());
		List<PersonInfo> personList = dao.findPoList(PersonInfo.class, condition);
		if(personList.size() == 0) {
			personInfo.setId(IDUtil.getUUID());
			personInfo.setCreateTime(DateUtil.getCurrentDatetime());
			personInfo.setCollectUser(device.getName());
			personInfo.setCollectDevice(device.getDeviceNo());
			dao.addPo(personInfo);
		}else if(personList.size() == 1) {
			// 重采逻辑
			personInfo.setId(personList.get(0).getId());
			personInfo.setCreateTime(DateUtil.getCurrentDatetime());
			personInfo.setCollectUser(device.getName());
			personInfo.setCollectDevice(device.getDeviceNo());
			dao.updatePo(personInfo);
		}
		//新增projectPerson表
		POCondition condition3 = new POCondition();
		condition3.addEQ("platformProId", device.getPlatformProId());
		condition3.addEQ("idNo", personInfo.getIdNo());
		List<ProjectPerson> projectPersonList = dao.findPoList(ProjectPerson.class, condition3);
		if(projectPersonList.size() == 1) {
			ProjectPerson projectPerson = projectPersonList.get(0);
			projectPerson.setCreateTime(DateUtil.getCurrentDatetime());
			projectPerson.setUploadTime("0");
			projectPerson.setUploaded("0"); // 未上传
			dao.updatePo(projectPerson);
		}else if(projectPersonList.size() == 0){
			ProjectPerson projectPerson = new ProjectPerson();
			projectPerson.setId(IDUtil.getUUID());
			projectPerson.setCreateTime(DateUtil.getCurrentDatetime());
			projectPerson.setIdNo(personInfo.getIdNo());
			projectPerson.setPlatformId(device.getPlatformId());
			projectPerson.setPlatformName(device.getPlatformName());
			projectPerson.setPlatformProId(device.getPlatformProId());
			projectPerson.setPlatformProName(device.getPlatformProName());
			projectPerson.setUploadTime("0");
			projectPerson.setState("0"); // 未入职
			projectPerson.setUploaded("0"); // 未上传
			
			// 不存在人员项目映射关系的情况要检查是否已经同步到外部人员表里
			POCondition condition4 = new POCondition();
			condition4.addEQ("idNo", personInfo.getIdNo());
			List<ProjectPersonOut> outUsers = dao.findPoList(ProjectPersonOut.class, condition4);
			if(outUsers.size() > 0) {
				ProjectPersonOut outUser = outUsers.get(0);
				projectPerson.setState("1");
				projectPerson.setPlatformWorkerId(outUser.getPlatformWorkerId());
				projectPerson.setPlatformWorkerName(outUser.getPlatformWorkerName());
				// 将外部人员设为已同步
				outUser.setState("1");
				dao.updatePo(outUser);
			}else {
				List<ProjectPerson> otherProUsers = dao.findPoList(ProjectPerson.class, condition4);
				if(otherProUsers.size() > 0) {
					ProjectPerson otherPersonInfo = otherProUsers.get(0);
					projectPerson.setState("1");
					projectPerson.setPlatformWorkerId(otherPersonInfo.getPlatformWorkerId());
					projectPerson.setPlatformWorkerName(otherPersonInfo.getPlatformWorkerName());
				}else {
					logger.debug("【用户采集】恭喜你，采集了一个新的用户");
				}
			}
			
			dao.addPo(projectPerson);
		}
		return Result.success();
	}

	@Override
	public User setSession(HttpSession session, User user) {
		SessionUtil.getInstance().setAttribute(SessionConstant.USER_ID, user.getId(), session);
		SessionUtil.getInstance().setAttribute(SessionConstant.USER_NAME, user.getName(), session);
		SessionUtil.getInstance().setAttribute(SessionConstant.USER_ACCOUNT, user.getAccount(), session);
		SessionUtil.getInstance().setAttribute(SessionConstant.USER_TYPE, user.getType(), session);
		SessionUtil.getInstance().setAttribute(SessionConstant.PLATFORM_NAME, PlatfromProperties.getName(), session);
		return user;
	}

	@Override
	public User checkUser(User userInfo) {
		POCondition condition = new POCondition();
		condition.addEQ("account", userInfo.getAccount());
		condition.addEQ("pwd", userInfo.getPwd());
		List<User> userList = dao.findPoList(User.class, condition);
		if(userList.size() == 1) {
			return userList.get(0);
		}else {
			return null;
		}
	}

	@Override
	public boolean checkHasAccount(User user) {
		POCondition con = new POCondition();
		con.addEQ("account", user.getAccount());
		List<User> userList = dao.findPoList(User.class, con);
		if(userList.size() == 0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public User addUser(User user) {
		String pwdMd5 = EncryptUtil.md5Encode(user.getPwd());
		user.setPwd(pwdMd5);
		user.setId(IDUtil.getUUID());
		user.setCreateTime(DateUtil.getCurrentDatetime());
		user.setType("ordinary");
		user.setState("1");
		dao.addPo(user);
		return user;
	}
	@Override
	public Jane addJane(Jane jane) {
		//jane.setUsername(jane.getUsername());
		//jane.setPassword(jane.getUsername());
		jane.setCreateTime(DateUtil.getCurrentDatetime());
		dao.addPo(jane);
		return jane;
	}

	@Override
	public List<Jane> getJaneList() {
		List<Jane> janes = dao.findPoList(Jane.class);
		return janes;
	}

	@Override
	public Pager<User> getUserPage(int pageSize, int pageNo, String searchKey) {
		POCondition condition = new POCondition();
		if(!"".equals(searchKey)) {
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("name");
			fields.add("account");
			condition.addOrLike(fields, searchKey);
		}
		condition.addOrderDesc("createTime");
		condition.addEQ("type", "ordinary"); //只显示普通用户
		Pager<User> projectWorkerPage = dao.pagePo(User.class, condition, pageNo, pageSize);
		return projectWorkerPage;
	}

	@Override
	public void changeUserState(String userId, String state) {
		POCondition condition = new POCondition();
		condition.addEQ("id", userId);
		Map<String, Object> matching = new HashMap<>();
		matching.put("state", state);
		dao.updatePoByCondition(User.class, matching, condition);
	}

	@Override
	public Pager<Device> getDevicePage(int pageSize, int pageNo, String searchKey) {
		POCondition condition = new POCondition();
		if(!"".equals(searchKey)) {
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("name");
			fields.add("account");
			fields.add("platformProName");
			condition.addOrLike(fields, searchKey);
		}
		condition.addOrderDesc("createTime");
		Pager<Device> devicePage = dao.pagePo(Device.class, condition, pageNo, pageSize);
		return devicePage;
	}

	@Override
	public void changeDeviceState(String deviceId, String state) {
		POCondition condition = new POCondition();
		condition.addEQ("id", deviceId);
		Map<String, Object> matching = new HashMap<>();
		matching.put("state", state);
		dao.updatePoByCondition(Device.class, matching, condition);
	}

	@Override
	public Pager<PersonInfo> getPersonPage(int pageSize, int pageNo, String searchKey) {
		POCondition condition = new POCondition();
		if(!"".equals(searchKey)) {
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("name");
			fields.add("idNo");
			condition.addOrLike(fields, searchKey);
		}
		condition.addOrderDesc("createTime");
		Pager<PersonInfo> personInfoPage = dao.pagePo(PersonInfo.class, condition, pageNo, pageSize);
		return personInfoPage;
	}

	@Override
	public Result<String> checkDeviceState(Device device) {
		if("0".equals(device.getState())) {
			return Result.error("设备未绑定");
		}
		if("2".equals(device.getState())) {
			return Result.error("设备换绑，请检查实名制平台");
		}
		return Result.success("验证通过");
	}

	@Override
	public ProjectPerson getProjectPerson(String proId, String idNo) {
		POCondition condition = new POCondition();
		condition.addEQ("platformProId", proId);
		condition.addEQ("idNo", idNo);
		List<ProjectPerson> personList = dao.findPoList(ProjectPerson.class, condition);
		if(personList.size() == 0) {
			return null;
		}
		return personList.get(0);
	}

	@Override
	public List<ProjectPersonOut> getOutUserList(String platformProId) {
		Date timeBefore = DateUtil.getMinuteAround(-3); // 三分钟之前的时间
		String syncTime = DateUtil.formatDatetime(timeBefore); // 转换成 yyyy-MM-dd HH:mm:ss，取得数据是三分钟之前得时间
		POCondition condition = new POCondition();
		condition.addEQ("platformProId", platformProId);
		condition.addEQ("state", "0");
		//condition.addLE("syncTime", syncTime);
		List<ProjectPersonOut> userList = dao.findPoList(ProjectPersonOut.class, condition);
		return userList;
	}

	@Override
	@Async
	public void addVipPerson(String uploadTime, String personNo) {
		PersonVip vipPerson = new PersonVip();
		vipPerson.setId(IDUtil.getUUID());
		vipPerson.setCreateTime(uploadTime);
		vipPerson.setPersonNo(personNo);
		vipPerson.setState("0");
		dao.addPo(vipPerson);
	}

	@Override
	public List<PersonVip> getVipFace(String syncTime) {
		POCondition condition = new POCondition();
		condition.addGE("createTime", syncTime);
		List<PersonVip> vipList = dao.findPoList(PersonVip.class, condition);
		return vipList;
	}

	@Override
	public List<Device> getAllDevice() {
		List<Device> deviceList = dao.findPoList(Device.class);
		if(deviceList != null) {
			return deviceList;
		}
		return null;
	}

	@Override
	public Device getDeviceByDeviceNo(String deviceNo) {
		POCondition condition = new POCondition();
		condition.addEQ("deviceNo", deviceNo);
		List<Device> deviceList = dao.findPoList(Device.class, condition);
		if(deviceList.size() == 1) {
			return deviceList.get(0);
		}
		return null;
	}

	@Override
	public List<Device> getProDevice(String proId) {
		POCondition condition = new POCondition();
		condition.addEQ("platformProId",proId);
		List<Device> deviceList = dao.findPoList(Device.class, condition);
		if (deviceList.size() > 0){
			return deviceList;
		}
		return null;
	}

	@Override
	public void checkDeviceState(Device device, int platformProId, String projectName) {
		String oldProId = device.getPlatformProId();
		if(oldProId == null || "" == oldProId) {
			device.setState("1");
			device.setPlatformProId(String.valueOf(platformProId));
			device.setPlatformProName(projectName);
			dao.updatePo(device);
		}else { //判断新旧proId是否相同
			if(oldProId.equals(String.valueOf(platformProId))) {
				device.setState("1"); // 启用
				dao.updatePo(device);
			}else {
				device.setState("2"); //用户换绑
				dao.updatePo(device);
			}
		}
	}

	@Override
	public List<Usermanagement> getUserMagList() {
		List<Usermanagement> userM = dao.findPoList(Usermanagement.class);
		return userM;
	}

	@Override
	public List<Usermanagement> getOneUserMagList(String id) {
		POCondition condition = new POCondition();
		condition.addEQ("id", id);
		List<Usermanagement> userM = dao.findPoList(Usermanagement.class,condition);
		System.out.println(userM);
		return userM;
	}

	@Override
	public Usermanagement addUserM(Usermanagement userM) {
		userM.setCreateTime(DateUtil.getCurrentDatetime());
		dao.addPo(userM);
		return userM;
	}

	@Override
	public void delUserM(String id) {
		dao.deletePo(Usermanagement.class,id);
	}

	@Override
	public Usermanagement modifyUserMan(Usermanagement userM) {
		userM.setCreateTime(DateUtil.getCurrentDatetime());
		dao.updatePo(userM);
		return userM;
	}
}
