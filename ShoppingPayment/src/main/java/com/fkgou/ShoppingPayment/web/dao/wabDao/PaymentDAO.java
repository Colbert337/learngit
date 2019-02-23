package com.fkgou.ShoppingPayment.web.dao.wabDao;

import java.math.BigDecimal;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.fkgou.ShoppingPayment.web.entity.QueryGoodsPara;

@Repository
public interface PaymentDAO {

	/**
	 * 验证是否存在密码
	 * 
	 * @param userId
	 * @return
	 */
	@Select("select user_pay_passwd from pdb_pay_user_base where user_id= #{userId}")
	String isExistPasswd(@Param("userId") String userId);

	/**
	 * 设置支付密码
	 * 
	 * @param userId
	 * @param passwd
	 * @return
	 */
	// @Update("update pdb_pay_user_base set user_pay_passwd = #{passwd} where
	// user_id = #{userId}")
	// boolean isInsert(@Param("userId") String userId, @Param("passwd") String
	// passwd);

	/**
	 * 验证支付密码是否正确
	 * 
	 * @param userId
	 * @param passwd
	 * @return
	 */
	@Select("select count(user_pay_passwd) from pdb_pay_user_base where user_id= #{userId} and user_pay_passwd=(select MD5(#{passwd}))")
	Integer isPasswd(@Param("userId") Integer userId, @Param("passwd") String passwd);

	/**
	 * 修改支付密码
	 * 
	 * @param userId
	 * @param newPasswd
	 * @return
	 */
	@Update("update pdb_pay_user_base set user_pay_passwd = (select MD5(#{newPasswd})) WHERE user_id = #{userId}")
	boolean changePaymentPasswd(@Param("userId") String userId, @Param("newPasswd") String newPasswd);

	/**
	 * 验证旧密码
	 * 
	 * @param userId
	 * @param oldPasswd
	 * @return
	 */
	@Select("select count(user_pay_passwd) from pdb_pay_user_base where user_id= #{userId} and user_pay_passwd=(select MD5(#{oldPasswd}))")
	boolean verifyOldPasswd(@Param("userId") String userId, @Param("oldPasswd") String oldPasswd);

	/**
	 * 找回密码(&&设置密码)
	 * 
	 * @param userId
	 * @param passwd
	 * @return
	 */
	@Update("update pdb_pay_user_base set user_pay_passwd = (select MD5(#{passwd})) WHERE user_id = #{userId}")
	boolean modifyPasswd(@Param("userId") Integer userId, @Param("passwd") String passwd);

	/**
	 * 去除购物金额
	 * 
	 * @param userId
	 * @param amountActuallyPaid
	 * @return
	 */
	@Update("update pdb_pay_user_resource set user_money = user_money - #{amountActuallyPaids} where user_id = #{userId}")
	boolean updateMoney(@Param("userId") Integer userId, @Param("amountActuallyPaids") BigDecimal amountActuallyPaids);

	/**
	 * 查询用户余额
	 * 
	 * @param userId
	 * @return
	 */
	@Select("select user_money from pdb_pay_user_resource where user_id = #{userId}")
	BigDecimal queryBalanceMoney(@Param("userId") Integer userId);

	/**
	 * 查询购物卡余额
	 * 
	 * @param userId
	 * @return
	 */
	@Select("select user_recharge_card from pdb_pay_user_resource where user_id = #{userId}")
	BigDecimal queryGiftCardMoney(@Param("userId") Integer userId);

	/**
	 * 扣除购物卡金额
	 * 
	 * @param userId
	 * @param amountActuallyPaid
	 * @return
	 */
	@Update("update pdb_pay_user_resource set user_recharge_card = user_recharge_card - #{amountActuallyPaids} where user_id = #{userId}")
	boolean updateGiftCardMoney(@Param("userId") Integer userId,
			@Param("amountActuallyPaids") BigDecimal amountActuallyPaids);

	/**
	 * trade表更新
	 * 
	 * @param orderId
	 * @param paymentTime
	 * @param userId
	 * @return
	 */
	@Update("update pdb_pay_consume_trade set order_state_id = 3,consume_trade_id = #{orderId},trade_pay_time = #{paymentTime},trade_finish_time = #{finishTime} where buyer_id = #{userId}")
	boolean updateTradeStatus(@Param("orderId") String orderId, @Param("paymentTime") String paymentTime,
			@Param("finishTime") String finishTime, @Param("userId") Integer userId);

	/**
	 * record表
	 * 
	 * @param orderId
	 * @param paymentTime
	 * @return
	 */
	@Update("update pdb_pay_consume_record set record_status = 3,plus_or_minus = 0,record_paytime = #{paymentTime} where order_id = #{orderId} and user_type = 2")
	boolean updateRecord(@Param("orderId") String orderId, @Param("paymentTime") String paymentTime);

	/**
	 * record表卖家更新
	 * 
	 * @param orderId
	 * @param paymentTime
	 * @return
	 */
	@Update("update pdb_pay_consume_record set record_status = 3,record_paytime = #{paymentTime} where order_id = #{orderId} and user_type = 1")
	boolean updateRecordBySeller(@Param("orderId") String orderId, @Param("paymentTime") String paymentTime);

	/**
	 * 查询订单金额及订单名称
	 * 
	 * @param orderId
	 * @return
	 */
	@Select("select trade_payment_amount,trade_desc from pdb_pay_consume_trade where order_id = #{orderId} and buyer_id = #{userId}")
	QueryGoodsPara queryGoodsPriceAndName(@Param("orderId") String orderId, @Param("userId") Integer userId);

}
