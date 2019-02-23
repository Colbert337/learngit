package com.fkgou.ShoppingPayment.web.dao.fkPayDao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.fkgou.ShoppingPayment.web.MybatisSQL.FkPaySql;
import com.fkgou.ShoppingPayment.web.entity.fkPayEntity.ComsumeRecordPara;
import com.fkgou.ShoppingPayment.web.entity.fkPayEntity.OrderSettlementPara;
import com.fkgou.ShoppingPayment.web.entity.fkPayEntity.RecordDetailsPara;

@Repository
public interface FkPayDao {

	/**
	 * 查询交易类型
	 * 
	 * @return
	 */
	@Select("select trade_type_id from pdb_wordbook_pay_trade_type")
	List<Integer> queryTradeId();

	/**
	 * 查询订单状态
	 * 
	 * @return
	 */
	@Select("select order_state_id from pdb_wordbook_pay_order_state")
	List<Integer> queryOrderStatus();

	/**
	 * 查询最近交易记录(支付首页)
	 * 
	 * @param userId
	 * @param tradeTypeId
	 * @return
	 */
	@Select("select consume_record_id,order_id,record_money,record_time,record_status,record_delete from pdb_pay_consume_record where user_id = #{userId} and trade_type_id = #{tradeTypeId} and record_delete = 0 order by record_time desc limit 10")
	List<ComsumeRecordPara> queryRecentDealRecord(@Param("userId") Integer userId,
			@Param("tradeTypeId") Integer tradeTypeId);

	/**
	 * 查询订单详情
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	@Select("select order_id,record_money,record_title,record_desc,record_time,trade_type_id,record_status,record_paytime,plus_or_minus from pdb_pay_consume_record where user_id = #{userId} and order_id = #{orderId} and record_delete = 0")
	List<RecordDetailsPara> queryRecordDetails(@Param("userId") Integer userId, @Param("orderId") String orderId);

	/**
	 * 删除订单
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	@Update("update pdb_pay_consume_record set record_delete = 1 where user_id = #{userId} and consume_record_id = #{recordId}")
	boolean updateRecordDelete(@Param("userId") Integer userId, @Param("recordId") Integer recordId);

	/**
	 * 交易查询
	 * 
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param tradeTypeId
	 * @param orderStatus
	 * @param cp
	 * @param pageSize
	 * @return
	 */
	@SelectProvider(type = FkPaySql.class, method = "queryOrderRecord")
	List<ComsumeRecordPara> queryOrderRecord(@Param("userId") Integer userId, @Param("startTime") String startTime,
			@Param("endTime") String endTime, @Param("tradeTypeId") Integer tradeTypeId,
			@Param("AllType") String AllType, @Param("orderStatus") Integer orderStatus,
			@Param("AllStatus") String AllStatus, @Param("cp") Integer cp, @Param("pageSize") Integer pageSize);

	/**
	 * 查询交易总数
	 * 
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param tradeTypeId
	 * @param orderStatus
	 * @return
	 */
	@SelectProvider(type = FkPaySql.class, method = "getRecordCount")
	Integer getRecordCount(@Param("userId") Integer userId, @Param("startTime") String startTime,
			@Param("endTime") String endTime, @Param("tradeTypeId") Integer tradeTypeId,
			@Param("AllType") String AllType, @Param("orderStatus") Integer orderStatus,
			@Param("AllStatus") String AllStatus);

	/**
	 * 验证支付密码
	 * 
	 * @param userId
	 * @param passwd
	 * @return
	 */
	@Select("select count(user_pay_passwd) from pdb_pay_user_base where user_id= #{fromUserId} and user_pay_passwd=(select MD5(#{passwd}))")
	Integer isPasswd(@Param("fromUserId") Integer fromUserId, @Param("passwd") String passwd);

	/**
	 * 转账信息
	 * 
	 * @param fromUserName
	 * @param toUserName
	 * @param sendTime
	 * @param money
	 * @param paymentExplain
	 * @param transferType
	 * @param transactionNumber
	 * @return
	 */
	@Insert("insert into pdb_pay_transfer_money (from_user,to_user,send_time,transfer_money,transfer_txt,transfer_type,transaction_number) values (#{fromUserName},#{toUserName},#{sendTime},#{money},#{paymentExplain},#{transferType},#{transactionNumber})")
	boolean insertTransfer(@Param("fromUserName") String fromUserName, @Param("toUserName") String toUserName,
			@Param("sendTime") String sendTime, @Param("money") String money,
			@Param("paymentExplain") String paymentExplain, @Param("transferType") Integer transferType,
			@Param("transactionNumber") String transactionNumber);

