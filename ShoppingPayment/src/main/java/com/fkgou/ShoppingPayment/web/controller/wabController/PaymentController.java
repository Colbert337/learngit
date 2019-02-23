package com.fkgou.ShoppingPayment.web.controller.wabController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkgou.ShoppingPayment.web.entity.QueryGoodsPara;
import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.entity.WapGoodsSaleSumUpdate;
import com.fkgou.ShoppingPayment.web.services.feginServices.MallsServices;
import com.fkgou.ShoppingPayment.web.services.feginServices.OrderServices;
import com.fkgou.ShoppingPayment.web.services.feginServices.UpdateOrderServices;
import com.fkgou.ShoppingPayment.web.services.wabServicesImpl.PaymentServicesImpl;
import com.fkgou.ShoppingPayment.web.util.FkGouConfig;
import com.fkgou.ShoppingPayment.web.util.RedisUtil;
import com.fkgou.ShoppingPayment.web.util.timeStampUtil;
import com.fkgou.ShoppingPayment.web.util.WXPayUtils.PayToolUtil;
import com.fkgou.userms.entity.UserInfo;

@CrossOrigin
@RestController
@RequestMapping("Payment")
public class PaymentController {

	@Autowired
	private PaymentServicesImpl PaymentServicesImpl;

	@Autowired
	UpdateOrderServices updateOrderServices;

	@Autowired
	MallsServices mallsServices;

	@Autowired
	OrderServices orderServices;

	final static Logger log = LoggerFactory.getLogger(PaymentController.class);

