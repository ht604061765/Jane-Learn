package com.tenfine.napoleon.framework.cache;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tenfine.napoleon.framework.exception.FrameworkRuntimeException;
import com.tenfine.napoleon.framework.util.ClassUtil;
import com.tenfine.napoleon.framework.util.StringUtil;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Version;

/**
 * beetl功能与Freemarker功能比较 http://javamonkey.iteye.com/blog/1573346
 */
public class SqlCache {
	private static final String BASE_PACKAGE = "com.tenfine";
	private static final Logger logger = LoggerFactory.getLogger(SqlCache.class);
	// sqlId => SQL
	private static final Map<String, String> SQLID_SQL_CACHE = new HashMap<String, String>();

	private static final StringTemplateLoader TPL_LOADER = new StringTemplateLoader();

	public static final freemarker.template.Configuration cfg = new freemarker.template.Configuration(
			new Version("2.3.26-incubating"));

	public static void init() {
		// 第一步：解析XML将其放入内存
		initSqlIdSQLMap();
		cfg.setTemplateLoader(TPL_LOADER);
	}

	private static void initSqlIdSQLMap() {
		// 解析XML将其放入内存
		logger.info("开始扫描 SQL XML 文件......");
		long begin = System.currentTimeMillis();
		List<String> list = traversalXMLFile();

		for (String filePath : list) {
			parseSQLXML(filePath);
		}
		logger.info("结束扫描 SQL XML文件，共耗时 [{}] ms!", System.currentTimeMillis() - begin);
	}

	public static void parseSQLXML(String filePath) {
		logger.info("开始解析文件 [{}]", filePath);
//		InputStream in = ClassLoader.getSystemResourceAsStream(filePath);
		InputStream in= new SqlCache().getClass().getResourceAsStream("/"+filePath);
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(in);
			Element root = doc.getRootElement();
			Element el;
			String id = "", clazz = "", key = "", value = "";
			for (Iterator<?> i = root.elementIterator("sql"); i.hasNext();) {
				el = (Element) i.next();
				id = el.attributeValue("id");
				clazz = el.attributeValue("clazz");
				if (StringUtil.isNotEmpty(clazz)) {
					key = clazz + "-" + id;
				} else {
					key = id;
				}
				value = el.getTextTrim();
				if (StringUtil.isEmpty(key) || StringUtil.isEmpty(value)) {
					logger.error("文件 [{}]存在空SqlId或空Sql，请检查！！！", filePath);
				} else {
					if (SQLID_SQL_CACHE.containsKey(key)) {
						logger.error("文件 [{}]中sqlId={}在其他位置已存在，请检查！！！", filePath, key);
					}
					SQLID_SQL_CACHE.put(key, value);
					TPL_LOADER.putTemplate(key, value);
				}
			}

		} catch (Exception e) {
			logger.error("解析文件过程中存在异常！！！", e);
			throw new FrameworkRuntimeException("解析文件过程中存在异常", e);
		}
	}

	private static List<String> traversalXMLFile() {
		List<String> xmlPathList = new ArrayList<String>();
		try {
			// 从包名获取 URL 类型的资源
			Enumeration<URL> urls = ClassUtil.getClassLoader().getResources(BASE_PACKAGE.replace(".", "/"));
			// 遍历 URL 资源
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if (url != null) {
					// 获取协议名（分为 file 与 jar）
					String protocol = url.getProtocol();

					if (protocol.equals("file")) {
						// 若在 class 目录中，则执行添加类操作
						// String packagePath = url.getPath().replaceAll("%20",
						// " ");
						String packagePath = url.getPath();
						addXMLFile(xmlPathList, packagePath, BASE_PACKAGE);
					} else if (protocol.equals("jar")) {
						// 若在 jar 包中，则解析 jar 包中的 entry
						JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
						JarFile jarFile = jarURLConnection.getJarFile();
						Enumeration<JarEntry> jarEntries = jarFile.entries();
						while (jarEntries.hasMoreElements()) {
							JarEntry jarEntry = jarEntries.nextElement();
							String jarEntryName = jarEntry.getName();
							// 判断该 entry 是否为 xml且排除pom.xml
							if (jarEntryName.endsWith("Qo.xml")) {
								// 获取类名
								xmlPathList.add(jarEntryName);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("扫描文件过程中存在异常！！！", e);
			throw new FrameworkRuntimeException("扫描文件过程中存在异常", e);
		}
		return xmlPathList;
	}

	private static void addXMLFile(List<String> xmlPathList, String packagePath, String packageName) {
		try {
			// 获取包名路径下的 xml 文件或目录
			File[] files = new File(packagePath).listFiles(new FileFilter() {
				public boolean accept(File file) {
					return (file.isFile() && file.getName().endsWith("Qo.xml")) || file.isDirectory();
				}
			});
			// 遍历文件或目录
			if (files != null) {
				for (File file : files) {
					String fileName = file.getName();
					// 判断是否为文件或目录
					if (file.isFile()) {// 如果是文件
						String path = file.getPath();
						int endIndex = path.length();
						path = path.substring(path.indexOf(BASE_PACKAGE.replace(".", File.separator)), endIndex);
						xmlPathList.add(path.replace("\\", "/"));
					} else {// 如果是目录
						// 获取子包
						String subPackagePath = fileName;
						if (StringUtil.isNotEmpty(packagePath)) {
							subPackagePath = packagePath + "/" + subPackagePath;
						}
						// 子包名
						String subPackageName = fileName;
						if (StringUtil.isNotEmpty(packageName)) {
							subPackageName = packageName + "." + subPackageName;
						}
						// 递归调用
						addXMLFile(xmlPathList, subPackagePath, subPackageName);
					}
				}
			}
		} catch (Exception e) {
			logger.error("扫描文件过程中存在异常！！！", e);
			throw new FrameworkRuntimeException("扫描文件过程中存在异常", e);
		}
	}

	/**
	 * 根据 SQLID 获取 SQL 语句
	 * 
	 * @param sqlId
	 * @return
	 */
	public static String getSql(String sqlId) {
		String sql;
		if (SQLID_SQL_CACHE.containsKey(sqlId)) {
			sql = SQLID_SQL_CACHE.get(sqlId);
		} else {
			logger.error("SQLID={}，对应的SQL未获取到，请检查并重启应用！！！", sqlId);
			throw new RuntimeException("SQLID=" + sqlId + "对应的SQL未获取到，请检查并重启应用！！！");
		}
		return sql;
	}
}