	/**
	 * 付款人record信息
	 * 
	 * @param transactionNumber
	 * @param fromUserId
	 * @param fromUserName
	 * @param money
	 * @param recordDate
	 * @param recordYear
	 * @param recordMonth
	 * @param recordDay
	 * @param recordTitle
	 * @param recordDesc
	 * @param sendTime
	 * @param tradeTypeId
	 * @param fromUserType
	 * @param recordStatus
	 * @param recordPayorder
	 * @param recordPaytime
	 * @param fromPlusOrMinus
	 * @return
	 */
	@Insert("insert into pdb_pay_consume_record (order_id,user_id,user_nickname,record_money,record_date,record_year,record_month,record_day,record_title,record_desc,record_time,trade_type_id,user_type,record_status,record_payorder,record_paytime,plus_or_minus) values (#{transactionNumber},#{fromUserId},#{fromUserName},#{money},#{recordDate},#{recordYear},#{recordMonth},#{recordDay},#{recordTitle},#{recordDesc},#{sendTime},#{tradeTypeId},#{fromUserType},#{recordStatus},#{recordPayorder},#{recordPaytime},#{fromPlusOrMinus})")
	boolean insertFromRecord(@Param("transactionNumber") String transactionNumber,
			@Param("fromUserId") Integer fromUserId, @Param("fromUserName") String fromUserName,
			@Param("money") String money, @Param("recordDate") String recordDate,
			@Param("recordYear") String recordYear, @Param("recordMonth") String recordMonth,
			@Param("recordDay") String recordDay, @Param("recordTitle") String recordTitle,
			@Param("recordDesc") String recordDesc, @Param("sendTime") String sendTime,
			@Param("tradeTypeId") Integer tradeTypeId, @Param("fromUserType") Integer fromUserType,
			@Param("recordStatus") Integer recordStatus, @Param("recordPayorder") String recordPayorder,
			@Param("recordPaytime") String recordPaytime, @Param("fromPlusOrMinus") Integer fromPlusOrMinus);

	/**
	 * 收款人record信息
	 * 
	 * @param transactionNumber
	 * @param toUserId
	 * @param toUserName
	 * @param money
	 * @param recordDate
	 * @param recordYear
	 * @param recordMonth
	 * @param recordDay
	 * @param recordTitle
	 * @param recordDesc
	 * @param sendTime
	 * @param tradeTypeId
	 * @param toUserType
	 * @param recordStatus
	 * @param recordPayorder
	 * @param recordPaytime
	 * @param toPlusOrMinus
	 * @return
	 */
	@Insert("insert into pdb_pay_consume_record (order_id,user_id,user_nickname,record_money,record_date,record_year,record_month,record_day,record_title,record_desc,record_time,trade_type_id,user_type,record_status,record_payorder,record_paytime,plus_or_minus) values (#{transactionNumber},#{toUserId},#{toUserName},#{money},#{recordDate},#{recordYear},#{recordMonth},#{recordDay},#{recordTitle},#{recordDesc},#{sendTime},#{tradeTypeId},#{toUserType},#{recordStatus},#{recordPayorder},#{recordPaytime},#{toPlusOrMinus})")
	boolean insertToRecord(@Param("transactionNumber") String transactionNumber, @Param("toUserId") String toUserId,
			@Param("toUserName") String toUserName, @Param("money") String money,
			@Param("recordDate") String recordDate, @Param("recordYear") String recordYear,
			@Param("recordMonth") String recordMonth, @Param("recordDay") String recordDay,
			@Param("recordTitle") String recordTitle, @Param("recordDesc") String recordDesc,
			@Param("sendTime") String sendTime, @Param("tradeTypeId") Integer tradeTypeId,
			@Param("toUserType") Integer toUserType, @Param("recordStatus") Integer recordStatus,
			@Param("recordPayorder") String recordPayorder, @Param("recordPaytime") String recordPaytime,
			@Param("toPlusOrMinus") Integer toPlusOrMinus);

	/**
	 * 更新转帐方资产信息
	 * 
	 * @param fromUserId
	 * @param money
	 * @return
	 */
	@Update("update pdb_pay_user_resource set user_money = user_money - #{money} where user_id = #{fromUserId}")
	boolean updateFromMoney(@Param("fromUserId") Integer fromUserId, @Param("money") String money);

	/**
	 * 更新收款方资产信息
	 * 
	 * @param toUserId
	 * @param money
	 * @return
	 */
	@Update("update pdb_pay_user_resource set user_money = user_money + #{money} where user_id = #{toUserId}")
	boolean updateToMoney(@Param("toUserId") String toUserId, @Param("money") String money);

	/**
	 * 更新转账状态
	 * 
	 * @param transactionNumber
	 */
	@Update("update pdb_pay_transfer_money set transfer_status = 1 where transaction_number = #{transactionNumber}")
	boolean updateTransfer(@Param("transactionNumber") String transactionNumber);

