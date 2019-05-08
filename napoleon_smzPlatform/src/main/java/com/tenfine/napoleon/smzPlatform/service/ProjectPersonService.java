package com.tenfine.napoleon.smzPlatform.service;

import com.tenfine.napoleon.framework.bean.Pager;
import com.tenfine.napoleon.user.dao.po.ProjectPerson;

public interface ProjectPersonService {

    Pager<ProjectPerson> getProjectPersonList(int pageNo, int pageSize, String platformProId, String searchKey);

}

