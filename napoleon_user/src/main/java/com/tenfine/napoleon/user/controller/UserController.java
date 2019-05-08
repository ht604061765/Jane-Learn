package com.tenfine.napoleon.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tenfine.napoleon.framework.bean.BaseController;
import com.tenfine.napoleon.framework.bean.Pager;
import com.tenfine.napoleon.framework.bean.Result;
import com.tenfine.napoleon.framework.util.EncryptUtil;
import com.tenfine.napoleon.framework.util.PlatfromProperties;
import com.tenfine.napoleon.framework.util.JsonUtil;
import com.tenfine.napoleon.framework.util.SessionUtil;
import com.tenfine.napoleon.user.dao.po.Device;
import com.tenfine.napoleon.user.dao.po.PersonInfo;
import com.tenfine.napoleon.user.dao.po.ProjectPerson;
import com.tenfine.napoleon.user.dao.po.User;
import com.tenfine.napoleon.user.service.UserService;

@Controller
public class UserController extends BaseController{
	
	@Autowired
    private UserService userService;
	
	@RequestMapping("/changeDeviceState")
	@ResponseBody
    public Result<String> changeDeviceState(HttpServletRequest request, String deviceId, String state) {
		userService.changeDeviceState(deviceId, state);
		return Result.success("修改成功");
    }
	
	@RequestMapping("/changeUserState")
	@ResponseBody
    public Result<String> changeUserState(HttpServletRequest request, String userId, String state) {
		userService.changeUserState(userId, state);
		return Result.success("修改成功");
    }
	
	@RequestMapping("/getPersonList")
	@ResponseBody
    public String getPersonList(HttpServletRequest request, int pageSize, int offset ,String searchKey) {
		int pageNo = offset/pageSize + 1;
		Pager<PersonInfo> personPage = userService.getPersonPage(pageSize, pageNo, searchKey);
		List<PersonInfo> personList = personPage.getRecordList();
		Map<String, Object> rtnData = new HashMap<>();
		rtnData.put("total", personPage.getTotalRecord());
		rtnData.put("rows", personList);
		return JsonUtil.toJSON(rtnData);
    }
	
	@RequestMapping("/getDeviceList")
	@ResponseBody
    public String getDeviceList(HttpServletRequest request, int pageSize, int offset ,String searchKey) {
		int pageNo = offset/pageSize + 1;
		Pager<Device> devicePage = userService.getDevicePage(pageSize, pageNo, searchKey);
		List<Device> deviceList = devicePage.getRecordList();
		Map<String, Object> rtnData = new HashMap<>();
		rtnData.put("total", devicePage.getTotalRecord());
		rtnData.put("rows", deviceList);
		return JsonUtil.toJSON(rtnData);
    }
	
	@RequestMapping("/getUserList")
	@ResponseBody
    public String getUserList(HttpServletRequest request, int pageSize, int offset ,String searchKey) {
		int pageNo = offset/pageSize + 1;
		Pager<User> userPage = userService.getUserPage(pageSize, pageNo, searchKey);
		List<User> userList = userPage.getRecordList();
		Map<String, Object> rtnData = new HashMap<>();
		rtnData.put("total", userPage.getTotalRecord());
		rtnData.put("rows", userList);
		return JsonUtil.toJSON(rtnData);
    }
	
	@RequestMapping("/addDeviceSubmit")
    @ResponseBody
    public Result addDeviceSubmit(Device device) {
	    if (userService.checkHasDevice(device.getAccount())) {
	        return Result.error("设备账号已存在");
        }
	    device.setPlatformId(PlatfromProperties.getId()); //预防后续括展
	    device.setPlatformName(PlatfromProperties.getId()); //预防后续括展
		Device newDevice = userService.addDevice(device);
		return Result.success(newDevice);
    }


	@RequestMapping("/api/getLogin")
	@ResponseBody
    public Result getLogin(String account, String pwd, String ver) {
		List<Device> deviceList = userService.getDeviceByAccount(account);
		if(deviceList.size() != 1) {
			return Result.error("用户不存在");
		}
		Device device = deviceList.get(0);
		String md5Pwd = EncryptUtil.md5Encode(pwd);
		if(!md5Pwd.equals(device.getPwd())) {
			return Result.error("密码错误");
		}
		// 校验用户状态
		Result check = userService.checkDeviceState(device);
		if(!check.isSuccess()){
			return check;
		}
        Map<String, Object> rtnData = new HashMap<>();
        rtnData.put("token", device.getToken());
        rtnData.put("projectName", device.getPlatformProName());
        rtnData.put("projectId", device.getPlatformProId());
        rtnData.put("deviceId", device.getDeviceNo());
        return Result.success(rtnData);
    }
	
	
	public static void main(String[] args) {
		String aaa = "th123456";
		System.out.println(EncryptUtil.md5Encode(aaa));
	}
	
	@RequestMapping("/api/getLogout")
	@ResponseBody
    public Result getLogout(String ver, String token) {
		// TODO 退出逻辑
		return Result.success();
    }
	
