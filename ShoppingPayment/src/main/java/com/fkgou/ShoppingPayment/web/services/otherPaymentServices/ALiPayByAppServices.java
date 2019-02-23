package com.fkgou.ShoppingPayment.web.services.otherPaymentServices;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkgou.ShoppingPayment.web.config.ALiPayConfig;
import com.fkgou.ShoppingPayment.web.dao.otherPaymentDao.ALiPayByAppDao;
import com.fkgou.ShoppingPayment.web.entity.QueryGoodsPara;
import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.RecordPara;
import com.fkgou.ShoppingPayment.web.util.FkGouConfig;
import com.fkgou.ShoppingPayment.web.util.RedisUtil;
import com.fkgou.ShoppingPayment.web.util.Url;
import com.fkgou.ShoppingPayment.web.util.timeStampUtil;
import com.fkgou.ShoppingPayment.web.util.WXPayUtils.PayToolUtil;
import com.fkgou.userms.entity.UserInfo;

@Service
public class ALiPayByAppServices {

	@Autowired
	ALiPayByAppDao aLiPayByAppDao;

	/**
	 * 支付宝购物支付
	 * 
	 * @param orderId
	 * @return
	 */
	public ResultResp userAliPayToApp(Map<Object, Object> para) {
		ResultResp rr = new ResultResp();
		String orderId = (String) para.get("orderId");
		BigDecimal totalPrice = new BigDecimal(0);
		String goodsName = null;
		// 判断订单号是否为纯数字
		if (PayToolUtil.isInteger(orderId)) {
			QueryGoodsPara queryGoodsPara = aLiPayByAppDao.queryGoodsPriceAndName(orderId);
			totalPrice = queryGoodsPara.getTrade_payment_amount();
			goodsName = queryGoodsPara.getTrade_desc();
		} else {
			RecordPara rp = aLiPayByAppDao.queryRecordPara(orderId);
			totalPrice = rp.getRecord_money();
			goodsName = rp.getRecord_desc();
		}
		String price = String.valueOf(totalPrice);
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(ALiPayConfig.URL, ALiPayConfig.APPID,
					ALiPayConfig.RSA_PRIVATE_KEY, ALiPayConfig.FORMAT, ALiPayConfig.CHARSET,
					ALiPayConfig.ALIPAY_PUBLIC_KEY, ALiPayConfig.SIGNTYPE);
			AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
			model.setOutTradeNo(orderId);// 订单号。
			model.setTimeoutExpress("30m");// 设置超时时间
			model.setTotalAmount(price);// 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]这里调试每次支付1分钱，在项目上线前应将此处改为订单的总金额
			model.setProductCode("QUICK_MSECURITY_PAY");// 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
			model.setBody("");// 对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
			model.setSubject(goodsName);
			request.setBizModel(model);
			request.setNotifyUrl(Url.getURLDecoderString(ALiPayConfig.shopping_notify_url));// 设置回调链接
			// 这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request); // 可以直接给客户端请求，无需再做处理。
			rr.setCode(1);
			rr.setData(response);
		} catch (Exception e) {
			e.printStackTrace();
			rr.setCode(2);
			rr.setDesc("程序异常!");
		}
		return rr;
	}

	/**
	 * 支付宝充值
	 * 
	 * @param para
	 * @return
	 */
	@Transactional
	public ResultResp userAliPayByRechargeToApp(Map<Object, Object> para) {
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
				boolean isInsertSuccessByDeposit = aLiPayByAppDao.createRechargeOrder(depositTradeNo, userId,
						depositTotalFee, depositPayChannel, depositGmtCreate, depositTradeStatus,
						depositIsRechargePackage);
				// 创建订单(consumeRecord)
				boolean isInsertSuccessByRecord = aLiPayByAppDao.createRecordOrder(depositTradeNo, userId, userName,
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
						AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
						AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
						model.setOutTradeNo(depositTradeNo);// 订单号。
						model.setTimeoutExpress("30m");// 设置超时时间
						model.setTotalAmount(depositTotalFee);// 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]这里调试每次支付1分钱，在项目上线前应将此处改为订单的总金额
						model.setProductCode("QUICK_MSECURITY_PAY");// 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
						request.setBizModel(model);
						request.setNotifyUrl(ALiPayConfig.recharge_notify_url);// 设置回调链接
						model.setBody("充值");// 对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
						model.setSubject("充值");
						// 这里和普通的接口调用不同，使用的是sdkExecute
						AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request); // 可以直接给客户端请求，无需再做处理。
						rr.setCode(1);
						rr.setData(response);
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
