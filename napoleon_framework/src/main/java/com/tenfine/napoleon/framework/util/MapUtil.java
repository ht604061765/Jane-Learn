package com.tenfine.napoleon.framework.util;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.MapUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 映射操作工具类
 */
public class MapUtil {

    /**
     * 判断 Map 是否非空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return MapUtils.isNotEmpty(map);
    }

    /**
     * 判断 Map 是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return MapUtils.isEmpty(map);
    }

    /**
     * 转置 Map
     */
    public static <K, V> Map<V, K> invert(Map<K, V> source) {
        Map<V, K> target = null;
        if (isNotEmpty(source)) {
            target = new LinkedHashMap<V, K>(source.size());
            for (Map.Entry<K, V> entry : source.entrySet()) {
                target.put(entry.getValue(), entry.getKey());
            }
        }
        return target;
    }

    /**
     * 将bean对象转换为map对象
     *
     * @param bean bean对象
     * @return map对象
     */
    public static Map<String, Object> beanToMap(final Object bean) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(bean);
            for (PropertyDescriptor descriptor : descriptors) {
                String name = descriptor.getName();
                if (!"class".equals(name)) {
                    map.put(name, propertyUtilsBean.getNestedProperty(bean, name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 将map对象转换为bean对象
     *
     * @param map   map对象
     * @param clazz bean对象类
     * @return bean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        T bean = null;
        try {
            bean = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            BeanUtils.populate(bean, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * 将List<bean>转换为List<map>对象
     *
     * @param beans List<bean>对象
     * @return List<Map>对象
     */
    public static <T> List<Map<String, Object>> beansToMaps(List<T> beans) {
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        if (beans != null && beans.size() > 0) {
            for (T bean : beans) {
                maps.add(beanToMap(bean));
            }
        }
        return maps;
    }

    /**
     * 将List<map>转换为List<T>
     *
     * @param maps  List<map>对象
     * @param clazz bean对象类
     */
    public static <T> List<T> mapsToBeans(List<Map<String, Object>> maps, Class<T> clazz) {
        List<T> beans = new ArrayList<T>();
        if (maps != null && maps.size() > 0) {
            for (Map<String, Object> map : maps) {
                beans.add(mapToBean(map, clazz));
            }
        }
        return beans;
    }
}

