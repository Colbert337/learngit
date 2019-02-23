package com.fkgou.ShoppingPayment.web.dao.otherPaymentDao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.fkgou.ShoppingPayment.web.entity.QueryGoodsPara;
import com.fkgou.ShoppingPayment.web.entity.callBackEntity.PayConsumeRecord;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.QueryRecordPara;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.RechargeCallBackPara;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.RecordPara;

@Repository
public interface WXPayDao {

	/**
	 * 查询商品名与商品价格
	 * 
	 * @param out_trade_no
	 * @return
	 */
	@Select("select trade_payment_amount,trade_desc from pdb_pay_consume_trade where order_id = #{out_trade_no}")
	QueryGoodsPara queryGoodsPriceAndName(@Param("out_trade_no") String out_trade_no);

	/**
	 * 查询商品订单号与订单金额
	 * 
	 * @param out_trade_no
	 * @return
	 */
	@Select("select ppcr.user_id,ppcr.order_id,ppcr.record_money from pdb_pay_consume_record ppcr where ppcr.order_id = #{orderId} and user_type = 2")
	PayConsumeRecord queryOrderNo(@Param("orderId") String out_trade_no);

	/**
	 * 更新ComsumeRecord
	 * 
	 * @param recordStatus
	 * @param paymentTime
	 * @param out_trade_no
	 * @return
	 */
	@Update("update pdb_pay_consume_record set record_status = #{recordStatus},record_paytime = #{paymentTime} where order_id = #{out_trade_no} and user_type = 2")
	boolean updateComsumeRecord(@Param("recordStatus") Integer recordStatus, @Param("paymentTime") String paymentTime,
			@Param("out_trade_no") String out_trade_no);

	/**
	 * 更新ConsumeTrade
	 * 
	 * @param orderStateId
	 * @param paymentTime
	 * @param closeTime
	 * @param out_trade_no
	 * @return
	 */
	@Update("update pdb_pay_consume_trade set order_state_id = #{orderStateId},trade_pay_time = #{paymentTime},trade_finish_time = #{closeTime} where consume_trade_id = #{out_trade_no}")
	boolean updateConsumeTrade(@Param("orderStateId") Integer orderStateId, @Param("paymentTime") String paymentTime,
			@Param("closeTime") String closeTime, @Param("out_trade_no") String out_trade_no);

	/**
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
	 * 对比参数，查询订单号与订单金额
	 * 
	 * @param out_trade_no
	 * @return
	 */
	@Select("select order_id,user_id,record_money from pdb_pay_consume_record where order_id = #{out_trade_no}")
	RechargeCallBackPara queryRechargeOrder(@Param("out_trade_no") String out_trade_no);

	/**
	 * 更新Deposit
	 * 
	 * @param depositGmtPayment
	 * @param depositGmtClose
	 * @param depositTradeStatus
	 * @param depositReturnTradeNo
	 * @param depositIsRechargePackage
	 * @param out_trade_no
	 * @return
	 */
	@Update("update pdb_pay_consume_deposit set deposit_gmt_payment = #{depositGmtPayment},deposit_gmt_close = #{depositGmtClose},deposit_trade_status = #{depositTradeStatus},deposit_return_trade_no = #{depositReturnTradeNo},deposit_is_recharge_package = #{depositIsRechargePackage} where deposit_trade_no = #{out_trade_no}")
	boolean updateDeposit(@Param("depositGmtPayment") String depositGmtPayment,
			@Param("depositGmtClose") String depositGmtClose, @Param("depositTradeStatus") Integer depositTradeStatus,
			@Param("depositReturnTradeNo") String depositReturnTradeNo,
			@Param("depositIsRechargePackage") Integer depositIsRechargePackage,
			@Param("out_trade_no") String out_trade_no);

	/**
	 * 更新pdb_pay_consume_record
	 * 
	 * @param gmt_payment
	 * @return
	 */
	@Update("update pdb_pay_consume_record set record_status = 6,record_paytime = #{depositGmtPayment} where order_id = #{out_trade_no}")
	boolean updateRecord(@Param("depositGmtPayment") String depositGmtPayment,
			@Param("out_trade_no") String out_trade_no);

	/**
	 * 更新用户资产
	 * 
	 * @param money
	 * @return
	 */
	@Update("update pdb_pay_user_resource set user_money = user_money + #{money} where user_id = #{userId}")
	boolean updateUserResource(@Param("money") String money, @Param("userId") Integer userId);

	/**
	 * 轮询查状态
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	@Select("select record_status from pdb_pay_consume_record where order_id = #{orderId} and user_id = #{userId}")
	Integer queryStatus(@Param("orderId") String orderId, @Param("userId") Integer userId);

	/**
	 * 查询金额与商品名
	 * 
	 * @param out_trade_no
	 * @return
	 */
	@Select("select record_money,record_desc from pdb_pay_consume_record where order_id = #{orderId} and user_type = 2")
	RecordPara queryRecordPara(@Param("orderId") String out_trade_no);

	/**
	 * 查询trade表
	 * 
	 * @param out_trade_no
	 * @return
	 */
	@Select("select order_id,buyer_id,trade_payment_amount,trade_desc from pdb_pay_consume_trade where order_id = #{out_trade_no}")
	QueryRecordPara queryRecordPriceAndName(@Param("out_trade_no") String out_trade_no);

	/**
	 * 更新状态record
	 * 
	 * @param recordStatus
	 * @param paymentTime
	 * @param out_trade_no
	 * @return
	 */
	@Update("update pdb_pay_consume_record set record_status = #{recordStatus},record_paytime = #{paymentTime} where record_payorder = #{out_trade_no} and user_type = 2")
	boolean updateComsumeRecordByPureDigital(@Param("recordStatus") Integer recordStatus,
			@Param("paymentTime") String paymentTime, @Param("out_trade_no") String out_trade_no);

	/**
	 * 更新状态trade
	 * 
	 * @param orderStateId
	 * @param paymentTime
	 * @param closeTime
	 * @param out_trade_no
	 * @return
	 */
	@Update("update pdb_pay_consume_trade set order_state_id = #{orderStateId},trade_pay_time = #{paymentTime},trade_finish_time = #{closeTime} where order_id = #{out_trade_no}")
	boolean updateConsumeTradeByPureDigital(@Param("orderStateId") Integer orderStateId,
			@Param("paymentTime") String paymentTime, @Param("closeTime") String closeTime,
			@Param("out_trade_no") String out_trade_no);

	/**
	 * 
	 * 
	 * @param tradeNo
	 * @param userId
	 * @return
	 */
	@Select("select deposit_total_fee from pdb_pay_consume_deposit where deposit_buyer_id = #{userId} and deposit_trade_no = #{tradeNo}")
	BigDecimal queryRechargeMoney(@Param("userId") Integer userId, @Param("tradeNo") String tradeNo);

}
