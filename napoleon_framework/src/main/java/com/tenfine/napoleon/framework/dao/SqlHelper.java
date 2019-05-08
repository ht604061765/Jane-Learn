package com.tenfine.napoleon.framework.dao;


import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.tenfine.napoleon.framework.annotation.Table;
import com.tenfine.napoleon.framework.bean.BasePO;
import com.tenfine.napoleon.framework.bean.BaseQo;
import com.tenfine.napoleon.framework.cache.SqlCache;
import com.tenfine.napoleon.framework.util.ArrayUtil;
import com.tenfine.napoleon.framework.util.ClassUtil;
import com.tenfine.napoleon.framework.util.StringUtil;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 对 SQL语句处理或片段处理
 */
public class SqlHelper {
	/**
	 * 根据类获取表名（需再优化）
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> String getTable(Class<T> clazz) {
		String tableName;
		if (clazz.isAnnotationPresent(Table.class)) {
			tableName = clazz.getAnnotation(Table.class).value();
		} else {
			tableName = "t_"+StringUtil.camelhumpToUnderline(StringUtil.firstToLower(clazz.getSimpleName()));
		}

		return tableName;
	}
	/**
	 * 根据类获取表名
	 * Hunter 181107重构，不要t_前缀
	 * @param clazz
	 * @return
	 */
//	public static <T> String getTable(Class<T> clazz) {
//		String tableName;
//		if (clazz.isAnnotationPresent(Table.class)) {
//			tableName = clazz.getAnnotation(Table.class).value();
//		} else {
//			tableName = StringUtil.camelhumpToUnderline(StringUtil.firstToLower(clazz.getSimpleName()));
//		}
//
//		return tableName;
//	}

	/**
	 * 获取实体类对应的insert语句
	 * 
	 * @param clazz
	 *            实体类
	 * @return
	 */
	public static <T extends BasePO> String getAddSQL(Class<T> clazz) {
		String tableName = SqlHelper.getTable(clazz);
		StringBuilder sql = new StringBuilder("insert into ").append(tableName);
		Field[] fields = ClassUtil.getClassField(clazz);
		if (ArrayUtil.isNotEmpty(fields)) {
			int i = 0;
			StringBuilder columns = new StringBuilder(" ");
			StringBuilder values = new StringBuilder(" values ");
			for (Field field : fields) {
				String fieldName = field.getName();
				String columnName = StringUtil.camelhumpToUnderline(field.getName());
				if (i == 0) {
					columns.append("(").append(columnName);
					values.append("(:").append(fieldName);
				} else {
					columns.append(", ").append(columnName);
					values.append(", :").append(fieldName);
				}
				if (i == fields.length - 1) {
					columns.append(")");
					values.append(")");
				}
				i++;
			}
			sql.append(columns).append(values);
		}
		return sql.toString();
	}

	/**
	 * 获取实体类对应的update语句
	 * 
	 * @param clazz
	 *            实体类
	 * @return
	 */
	public static <T extends BasePO> String getUpdateSQL(Class<T> clazz) {
		Field[] fields = ClassUtil.getClassField(clazz);

		StringBuilder sql = new StringBuilder("update ").append(getTable(clazz));
		sql.append(" set ");
		int i = 0;
		for (Field field : fields) {
			String fieldName = field.getName();
			if ("id".equals(fieldName))
				continue;
			String columnName = StringUtil.camelhumpToUnderline(fieldName);
			if (i != 0) {
				sql.append(", ");
			}
			sql.append(columnName).append(" = :").append(fieldName);
			i++;
		}
		sql.append(" where id=:id");
		return sql.toString();
	}

	public static String generateIN(String[] array) {
		if (ArrayUtil.isEmpty(array))
			return null;
		StringBuilder stringBuilder = new StringBuilder("(");
		for (String obj : array) {
			stringBuilder.append("'").append(obj).append("'").append(",");
		}
		String arrayStr = stringBuilder.toString();
		arrayStr = arrayStr.substring(0, arrayStr.length() - 1);
		arrayStr = arrayStr + ")";
		return arrayStr;
	}

