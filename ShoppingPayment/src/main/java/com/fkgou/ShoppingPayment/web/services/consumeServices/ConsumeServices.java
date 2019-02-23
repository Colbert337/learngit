package com.fkgou.ShoppingPayment.web.services.consumeServices;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkgou.ShoppingPayment.web.dao.feginDao.ConsumeDao;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.AddMoney;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.ConsumeRecordByBusiness;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.ConsumeRecordPara;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.ConsumeTradePara;
import com.fkgou.ShoppingPayment.web.util.FkGouConfig;
import com.fkgou.ShoppingPayment.web.util.RedisUtil;
import com.fkgou.ShoppingPayment.web.util.timeStampUtil;
import com.fkgou.userms.entity.UserInfo;

@Service
public class ConsumeServices {

	@Autowired
	ConsumeDao consumeDao;

	final static Logger log = LoggerFactory.getLogger(ConsumeServices.class);

	/**
	 * Trade
	 * 
	 * @param map
	 * @return
	 */
	@Transactional
	public boolean insertConsumeTrade(ConsumeTradePara ctp) {
		String orderId = ctp.getOrderId();
		String paymentNumber = ctp.getPaymentNumber();
		String consumeTradeId = paymentNumber;// 支付单号
		Integer userId = ctp.getUserId();
		Integer sellerId = ctp.getSellerId();
		Integer payUserId = userId;
		Integer orderStateId = 1;// 订单状态
		Integer tradeTypeId = 1;// 交易类型
		Integer paymentChannelId = 1;// 支付渠道
		BigDecimal tradePaymentAmount = ctp.getTotalPrice();// 实付金额，在线支付金额
		BigDecimal orderPaymentAmount = tradePaymentAmount;
		BigDecimal tradeDiscount = ctp.getTradeDiscount();
		String tradeDate = timeStampUtil.getYear() + "-" + timeStampUtil.getMonth() + "-" + timeStampUtil.getDay();
		String tradeYear = timeStampUtil.getYear();
		String tradeMonth = timeStampUtil.getMonth();
		String tradeDay = timeStampUtil.getDay();
		String tradeTitle = "购物";
		List<String> goodsName = ctp.getGoodsName();
		String tradeDesc = goodsName.get(0);
		String tradeRemark = "";
		String tradeCreateTime = timeStampUtil.getNowDate();
		boolean isInsertSuccess = consumeDao.insertConsumeTrade(consumeTradeId, orderId, userId, sellerId, payUserId,
				orderStateId, tradeTypeId, paymentChannelId, orderPaymentAmount, tradePaymentAmount, tradeDiscount,
				tradeDate, tradeYear, tradeMonth, tradeDay, tradeTitle, tradeDesc, tradeRemark, tradeCreateTime);
		return isInsertSuccess;
	}

