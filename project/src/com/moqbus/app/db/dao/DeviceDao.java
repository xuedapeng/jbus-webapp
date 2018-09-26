package com.moqbus.app.db.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.moqbus.app.db.bean.DeviceEntity;
import com.moqbus.app.db.bean.UserEntity;




public class DeviceDao extends BaseZDao {

	static Logger logger = Logger.getLogger(DeviceDao.class);
	
	public DeviceDao(EntityManager _em) {
		super(_em);
	}
	
	public DeviceEntity findById(Integer id) {
		return this.em.find(DeviceEntity.class, id);
	}

	@SuppressWarnings("unchecked")
	public DeviceEntity findByDeviceSn(String deviceSn) {

		StringBuffer queryString = new StringBuffer();
		queryString.append("from DeviceEntity");
		queryString.append(" where deviceSn =:deviceSn");
		
		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("deviceSn", deviceSn);
		
		List<DeviceEntity> list = (List<DeviceEntity>)query.getResultList();
		
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		
		return null;

	}
	
	
	@SuppressWarnings("unchecked")
	public List<DeviceEntity> findByBranchId(Integer branchId) {

		StringBuffer queryString = new StringBuffer();
		queryString.append("from DeviceEntity");
		queryString.append(" where branchId =:branchId");
		
		Query query = getEntityManager().createQuery(queryString.toString());
		query.setParameter("branchId", branchId);
		
		List<DeviceEntity> list = (List<DeviceEntity>)query.getResultList();
		
		if(list != null && list.size() > 0){
			return list;
		}
		
		return new ArrayList<DeviceEntity>();

	}
	
	
}
