package com.fkgou.ShoppingPayment.web.services.wabServicesImpl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkgou.ShoppingPayment.web.dao.wabDao.RechargeDao;
import com.fkgou.ShoppingPayment.web.entity.RechargeCard;
import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.util.FkGouConfig;
import com.fkgou.ShoppingPayment.web.util.RedisUtil;
import com.fkgou.ShoppingPayment.web.util.timeStampUtil;
import com.fkgou.userms.entity.UserInfo;

@Service
public class RechargeServicesImpl {

	@Autowired
	RechargeDao rechargeDao;

	/**
	 * 充值卡充值
	 * 
	 * @param map
	 * @return
	 */
	@Transactional
	public ResultResp rechargeCard(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
		boolean isExist = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExist) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				Integer userId = ui.getUserId();
				String userName = ui.getUserName();
				String cardCode = (String) map.get("cardCode");
				String cardPasswd = (String) map.get("cardPasswd");
				Integer type = (Integer) map.get("type");// 1 wap 2 ios 3
															// Android 4 pc
				String from = null;
				if (type == 1) {
					from = "wap";
				} else if (type == 2) {
					from = "ios";
				} else if (type == 3) {
					from = "Android";
				} else if (type == 4) {
					from = "pc";
				}
				// 充值表所需参数
				String depositGmtCreate = timeStampUtil.getNowDate();// 交易创建时间
				String depositTradeNo = "R" + timeStampUtil.getTimeStamp();// 充值订单号
				String payChannel = "6";// 充值的付款方式
				String gmtClose = "0000-00-00 00:00:00";// 交易关闭时间
				Integer depositTradeStatus = 8;// 交易状态
				String depositReturnTradeNo = depositTradeNo;// 充值成功以后的流水
				int RechargePackage = 0; // 普通充值卡充值

				// 交易明细表所需参数
				String orderId = depositTradeNo;
				String date = timeStampUtil.getYear() + "-" + timeStampUtil.getMonth() + "-" + timeStampUtil.getDay();// 日期
				String year = timeStampUtil.getYear();// 年
				String month = timeStampUtil.getMonth();// 月
				String day = timeStampUtil.getDay();// 日
				String title = "充值";// 交易类型
				String desc = "充值卡充值";// 描述
				int typeId = 3;// 交易类型
				int userType = 2;// 收款方
				Integer recordStatus = 8;// 处理中
				String recordPayorder = orderId;// 实际支付单号

				// 判断账密是否正确
				RechargeCard rechargeCard = rechargeDao.judgeIsTrue(cardCode, cardPasswd);
				if (rechargeCard != null) {
					Integer buyersUserId = rechargeCard.getUser_id();
					if (buyersUserId != 0) {
						rr.setCode(2);
						rr.setDesc("该充值卡已使用！");
					} else {
						BigDecimal fee = rechargeCard.getCard_money();
						// 充值成功 更新卡状态
						String cardFetchTime = timeStampUtil.getNowDate();
						boolean isUpdateSuccess = rechargeDao.updateRechargeCard(cardCode, userId, userName, from,
								cardFetchTime);
						if (isUpdateSuccess) {
							// 充值成功插入充值信息表
							boolean isInsertByConsumeDeposit = rechargeDao.insertByConsumeDeposit(depositTradeNo,
									userId, fee, payChannel, depositGmtCreate, cardFetchTime, gmtClose,
									depositTradeStatus, depositReturnTradeNo, RechargePackage);
							// 更新用户资产信息
							boolean isUpdateByUserResource = rechargeDao.updateMoney(fee, userId);
							if (isInsertByConsumeDeposit && isUpdateByUserResource) {
								// 插入交易明细表
								boolean isInsertByConsumeRecord = rechargeDao.insertConsumeRecord(orderId, userId,
										userName, fee, date, year, month, day, title, desc, depositGmtCreate, typeId,
										userType, recordStatus, recordPayorder, cardFetchTime);
								// 更改订单充值状态
								if (isInsertByConsumeRecord) {
									int newRecordStatus = 6;// 新订单状态
									rechargeDao.upConsumeRecord(newRecordStatus, recordStatus, orderId);
								}
							}
							rr.setCode(1);
							rr.setDesc("充值成功！");
						} else {
							rr.setCode(2);
							rr.setDesc("充值失败！");
						}
					}
				} else {
					rr.setCode(2);
					rr.setDesc("账号或密码错误！");
				}
			} catch (Exception e) {
				rr.setCode(2);
				rr.setDesc(e.getMessage());
			}
		} else {
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}
}