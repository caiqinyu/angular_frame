package com.frame.base.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.frame.base.repository.jdbc.JDBCBaseDAO;
import com.frame.base.repository.jpa.DynamicSpecifications;
import com.frame.base.repository.jpa.JPABaseDAO;
import com.frame.base.repository.jpa.SearchFilter;
import com.frame.base.utils.ReflectionUtils;
import com.google.common.collect.Lists;

/**
 * <pre>
 * Title:基础业务逻辑类
 * Description: 封装常用的基础功能，具体的业务逻辑类继承此类后对于基础的增、删、改、查操作基本不用编码
 *              对于需要些使用JQL、SQL查询的功能，在继承的类中增加新的方法调用DAO实现，此类中不开放这些方法
 * </pre>
 * 
 * @author caiqy
 * 
 */

public class BaseBS<T> {

	// 直接使用JPAbaseDAO进行数据操作，如果有复杂的数据操作，需要定义一个独立的D\AO类
	@Autowired
	protected JPABaseDAO<T, Serializable> baseDAO;
	@Autowired
	protected JDBCBaseDAO jdbcBaseDAO;

	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public BaseBS() {
		this.entityClass = (Class<T>) ReflectionUtils.getTypeArguments(BaseBS.class, this.getClass()).get(0);
	}

	public Specification<T> buildSpecification(Map<String, SearchFilter> searchFilter) {
		return DynamicSpecifications.bySearchFilter(searchFilter.values(), this.entityClass);
	}

	public Specification<?> buildSpecification(Map<String, SearchFilter> searchFilter, Class<?> entityClass) {
		return DynamicSpecifications.bySearchFilter(searchFilter.values(), entityClass);
	}

	/**
	 * 保存对象
	 * 
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void saveEntity(T entity) {
		this.baseDAO.persist(entity);
	}

	/**
	 * 修改对象
	 * 
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public T updateEntity(T entity) {
		return this.baseDAO.merge(entity);
	}

	/**
	 * 保存或者修改对象
	 * 
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public T saveOrUpdateEntity(T entity) {
		return this.baseDAO.save(entity);
	}

	/**
	 * 根据Id获取指定类型的对象
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <X> X getEntityById(final Class entityClass, final Serializable id) {
		return (X) this.baseDAO.getObjectById(entityClass, id);
	}

	/**
	 * 根据属性值获取指定类型的对象
	 * 
	 * @param entityClass
	 *            实体类的class
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <X> X getEntityByProperty(final Class entityClass, String propertyName, Object value) {
		String jql = "select obj from " + entityClass.getSimpleName() + " obj where obj." + propertyName + "=?0";
		return (X) this.baseDAO.findUniqueWithIndexParam(jql, value);
	}

	/**
	 * 根据Class获取指定类型的对象列表
	 * 
	 * @param entityClass
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <X> List<X> getEntityList(final Class entityClass) {
		String jql = "select obj from " + entityClass.getSimpleName() + " obj ";
		return (List<X>) this.baseDAO.findWithIndexParam(jql);
	}

	/**
	 * 根据属性值获取指定类型的对象列表
	 * 
	 * @param entityClass
	 *            实体类的class
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <X> List<X> getEntityListByProperty(final Class entityClass, String propertyName, Object value) {
		String jql = "select obj from " + entityClass.getSimpleName() + " obj where obj." + propertyName + "=?0";
		return (List<X>) this.baseDAO.findWithIndexParam(jql, value);
	}

	/**
	 * 根据ID获取实体对象.
	 */
	@SuppressWarnings("unchecked")
	public T getEntityById(final Serializable id) {
		return (T) this.baseDAO.getObjectById(entityClass, id);
	}

	/**
	 * 删除实体对象.
	 */
	@Transactional(readOnly = false)
	public void removeEntity(final T entity) {
		this.baseDAO.remove(baseDAO.merge(entity));
	}

	/**
	 * 按id删除对象.
	 */
	@Transactional(readOnly = false)
	public void removeEntityById(final Serializable id) {
		this.baseDAO.removeById(this.entityClass, id);
	}

	/**
	 * 删除实体对象集合
	 */
	@Transactional(readOnly = false)
	public void removeEntity(final List<T> entitys) {
		if (entitys == null) {
			return;
		}
		for (T t : entitys) {
			this.baseDAO.remove(t);
		}
	}

