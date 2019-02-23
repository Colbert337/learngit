package com.fkgou.ShoppingPayment.web.dao.MQDao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MQDAO {

	/**
	 * 创建用户资产
	 * 
	 * @param userId
	 * @return
	 */
	@Insert("insert into pdb_pay_user_resource (user_id,user_growth) values (#{userId},0)")
	boolean insertByUserAssets(@Param("userId") Integer userId);

	/**
	 * 创建用户基础信息
	 * 
	 * @param userId
	 * @return
	 */
	@Insert("insert into pdb_pay_user_base (user_id) values (#{userId})")
	boolean insertByUserBase(@Param("userId") Integer userId);

}
