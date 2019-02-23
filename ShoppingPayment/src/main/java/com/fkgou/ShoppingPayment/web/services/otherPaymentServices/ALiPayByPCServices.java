package com.fkgou.ShoppingPayment.web.services.otherPaymentServices;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkgou.ShoppingPayment.web.config.ALiPayConfig;
import com.fkgou.ShoppingPayment.web.dao.otherPaymentDao.ALiPayByPcDao;
import com.fkgou.ShoppingPayment.web.entity.QueryGoodsPara;
import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.RecordPara;
import com.fkgou.ShoppingPayment.web.util.FkGouConfig;
import com.fkgou.ShoppingPayment.web.util.RedisUtil;
import com.fkgou.ShoppingPayment.web.util.timeStampUtil;
import com.fkgou.ShoppingPayment.web.util.WXPayUtils.PayToolUtil;
import com.fkgou.userms.entity.UserInfo;

@Service
public class ALiPayByPCServices {

	@Autowired
	ALiPayByPcDao aLiPayByPcDao;

	/**
	 * 支付宝支付pc
	 * 
	 * @param para
	 * @return
	 */
	public ResultResp userAliPayToPc(Map<Object, Object> para, HttpServletRequest request,
			HttpServletResponse response) {
		ResultResp rr = new ResultResp();
		String orderId = (String) para.get("orderId");
		BigDecimal totalPrice = new BigDecimal(0);
		String goodsName = null;
		if (PayToolUtil.isInteger(orderId)) {
			QueryGoodsPara queryGoodsPara = aLiPayByPcDao.queryGoodsPriceAndName(orderId);
			totalPrice = queryGoodsPara.getTrade_payment_amount();
			goodsName = queryGoodsPara.getTrade_desc();
		} else {
			RecordPara rp = aLiPayByPcDao.queryRecordPara(orderId);
			totalPrice = rp.getRecord_money();
			goodsName = rp.getRecord_desc();
		}
		String price = String.valueOf(totalPrice);
		try {
			// 获得初始化的AlipayClient
			AlipayClient alipayClient = new DefaultAlipayClient(ALiPayConfig.URL, ALiPayConfig.APPID,
					ALiPayConfig.RSA_PRIVATE_KEY, ALiPayConfig.FORMAT, ALiPayConfig.CHARSET,
					ALiPayConfig.ALIPAY_PUBLIC_KEY, ALiPayConfig.SIGNTYPE);

			// 设置请求参数
			AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
			alipayRequest.setReturnUrl(ALiPayConfig.shopping_return_url_pc);
			alipayRequest.setNotifyUrl(ALiPayConfig.shopping_notify_url);

			// 商户订单号，商户网站订单系统中唯一订单号，必填
			String out_trade_no = orderId;
			// 付款金额，必填
			String total_amount = price;
			// 订单名称，必填
			String subject = goodsName;
			// 商品描述，可空
			String body = "购物";
			// 封装请求支付信息
			AlipayTradePagePayModel model = new AlipayTradePagePayModel();
			model.setOutTradeNo(out_trade_no);
			model.setTotalAmount(total_amount);
			model.setSubject(subject);
			model.setBody(body);
			model.setTimeoutExpress("30m");
			// 销售产品码 必填
			model.setProductCode("FAST_INSTANT_TRADE_PAY");
			alipayRequest.setBizModel(model);
			// 调用SDK生成表单
			String result = alipayClient.pageExecute(alipayRequest).getBody();
			response.setContentType("text/html;charset=" + ALiPayConfig.CHARSET);
			rr.setCode(1);
			rr.setData(result);
		} catch (Exception e) {
			rr.setCode(2);
			rr.setDesc("错误结果：" + e.getMessage());
		}
		return rr;
	}

	/**
	 * 支付宝充值pc
	 * 
	 * @param para
	 * @return
	 */
	public ResultResp userAliPayByRechargeToPc(Map<Object, Object> para) {
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
				// consumeDepositPara
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
				boolean isInsertSuccessByDeposit = aLiPayByPcDao.createRechargeOrder(depositTradeNo, userId,
						depositTotalFee, depositPayChannel, depositGmtCreate, depositTradeStatus,
						depositIsRechargePackage);
				// 创建订单(consumeRecord)
				boolean isInsertSuccessByRecord = aLiPayByPcDao.createRecordOrder(depositTradeNo, userId, userName,
						depositTotalFee, recordDate, recordYear, recordMonth, recordDay, recordTitle, recordDesc,
						depositGmtCreate, tradeTypeId, userType, depositTradeStatus, recordPayorder);
				if (isInsertSuccessByDeposit && isInsertSuccessByRecord) {
					rr.setCode(1);
					rr.setDesc("创建订单成功！");
					double price = Double.valueOf(depositTotalFee);
					if (price >= 0) {
						AlipayClient alipayClient = new DefaultAlipayClient(ALiPayConfig.URL, ALiPayConfig.APPID,
								ALiPayConfig.RSA_PRIVATE_KEY, ALiPayConfig.FORMAT, ALiPayConfig.CHARSET,
								ALiPayConfig.ALIPAY_PUBLIC_KEY, ALiPayConfig.SIGNTYPE);
						// 设置请求参数
						AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
						alipayRequest.setReturnUrl(ALiPayConfig.recharge_return_url_pc);
						alipayRequest.setNotifyUrl(ALiPayConfig.recharge_notify_url);

						// 商户订单号，商户网站订单系统中唯一订单号，必填
						String out_trade_no = depositTradeNo;
						// 付款金额，必填
						String total_amount = depositTotalFee;
						// 订单名称，必填
						String subject = recordTitle;
						// 商品描述，可空
						String body = recordDesc;
						// 封装请求支付信息
						AlipayTradePagePayModel model = new AlipayTradePagePayModel();
						model.setOutTradeNo(out_trade_no);
						model.setTotalAmount(total_amount);
						model.setSubject(subject);
						model.setBody(body);
						model.setTimeoutExpress("30m");
						// 销售产品码 必填
						model.setProductCode("FAST_INSTANT_TRADE_PAY");
						alipayRequest.setBizModel(model);
						// 调用SDK生成表单
						String result = alipayClient.pageExecute(alipayRequest).getBody();
						rr.setCode(1);
						rr.setData(result);
					} else {
						rr.setCode(2);
						rr.setDesc("付款金额错误！");
						return rr;
					}
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

}
