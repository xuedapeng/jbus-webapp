package com.moqbus.app.db.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.moqbus.app.db.bean.UserEntity;




public class UserDao extends BaseZDao {

	static Logger logger = Logger.getLogger(UserDao.class);
	
	public UserDao(EntityManager _em) {
		super(_em);
	}
	
	public UserEntity findById(Integer id) {
		return this.em.find(UserEntity.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public UserEntity findBySecretId(String secretId) {

		StringBuffer queryString = new StringBuffer();
		queryString.append("from UserEntity");
		queryString.append(" where secretId =:secretId");
		
		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("secretId", secretId);
		
		List<UserEntity> list = (List<UserEntity>)query.getResultList();
		
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		
		return null;

	}
	
	@SuppressWarnings("unchecked")
	public UserEntity findByAccount(String account) {

		StringBuffer queryString = new StringBuffer();
		queryString.append("from UserEntity");
		queryString.append(" where account =:account");
		
		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("account", account);
		
		List<UserEntity> list = (List<UserEntity>)query.getResultList();
		
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		
		return null;

	}

	@SuppressWarnings("unchecked")
	public List<UserEntity> findByIds(List<Integer> ids) {

		StringBuffer queryString = new StringBuffer();
		queryString.append("from UserEntity");
		queryString.append(" where id in (:ids)");
		
		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("ids", ids);
		
		List<UserEntity> list = (List<UserEntity>)query.getResultList();
		
		if(list != null && list.size() > 0){
			return list;
		}
		
		return new ArrayList<UserEntity>();

	}
}
