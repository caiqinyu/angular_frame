package com.frame.base.repository.jdbc.support;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

/**
 * 
 * 复杂存储过程的扩展类,扩展了spring StoredProcedure 类
 * 
 * 调用方法举例： THIS 指的是 集成JdbcEntityManager 的DAO实现类 ， 在FlightsDaoImpl 方法中执行存储过程示例如下：
 * 
 * StoredProcedure sp = this.getStoredProcedure("test_proc"); //声明Out参数，字符串类型
 * sp.addOutParameter("field1",Types.INTERGER); //传入InOut参数，日期类型
 * sp.addInOutParameter("p_date", new Date(),Types.DATE);//传入InOut参数，integer类型
 * sp.addInOutParameter("p_int", 23,1); //传入In参数，字符串类型 sp.addInParameter("p1",
 * "abc",Types.STRING); //执行存储过程 Map<String, Object> result = sp.execute();

 ************************************************* 
 */
public class StoredProcedure extends
		org.springframework.jdbc.object.StoredProcedure {

	/**
	 * 输入参数Map
	 */
	Map<String, Object> inParameters = new HashMap<String, Object>();

	/**
	 * 构造方法
	 */
	public StoredProcedure(String StoredProcedureName, DataSource dataSource) {
		super.setDataSource(dataSource);
		super.setSql(StoredProcedureName);
	}

	// 返回游标，并转换成javabean ，未实现
	// public void addOutCursorParameter(String name, Class<?> entityClass){
	// super.declareParameter(new SqlOutParameter(name,entityClass));
	// compile();
	// }

	/**
	 * 
	 * 添加存储过程的输入参数
	 * 
	 * @param name
	 * @param value
	 * @param type
	 *            Date: 2011-5-26上午10:05:55
	 */
	public void addInParameter(String name, Object value, int type) {
		inParameters.put(name, value);
		super.declareParameter(new SqlParameter(name, type));
		compile();

	}

	/**
	 * 
	 * 增加输出参数
	 * 
	 * @param name
	 * @param type
	 *            Date: 2011-5-26上午10:05:34
	 */
	public void addOutParameter(String name, int type) {
		super.declareParameter(new SqlOutParameter(name, type));
		compile();
	}

	/**
	 * 
	 * 添加存储过程的输入，输出参数
	 * 
	 * @param name
	 * @param value
	 * @param type
	 *            Date: 2011-5-26上午10:05:55
	 */
	public void addInOutParameter(String name, Object value, int type) {
		super.declareParameter(new SqlInOutParameter(name, type));
		compile();
	}

	/**
	 * 
	 * 执行存储过程
	 * 
	 * @return Date: 2011-5-26上午10:06:05
	 */
	public Map<String, Object> execute() {
		return super.execute(inParameters);
	}

}
