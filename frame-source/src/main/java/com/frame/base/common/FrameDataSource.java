/**
 * 通用数据源
 */
package com.frame.base.common;

import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;

@SuppressWarnings("rawtypes")
public class FrameDataSource implements FactoryBean {

	private DataSource dataSource = null;

	public DataSource getDataSource() {
		return this.dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	
	public Object getObject() throws Exception {

		return this.dataSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	
	public Class getObjectType() {

		return DataSource.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	
	public boolean isSingleton() {
		return true;
	}
}
