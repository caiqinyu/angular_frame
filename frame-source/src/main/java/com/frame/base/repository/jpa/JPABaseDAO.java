package com.frame.base.repository.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import ch.qos.logback.classic.Logger;

import com.frame.base.common.FrameDataSource;
import com.frame.base.utils.DialectUtils;
import com.frame.base.utils.SQLUtils;

/**
 * 整合的DAO
 */

@Repository("baseDao")
public class JPABaseDAO<T, PK extends Serializable> extends AbstractDaoSupport<JpaEntityManager> {
	@Autowired
	private FrameDataSource frameDataSource;
	/** Log */
	protected static Logger logger = (Logger) LoggerFactory.getLogger(JPABaseDAO.class);

	/** Search - Processor */
	private JPASearchProcessor searchProcessor = null;

	/** @Constructor */
	public JPABaseDAO() {
		this.searchProcessor = new JPASearchProcessor(new JPAAnnotationMetadataUtil());
	}

	/**
	 * 保存新增的对象
	 * 
	 * @param entity
	 */
	public void persist(final Object entity) {
		Assert.notNull(entity, "entity不能为空");
		logger.debug("persist entity: {}", entity);
		this.getEntityManager().persist(entity);
	}

	/**
	 * 修改对象
	 * 
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	public T merge(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		logger.debug("merge entity: {}", entity);
		return (T) this.getEntityManager().merge(entity);
	}

	/**
	 * 保存或者修改对象
	 * 
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	public T save(final T entity) {
		logger.debug("save entity: {}", entity);
		return (T) this.getEntityManager().save(entity);
	}

	/**
	 * 删除对象.
	 * 
	 * @param entity
	 *            .
	 */
	public void remove(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		this.getEntityManager().remove(entity);
		logger.debug("delete entity: {}", entity);
	}

	/**
	 * 根据id获取对象
	 * 
	 * @param entityClass
	 *            entity的class类型
	 * @param id
	 *            主键
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object getObjectById(final Class entityClass, final PK id) {
		Assert.notNull(id, "id不能为空");
		return this.getEntityManager().get(entityClass, id);
	}

	/**
	 * 构建sql
	 * 
	 * @param sql
	 * @param values
	 * @return Date：2013-1-17上午9:13:11
	 */
	public Query createNativeQueryWithIndexParam(final String sql, final Object... values) {
		return this.getEntityManager().createNativeQueryWithIndexParam(sql, values);
	}

	/**
	 * 按SQL分页查询对象列表.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <X> SearchResult<X> findPageWithNameParamByNativeSQL(final int firstResult, final int maxResult,
			final String sql, final Map<String, ?> values) {

		SearchResult searResult = new SearchResult();

		List<Object[]> objectList = createNativeQueryWithNameParam(sql, values).setFirstResult(firstResult)
				.setMaxResults(maxResult).getResultList();
		String databaseType = DialectUtils.getDataBaseType(frameDataSource.getDataSource());
		if (databaseType.equals("db2")) {
			List<Object[]> newobjectList = new ArrayList();
			for (Object[] object : objectList) {
				Object[] newobject = new Object[object.length - 1];
				for (int i = 0; i < object.length - 1; i++) {
					newobject[i] = object[i + 1];
				}
				newobjectList.add(newobject);
			}
			objectList = newobjectList;
		}

		// 查询记录总数
		String countSql = SQLUtils.buildCountSQL(sql);
		Object totalCount = createNativeQueryWithNameParam(countSql, values).getSingleResult();
		searResult.setResult(objectList);
		searResult.setTotalCount(totalCount);

		return searResult;
	}

	/**
	 * 根据查询SQL语句与参数列表创建Query对象，JQL中参数按名称绑定
	 * 
	 * @param values
	 *            一个或者多个参数
	 */
	public Query createNativeQueryWithNameParam(final String sql, final Map<String, ?> values) {
		return this.getEntityManager().createNativeQueryWithNameParam(sql, values);
	}

	public void flush() {
		this.getEntityManager().flush();
	}

	public void commit() {
		this.getEntityManager().commit();
	}

