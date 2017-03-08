package org.taniwan.common.base.jdbc;

import org.taniwan.common.base.util.BeanReflectUtil;
import org.taniwan.common.base.util.BeanToMapUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * jdbc执行器组件，支持复查的多表联合查询映射及tinyint映射枚举（强于mybatis的映射能力）
 * 例如：select t1.f1, t2.f1 from table1 as t1, table2 as t2
 * @author Chao Zhang
 * @date 2017/1/23-16:48
 */
@Component
public class JdbcRepository{

	private static final Logger log = LoggerFactory.getLogger(JdbcRepository.class);

	@Autowired
	private NamedParameterJdbcTemplate paramJdbcTemplate;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void filterSqlKeyword(String value){
		String temp = StringUtils.replaceEach(value, new String[]{"DELETE","delete","UPDATE","update","TRUNCATE","truncate"}, new String[]{});
		if(!temp.equalsIgnoreCase(value)){
			throw new RuntimeException("sql Contain keyword: " + "DELETE delete UPDATE update TRUNCATE truncate");
		}
	}

	/**
	 * 执行带命名占位符的update、insert语句，返回受影响的行
	 * @param po  实体类
	 * @param sql
	 * @return 受影响的行
	 */
	public <P> int update(String sql, P po){
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(po);
		return paramJdbcTemplate.update(sql, paramSource);
	}

	/**
	 * 执行带?的update、insert语句，返回受影响的行
	 * @param sql
	 * @param args
	 * @param <P>
	 * @return
	 */
	public <P> int updateArgs(String sql, Object... args){
		if(ArrayUtils.isEmpty(args)){
			return jdbcTemplate.update(sql);
		}
		return jdbcTemplate.update(sql, args);
	}

	/**
	 * 批量执行update、insert语句，返回受影响的行
	 * @param sql SQL语句
	 * @param pos 参数对象列表
	 * @param <T> 参数类型
	 * @return
	 */
	public <T> int[] batchUpdate(String sql, final List<T> pos) {
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(pos.toArray());
		int[] updateCounts = paramJdbcTemplate.batchUpdate(sql, batch);
		return updateCounts;
	}

	/**
	 * 执行update、delete语句，返回受影响的行
	 * @param paramMap
	 * @param sql
	 * @return
	 */
	public int update(String sql, Map<String, Object> paramMap){
		return paramJdbcTemplate.update(sql, paramMap);
	}

	/**
	 * 查询多行记录
	 * <ul>
	 * sql的字段（名称和类型）一定要和实体类的字段一致，否则映射不了。<br/>
	 * select * from t1 where f1=:f1, f2=:f2 f1、f2用对象封装或者用map封装<br/>
	 * select * from t2 where f1 in(:list) 占位符必须是list<br/>
	 * </ul>
	 * @author 张超
	 * @date 2016年8月25日-下午4:09:44
	 * @param sql
	 * @param beanType 字段类型
	 * @param param 实体类 | list | map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <P> List<P> queryForList(String sql, Class<P> beanType, Object param) {
		try {
			if (param instanceof Map) {
				if (BeanReflectUtil.isBaseType(beanType)) {
					return paramJdbcTemplate.queryForList(sql, (Map<String, Object>) param, beanType);
				}
				return BeanToMapUtil.convertMap(beanType, paramJdbcTemplate.queryForList(sql, (Map<String, Object>) param));
			}
			else if (param instanceof List) {
				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("list", param);
				if (BeanReflectUtil.isBaseType(beanType)) {
					return paramJdbcTemplate.queryForList(sql, paramMap, beanType);
				}
				return BeanToMapUtil.convertMap(beanType, paramJdbcTemplate.queryForList(sql, paramMap));
			}
			else {
				SqlParameterSource paramSource = new BeanPropertySqlParameterSource(param);
				if (BeanReflectUtil.isBaseType(beanType)) {
					return paramJdbcTemplate.queryForList(sql, paramSource, beanType);
				}
				return BeanToMapUtil.convertMap(beanType, paramJdbcTemplate.queryForList(sql, paramSource));
			}
		}
		catch (EmptyResultDataAccessException e) {
			log.warn("sql: " + sql + "\n not found data, return empty collection.");
			return new ArrayList<>();
		}
	}

	/**
	 * 查询单条记录
	 * <ul>
	 * sql的字段（名称和类型）一定要和实体类的字段一致，否则映射不了。<br/>
	 * select * from t1 where f1=:f1, f2=:f2 f1、f2用对象封装或者用map封装<br/>
	 * select * from t2 where f1 in(:list) 占位符必须是list<br/>
	 * </ul>
	 * @author 张超
	 * @date 2016年8月25日-下午4:09:44
	 * @param sql
	 * @param beanType 字段类型
	 * @param param 实体类 | list | map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <P> P queryForObj(String sql, Class<P> beanType, Object param) {
		try {
			if (param instanceof Map) {
				if (BeanReflectUtil.isBaseType(beanType)) {
					return paramJdbcTemplate.queryForObject(sql, (Map<String, Object>) param, beanType);
				}
				return BeanToMapUtil.convertMap(beanType, paramJdbcTemplate.queryForMap(sql, (Map<String, ?>) param));
			}
			else if (param instanceof List) {
				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("list", param);
				if (BeanReflectUtil.isBaseType(beanType)) {
					return paramJdbcTemplate.queryForObject(sql, paramMap, beanType);
				}
				return BeanToMapUtil.convertMap(beanType, paramJdbcTemplate.queryForMap(sql, paramMap));
			}
			else {
				SqlParameterSource paramSource = new BeanPropertySqlParameterSource(param);
				if (BeanReflectUtil.isBaseType(beanType)) {
					return paramJdbcTemplate.queryForObject(sql, paramSource, beanType);
				}
				return BeanToMapUtil.convertMap(beanType, paramJdbcTemplate.queryForMap(sql, paramSource));
			}
		}
		catch (EmptyResultDataAccessException e) {
			log.warn("sql: " + sql + "\n not found data, return null.");
			return null;
		}
	}

	/**
	 *  无where条件查询多条记录
	 * @param sql
	 * @param beanType 映射实体类
	 * @param <P>
	 * @return
	 */
	public <P> List<P> queryForList(String sql, Class<P> beanType) {
		try {
			if (BeanReflectUtil.isBaseType(beanType)) {
				return jdbcTemplate.queryForList(sql, beanType);
			}
			return BeanToMapUtil.convertMap(beanType, jdbcTemplate.queryForList(sql));
		}
		catch (EmptyResultDataAccessException e) {
			log.warn("sql: " + sql + "\n not found data, return empty collection.");
			return new ArrayList<>();
		}
	}

