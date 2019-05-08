/**
 * @author Hunter
 * 2018-1-2 下午3:38:54
 */
package com.tenfine.napoleon.framework.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.tenfine.napoleon.framework.util.PlatfromProperties;

@Configuration
public class ConfigListener implements ServletContextListener{

    // 平台id
    @Value("${platform.id}")
    private String platformId;
    
    // 平台名称
    @Value("${platform.name}")
    private String platformName;

    // socket服务
    @Value("${platform.socketServer}")
    private String socketServer;

    // socket端口
    @Value("${platform.socketPort}")
    private int socketPort;
    
	// 考勤数据上传指令配置
    @Value("${platform.cmd.attendance}")
    private String attendanceCmd;
	
	public void contextInitialized(ServletContextEvent sce) {
		
		PlatfromProperties.setId(this.platformId);
		PlatfromProperties.setName(this.platformName);
		PlatfromProperties.setSocketServer(this.socketServer);
		PlatfromProperties.setSocketPort(this.socketPort);
		PlatfromProperties.setAttendanceCmd(this.attendanceCmd);
	}
	
	public void contextDestroyed(ServletContextEvent sce) {
	}

}


