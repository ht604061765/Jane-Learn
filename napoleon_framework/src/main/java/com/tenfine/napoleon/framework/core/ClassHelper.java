package com.tenfine.napoleon.framework.core;


import java.lang.annotation.Annotation;
import java.util.List;

import com.tenfine.napoleon.framework.core.impl.DefaultClassScanner;

/**
 * 根据条件获取相关类
 */
public class ClassHelper {

    /**
     * 获取基础包名
     */
    private static final String BASE_PACKAGE = "com.hunter";

    private static ClassScanner classScanner = new DefaultClassScanner();
    
    /**
     * 获取基础包名中的所有类
     */
    public static List<Class<?>> getClassList() {
        return classScanner.getClassList(BASE_PACKAGE);
    }

    /**
     * 获取基础包名中指定父类或接口的相关类
     */
    public static List<Class<?>> getClassListBySuper(Class<?> superClass) {
        return classScanner.getClassListBySuper(BASE_PACKAGE, superClass);
    }

    /**
     * 获取基础包名中指定注解的相关类
     */
    public static List<Class<?>> getClassListByAnnotation(Class<? extends Annotation> annotationClass) {
        return classScanner.getClassListByAnnotation(BASE_PACKAGE, annotationClass);
    }
}
