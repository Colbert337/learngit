package com.fkgou.ShoppingPayment.web.services.otherPaymentServices;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkgou.ShoppingPayment.web.config.ALiPayConfig;
import com.fkgou.ShoppingPayment.web.dao.otherPaymentDao.ALiPayDao;
import com.fkgou.ShoppingPayment.web.entity.QueryGoodsPara;
import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.entity.callBackEntity.PayConsumeDeposit;
import com.fkgou.ShoppingPayment.web.entity.callBackEntity.PayConsumeRecord;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.QueryRecordPara;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.RecordPara;
import com.fkgou.ShoppingPayment.web.services.feginServices.UpdateOrderServices;
import com.fkgou.ShoppingPayment.web.util.FkGouConfig;
import com.fkgou.ShoppingPayment.web.util.RedisUtil;
import com.fkgou.ShoppingPayment.web.util.timeStampUtil;
import com.fkgou.userms.entity.UserInfo;

@Service
public class ALiPayServices {

	@Autowired
	ALiPayDao aLiPayDao;

	@Autowired
	UpdateOrderServices updateOrderServices;

	final static Logger log = LoggerFactory.getLogger(ALiPayServices.class);

	/**
	 * 创建支付宝充值订单
	 * 
	 * @param para
	 * @return
	 */
	@Transactional
	public ResultResp createRechargeOrder(Map<Object, Object> para) {
		ResultResp rr = new ResultResp();
		String token = (String) para.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				Integer userId = ui.getUserId();
				String userName = ui.getUserName();
				String depositTradeNo = "R" + timeStampUtil.getALiTimeStamp();// 订单号
				String depositTotalFee = (String) para.get("money");// 充值金额
				Integer depositPayChannel = 1;// 充值的付款方式
				String depositGmtCreate = timeStampUtil.getNowDate();// 交易创建时间
				Integer depositTradeStatus = 8;// 交易状态
				Integer depositIsRechargePackage = 0;// 是否为套餐充值，1-是；0-普通充值
				// consumeRecordPara
				String recordDate = timeStampUtil.getYear() + "-" + timeStampUtil.getMonth() + "-"
						+ timeStampUtil.getDay();
				String recordYear = timeStampUtil.getYear();
				String recordMonth = timeStampUtil.getMonth();
				String recordDay = timeStampUtil.getDay();
				String recordTitle = "充值";
				String recordDesc = "充值";
				Integer tradeTypeId = 3;// 交易类型
				Integer userType = 2;// 1-收款方 2-付款方
				String recordPayorder = depositTradeNo;// 实际支付单号
				// 创建支付订单(consumeDeposit)
				boolean isInsertSuccessByDeposit = aLiPayDao.createRechargeOrder(depositTradeNo, userId,
						depositTotalFee, depositPayChannel, depositGmtCreate, depositTradeStatus,
						depositIsRechargePackage);
				// 创建订单(consumeRecord)
				boolean isInsertSuccessByRecord = aLiPayDao.createRecordOrder(depositTradeNo, userId, userName,
						depositTotalFee, recordDate, recordYear, recordMonth, recordDay, recordTitle, recordDesc,
						depositGmtCreate, tradeTypeId, userType, depositTradeStatus, recordPayorder);
				if (isInsertSuccessByDeposit && isInsertSuccessByRecord) {
					rr.setCode(1);
					rr.setData(depositTradeNo);
					rr.setDesc("创建订单成功！");
				} else {
					rr.setCode(2);
					rr.setDesc("创建订单失败！");
				}
			} catch (Exception e) {
				rr.setCode(2);
				rr.setDesc(e.getMessage());
			}
		} else {
			rr.setCode(2);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 支付宝充值
	 * 
	 * @param para
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	public ResultResp useAlipayToRecharge(Map<Object, Object> para, HttpServletRequest request,
			HttpServletResponse response) {
		ResultResp rr = new ResultResp();
		String token = (String) para.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				Integer userId = ui.getUserId();
				String tradeNo = (String) para.get("tradeNo");
				BigDecimal money = aLiPayDao.selectMoney(userId, tradeNo);
				double price = Double.valueOf(money.toString());
				String totalAmount = String.valueOf(price);
				if (price >= 0) {
					// 商户订单号，商户网站订单系统中唯一订单号，必填
					String out_trade_no = tradeNo;
					// 订单名称，必填
					String subject = "充值";
					// 付款金额，必填
					String total_amount = totalAmount;
					// 商品描述，可空
					String body = "充值";
					// 超时时间 可空
					String timeout_express = "2m";
					// 销售产品码 必填
					String product_code = "QUICK_WAP_WAY";
					// SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
					// 调用RSA签名方式
					AlipayClient client = new DefaultAlipayClient(ALiPayConfig.URL, ALiPayConfig.APPID,
							ALiPayConfig.RSA_PRIVATE_KEY, ALiPayConfig.FORMAT, ALiPayConfig.CHARSET,
							ALiPayConfig.ALIPAY_PUBLIC_KEY, ALiPayConfig.SIGNTYPE);
					AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

					// 封装请求支付信息
					AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
					model.setOutTradeNo(out_trade_no);
					model.setSubject(subject);
					model.setTotalAmount(total_amount);
					model.setBody(body);
					model.setTimeoutExpress(timeout_express);
					model.setProductCode(product_code);
					alipay_request.setBizModel(model);
					// 设置异步通知地址
					alipay_request.setNotifyUrl(ALiPayConfig.recharge_notify_url);
					// 设置同步地址
					alipay_request.setReturnUrl(ALiPayConfig.recharge_return_url);

					// form表单生产
					String form = "";
					try {
						// 调用SDK生成表单
						form = client.pageExecute(alipay_request).getBody();
						response.setContentType("text/html;charset=" + ALiPayConfig.CHARSET);
						// response.getWriter().write(form);// 直接将完整的表单html输出到页面
						// response.getWriter().flush();
						// response.getWriter().close();
						rr.setCode(1);
						rr.setData(form);
					} catch (AlipayApiException e) {
						e.printStackTrace();
						rr.setDesc(e.getMessage());
					}
				} else {
					rr.setCode(2);
					rr.setDesc("金额错误！");
					return rr;
				}
			} catch (Exception e) {
				rr.setCode(2);
				rr.setDesc(e.getMessage());
			}
		} else {
			rr.setCode(2);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 查询orderId
	 * 
	 * @param out_trade_no
	 * @return
	 */
	public PayConsumeRecord queryorderNo(String out_trade_no) {
		return aLiPayDao.queryorderNo(out_trade_no);
	}

	/**
	 * 更新ComsumeRecord
	 * 
	 * @param recordStatus
	 * @param paymentTime
	 * @param out_trade_no
	 */
	public boolean updateComsumeRecord(Integer recordStatus, String paymentTime, String out_trade_no) {
		return aLiPayDao.updateComsumeRecord(recordStatus, paymentTime, out_trade_no);
	}

	/**
	 * 查询recode订单状态
	 * 
	 * @param out_trade_no
	 * @return
	 */
	public Integer queryRecodeStatus(String out_trade_no) {
		return aLiPayDao.queryRecodeStatus(out_trade_no);
	}

	/**
	 * 更改ConsumeTrade
	 * 
	 * @param orderStateId
	 * @param paymentTime
	 * @param gmt_close
	 */
	public boolean updateConsumeTrade(Integer orderStateId, String paymentTime, String closeTime, String out_trade_no) {
		return aLiPayDao.updateConsumeTrade(orderStateId, paymentTime, closeTime, out_trade_no);
	}

	/**
	 * 查询充值状态
	 * 
	 * @param out_trade_no
	 * @return
	 */
	public Integer queryTradeStatus(String out_trade_no) {
		return aLiPayDao.queryTradeStatus(out_trade_no);
	}

	/**
	 * 查询pdb_pay_consume_deposit订单号及金额
	 * 
	 * @param out_trade_no
	 * @return
	 */
	public PayConsumeDeposit queryPayConsumeDeposit(String out_trade_no) {
		return aLiPayDao.queryPayConsumeDeposit(out_trade_no);
	}

	/**
	 * 更新pdb_pay_consume_deposit
	 * 
	 * @param depositGmtPayment
	 * @param depositGmtClose
	 * @param depositTradeStatus
	 * @param depositReturnTradeNo
	 * @param depositIsRechargePackage
	 * @param out_trade_no
	 * @return
	 */
	public boolean updateDeposit(String depositGmtPayment, String depositGmtClose, Integer depositTradeStatus,
			String depositReturnTradeNo, Integer depositIsRechargePackage, String out_trade_no) {
		return aLiPayDao.updateDeposit(depositGmtPayment, depositGmtClose, depositTradeStatus, depositReturnTradeNo,
				depositIsRechargePackage, out_trade_no);
	}

	/**
	 * 更新pdb_pay_consume_record
	 * 
	 * @param depositGmtPayment
	 * @param out_trade_no
	 * @return
	 */
	public boolean updateRecord(String depositGmtPayment, String out_trade_no) {
		return aLiPayDao.updateRecord(depositGmtPayment, out_trade_no);
	}

	/**
	 * 更新用户资产
	 * 
	 * @param money
	 * @param userId
	 * @return
	 */
	public boolean updateUserResource(String money, Integer userId) {
		return aLiPayDao.updateUserResource(money, userId);
	}

	/**
	 * 查询订单号，商品名与价格
	 * 
	 * @param orderId
	 * @return
	 */
	public QueryRecordPara queryRecordPriceAndName(String orderId) {
		return aLiPayDao.queryRecordPriceAndName(orderId);
	}

	/**
	 * 查询商品名与价格
	 * 
	 * @param orderId
	 * @return
	 */
	public QueryGoodsPara queryGoodsPriceAndName(String orderId) {
		return aLiPayDao.queryGoodsPriceAndName(orderId);
	}

	/**
	 * 查询商品价格与商品名
	 * 
	 * @param orderId
	 * @return
	 */
	public RecordPara queryRecordPara(String orderId) {
		return aLiPayDao.queryRecordPara(orderId);
	}

	/**
	 * 更改状态record
	 * 
	 * @param recordStatus
	 * @param paymentTime
	 * @param out_trade_no
	 * @return
	 */
	public boolean updateComsumeRecordByPureDigital(Integer recordStatus, String paymentTime, String out_trade_no) {
		return aLiPayDao.updateComsumeRecordByPureDigital(recordStatus, paymentTime, out_trade_no);
	}

	/**
	 * 更改状态trade
	 * 
	 * @param orderStateId
	 * @param paymentTime
	 * @param closeTime
	 * @param out_trade_no
	 * @return
	 */
	public boolean updateConsumeTradeByPureDigital(Integer orderStateId, String paymentTime, String closeTime,
			String out_trade_no) {
		return aLiPayDao.updateConsumeTradeByPureDigital(orderStateId, paymentTime, closeTime, out_trade_no);
	}

}
