package com.tenfine.napoleon.user.controller;

import com.tenfine.napoleon.framework.bean.BaseController;
import com.tenfine.napoleon.framework.bean.Result;
import com.tenfine.napoleon.framework.util.PlatfromProperties;
import com.tenfine.napoleon.user.dao.po.*;
import com.tenfine.napoleon.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class JaneController extends BaseController {
    @Autowired
    private UserService userService;

    @RequestMapping("/menu")
    public String index() {
        return "menu";
    }

    @RequestMapping("/submitTest")
    @ResponseBody
    public Result<String> regSubmit(Jane jane) {
//        User userInfo = userService.addUser(user);
//        userService.setSession(request.getSession(), userInfo);
//        return Result.success("注册成功！");

        System.out.println(jane.getUsername() + jane.getPassword());
        userService.addJane(jane);
        return Result.success("Congratulations!");
        }

    /**
     * 新增用户信息
     */
    @RequestMapping("/submitUserM")
    @ResponseBody
    public Result<String> addUserM(Usermanagement userM) {
        userService.addUserM(userM);
        return Result.success("Congratulations!");
    }

    /**
     * 人员详情模态框
     */
    @RequestMapping("/Jantest1")
    public ModelAndView Jantest1() {
        ModelAndView mv = new ModelAndView("jane");
        List<Jane> janes = userService.getJaneList();
        mv.addObject("uList", janes);
        return mv;
    }

    @RequestMapping("/userIndex")
    public String userIndex() {
        return "userInfo";
    }

    /**
     * 查询用户信息
     */
    @RequestMapping("/userManage")
    public ModelAndView userManage() {
        System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!");
        ModelAndView mv = new ModelAndView("userInfo");
        List<Usermanagement> userM = userService.getUserMagList();
        mv.addObject("userM", userM);
        return mv;
    }

    /**
     * 查询用户其中一条信息
     */
    @RequestMapping("/queryOneUserMan")
    public ModelAndView userOneManage(@PathVariable("id") String id) {
        ModelAndView mv = new ModelAndView("userInfo");
        List<Usermanagement> userM = userService.getUserMagList();
        Usermanagement user =  userService.getOneUserMagList(id);
        mv.addObject("userM", userM);
        mv.addObject("userOneM",user);
        return mv;
    }

    /**
     * 修改Jane
     */
    @RequestMapping("/modelModifyJane")
    public ModelAndView modelModifyJane(String id) {
        ModelAndView mv = new ModelAndView("modelModifyJane");
        List<Usermanagement> userM = userService.getUserMagList();
        Usermanagement user =  userService.getOneUserMagList(id);
        mv.addObject("userM", userM);
        mv.addObject("userOneM",user);
        return mv;
    }

    /**
     * 打开整个ORG列表
     */
    @RequestMapping("/modelConnectOrg")
    public ModelAndView modelconnectOrg(String id) {
        ModelAndView mv = new ModelAndView("modelconnectOrg");
        List<Usermanagement> userM = userService.getUserMagList();
        List<Orglist> org =  userService.getOrgList();
        Usermanagement user =  userService.getOneUserMagList(id);
        mv.addObject("userM", userM);
        mv.addObject("orgM",org);
        mv.addObject("userOneM",user);
        return mv;
    }

    /**
     * 关联ORG
     */
    @RequestMapping("/connectOrg")
    @ResponseBody
    public Result<String> addConnectOrg(String orgId,String id) {
        Usermanagement user =  userService.getOneUserMagList(id);
        user.setOrgId(orgId);
        userService.updateUserM(user);
        return Result.success("Congratulations!");
    }

    /**
     * 删除用户信息
     */
    @RequestMapping("/delUserMan")
    @ResponseBody
    public Result<String> delUserM(String id) {
        System.out.println(id);
        userService.delUserM(id);
        return Result.success("Congratulations to delete!");
    }

   /**
    * 修改用户信息
    */
    @RequestMapping("/modifyUserInfo")
    @ResponseBody
    public Result<String> modifyUserMan(Usermanagement userOneM) {
        userService.updateUserM(userOneM);
        return Result.success("Congratulations!");
    }
}

