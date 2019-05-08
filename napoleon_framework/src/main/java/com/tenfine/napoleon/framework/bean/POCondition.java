package com.tenfine.napoleon.framework.bean;

import com.tenfine.napoleon.framework.dao.SqlHelper;
import com.tenfine.napoleon.framework.exception.FrameworkRuntimeException;
import com.tenfine.napoleon.framework.util.ArrayUtil;
import com.tenfine.napoleon.framework.util.ClassUtil;
import com.tenfine.napoleon.framework.util.StringUtil;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by danan on 2017/6/20.
 */
public class POCondition {

    private Map<String, Object> conditionMap = new HashMap<String, Object>();

    /**
     * 查询sql
     */
    private StringBuilder whereSqlBuider = new StringBuilder(" where 1=1 ");

    /**
     * 排序依据
     */
    private String orders = "";

    public Map<String, Object> getConditionMap() {
        return conditionMap;
    }

    /**
     * 增加升序规则
     *
     * @param attrCode 排序字段
     */
    public void addOrderAsc(String attrCode) {
        addOrder(attrCode, "asc");
    }

    /**
     * 增加降序规则
     *
     * @param attrCode 排序字段
     */
    public void addOrderDesc(String attrCode) {
        addOrder(attrCode, "desc");
    }

    private void addOrder(String attrCode, String asc) {
        if (!(StringUtils.hasText("order by") && StringUtils.hasText(orders))) {
            orders = "order by " + StringUtil.camelhumpToUnderline(StringUtil.firstToLower(attrCode)) + " " + asc;
        } else {
            orders += ", " + StringUtil.camelhumpToUnderline(StringUtil.firstToLower(attrCode)) + " " + asc;
        }
    }

    /**
     * 加入Between 查询条件
     *
     * @param fieldName 字段名称
     * @param minValue  下限值
     * @param maxValue  上限值
     */
    public void addBetween(String fieldName, Object minValue, Object maxValue) {
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        Assert.notNull(minValue, "minValue cant not be null");
        Assert.notNull(maxValue, "maxValue cant not be null");
        whereSqlBuider.append(" and ").append(StringUtil.camelhumpToUnderline(realFieldName)).append(" between :")
                .append(realFieldName).append("_1 and :").append(StringUtil.camelhumpToUnderline(realFieldName))
                .append("_2 ");
        this.conditionMap.put(realFieldName + "_1", minValue);
        this.conditionMap.put(realFieldName + "_2", maxValue);
    }

    /**
     * 加入自定义 like查询条件 不会自动增加% %
     *
     * @param fieldName 字段名称
     * @param obj       like值
     */
    public void addLeftLike(String fieldName, String obj) {
        Assert.notNull(obj, "para obj cant not be null");
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        whereSqlBuider.append(" and ").append(realFieldName).append(" like :").append(realFieldName).append("_1||'%' ");
        this.conditionMap.put(realFieldName + "_1", obj);

    }

    /**
     * 加入自定义 like查询条件 不会自动增加% %
     *
     * @param fieldName 字段名称
     * @param obj       like值
     */
    public void addRightLike(String fieldName, String obj) {
        Assert.notNull(obj, "para obj cant not be null");
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        whereSqlBuider.append(" and ").append(realFieldName).append(" like '%'||:").append(realFieldName).append("_1 ");
        this.conditionMap.put(realFieldName + "_1", obj);

    }

    /**
     * 加入自定义not like查询条件,不会自动增加% %
     *
     * @param fieldName 字段名称
     * @param obj       like值
     */
    public void addNotLeftLike(String fieldName, String obj) {
        Assert.notNull(obj, "para obj cant not be null");
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        whereSqlBuider.append(" and ").append(realFieldName).append(" not like :").append(realFieldName).append("_1||'%' ");
        this.conditionMap.put(realFieldName + "_1", obj);

    }

    /**
     * 加入自定义not like查询条件,不会自动增加% %
     *
     * @param fieldName 字段名称
     * @param obj       like值
     */
    public void addNotRightLike(String fieldName, String obj) {
        Assert.notNull(obj, "para obj cant not be null");
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        whereSqlBuider.append(" and ").append(realFieldName).append(" not like '%'||:").append(realFieldName).append("_1 ");
        this.conditionMap.put(realFieldName + "_1", obj);

    }

