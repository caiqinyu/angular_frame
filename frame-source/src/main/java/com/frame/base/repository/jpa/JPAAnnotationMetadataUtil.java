package com.frame.base.repository.jpa;


import java.io.Serializable;

import javax.persistence.Entity;


public class JPAAnnotationMetadataUtil  {

	public Metadata get(Class<?> klass) {
		return JPAAnnotationMetadata.getMetadata(klass);
	}

	public Metadata get(Class<?> rootEntityClass, String propertyPath) {
		Metadata md = get(rootEntityClass);
		if (propertyPath == null || propertyPath.equals("")) {
			return md;
		} else {
			for (String prop : propertyPath.split("\\.")) {
				if ("id".equals(prop)) {
					md = md.getIdType();
				} else {
					md = md.getPropertyType(prop);
				}
				if (md == null)
					throw new IllegalArgumentException("Property path '" + propertyPath + "' invalid for type " + rootEntityClass.getName());
			}
			return md;
		}
	}

	/**
	 * 获取主键值
	 * @param object
	 * @return
	 */
	public Serializable getId(Object object) {
		Metadata md = get(object.getClass());
		return md.getIdValue(object);
	}

	public boolean isId(Class<?> rootClass, String propertyPath) {
		if (propertyPath == null || "".equals(propertyPath))
			return false;
		// with hibernate, "id" always refers to the id property, no matter what
		// that property is named. just make sure the segment before this "id"
		// refers to an entity since only entities have ids.
		if (propertyPath.equals("id")
				|| (propertyPath.endsWith(".id") && get(rootClass, propertyPath.substring(0, propertyPath.length() - 3))
						.isEntity()))
			return true;

		// see if the property is the identifier property of the entity it
		// belongs to.
		int pos = propertyPath.lastIndexOf(".");
		if (pos != -1) {
			Metadata parentType = get(rootClass, propertyPath.substring(0, pos));
			if (!parentType.isEntity())
				return false;
			return propertyPath.substring(pos + 1).equals(parentType.getIdProperty());
		} else {
			return propertyPath.equals(get(rootClass).getIdProperty());
		}
	}

	@SuppressWarnings("unchecked")
	public <T> Class<T> getUnproxiedClass(Class<?> klass) {
		while (klass.getAnnotation(Entity.class) == null) {
			klass = klass.getSuperclass();
			if (Object.class.equals(klass))
				return null;
		}
		
		return (Class<T>) klass;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Class<T> getUnproxiedClass(Object entity) {
		return (Class<T>) getUnproxiedClass(entity.getClass());
	}
	
	
	/**
	 * 获取主键属性名称
	 * @param object 实体对象
	 * @return
	 */
	public String getIdPropertyName(Object object){
		
		Metadata md = get(object.getClass());
		return md.getIdProperty();
	}
	
	/**
	 * 获取主键类型
	 * @param object 实体对象
	 * @return
	 */
	public Metadata getIdPropertyType(Object object) {
		
		Metadata md = get(object.getClass());
		return md.getIdType();
	}
	
	/**
	 * 判断 键 是否可嵌入
	 * @param key 键 原数据
	 * @return
	 */
	public boolean isEmeddable(Metadata key) {
		
		return key.isEmeddable();
	}
}	