	/**
	 * 按属性删除对象
	 * 
	 * @param propertyName
	 *            属性名
	 * @param value
	 *            属性值，支持多个属性值，属性值之间用，分割
	 */
	@Transactional(readOnly = false)
	public void removeEntityByProperty(final String propertyName, final String value) {
		if (StringUtils.isBlank(value)) {
			return;
		}
		String[] values = value.split(",");
		List<Object> valueList = Lists.newArrayList();
		if (values != null) {
			for (String val : values) {
				valueList.add(val);
			}
		}
		List<T> objs = this.baseDAO.findByPropertyAndOrderWithParams(this.entityClass, propertyName, valueList, null,
				false);
		this.removeEntity(objs);
	}

	/**
	 * 根据id获取对象列表
	 * 
	 * @param ids
	 * @return
	 */
	public List<T> getEntityListById(final Collection<Serializable> ids) {
		return this.baseDAO.get(this.entityClass, ids);
	}

	/**
	 * 根据Id获取对象列表,并排序
	 * 
	 * @param ids
	 * @param orderByProperty
	 * @param isDesc
	 *            true 降序，false升序
	 * @return
	 */

	public List<T> getEntityListByProperty(final Collection<Serializable> ids, String orderByProperty, boolean isDesc) {
		return this.baseDAO.get(this.entityClass, ids, orderByProperty, isDesc);
	}

	/**
	 * 获取全部对象
	 * 
	 * @return
	 */
	public List<T> getAllEntityList() {
		return this.baseDAO.getAll(this.entityClass);
	}

	/**
	 * 
	 * 获取全部对象, 支持按属性行序
	 * 
	 * @param orderByProperty
	 *            排序的属性名称
	 * @param isDesc
	 *            true：降序，false:升序
	 * @return
	 */

	public List<T> getAllEntityList(String orderByProperty, boolean isDesc) {
		return this.baseDAO.getAll(this.entityClass, orderByProperty, isDesc);
	}
	
	/**
	 * 
	 * 获取全部对象, 支持按属性行序
	 * 
	 * @param orderByProperty
	 *            排序的属性名称
	 * @param isDesc
	 *            true：降序，false:升序
	 * @return
	 */

	public <X> List<X> getAllEntityList(Class<X> entityClass , String orderByProperty, boolean isDesc) {
		return this.baseDAO.getAllOrder(entityClass, orderByProperty, isDesc);
	}

	/**
	 * 按照属性查找对象，匹配方式为"="
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @return
	 */

	public List<T> findEntityListByProperty(final String propertyName, final Object value) {
		return this.baseDAO.findByProperty(this.entityClass, propertyName, value);
	}

	/**
	 * 按照属性查找对象，匹配方式为"="
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @param orderByProperty
	 *            排序属性名称
	 * @param isDesc
	 *            true:降序，false：升序
	 * @return
	 */
	public List<T> findEntityListByPropertyAndOrder(final String propertyName, final Object value,
			final String orderByProperty, boolean isDesc) {
		return this.baseDAO.findByPropertyAndOrder(this.entityClass, propertyName, value, orderByProperty, isDesc);
	}

	/**
	 * 按照属性查找对象，匹配方式为"in"
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @param orderByProperty
	 *            排序属性名称
	 * @param isDesc
	 *            true:降序，false：升序
	 * @return
	 */

	public List<T> findByPropertyAndOrderWithParams(final String propertyName, final Collection<?> values,
			final String orderByProperty, boolean isDesc) {
		return this.baseDAO.findByPropertyAndOrderWithParams(this.entityClass, propertyName, values, orderByProperty,
				isDesc);
	}

	/**
	 * 按属性查找唯一对象, 匹配方式为相等.
	 */

	public T findUniqueEntityByProperty(final String propertyName, final Object value) {
		return this.baseDAO.findUniqueByProperty(this.entityClass, propertyName, value);
	}
	/**
	 * 按照属性查找对象，匹配方式为"=",in时，value对应的值是List
	 * @param propertyName 属性名称数组
	 * @param value 属性值数组
	 * @return
	 */
	public <X> List<X> findByPropertys( Class<X> entityClass, String[] propertyNames,  Object[] values){
		return this.baseDAO.findByPropertys(entityClass, propertyNames, values);
	}
}