	/**
	 * 按id删除对象.
	 */
	public void removeById(final Class<T> entityClass, final PK id) {
		this.getEntityManager().remove(this.getEntityManager().get(entityClass, id));
		logger.debug("delete entity {},id is {}", entityClass.getSimpleName(), id);
	}

	/**
	 * 根据id获取对象列表
	 * 
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> get(final Class<T> entityClass, final Collection<PK> ids) {
		Search search = new Search(entityClass);
		search.setResultMode(Search.RESULT_LIST);
		JPAAnnotationMetadataUtil metadataUtil = new JPAAnnotationMetadataUtil();
		T entity;
		try {
			entity = entityClass.newInstance();
		} catch (Exception e) {
			logger.error("根据主键集合查找对象 {}发生异常！ ", entityClass.getClass());
			throw new RuntimeException("根据主键集合查找对象发生异常", e);
		}
		String propertyName = metadataUtil.getIdPropertyName(entity);
		SearchUtil.addFilterIn(search, propertyName, ids);
		return searchProcessor.search(this.getEntityManager().getPersistEntityManager(), search);
	}

	/**
	 * 根据id获取对象列表,并排序
	 * 
	 * @param ids
	 * @param orderByProperty
	 * @param isDesc
	 *            true 降序，false升序
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> get(final Class<T> entityClass, final Collection<PK> ids, String orderByProperty, boolean isDesc) {
		Search search = new Search(entityClass);
		search.setResultMode(Search.RESULT_LIST);

		JPAAnnotationMetadataUtil metadataUtil = new JPAAnnotationMetadataUtil();
		T entity;
		try {
			entity = entityClass.newInstance();
		} catch (Exception e) {
			logger.error("根据主键集合查找对象 {}发生异常！ ", entityClass.getClass());
			throw new RuntimeException("根据主键集合查找对象发生异常", e);
		}
		String propertyName = metadataUtil.getIdPropertyName(entity);

		SearchUtil.addFilterIn(search, propertyName, ids);
		SearchUtil.addSort(search, orderByProperty, isDesc);

		return searchProcessor.search(this.getEntityManager().getPersistEntityManager(), search);
	}

	/**
	 * 获取全部对象
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAll(final Class<T> entityClass) {
		Search search = new Search(entityClass);
		return searchProcessor.search(this.getEntityManager().getPersistEntityManager(), search);
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
	@SuppressWarnings("unchecked")
	public List<T> getAll(final Class<T> entityClass, String orderByProperty, boolean isDesc) {
		Search search = new Search(entityClass);
		SearchUtil.addSort(search, orderByProperty, isDesc);
		return searchProcessor.search(this.getEntityManager().getPersistEntityManager(), search);
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
	@SuppressWarnings("unchecked")
	public <X> List<X> getAllOrder(final Class<X> entityClass, String orderByProperty, boolean isDesc) {
		Search search = new Search(entityClass);
		SearchUtil.addSort(search, orderByProperty, isDesc);
		return searchProcessor.search(this.getEntityManager().getPersistEntityManager(), search);
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
	@SuppressWarnings("unchecked")
	public List<T> findByProperty(final Class<T> entityClass, final String propertyName, final Object value) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Search search = new Search(entityClass);
		SearchUtil.addFilterEqual(search, propertyName, value);
		return searchProcessor.search(this.getEntityManager().getPersistEntityManager(), search);
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
	@SuppressWarnings("unchecked")
	public List<T> findByPropertyAndOrder(final Class<T> entityClass, final String propertyName, final Object value,
			final String orderByProperty, boolean isDesc) {
		Assert.hasText(propertyName, "propertyName不能为空");

		Search search = new Search(entityClass);
		SearchUtil.addFilterEqual(search, propertyName, value);
		SearchUtil.addSort(search, orderByProperty, isDesc);

		return searchProcessor.search(this.getEntityManager().getPersistEntityManager(), search);
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
	@SuppressWarnings("unchecked")
	public List<T> findByPropertysAndOrder(Class<T> entityClass, String[] propertyNames,
			Object[] values, String[] orderByPropertys, boolean[] isDesc) {
		Search search = new Search(entityClass);
		Sort[] sorts = new Sort[propertyNames.length];
		
		for(int i = 0; i < propertyNames.length ; i ++ ){
			Assert.hasText(propertyNames[i], "propertyName不能为空");
			SearchUtil.addFilterEqual(search, propertyNames[i], values[i]);
			Sort sort = new Sort();
			sort.setProperty(propertyNames[i]);
			if(i < isDesc.length ){
				sort.setDesc(isDesc[i]);
			}
			sorts[i] = sort;
		}
		SearchUtil.addSorts(search, sorts);
		return searchProcessor.search(this.getEntityManager().getPersistEntityManager(), search);
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
	@SuppressWarnings("unchecked")
	public List<T> findByPropertyAndOrderWithParams(final Class<T> entityClass, final String propertyName,
			final Collection<?> values, final String orderByProperty, boolean isDesc) {
		Assert.hasText(propertyName, "propertyName不能为空");

		Search search = new Search(entityClass);
		SearchUtil.addFilterIn(search, propertyName, values);
		SearchUtil.addSort(search, orderByProperty, isDesc);

		return searchProcessor.search(this.getEntityManager().getPersistEntityManager(), search);
	}

	/**
	 * 按属性查找唯一对象, 匹配方式为相等.
	 */
	@SuppressWarnings("unchecked")
	public T findUniqueByProperty(final Class<T> entityClass, final String propertyName, final Object value) {

		Search search = new Search(entityClass);
		search.setResultMode(Search.RESULT_SINGLE);
		SearchUtil.addFilterEqual(search, propertyName, value);

		return (T) searchProcessor.searchUnique(this.getEntityManager().getPersistEntityManager(), search);
	}

