package com.fkgou.ShoppingPayment.web.services.consumeServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fkgou.ShoppingPayment.web.dao.feginDao.CreateUserDao;

@Service
public class CreateUserServices {

	@Autowired
	CreateUserDao createUserDao;

	/**
	 * 创建用户
	 * 
	 * @param userId
	 * @return
	 */
	public boolean createUser(Integer userId) {
		boolean isInsertByUserBaseSuccess = createUserDao.createUser(userId);
		boolean isInsertByUseResourceSuccess = createUserDao.createUserResource(userId);
		if (isInsertByUserBaseSuccess && isInsertByUseResourceSuccess) {
			return true;
		} else {
			return false;
		}
	}

}
