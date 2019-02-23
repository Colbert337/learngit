package com.fkgou.ShoppingPayment.web.dao.feginDao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateUserDao {

	/**
	 * 创建用户
	 * 
	 * @param userId
	 * @return
	 */
	@Insert("insert into pdb_pay_user_base (user_id) values (#{userId})")
	boolean createUser(@Param("userId") Integer userId);

	/**
	 * 创建用户资金信息
	 * 
	 * @param userId
	 * @return
	 */
	@Insert("insert into pdb_pay_user_resource (user_id) values (#{userId})")
	boolean createUserResource(@Param("userId") Integer userId);

}
