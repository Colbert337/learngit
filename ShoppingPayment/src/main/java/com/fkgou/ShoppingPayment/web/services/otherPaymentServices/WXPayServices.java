package com.fkgou.ShoppingPayment.web.services.otherPaymentServices;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkgou.ShoppingPayment.web.config.WXPayConfig;
import com.fkgou.ShoppingPayment.web.dao.otherPaymentDao.WXPayDao;
import com.fkgou.ShoppingPayment.web.entity.QueryGoodsPara;
import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.entity.callBackEntity.PayConsumeRecord;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.QueryRecordPara;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.RechargeCallBackPara;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.RecordPara;
import com.fkgou.ShoppingPayment.web.services.feginServices.MallsServices;
import com.fkgou.ShoppingPayment.web.services.feginServices.OrderServices;
import com.fkgou.ShoppingPayment.web.services.feginServices.UpdateOrderServices;
import com.fkgou.ShoppingPayment.web.util.FkGouConfig;
import com.fkgou.ShoppingPayment.web.util.RedisUtil;
import com.fkgou.ShoppingPayment.web.util.Url;
import com.fkgou.ShoppingPayment.web.util.timeStampUtil;
import com.fkgou.ShoppingPayment.web.util.WXPayUtils.PayCommonUtil;
import com.fkgou.ShoppingPayment.web.util.WXPayUtils.PayToolUtil;
import com.fkgou.ShoppingPayment.web.util.WXPayUtils.StringUtil;
import com.fkgou.userms.entity.UserInfo;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.PaymentApi;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.jfinal.weixin.sdk.utils.JsonUtils;

@Service
public class WXPayServices {

	@Autowired
	WXPayDao wxPayDao;

	@Autowired
	UpdateOrderServices updateOrderServices;

	@Autowired
	MallsServices mallsServices;

	@Autowired
	OrderServices orderServices;

	final static Logger log = LoggerFactory.getLogger(WXPayServices.class);

	/**
	 * 查询商品名与商品价格
	 * 
	 * @param out_trade_no
	 * @return
	 */
	public QueryGoodsPara queryGoodsPriceAndName(String out_trade_no) {
		return wxPayDao.queryGoodsPriceAndName(out_trade_no);
	}

