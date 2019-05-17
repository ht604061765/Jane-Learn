package com.tenfine.napoleon.user.controller;

import com.tenfine.napoleon.framework.bean.BaseController;
import com.tenfine.napoleon.framework.bean.Result;
import com.tenfine.napoleon.framework.util.PlatfromProperties;
import com.tenfine.napoleon.user.dao.po.Jane;
import com.tenfine.napoleon.user.dao.po.PersonInfo;
import com.tenfine.napoleon.user.dao.po.User;
import com.tenfine.napoleon.user.dao.po.Usermanagement;
import com.tenfine.napoleon.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class JaneController extends BaseController {
    @Autowired
    private UserService userService;

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
        System.out.println(userM.getUsername() + userM.getPassword());
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
        ModelAndView mv = new ModelAndView("userInfo");
        List<Usermanagement> userM = userService.getUserMagList();
        mv.addObject("uList", userM);
        return mv;
    }

    /**
     * 查询用户其中一条信息
     */
    @RequestMapping("/queryOneUserMan")
    public ModelAndView userOneManage(String id) {
        System.out.println(id);
        ModelAndView mv = new ModelAndView("userInfo");
        List<Usermanagement> userM = userService.getOneUserMagList(id);
        mv.addObject("uList", userM);
        mv.addObject("userOneM",userM);
        return mv;
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

//    /**
//     * 修改用户信息
//     */
//    @RequestMapping("/modifyUserMan")
//    @ResponseBody
//    public Result<String> modifyUserMan(String id) {
//        System.out.println(id);
//        userService.modifyUserMan(id);
//        return Result.success("Congratulations to delete!");
//    }
}

