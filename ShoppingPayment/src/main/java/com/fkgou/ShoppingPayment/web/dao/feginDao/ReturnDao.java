package com.fkgou.ShoppingPayment.web.dao.feginDao;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnDao {

	/**
	 * 插入record表
	 * 
	 * @param map
	 * @return
	 */
	@Insert(" insert into pdb_pay_consume_record (order_id,user_id,user_nickname,record_money,record_date,record_year,"
			+ "record_month,record_day,record_title,record_desc,record_time,trade_type_id,user_type,record_status,record_payorder,record_paytime)"
			+ " values (#{orderId},#{userId},#{userNickname},#{recordMoney},#{recordDate},#{recordYear},#{recordMonth},"
			+ "#{recordDay},#{recordTitle},#{recordDesc},#{recordTime},#{tradeTypeId},#{userType},#{recordStatus},"
			+ "#{recordPayorder},#{recordPaytime}) ")
	Integer insertRecord(Map<String, String> map);

	/**
	 * 更新record表
	 * 
	 * @param map
	 * @return
	 */
	@Update("update pdb_pay_consume_record set record_status = #{recordStatus} where order_id = #{orderId}")
	Integer updateRecord(Map<String, String> map);

	/**
	 * 更新买家资产
	 * 
	 * @param map
	 * @return
	 */
	@Update("update pdb_pay_user_resource set user_money = user_money + #{userMoney} where user_id = #{userId}")
	Integer updateUserResource(Map<String, String> map);

	/**
	 * 更新卖家资产
	 * 
	 * @param resourceMap
	 * @return
	 */
	@Update("update pdb_pay_user_resource set user_money_frozen = user_money_frozen + #{userMoneyFrozen} where user_id = #{userId}")
	Integer updateSellerResource(Map<String, String> resourceMap);

}
