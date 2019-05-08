package com.tenfine.napoleon.smzPlatform.service.impl;

import com.tenfine.napoleon.framework.bean.POCondition;
import com.tenfine.napoleon.smzPlatform.dao.PersonInfoDao;
import com.tenfine.napoleon.smzPlatform.service.PersonInfoService;
import com.tenfine.napoleon.user.dao.po.PersonInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    @Autowired
    PersonInfoDao personInfoDao;

    @Override
    public PersonInfo getPersonInfoByidNo(String idNo) {
        POCondition condition = new POCondition();
        condition.addEQ("idNo", idNo);
        List<PersonInfo> personInfoList = personInfoDao.findPoList(PersonInfo.class, condition);
        if (null != personInfoList && personInfoList.size() > 0) {
            return personInfoList.get(0);
        }
        return null;
    }
}
