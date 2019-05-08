package com.tenfine.napoleon.user.controller;

import com.tenfine.napoleon.framework.bean.BaseController;
import com.tenfine.napoleon.framework.bean.Result;
import com.tenfine.napoleon.framework.util.PlatfromProperties;
import com.tenfine.napoleon.user.dao.po.Jane;
import com.tenfine.napoleon.user.dao.po.PersonInfo;
import com.tenfine.napoleon.user.dao.po.User;
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
     * 人员详情模态框
     */
    @RequestMapping("/Jantest1")
    public ModelAndView Jantest1() {
        ModelAndView mv = new ModelAndView("jane");
        List<Jane> janes = userService.getJaneList();
        mv.addObject("uList", janes);
        return mv;
    }
}

