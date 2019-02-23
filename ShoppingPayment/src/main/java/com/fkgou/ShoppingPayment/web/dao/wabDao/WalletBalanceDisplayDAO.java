package com.fkgou.ShoppingPayment.web.dao.wabDao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.fkgou.ShoppingPayment.web.entity.AccountBalanceDisplay;
import com.fkgou.ShoppingPayment.web.entity.SimplifyPayConsumeTrade;

public interface WalletBalanceDisplayDAO {

	/**
	 * 查询账户余额
	 * 
	 * @param userId
	 * @return
	 */
	@Select("select user_money,user_recharge_card from pdb_pay_user_resource where user_id = #{userId}")
	AccountBalanceDisplay queryAccountBalance(@Param("userId") String userId);

	/**
	 * 最近账单
	 * 
	 * @param userId
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@Select("select ppcr.order_id,ppcr.record_status,ppcr.trade_type_id,ppcr.record_money,ppcr.record_time,ppcr.plus_or_minus from pdb_pay_consume_record ppcr where ppcr.user_id = #{userId} and user_type = 2 order by ppcr.record_time desc limit #{currentPage},#{pageSize}")
	List<SimplifyPayConsumeTrade> queryRecentOrder(@Param("userId") String userId,
			@Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);

	/**
	 * 账单总数
	 * 
	 * @param userId
	 * @return
	 */
	@Select("select count(*) from pdb_pay_consume_record where user_id = #{userId}")
	int getShipmentsOrderConut(@Param("userId") String userId);

	/**
	 * 商家端交易记录
	 * 
	 * @param userId
	 * @param cp
	 * @param pageSize
	 * @return
	 */
	@Select("select ppcr.order_id,ppcr.record_status,ppcr.trade_type_id,ppcr.record_money,ppcr.record_time,plus_or_minus from pdb_pay_consume_record ppcr where ppcr.user_id = #{userId} and user_type = 1 order by ppcr.record_time desc limit #{cp},#{pageSize}")
	List<SimplifyPayConsumeTrade> queryBusinessRecentOrder(@Param("userId") String userId, @Param("cp") Integer cp,
			@Param("pageSize") Integer pageSize);

}