	public static String generateIN(Long[] array) {
		if (ArrayUtil.isEmpty(array))
			return null;
		StringBuilder stringBuilder = new StringBuilder("(");
		for (Long obj : array) {
			stringBuilder.append(obj).append(",");
		}
		String arrayStr = stringBuilder.toString();
		arrayStr = arrayStr.substring(0, arrayStr.length() - 1);
		arrayStr = arrayStr + ")";

		return arrayStr;
	}
	
	public static String generateWholeSQL4Pager(String dbType,String sql,int pageNo, int pageSize){
		StringBuilder wholeSQL = new StringBuilder();
		if (dbType.equalsIgnoreCase("mysql")) {
			int pageStart = (pageNo - 1) * pageSize;
			wholeSQL.append("select * from (");
			wholeSQL.append(sql).append(") as a ");
			wholeSQL.append(" limit ").append(pageStart).append(", ").append(pageSize);
		} else if (dbType.equalsIgnoreCase("oracle")) {
			int pageStart = (pageNo - 1) * pageSize + 1;
			int pageEnd = pageStart + pageSize;

			/*
			 * select a.* from ( select rownum rn, t.* from 表名 t where 条件 order
			 * by 排序 ) a where a.rn >= 开始位置 and a.rn < 结束位置
			 */
			wholeSQL.append("select a.* from (select rownum rn, t.* from (").append(sql).append(") t");
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
			wholeSQL.append("* from (").append(sql).append(") t");
			wholeSQL.append(") tt where temprownumber>").append(pageStart).append(")");
		}
		
		return wholeSQL.toString();
	}

	public static <T extends BasePO> String getUpdatePartSQL(Class<T> clazz, String[] partFieldNames) {
		String tableName = SqlHelper.getTable(clazz);
		StringBuilder sql = new StringBuilder("update ").append(tableName).append(" set ");
		int i = 0;
		for (String fieldName : partFieldNames) {
			if ("id".equals(fieldName)) {
				continue;
			} else {
				String columnName = StringUtil.camelhumpToUnderline(fieldName);
				if (i != 0) {
					sql.append(", ");
				}
				sql.append(columnName).append(" = :").append(fieldName);
			}
			i++;
		}
		sql.append(" where id=:id");
		return sql.toString();
	}
	
	public static <T extends BasePO> String getUpdatePartSQLNoWhere(Class<T> clazz, Map<String, Object> matching) {
		String tableName = SqlHelper.getTable(clazz);
		StringBuilder sql = new StringBuilder("update ").append(tableName).append(" set ");
		Set<String> fieldNames = matching.keySet();
		Iterator<String> it = fieldNames.iterator();
		while (it.hasNext()) {  
			String fieldName = it.next();
			if ("id".equals(fieldName)) { //id不允许修改
				continue;
			} else {
				String columnName = StringUtil.camelhumpToUnderline(fieldName);
				sql.append(columnName).append("=").append("'" + matching.get(fieldName) + "'");
			}
		}
		return sql.toString();
	}

	public static <T extends BaseQo> String getExecuteSQL(String sqlId, Class<T> clazz,Map<String, Object> params) {
		Template temp;
		String queryString = null;
		try {
			temp = SqlCache.cfg.getTemplate(clazz.getName()+"-"+sqlId);
			StringWriter queryStringWriter = new StringWriter();
			temp.process(params, queryStringWriter);
			queryString = queryStringWriter.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return queryString;
	}
	
	public static String getExecuteSQL(String sqlId, Map<String, Object> params) {
		Template temp;
		String queryString = null;
		try {
			temp = SqlCache.cfg.getTemplate(sqlId);
			StringWriter queryStringWriter = new StringWriter();
			temp.process(params, queryStringWriter);
			queryString = queryStringWriter.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return queryString;
	}
	
	public static <T extends BasePO> String getExecuteSQL(Class<T> clazz) {
		String tableName = SqlHelper.getTable(clazz);
		String sql = new StringBuilder("select * from ").append(tableName).toString();
		return sql;
	}
}