	/**
	 * 微信支付回调
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	public String notifyWeiXinPay(HttpServletRequest request, HttpServletResponse response) {
		log.info("------支付成功，进入回调------");
		try {
			InputStream inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			String resultxml = new String(outSteam.toByteArray(), "utf-8");
			log.info("resultxml:" + resultxml);
			@SuppressWarnings("unchecked")
			Map<String, String> params = PayCommonUtil.doXMLParse(resultxml);
			outSteam.close();
			inStream.close();
			Map<String, String> return_data = new HashMap<String, String>();
			if (!PayCommonUtil.isTenpaySign(params)) {
				// 支付失败
				log.info("------支付失败------");
				return_data.put("return_code", "FAIL");
				return_data.put("return_msg", "return_code不正确");
				log.info("return_data：" + StringUtil.GetMapToXML(return_data));
				return StringUtil.GetMapToXML(return_data);
			} else {
				System.out.println("===============付款成功==============");
				log.info("===============付款成功==============");
				// ------------------------------
				// 处理业务开始
				// ------------------------------
				// 此处处理订单状态，结合自己的订单数据完成订单状态的更新
				// ------------------------------
				String out_trade_no = params.get("out_trade_no");
				log.info("-----out_trade_no-----" + out_trade_no);
				String total_fee = params.get("total_fee");// 获取wx传来的订单价格
				String totalPrice = PayToolUtil.changeF2Y(total_fee);// 分换元
				BigDecimal tp = new BigDecimal(totalPrice);
				log.info("-----total_fee-----" + total_fee);
				log.info("-----totalPrice-----" + totalPrice);
				String mch_id = params.get("mch_id");// 微信支付分配的商户号
				log.info("-----mch_id-----" + mch_id);
				String transaction_id = params.get("transaction_id");// 微信支付订单号
				log.info("-----transaction_id-----" + transaction_id);
				// 判断纯数字订单，实际支付订单
				if (PayToolUtil.isInteger(out_trade_no)) {
					QueryRecordPara queryGoodsPara = wxPayDao.queryRecordPriceAndName(out_trade_no);
					String orderId = queryGoodsPara.getOrder_id();
					BigDecimal recordMoney = queryGoodsPara.getTrade_payment_amount();
					if (out_trade_no.equals(orderId) && tp.compareTo(recordMoney) == 0
							&& mch_id.equals(WXPayConfig.MCH_ID)) {
						log.info("----判断成功----");
						Map<Object, Object> map = new HashMap<Object, Object>();
						Integer payCode = 3;
						Integer orderStatus = 3;// 待发货
						Integer paymentId = 1;// 支付方式
						String paymentName = "在线支付";// 支付方式名称
						String paymentTime = timeStampUtil.getNowDate();// 支付时间
						String paymentOtherNumber = transaction_id;// 第三方支付号
						map.put("payCode", payCode);
						map.put("paymentId", paymentId);
						map.put("paymentNumber", out_trade_no);// 商户订单号
						map.put("orderStatus", orderStatus);
						map.put("paymentName", paymentName);
						map.put("paymentTime", paymentTime);
						map.put("paymentOtherNumber", paymentOtherNumber);
						log.info("-----feign调用-----");
						boolean a = updateOrderServices.HFiveUpdateOrderBaseByPayment(map);
						log.info("-----a-----" + a);
						// 更改支付表状态ConsumeRecord
						Integer recordStatus = 3;
						log.info("----1-----");
						boolean updateConsumeRecord = wxPayDao.updateComsumeRecordByPureDigital(recordStatus,
								paymentTime, out_trade_no);
						log.info("----updateConsumeRecord----" + updateConsumeRecord);
						// 更改ConsumeTrade
						Integer orderStateId = 3;
						String closeTime = timeStampUtil.getNowDate();
						log.info("----1-----");
						boolean updateConsumeTrade = wxPayDao.updateConsumeTradeByPureDigital(orderStateId, paymentTime,
								closeTime, out_trade_no);
						log.info("----updateConsumeTrade----" + updateConsumeTrade);
						if (updateConsumeRecord && updateConsumeTrade) {
							log.info("----更新成功----");
							return_data.put("return_code", "SUCCESS");
							return_data.put("return_msg", "OK");
							return StringUtil.GetMapToXML(return_data);
						} else {
							log.info("----更新失败----");
							return_data.put("return_code", "FAIL");
							return_data.put("return_msg", "更新失败");
							return StringUtil.GetMapToXML(return_data);
						}
					} else {
						log.info("订单号或订单金额不符！");
						return_data.put("return_code", "FAIL");
						return_data.put("return_msg", "订单号或订单金额不符！");
						return StringUtil.GetMapToXML(return_data);
					}
				} else {
					// 支付订单号
					PayConsumeRecord payConsumeRecord = wxPayDao.queryOrderNo(out_trade_no);
					log.info("----payConsumeRecord----" + payConsumeRecord.toString());
					String orderId = payConsumeRecord.getOrder_id();
					BigDecimal recordMoney = payConsumeRecord.getRecord_money();
					log.info("----进入循环----");
					log.info("-----orderId-----" + orderId);
					log.info("-----recordMoney-----" + recordMoney);
					log.info("-----orderId-----" + orderId);
					log.info("-----进入 判断-----");
					if (out_trade_no.equals(orderId) && tp.compareTo(recordMoney) == 0
							&& mch_id.equals(WXPayConfig.MCH_ID)) {
						log.info("----判断成功----");
						Map<Object, Object> map = new HashMap<Object, Object>();
						Integer payCode = 3;
						Integer orderStatus = 3;// 待发货
						Integer paymentId = 1;// 支付方式
						String paymentName = "在线支付";// 支付方式名称
						String paymentTime = timeStampUtil.getNowDate();// 支付时间
						String paymentOtherNumber = transaction_id;// 第三方支付号
						map.put("payCode", payCode);
						map.put("paymentId", paymentId);
						map.put("paymentNumber", out_trade_no);// 商户订单号
						map.put("orderStatus", orderStatus);
						map.put("paymentName", paymentName);
						map.put("paymentTime", paymentTime);
						map.put("paymentOtherNumber", paymentOtherNumber);
						log.info("-----feign调用-----");
						boolean updatePayment = updateOrderServices.HFiveUpdateOrderBaseByPayment(map);
						log.info("-----updatePayment-----" + updatePayment);
						// 更改支付表状态ConsumeRecord
						Integer recordStatus = 3;
						log.info("----1-----");
						boolean updateConsumeRecord = wxPayDao.updateComsumeRecord(recordStatus, paymentTime,
								out_trade_no);
						log.info("----updateConsumeRecord----" + updateConsumeRecord);
						// 更改ConsumeTrade
						Integer orderStateId = 3;
						String closeTime = timeStampUtil.getNowDate();
						log.info("----1-----");
						boolean updateConsumeTrade = wxPayDao.updateConsumeTrade(orderStateId, paymentTime, closeTime,
								out_trade_no);
						log.info("----updateConsumeTrade----" + updateConsumeTrade);
						if (updateConsumeRecord && updateConsumeTrade) {
							log.info("----更新成功----");
							return_data.put("return_code", "SUCCESS");
							return_data.put("return_msg", "OK");
							return StringUtil.GetMapToXML(return_data);
						} else {
							log.info("----更新失败----");
							return_data.put("return_code", "FAIL");
							return_data.put("return_msg", "更新失败");
							return StringUtil.GetMapToXML(return_data);
						}
					} else {
						log.info("订单号或订单金额不符！");
						return_data.put("return_code", "FAIL");
						return_data.put("return_msg", "订单号或订单金额不符！");
						return StringUtil.GetMapToXML(return_data);
					}
				}
			}
		} catch (Exception e) {
			log.info("错误结果：" + e.getMessage());
		}
		return "OK";
	}

	/**
	 * 微信支付回调WAP
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	public String notifyWeiXinPayByWAP(HttpServletRequest request, HttpServletResponse response) {
		log.info("------进入回调------");
		try {
			InputStream inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			String resultxml = new String(outSteam.toByteArray(), "utf-8");
			log.info("resultxml:" + resultxml);
			@SuppressWarnings("unchecked")
			Map<String, String> params = PayCommonUtil.doXMLParse(resultxml);
			outSteam.close();
			inStream.close();
			Map<String, String> return_data = new HashMap<String, String>();
			if (!PayCommonUtil.isTenpaySignWAP(params)) {
				// 支付失败
				log.info("------支付失败------");
				return_data.put("return_code", "FAIL");
				return_data.put("return_msg", "return_code不正确");
				log.info("return_data：" + StringUtil.GetMapToXML(return_data));
				return StringUtil.GetMapToXML(return_data);
			} else {
				System.out.println("===============付款成功==============");
				log.info("===============付款成功==============");
				// ------------------------------
				// 处理业务开始
				// ------------------------------
				// 此处处理订单状态，结合自己的订单数据完成订单状态的更新
				// ------------------------------
				String out_trade_no = params.get("out_trade_no");
				log.info("-----out_trade_no-----" + out_trade_no);
				String total_fee = params.get("total_fee");// 获取wx传来的订单价格
				String totalPrice = PayToolUtil.changeF2Y(total_fee);// 分换元
				BigDecimal tp = new BigDecimal(totalPrice);
				log.info("-----total_fee-----" + total_fee);
				log.info("-----totalPrice-----" + totalPrice);
				String mch_id = params.get("mch_id");// 微信支付分配的商户号
				log.info("-----mch_id-----" + mch_id);
				String transaction_id = params.get("transaction_id");// 微信支付订单号
				log.info("-----transaction_id-----" + transaction_id);
				if (PayToolUtil.isInteger(out_trade_no)) {
					QueryRecordPara queryGoodsPara = wxPayDao.queryRecordPriceAndName(out_trade_no);
					String orderId = queryGoodsPara.getOrder_id();
					BigDecimal recordMoney = queryGoodsPara.getTrade_payment_amount();
					if (out_trade_no.equals(orderId) && tp.compareTo(recordMoney) == 0
							&& mch_id.equals(WXPayConfig.MCH_ID)) {
						log.info("----判断成功----");
						Map<Object, Object> map = new HashMap<Object, Object>();
						Integer payCode = 3;
						Integer orderStatus = 3;// 待发货
						Integer paymentId = 1;// 支付方式
						String paymentName = "在线支付";// 支付方式名称
						String paymentTime = timeStampUtil.getNowDate();// 支付时间
						String paymentOtherNumber = transaction_id;// 第三方支付号
						map.put("payCode", payCode);
						map.put("paymentId", paymentId);
						map.put("paymentNumber", out_trade_no);// 商户订单号
						map.put("orderStatus", orderStatus);
						map.put("paymentName", paymentName);
						map.put("paymentTime", paymentTime);
						map.put("paymentOtherNumber", paymentOtherNumber);
						log.info("-----feign调用-----");
						boolean a = updateOrderServices.HFiveUpdateOrderBaseByPayment(map);
						log.info("-----a-----" + a);
						// 更改支付表状态ConsumeRecord
						Integer recordStatus = 3;
						log.info("----1-----");
						boolean updateConsumeRecord = wxPayDao.updateComsumeRecordByPureDigital(recordStatus,
								paymentTime, out_trade_no);
						log.info("----updateConsumeRecord----" + updateConsumeRecord);
						// 更改ConsumeTrade
						Integer orderStateId = 3;
						String closeTime = timeStampUtil.getNowDate();
						log.info("----1-----");
						boolean updateConsumeTrade = wxPayDao.updateConsumeTradeByPureDigital(orderStateId, paymentTime,
								closeTime, out_trade_no);
						log.info("----updateConsumeTrade----" + updateConsumeTrade);
						if (updateConsumeRecord && updateConsumeTrade) {
							log.info("----更新成功----");
							return_data.put("return_code", "SUCCESS");
							return_data.put("return_msg", "OK");
							return StringUtil.GetMapToXML(return_data);
						} else {
							log.info("----更新失败----");
							return_data.put("return_code", "FAIL");
							return_data.put("return_msg", "更新失败");
							return StringUtil.GetMapToXML(return_data);
						}
					} else {
						log.info("订单号或订单金额不符！");
						return_data.put("return_code", "FAIL");
						return_data.put("return_msg", "订单号或订单金额不符！");
						return StringUtil.GetMapToXML(return_data);
					}
				} else {
					PayConsumeRecord payConsumeRecord = wxPayDao.queryOrderNo(out_trade_no);
					log.info("----payConsumeRecord----" + payConsumeRecord.toString());
					String orderId = payConsumeRecord.getOrder_id();
					BigDecimal recordMoney = payConsumeRecord.getRecord_money();
					log.info("----进入循环----");
					log.info("-----orderId-----" + orderId);
					log.info("-----recordMoney-----" + recordMoney);
					log.info("-----orderId-----" + orderId);
					log.info("-----进入 判断-----");
					if (out_trade_no.equals(orderId) && tp.compareTo(recordMoney) == 0
							&& mch_id.equals(WXPayConfig.WAP_MCH_ID)) {
						log.info("----判断成功----");
						Map<Object, Object> map = new HashMap<Object, Object>();
						Integer payCode = 3;
						Integer orderStatus = 3;// 待发货
						Integer paymentId = 1;// 支付方式
						String paymentName = "在线支付";// 支付方式名称
						String paymentTime = timeStampUtil.getNowDate();// 支付时间
						String paymentOtherNumber = transaction_id;// 第三方支付号
						map.put("payCode", payCode);
						map.put("paymentId", paymentId);
						map.put("paymentNumber", out_trade_no);// 商户订单号
						map.put("orderStatus", orderStatus);
						map.put("paymentName", paymentName);
						map.put("paymentTime", paymentTime);
						map.put("paymentOtherNumber", paymentOtherNumber);
						log.info("-----feign调用-----");
						boolean a = updateOrderServices.HFiveUpdateOrderBaseByPayment(map);
						log.info("-----a-----" + a);
						// 更改支付表状态ConsumeRecord
						Integer recordStatus = 3;
						log.info("----1-----");
						boolean updateConsumeRecord = wxPayDao.updateComsumeRecord(recordStatus, paymentTime,
								out_trade_no);
						log.info("----updateConsumeRecord----" + updateConsumeRecord);
						// 更改ConsumeTrade
						Integer orderStateId = 3;
						String closeTime = timeStampUtil.getNowDate();
						log.info("----1-----");
						boolean updateConsumeTrade = wxPayDao.updateConsumeTrade(orderStateId, paymentTime, closeTime,
								out_trade_no);
						log.info("----updateConsumeTrade----" + updateConsumeTrade);
						if (updateConsumeRecord && updateConsumeTrade) {
							log.info("----更新成功----");
							return_data.put("return_code", "SUCCESS");
							return_data.put("return_msg", "OK");
							return StringUtil.GetMapToXML(return_data);
						} else {
							log.info("----更新失败----");
							return_data.put("return_code", "FAIL");
							return_data.put("return_msg", "更新失败");
							return StringUtil.GetMapToXML(return_data);
						}
					} else {
						log.info("订单号或订单金额不符！");
						return_data.put("return_code", "FAIL");
						return_data.put("return_msg", "订单号或订单金额不符！");
						return StringUtil.GetMapToXML(return_data);
					}
				}
			}
		} catch (Exception e) {
			log.info("错误结果：" + e.getMessage());
		}
		return "OK";
	}

	/**
	 * 微信充值
	 * 
	 * @param map
	 * @return
	 */
	@Transactional
	public ResultResp userWXPayToRecharge(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
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
				String depositTotalFee = (String) map.get("money");// 充值金额
				Integer depositPayChannel = 1;// 充值的付款方式
				String depositGmtCreate = timeStampUtil.getNowDate();// 交易创建时间
				Integer depositTradeStatus = 1;// 交易状态
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
				boolean isInsertSuccessByDeposit = wxPayDao.createRechargeOrder(depositTradeNo, userId, depositTotalFee,
						depositPayChannel, depositGmtCreate, depositTradeStatus, depositIsRechargePackage);
				// 创建订单(consumeRecord)
				boolean isInsertSuccessByRecord = wxPayDao.createRecordOrder(depositTradeNo, userId, userName,
						depositTotalFee, recordDate, recordYear, recordMonth, recordDay, recordTitle, recordDesc,
						depositGmtCreate, tradeTypeId, userType, depositTradeStatus, recordPayorder);
				if (isInsertSuccessByDeposit && isInsertSuccessByRecord) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("appid", WXPayConfig.APP_ID);
					params.put("mch_id", WXPayConfig.MCH_ID);
					params.put("nonce_str", PayToolUtil.create_nonce_str());
					params.put("body", "充值");
					params.put("out_trade_no", depositTradeNo);
					params.put("total_fee", PayToolUtil.getMoney(depositTotalFee));

					params.put("spbill_create_ip", PayToolUtil.localIp());
					params.put("notify_url", WXPayConfig.NOTIFY_URL_By_Recharge);
					params.put("trade_type", "APP");

					String sign = PaymentKit.createSign(params, WXPayConfig.API_KEY);
					params.put("sign", sign);

					String xmlResult = PaymentApi.pushOrder(params);

					Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

					String return_code = result.get("return_code");
					String return_msg = result.get("return_msg");
					if (StrKit.isBlank(return_code) || !"SUCCESS".equals(return_code)) {
						rr.setCode(2);
						rr.setDesc(return_msg);
						return rr;
					}
					String result_code = result.get("result_code");
					if (StrKit.isBlank(result_code) || !"SUCCESS".equals(result_code)) {
						rr.setCode(2);
						rr.setDesc(return_msg);
						return rr;
					}
					// 以下字段在return_code 和result_code都为SUCCESS的时候有返回
					String prepay_id = result.get("prepay_id");
					// 封装调起微信支付的参数
					// https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12
					Map<String, String> packageParams = new HashMap<String, String>();
					packageParams.put("appid", WXPayConfig.APP_ID);
					packageParams.put("partnerid", WXPayConfig.MCH_ID);
					packageParams.put("prepayid", prepay_id);
					packageParams.put("package", "Sign=WXPay");
					packageParams.put("noncestr", PayToolUtil.create_nonce_str());
					packageParams.put("timestamp", System.currentTimeMillis() / 1000 + "");
					String packageSign = PaymentKit.createSign(packageParams, WXPayConfig.API_KEY);
					packageParams.put("sign", packageSign);

					rr.setCode(1);
					rr.setData(packageParams);
				} else {
					rr.setCode(2);
					rr.setDesc("失败");
				}
			} catch (Exception e) {
				log.info("错误结果：" + e.getMessage());
			}
		} else {
			rr.setCode(2);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 微信充值回调
	 * 
	 * @param map
	 * @return
	 */
	@Transactional
	public String notifyWXPayToRecharge(HttpServletRequest request, HttpServletResponse response) {
		log.info("------微信充值支付成功，进入回调------");
		try {
			InputStream inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			String resultxml = new String(outSteam.toByteArray(), "utf-8");
			log.info("resultxml:" + resultxml);
			@SuppressWarnings("unchecked")
			Map<String, String> params = PayCommonUtil.doXMLParse(resultxml);
			outSteam.close();
			inStream.close();
			Map<String, String> return_data = new HashMap<String, String>();
			if (!PayCommonUtil.isTenpaySign(params)) {
				// 支付失败
				log.info("------支付失败------");
				return_data.put("return_code", "FAIL");
				return_data.put("return_msg", "return_code不正确");
				log.info("return_data：" + StringUtil.GetMapToXML(return_data));
				return StringUtil.GetMapToXML(return_data);
			} else {
				log.info("===============付款成功==============");
				// ------------------------------
				// 处理业务开始
				// ------------------------------
				// 此处处理订单状态，结合自己的订单数据完成订单状态的更新
				// ------------------------------
				String out_trade_no = params.get("out_trade_no");
				log.info("-----out_trade_no-----" + out_trade_no);
				String total_fee = params.get("total_fee");// 获取wx传来的订单价格
				BigDecimal totalFee = new BigDecimal(total_fee);// 类型转换
				BigDecimal totalPrice = new BigDecimal(0);// 定义变量
				BigDecimal divisor = new BigDecimal(100);// 定义除数为100,以便将分换算为元
				totalPrice = totalFee.divide(divisor);// 相除得到元
				String fee = totalPrice.toString();
				log.info("-----total_fee-----" + total_fee);
				log.info("-----totalPrice-----" + totalPrice);
				String mch_id = params.get("mch_id");// 微信支付分配的商户号
				log.info("-----mch_id-----" + mch_id);
				String transaction_id = params.get("transaction_id");// 微信支付订单号
				log.info("-----transaction_id-----" + transaction_id);
				RechargeCallBackPara rcbp = wxPayDao.queryRechargeOrder(out_trade_no);
				if (rcbp != null) {
					String orderId = rcbp.getOrder_id();
					log.info("-----orderId-----" + orderId);
					String totalMoney = rcbp.getRecord_money();
					log.info("-----totalMoney-----" + totalMoney);
					if (out_trade_no.equals(orderId) && fee.equals(totalMoney) && mch_id.equals(WXPayConfig.MCH_ID)) {
						log.info("-----匹配成功-----");
						log.info("-----------");
						// 更新pdb_pay_consume_deposit所需参数
						String depositGmtPayment = timeStampUtil.getNowDate();
						String depositGmtClose = timeStampUtil.getNowDate();
						Integer depositTradeStatus = 6;
						String depositReturnTradeNo = transaction_id;
						Integer depositIsRechargePackage = 0;
						log.info("-----开始更新-----");
						boolean isUpdateDepositSuccess = wxPayDao.updateDeposit(depositGmtPayment, depositGmtClose,
								depositTradeStatus, depositReturnTradeNo, depositIsRechargePackage, out_trade_no);
						log.info("-----开始更新-----");
						boolean isUpdateRecord = wxPayDao.updateRecord(depositGmtPayment, out_trade_no);
						log.info("-----判断是否成功-----");
						// 更新成功，增加金额
						if (isUpdateDepositSuccess && isUpdateRecord) {
							String money = rcbp.getRecord_money();
							Integer userId = rcbp.getUser_id();
							wxPayDao.updateUserResource(money, userId);
							log.info("----更新成功----");
							return_data.put("return_code", "SUCCESS");
							return_data.put("return_msg", "OK");
							return StringUtil.GetMapToXML(return_data);
						} else {
							log.info("----更新失败----");
							return_data.put("return_code", "FAIL");
							return_data.put("return_msg", "更新失败");
							return StringUtil.GetMapToXML(return_data);
						}
					} else {
						log.info("订单号或订单金额不符！");
						return_data.put("return_code", "FAIL");
						return_data.put("return_msg", "订单号或订单金额不符！");
						return StringUtil.GetMapToXML(return_data);
					}
				} else {
					log.info("获取数据为空！");
					return_data.put("return_code", "FAIL");
					return_data.put("return_msg", "获取数据为空！");
					return StringUtil.GetMapToXML(return_data);
				}

			}
		} catch (Exception e) {
			log.info("错误结果：" + e.getMessage());
		}
		return "OK";
	}

	/**
	 * wap端微信充值回调
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String notifyWXPayToRechargeByWap(HttpServletRequest request, HttpServletResponse response) {
		log.info("------支付成功，进入回调------");
		try {
			InputStream inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			String resultxml = new String(outSteam.toByteArray(), "utf-8");
			log.info("resultxml:" + resultxml);
			@SuppressWarnings("unchecked")
			Map<String, String> params = PayCommonUtil.doXMLParse(resultxml);
			outSteam.close();
			inStream.close();
			Map<String, String> return_data = new HashMap<String, String>();
			if (!PayCommonUtil.isTenpaySignWAP(params)) {
				// 支付失败
				log.info("------支付失败------");
				return_data.put("return_code", "FAIL");
				return_data.put("return_msg", "return_code不正确");
				log.info("return_data：" + StringUtil.GetMapToXML(return_data));
				return StringUtil.GetMapToXML(return_data);
			} else {
				System.out.println("===============付款成功==============");
				log.info("===============付款成功==============");
				// ------------------------------
				// 处理业务开始
				// ------------------------------
				// 此处处理订单状态，结合自己的订单数据完成订单状态的更新
				// ------------------------------
				String out_trade_no = params.get("out_trade_no");
				log.info("-----out_trade_no-----" + out_trade_no);
				String total_fee = params.get("total_fee");// 获取wx传来的订单价格
				BigDecimal totalFee = new BigDecimal(total_fee);// 类型转换
				BigDecimal totalPrice = new BigDecimal(0);// 定义变量
				BigDecimal divisor = new BigDecimal(100);// 定义除数为100,以便将分换算为元
				totalPrice = totalFee.divide(divisor);// 相除得到元
				String fee = totalPrice.toString();
				log.info("-----total_fee-----" + total_fee);
				log.info("-----totalPrice-----" + totalPrice);
				String mch_id = params.get("mch_id");// 微信支付分配的商户号
				log.info("-----mch_id-----" + mch_id);
				String transaction_id = params.get("transaction_id");// 微信支付订单号
				log.info("-----transaction_id-----" + transaction_id);
				RechargeCallBackPara rcbp = wxPayDao.queryRechargeOrder(out_trade_no);
				if (rcbp != null) {
					String orderId = rcbp.getOrder_id();
					log.info("-----orderId-----" + orderId);
					String totalMoney = rcbp.getRecord_money();
					log.info("-----totalMoney-----" + totalMoney);
					if (out_trade_no.equals(orderId) && fee.equals(totalMoney) && mch_id.equals(WXPayConfig.MCH_ID)) {
						log.info("-----匹配成功-----");
						log.info("-----------");
						// 更新pdb_pay_consume_deposit所需参数
						String depositGmtPayment = timeStampUtil.getNowDate();
						String depositGmtClose = timeStampUtil.getNowDate();
						Integer depositTradeStatus = 6;
						String depositReturnTradeNo = transaction_id;
						Integer depositIsRechargePackage = 0;
						log.info("-----开始更新-----");
						boolean isUpdateDepositSuccess = wxPayDao.updateDeposit(depositGmtPayment, depositGmtClose,
								depositTradeStatus, depositReturnTradeNo, depositIsRechargePackage, out_trade_no);
						log.info("-----开始更新-----");
						boolean isUpdateRecord = wxPayDao.updateRecord(depositGmtPayment, out_trade_no);
						log.info("-----判断是否成功-----");
						// 更新成功，增加金额
						if (isUpdateDepositSuccess && isUpdateRecord) {
							String money = rcbp.getRecord_money();
							Integer userId = rcbp.getUser_id();
							wxPayDao.updateUserResource(money, userId);
							log.info("----更新成功----");
							return_data.put("return_code", "SUCCESS");
							return_data.put("return_msg", "OK");
							return StringUtil.GetMapToXML(return_data);
						} else {
							log.info("----更新失败----");
							return_data.put("return_code", "FAIL");
							return_data.put("return_msg", "更新失败");
							return StringUtil.GetMapToXML(return_data);
						}
					} else {
						log.info("订单号或订单金额不符！");
						return_data.put("return_code", "FAIL");
						return_data.put("return_msg", "订单号或订单金额不符！");
						return StringUtil.GetMapToXML(return_data);
					}
				} else {
					log.info("获取数据为空！");
					return_data.put("return_code", "FAIL");
					return_data.put("return_msg", "获取数据为空！");
					return StringUtil.GetMapToXML(return_data);
				}

			}
		} catch (Exception e) {
			log.info("错误结果：" + e.getMessage());
		}
		return "OK";
	}

	/**
	 * pc端微信购物支付
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp userWXPayToPayByPc(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String out_trade_no = (String) map.get("orderId");
		BigDecimal totalPrice = new BigDecimal(0);
		String goodsName = null;
		// 查询商品名与商品价格
		if (PayToolUtil.isInteger(out_trade_no)) {
			QueryGoodsPara queryGoodsPara = wxPayDao.queryGoodsPriceAndName(out_trade_no);
			totalPrice = queryGoodsPara.getTrade_payment_amount();// 总价
			goodsName = queryGoodsPara.getTrade_desc();
		} else {
			RecordPara rp = wxPayDao.queryRecordPara(out_trade_no);
			totalPrice = rp.getRecord_money();
			goodsName = rp.getRecord_desc();
		}
		String payAmount = String.valueOf(totalPrice);
		String price = PayToolUtil.getMoney(payAmount);
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", WXPayConfig.APP_ID);
		params.put("mch_id", WXPayConfig.MCH_ID);
		params.put("nonce_str", PayToolUtil.create_nonce_str());
		params.put("body", goodsName);
		params.put("out_trade_no", out_trade_no);
		params.put("total_fee", price);

		params.put("spbill_create_ip", PayToolUtil.localIp());
		params.put("notify_url", WXPayConfig.NOTIFY_URL);
		params.put("trade_type", "NATIVE");

		String sign = PaymentKit.createSign(params, WXPayConfig.API_KEY);
		params.put("sign", sign);

		String xmlResult = PaymentApi.pushOrder(params);

		Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

		String return_code = result.get("return_code");
		String return_msg = result.get("return_msg");
		if (StrKit.isBlank(return_code) || !"SUCCESS".equals(return_code)) {
			rr.setCode(2);
			rr.setDesc(return_msg);
			return rr;
		}
		String result_code = result.get("result_code");
		if (StrKit.isBlank(result_code) || !"SUCCESS".equals(result_code)) {
			rr.setCode(2);
			rr.setDesc(return_msg);
			return rr;
		}

		String qrCodeUrl = result.get("code_url");
		rr.setCode(1);
		rr.setData(qrCodeUrl);
		return rr;
	}

	/**
	 * 微信充值pc
	 * 
	 * @param map
	 * @return
	 */
	@Transactional
	public ResultResp userWXPayToRechargeByPc(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
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
				String depositTotalFee = (String) map.get("money");// 充值金额
				Integer depositPayChannel = 1;// 充值的付款方式
				String depositGmtCreate = timeStampUtil.getNowDate();// 交易创建时间
				Integer depositTradeStatus = 1;// 交易状态
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
				boolean isInsertSuccessByDeposit = wxPayDao.createRechargeOrder(depositTradeNo, userId, depositTotalFee,
						depositPayChannel, depositGmtCreate, depositTradeStatus, depositIsRechargePackage);
				// 创建订单(consumeRecord)
				boolean isInsertSuccessByRecord = wxPayDao.createRecordOrder(depositTradeNo, userId, userName,
						depositTotalFee, recordDate, recordYear, recordMonth, recordDay, recordTitle, recordDesc,
						depositGmtCreate, tradeTypeId, userType, depositTradeStatus, recordPayorder);
				if (isInsertSuccessByDeposit && isInsertSuccessByRecord) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("appid", WXPayConfig.APP_ID);
					params.put("mch_id", WXPayConfig.MCH_ID);
					params.put("nonce_str", PayToolUtil.create_nonce_str());
					params.put("body", "充值");
					params.put("out_trade_no", depositTradeNo);
					params.put("total_fee", PayToolUtil.getMoney(depositTotalFee));

					params.put("spbill_create_ip", PayToolUtil.localIp());
					params.put("notify_url", WXPayConfig.NOTIFY_URL_By_Recharge);
					params.put("trade_type", "NATIVE");

					String sign = PaymentKit.createSign(params, WXPayConfig.API_KEY);
					params.put("sign", sign);

					String xmlResult = PaymentApi.pushOrder(params);

					Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

					String return_code = result.get("return_code");
					String return_msg = result.get("return_msg");
					if (StrKit.isBlank(return_code) || !"SUCCESS".equals(return_code)) {
						rr.setCode(2);
						rr.setDesc(return_msg);
						return rr;
					}
					String result_code = result.get("result_code");
					if (StrKit.isBlank(result_code) || !"SUCCESS".equals(result_code)) {
						rr.setCode(2);
						rr.setDesc(return_msg);
						return rr;
					}

					String qrCodeUrl = result.get("code_url");
					rr.setCode(1);
					rr.setData(qrCodeUrl);
					rr.setDesc(depositTradeNo);
				} else {
					rr.setCode(2);
					rr.setDesc("失败");
				}
			} catch (Exception e) {
				log.info("错误结果：" + e.getMessage());
			}
		} else {
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 轮询微信支付后状态
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp queryWxPayStatus(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				Integer userId = ui.getUserId();
				String orderId = (String) map.get("orderId");
				Integer status = wxPayDao.queryStatus(orderId, userId);
				if (status == 1) {
					rr.setCode(2);
					rr.setDesc("状态未更改成功！");
				} else {
					rr.setCode(1);
					rr.setDesc("状态更改成功！");
				}
			} catch (Exception e) {
				rr.setCode(2);
				log.info("queryWxPayStatusErrorResults:" + e.getMessage());
			}
		} else {
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 微信购物支付h5
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp userWXPayToPayByHFive(Map<Object, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		ResultResp rr = new ResultResp();
		String out_trade_no = (String) map.get("orderId");
		String token = (String) map.get("token");
		BigDecimal totalPrice = new BigDecimal(0);
		String goodsName = null;
		// 查询商品名与商品价格
		if (PayToolUtil.isInteger(out_trade_no)) {
			QueryGoodsPara queryGoodsPara = wxPayDao.queryGoodsPriceAndName(out_trade_no);
			totalPrice = queryGoodsPara.getTrade_payment_amount();// 总价
			goodsName = queryGoodsPara.getTrade_desc();
		} else {
			RecordPara rp = wxPayDao.queryRecordPara(out_trade_no);
			totalPrice = rp.getRecord_money();
			goodsName = rp.getRecord_desc();
		}
		String payAmount = String.valueOf(totalPrice);
		String price = PayToolUtil.getMoney(payAmount);
		String scene_info = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"http://118.190.151.246:8080/ShoppingPayment/WXPay/userWXPayToPayByHFive\",\"wap_name\": \"蜂狂购购物\"}}";
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", WXPayConfig.APP_ID);
		params.put("mch_id", WXPayConfig.MCH_ID);
		params.put("nonce_str", PayToolUtil.create_nonce_str());
		params.put("body", goodsName);
		params.put("out_trade_no", out_trade_no);
		params.put("total_fee", price);
		params.put("scene_info", scene_info);

		params.put("spbill_create_ip", PayToolUtil.getIpAddr(request));
		params.put("notify_url", WXPayConfig.NOTIFY_URL);
		params.put("trade_type", "MWEB");

		String sign = PaymentKit.createSign(params, WXPayConfig.API_KEY);
		params.put("sign", sign);

		String xmlResult = PaymentApi.pushOrder(params);

		Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

		String return_code = result.get("return_code");
		String return_msg = result.get("return_msg");
		if (StrKit.isBlank(return_code) || !"SUCCESS".equals(return_code)) {
			rr.setCode(2);
			rr.setDesc(return_msg);
			return rr;
		}
		String result_code = result.get("result_code");
		if (StrKit.isBlank(result_code) || !"SUCCESS".equals(result_code)) {
			rr.setCode(2);
			rr.setDesc(return_msg);
			return rr;
		}
		String urlCode = "https://test1.fkgou.com/wap/pages/order.html?order=0&&fkg=" + token;
		// 确认支付过后跳的地址,需要经过urlencode处理
		try {
			String urlString = Url.getURLEncoderString(urlCode);
			String mweb_url = result.get("mweb_url") + "&redirect_url=" + urlString;
			// response.sendRedirect(mweb_url);
			rr.setCode(1);
			rr.setData(mweb_url);
		} catch (Exception e) {
			log.info("错误结果:" + e.getMessage());
		}
		return rr;
	}

	/**
	 * 微信充值h5
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp userWXPayToRechargeByHFive(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String out_trade_no = (String) map.get("orderId");
		String token = (String) map.get("token");
		BigDecimal totalPrice = new BigDecimal(0);
		String goodsName = null;
		// 查询商品名与商品价格
		if (PayToolUtil.isInteger(out_trade_no)) {
			QueryGoodsPara queryGoodsPara = wxPayDao.queryGoodsPriceAndName(out_trade_no);
			totalPrice = queryGoodsPara.getTrade_payment_amount();// 总价
			goodsName = queryGoodsPara.getTrade_desc();
		} else {
			RecordPara rp = wxPayDao.queryRecordPara(out_trade_no);
			totalPrice = rp.getRecord_money();
			goodsName = rp.getRecord_desc();
		}
		String payAmount = String.valueOf(totalPrice);
		String price = PayToolUtil.getMoney(payAmount);
		String scene_info = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"http://118.190.151.246:8080/ShoppingPayment/WXPay/userWXPayToRechargeByHFive\",\"wap_name\": \"蜂狂购充值\"}}";
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", WXPayConfig.APP_ID);
		params.put("mch_id", WXPayConfig.MCH_ID);
		params.put("nonce_str", PayToolUtil.create_nonce_str());
		params.put("body", goodsName);
		params.put("out_trade_no", out_trade_no);
		params.put("total_fee", price);
		params.put("scene_info", scene_info);

		params.put("spbill_create_ip", PayToolUtil.localIp());
		params.put("notify_url", WXPayConfig.NOTIFY_URL);
		params.put("trade_type", "MWEB");

		String sign = PaymentKit.createSign(params, WXPayConfig.API_KEY);
		params.put("sign", sign);

		String xmlResult = PaymentApi.pushOrder(params);

		Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

		String return_code = result.get("return_code");
		String return_msg = result.get("return_msg");
		if (StrKit.isBlank(return_code) || !"SUCCESS".equals(return_code)) {
			rr.setCode(2);
			rr.setDesc(return_msg);
			return rr;
		}
		String result_code = result.get("result_code");
		if (StrKit.isBlank(result_code) || !"SUCCESS".equals(result_code)) {
			rr.setCode(2);
			rr.setDesc(return_msg);
			return rr;
		}
		String urlCode = "https://test1.fkgou.com/wap/pages/order.html?order=0&&fkg=" + token;
		// 确认支付过后跳的地址,需要经过urlencode处理
		try {
			String urlString = Url.getURLEncoderString(urlCode);
			String mweb_url = result.get("mweb_url") + "&redirect_url=" + urlString;
			// response.sendRedirect(mweb_url);
			rr.setCode(1);
			rr.setData(mweb_url);
		} catch (Exception e) {
			log.info("错误结果:" + e.getMessage());
		}
		return rr;
	}

	/**
	 * 查询金额与商品名
	 * 
	 * @param out_trade_no
	 * @return
	 */
	public RecordPara queryRecordPara(String out_trade_no) {
		return wxPayDao.queryRecordPara(out_trade_no);
	}

	/**
	 * 微信购物支付jsapi
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	public ResultResp userWXPayToPayByOfficialAccounts(Map<Object, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		ResultResp rr = new ResultResp();
		String out_trade_no = (String) map.get("orderId");
		String token = (String) map.get("token");
		String openId = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			UserInfo ui = new UserInfo();
			String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
			ui = mapper.readValue(userinfo, UserInfo.class);// String转object
			openId = ui.getBindOpenid();// 获取用户微信标识
			if (openId == null) {
				rr.setCode(2);
				rr.setDesc("此用户未绑定微信，请通知用户绑定微信账号！");
				return rr;
			}
		} catch (Exception e) {
			rr.setCode(500);
			log.info("openIdErrorResult:" + e.getMessage());
		}
		BigDecimal totalPrice = new BigDecimal(0);
		String goodsName = null;
		// 查询商品名与商品价格
		if (PayToolUtil.isInteger(out_trade_no)) {
			QueryGoodsPara queryGoodsPara = wxPayDao.queryGoodsPriceAndName(out_trade_no);
			totalPrice = queryGoodsPara.getTrade_payment_amount();// 总价
			goodsName = queryGoodsPara.getTrade_desc();
		} else {
			RecordPara rp = wxPayDao.queryRecordPara(out_trade_no);
			totalPrice = rp.getRecord_money();
			goodsName = rp.getRecord_desc();
		}
		String payAmount = String.valueOf(totalPrice);
		String price = PayToolUtil.getMoney(payAmount);
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", WXPayConfig.WAP_APP_ID);
		params.put("mch_id", WXPayConfig.WAP_MCH_ID);
		params.put("nonce_str", PayToolUtil.create_nonce_str());
		params.put("body", goodsName);
		params.put("out_trade_no", out_trade_no);
		params.put("total_fee", price);

		params.put("spbill_create_ip", PayToolUtil.getIpAddr(request));
		params.put("notify_url", WXPayConfig.WAP_NOTIFY_URL);
		params.put("trade_type", "JSAPI");
		params.put("openid", openId);

		String sign = PaymentKit.createSign(params, WXPayConfig.WAP_API_KEY);
		params.put("sign", sign);

		String xmlResult = PaymentApi.pushOrder(params);

		Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

		String return_code = result.get("return_code");
		String return_msg = result.get("return_msg");
		if (StrKit.isBlank(return_code) || !"SUCCESS".equals(return_code)) {
			rr.setCode(2);
			rr.setDesc(return_msg);
			return rr;
		}
		String result_code = result.get("result_code");
		if (StrKit.isBlank(result_code) || !"SUCCESS".equals(result_code)) {
			rr.setCode(2);
			rr.setDesc(return_msg);
			return rr;
		}

		String prepay_id = result.get("prepay_id");

		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appId", WXPayConfig.WAP_APP_ID);
		packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
		packageParams.put("nonceStr", System.currentTimeMillis() + "");
		packageParams.put("package", "prepay_id=" + prepay_id);
		packageParams.put("signType", "MD5");
		String packageSign = PaymentKit.createSign(packageParams, WXPayConfig.WAP_API_KEY);
		packageParams.put("paySign", packageSign);
		log.info("packageSign:" + packageSign);
		String jsonStr = JsonUtils.toJson(packageParams);
		log.info("jsonStr:" + jsonStr);
		rr.setCode(1);
		rr.setData(jsonStr);
		return rr;
	}

	/**
	 * 微信充值支付jsapi
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp userWXPayToRechargeByOfficialAccounts(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		String openId = null;
		if (isExistToken) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				Integer userId = ui.getUserId();
				openId = ui.getBindOpenid();
				if (openId == null) {
					rr.setCode(2);
					rr.setDesc("此用户未绑定微信，请通知用户绑定微信账号！");
					return rr;
				}
				String tradeNo = (String) map.get("tradeNo");
				BigDecimal money = wxPayDao.queryRechargeMoney(userId, tradeNo);
				String fee = money.toString();
				Map<String, String> params = new HashMap<String, String>();
				params.put("appid", WXPayConfig.WAP_APP_ID);
				params.put("mch_id", WXPayConfig.WAP_MCH_ID);
				params.put("nonce_str", PayToolUtil.create_nonce_str());
				params.put("body", "充值");
				params.put("out_trade_no", tradeNo);
				params.put("total_fee", PayToolUtil.getMoney(fee));

				params.put("spbill_create_ip", PayToolUtil.localIp());
				params.put("notify_url", WXPayConfig.NOTIFY_URL_By_WAPRecharge);
				params.put("trade_type", "JSAPI");
				params.put("openid", openId);

				String sign = PaymentKit.createSign(params, WXPayConfig.WAP_API_KEY);
				params.put("sign", sign);

				String xmlResult = PaymentApi.pushOrder(params);

				Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

				String return_code = result.get("return_code");
				String return_msg = result.get("return_msg");
				if (StrKit.isBlank(return_code) || !"SUCCESS".equals(return_code)) {
					rr.setCode(2);
					rr.setDesc(return_msg);
					return rr;
				}
				String result_code = result.get("result_code");
				if (StrKit.isBlank(result_code) || !"SUCCESS".equals(result_code)) {
					rr.setCode(2);
					rr.setDesc(return_msg);
					return rr;
				}

				String prepay_id = result.get("prepay_id");

				Map<String, String> packageParams = new HashMap<String, String>();
				packageParams.put("appId", WXPayConfig.WAP_APP_ID);
				packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
				packageParams.put("nonceStr", System.currentTimeMillis() + "");
				packageParams.put("package", "prepay_id=" + prepay_id);
				packageParams.put("signType", "MD5");
				String packageSign = PaymentKit.createSign(packageParams, WXPayConfig.WAP_API_KEY);
				packageParams.put("paySign", packageSign);
				String jsonStr = JsonUtils.toJson(packageParams);
				rr.setCode(1);
				rr.setData(jsonStr);
			} catch (Exception e) {
				log.info("错误结果：" + e.getMessage());
			}
		} else {
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}
}