	/**
	 * 按JQL查询对象列表.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> findWithIndexParam(final String jql, final Object... values) {
		return this.getEntityManager().createQueryWithIndexParam(jql, values).getResultList();
	}

	/**
	 * 按JQL查询对象列表.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> findWithNameParm(final String jql, final Map<String, ?> values) {
		return this.getEntityManager().createQueryWithNameParam(jql, values).getResultList();
	}

	/**
	 * 按原生SQL查询对象列表.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findByNativeSQLWithIndexParam(final String sql, final Object... values) {
		return this.getEntityManager().createNativeQueryWithIndexParam(sql, values).getResultList();
	}

	/**
	 * 按原生SQL查询对象列表.
	 * 
	 * @param values
	 *            数量可变的参数,按名称绑定.
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findByNativeSQLWithNameParam(final String sql, final Map<String, ?> values) {
		try {
			return this.getEntityManager().createNativeQueryWithNameParam(sql, values).getResultList();
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}

	/**
	 * 按JQL查询唯一对象.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	@SuppressWarnings("unchecked")
	public <X> X findUniqueWithIndexParam(final String jql, final Object... values) {
		try {
			return (X) this.getEntityManager().createQueryWithIndexParam(jql, values).getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}

	/**
	 * 按JQL查询唯一对象.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	@SuppressWarnings("unchecked")
	public <X> X findUniqueWithNameParam(final String jql, final Map<String, ?> values) {
		return (X) this.getEntityManager().createQueryWithNameParam(jql, values).getSingleResult();
	}

	/**
	 * 执行JQL进行批量修改/删除操作.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 * @return 更新记录数.
	 */
	public int batchExecuteWithIndexParam(final String jql, final Object... values) {
		return this.getEntityManager().createQueryWithIndexParam(jql, values).executeUpdate();
	}

	/**
	 * 执行JQL进行批量修改/删除操作.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 * @return 更新记录数.
	 */
	public int batchExecuteWithNameParam(final String jql, final Map<String, ?> values) {
		return this.getEntityManager().createQueryWithNameParam(jql, values).executeUpdate();
	}

