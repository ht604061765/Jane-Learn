package com.tenfine.napoleon.framework.util;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tenfine.napoleon.framework.bean.BaseBean;

/**
 * 对象操作工具类
 */
public class ObjectUtil {

	private static final Logger logger = LoggerFactory.getLogger(ObjectUtil.class);

	/**
	 * 设置成员变量
	 */
	public static void setField(Object obj, String fieldName, Object fieldValue) {
		try {
			if (PropertyUtils.isWriteable(obj, fieldName)) {
				PropertyUtils.setProperty(obj, fieldName, fieldValue);
			}
		} catch (Exception e) {
			logger.error("设置成员变量出错！", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取成员变量
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		Object propertyValue = null;
		try {
			if (PropertyUtils.isReadable(obj, fieldName)) {
				propertyValue = PropertyUtils.getProperty(obj, fieldName);
			}
		} catch (Exception e) {
			logger.error("获取成员变量出错！", e);
			throw new RuntimeException(e);
		}
		return propertyValue;
	}

	/**
	 * 复制所有成员变量
	 */
	public static void copyFields(Object source, Object target) {
		try {
			for (Field field : source.getClass().getDeclaredFields()) {
				// 若不为 static 成员变量，则进行复制操作
				if (!Modifier.isStatic(field.getModifiers())) {
					field.setAccessible(true); // 可操作私有成员变量
					field.set(target, field.get(source));
				}
			}
		} catch (Exception e) {
			logger.error("复制成员变量出错！", e);
			throw new RuntimeException(e);
		}
	}

	// public static void copyFieldsSmart(Object source, Object target) {
	// try {
	// for (Field field : ClassUtil.getClassField(source.getClass())) {
	// // 若不为 static 成员变量，则进行复制操作
	// if (!Modifier.isStatic(field.getModifiers())) {
	// Field targetField = getObjectField(target,field.getName());
	// if(targetField!=null){
	// targetField.setAccessible(true); // 可操作私有成员变量
	// targetField.set(target, ObjectUtil.getFieldValue(source,
	// field.getName()));
	// }
	// }
	// }
	// } catch (Exception e) {
	// logger.error("复制成员变量出错！", e);
	// throw new RuntimeException(e);
	// }
	// }

	public static Field getObjectField(Object obj, String fieldName) {
		try {
			Field f = obj.getClass().getDeclaredField(fieldName);
			return f;
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		}
		return null;
	}

	/**
	 * 通过反射创建实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String className) {
		T instance;
		try {
			Class<?> commandClass = ClassUtil.loadClass(className);
			instance = (T) commandClass.newInstance();
		} catch (Exception e) {
			logger.error("创建实例出错！", e);
			throw new RuntimeException(e);
		}
		return instance;
	}

	/**
	 * 获取对象的字段映射（字段名 => 字段值），包含父类属性，忽略 static 字段
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> getFieldValueMap(Object obj) {
		return getFieldValueMap(obj, true, false, true);
	}

	/**
	 * 获取对象的字段映射（字段名 => 字段值），包含父类属性，忽略 static 字段。排除空值或NULL的字段
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> getFieldValueMapWithoutEmpty(Object obj) {
		return getFieldValueMap(obj, true, false, false);
	}

	/**
	 * 获取对象的字段映射（字段名 => 字段值）
	 * 
	 * @param obj
	 *            对象
	 * @param isStaticIgnored
	 *            是否类属性忽略
	 * @param isSuperFieldIgnored
	 *            是否父类字段忽略
	 * @return
	 */
	public static Map<String, Object> getFieldValueMap(Object obj, boolean isStaticIgnored, boolean isSuperFieldIgnored,
			boolean isContainEmptyField) {
		Map<String, Object> fieldMap = new LinkedHashMap<String, Object>();
		Field[] fields;
		if (isSuperFieldIgnored) {
			fields = obj.getClass().getDeclaredFields();
		} else {
			fields = ClassUtil.getClassField(obj.getClass());
		}

		for (Field field : fields) {
			if (isStaticIgnored && Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			String fieldName = field.getName();
			Object fieldValue = ObjectUtil.getFieldValue(obj, fieldName);
			if (isContainEmptyField) {
				fieldMap.put(fieldName, fieldValue);
			} else {
				if (fieldValue == null
						|| (fieldValue instanceof String && StringUtil.isEmpty(fieldValue.toString().trim()))) {
					continue;
				} else {
					fieldMap.put(fieldName, fieldValue);
				}
			}
		}
		return fieldMap;
	}

	public static <T extends BaseBean> String obj2Str(T obj) {
		StringBuilder builder = new StringBuilder();
		Map<String, Object> map = getFieldValueMap(obj);
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			builder.append(",").append(entry.getKey()).append("=").append(entry.getValue());
		}
		return builder.toString();
	}
}