	/**
	 * 更新记录状态
	 * 
	 * @param fromUserId
	 * @param transactionNumber
	 */
	@Update("update pdb_pay_consume_record set record_status = 6 where user_id = #{fromUserId} and order_id = #{transactionNumber}")
	boolean updateFromRecord(@Param("fromUserId") Integer fromUserId,
			@Param("transactionNumber") String transactionNumber);

	/**
	 * 更新记录状态
	 * 
	 * @param toUserId
	 * @param transactionNumber
	 */
	@Update("update pdb_pay_consume_record set record_status = 6 where user_id = #{toUserId} and order_id = #{transactionNumber}")
	boolean updateToRecord(@Param("toUserId") String toUserId, @Param("transactionNumber") String transactionNumber);

	/**
	 * 查询提现中，可提现余额
	 * 
	 * @param userId
	 * @return
	 */
	@Select("select user_money from pdb_pay_user_resource where user_id = #{userId}")
	BigDecimal queryAccountOfBalance(@Param("userId") Integer userId);

	/**
	 * 提现申请记录
	 * 
	 * @param userId
	 * @param orderId
	 * @param money
	 * @param createTime
	 * @param withdrawalInstructions
	 * @param bankName
	 * @param bankNum
	 * @return
	 */
	@Insert("insert into pdb_pay_consume_withdraw (pay_uid,orderid,amount,add_time,con,bank,cardno,cardname,user_name) values (#{userId},#{orderId},#{money},#{createTime},#{withdrawalInstructions},#{bankName},#{bankNum},#{payee},#{userName})")
	boolean insertConsumeWithdraw(@Param("userId") Integer userId, @Param("userName") String userName,
			@Param("orderId") String orderId, @Param("money") String money, @Param("createTime") String createTime,
			@Param("withdrawalInstructions") String withdrawalInstructions, @Param("bankName") String bankName,
			@Param("bankNum") String bankNum, @Param("payee") String payee);

	/**
	 * 查询商家冻结资金
	 * 
	 * @param userId
	 * @return
	 */
	@Select("select user_money_frozen from pdb_pay_user_resource where user_id = #{userId}")
	BigDecimal queryMoneyFrozen(@Param("userId") Integer userId);

	/**
	 * 分页查询结算单
	 * 
	 * @param shopId
	 * @param cp
	 * @param pageSize
	 * @return
	 */
	@Select("select consume_record_id,order_id,record_money from pdb_pay_consume_record where user_id = #{userId} and trade_type_id = 11 limit #{cp},#{pageSize}")
	List<OrderSettlementPara> querySettlementOrder(@Param("userId") Integer userId, @Param("cp") Integer cp,
			@Param("pageSize") Integer pageSize);

	/**
	 * 查询结算单总数
	 * 
	 * @param shopId
	 * @return
	 */
	@Select("select count(*) from pdb_pay_consume_record where user_id = #{userId} and trade_type_id = 11")
	Integer getSettlementOrderCount(@Param("userId") Integer userId);

	/**
	 * 查询结算单价格
	 * 
	 * @param recordId
	 * @param shopId
	 * @return
	 */
	@Select("select record_money from pdb_pay_consume_record where consume_record_id = #{recordId} and user_id = #{userId}")
	BigDecimal queryRecordMoney(@Param("recordId") Integer recordId, @Param("userId") Integer userId);

	/**
	 * 计算总提现金额
	 * 
	 * @param userId
	 * @return
	 */
	@Select("select sum(record_money) from pdb_pay_consume_record where user_id = #{userId} and trade_type_id = 11")
	BigDecimal queryAllMoney(@Param("userId") Integer userId);

	/**
	 * 商家提现
	 * 
	 * @param userId
	 * @param userName
	 * @param orderId
	 * @param money
	 * @param createTime
	 * @param withdrawalInstructions
	 * @param bankName
	 * @param bankNum
	 * @param payee
	 * @param statementNo
	 * @param shopName
	 * @return
	 */
	@Insert("insert into pdb_pay_consume_withdraw (pay_uid,orderid,amount,add_time,con,bank,cardno,cardname,user_name,settlement_order_id,shop_name) values (#{userId},#{orderId},#{money},#{createTime},#{withdrawalInstructions},#{bankName},#{bankNum},#{payee},#{userName},#{statementNo},#{shopName})")
	boolean insertConsumeWithdrawBySeller(@Param("userId") Integer userId, @Param("userName") String userName,
			@Param("orderId") String orderId, @Param("money") String money, @Param("createTime") String createTime,
			@Param("withdrawalInstructions") String withdrawalInstructions, @Param("bankName") String bankName,
			@Param("bankNum") String bankNum, @Param("payee") String payee, @Param("statementNo") String statementNo,
			@Param("shopName") String shopName);

	/**
	 * 查询所有结算单
	 * 
	 * @param userId
	 * @return
	 */
	@Select("select order_id from pdb_pay_consume_record where user_id = #{userId} and trade_type_id = 11 ")
	String queryAllStatementNo(@Param("userId") Integer userId);

}
