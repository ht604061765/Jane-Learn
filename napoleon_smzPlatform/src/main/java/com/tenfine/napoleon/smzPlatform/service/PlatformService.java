package com.tenfine.napoleon.smzPlatform.service;

import java.util.List;

import com.tenfine.napoleon.framework.bean.Pager;
import com.tenfine.napoleon.smzPlatform.dao.po.Platform;
import com.tenfine.napoleon.smzPlatform.dao.po.Project;
import com.tenfine.napoleon.user.dao.po.Device;

/**
 * @author Hunter
 */
public interface PlatformService {
	/**
	 * 批量修改项目的832提交时间
	 * @param projectList
	 */
	void changeUploadTimeBatch(List<Project> projectList);

	boolean checkProject(String userId, String deviceNo, int proId, String proName);
	
	Platform getPlatformById(String id);
	
	Platform testAddPlatform();

    Pager<Platform> getPlatformList(int pageNo, int pageSize, String searchKey);
	
	Project getPlatformProject(String platformProId, String platformId);
	
	List<Project> getAllProject();
}