	/**
	 * 按JQL分页查询对象列表.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <X> SearchResult<X> findPageIndexParam(final int firstResult, final int maxResult, final String jql,
			final Object... values) {
		SearchResult searResult = new SearchResult();

		List objectList = this.getEntityManager().createQueryWithIndexParam(jql, values).setFirstResult(firstResult)
				.setMaxResults(maxResult).getResultList();
		// 查询记录总数
		String countJql = SQLUtils.buildCountSQL(jql);
		Long totalCount = (Long) this.getEntityManager().createQueryWithIndexParam(countJql, values).getSingleResult();

		searResult.setResult(objectList);
		searResult.setTotalCount(totalCount.intValue());

		return searResult;
	}

	/**
	 * 按JQL分页查询对象列表.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <X> SearchResult<X> findPageWithNameParam(final int firstResult, final int maxResult, final String jql,
			final Map<String, ?> values) {
		SearchResult searResult = new SearchResult();

		List objectList = this.getEntityManager().createQueryWithNameParam(jql, values).setFirstResult(firstResult)
				.setMaxResults(maxResult).getResultList();
		// 查询记录总数
		String countJql = SQLUtils.buildCountSQL(jql);
		Long totalCount = (Long) this.getEntityManager().createQueryWithNameParam(countJql, values).getSingleResult();

		searResult.setResult(objectList);
		searResult.setTotalCount(totalCount.intValue());

		return searResult;
	}

	/**
	 * 根据查询条件，返回对象列表
	 * 
	 * @param search
	 *            查询对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> search(final Class<T> entityClass, Search search) {
		if (search == null)
			throw new NullPointerException("Search is null.");
		if (search.getSearchClass() == null)
			throw new NullPointerException("Search class is null.");
		if (entityClass != null && !search.getSearchClass().equals(entityClass))
			throw new IllegalArgumentException("Search class does not match expected type: " + entityClass.getName());
		return this.searchProcessor.search(this.getEntityManager().getPersistEntityManager(), entityClass, search);
	}

	/**
	 * 根据查询条件，返回符合条件的单个对象，用户单表查询
	 * 
	 * @param search
	 *            查询对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <X> X searchUnique(Search search) {
		if (search == null)
			throw new NullPointerException("Search is null.");
		if (search.getSearchClass() == null)
			throw new NullPointerException("Search class is null.");
		return (X) this.searchProcessor.searchUnique(this.getEntityManager().getPersistEntityManager(), search);
	}

	/**
	 * 返回记录总数，用于单表查询
	 * 
	 * @param search
	 *            查询对象
	 * @return
	 */
	public int count(Search search) {
		if (search == null)
			throw new NullPointerException("Search is null.");
		if (search.getSearchClass() == null)
			throw new NullPointerException("Search class is null.");
		return this.searchProcessor.count(this.getEntityManager().getPersistEntityManager(), search);
	}

	/**
	 * 根据查询JQL语句与参数列表创建Query对象，JQL中参数按顺序绑定，从0开始.
	 * 
	 * @param values
	 *            一个或者多个参数
	 */
	public Query createQueryWithIndexParam(final String queryJQL, final Object... values) {
		return this.getEntityManager().createQueryWithIndexParam(queryJQL, values);
	}

	/**
	 * 根据查询JQL语句与命名参数列表创建Query对象，JQL中参数按名称绑定
	 * 
	 * @param values
	 *            参数Map
	 */
	public Query createQueryWithNameParam(final String queryJQL, final Map<String, ?> values) {
		return this.getEntityManager().createQueryWithNameParam(queryJQL, values);
	}
	
	/**
	 * 按照属性查找对象，匹配方式为"="
	 * @param propertyName 属性名称
	 * @param value 属性值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> findByPropertys(final Class<X> entityClass, final String[] propertyNames, final Object[] values) {

		Search search = new Search(entityClass);

		if (propertyNames != null && values != null) {
			for (int i = 0; i < propertyNames.length; i++) {
				Assert.hasText(propertyNames[i], "propertyName不能为空");
				if (values[i] instanceof List) {
					List<String> list = (List<String>) values[i];
					SearchUtil.addFilterIn(search, propertyNames[i], list);
				} else {
					SearchUtil.addFilterEqual(search, propertyNames[i], values[i]);
				}

			}
		}

		return searchProcessor.search(this.getEntityManager().getPersistEntityManager(), search);
	}
}
