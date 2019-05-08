package com.tenfine.napoleon.smzPlatform.service;

import com.tenfine.napoleon.framework.bean.Pager;
import com.tenfine.napoleon.smzPlatform.dao.po.Project;

import java.util.List;

public interface ProjectService {

    Pager<Project> getProjectListByPlatformId(int pageNo, int pageSize, String platfromId, String searchKey);

    /**
     * 项目管理界面数据获取
     */
    Pager<Project> getProjectPage(int pageNo, int pageSize, String searchKey);

    /**
     * 通过项目ID获取项目详情
     */
    Project getProjectByPlatformProjectId(String platformProjectId);
}
