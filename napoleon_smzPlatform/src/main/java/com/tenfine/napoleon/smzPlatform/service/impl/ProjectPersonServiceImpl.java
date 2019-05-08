package com.tenfine.napoleon.smzPlatform.service.impl;

import com.tenfine.napoleon.framework.bean.POCondition;
import com.tenfine.napoleon.framework.bean.Pager;
import com.tenfine.napoleon.smzPlatform.dao.ProjectPersonDao;
import com.tenfine.napoleon.smzPlatform.service.ProjectPersonService;
import com.tenfine.napoleon.user.dao.po.ProjectPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProjectPersonServiceImpl implements ProjectPersonService {

    @Autowired
    ProjectPersonDao personDao;

    @Override
    public Pager<ProjectPerson> getProjectPersonList(int pageNo, int pageSize, String platformProId, String searchKey) {
        POCondition condition = new POCondition();
        condition.addEQ("platformProId", platformProId);
        if (!"".equals(searchKey)) {
            ArrayList<String> fields = new ArrayList<String>();
            fields.add("platformWorkerName");
            fields.add("idNo");
            condition.addOrLike(fields, searchKey);
        }
        condition.addOrderDesc("createTime");
        return personDao.pagePo(ProjectPerson.class, condition, pageNo, pageSize);
    }



}
