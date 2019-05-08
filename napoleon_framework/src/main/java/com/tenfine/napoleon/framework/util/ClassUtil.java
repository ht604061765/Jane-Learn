package com.tenfine.napoleon.framework.util;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类操作工具类
 */
public class ClassUtil {

	private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);

	/**
	 * 获取类加载器
	 */
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * 获取类路径
	 */
	public static String getClassPath() {
		String classpath = "";
		URL resource = getClassLoader().getResource("");
		if (resource != null) {
			classpath = resource.getPath();
		}
		return classpath;
	}

	/**
	 * 加载类（将自动初始化）
	 */
	public static Class<?> loadClass(String className) {
		return loadClass(className, true);
	}

	/**
	 * 加载类
	 */
	public static Class<?> loadClass(String className, boolean isInitialized) {
		Class<?> cls;
		try {
			cls = Class.forName(className, isInitialized, getClassLoader());
		} catch (ClassNotFoundException e) {
			logger.error("加载类出错！", e);
			throw new RuntimeException(e);
		}
		return cls;
	}

	/**
	 * 是否为 int 类型（包括 Integer 类型）
	 */
	public static boolean isInt(Class<?> type) {
		return type.equals(int.class) || type.equals(Integer.class);
	}

	/**
	 * 是否为 long 类型（包括 Long 类型）
	 */
	public static boolean isLong(Class<?> type) {
		return type.equals(long.class) || type.equals(Long.class);
	}

	/**
	 * 是否为 double 类型（包括 Double 类型）
	 */
	public static boolean isDouble(Class<?> type) {
		return type.equals(double.class) || type.equals(Double.class);
	}

	/**
	 * 是否为 String 类型
	 */
	public static boolean isString(Class<?> type) {
		return type.equals(String.class);
	}

	/**
	 * 获取类的属性（含父类属性，不含static类属性）
	 * 
	 * @param clazz
	 * @return
	 */
	public static Field[] getClassField(Class<?> clazz) {
		List<Field> fieldList = new ArrayList<Field>();
		getFieldList(fieldList, clazz);
		int fieldSize = fieldList.size();
		Set<Field> set = CollectionUtil.listToSet(fieldList);
		Field[] fields = set.toArray(new Field[fieldSize]);
		//Field[] fields = fieldList.toArray(new Field[fieldSize]);
		return fields;
	}
	
	/**
	 * 获取类的属性（含父类属性，不含static类属性）
	 * 
	 * @param clazz
	 * @return
	 */
	public static Field[] getFields(Class<?> clazz) {
		List<Field> fieldList = new ArrayList<Field>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			if (!Modifier.isStatic(f.getModifiers()))
				fieldList.add(f);
		}
		
		int fieldSize = fieldList.size();
		Set<Field> set = CollectionUtil.listToSet(fieldList);
		Field[] nonStaticFields = set.toArray(new Field[fieldSize]);
		//Field[] fields = fieldList.toArray(new Field[fieldSize]);
		return nonStaticFields;
	}

	private static void getFieldList(List<Field> fieldList, Class<?> clazz) {
		try {
			Field[] fields = clazz.getDeclaredFields();
			for (Field f : fields) {
				if (!Modifier.isStatic(f.getModifiers()))
					fieldList.add(f);
			}
			Class<?> superClazz = clazz.getSuperclass();
			getFieldList(fieldList, superClazz);
		} catch (Exception e) {

		}
	}
}
