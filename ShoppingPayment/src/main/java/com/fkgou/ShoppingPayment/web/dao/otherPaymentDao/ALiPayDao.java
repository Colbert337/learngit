package com.fkgou.ShoppingPayment.web.dao.otherPaymentDao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.fkgou.ShoppingPayment.web.entity.QueryGoodsPara;
import com.fkgou.ShoppingPayment.web.entity.callBackEntity.PayConsumeDeposit;
import com.fkgou.ShoppingPayment.web.entity.callBackEntity.PayConsumeRecord;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.QueryRecordPara;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.RecordPara;

@Repository
public interface ALiPayDao {

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
	 * 查询充值金额
	 * 
	 * @param userId
	 * @param tradeNo
	 * @return
	 */
	@Select("select deposit_total_fee from pdb_pay_consume_deposit where deposit_buyer_id = #{userId} and deposit_trade_no = #{tradeNo}")
	BigDecimal selectMoney(@Param("userId") Integer userId, @Param("tradeNo") String tradeNo);

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
	 * 根据订单号查询，后与支付宝返回参数做对比
	 * 
	 * @param out_trade_no
	 * @return
	 */
	@Select("select ppcr.order_id,ppcr.record_money from pdb_pay_consume_record ppcr where ppcr.order_id = #{orderId} and user_type = 2")
	PayConsumeRecord queryorderNo(@Param("orderId") String out_trade_no);

	/**
	 * 更改comsumeRecord表
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
	 * 查询recode订单状态
	 * 
	 * @param out_trade_no
	 * @return
	 */
	@Select("select ppcr.order_state_id from pdb_pay_consume_record ppcr where ppcr.order_id = #{out_trade_no}")
	Integer queryRecodeStatus(@Param("out_trade_no") String out_trade_no);

	/**
	 * 查询pdb_pay_consume_deposit订单号及金额
	 * 
	 * @param out_trade_no
	 * @return
	 */
	@Select("select ppcd.deposit_trade_no,ppcd.deposit_total_fee,deposit_buyer_id from pdb_pay_consume_deposit ppcd where ppcd.deposit_trade_no = #{out_trade_no}")
	PayConsumeDeposit queryPayConsumeDeposit(@Param("out_trade_no") String out_trade_no);

	/**
	 * 更新pdb_pay_consume_deposit
	 * 
	 * @param depositGmtPayment
	 * @param depositGmtClose
	 * @param depositTradeStatus
	 * @param depositReturnTradeNo
	 * @param depositIsRechargePackage
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
	 * 查询充值状态
	 * 
	 * @param out_trade_no
	 * @return
	 */
	@Select("select deposit_trade_status from pdb_pay_consume_deposit where deposit_trade_no = #{out_trade_no}")
	Integer queryTradeStatus(@Param("out_trade_no") String out_trade_no);

	/**
	 * 更改ConsumeTrade
	 * 
	 * @param orderStateId
	 * @param paymentTime
	 * @param gmt_close
	 * @return
	 */
	@Update("update pdb_pay_consume_trade set order_state_id = #{orderStateId},trade_pay_time = #{paymentTime},trade_finish_time = #{closeTime} where consume_trade_id = #{out_trade_no}")
	boolean updateConsumeTrade(@Param("orderStateId") Integer orderStateId, @Param("paymentTime") String paymentTime,
			@Param("closeTime") String closeTime, @Param("out_trade_no") String out_trade_no);

	/**
	 * 查询订单号，商品价格商品名
	 * 
	 * @param orderId
	 * @return
	 */
	@Select("select order_id,trade_payment_amount,trade_desc from pdb_pay_consume_trade where order_id = #{orderId}")
	QueryRecordPara queryRecordPriceAndName(@Param("orderId") String orderId);

	/**
	 * 查询商品价格商品名
	 * 
	 * @param orderId
	 * @return
	 */
	@Select("select trade_payment_amount,trade_desc from  pdb_pay_consume_trade where order_id = #{orderId}")
	QueryGoodsPara queryGoodsPriceAndName(String orderId);

	/**
	 * 查询金额与商品名
	 * 
	 * @param orderId
	 * @return
	 */
	@Select("select record_money,record_desc from pdb_pay_consume_record where order_id = #{orderId} and user_type = 2")
	RecordPara queryRecordPara(@Param("orderId") String orderId);

	/**
	 * 更改状态record
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
	 * 更改trade
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

}
