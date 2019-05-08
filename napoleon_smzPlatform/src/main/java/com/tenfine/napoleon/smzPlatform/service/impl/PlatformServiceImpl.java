package com.tenfine.napoleon.smzPlatform.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.tenfine.napoleon.framework.bean.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.tenfine.napoleon.framework.bean.BaseService;
import com.tenfine.napoleon.framework.bean.POCondition;
import com.tenfine.napoleon.framework.util.DateUtil;
import com.tenfine.napoleon.framework.util.IDUtil;
import com.tenfine.napoleon.smzPlatform.dao.PlatformDao;
import com.tenfine.napoleon.smzPlatform.dao.po.Platform;
import com.tenfine.napoleon.smzPlatform.dao.po.Project;
import com.tenfine.napoleon.smzPlatform.service.PlatformService;
import com.tenfine.napoleon.user.dao.po.Device;

/**
 * @author Hunter
 */
@Service("PlatformService")
public class PlatformServiceImpl extends BaseService implements PlatformService{

	private final PlatformDao dao;

	@Autowired
	public PlatformServiceImpl(PlatformDao dao) {
		this.dao = dao;
	}

	@Override
	public void changeUploadTimeBatch(List<Project> projectList) {
		for (Project project : projectList){
			project.setSyncTime(DateUtil.getCurrentDatetime());
		}
		dao.updatePoBatch(projectList);
	}

	@Override
	public Platform getPlatformById(String id) {
		POCondition condition = new POCondition();
		condition.addEQ("id", id);
		List<Platform> platformList = dao.findPoList(Platform.class, condition);
		return platformList.get(0);
	}

	@Override
	public Platform testAddPlatform() {
		Platform newPlatform = new Platform();
		newPlatform.setId(IDUtil.getUUID());
		newPlatform.setCreateTime(DateUtil.getCurrentDatetime());
		newPlatform.setName("测试专用平台");
		newPlatform.setSecret(IDUtil.getUUID16());
		newPlatform.setSocketServer("192.168.41.213");
		newPlatform.setSocketPort("9333");
		newPlatform.setState("1");
		dao.addPo(newPlatform);
		return newPlatform;
	}

	@Override
	public Pager<Platform> getPlatformList(int pageNo, int pageSize, String searchKey) {
	    POCondition condition = new POCondition();
	    if (!"".equals(searchKey)) {
	        condition.addLike("name", searchKey);
        }
		Pager<Platform> platformList = dao.pagePo(Platform.class, condition, pageNo, pageSize);
		return platformList;
	}

	@Override
	public Project getPlatformProject(String platformProId, String platformId) {
		POCondition condition = new POCondition();
		condition.addEQ("platformId", platformId);
		condition.addEQ("platformProjectId", platformProId);
		List<Project> projectList = dao.findPoList(Project.class, condition);
		if(projectList.size() == 1) {
			Project oldProject = projectList.get(0);
			Project newProject = oldProject;
			newProject.setSyncTime(DateUtil.getCurrentDatetime());
			dao.updatePo(newProject);
			return oldProject;
		}
		return null;
	}

	@Override
	public List<Project> getAllProject() {
		POCondition condition = new POCondition();
		condition.addOrderAsc("platformProjectId");
		List<Project> projectList = dao.findPoList(Project.class, condition);
		return projectList;
	}

	@Override
	public boolean checkProject(String userId, String deviceNo, int proId, String proName) {
		Device device = dao.findPo(Device.class, userId);
		// 如果是新项目需要新建项目信息
		POCondition condition = new POCondition();
		condition.addEQ("platformProjectId", proId);
		condition.addEQ("platformId", device.getPlatformId());
		List<Project> projectList = dao.findPoList(Project.class, condition);
		if(projectList.size() == 0) {
			Project project = new Project();
			project.setId(IDUtil.getUUID());
			project.setCreateTime(DateUtil.getCurrentDatetime());
			project.setName(proName);
			project.setPlatformProjectId(String.valueOf(proId));
			project.setPlatformId(device.getPlatformId());
			project.setSyncTime("0");
			dao.addPo(project);
			return true;
		}
		
		return false;
	}

	
	
	
}
