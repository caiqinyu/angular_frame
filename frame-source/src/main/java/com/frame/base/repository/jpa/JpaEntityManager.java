package com.frame.base.repository.jpa;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Query;

/**
 * 功能描述: 基于JPA的entity管理接口 Copyright: Copyright (c) 2011 Company: 北京宇信易诚科技有限公司
 * 
 * @author
 * @version 1.0
 * @see HISTORY 2011-5-18下午02:46:49 创建文件
 **************************************************/

public interface JpaEntityManager extends EntityManager {

	
	/**
	 * 获取persistence.EntityManager实例
	 * @return
	 * Date：2013-1-16下午10:36:58
	 */
	public javax.persistence.EntityManager getPersistEntityManager() ;
	
	/**
	 * 保存新增的对象
	 * 
	 * @param entity
	 */
	public void persist(final Object entity);

	/**
	 * 修改对象
	 * 
	 * @param entity
	 */
	public Object merge(final Object entity);

	/**
	 * 保存或者修改对象
	 * 
	 * @param entity
	 */
	public Object save(final Object entity);

	/**
	 * 删除对象.
	 * 
	 * @param entity
	 *            .
	 */
	public void remove(final Object entity);

	/**
	 * 根据id获取对象
	 * 
	 * @param entityClass
	 *            entity的class类型
	 * @param id
	 *            主键
	 * @return
	 */
	public Object get(final Class<?> entityClass, final Serializable id);

	/**
	 * 根据id获取对象
	 * 
	 * @param entityClass
	 *            entity的class类型
	 * @param id
	 *            主键
	 * @return
	 */
	public Object getObjectById(Class<?> entityClass, final Serializable id);

	/**
	 * 根据查询JQL语句与参数列表创建Query对象，JQL中参数按顺序绑定，从0开始.
	 * 
	 * @param values
	 *            一个或者多个参数
	 */
	public Query createQueryWithIndexParam(final String queryJQL, final Object... values);

	/**
	 * 根据查询JQL语句与命名参数列表创建Query对象，JQL中参数按名称绑定
	 * 
	 * @param values
	 *            参数Map
	 */
	public Query createQueryWithNameParam(final String queryJQL, final Map<String, ?> values);

	/**
	 * 根据查询SQL语句与参数列表创建Query对象，SQL中参数按顺序绑定，从0开始.
	 * 
	 * @param values
	 *            一个或者多个参数
	 */
	public Query createNativeQueryWithIndexParam(final String sql, final Object... values);

	/**
	 * 根据查询SQL语句与参数列表创建Query对象，JQL中参数按名称绑定
	 * 
	 * @param values
	 *            一个或者多个参数
	 */
	public Query createNativeQueryWithNameParam(final String sql, final Map<String, ?> values);

	/**
	 * Flush当前Session.
	 */
	public void flush();

	/**
	 * Flush当前Session.
	 */
	public void commit();

	/**
	 * 验证ID是否有效
	 * 
	 * @param id
	 * @return
	 */
	public boolean validId(Serializable id);

}
