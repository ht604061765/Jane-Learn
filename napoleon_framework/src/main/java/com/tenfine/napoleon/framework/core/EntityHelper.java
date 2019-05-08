package com.tenfine.napoleon.framework.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tenfine.napoleon.framework.annotation.Column;
import com.tenfine.napoleon.framework.annotation.Entity;
import com.tenfine.napoleon.framework.annotation.Table;
import com.tenfine.napoleon.framework.util.ArrayUtil;
import com.tenfine.napoleon.framework.util.MapUtil;
import com.tenfine.napoleon.framework.util.StringUtil;

/**
 * 初始化 Entity 结构（暂不用，日后优化性能再说）
 */
@Deprecated
public class EntityHelper {
	private static final Logger logger = LoggerFactory.getLogger(EntityHelper.class);
    /**
     * 实体类 => 表名
     */
    private static final Map<Class<?>, String> entityClassTableNameMap = new HashMap<Class<?>, String>();

    /**
     * 实体类 => (字段名 => 列名)
     */
    private static final Map<Class<?>, Map<String, String>> entityClassFieldMapMap = new HashMap<Class<?>, Map<String, String>>();

    static {
        // 获取并遍历所有实体类
        List<Class<?>> entityClassList = ClassHelper.getClassListByAnnotation(Entity.class);
        for (Class<?> entityClass : entityClassList) {
        	initEntityNameMap(entityClass);
        	initEntityFieldMapMap(entityClass);
        }
    }

    /**
     * 初始化产生@Entity的实体与表关联关系
     * 
     * @param entityClass
     */
    private static void initEntityNameMap(Class<?> entityClass) {
        // 判断该实体类上是否存在 Table 注解
        String tableName;
        if (entityClass.isAnnotationPresent(Table.class)) {
            // 若已存在，则使用该注解中定义的表名
            tableName = entityClass.getAnnotation(Table.class).value();
        } else {
            // 若不存在，则将实体类名转换为下划线风格的表名
            tableName = StringUtil.camelhumpToUnderline(entityClass.getSimpleName());
        }
        logger.info("Entity: " + entityClass.getSimpleName()+" ==> " +tableName);
        entityClassTableNameMap.put(entityClass, tableName);
    }

    private static void initEntityFieldMapMap(Class<?> entityClass) {
        // 获取并遍历该实体类中所有的字段（不包括父类中的方法）
        Field[] fields = entityClass.getDeclaredFields();
        if (ArrayUtil.isNotEmpty(fields)) {
            // 创建一个 fieldMap（用于存放列名与字段名的映射关系）
            Map<String, String> fieldMap = new HashMap<String, String>();
            for (Field field : fields) {
            	if(Modifier.isStatic(field.getModifiers()))
            		continue;
                String fieldName = field.getName();
                String columnName;
                // 判断该字段上是否存在 Column 注解
                if (field.isAnnotationPresent(Column.class)) {
                    // 若已存在，则使用该注解中定义的列名
                    columnName = field.getAnnotation(Column.class).value();
                } else {
                    // 若不存在，则将字段名转换为下划线风格的列名
                    columnName = StringUtil.camelhumpToUnderline(fieldName);
                }
                logger.info("FieldName: " + fieldName + " ==> " + columnName);
                fieldMap.put(fieldName, columnName);
            }
            entityClassFieldMapMap.put(entityClass, fieldMap);
        }
    }

    /**
     * 获取实体类对应的表名
     * 
     * @param entityClass
     * @return
     */
    public static String getTableName(Class<?> entityClass) {
        return entityClassTableNameMap.get(entityClass);
    }

    /**
     * 获取实体类map<属性名，字段名>
     * 
     * @param entityClass
     * @return
     */
    public static Map<String, String> getFieldMap(Class<?> entityClass) {
        return entityClassFieldMapMap.get(entityClass);
    }

    /**
     * 获取实体类map<字段名，属性名>
     * 
     * @param entityClass
     * @return
     */
    public static Map<String, String> getColumnMap(Class<?> entityClass) {
        return MapUtil.invert(getFieldMap(entityClass));
    }

    /**
     * 获取实体类属性对应的字段名称
     * 
     * @param entityClass
     * @param fieldName
     * @return
     */
    public static String getColumnName(Class<?> entityClass, String fieldName) {
        String columnName = getFieldMap(entityClass).get(fieldName);
        return StringUtil.isNotEmpty(columnName) ? columnName : fieldName;
    }
}