	@RequestMapping("/api/getMyUserList")
	@ResponseBody
    public Result getMyUserList(String ver, String token, Integer projectId, Integer teamId, String keyWord, Long sort) {
		List<Device> deviceList = userService.getDeviceByToken(token);
		if(deviceList.size() != 1) {
			return Result.error("用户校验出错，请检查您提交的token");
		}
		Device device = deviceList.get(0);
		Result check = userService.checkDeviceState(device);
		if(!check.isSuccess()){
			return check;
		}
        List<Map<String, Object>> rtnData = new ArrayList<>();
		List<ProjectPerson> personList = userService.getProjectPerson(String.valueOf(projectId));
		if(personList == null) {
			return Result.success(null);
		}
        for (int i = 1; i < personList.size(); i++) {
        	ProjectPerson person = personList.get(i);
            Map<String, Object> personMap = new HashMap<>();;
        	personMap.put("uid", person.getPlatformWorkerId());
        	personMap.put("name", person.getPlatformWorkerName());
        	personMap.put("id_number", person.getIdNo());
        	personMap.put("face_state", person.getState());
        	personMap.put("sort", "0");
        	personMap.put("projectUserId", person.getPlatformWorkerId());
        	rtnData.add(personMap);
        }
		return Result.success(rtnData);
	}
	
	@RequestMapping("/api/getPersonInfo")
	@ResponseBody
    public Result<Map<String, Object>> getPersonInfo(String ver, String token, Integer uid, Integer projectId) {
		List<Device> deviceList = userService.getDeviceByToken(token);
		if(deviceList.size() != 1) {
			return Result.error("用户校验出错，请检查您提交的token");
		}
		Device device = deviceList.get(0);
		Result check = userService.checkDeviceState(device);
		if(!check.isSuccess()){
			return check;
		}
		ProjectPerson projectPerson = userService.getProjectPerson(projectId, uid);
		PersonInfo personInfo = userService.getPersonByIdNo(projectPerson.getIdNo());
		
	    Map<String, Object> map = new HashMap<>();
	    map.put("header_img", "待开发"); //头像路径相对内容
	    map.put("name", personInfo.getName());
	    map.put("teamName", "待开发");
	    map.put("id_number", personInfo.getIdNo());
	    map.put("phone", "待开发");
	    map.put("sex", personInfo.getSex());
	    map.put("nation", personInfo.getNation());
	    map.put("birthday", personInfo.getBirthday());
	    map.put("address", personInfo.getAddress());
	    map.put("bank_account", "待开发");
	    map.put("bank_name", "待开发");
	    map.put("people_type_code", "待开发");
	    map.put("people_type_name", "待开发");
	    map.put("position_id", "待开发");
	    map.put("position_name", "待开发");
	    map.put("certificate_number", "待开发");
	    map.put("contract_img", "待开发");
	    map.put("teamList", "待开发");
	    return Result.success(map);
    }
	
	@RequestMapping("/loginSubmit")
	@ResponseBody
    public Result<String> loginSubmit(HttpServletRequest request, User user) {
		User userInfo = userService.checkUser(user);
		if(userInfo != null) {
			if("0".equals(userInfo.getState())) {
				return Result.error("该账户被禁用，请联系管理员处理");
			}
			userService.setSession(request.getSession(), userInfo);
			return Result.success("登录成功");
		}else {
			return Result.error("用户验证失败，请检查后重试");
		}
    }
	
	@RequestMapping("/logout")
	@ResponseBody
    public Result<String> logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		SessionUtil.getInstance().invalidate(session);
		return Result.success("logout success");
    }
	
	@RequestMapping("/regSubmit")
	@ResponseBody
    public Result<String> regSubmit(HttpServletRequest request, User user) {
		if(userService.checkHasAccount(user)) {
			user.setPlatformId(PlatfromProperties.getId()); //预防后续括展
			user.setPlatformName(PlatfromProperties.getName()); //预防后续括展
			User userInfo = userService.addUser(user);
			userService.setSession(request.getSession(), userInfo);
			return Result.success("注册成功！");
		}else {
			return Result.error("用户已经存在");
		}
    }
    /* ============================== 视图 ============================== */
	
	/**
     * 人员详情模态框
     */
	@RequestMapping("/modelPersonDetails")
    public ModelAndView modelPersonDetails(HttpServletRequest request, String idNo) {
		ModelAndView mv = new ModelAndView("modelPersonDetails");
		PersonInfo personInfo = userService.getPersonByIdNo(idNo);
		mv.addObject("personInfo", personInfo);
		return mv;
    }
	
	/**
     * 添加设备模态框
     */
	@RequestMapping("/modelAddDevice")
    public String modelAddDevice() {
		return "modelAddDevice";
    }
	
	/**
     * 添加用户模态框
     */
	@RequestMapping("/modelAddUser")
    public String modelAddUser() {
		return "modelAddUser";
    }
	
	/**
     * 人员列表页面
     */
	@RequestMapping("/personListPage")
    public String personListPage() {
		return "personListPage";
    }
	
	/**
     * 账号管理页面
     */
	@RequestMapping("/userListPage")
    public String userListPage() {
		return "userListPage";
    }
	
	/**
     * 设备管理页面
     */
	@RequestMapping("/deviceListPage")
    public String deviceListPage() {
		return "deviceListPage";
    }
	
    /**
     * 登录页面
     */
	@RequestMapping("/login")
	public String login() {
	    return "login";
    }
	@RequestMapping("/jane")
	public String jane() {
		return "jane";
	}
	/**
	 * 主页面
	 */
	@RequestMapping("/")
	public String index() {
		return "index";
    }
	
    /**
     * 发布测试页
     */
    @RequestMapping("/testPage")
    public ModelAndView testPage() {
    	ModelAndView mv = new ModelAndView("testPage");
        return mv;
    }
    
    /**
     * 注册页面
     */
    @RequestMapping("/reg")
    @Deprecated
    public String reg() {
        return "reg";
    }
}
