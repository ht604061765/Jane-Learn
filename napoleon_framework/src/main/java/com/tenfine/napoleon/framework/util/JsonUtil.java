package com.tenfine.napoleon.framework.util;

import net.sf.json.JSONArray;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON 操作工具类
 */
public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将 Java 对象转为 JSON 字符串
     */
    public static <T> String toJSON(T obj) {
        String jsonStr;
        try {
            jsonStr = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("Java对象 转 JSON 出错！", e);
            throw new RuntimeException(e);
        }
        return jsonStr;
    }

    /**
     * 将 JSON 字符串转为 Java 对象
     */
    public static <T> T fromJSON(String json, Class<T> type) {
        T obj;
        try {
            obj = objectMapper.readValue(json, type);
        } catch (Exception e) {
            logger.error("JSON 转 Java对象 出错！", e);
            throw new RuntimeException(e);
        }
        return obj;
    }

    //返回 list 对象数组 字符串 JSON 要添加[]
    public static Object json2ObjectList(String strJson, @SuppressWarnings("rawtypes")Class beanClass) {
        return JSONArray.toCollection(JSONArray.fromObject(strJson), beanClass);
    }
}

