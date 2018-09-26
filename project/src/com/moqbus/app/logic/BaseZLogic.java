package com.moqbus.app.logic;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.moqbus.app.common.constant.StatusConst;
import com.moqbus.app.db.bean.UserEntity;
import com.moqbus.app.db.dao.UserDao;

import fw.jbiz.common.conf.ZSystemConfig;
import fw.jbiz.ext.json.ZSimpleJsonObject;
import fw.jbiz.ext.memcache.ZCacheManager;
import fw.jbiz.ext.memcache.interfaces.ICache;
import fw.jbiz.logic.ZLogic;
import fw.jbiz.logic.ZLogicParam;

public abstract class BaseZLogic extends ZLogic {

//	protected static ICache cacheUserSecretKey = ZCacheManager.getInstance("user.secretKey");
//	protected static ICache cacheUserId = ZCacheManager.getInstance("user.id");

	
	public BaseZLogic() {
		// 增加访问统计过滤器
		//		addFilter(new ZStatsFilter());
	}
	
	protected String getPersistenceUnitName() {
		return ZSystemConfig.getProperty("persistence_unit_name");
	}
	
	protected UserEntity _loginUser;

   /*
    * 身份认证
    */
	@Override
   protected boolean auth(ZLogicParam logicParam, ZSimpleJsonObject res, EntityManager em){
		BaseZLogicParam bParam = (BaseZLogicParam)logicParam;
		
		if (StringUtils.isEmpty(bParam.getSecretId())
				|| StringUtils.isEmpty(bParam.getSecretKey())) {
			
			res.add("status", -2)
				.add("msg", "auth failed.");
			
			return false;
		}
		
		_loginUser = new UserDao(em).findBySecretId(bParam.getSecretId());
		if (_loginUser == null 
				|| !bParam.getSecretKey().equals(_loginUser.getSecretKey())) {
			
			res.add("status", -2)
				.add("msg", "auth failed.");
		
			return false;
			
		}
		
		// 权限check
		if (!checkPrivilege(logicParam, em)) {

			res.add("status", -2)
				.add("msg", "privilege not enough.");
		
			return false;
		}
		
		return true;
   }
	
	protected boolean checkNotEmpty(String... vals) {
		for(String val: vals) {
			if (StringUtils.isEmpty(val)) {
				return false;
			}
		}
		
		return true;
	}
	
	protected boolean checkPrivilege(ZLogicParam logicParam, EntityManager em) {
		// 系统管理员
		if (logicParam.getClass().getName().contains("logic.sysman.param")) {
			if (!StatusConst.USER_ROLE_SYS_ADMIN.equals(_loginUser.getRole())) {
				return false;
			}
		}
		
		
		return true;
	}
	
}
