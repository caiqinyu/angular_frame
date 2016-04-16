package com.frame.base.repository.jdbc;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

import com.frame.base.repository.jdbc.support.ArgPreparedStatementSetter;
import com.frame.base.repository.jdbc.support.ArgTypePreparedStatementSetter;
import com.frame.base.repository.jdbc.support.StoredProcedure;

/**
 * 功能描述: 基于Jdbc的entity管理实现类 ， 提供基于jdbc的sql执行功能，包括存储过程执行， 同时提供基于sql的查询功能
 * ，主要用于多表关联查询，特别是左/右连接查询。
 * 
 * spring-database.xml中配置注入
 * 
 * @version 1.0
 *************************************************/
//@Repository("jdbcBaseDAO")
public class JDBCBaseDAO extends NamedParameterJdbcDaoSupport {

/**  使用autowired目前暂时有问题，使用配置文件注入的方式实现暂时 **/
//	@Autowired
//	@Resource(name = "FrameDataSource")
//	public void setMyDataSource(DataSource dataSource) {
//		super.setDataSource(dataSource);
//	}
	
	public void executeSQL(String sql) {
		this.getJdbcTemplate().execute(sql);
	}

	public int update(String sql, Object[] args, int[] types) {
		return this.getJdbcTemplate().update(sql, new ArgTypePreparedStatementSetter(args, types));
	}

	public int[] batchUpdate(String[] sql) {
		return this.getJdbcTemplate().batchUpdate(sql);
	}

	public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
		return this.getJdbcTemplate().batchUpdate(sql, batchArgs);
	}

	public int[] batchUpdate(String sql, List<Object[]> batchArgs, int[] types) {
		return this.getJdbcTemplate().batchUpdate(sql, batchArgs, types);
	}
	
	public void execute(String sql){
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			ps.execute();
			conn.commit();
		}catch(SQLException se){
			se.printStackTrace();
			if(se.getNextException() != null){				
				se.getNextException().printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			this.releaseConnection(conn);
		}
	}

	public void batchUpdate(String sql, List<Object[]> batchArgs, int cycleSize){
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			int count = 0;
			logger.debug("------------------------------sql执行开始--------------------------------------\r\n");
			for (Object[] obj : batchArgs) {
				count++;
				String info="";
				for (int i = 0; i < obj.length; i++) {
					info+=obj[i]+",";
					ps.setObject(i + 1, obj[i]);
				}
				logger.debug("执行第"+count+"条信息---"+sql+"("+info+")");
				ps.addBatch();
				if (count % cycleSize == 0) {
					ps.executeBatch();
					conn.commit();
				}
			}
			if (batchArgs.size() % cycleSize != 0) {
				ps.executeBatch();
				conn.commit();
			}
			logger.debug("---------------------------sql执行完成共插入"+count+"信息--------------------------------------\r\n");
		}catch(SQLException se){
			try {
				conn.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			se.printStackTrace();
			if(se.getNextException() != null){				
				se.getNextException().printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			this.releaseConnection(conn);
		}

	}

	public int update(String sql, Object[] args) {
		return this.getJdbcTemplate().update(sql, new ArgPreparedStatementSetter(args));
	}

	public <T> List<T> find(String sql, Class<T> entityClass) {
		return this.getJdbcTemplate().query(sql, new BeanPropertyRowMapper<T>(entityClass));
	}

	public <T> List<T> find(String sql, Object[] params, Class<T> entityClass) {
		return this.getJdbcTemplate().query(sql, params, new EntityRowMapper<T>(entityClass));
	}

	public <T> List<T> find(String sql, Class<T> entityClass, int start, int size) {
		List<T> result = this.getJdbcTemplate().query(sql, new RangEntityRowMapper<T>(entityClass, start, size));
		return result;
	}

	public <T> T findObject(String sql, Class<T> entityClass) {
		return this.getJdbcTemplate().queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance(entityClass));
	}

	public <T> T findObject(String sql, Object[] params, Class<T> entityClass) {
		return this.getJdbcTemplate().queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance(entityClass),
				params);
	}

	public <T> List<T> find(String sql, Object[] params, Class<T> entityClass, int start, int size) {
		List<T> result = this.getJdbcTemplate()
				.query(sql, params, new RangEntityRowMapper<T>(entityClass, start, size));
		return result;
	}

	public Map<String, Object> findObject(String sql, Object[] params) {
		return this.getJdbcTemplate().queryForMap(sql, params);
	}

	public List<Map<String, Object>> find(String sql, Object[] params) {
		return this.getJdbcTemplate().queryForList(sql, params);
	}

	public StoredProcedure getStoredProcedure(String StoredProcedureName) {
		return new StoredProcedure(StoredProcedureName, this.getDataSource());
	}

	/**
	 * 结果集和实体对象的映射器
	 * 
	 */
	private class RangEntityRowMapper<T> extends JDBCBaseDAO.EntityRowMapper<T> {
		private int start = 0;
		private int size = 0;

		public RangEntityRowMapper(Class<T> entityClass, int start, int size) {
			super(entityClass);
			this.start = start;
			this.size = size;
		}

		/**
		 * 把结果集的指定行映射成对象
		 * 
		 * @param rs
		 * @param rowNum
		 * @return
		 */
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			if (this.start > rowNum) {
				rs.absolute(this.start);
				return null;
			}
			if (rowNum >= this.start + this.size) {
				rs.last();
				return null;
			}
			return super.mapRow(rs, rowNum);
		}
	}

	/**
	 * 功能描述: 实体和记录行的映射类
	 * 
	 */
	private class EntityRowMapper<T> implements RowMapper<T> {
		private Class<T> entityClass;

		public EntityRowMapper(Class<T> entityClass) {
			this.entityClass = entityClass;
		}

		/**
		 * 指定行数的记录映射到实体对象
		 * 
		 * @param rs
		 * @param rowNum
		 * @return
		 * 
		 */
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			try {
				T obj = this.entityClass.newInstance();
				Field fields[] = obj.getClass().getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					// 暴力访问
					field.setAccessible(true);
					this.typeMapper(field, obj, rs);
					// 恢复默认
					field.setAccessible(false);
				}
				return obj;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/*
		 * 数据类型包装器
		 * 
		 * @param field 目标属性
		 * 
		 * @param obj 目标对象
		 * 
		 * @param rs 结果集
		 * 
		 * @throws Exception
		 */
		private void typeMapper(Field field, Object obj, ResultSet rs) throws Exception {
			String type = field.getType().getName();
			if (type.equals("java.lang.String")) {
				field.set(obj, rs.getString(field.getName()));
			} else if (type.equals("int") || type.equals("java.lang.Integer")) {
				field.set(obj, rs.getInt(field.getName()));
			} else if (type.equals("long") || type.equals("java.lang.Long")) {
				field.set(obj, rs.getLong(field.getName()));
			} else if (type.equals("boolean") || type.equals("java.lang.Boolean")) {
				field.set(obj, rs.getBoolean(field.getName()));
			} else if (type.equals("java.util.Date")) {
				field.set(obj, rs.getDate(field.getName()));
			}
		}
	}

}
