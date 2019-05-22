package com.tenfine.napoleon.user.service;

import com.tenfine.napoleon.framework.bean.Pager;
import com.tenfine.napoleon.framework.bean.Result;
import com.tenfine.napoleon.user.dao.po.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Hunter
 */
public interface UserService {
	/**
	 * 获取项目下的所有设备
	 */
	List<Device> getProDevice(String proId);
	/**
	 * 校验用户状态
	 * 如果之前的platformProjectId不为空，返回Ture，反之返回False
	 */
	void checkDeviceState(Device device, int platformProId, String projectName);
	
	/**
	 * 获取符合要求的Vip数据
	 */
	List<PersonVip> getVipFace(String syncTime);
	
	/**
	 * 添加Vip用户数据
	 */
	void addVipPerson(String uploadTime, String personNo);
	
	/**
	 * 获取项目下得所有外部人员
	 */
	List<ProjectPersonOut> getOutUserList(String platformProId);
		
	/**
	 * 校验设备状态
	 */
	Result<String> checkDeviceState(Device device);
	
	/**
	 * 改变设备状态
	 */
	void changeDeviceState(String deviceId, String state);
	
	/**
	 * 改变用户状态
	 */
	void changeUserState(String userId, String state);
	
	/**
	 * 人员列表界面数据获取
	 */
	Pager<PersonInfo> getPersonPage(int pageSize, int pageNo, String searchKey);
	
	/**
	 * 设备管理界面数据获取
	 */
	Pager<Device> getDevicePage(int pageSize, int pageNo, String searchKey);
	
	/**
	 * 用户管理界面数据获取
	 */
	Pager<User> getUserPage(int pageSize, int pageNo, String searchKey);
	
	/**
	 * 加入会话
	 */
	User setSession(HttpSession session, User user);
	
	/**
	 * 新增采集人员
	 */
	Result<String> addCollectPerson(Device user, PersonInfo personInfo);
	
	/**
	 * 用身份证号获取人员详细信息
	 */
	PersonInfo getPersonByIdNo(String idNo);
	
	/**
	 * 获取项目人员信息
	 */
	ProjectPerson getProjectPerson(int platformProjectId, int platformWorkerId);
	
	/**
	 * 获取项目人员信息
	 */
	ProjectPerson getProjectPerson(String proId, String idNo);
	
	/**
	 * 获取项目人员列表
	 */
	List<ProjectPerson> getProjectPerson(String projectId);
	
	/**
	 * 新增一个项目用户，返回新增后的用户
	 * @param userInfo：注册用户信息
	 */
	Device addDevice(Device userInfo);

	/**
	 * 通过account获取项目用户集合
	 * @param account：用户账号
	 */
	List<Device> getDeviceByAccount(String account);
	
	/**
	 * 通过deviceNo获取项目用户集合
	 */
	Device getDeviceByDeviceNo(String deviceNo);
	
	/**
	 * 通过token获取项目用户集合
	 * @param
	 */
	List<Device> getDeviceByToken(String token);

    /**
     * 校验存在项目用户
     * @param account
     * @return
     */
    boolean checkHasDevice(String account);
    
    /**
     * 校验登录用户
     */
    User checkUser(User userInfo);
    
    /**
     * 校验是否存在用户
     */
	boolean checkHasAccount(User user);
	
    /**
     * 新增用户
     */
	User addUser(User user);

	/**
	 * 新增测试
	 * @return
	 */
	Jane addJane(Jane Jane);

	/**
	 * 新增测试
	 * @return
	 */
	List<Jane> getJaneList();
	
	/**
	 * 模拟所有设备链接
	 */
	List<Device> getAllDevice();

	/**
	 * 查询用户列表测试
	 * @return
	 */
	List<Usermanagement> getUserMagList();

	/**
	 * 查询机构列表测试
	 * @return
	 */
	List<Orglist> getOrgList();

	/**
	 * 通过机构ID查询用户列表测试
	 * @return
	 */
	List<Usermanagement> getUserMagListByOrgId(String orgId);

	/**
	 * 查询一条数据测试
	 * @return
	 */
	Usermanagement getOneUserMagList(String id);

	/**
	 * 新增测试
	 * @return
	 */
	Usermanagement addUserM(Usermanagement userM);

	/**
	 * 修改测试
	 * @return
	 */
	Usermanagement updateUserM(Usermanagement userOneM);
	/**
	 * 删除测试
	 */
	void delUserM(String id);

	/**
	 * 修改测试
	 */
	Usermanagement modifyUserMan(Usermanagement userM);
}
