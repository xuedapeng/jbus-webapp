package com.moqbus.app.db.dao;


import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.moqbus.app.db.bean.OperationEntity;

public class OperationDao extends BaseZDao {

	static Logger logger = Logger.getLogger(OperationDao.class);
	
	public OperationDao(EntityManager _em) {
		super(_em);
	}
	
	public OperationEntity findById(Integer id) {
		return this.em.find(OperationEntity.class, id);
	}

}
