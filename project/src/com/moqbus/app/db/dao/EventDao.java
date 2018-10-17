package com.moqbus.app.db.dao;


import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.moqbus.app.db.bean.DeviceEntity;
import com.moqbus.app.db.bean.EventEntity;

public class EventDao extends BaseZDao {

	static Logger logger = Logger.getLogger(EventDao.class);
	
	public EventDao(EntityManager _em) {
		super(_em);
	}
	
	public EventEntity findById(Integer id) {
		return this.em.find(EventEntity.class, id);
	}

}
