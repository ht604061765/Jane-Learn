package com.tenfine.napoleon.framework.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * 属性文件操作工具类
 */
public class PropsUtil {

	private static final Logger logger = LoggerFactory.getLogger(PropsUtil.class);
	
	/**
	 * 加载属性文件
	 */
	public static Properties loadProps(String propsPath) {
		Properties props = new Properties();
		InputStream is = null;
		try {
			if (StringUtil.isEmpty(propsPath)) {
				throw new IllegalArgumentException();
			}
			String suffix = ".properties";
			if (propsPath.lastIndexOf(suffix) == -1) {
				propsPath += suffix;
			}
			is = ClassUtil.getClassLoader().getResourceAsStream(propsPath);
			if (is != null) {
				props.load(is);
			}
		} catch (Exception e) {
			logger.error("加载属性文件出错！", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				logger.error("释放资源出错！", e);
			}
		}
		return props;
	}

	/**
	 * 加载属性文件，并转为 Map
	 */
	public static Map<String, String> loadPropsToMap(String propsPath) {
		Map<String, String> map = new HashMap<String, String>();
		Properties props = loadProps(propsPath);
		for (String key : props.stringPropertyNames()) {
			map.put(key, props.getProperty(key));
		}
		return map;
	}

	/**
	 * 
	 * @param propsPath
	 *            文件路径
	 * @param key
	 *            属性的key
	 * @return
	 */
	public static String getPropsValue(String propsPath, String key) {
		String value = "";
		Properties props = loadProps(propsPath);
		if (props.containsKey(key)) {
			value = props.getProperty(key);
		}
		return value;
	}

	/**
	 * 更新配置文件
	 * 
	 * @param propsPath
	 *            文件路径
	 * @param keyname
	 *            属性的key
	 * @param keyvalue
	 *            属性的值
	 */
	public static void updateProperty(String propsPath, String keyname, String keyvalue) {
		FileOutputStream outputFile = null;
		Properties properties = loadProps(propsPath);
		try {
			properties.setProperty(keyname, keyvalue);
			String path = ClassUtil.getClassLoader().getResource(propsPath).getPath();
			outputFile = new FileOutputStream(path);
			properties.store(outputFile, "modify");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputFile != null) {
					outputFile.flush();
					outputFile.close();
				}
			} catch (IOException e) {
				logger.error("释放资源出错！", e);
			}
		}
	}

	public static void main(String args[]) {
		System.out.println("Hello World!");
		System.out.println(PropsUtil.getPropsValue("application-dev.properties", "test"));
		PropsUtil.updateProperty("application-dev.properties", "test", "我爱中国");

	}

}