    /**
     * 加入等于条件
     *
     * @param fieldName 字段名称
     * @param obj       like值
     */
    public void addEQ(String fieldName, Object obj) {
        Assert.notNull(obj, "para obj cant not be null");
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        whereSqlBuider.append(" and ").append(StringUtil.camelhumpToUnderline(realFieldName)).append(" =:")
                .append(realFieldName).append("_1 ");
        this.conditionMap.put(realFieldName + "_1", obj);
    }
    
    
    /**
     * 加入或者等于条件
     * @param fieldName
     * @param obj
     */
    public void addOrEQ(String fieldName, Object obj) {
        Assert.notNull(obj, "para obj cant not be null");
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        whereSqlBuider.append(" or ").append(StringUtil.camelhumpToUnderline(realFieldName)).append(" =:")
                .append(realFieldName).append("_1 ");
        this.conditionMap.put(realFieldName + "_1", obj);
    }

    /**
     * 加入大于等于条件
     *
     * @param fieldName 字段名称
     * @param obj       值
     */
    public void addGE(String fieldName, Object obj) {
        Assert.notNull(obj, "para obj cant not be null");
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        whereSqlBuider.append(" and ").append(StringUtil.camelhumpToUnderline(realFieldName)).append(" >= :")
                .append(realFieldName).append("_GE ");
        this.conditionMap.put(realFieldName + "_GE", obj);
    }

    /**
     * 加入大于查询条件
     *
     * @param fieldName 字段名称
     * @param obj       值
     */
    public void addGT(String fieldName, Object obj) {
        Assert.notNull(obj, "para obj cant not be null");
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        whereSqlBuider.append(" and ").append(StringUtil.camelhumpToUnderline(realFieldName)).append(" > :")
                .append(realFieldName).append("_1 ");
        this.conditionMap.put(realFieldName + "_1", obj);
    }

    /**
     * 加入in查询条件
     *
     * @param fieldName 字段名称
     * @param values    值集合
     */
    public void addIn(String fieldName, Object[] values) {
        if (ArrayUtil.isEmpty(values)) {
            throw new FrameworkRuntimeException("IN中值不能为空");
        }
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        whereSqlBuider.append(" and ").append(realFieldName).append(" in ");
        Class<?> objClazz = values[0].getClass();
        if (ClassUtil.isString(objClazz)) {
            String[] vals = (String[]) values;
            whereSqlBuider.append(SqlHelper.generateIN(vals));
        } else if (ClassUtil.isLong(objClazz)) {
            Long[] vals = (Long[]) values;
            whereSqlBuider.append(SqlHelper.generateIN(vals));
        }
    }

    /**
     * 加入空或非空条件
     *
     * @param fieldName 字段名称
     * @param isNull    是否为空,true表示为空,false表示非空
     */
    public void addIsNull(String fieldName, boolean isNull) {
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        if (isNull) {
            whereSqlBuider.append(" and ").append(realFieldName).append(" is null ");
        } else {
            whereSqlBuider.append(" and ").append(realFieldName).append(" is not null ");
        }
    }

    /**
     * 加入小于等于条件
     *
     * @param fieldName 字段名称
     * @param obj       值
     */
    public void addLE(String fieldName, Object obj) {
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        Assert.notNull(obj, "para obj cant not be null");
        whereSqlBuider.append(" and ").append(realFieldName).append(" <= :").append(realFieldName).append("_LE");
        this.conditionMap.put(realFieldName + "_LE", obj);
    }

    /**
     * 加入like查询条件
     *
     * @param fieldName 字段名称
     * @param obj       like值
     */
    public void addLike(String fieldName, String obj) {
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        Assert.notNull(obj, "para obj cant not be null");
		String likeStr = "'%" + obj + "%'";
        whereSqlBuider.append(" and ").append(realFieldName).append(" like ").append(likeStr);
        this.conditionMap.put(realFieldName, obj);
    }

    
    /**
     * 加入like查询条件（多个字段同时like一个值）,查询时自动加上前后的% %
     *
     * @param fields 多个字段名称
     * @param objs   like值
     */
    public void addOrLike(ArrayList<String> fields, Object obj) {
        Assert.notNull(fields, "fields obj cant not be null");
        Assert.notNull(obj, "likes obj cant not be null");

        for (int i = 0, n= fields.size(); i < n ; i++) {
            String realFieldName = StringUtil.camelhumpToUnderline(fields.get(i));
            if (i == 0) {
                whereSqlBuider.append(" and (");
            } else {
                whereSqlBuider.append(" or ");
            }
            whereSqlBuider.append(realFieldName).append(" like '%' :").append("paramObj '%'");
        }
        this.conditionMap.put("paramObj", obj);
        whereSqlBuider.append(") ");
    }
    

