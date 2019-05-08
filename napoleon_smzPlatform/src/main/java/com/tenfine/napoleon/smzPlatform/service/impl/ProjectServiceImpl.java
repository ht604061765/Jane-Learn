package com.tenfine.napoleon.smzPlatform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tenfine.napoleon.framework.bean.POCondition;
import com.tenfine.napoleon.framework.bean.Pager;
import com.tenfine.napoleon.smzPlatform.dao.ProjectDao;
import com.tenfine.napoleon.smzPlatform.dao.po.Project;
import com.tenfine.napoleon.smzPlatform.service.ProjectService;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    ProjectDao projectDao;

    @Override
    public Pager<Project> getProjectListByPlatformId(int pageNo, int pageSize, String platfromId, String searchKey) {
        POCondition condition = new POCondition();
        condition.addEQ("platformId", platfromId);
        if (!"".equals(platfromId)) {
            condition.addLike("name", searchKey);
        }
        return projectDao.pagePo(Project.class, condition, pageNo, pageSize);
    }

    @Override
    public Pager<Project> getProjectPage(int pageNo, int pageSize, String searchKey) {
        POCondition condition = new POCondition();
        if(!"".equals(searchKey)) {
            condition.addLike("name", searchKey);
        }
        condition.addOrderDesc("syncTime");
        return projectDao.pagePo(Project.class, condition, pageNo, pageSize);
    }

    @Override
    public Project getProjectByPlatformProjectId(String platformProjectId) {
        POCondition condition = new POCondition();
        condition.addEQ("platformProjectId", platformProjectId);
        List<Project> projectList = projectDao.findPoList(Project.class, condition);
        if(projectList.size() != 1) {
            return null;
        }
        return projectList.get(0);
    }


}
