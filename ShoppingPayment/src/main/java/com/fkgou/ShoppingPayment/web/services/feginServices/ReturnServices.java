package com.fkgou.ShoppingPayment.web.services.feginServices;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.fkgou.ShoppingPayment.web.dao.feginDao.ReturnDao;

@Service
public class ReturnServices {

	@Autowired
	ReturnDao returnDao;

	/**
	 * 插入record表
	 * 
	 * @param returnMap
	 * @return
	 */
	@Transactional
	public Integer insertRecord(Map<String, Map<String, String>> returnMap) {
		Integer one = returnDao.insertRecord(returnMap.get("user"));
		Integer two = returnDao.insertRecord(returnMap.get("seller"));
		if (1 != one || 1 != two) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}
		return 1;
	}

	/**
	 * 更新用户&卖家资产
	 * 
	 * @param map
	 * @return
	 */
	public Integer updateMoney(Map<String, String> map) {
		Integer one = returnDao.updateRecord(map);
		Integer two = returnDao.updateUserResource(map);
		Map<String, String> resourceMap = new HashMap<String, String>();
		resourceMap.put("userId", map.get("sellerId"));
		resourceMap.put("userMoneyFrozen", "-" + map.get("userMoney"));
		Integer three = returnDao.updateSellerResource(resourceMap);
		if (1 != one || 1 != two || 1 != three) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}
		return 1;
	}

}
