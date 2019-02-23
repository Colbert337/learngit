package com.fkgou.ShoppingPayment.web.dao.wabDao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.fkgou.ShoppingPayment.web.entity.RechargeCard;

/**
 * 
 * @author ilove
 *
 */

@Repository
public interface RechargeDao {

	/**
	 * 判断账密是否正确
	 * 
	 * @param cardCode
	 * @param cardPasswd
	 * @return
	 */
	@Select("select card_money,user_id from pdb_pay_card_info where card_code= #{cardCode} and card_password = #{cardPasswd}")
	RechargeCard judgeIsTrue(@Param("cardCode") String cardCode, @Param("cardPasswd") String cardPasswd);

	/**
	 * 更新充值卡状态
	 * 
	 * @param cardCode
	 * @param userId
	 * @param userName
	 * @param cardFetchTime
	 * @param from
	 * @return
	 */
	@Update("update pdb_pay_card_info set user_id = #{userId},user_account= #{userName},card_fetch_time= #{cardFetchTime},server_id = #{from} where card_code= #{cardCode}")
	boolean updateRechargeCard(@Param("cardCode") String cardCode, @Param("userId") Integer userId,
			@Param("userName") String userName, @Param("from") String from,
			@Param("cardFetchTime") String cardFetchTime);

	/**
	 * 用户更新资产信息
	 * 
	 * @param fee
	 * @param userId
	 * @return
	 */
	@Update("update pdb_pay_user_resource set user_recharge_card = user_recharge_card + #{fee} where user_id = #{userId}")
	boolean updateMoney(@Param("fee") BigDecimal fee, @Param("userId") Integer userId);

	/**
	 * 更改订单充值状态
	 * 
	 * @param recordStatus
	 * @param recordstatus2
	 * @param orderId
	 */
	@Update("update pdb_pay_consume_record set record_status = #{newRecordStatus} where record_status = #{recordStatus} and order_id = #{orderId}")
	void upConsumeRecord(@Param("newRecordStatus") int newRecordStatus, @Param("recordStatus") Integer recordStatus,
			@Param("orderId") String orderId);

	/**
	 * 插入充值表
	 * 
	 * @param depositTradeNo
	 * @param userId
	 * @param fee
	 * @param payChannel
	 * @param depositGmtCreate
	 * @param cardFetchTime
	 * @param gmtClose
	 * @param depositTradeStatus
	 * @param depositReturnTradeNo
	 * @param rechargePackage
	 * @return
	 */
	@Insert("insert into pdb_pay_consume_deposit (deposit_trade_no,deposit_buyer_id,deposit_total_fee,deposit_pay_channel,deposit_gmt_create,deposit_gmt_payment,deposit_gmt_close,deposit_trade_status,deposit_return_trade_no,deposit_is_recharge_package) values (#{depositTradeNo},#{userId},#{fee},#{payChannel},#{depositGmtCreate},#{cardFetchTime},#{gmtClose},#{depositTradeStatus},#{depositReturnTradeNo},#{rechargePackage}) ")
	boolean insertByConsumeDeposit(@Param("depositTradeNo") String depositTradeNo, @Param("userId") Integer userId,
			@Param("fee") BigDecimal fee, @Param("payChannel") String payChannel,
			@Param("depositGmtCreate") String depositGmtCreate, @Param("cardFetchTime") String cardFetchTime,
			@Param("gmtClose") String gmtClose, @Param("depositTradeStatus") Integer depositTradeStatus,
			@Param("depositReturnTradeNo") String depositReturnTradeNo, @Param("rechargePackage") int rechargePackage);

	/**
	 * 插入交易明细表
	 * 
	 * @param orderId
	 * @param userId
	 * @param userName
	 * @param fee
	 * @param date
	 * @param year
	 * @param month
	 * @param day
	 * @param title
	 * @param desc
	 * @param depositGmtCreate
	 * @param typeId
	 * @param userType
	 * @param recordStatus
	 * @param recordPayorder
	 * @param cardFetchTime
	 * @return
	 */
	@Insert("insert into pdb_pay_consume_record (order_id,user_id,user_nickname,record_money,record_date,record_year,record_month,record_day,record_title,record_desc,record_time,trade_type_id,user_type,record_status,record_payorder,record_paytime) values (#{orderId},#{userId},#{userName},#{fee},#{date},#{year},#{month},#{day},#{title},#{desc},#{depositGmtCreate},#{typeId},#{userType},#{recordStatus},#{recordPayorder},#{cardFetchTime})")
	boolean insertConsumeRecord(@Param("orderId") String orderId, @Param("userId") Integer userId,
			@Param("userName") String userName, @Param("fee") BigDecimal fee, @Param("date") String date,
			@Param("year") String year, @Param("month") String month, @Param("day") String day,
			@Param("title") String title, @Param("desc") String desc,
			@Param("depositGmtCreate") String depositGmtCreate, @Param("typeId") int typeId,
			@Param("userType") int userType, @Param("recordStatus") Integer recordStatus,
			@Param("recordPayorder") String recordPayorder, @Param("cardFetchTime") String cardFetchTime);

}
