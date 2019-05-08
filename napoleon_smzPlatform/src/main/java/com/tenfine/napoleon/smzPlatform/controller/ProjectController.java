package com.tenfine.napoleon.smzPlatform.controller;

import com.tenfine.napoleon.framework.bean.Pager;
import com.tenfine.napoleon.framework.util.JsonUtil;
import com.tenfine.napoleon.framework.util.PathStatic;
import com.tenfine.napoleon.smzPlatform.dao.po.Project;
import com.tenfine.napoleon.smzPlatform.service.PersonInfoService;
import com.tenfine.napoleon.smzPlatform.service.ProjectPersonService;
import com.tenfine.napoleon.smzPlatform.service.ProjectService;
import com.tenfine.napoleon.user.dao.po.PersonInfo;
import com.tenfine.napoleon.user.dao.po.ProjectPerson;
import jdk.nashorn.internal.ir.ReturnNode;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    ProjectService projectService;
    @Autowired
    ProjectPersonService personService;
    @Autowired
    PersonInfoService personInfoService;

    @RequestMapping("/projectList")
    public ModelAndView projectList(String platformId) {
        ModelAndView view = new ModelAndView("projectList");
        view.addObject("platformId", platformId);
        return view;
    }

    /**
     * 项目详情模态框
     */
    @RequestMapping("/modelProjectDetails")
    public ModelAndView modelProjectDetails(HttpServletRequest request, String platformProjectId) {
        ModelAndView mv = new ModelAndView("modelProjectDetails");
        Project project = projectService.getProjectByPlatformProjectId(platformProjectId);
        mv.addObject("project", project);
        return mv;
    }

    /**
     * 项目管理页面
     */
    @RequestMapping("/projectListPage")
    public String projectListPage() {
        return "projectListPage";
    }

    @RequestMapping("/getProjectList2")
    @ResponseBody
    public String getProjectList2(int pageSize, int offset, String searchKey) {
        int pageNo = offset / pageSize + 1;
        Pager<Project> projectPager = projectService.getProjectPage(pageNo, pageSize, searchKey);
        Map<String, Object> map = new HashMap<>();
        map.put("total", projectPager.getTotalRecord());
        map.put("rows", projectPager.getRecordList());
        return JsonUtil.toJSON(map);
    }

    @RequestMapping("/getProjectList")
    @ResponseBody
    public String getProjectList(int pageSize, int offset, String pageParam, String searchKey) {
        JSONObject object = JSONObject.fromObject(pageParam);
        String platformId = object.getString("platformId");
        int pageNo = offset / pageSize + 1;
        Pager<Project> projectPager = projectService.getProjectListByPlatformId(pageNo, pageSize, platformId, searchKey);
        Map<String, Object> map = new HashMap<>();
        map.put("total", projectPager.getTotalRecord());
        map.put("rows", projectPager.getRecordList());
        return JsonUtil.toJSON(map);
    }


    @RequestMapping("/projectUserList")
    public ModelAndView projectUserList(String platformProjectId) {
        ModelAndView view = new ModelAndView("projectUserList");
        view.addObject("platformProjectId", platformProjectId);
        return view;
    }


    @RequestMapping("/getProjectUserList")
    @ResponseBody
    public String getProjectUserList(int pageSize, int offset, String pageParam, String searchKey) {
        JSONObject object = JSONObject.fromObject(pageParam);
        String platformProId = object.getString("platformProId");
        int pageNo = offset / pageSize + 1;
        Pager<ProjectPerson> personPager = personService.getProjectPersonList(pageNo, pageSize, platformProId, searchKey);
        Map<String, Object> map = new HashMap<>();
        map.put("total", personPager.getTotalRecord());
        map.put("rows", personPager.getRecordList());
        return JsonUtil.toJSON(map);
    }


    @RequestMapping("/getImg")
    @ResponseBody
    public void getImg(HttpServletResponse response, String type, String idNo, String timeStamp) throws IOException {
    	String imagePath = "";
    	if("collect".equals(type)) {
    		imagePath = PathStatic.getCollectPath(idNo) + "RGB.jpg";
    	}else if("cardFront".equals(type)) {
    		imagePath = PathStatic.getCollectPath(idNo) + "cardFront.jpg";
    	}else if("cardBack".equals(type)) {
    		imagePath = PathStatic.getCollectPath(idNo) + "cardBack.jpg";
    	}else if("attend".equals(type)) {
    		imagePath = PathStatic.getAttendancePath(idNo) + idNo + "/" + timeStamp + "attend.jpg";
    	}
    	File file = new File(imagePath);
        if (file.exists()) {
            FileInputStream in = new FileInputStream(file);
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[1024];
            while (in.read(b) != -1) {
                os.write(b);
            }
            in.close();
            os.flush();
            os.close();
        }
    }

    @RequestMapping("personInfoDetail")
    @ResponseBody
    public PersonInfo projectPersonDetail(String idNo) {
        return personInfoService.getPersonInfoByidNo(idNo);
    }

}