	/**
	 * 验证是否存在支付密码
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("VerifyExistencePasswd")
	public ResultResp VerifyExistencePasswd(@RequestBody Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			String userId = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USERID + token);
			String isExist = PaymentServicesImpl.isExistPasswd(userId);
			if ("".equals(isExist)) {
				rr.setCode(2);
				rr.setDesc("支付密码不存在，请通知用户设置支付密码！");
			} else {
				rr.setCode(1);
				rr.setDesc("支付密码已存在！");
			}
		} else {
			rr.setCode(2);
			rr.setDesc("token失效，请重新获取！！");
		}
		return rr;
	}

	/**
	 * 设置支付密码与找回
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("setPaymentPassword")
	public ResultResp setPaymentPassword(@RequestBody Map<String, Object> map) {
		ResultResp rr = PaymentServicesImpl.setPaymentPassword(map);
		return rr;
	}

	/**
	 * 修改支付密码
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("changePaymentPasswd")
	public ResultResp changePaymentPasswd(@RequestBody Map<String, Object> map) {
		ResultResp rr = PaymentServicesImpl.changePaymentPasswd(map);
		return rr;
	}

	/**
	 * 余额支付&&购物卡支付
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("balancePayment")
	public ResultResp balancePayment(@RequestBody Map<Object, Object> map) {
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
				Integer errorNum = 0;// 错误次数
				// 获取支付订单编号，并改变状态
				String orderId = (String) map.get("orderIdList");// 接收付款订单编号
				String passwd = (String) map.get("passwd");
				Integer type = (Integer) map.get("type");// 1:余额支付 2:购物卡支付
				if (type == 1) {
					Integer isExist = PaymentServicesImpl.isPasswd(userId, passwd);// 验证支付密码
					if (isExist == 1) {
						BigDecimal amountActuallyPaids = new BigDecimal(0);
						// 查询此订单是否已经生成订单
						if (PayToolUtil.isInteger(orderId)) {
							QueryGoodsPara queryGoodsPara = PaymentServicesImpl.queryGoodsPriceAndName(orderId, userId);
							BigDecimal paymentAmount = queryGoodsPara.getTrade_payment_amount();
							amountActuallyPaids = amountActuallyPaids.add(paymentAmount);
						} else {
							String amountActuallyPaid = RedisUtil
									.get(FkGouConfig.FKGOU_USERAMOUNTACTUALLYPAID + "--" + token + "--" + userId);// 实付金额
							amountActuallyPaids = new BigDecimal(amountActuallyPaid);// 类型转换
						}
						BigDecimal userBalanceMoney = PaymentServicesImpl.queryBalanceMoney(userId);// 查询账户余额
						// 判断实付金额与余额
						if (userBalanceMoney.compareTo(amountActuallyPaids) == 1
								|| userBalanceMoney.compareTo(amountActuallyPaids) == 0) {
							boolean updateMoney = PaymentServicesImpl.updateMoney(userId, amountActuallyPaids);// 扣除购物金额
							if (updateMoney) {
								rr.setCode(1);
								rr.setDesc("支付成功！");

								// 调用方法,更改状态
								Map<Object, Object> statusMap = new HashMap<Object, Object>();
								statusMap.put("orderId", orderId);
								statusMap.put("token", token);
								changeStatus(statusMap);
							} else {
								rr.setCode(3);
								rr.setDesc("支付失败！");
							}
						} else {
							rr.setCode(4);
							rr.setDesc("账户余额不足，请选择其他支付方式!");
						}
					} else {
						rr.setCode(5);
						errorNum++;
						if (errorNum == 3) {
							rr.setCode(6);
							rr.setDesc("密码错误三次！");
						}
						rr.setDesc("密码错误！");
					}
				} else {
					// 购物卡支付
					Integer isExist = PaymentServicesImpl.isPasswd(userId, passwd);// 验证支付密码
					if (isExist == 1) {
						BigDecimal amountActuallyPaids = new BigDecimal(0);
						// 查询此订单是否已经生成订单
						if (PayToolUtil.isInteger(orderId)) {
							QueryGoodsPara queryGoodsPara = PaymentServicesImpl.queryGoodsPriceAndName(orderId, userId);
							BigDecimal paymentAmount = queryGoodsPara.getTrade_payment_amount();
							amountActuallyPaids = amountActuallyPaids.add(paymentAmount);
						} else {
							String amountActuallyPaid = RedisUtil
									.get(FkGouConfig.FKGOU_USERAMOUNTACTUALLYPAID + "--" + token + "--" + userId);// 实付金额
							amountActuallyPaids = new BigDecimal(amountActuallyPaid);// 类型转换
						}
						BigDecimal userGiftCardMoney = PaymentServicesImpl.queryGiftCardMoney(userId);// 查询账户购物卡余额
						// 判断实付金额与余额
						if (userGiftCardMoney.compareTo(amountActuallyPaids) == 1
								|| userGiftCardMoney.compareTo(amountActuallyPaids) == 0) {
							boolean updateMoney = PaymentServicesImpl.updateGiftCardMoney(userId, amountActuallyPaids);// 扣除购物金额
							if (updateMoney) {
								rr.setCode(1);
								rr.setDesc("支付成功！");

								// 调用方法,更改状态
								Map<Object, Object> statusMap = new HashMap<Object, Object>();
								statusMap.put("orderId", orderId);
								statusMap.put("token", token);
								changeStatus(statusMap);

							} else {
								rr.setCode(3);
								rr.setDesc("支付失败！");
							}
						} else {
							rr.setCode(4);
							rr.setDesc("账户余额不足，请选择其他支付方式!");
						}
					} else {
						rr.setCode(5);
						errorNum++;
						if (errorNum == 3) {
							rr.setCode(6);
							rr.setDesc("密码错误三次！");
						}
						rr.setDesc("密码错误！");
					}
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

	/**
	 * 更改状态
	 * 
	 * @param map
	 */
	@Transactional
	public void changeStatus(Map<Object, Object> map) {
		String orderId = (String) map.get("orderId");
		String token = (String) map.get("token");
		String paymentTime = timeStampUtil.getNowDate();// 支付时间
		try {
			ObjectMapper mapper = new ObjectMapper();
			UserInfo ui = new UserInfo();
			String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
			ui = mapper.readValue(userinfo, UserInfo.class);// String转object
			Integer userId = ui.getUserId();
			String userName = ui.getUserName();
			// 支付成功，向malls服务发送消息，削减库存
			log.info("测试时间1：" + timeStampUtil.getNowDate());
			WapGoodsSaleSumUpdate wssu = new WapGoodsSaleSumUpdate();
			String goodsId = RedisUtil.get(FkGouConfig.FKGOU_USERGOODSID + "--" + token + "--" + userId);
			Integer goodsID = Integer.parseInt(goodsId);
			String goodsNum = RedisUtil.get(FkGouConfig.FKGOU_USERGOODSNUMBER + "--" + token + "--" + userId);
			Integer goodsNumber = Integer.parseInt(goodsNum);
			String commonId = RedisUtil.get(FkGouConfig.FKGOU_USERCOMMONID + "--" + token + "--" + userId);
			Integer commonID = Integer.parseInt(commonId);
			List<WapGoodsSaleSumUpdate> goodsDetails = new ArrayList<WapGoodsSaleSumUpdate>();
			wssu.setGoodsId(goodsID);
			wssu.setGoodsNum(goodsNumber);
			wssu.setCommonId(commonID);
			goodsDetails.add(wssu);
			Map<String, List<WapGoodsSaleSumUpdate>> goodsSaleSum = new HashMap<String, List<WapGoodsSaleSumUpdate>>();
			goodsSaleSum.put("goodsDetails", goodsDetails);
			mallsServices.updateSaleSum(goodsSaleSum);
			log.info("测试时间2：" + timeStampUtil.getNowDate());

			// 更新trade&record状态
			log.info("测试时间3：" + timeStampUtil.getNowDate());
			String finishTime = timeStampUtil.getNowDate();
			PaymentServicesImpl.updateTradeStatus(orderId, paymentTime, finishTime, userId);
			PaymentServicesImpl.updateRecord(orderId, paymentTime);
			PaymentServicesImpl.updateRecordBySeller(orderId, paymentTime);
			log.info("测试时间4：" + timeStampUtil.getNowDate());

			// 获取红包及优惠券编号
			// log.info("测试时间5：" + timeStampUtil.getNowDate());
			// Map<String, Object> queryMap = new HashMap<String, Object>();
			// queryMap.put("orderId", orderId);
			// queryMap.put("userId", userId);
			// ResultResp rr = orderServices.queryPreferentialId(queryMap);
			// PreferentialNoPara preferentialId =
			// JsonUtil.string2Obj(rr.getData().toString(),
			// PreferentialNoPara.class);
			// String redpacketId = preferentialId.getRedpacketId();
			// List<String> voucherIdList = preferentialId.getVoucherIdList();
			// Map<String, Object> statusMap = new HashMap<String, Object>();
			// statusMap.put("redpacketId", redpacketId);
			// statusMap.put("voucherId", voucherIdList);
			// statusMap.put("type", "1");
			// mallsServices.updateSalesPrm(statusMap);
			// log.info("测试时间6：" + timeStampUtil.getNowDate());

			// 发送feign,提交下单通知
			log.info("测试时间7：" + timeStampUtil.getNowDate());
			Map<Object, Object> messageMap = new HashMap<Object, Object>();
			messageMap.put("userId", userId);
			messageMap.put("userName", userName);
			messageMap.put("orderId", orderId);// 支付号
			messageMap.put("paymentTime", paymentTime);
			updateOrderServices.orderToInform(messageMap);
			log.info("测试时间8：" + timeStampUtil.getNowDate());

			// 发送feign请求，更改payment状态
			log.info("测试时间9：" + timeStampUtil.getNowDate());
			Map<Object, Object> paymentMap = new HashMap<Object, Object>();
			Integer payCode = 1;// 支付方式1-余额支付，2-支付宝支付，3-微信支付，4-通联支付
			Integer paymentId = 1;// 支付方式
			Integer orderStatus = 3;// 订单状态
			String paymentName = "在线支付";// 支付方式名称
			paymentMap.put("payCode", payCode);
			paymentMap.put("payOrderId", orderId);
			paymentMap.put("paymentId", paymentId);
			paymentMap.put("orderStatus", orderStatus);
			paymentMap.put("paymentName", paymentName);
			paymentMap.put("paymentTime", paymentTime);
			updateOrderServices.UpdateOrderBaseByPayment(paymentMap);
			log.info("测试时间10：" + timeStampUtil.getNowDate());
		} catch (Exception e) {
			log.info("错误结果:" + e.getMessage());
		}
		return;
	}
}
