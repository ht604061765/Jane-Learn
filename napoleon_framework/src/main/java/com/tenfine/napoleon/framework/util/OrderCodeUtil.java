package com.tenfine.napoleon.framework.util;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tenfine.napoleon.framework.bean.BaseTreePO;
import com.tenfine.napoleon.framework.bean.POCondition;
import com.tenfine.napoleon.framework.dao.BaseDao;

/**
 * 排序码工具类
 */
public class OrderCodeUtil {
    private Logger logger = LoggerFactory.getLogger(OrderCodeUtil.class);

    /**
     * 生成排序码
     *
     * @param clazz 实体类
     * @param po    实体
     * @param dao   dao层
     * @return 排序码
     */
    public static <PO extends BaseTreePO, DAO extends BaseDao> String createSingleOrderCode(Class<PO> clazz, PO po, DAO dao) {
        POCondition condition = new POCondition();
        String parentOrderCode = "";
        if ("t001".equals(po.getPid())) {
            condition.addEQLength("orderCode", 6);
            condition.addLeftLike("orderCode", "001");
            parentOrderCode = "001";
        } else if ("t002".equals(po.getPid())) {
            condition.addEQLength("orderCode", 6);
            condition.addLeftLike("orderCode", "002");
            parentOrderCode = "002";
        } else {
            PO parentPo = dao.findPo(clazz, po.getPid());
            if (parentPo != null) {
                parentOrderCode = parentPo.getOrderCode();
                if (parentOrderCode != null) {
                    condition.addEQLength("orderCode", parentOrderCode.length() + 3);
                    condition.addLeftLike("orderCode", parentOrderCode);
                    condition.addNotIn("orderCode", new String[]{parentOrderCode});
                }
            } else {
            	parentOrderCode = "001";
                condition.addIsNull("pid", true);
            }
        }
        condition.addOrderAsc("orderCode");
        List<PO> pos = dao.findPoList(clazz, condition);
        String orderCode = "";
        if (pos.size() == 0) {
            orderCode = parentOrderCode + "001";
        } else {
            String maxOrderCode = pos.get(pos.size() - 1).getOrderCode();
            Long orderCodeNum = CastUtil.castLong(maxOrderCode) + 1;
            orderCode = "000" + CastUtil.castString(orderCodeNum);
            orderCode = orderCode.substring(orderCode.length() % 3);
        }
        return orderCode;
    }

    /**
     * 按分类生成排序码
     *
     * @param clazz             实体类
     * @param po                实体
     * @param dao               dao层
     * @param classifyFieldsMap 分类字段
     * @return 排序码
     */
    public static <PO extends BaseTreePO, DAO extends BaseDao> String createSingleOrderCode(Class<PO> clazz, PO po, DAO dao, Map<String, Object> classifyFieldsMap) {
        POCondition condition = new POCondition();
        String parentOrderCode = "";
        if ("t001".equals(po.getPid())) {
            condition.addEQLength("orderCode", 6);
            condition.addLeftLike("orderCode", "001");
            parentOrderCode = "001";
        } else if ("t002".equals(po.getPid())) {
            condition.addEQLength("orderCode", 6);
            condition.addLeftLike("orderCode", "002");
            parentOrderCode = "002";
        } else {
            PO parentPo = dao.findPo(clazz, po.getPid());
            if (parentPo != null) {
                parentOrderCode = parentPo.getOrderCode();
                if (parentOrderCode != null) {
                    condition.addEQLength("orderCode", parentOrderCode.length() + 3);
                    condition.addLeftLike("orderCode", parentOrderCode);
                    condition.addNotIn("orderCode", new String[]{parentOrderCode});
                }
            } else {
            	parentOrderCode = "001";
                condition.addIsNull("pid", true);
            }
        }
        for (String key : classifyFieldsMap.keySet()) {
            if (StringUtil.isEmpty(classifyFieldsMap.get(key) == null ? "" : classifyFieldsMap.get(key).toString())) {
                condition.addIsNull(key, true);
            } else {
                condition.addEQ(key, classifyFieldsMap.get(key));
            }
        }
        condition.addOrderAsc("orderCode");
        List<PO> pos = dao.findPoList(clazz, condition);
        String orderCode = "";
        if (pos.size() == 0) {
            orderCode = parentOrderCode + "001";
        } else {
            String maxOrderCode = pos.get(pos.size() - 1).getOrderCode();
            Long orderCodeNum = CastUtil.castLong(maxOrderCode) + 1;
            orderCode = "000" + CastUtil.castString(orderCodeNum);
            orderCode = orderCode.substring(orderCode.length() % 3);
        }
        return orderCode;
    }

    /**
     * 按分类生成排序码
     *
     * @param clazz             实体类
     * @param po                实体
     * @param dao               dao层
     * @param classifyFieldsMap 分类字段
     * @param num               orderCode位数
     * @return 排序码
     */
    public static <PO extends BaseTreePO, DAO extends BaseDao> String createSingleOrderCode(Class<PO> clazz, PO po, DAO dao, Map<String, Object> classifyFieldsMap, Integer num) {
        POCondition condition = new POCondition();
        String orderCodePrefix = createOrderCodePrefix(num);
        String parentOrderCode = "";
        if ("t001".equals(po.getPid())) {
            condition.addEQLength("orderCode", num * 2);
            condition.addLeftLike("orderCode", orderCodePrefix.concat("1"));
            parentOrderCode = orderCodePrefix.concat("1");
        } else if ("t002".equals(po.getPid())) {
            condition.addEQLength("orderCode", num * 2);
            condition.addLeftLike("orderCode", orderCodePrefix.concat("2"));
            parentOrderCode = orderCodePrefix.concat("2");
        } else {
            PO parentPo = dao.findPo(clazz, po.getPid());
            if (parentPo != null) {
                parentOrderCode = parentPo.getOrderCode();
                if (parentOrderCode != null) {
                    condition.addEQLength("orderCode", parentOrderCode.length() + num);
                    condition.addLeftLike("orderCode", parentOrderCode);
                    condition.addNotIn("orderCode", new String[]{parentOrderCode});
                }
            } else {
                condition.addIsNull("pid", true);
                condition.addEQLength("orderCode", num * 2);
                condition.addLeftLike("orderCode", orderCodePrefix.concat("1"));
                parentOrderCode = orderCodePrefix.concat("1");
            }
        }
        for (String key : classifyFieldsMap.keySet()) {
            if (StringUtil.isEmpty(classifyFieldsMap.get(key) == null ? "" : classifyFieldsMap.get(key).toString())) {
                condition.addIsNull(key, true);
            } else {
                condition.addEQ(key, classifyFieldsMap.get(key));
            }
        }
        condition.addOrderAsc("orderCode");
        List<PO> pos = dao.findPoList(clazz, condition);
        String orderCode = "";
        if (pos.size() == 0) {
            orderCode = parentOrderCode + orderCodePrefix + "1";
        } else {
            String maxOrderCode = pos.get(pos.size() - 1).getOrderCode();
            Long orderCodeNum = CastUtil.castLong(maxOrderCode) + 1;
            orderCode = "0" + orderCodePrefix + CastUtil.castString(orderCodeNum);
            orderCode = orderCode.substring(orderCode.length() % num);
        }
        return orderCode;
    }

    /**
     * 生成orderCode前缀 （例如：4位orderCode生成"000"）
     *
     * @param num orderCode位数
     */
    private static String createOrderCodePrefix(Integer num) {
        String orderCodePrefix = "";
        for (int i = 1; i < num; i++) {
            orderCodePrefix = orderCodePrefix.concat("0");
        }
        return orderCodePrefix;
    }
}

