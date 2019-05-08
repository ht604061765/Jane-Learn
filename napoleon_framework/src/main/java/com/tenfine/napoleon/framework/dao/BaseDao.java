package com.tenfine.napoleon.framework.dao;

import com.tenfine.napoleon.framework.bean.*;
import com.tenfine.napoleon.framework.exception.FrameworkRuntimeException;
import com.tenfine.napoleon.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDao {
    private static final Logger logger = LoggerFactory.getLogger(BaseDao.class);

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Value("${spring.datasource.dbType}")
    private String dbType = "mysql";
    
    /**
     * 根据主键获取实体对象
     *
     * @param clazz 实体类
     * @param id    主键ID
     */
    public <T extends BasePO> T findPo(Class<T> clazz, String id) {
        String tableName = SqlHelper.getTable(clazz);
        StringBuilder sql = new StringBuilder("select * from ").append(tableName).append(" where id = :id");
        printSQL(sql.toString());
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", id);
        List<T> list = this.getListBySql(sql.toString(), paramMap, clazz);
        T obj = null;
        if (CollectionUtil.isNotEmpty(list)) {
            obj = list.get(0);
        }
        return obj;
    }

    /**
     * 获取所有实体对象
     *
     * @param clazz 实体类
     */
    public <T extends BasePO> List<T> findPoList(Class<T> clazz) {
        String tableName = SqlHelper.getTable(clazz);
        StringBuilder sql = new StringBuilder("select * from ").append(tableName);

        Map<String, Object> paramMap = new HashMap<String, Object>();
        return this.getListBySql(sql.toString(), paramMap, clazz);
    }

    /**
     * 获取所有符合条件实体对象
     *
     * @param clazz     实体类
     * @param condition 条件
     */
    public <T extends BasePO> List<T> findPoList(Class<T> clazz, POCondition condition) {
        String tableName = SqlHelper.getTable(clazz);
        String whereSql = condition.getWhereAndOrderSql();
        StringBuilder sql = new StringBuilder("select * from ").append(tableName).append(whereSql);
        return this.getListBySql(sql.toString(), condition.getConditionMap(), clazz);
    }

    /**
     * 获取所有符合条件实体对象（分页）
     *
     * @param clazz 实体类
     */
    public <T extends BasePO> List<T> findPoList(Class<T> clazz, POCondition condition, int pageNo, int pageSize) {

        if (clazz == null) {
            logger.warn("clazz不允许为null");
            return null;
        }
        if (condition == null) {// 提高容错性
            logger.warn("EntityCondition对象为空，框架已为你初始化对象，请检查代码");
            condition = new POCondition();
        }
        String tableName = SqlHelper.getTable(clazz);
        String sql = new StringBuilder("select * from ").append(tableName).toString();

        StringBuilder wholeSQL = new StringBuilder();
        if (dbType.equalsIgnoreCase("mysql")) {
            int pageStart = (pageNo - 1) * pageSize;
            wholeSQL.append("select * from (");
            wholeSQL.append(sql).append(condition.getWhereAndOrderSql()).append(") as a ");
            wholeSQL.append(" limit ").append(pageStart).append(", ").append(pageSize);
        } else if (dbType.equalsIgnoreCase("oracle")) {
            int pageStart = (pageNo - 1) * pageSize + 1;
            int pageEnd = pageStart + pageSize;

			/*
             * select a.* from ( select rownum rn, t.* from 表名 t where 条件 order
			 * by 排序 ) a where a.rn >= 开始位置 and a.rn < 结束位置
			 */
            wholeSQL.append("select a.* from (select rownum rn, t.* from (").append(sql)
                    .append(condition.getWhereAndOrderSql()).append(") t");
            wholeSQL.append(") a where a.rn >= ").append(pageStart).append(" and a.rn < ").append(pageEnd);
        } else if (dbType.equalsIgnoreCase("mssql")) {
            int pageStart = (pageNo - 1) * pageSize;
            /*
             * select * from ( select row_number() over(order by tempcolumn)
			 * temprownumber,* from (select top 开始位置+10 tempcolumn=0,* from
			 * table1)t )tt where temprownumber>开始位置
			 */
            wholeSQL.append("select * from (");
            wholeSQL.append("select row_number() over(order by tempcolumn) temprownumber,* from (");
            wholeSQL.append("select top ").append(pageStart).append("+").append(pageSize).append(" tempcolumn=0,");
            wholeSQL.append("* from (").append(sql).append(condition.getWhereAndOrderSql()).append(") t");
            wholeSQL.append(") tt where temprownumber>").append(pageStart).append(")");
        }

        List<T> recordList = this.getListBySql(wholeSQL.toString(), condition.getConditionMap(), clazz);
        return recordList;
    }


    /**
     * 内部通用查询方法,返回的是装入Qo对象的list列表 运用class-sqlId模式
     *
     * @param sqlId    XxxQo.xml中定义的sqlId
     * @param clazz    XxxQo.xml中定义的clazz
     * @param paramMap 对应SQL的参数
     */
    public <T extends BaseQo> List<T> queryQoList(String sqlId, Class<T> clazz, Map<String, Object> paramMap) {
        String sql = SqlHelper.getExecuteSQL(sqlId, clazz, paramMap);
        return this.getListBySql(sql, paramMap, clazz);
    }

    /**
     * 内部通用查询方法,返回的是装入map对象的list列表 运用class-sqlId模式 map为属性-属性值键值对
     *
     * @param sqlId    XxxQo.xml中定义的sqlId
     * @param paramMap 对应SQL的参数
     */
    public List<Map<String, Object>> queryMapList(String sqlId, Map<String, Object> paramMap) {

        String sql = SqlHelper.getExecuteSQL(sqlId, paramMap);
        return this.getListBySql(sql, paramMap);
    }

    /**
     * 获取查询结果对象记录数
     *
     * @param paramMap 对应SQL的参数
     * @return 对象记录数
     */
    public Integer getRecordCount(String sqlId, Map<String, Object> paramMap) {
        String sql = SqlHelper.getExecuteSQL(sqlId, paramMap);
        StringBuilder sqlbuilder = new StringBuilder("select count(1) from (");
        sqlbuilder.append(sql);
        sqlbuilder.append(") t");
        return namedParameterJdbcTemplate.queryForObject(sqlbuilder.toString(), paramMap, Integer.class);
    }

    /**
     * 获取查询结果对象记录数
     *
     * @param clazz    查询结果类
     * @param paramMap 对应SQL的参数
     */
    public <T extends BaseQo> Integer getRecordCount(String sqlId, Class<T> clazz, Map<String, Object> paramMap) {
        String sql = SqlHelper.getExecuteSQL(sqlId, clazz, paramMap);
        StringBuilder sqlbuilder = new StringBuilder("select count(1) from (");
        sqlbuilder.append(sql);
        sqlbuilder.append(") t");
        return namedParameterJdbcTemplate.queryForObject(sqlbuilder.toString(), paramMap, Integer.class);
    }

    /**
     * 根据条件获取实体对象记录数
     *
     * @param clazz     实体类
     * @param condition 条件对象
     */
    public <T extends BasePO> Integer getRecordCount(Class<?> clazz, POCondition condition) {
        String tableName = SqlHelper.getTable(clazz);
        StringBuilder sqlBuilder = new StringBuilder("select count(1) from ");
        sqlBuilder.append(tableName);
        sqlBuilder.append(condition.getWhereSql());
        printSQL(sqlBuilder.toString());

        return namedParameterJdbcTemplate.queryForObject(sqlBuilder.toString(), condition.getConditionMap(),
                Integer.class);
    }

    /**
     * 内部用：给定一组id（最多支持1000个），获取已存在的id集合
     *
     * @param tableName 表名
     * @param ids       主键ID值数组
     */
    private <T extends BasePO> List<String> getExistIds(String tableName, String[] ids) {
        // todo 需要判断ids的长度，如果过长需要拆分
        if (ids.length > 1000 || ids.length == 0) {
            logger.error("当前待检查的id数量={}，要求：0<id数量<=1000", ids.length);
            new FrameworkRuntimeException("当前待检查的id数量=" + ids.length + "，要求：0<id数量<=1000");
        }

        String idstr = SqlHelper.generateIN(ids);
        String sql = "select id from " + tableName + " where id in " + idstr;
        return namedParameterJdbcTemplate.queryForList(sql, new HashMap<String, Object>(), String.class);
    }

    /**
     * 获取查询结果对象分页对象，注意对应SQL不需要写分页相关片段（支持mysql,oracle,mssql）
     *
     * @param sqlId    sql语句唯一标识
     * @param paramMap 对应SQL的参数
     * @param pageNo   第几页
     * @param pageSize 每页几条
     */
    public <T extends BaseQo> Pager<T> pageQo(String sqlId, Class<T> clazz, Map<String, Object> paramMap, int pageNo,
                                              int pageSize) {
        String sql = SqlHelper.getExecuteSQL(sqlId, clazz, paramMap);
        int totalRecord = getRecordCount(sqlId, clazz, paramMap);
        String wholeSQL = SqlHelper.generateWholeSQL4Pager(dbType, sql, pageNo, pageSize);
        List<T> recordList = this.getListBySql(wholeSQL, paramMap, clazz);
        return new Pager<T>(pageNo, pageSize, totalRecord, recordList);
    }

    /**
     * 获取查询结果对象分页对象，注意对应SQL不需要写分页相关片段（支持mysql,oracle,mssql）
     *
     * @param sqlId    sql语句唯一标识
     * @param paramMap 对应SQL的参数
     * @param pageNo   第几页
     * @param pageSize 每页几条
     */
    public Pager<Map<String, Object>> pageMap(String sqlId, Map<String, Object> paramMap, int pageNo, int pageSize) {
        String sql = SqlHelper.getExecuteSQL(sqlId, paramMap);
        int totalRecord = getRecordCount(sqlId, paramMap);
        String wholeSQL = SqlHelper.generateWholeSQL4Pager(dbType, sql, pageNo, pageSize);
        List<Map<String, Object>> recordList = this.getListBySql(wholeSQL, paramMap);
        return new Pager<Map<String, Object>>(pageNo, pageSize, totalRecord, recordList);
    }

    /**
     * 获取实体的分页对象
     *
     * @param clazz     实体类
     * @param condition 查询条件
     * @param pageNo    第几页
     * @param pageSize  每页几条
     */
    public <T extends BasePO> Pager<T> pagePo(Class<T> clazz, POCondition condition, int pageNo, int pageSize) {
        if (clazz == null) {
            logger.warn("clazz不允许为null");
            return null;
        }
        if (condition == null) {
            logger.warn("EntityCondition对象为空，框架已为你初始化对象，请检查代码");
            condition = new POCondition();
        }
        
        String whereSql = condition.getWhereAndOrderSql();
        
        String sql = SqlHelper.getExecuteSQL(clazz);
        int totalRecord = getRecordCount(clazz, condition);
        String wholeSQL = SqlHelper.generateWholeSQL4Pager(dbType, sql+whereSql, pageNo, pageSize);
        List<T> recordList = this.getListBySql(wholeSQL.toString(), condition.getConditionMap(), clazz);
        
        //List<T> recordList = this.findPoList(clazz, condition, pageNo, pageSize);
        
        return new Pager<T>(pageNo, pageSize, totalRecord, recordList);
    }

    /**
     * 新增实体，如id为空则交给框架赋值
     *
     * @param object 实体对象
     * @return 返回新增记录ID
     */
    public <T extends BasePO> String addPo(T object) {
        if (object == null) {
            logger.warn("实体记录为null");
            throw new FrameworkRuntimeException("实体记录为null");
        }
        if (StringUtil.isEmpty(object.getId())){
            object.setId(IDUtil.getUUID());
        }
        List<T> objects = new ArrayList<T>();
        objects.add(object);
        addPoBatch(objects);
        return object.getId();
    }

    /**
     * 批量新增实体记录，如id无值则交给框架生成（无返回）
     *
     * @param poList 实体记录集合
     */
    public <T extends BasePO> void addPoBatch(List<T> poList) {
        if (CollectionUtil.isEmpty(poList)) {
            logger.warn("实体记录集合为空");
//			throw new FrameworkRuntimeException("实体记录集合为空");
            return;
        }
        Class<? extends BasePO> clazz = poList.get(0).getClass();
        String sql = SqlHelper.getAddSQL(clazz);// 获取SQL
        int arraySize = poList.size();
        SqlParameterSource[] batchArgs = new SqlParameterSource[arraySize];
        for (int i = 0; i < arraySize; i++) {
            T object = poList.get(i);
            if (object == null)
                continue;
            if (StringUtil.isEmpty(object.getId())) {
                object.setId(IDUtil.getUUID());
            }
            batchArgs[i] = new BeanPropertySqlParameterSource(object);
        }
        printSQL(sql);
        namedParameterJdbcTemplate.batchUpdate(sql, batchArgs);
    }

    /**
     * 批量修改实体记录，如对象ID不存在则不处理（无返回，如有返回需求反馈给tangl）
     *
     * @param poList 实体记录集合
     */
    public <T extends BasePO> void updatePoBatch(List<T> poList) {
        if (CollectionUtil.isEmpty(poList)) {
            logger.error("实体记录集合为空");
//			throw new FrameworkRuntimeException("实体记录集合为空");
            return;
        }
        Class<? extends BasePO> clazz = poList.get(0).getClass();
        String sql = SqlHelper.getUpdateSQL(clazz);// 获取SQL

        int arraySize = poList.size();
        SqlParameterSource[] batchArgs = new SqlParameterSource[arraySize];
        for (int i = 0; i < arraySize; i++) {
            T object = poList.get(i);
            if (object == null || object.getId() == null)// 安全检查
                continue;
            batchArgs[i] = new BeanPropertySqlParameterSource(object);
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batchArgs);
    }

    /**
     * 修改实体对象，全属性覆盖
     *
     * @param po 实体记录
     */
    public <T extends BasePO> void updatePo(T po) {
        if (po == null) {
            logger.error("实体记录为null");
            throw new FrameworkRuntimeException("实体记录为null");
        }

        // object中的id不允许是空
        if (po.getId() == null) {
            logger.error("实体对象ID为空，修改不成功");
            throw new FrameworkRuntimeException("实体对象ID为空，修改不成功");
        } else {
            List<T> objects = new ArrayList<T>();
            objects.add(po);
            updatePoBatch(objects);
        }
    }

    /**
     * 修改对象属性值，修改字段由partFieldNames指定
     *
     * @param po             实体记录
     * @param partFieldNames 需修改属性名集合
     */
    public <T extends BasePO> void updatePoPart(T po, String[] partFieldNames) {
        if (po == null) {
            logger.error("实体记录为null");
            throw new FrameworkRuntimeException("实体记录为null");
        }
        String id = po.getId();// 获取ID
        // object中的id不允许是空
        if (id == null) {
            logger.error("实体对象ID为空，修改不成功");
            throw new FrameworkRuntimeException("实体对象ID为空，修改不成功");
        } else {
            String sql = SqlHelper.getUpdatePartSQL(po.getClass(), partFieldNames);
            BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(po);
            int affectedCount = namedParameterJdbcTemplate.update(sql, paramSource);
            if (affectedCount == 0) {
                logger.error("实体记录id={}修改不成功，或无此id", id);
            }
        }
    }

    /**
     * 新增或修改实体记录（较addEntity和updateEntity性能稍差）
     *
     * @param po 实体记录
     */
    public <T extends BasePO> void addOrUpdatePo(T po) {
        Class<?> clazz = po.getClass();
        String tableName = SqlHelper.getTable(clazz);
        List<String> idList = this.getExistIds(tableName, new String[]{po.getId()});
        if (idList.contains(po.getId())) {
            this.updatePo(po);
        } else {
            this.addPo(po);
        }
    }

    /**
     * 批量修改部分字段的实体记录
     *
     * @param poList         实体记录list
     * @param partFieldNames 实体字段名字符串数组
     */
    public <T extends BasePO> void updatePoListPart(List<T> poList, String[] partFieldNames) {
        if (CollectionUtil.isEmpty(poList)) {
            logger.error("实体记录集合为空");
            throw new FrameworkRuntimeException("实体记录集合为空");
        }
        Class<? extends BasePO> clazz = poList.get(0).getClass();
        String sql = SqlHelper.getUpdatePartSQL(clazz, partFieldNames);
        int arraySize = poList.size();
        SqlParameterSource[] batchArgs = new SqlParameterSource[arraySize];
        for (int i = 0; i < arraySize; i++) {
            T object = poList.get(i);
            if (object == null)
                continue;
            if (StringUtil.isEmpty(object.getId()))
                object.setId(IDUtil.getUUID());
            batchArgs[i] = new BeanPropertySqlParameterSource(object);
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batchArgs);
    }

    /**
     * 删除记录
     *
     * @param clazz 类名
     * @param id    主键
     */
    public <T extends BasePO> void deletePo(Class<T> clazz, String id) {
        deletePoList(clazz, new String[]{id});
    }

    /**
     * 批量删除记录 YES
     *
     * @param clazz 类名
     * @param ids   id字符串数组
     */
    public <T extends BasePO> void deletePoList(Class<T> clazz, String[] ids) {
        if (ArrayUtil.isEmpty(ids)) {
            logger.error("数组ids为空");
            throw new FrameworkRuntimeException("数组ids为空");
        }
        String tableName = SqlHelper.getTable(clazz);
        String inSQL = SqlHelper.generateIN(ids);
        String sql = "delete from " + tableName + " where id in " + inSQL;
        namedParameterJdbcTemplate.update(sql, new HashMap<String, Object>());
    }

    /**
     * 混合新增或修改对象集合（注意执行效率稍低，如能明确新增用addBoBatch，修改用updateBoBatch）
     *
     * @param objects 对象
     */
    public <T extends BasePO> void addOrUpdatePoBatch(List<T> objects) {
        if (CollectionUtil.isEmpty(objects)) {
            logger.warn("实体记录集合为空");
//			throw new FrameworkRuntimeException("实体记录集合为空");
            return;
        }
        String tableName = SqlHelper.getTable(objects.get(0).getClass());
        String[] ids = new String[objects.size()];
        int index = 0;
        for (T object : objects) {
            ids[index] = object.getId();
            index++;
        }
        List<String> idExistList = this.getExistIds(tableName, ids);
        List<T> addObjects = new ArrayList<T>();
        List<T> updateObjects = new ArrayList<T>();
        for (T object : objects) {
            if (idExistList.contains(object.getId())) {
                updateObjects.add(object);
            } else {
                addObjects.add(object);
            }
        }
        this.addPoBatch(addObjects);
        this.updatePoBatch(updateObjects);
    }

    /**
     * 按条件删除记录
     *
     * @param clazz     类名
     * @param condition 条件
     */
    public <T extends BasePO> void deletePoByCondition(Class<T> clazz, POCondition condition) {
        String tableName = SqlHelper.getTable(clazz);
        String sql = "delete from " + tableName + condition.getWhereSql();
        printSQL(sql);
        namedParameterJdbcTemplate.update(sql, condition.getConditionMap());
    }
    
	/**
	 * 按条件修改记录
	 * @param clazz 类名
	 * @param condition 条件
	 * @param matching 字段与值的映射
	 */

	public <T extends BasePO> void updatePoByCondition(Class<T> clazz,Map<String, Object> matching, POCondition condition) {
		String sql = SqlHelper.getUpdatePartSQLNoWhere(clazz, matching);
		sql += condition.getWhereSql();
		printSQL(sql);
		namedParameterJdbcTemplate.update(sql, condition.getConditionMap());
	}

    /**
     * 内部通用查询方法
     *
     * @param sql      查询语句
     * @param paramMap 参数Map
     * @param clazz    类名
     */
	private <T extends Queryable> List<T> getListBySql(String sql, Map<String, Object> paramMap, final Class<T> clazz) {
        if (clazz == null || StringUtil.isEmpty(sql)) {
            logger.error("参数sql或clazz为空，请检查程序");
            return null;
        }
        if (MapUtil.isEmpty(paramMap)) {
            paramMap = new HashMap<String, Object>(1);
        }
        RowMapper<T> rowMapper = new RowMapper<T>() {
            ResultSetMetaData rsmd = null;

            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                T entity = null;
                if (rs == null) {
                    logger.warn("ResultSet为空");
                    return entity;
                }
                if (rsmd == null) {
                    rsmd = rs.getMetaData();
                }

                try {
                    entity = clazz.newInstance();
                } catch (Exception e) {
                    logger.error("无法创建实例：" + clazz.getSimpleName(), e);
                    throw new FrameworkRuntimeException("无法创建实例", e);
                }

                // 注意：列序号从1开始
                for (int colIndex = 1, colCount = rsmd.getColumnCount(); colIndex <= colCount; colIndex++) {
                    // 获取列名，并转为驼峰形式，即为对应属性名
                    String propName = StringUtil
                            .underlineToCamelhump(StringUtil.lowerCase(rsmd.getColumnName(colIndex)));
                    String typeName = rsmd.getColumnTypeName(colIndex);
                    Object value = null;

                    if ("NUMBER".equals(typeName)) {
                        value = rs.getBigDecimal(colIndex);
                    } else if ("VARCHAR2".equals(typeName) || "VARCHAR".equals(typeName)|| "CHAR".equals(typeName) || "NVARCHAR2".equals(typeName)) {
                        value = rs.getString(colIndex);
                    } else if ("DATE".equals(typeName)) {
                        value = rs.getDate(colIndex);
                    }else if ("TIMESTAMP".equals(typeName)) {
                        value = rs.getTimestamp(colIndex);
                    }

                    //logger.info("{}=={}=={}", propName, typeName, value);
                    try {
                        // 设置成员变量
                        ObjectUtil.setField(entity, propName, value);
                    } catch (Exception e) {
                        logger.error("无法设置类{}对象的属性{}={}", clazz.getSimpleName(), propName, value);
                        throw new FrameworkRuntimeException("无法设置对象的属性", e);
                    }
                }

                return entity;
            }

        };
        printSQL(sql);
        return namedParameterJdbcTemplate.query(sql, paramMap, rowMapper);
    }

    /**
     * 内部通用查询方法,返回的是装入list的map对象 map为属性-属性值键值对
     *
     * @param sql      查询语句
     * @param paramMap 参数Map
     */
    public List<Map<String, Object>> getListBySql(String sql, Map<String, Object> paramMap) {
        if (StringUtil.isEmpty(sql)) {
            logger.error("参数sql为空，请检查程序");
            return null;
        }
        if (MapUtil.isEmpty(paramMap)) {
            logger.warn("请确认参数paramMap为空，否则可能引起异常。框架已为其初始化");
            paramMap = new HashMap<String, Object>(1);
        }
        printSQL(sql);
        return namedParameterJdbcTemplate.queryForList(sql, paramMap);
    }

    private void printSQL(String sql) {
//        logger.info("SQL = {}", sql);
    }

}
