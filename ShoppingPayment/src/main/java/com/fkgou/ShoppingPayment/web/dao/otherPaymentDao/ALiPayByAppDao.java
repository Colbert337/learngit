package com.fkgou.ShoppingPayment.web.dao.otherPaymentDao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.fkgou.ShoppingPayment.web.entity.QueryGoodsPara;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.RecordPara;

@Repository
public interface ALiPayByAppDao {

	/**
	 * 支付前生成预付订单(consume_deposit)
	 * 
	 * @param depositTradeNo
	 * @param userId
	 * @param depositTotalFee
	 * @param depositPayChannel
	 * @param depositGmtCreate
	 * @param depositTradeStatus
	 * @param depositIsRechargePackage
	 * @return
	 */
	@Insert("insert into pdb_pay_consume_deposit (deposit_trade_no,deposit_buyer_id,deposit_total_fee,deposit_pay_channel,deposit_gmt_create,deposit_trade_status,deposit_is_recharge_package) values (#{depositTradeNo},#{userId},#{depositTotalFee},#{depositPayChannel},#{depositGmtCreate},#{depositTradeStatus},#{depositIsRechargePackage})")
	boolean createRechargeOrder(@Param("depositTradeNo") String depositTradeNo, @Param("userId") Integer userId,
			@Param("depositTotalFee") String depositTotalFee, @Param("depositPayChannel") Integer depositPayChannel,
			@Param("depositGmtCreate") String depositGmtCreate, @Param("depositTradeStatus") Integer depositTradeStatus,
			@Param("depositIsRechargePackage") Integer depositIsRechargePackage);

	/**
	 * 支付前生成预付订单(consume_record)
	 * 
	 * @param depositTradeNo
	 * @param userId
	 * @param userName
	 * @param depositTotalFee
	 * @param recordDate
	 * @param recordYear
	 * @param recordMonth
	 * @param recordDay
	 * @param recordTitle
	 * @param recordDesc
	 * @param depositGmtCreate
	 * @param tradeTypeId
	 * @param userType
	 * @param depositTradeStatus
	 * @param recordPayorder
	 * @return
	 */
	@Insert("insert into pdb_pay_consume_record (order_id,user_id,user_nickname,record_money,record_date,record_year,record_month,record_day,record_title,record_desc,record_time,trade_type_id,user_type,record_status,record_payorder) values (#{depositTradeNo},#{userId},#{userName},#{depositTotalFee},#{recordDate},#{recordYear},#{recordMonth},#{recordDay},#{recordTitle},#{recordDesc},#{depositGmtCreate},#{tradeTypeId},#{userType},#{depositTradeStatus},#{recordPayorder})")
	boolean createRecordOrder(@Param("depositTradeNo") String depositTradeNo, @Param("userId") Integer userId,
			@Param("userName") String userName, @Param("depositTotalFee") String depositTotalFee,
			@Param("recordDate") String recordDate, @Param("recordYear") String recordYear,
			@Param("recordMonth") String recordMonth, @Param("recordDay") String recordDay,
			@Param("recordTitle") String recordTitle, @Param("recordDesc") String recordDesc,
			@Param("depositGmtCreate") String depositGmtCreate, @Param("tradeTypeId") Integer tradeTypeId,
			@Param("userType") Integer userType, @Param("depositTradeStatus") Integer depositTradeStatus,
			@Param("recordPayorder") String recordPayorder);

	/**
	 * 查询商品价格商品名
	 * 
	 * @param orderId
	 * @return
	 */
	@Select("select trade_payment_amount,trade_desc from  pdb_pay_consume_trade where order_id = #{orderId}")
	QueryGoodsPara queryGoodsPriceAndName(@Param("orderId") String orderId);

	/**
	 * 查询金额与商品名
	 * 
	 * @param orderId
	 * @return
	 */
	@Select("select record_money,record_desc from pdb_pay_consume_record where order_id = #{orderId} and user_type = 2")
	RecordPara queryRecordPara(@Param("orderId") String orderId);

}