	/**
	 * Record
	 * 
	 * @param map
	 * @return
	 */
	@Transactional
	public boolean insertConsumeRecord(ConsumeRecordPara crp) {
		Integer userId = crp.getUserId();
		String userName = crp.getUserName();
		BigDecimal paymentAmout = crp.getTotalPrice();
		String tradeDate = timeStampUtil.getYear() + "-" + timeStampUtil.getMonth() + "-" + timeStampUtil.getDay();
		String tradeYear = timeStampUtil.getYear();
		String tradeMonth = timeStampUtil.getMonth();
		String tradeDay = timeStampUtil.getDay();
		String tradeTitle = "购物";
		List<String> goodsName = crp.getGoodsName();
		String tradeDesc = goodsName.get(0);
		String recordTime = timeStampUtil.getNowDate();
		Integer tradeTypeId = 1;
		Integer userType = 2;
		Integer recordStatus = 1;
		String recordPayorderId = crp.getOrderId();// 实际单号
		String paymentNumber = crp.getPaymentNumber();// 支付号
		boolean isInsertBuyerSuccess = consumeDao.insertConsumeRecord(paymentNumber, userId, userName, paymentAmout,
				tradeDate, tradeYear, tradeMonth, tradeDay, tradeTitle, tradeDesc, recordTime, tradeTypeId, userType,
				recordStatus, recordPayorderId);
		// boolean isInsertSellerSuccess =
		// consumeDao.insertConsumeRecordToSeller(paymentNumber, sellerId,
		// userName,
		// paymentAmout, tradeDate, tradeYear, tradeMonth, tradeDay, tradeTitle,
		// tradeDesc, recordTime,
		// tradeTypeId, userSellerType, recordStatus, recordPayorderId);
		if (isInsertBuyerSuccess) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 存放卖家记录
	 * 
	 * @param crb
	 * @return
	 */
	public boolean insertConsumeRecordByBusiness(ConsumeRecordByBusiness crb) {
		Integer sellerId = crb.getSellId();
		String userName = crb.getUserName();
		BigDecimal paymentAmout = crb.getTotalPrice();
		String tradeDate = timeStampUtil.getYear() + "-" + timeStampUtil.getMonth() + "-" + timeStampUtil.getDay();
		String tradeYear = timeStampUtil.getYear();
		String tradeMonth = timeStampUtil.getMonth();
		String tradeDay = timeStampUtil.getDay();
		String tradeTitle = "购物";
		List<String> goodsName = crb.getGoodsName();
		String tradeDesc = goodsName.get(0);
		String recordTime = timeStampUtil.getNowDate();
		Integer tradeTypeId = 1;
		Integer userType = 1;
		Integer recordStatus = 1;
		String recordPayorderId = crb.getOrderId();// 实际单号
		String paymentNumber = crb.getPaymentNumber();// 支付号
		boolean isInsertSellerSuccess = consumeDao.insertConsumeRecordToSeller(paymentNumber, sellerId, userName,
				paymentAmout, tradeDate, tradeYear, tradeMonth, tradeDay, tradeTitle, tradeDesc, recordTime,
				tradeTypeId, userType, recordStatus, recordPayorderId);
		if (isInsertSellerSuccess) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 发货成功，改变状态
	 * 
	 * @param crp
	 * @return
	 */
	@Transactional
	public boolean changeConsumeRecord(String orderId) {
		boolean isChangeOrderStatus = consumeDao.updateOrderStatus(orderId);
		if (isChangeOrderStatus) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 添加结算单订单
	 * 
	 * @param map
	 * @return
	 */
	@Transactional
	public boolean insertSettlementRecord(Map<String, Object> map) {
		String token = (String) map.get("token");
		boolean flag = false;
		try {
			ObjectMapper mapper = new ObjectMapper();
			UserInfo ui = new UserInfo();
			String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
			ui = mapper.readValue(userinfo, UserInfo.class);// String转object
			Integer userId = ui.getUserId();
			String userName = ui.getUserName();
			String settlementId = (String) map.get("settlementId");// 结算编号
			String settlementMoney = (String) map.get("settlementMoney");// 结算金额
			String recordDate = timeStampUtil.getYear() + "-" + timeStampUtil.getMonth() + "-" + timeStampUtil.getDay();
			String recordYear = timeStampUtil.getYear();
			String recordMonth = timeStampUtil.getMonth();
			String recordDay = timeStampUtil.getDay();
			String recordTitle = "商家结算";
			String recordDesc = "结算";
			String recordTime = timeStampUtil.getNowDate();
			Integer tradeTypeId = 11;
			Integer userType = 1;
			Integer recordStatus = 6;
			String recordPayorder = settlementId;
			boolean isInsertSuccess = consumeDao.insertSettlementRecord(settlementId, userId, userName, settlementMoney,
					recordDate, recordYear, recordMonth, recordDay, recordTitle, recordDesc, recordTime, tradeTypeId,
					userType, recordStatus, recordPayorder);
			if (isInsertSuccess) {
				flag = true;
			}
		} catch (Exception e) {
			log.info("insertSettlementRecordErrorResults:" + e.getMessage());
		}
		return flag;
	}

	/**
	 * 增加卖家金额
	 * 
	 * @param map
	 * @return
	 */
	@Transactional
	public boolean addMoney(AddMoney am) {
		Integer sellerId = am.getSellerId();
		BigDecimal money = am.getMoney();
		boolean isUpdateSuccess = consumeDao.updateSellerMoney(sellerId, money);
		if (isUpdateSuccess) {
			return true;
		} else {
			return false;
		}
	}
}