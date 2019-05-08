package com.tenfine.napoleon.framework.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

public class CollectionUtil {

    /**
     * 判断集合是否非空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return CollectionUtils.isNotEmpty(collection);
    }

    /**
     * 判断集合是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return CollectionUtils.isEmpty(collection);
    }
    
    /**
     * List转Set
     * 
     * @param objs
     * @return
     */
    public static <T> Set<T> listToSet(List<T> objs) {
    	Set<T> dataSet = new HashSet<T>();
    	for(T t : objs){
    		if(t != null)
    			dataSet.add(t);
    	}
    	return dataSet;
    }
    
    /**
     * Set转为数组
     * 
     * @param set
     * @return
     */
    public static Object[] setToArray(Set<?> set){
    	Object[] objs = new Object[set.size()];
    	CollectionUtils.addAll(set, objs);
    	return objs;
    }}
