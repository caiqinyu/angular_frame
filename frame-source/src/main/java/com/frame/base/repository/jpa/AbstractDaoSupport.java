package com.frame.base.repository.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("hiding")
@Component
public abstract class AbstractDaoSupport<EntityManager> {

	private EntityManager entityManager;

	@Autowired
	public void setEntityManager(EntityManager em) {
		this.entityManager = em;
	}

	public AbstractDaoSupport() {
	}

	public EntityManager getEntityManager() {
		return this.entityManager;
	}

}