package com.fkgou.ShoppingPayment.web.dao.feginDao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumeDao {

	/**
	 * 
	 * @param paymentNumber
	 * @param userId
	 * @param sellerId
	 * @param payUserId
	 * @param orderStateId
	 * @param tradeTypeId
	 * @param paymentChannelId
	 * @param orderPaymentAmount
	 * @param order_payment_amount
	 * @param tradeDiscount
	 * @param tradeDate
	 * @param tradeYear
	 * @param tradeMonth
	 * @param tradeDay
	 * @param tradeTitle
	 * @param tradeDesc
	 * @param tradeRemark
	 * @param tradeCreateTime
	 * @return
	 */
	@Insert("insert into pdb_pay_consume_trade (consume_trade_id,order_id,buyer_id,seller_id,pay_user_id,order_state_id,trade_type_id,payment_channel_id,order_payment_amount,trade_payment_amount,trade_discount,trade_date,trade_year,trade_month,trade_day,trade_title,trade_desc,trade_remark,trade_create_time) values (#{consumeTradeId},#{orderId},#{userId},#{sellerId},#{payUserId},#{orderStateId},#{tradeTypeId},#{paymentChannelId},#{orderPaymentAmount},#{tradePaymentAmount},#{tradeDiscount},#{tradeDate},#{tradeYear},#{tradeMonth},#{tradeDay},#{tradeTitle},#{tradeDesc},#{tradeRemark},#{tradeCreateTime})")
	boolean insertConsumeTrade(@Param("consumeTradeId") String consumeTradeId, @Param("orderId") String orderId,
			@Param("userId") Integer userId, @Param("sellerId") Integer sellerId, @Param("payUserId") Integer payUserId,
			@Param("orderStateId") Integer orderStateId, @Param("tradeTypeId") Integer tradeTypeId,
			@Param("paymentChannelId") Integer paymentChannelId,
			@Param("orderPaymentAmount") BigDecimal orderPaymentAmount,
			@Param("tradePaymentAmount") BigDecimal tradePaymentAmount,
			@Param("tradeDiscount") BigDecimal tradeDiscount, @Param("tradeDate") String tradeDate,
			@Param("tradeYear") String tradeYear, @Param("tradeMonth") String tradeMonth,
			@Param("tradeDay") String tradeDay, @Param("tradeTitle") String tradeTitle,
			@Param("tradeDesc") String tradeDesc, @Param("tradeRemark") String tradeRemark,
			@Param("tradeCreateTime") String tradeCreateTime);

	/**
	 * 插入record表
	 * 
	 * @param paymentNumber
	 * @param userId
	 * @param userName
	 * @param paymentAmout
	 * @param tradeDate
	 * @param tradeYear
	 * @param tradeMonth
	 * @param tradeDay
	 * @param tradeTitle
	 * @param tradeDesc
	 * @param recordTime
	 * @param tradeTypeId
	 * @param userType
	 * @param recordStatus
	 * @param orderId
	 * @return
	 */
	@Insert("insert into pdb_pay_consume_record (order_id,user_id,user_nickname,record_money,record_date,record_year,record_month,record_day,record_title,record_desc,record_time,trade_type_id,user_type,record_status,record_payorder,plus_or_minus) values (#{paymentNumber},#{userId},#{userName},#{paymentAmout},#{tradeDate},#{tradeYear},#{tradeMonth},#{tradeDay},#{tradeTitle},#{tradeDesc},#{recordTime},#{tradeTypeId},#{userType},#{recordStatus},#{recordPayorderId},0)")
	boolean insertConsumeRecord(@Param("paymentNumber") String paymentNumber, @Param("userId") Integer userId,
			@Param("userName") String userName, @Param("paymentAmout") BigDecimal paymentAmout,
			@Param("tradeDate") String tradeDate, @Param("tradeYear") String tradeYear,
			@Param("tradeMonth") String tradeMonth, @Param("tradeDay") String tradeDay,
			@Param("tradeTitle") String tradeTitle, @Param("tradeDesc") String tradeDesc,
			@Param("recordTime") String recordTime, @Param("tradeTypeId") Integer tradeTypeId,
			@Param("userType") Integer userType, @Param("recordStatus") Integer recordStatus,
			@Param("recordPayorderId") String recordPayorderId);

	/**
	 * 插入record表卖家
	 * 
	 * @param paymentNumber
	 * @param sellerId
	 * @param userName
	 * @param paymentAmout
	 * @param tradeDate
	 * @param tradeYear
	 * @param tradeMonth
	 * @param tradeDay
	 * @param tradeTitle
	 * @param tradeDesc
	 * @param recordTime
	 * @param tradeTypeId
	 * @param userSellerType
	 * @param recordStatus
	 * @param recordPayorderId
	 * @return
	 */
	@Insert("insert into pdb_pay_consume_record (order_id,user_id,user_nickname,record_money,record_date,record_year,record_month,record_day,record_title,record_desc,record_time,trade_type_id,user_type,record_status,record_payorder,plus_or_minus) values (#{paymentNumber},#{sellerId},#{userName},#{paymentAmout},#{tradeDate},#{tradeYear},#{tradeMonth},#{tradeDay},#{tradeTitle},#{tradeDesc},#{recordTime},#{tradeTypeId},#{userType},#{recordStatus},#{recordPayorderId},1)")
	boolean insertConsumeRecordToSeller(@Param("paymentNumber") String paymentNumber,
			@Param("sellerId") Integer sellerId, @Param("userName") String userName,
			@Param("paymentAmout") BigDecimal paymentAmout, @Param("tradeDate") String tradeDate,
			@Param("tradeYear") String tradeYear, @Param("tradeMonth") String tradeMonth,
			@Param("tradeDay") String tradeDay, @Param("tradeTitle") String tradeTitle,
			@Param("tradeDesc") String tradeDesc, @Param("recordTime") String recordTime,
			@Param("tradeTypeId") Integer tradeTypeId, @Param("userType") Integer userType,
			@Param("recordStatus") Integer recordStatus, @Param("recordPayorderId") String recordPayorderId);

	/**
	 * 改变订单状态
	 * 
	 * @param orderId
	 * @return
	 */
	@Update("update pdb_pay_consume_record set record_status = 4 where record_payorder = #{orderId}")
	boolean updateOrderStatus(@Param("orderId") String orderId);

	/**
	 * 添加结算单订单
	 * 
	 * @param settlementId
	 * @param userId
	 * @param userName
	 * @param settlementMoney
	 * @param recordDate
	 * @param recordYear
	 * @param recordMonth
	 * @param recordDay
	 * @param recordTitle
	 * @param recordDesc
	 * @param recordTime
	 * @param tradeTypeId
	 * @param userType
	 * @param recordStatus
	 * @param recordPayorder
	 * @return
	 */
	@Insert("insert into pdb_pay_consume_record (order_id,user_id,user_nickname,record_money,record_date,record_year,record_month,record_day,record_title,record_desc,record_time,trade_type_id,user_type,record_status,record_payorder) values (#{settlementId},#{userId},#{userName},#{settlementMoney},#{recordDate},#{recordYear},#{recordMonth},#{recordDay},#{recordTitle},#{recordDesc},#{recordTime},#{tradeTypeId},#{userType},#{recordStatus},#{recordPayorder})")
	boolean insertSettlementRecord(@Param("settlementId") String settlementId, @Param("userId") Integer userId,
			@Param("userName") String userName, @Param("settlementMoney") String settlementMoney,
			@Param("recordDate") String recordDate, @Param("recordYear") String recordYear,
			@Param("recordMonth") String recordMonth, @Param("recordDay") String recordDay,
			@Param("recordTitle") String recordTitle, @Param("recordDesc") String recordDesc,
			@Param("recordTime") String recordTime, @Param("tradeTypeId") Integer tradeTypeId,
			@Param("userType") Integer userType, @Param("recordStatus") Integer recordStatus,
			@Param("recordPayorder") String recordPayorder);

	/**
	 * 增加卖家资金
	 * 
	 * @param sellerId
	 * @param money
	 * @return
	 */
	@Update("update pdb_pay_user_resource set user_money_frozen = user_money_frozen + #{money} where user_id = #{sellerId}")
	boolean updateSellerMoney(@Param("sellerId") Integer sellerId, @Param("money") BigDecimal money);

}
