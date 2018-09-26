package com.moqbus.app.db.dao;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.moqbus.app.db.bean.PrivilegeEntity;

public class PrivilegeDao extends BaseZDao {

	static Logger logger = Logger.getLogger(PrivilegeDao.class);
	
	public PrivilegeDao(EntityManager _em) {
		super(_em);
	}
	
	@SuppressWarnings("unchecked")
	public List<PrivilegeEntity> findBySubject(Integer subjectType, Integer subjectId) {

		StringBuffer queryString = new StringBuffer();
		queryString.append("from PrivilegeEntity");
		queryString.append(" where subjectType =:subjectType");
		queryString.append(" and subjectId =:subjectId");
		
		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("subjectType", subjectType);
		query.setParameter("subjectId", subjectId);
		
		List<PrivilegeEntity> list = (List<PrivilegeEntity>)query.getResultList();
		
		if(list != null && list.size() > 0){
			return list;
		}
		
		return new ArrayList<PrivilegeEntity>();

	}


}