	/**
	 * 无where条件查询单条记录
	 * @param sql
	 * @param beanType 映射实体类
	 * @param <P>
	 * @return
	 */
	public <P> P queryForObj(String sql, Class<P> beanType) {
		try {
			if (BeanReflectUtil.isBaseType(beanType)) {
				return jdbcTemplate.queryForObject(sql, beanType);
			}
			return BeanToMapUtil.convertMap(beanType, jdbcTemplate.queryForMap(sql));
		}
		catch (EmptyResultDataAccessException e) {
			log.warn("sql: " + sql + "\n not found data, return null.");
			return null;
		}
	}

	/**
	 * 执行带?的查询预编译sql，用在where条件参数个数不多的时候，减少创建map和bean的繁琐<br/>
	 *  SELECT * FROM table1 WHERE id IN(?)
	 * @param sql
	 * @param beanType 映射实体类
	 * @param params  可以为null
	 * @param <P>
	 * @return
	 */
	public <P> List<P> queryArgsForList(String sql, Class<P> beanType, Object... params){
		if(ArrayUtils.isEmpty(params)){
			return queryArgsForList(sql, beanType);
		}
		try{
			if (BeanReflectUtil.isBaseType(beanType)) {
				return jdbcTemplate.queryForList(sql, beanType, params);
			}
			return BeanToMapUtil.convertMap(beanType, jdbcTemplate.queryForList(sql, params));
		}
		catch (EmptyResultDataAccessException e) {
			log.warn("sql: " + sql + "\n not found data, return empty collection.");
			return new ArrayList<>();
		}
	}

	/**
	 * 执行带?的查询预编译sql, 用在where条件参数个数不多的时候，减少创建map和bean的繁琐<br/>
	 * SELECT * FROM table1 WHERE id=?
	 * @param sql
	 * @param beanType  映射实体类
	 * @param params  可以为null
	 * @param <P>
	 * @return
	 */
	public <P> P queryArgsForObj(String sql, Class<P> beanType, Object... params){
		if(ArrayUtils.isEmpty(params)){
			return queryForObj(sql, beanType);
		}
		try{
			if(BeanReflectUtil.isBaseType(beanType)){
				return jdbcTemplate.queryForObject(sql, beanType, params);
			}
			return BeanToMapUtil.convertMap(beanType, jdbcTemplate.queryForMap(sql, params));
		}
		catch (EmptyResultDataAccessException e) {
			log.warn("sql: " + sql + "\n not found data, return null.");
			return null;
		}
	}

	/**
	 * 执行ddl语句，操作失败则抛{@DataAccessException}异常
	 * @param ddlSql
	 */
	public void executeDDLsql(String ddlSql)throws DataAccessException {
		jdbcTemplate.execute(ddlSql);
	}

}