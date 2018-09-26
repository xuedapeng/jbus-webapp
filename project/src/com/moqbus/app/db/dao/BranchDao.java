package com.moqbus.app.db.dao;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.moqbus.app.db.bean.BranchEntity;



public class BranchDao extends BaseZDao {

	static Logger logger = Logger.getLogger(BranchDao.class);
	
	public BranchDao(EntityManager _em) {
		super(_em);
	}
	
	public BranchEntity findById(Integer id) {
		return this.em.find(BranchEntity.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<BranchEntity> findByIds(List<Integer> ids) {

		StringBuffer queryString = new StringBuffer();
		queryString.append("from BranchEntity");
		if (ids != null) {
			queryString.append(" where id in (:ids)");
		}
		
		Query query = getEntityManager().createQuery(queryString.toString());
		if (ids != null) {
			query.setParameter("ids", ids);
		}
		
		List<BranchEntity> list = (List<BranchEntity>)query.getResultList();
		
		if(list != null && list.size() > 0){
			return list;
		}
		
		return new ArrayList<BranchEntity>();

	}

}
