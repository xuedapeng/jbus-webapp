package com.moqbus.app.db.dao;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.moqbus.app.db.bean.BranchEntity;
import com.moqbus.app.db.bean.BranchUserEntity;



public class BranchUserDao extends BaseZDao {

	static Logger logger = Logger.getLogger(BranchUserDao.class);
	
	public BranchUserDao(EntityManager _em) {
		super(_em);
	}
	
	@SuppressWarnings("unchecked")
	public BranchUserEntity findByUserId(Integer userId) {

		StringBuffer queryString = new StringBuffer();
		queryString.append("from BranchUserEntity");
		queryString.append(" where userId =:userId");
		
		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("userId", userId);
		
		List<BranchUserEntity> list = (List<BranchUserEntity>)query.getResultList();
		
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		
		return null;

	}

	@SuppressWarnings("unchecked")
	public List<BranchUserEntity> findByBranchId(Integer branchId) {

		StringBuffer queryString = new StringBuffer();
		queryString.append("from BranchUserEntity");
		queryString.append(" where branchId =:branchId");
		
		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("branchId", branchId);
		
		List<BranchUserEntity> list = (List<BranchUserEntity>)query.getResultList();
		
		if(list != null && list.size() > 0){
			return list;
		}
		
		return new ArrayList<BranchUserEntity>();

	}
}
