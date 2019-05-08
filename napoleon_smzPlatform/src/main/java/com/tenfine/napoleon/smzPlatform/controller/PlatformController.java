package com.tenfine.napoleon.smzPlatform.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tenfine.napoleon.framework.bean.Pager;
import com.tenfine.napoleon.framework.util.JsonUtil;
import com.tenfine.napoleon.smzPlatform.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tenfine.napoleon.framework.bean.BaseController;
import com.tenfine.napoleon.framework.bean.Result;
import com.tenfine.napoleon.smzPlatform.dao.po.Platform;
import com.tenfine.napoleon.smzPlatform.service.PlatformService;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PlatformController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(PlatformController.class);

    private final PlatformService platformService;

    @Autowired
    ProjectService projectService;
    
	@Autowired
	public PlatformController(PlatformService platformService) {
		this.platformService = platformService;
	}


	@RequestMapping("/addPlatform")
	@ResponseBody
    public Result addPlatform(HttpServletRequest request) {
		Platform newPlatform = platformService.testAddPlatform();
		return Result.success(newPlatform);
    }


    @RequestMapping("/platformList")
    public String platformPage() {
	    return "platformList";
    }


	@RequestMapping("/getPlatformList")
	@ResponseBody
    public String getPlatformList(int pageSize, int offset, String searchKey) {
        int pageNo = offset / pageSize + 1;
		Pager<Platform> platformPager = platformService.getPlatformList(pageNo, pageSize, searchKey);
        Map<String, Object> map = new HashMap<>();
        map.put("total", platformPager.getTotalRecord());
        map.put("rows", platformPager.getRecordList());
        return JsonUtil.toJSON(map);
    }

    /* ============================== 视图 ============================== */
	
}