    /**
     * 加入like查询条件（多个）,查询时自动加上前后的% %
     *
     * @param fieldNames 字段名称
     * @param objs       like值
     */
    public void addLikes(String[] fieldNames, Object[] objs) {
        Assert.notNull(objs, "para obj cant not be null");
        if (fieldNames.length > 0) {
            for (int i = 0; i < fieldNames.length; i++) {
                String realFieldName = StringUtil.camelhumpToUnderline(fieldNames[i]);
                if (i == 0) {
                    whereSqlBuider.append(" and (");
                } else {
                    whereSqlBuider.append(" or ");
                }
                whereSqlBuider.append(realFieldName).append(" like '%'||:").append(realFieldName).append("_1||'%'");
                if (i == fieldNames.length - 1) {
                    whereSqlBuider.append(") ");
                } else {
                    whereSqlBuider.append(" ");
                }
                this.conditionMap.put(realFieldName + "_1", objs[i]);
            }
        }
    }

    /**
     * 加入小于查询条件
     *
     * @param fieldName 字段名称
     * @param obj       值
     */
    public void addLT(String fieldName, Object obj) {
        Assert.notNull(obj, "para obj cant not be null");
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        whereSqlBuider.append(" and ").append(realFieldName).append(" < :").append(realFieldName).append("_LT ");
        this.conditionMap.put(realFieldName + "_LT", obj);
    }

    /**
     * 加入不等于条件
     *
     * @param fieldName 字段名称
     * @param obj       值
     */
    public void addNE(String fieldName, Object obj) {
        Assert.notNull(obj, "para obj cant not be null");
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        whereSqlBuider.append(" and ").append(realFieldName).append(" <> :").append(realFieldName).append("_1 ");
        this.conditionMap.put(realFieldName + "_1", obj);
    }

    /**
     * 加入not between 查询条件
     *
     * @param fieldName 字段名称
     * @param minValue  下限值
     * @param maxValue  上限值
     */
    public void addNotBetween(String fieldName, String minValue, String maxValue) {
        Assert.notNull(minValue, "para minValue cant not be null");
        Assert.notNull(maxValue, "para maxValue cant not be null");
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        whereSqlBuider.append(" and ").append(realFieldName).append(" not between :").append(realFieldName)
                .append("_1 and :").append(realFieldName).append("_2 ");
        this.conditionMap.put(realFieldName + "_1", minValue);
        this.conditionMap.put(realFieldName + "_2", maxValue);
    }

    /**
     * 加入not in查询条件
     *
     * @param fieldName 字段名称
     * @param values    值集合
     */
    public void addNotIn(String fieldName, Object[] values) {
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        whereSqlBuider.append(" and ").append(realFieldName).append(" not in ");
        Class<?> objClazz = values[0].getClass();
        if (ClassUtil.isString(objClazz)) {
            String[] vals = (String[]) values;
            whereSqlBuider.append(SqlHelper.generateIN(vals));
        } else if (ClassUtil.isLong(objClazz)) {
            Long[] vals = (Long[]) values;
            whereSqlBuider.append(SqlHelper.generateIN(vals));
        }
    }

    /**
     * 加入not like查询条件 查询时自动增加% %
     *
     * @param fieldName 字段名称
     * @param obj       like值
     */
    public void addNotLike(String fieldName, Object obj) {
        Assert.notNull(obj, "para obj cant not be null");
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        whereSqlBuider.append(" and ").append(realFieldName).append(" not like '%'||:").append(fieldName).append("_1||'%' ");
        this.conditionMap.put(realFieldName + "_1", obj);
    }

    /**
     * 加入字符串长度相等条件
     *
     * @param fieldName 字段名称
     * @param obj       值
     */
    public void addEQLength(String fieldName, Object obj) {
        Assert.notNull(obj, "para obj cant not be null");
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        whereSqlBuider.append(" and length(").append(realFieldName).append(") = ").append(obj.toString());
    }
    
    /**
     * 加入字符串长度间乎条件
     *
     * @param fieldName 字段名称
     * @param start       值
     * @param end      值
     */
    public void addBetweenLength(String fieldName, Integer start, Integer end) {
        Assert.notNull(start, "para start cant not be null");
        Assert.notNull(end, "para end cant not be null");
        String realFieldName = StringUtil.camelhumpToUnderline(fieldName);
        whereSqlBuider.append(" and length(").append(realFieldName).append(") >= ").append(start.toString());
        whereSqlBuider.append(" and length(").append(realFieldName).append(") <= ").append(end.toString());
    }

    /*
     * (non-Javadoc)
     */
    public String getWhereAndOrderSql() {
        return this.whereSqlBuider.toString() + " " + this.orders;
    }

    public String getWhereSql() {
        return this.whereSqlBuider.toString();
    }

}

