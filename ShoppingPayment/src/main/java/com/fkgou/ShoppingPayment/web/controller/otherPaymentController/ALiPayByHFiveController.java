package com.fkgou.ShoppingPayment.web.controller.otherPaymentController;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fkgou.ShoppingPayment.web.config.ALiPayConfig;
import com.fkgou.ShoppingPayment.web.entity.QueryGoodsPara;
import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.entity.callBackEntity.PayConsumeDeposit;
import com.fkgou.ShoppingPayment.web.entity.callBackEntity.PayConsumeRecord;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.QueryRecordPara;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.RecordPara;
import com.fkgou.ShoppingPayment.web.services.feginServices.MallsServices;
import com.fkgou.ShoppingPayment.web.services.feginServices.OrderServices;
import com.fkgou.ShoppingPayment.web.services.feginServices.UpdateOrderServices;
import com.fkgou.ShoppingPayment.web.services.otherPaymentServices.ALiPayServices;
import com.fkgou.ShoppingPayment.web.util.Url;
import com.fkgou.ShoppingPayment.web.util.timeStampUtil;
import com.fkgou.ShoppingPayment.web.util.WXPayUtils.PayToolUtil;

/**
 * 支付宝支付H5界面
 * 
 * @author ilove
 *
 */

@CrossOrigin
@RestController
@RequestMapping("ALiPay")
public class ALiPayByHFiveController {

	@Autowired
	ALiPayServices aLiPayServices;

	@Autowired
	UpdateOrderServices updateOrderServices;

	@Autowired
	MallsServices mallsServices;

	@Autowired
	OrderServices orderServices;

	final static Logger log = LoggerFactory.getLogger(ALiPayByHFiveController.class);

	/**
	 * 支付宝支付
	 * 
	 * @param para
	 * @param request
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "useAlipayToPay")
	public ResultResp useAlipayToPay(@RequestBody Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		ResultResp rr = new ResultResp();
		String orderId = (String) map.get("orderId");
		BigDecimal totalPrice = new BigDecimal(0);
		String goodsName = null;
		if (PayToolUtil.isInteger(orderId)) {
			QueryGoodsPara queryGoodsPara = aLiPayServices.queryGoodsPriceAndName(orderId);
			totalPrice = queryGoodsPara.getTrade_payment_amount();
			goodsName = queryGoodsPara.getTrade_desc();
		} else {
			RecordPara rp = aLiPayServices.queryRecordPara(orderId);
			totalPrice = rp.getRecord_money();
			goodsName = rp.getRecord_desc();
		}
		String price = String.valueOf(totalPrice);
		// 商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = orderId;
		// 订单名称，必填
		String subject = goodsName;
		// 付款金额，必填
		String total_amount = price;
		// 商品描述，可空
		String body = "购物";
		// 超时时间 可空
		String timeout_express = "30m";
		// 销售产品码 必填
		String product_code = "QUICK_WAP_WAY";
		// SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
		// 调用RSA签名方式
		AlipayClient client = new DefaultAlipayClient(ALiPayConfig.URL, ALiPayConfig.APPID,
				ALiPayConfig.RSA_PRIVATE_KEY, ALiPayConfig.FORMAT, ALiPayConfig.CHARSET, ALiPayConfig.ALIPAY_PUBLIC_KEY,
				ALiPayConfig.SIGNTYPE);
		AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

		// 封装请求支付信息
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setBody(body);
		model.setSubject(subject);
		model.setOutTradeNo(out_trade_no);
		model.setTimeoutExpress(timeout_express);
		model.setTotalAmount(total_amount);
		model.setProductCode(product_code);
		alipay_request.setBizModel(model);
		// 设置异步通知地址
		alipay_request.setNotifyUrl(Url.getURLDecoderString(ALiPayConfig.shopping_notify_url));
		// 设置同步地址
		alipay_request.setReturnUrl(Url.getURLDecoderString(ALiPayConfig.shopping_return_url));

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
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return rr;
	}

	/**
	 * 创建充值订单
	 * 
	 * @param para
	 * @return
	 */
	@RequestMapping("createRechargeOrder")
	public ResultResp createRechargeOrder(@RequestBody Map<Object, Object> para) {
		ResultResp rr = aLiPayServices.createRechargeOrder(para);
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
	@RequestMapping("useAlipayToRecharge")
	public ResultResp useAlipayToRecharge(@RequestBody Map<Object, Object> para, HttpServletRequest request,
			HttpServletResponse response) {
		ResultResp rr = aLiPayServices.useAlipayToRecharge(para, request, response);
		return rr;
	}

	/**
	 * 支付宝购物异步回调
	 * 
	 * @param para
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "alipayNotifyUrl", method = { RequestMethod.POST, RequestMethod.GET })
	public void alipayNotifyUrl(HttpServletRequest request, HttpServletResponse response) {
		log.info("----进入异步回调----");
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			try {
				// 乱码解决，这段代码在出现乱码时使用
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				// "UTF-8");
				params.put(name, valueStr);
			} catch (Exception e) {
				log.info("错误结果1:" + e.getMessage());
			}
		}

		try {
			log.info("-----进入开始验签------");
			boolean signVerified = AlipaySignature.rsaCheckV1(params, ALiPayConfig.ALIPAY_PUBLIC_KEY,
					ALiPayConfig.CHARSET, ALiPayConfig.SIGNTYPE); // 调用SDK验证签名
			log.info("----------------");
			PrintWriter out = response.getWriter();
			/**
			 * 实际验证过程建议商户务必添加以下校验： 1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
			 * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
			 * 3、校验通知中的seller_id（或者seller_email)
			 * 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
			 * 4、验证app_id是否为该商户本身。
			 */
			if (signVerified) {
				log.info("-----验签成功----");
				// 商户订单号
				String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
				log.info("-----out_trade_no-----" + out_trade_no);
				// 支付宝交易号
				String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
				log.info("-----trade_no-----" + trade_no);
				// 交易状态
				String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
				log.info("-----trade_status-----" + trade_status);
				// 交易订单金额
				String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
				log.info("-----total_amount-----" + total_amount);
				// 卖家支付宝用户号
				String seller_id = new String(request.getParameter("seller_id").getBytes("ISO-8859-1"), "UTF-8");
				log.info("-----seller_id-----" + seller_id);
				// 判断状态
				// 如果状态为TRADE_FINISHED,则交易结束，不可退款
				if (trade_status.equals("TRADE_FINISHED")) {
					// 判断该笔订单是否在商户网站中已经做过处理
					// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
					// 如果有做过处理，不执行商户的业务程序

					// 注意：
					// 如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
					// 如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
					out.println("success");
					out.close();
				} else if (trade_status.equals("WAIT_BUYER_PAY")) {
					out.println("success");
					out.close();
				} else if (trade_status.equals("TRADE_CLOSED")) {
					out.println("success");
					out.close();
				} else if (trade_status.equals("TRADE_SUCCESS")) {
					log.info("----TRADE_SUCCESS----");
					// 订单号是纯数字，trade查询
					if (PayToolUtil.isInteger(out_trade_no)) {
						QueryRecordPara queryGoodsPara = aLiPayServices.queryRecordPriceAndName(out_trade_no);
						String orderId = queryGoodsPara.getOrder_id();
						BigDecimal recordMoney = queryGoodsPara.getTrade_payment_amount();
						log.info("-----进入判断-----");
						if (out_trade_no.equals(orderId) && total_amount.equals(recordMoney.toString())
								&& seller_id.equals(ALiPayConfig.sellerId)) {
							log.info("----判断成功----");
							Map<Object, Object> map = new HashMap<Object, Object>();
							Integer payCode = 2;// 支付方式
							Integer orderStatus = 3;// 待发货
							Integer paymentId = 1;// 支付方式
							String paymentName = "在线支付";// 支付方式名称
							String paymentTime = timeStampUtil.getNowDate();// 支付时间
							String paymentOtherNumber = trade_no;// 第三方支付号
							map.put("payCode", payCode);// 支付方式
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
							boolean updateConsumeRecord = aLiPayServices.updateComsumeRecordByPureDigital(recordStatus,
									paymentTime, out_trade_no);
							log.info("----updateConsumeRecord----" + updateConsumeRecord);
							// 更改ConsumeTrade
							Integer orderStateId = 3;
							String closeTime = timeStampUtil.getNowDate();
							log.info("----1-----");
							boolean updateConsumeTrade = aLiPayServices.updateConsumeTradeByPureDigital(orderStateId,
									paymentTime, closeTime, out_trade_no);
							log.info("----updateConsumeTrade----" + updateConsumeTrade);
							if (updateConsumeRecord && updateConsumeTrade) {
								log.info("----更新成功----");
								out.println("success");
								out.close();
							} else {
								log.info("----更新失败----");
								out.println("fail");
								out.close();
							}
						} else {
							log.info("订单号或订单金额不符！");
							out.println("fail");
							out.close();
						}
					} else {
						// 订单号不是纯数字Record表查询
						Integer recodeStatus = aLiPayServices.queryRecodeStatus(out_trade_no);
						log.info("----recodeStatus----" + recodeStatus);
						// 判断订单状态是否改变
						if (recodeStatus == 1) {
							log.info("----进入状态判断----");
							PayConsumeRecord payConsumeRecord = aLiPayServices.queryorderNo(out_trade_no);
							log.info("----payConsumeRecord----" + payConsumeRecord.toString());
							String orderId = payConsumeRecord.getOrder_id();
							BigDecimal recordMoney = payConsumeRecord.getRecord_money();
							log.info("-----orderId-----" + orderId);
							log.info("-----recordMoney-----" + recordMoney);
							log.info("-----orderId-----" + orderId);
							log.info("-----进入 判断-----");
							if (out_trade_no.equals(orderId) && total_amount.equals(recordMoney.toString())
									&& seller_id.equals(ALiPayConfig.sellerId)) {
								log.info("----判断成功----");
								Map<Object, Object> map = new HashMap<Object, Object>();
								Integer payCode = 2;// 支付方式
								Integer orderStatus = 3;// 待发货
								Integer paymentId = 1;// 支付方式
								String paymentName = "在线支付";// 支付方式名称
								String paymentTime = timeStampUtil.getNowDate();// 支付时间
								String paymentOtherNumber = trade_no;// 第三方支付号
								map.put("payCode", payCode);// 支付方式
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
								boolean updateConsumeRecord = aLiPayServices.updateComsumeRecord(recordStatus,
										paymentTime, out_trade_no);
								log.info("----updateConsumeRecord----" + updateConsumeRecord);
								// 更改ConsumeTrade
								Integer orderStateId = 3;
								String closeTime = timeStampUtil.getNowDate();
								log.info("----1-----");
								boolean updateConsumeTrade = aLiPayServices.updateConsumeTrade(orderStateId,
										paymentTime, closeTime, out_trade_no);
								log.info("----updateConsumeTrade----" + updateConsumeTrade);
								if (updateConsumeRecord && updateConsumeTrade) {
									log.info("----更新成功----");
									out.println("success");
									out.close();
								} else {
									log.info("----更新失败----");
									out.println("fail");
									out.close();
								}
							} else {
								log.info("订单号或订单金额不符！");
								out.println("fail");
								out.close();
							}
						} else {
							// 结果已处理，返回success
							out.println("success");
							out.close();
						}
					}
				}
			} else {
				log.info("验签失败！");
				out.println("fail");
				out.close();
			}
		} catch (Exception e) {
			log.info("错误结果2:" + e.getMessage());
		}
		return;
	}

	/**
	 * 支付宝购物同步回调
	 * 
	 * @param para
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "alipayReturnUrl", method = RequestMethod.GET)
	public void alipayReturnUrl(HttpServletRequest request, HttpServletResponse response) {
		log.info("支付成功, 进入同步通知接口...");

		// 获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			} // 乱码解决，这段代码在出现乱码时使用
			try {
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
				params.put(name, valueStr);
			} catch (Exception e) {
				log.info("错误结果：" + e.getMessage());
			}
		}

		try {
			// 验签
			boolean signVerified = AlipaySignature.rsaCheckV1(params, ALiPayConfig.ALIPAY_PUBLIC_KEY,
					ALiPayConfig.CHARSET, ALiPayConfig.SIGNTYPE);
			if (signVerified) {
				// 商户订单号
				String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
				// 支付宝交易号
				String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
				// 付款金额
				String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

				log.info("********************** 支付成功(支付宝同步通知) **********************");
				log.info("* 订单号: {}", out_trade_no);
				log.info("* 支付宝交易号: {}", trade_no);
				log.info("* 实付金额: {}", total_amount);
				log.info("***************************************************************");

				// 订单号是纯数字，trade查询
				if (PayToolUtil.isInteger(out_trade_no)) {
					QueryRecordPara queryGoodsPara = aLiPayServices.queryRecordPriceAndName(out_trade_no);
					String orderId = queryGoodsPara.getOrder_id();
					BigDecimal recordMoney = queryGoodsPara.getTrade_payment_amount();
					if (out_trade_no.equals(orderId) && total_amount.equals(recordMoney.toString())) {
						Map<Object, Object> map = new HashMap<Object, Object>();
						Integer payCode = 2;
						Integer orderStatus = 3;// 待发货
						Integer paymentId = 1;// 支付方式
						String paymentName = "在线支付";// 支付方式名称
						String paymentTime = timeStampUtil.getNowDate();// 支付时间
						String paymentOtherNumber = trade_no;// 第三方支付号
						map.put("payCode", payCode);
						map.put("paymentId", paymentId);
						map.put("paymentNumber", out_trade_no);// 商户订单号
						map.put("orderStatus", orderStatus);
						map.put("paymentName", paymentName);
						map.put("paymentTime", paymentTime);
						map.put("paymentOtherNumber", paymentOtherNumber);
						updateOrderServices.HFiveUpdateOrderBaseByPayment(map);
						// 更改支付表状态
						Integer recordStatus = 3;
						aLiPayServices.updateComsumeRecordByPureDigital(recordStatus, paymentTime, out_trade_no);
						// 更改ConsumeTrade
						Integer orderStateId = 3;
						String gmtCloseTime = timeStampUtil.getNowDate();
						aLiPayServices.updateConsumeTradeByPureDigital(orderStateId, paymentTime, gmtCloseTime,
								out_trade_no);
					} else {
						log.info("订单号或订单金额不符！");
					}
				} else {
					PayConsumeRecord payConsumeRecord = aLiPayServices.queryorderNo(out_trade_no);
					String orderId = payConsumeRecord.getOrder_id();
					BigDecimal recordMoney = payConsumeRecord.getRecord_money();
					if (out_trade_no.equals(orderId) && total_amount.equals(recordMoney.toString())) {
						Map<Object, Object> map = new HashMap<Object, Object>();
						Integer payCode = 2;
						Integer orderStatus = 3;// 待发货
						Integer paymentId = 1;// 支付方式
						String paymentName = "在线支付";// 支付方式名称
						String paymentTime = timeStampUtil.getNowDate();// 支付时间
						String paymentOtherNumber = trade_no;// 第三方支付号
						map.put("payCode", payCode);
						map.put("paymentId", paymentId);
						map.put("paymentNumber", out_trade_no);// 商户订单号
						map.put("orderStatus", orderStatus);
						map.put("paymentName", paymentName);
						map.put("paymentTime", paymentTime);
						map.put("paymentOtherNumber", paymentOtherNumber);
						updateOrderServices.HFiveUpdateOrderBaseByPayment(map);
						// 更改支付表状态
						Integer recordStatus = 3;
						aLiPayServices.updateComsumeRecord(recordStatus, paymentTime, out_trade_no);
						// 更改ConsumeTrade
						Integer orderStateId = 3;
						String gmtCloseTime = timeStampUtil.getNowDate();
						aLiPayServices.updateConsumeTrade(orderStateId, paymentTime, gmtCloseTime, out_trade_no);
					} else {
						log.info("订单号或订单金额不符！");
					}
				}
			} else {
				log.info("验签失败！");
			}
			response.sendRedirect("https://www.fkgou.com/wap/pages/personal.html?1=&type=&bind=&");
		} catch (Exception e) {
			log.info("错误结果：" + e.getMessage());
		}
		return;
	}

	/**
	 * 支付宝购物同步回调Pc
	 * 
	 * @param para
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "alipayReturnUrlByPc", method = RequestMethod.GET)
	public void alipayReturnUrlByPc(HttpServletRequest request, HttpServletResponse response) {
		log.info("支付成功, 进入同步通知接口...");

		// 获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			} // 乱码解决，这段代码在出现乱码时使用
			try {
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
				params.put(name, valueStr);
			} catch (Exception e) {
				log.info("错误结果：" + e.getMessage());
			}
		}

		try {
			// 验签
			boolean signVerified = AlipaySignature.rsaCheckV1(params, ALiPayConfig.ALIPAY_PUBLIC_KEY,
					ALiPayConfig.CHARSET, ALiPayConfig.SIGNTYPE);
			if (signVerified) {
				// 商户订单号
				String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
				// 支付宝交易号
				String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
				// 付款金额
				String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

				log.info("********************** 支付成功(支付宝同步通知) **********************");
				log.info("* 订单号: {}", out_trade_no);
				log.info("* 支付宝交易号: {}", trade_no);
				log.info("* 实付金额: {}", total_amount);
				log.info("***************************************************************");

				// 订单号是纯数字，trade查询
				if (PayToolUtil.isInteger(out_trade_no)) {
					QueryRecordPara queryGoodsPara = aLiPayServices.queryRecordPriceAndName(out_trade_no);
					String orderId = queryGoodsPara.getOrder_id();
					BigDecimal recordMoney = queryGoodsPara.getTrade_payment_amount();
					if (out_trade_no.equals(orderId) && total_amount.equals(recordMoney.toString())) {
						Map<Object, Object> map = new HashMap<Object, Object>();
						Integer payCode = 2;
						Integer orderStatus = 3;// 待发货
						Integer paymentId = 1;// 支付方式
						String paymentName = "在线支付";// 支付方式名称
						String paymentTime = timeStampUtil.getNowDate();// 支付时间
						String paymentOtherNumber = trade_no;// 第三方支付号
						map.put("payCode", payCode);
						map.put("paymentId", paymentId);
						map.put("paymentNumber", out_trade_no);// 商户订单号
						map.put("orderStatus", orderStatus);
						map.put("paymentName", paymentName);
						map.put("paymentTime", paymentTime);
						map.put("paymentOtherNumber", paymentOtherNumber);
						updateOrderServices.HFiveUpdateOrderBaseByPayment(map);
						// 更改支付表状态
						Integer recordStatus = 3;
						aLiPayServices.updateComsumeRecordByPureDigital(recordStatus, paymentTime, out_trade_no);
						// 更改ConsumeTrade
						Integer orderStateId = 3;
						String gmtCloseTime = timeStampUtil.getNowDate();
						aLiPayServices.updateConsumeTradeByPureDigital(orderStateId, paymentTime, gmtCloseTime,
								out_trade_no);
					} else {
						log.info("订单号或订单金额不符！");
					}
				} else {
					PayConsumeRecord payConsumeRecord = aLiPayServices.queryorderNo(out_trade_no);
					String orderId = payConsumeRecord.getOrder_id();
					BigDecimal recordMoney = payConsumeRecord.getRecord_money();
					if (out_trade_no.equals(orderId) && total_amount.equals(recordMoney.toString())) {
						Map<Object, Object> map = new HashMap<Object, Object>();
						Integer payCode = 2;
						Integer orderStatus = 3;// 待发货
						Integer paymentId = 1;// 支付方式
						String paymentName = "在线支付";// 支付方式名称
						String paymentTime = timeStampUtil.getNowDate();// 支付时间
						String paymentOtherNumber = trade_no;// 第三方支付号
						map.put("payCode", payCode);
						map.put("paymentId", paymentId);
						map.put("paymentNumber", out_trade_no);// 商户订单号
						map.put("orderStatus", orderStatus);
						map.put("paymentName", paymentName);
						map.put("paymentTime", paymentTime);
						map.put("paymentOtherNumber", paymentOtherNumber);
						updateOrderServices.HFiveUpdateOrderBaseByPayment(map);
						// 更改支付表状态
						Integer recordStatus = 3;
						aLiPayServices.updateComsumeRecord(recordStatus, paymentTime, out_trade_no);
						// 更改ConsumeTrade
						Integer orderStateId = 3;
						String gmtCloseTime = timeStampUtil.getNowDate();
						aLiPayServices.updateConsumeTrade(orderStateId, paymentTime, gmtCloseTime, out_trade_no);
					} else {
						log.info("订单号或订单金额不符！");
					}
				}
			} else {
				log.info("验签失败！");
			}
			response.sendRedirect("https://www.fkgou.com/pc/pages/alipay.html");
		} catch (Exception e) {
			log.info("错误结果：" + e.getMessage());
		}
		return;
	}

	/**
	 * 支付宝充值异步回调
	 * 
	 * @param para
	 * @param request
	 * @param response
	 * @return
	 * @return
	 */
	@Transactional
	@RequestMapping("alipayRechargeNotifyUrl")
	public void alipayRechargeNotifyUrl(HttpServletRequest request, HttpServletResponse response) {
		log.info("----进入异步回调----");
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		log.info("requestParams:" + requestParams.toString());
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			try {
				// 乱码解决，这段代码在出现乱码时使用
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				// "utf-8");
				params.put(name, valueStr);
				log.info("params:" + params.toString());
			} catch (Exception e) {
				log.info("错误结果1:" + e.getMessage());
			}
		}
		try {
			log.info("-----进入开始验签------");
			boolean signVerified = AlipaySignature.rsaCheckV1(params, ALiPayConfig.ALIPAY_PUBLIC_KEY,
					ALiPayConfig.CHARSET, ALiPayConfig.SIGNTYPE); // 调用SDK验证签名
			log.info("----------------");
			PrintWriter out = response.getWriter();
			/**
			 * 实际验证过程建议商户务必添加以下校验： 1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
			 * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
			 * 3、校验通知中的seller_id（或者seller_email)
			 * 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
			 * 4、验证app_id是否为该商户本身。
			 */
			if (signVerified) {
				log.info("-----验签成功----");
				// 商户订单号
				String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
				log.info("-----out_trade_no-----" + out_trade_no);
				// 支付宝交易号
				String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
				log.info("-----trade_no-----" + trade_no);
				// 交易状态
				String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
				log.info("-----trade_status-----" + trade_status);
				// 交易订单金额
				String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
				log.info("-----total_amount-----" + total_amount);
				// 卖家支付宝用户号
				String seller_id = new String(request.getParameter("seller_id").getBytes("ISO-8859-1"), "UTF-8");
				log.info("-----seller_id-----" + seller_id);
				// 判断状态
				// 如果状态为TRADE_FINISHED,则交易结束，不可退款
				if (trade_status.equals("TRADE_FINISHED")) {
					// 判断该笔订单是否在商户网站中已经做过处理
					// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
					// 如果有做过处理，不执行商户的业务程序

					// 注意：
					// 如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
					// 如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
					out.println("success");
					out.close();
				} else if (trade_status.equals("WAIT_BUYER_PAY")) {
					out.println("success");
					out.close();
				} else if (trade_status.equals("TRADE_CLOSED")) {
					out.println("success");
					out.close();
				} else if (trade_status.equals("TRADE_SUCCESS")) {
					log.info("----TRADE_SUCCESS----");
					Integer TradeStatus = aLiPayServices.queryTradeStatus(out_trade_no);
					if (TradeStatus == 8) {
						log.info("-----执行逻辑处理-----");
						PayConsumeDeposit payConsumeDeposit = aLiPayServices.queryPayConsumeDeposit(out_trade_no);
						if (out_trade_no.equals(payConsumeDeposit.getDeposit_trade_no().toString())
								&& total_amount.equals(payConsumeDeposit.getDeposit_total_fee().toString())
								&& seller_id.equals(ALiPayConfig.sellerId)) {
							log.info("-----------");
							// 更新pdb_pay_consume_deposit所需参数
							String depositGmtPayment = timeStampUtil.getNowDate();
							String depositGmtClose = timeStampUtil.getNowDate();
							Integer depositTradeStatus = 6;
							String depositReturnTradeNo = trade_no;
							Integer depositIsRechargePackage = 0;

							log.info("-----开始更新-----");
							boolean isUpdateDepositSuccess = aLiPayServices.updateDeposit(depositGmtPayment,
									depositGmtClose, depositTradeStatus, depositReturnTradeNo, depositIsRechargePackage,
									out_trade_no);
							log.info("-----开始更新-----");
							boolean isUpdateRecord = aLiPayServices.updateRecord(depositGmtPayment, out_trade_no);
							log.info("-----判断是否成功-----");
							// 更新成功，增加金额
							if (isUpdateDepositSuccess && isUpdateRecord) {
								String money = payConsumeDeposit.getDeposit_total_fee();
								Integer userId = payConsumeDeposit.getDeposit_buyer_id();
								aLiPayServices.updateUserResource(money, userId);
								log.info("----更新成功----");
								out.println("success");
								out.close();
							} else {
								log.info("----更新失败----");
								out.println("fail");
								out.close();
							}
						} else {
							log.info("订单号或订单金额不符！");
							out.println("fail");
							out.close();
						}
					} else {
						// 结果已处理，返回success
						out.println("success");
						out.close();
					}
				}
			} else {
				log.info("验签失败！");
				out.println("fail");
				out.close();
			}
		} catch (Exception e) {
			log.info("错误结果2:" + e.getMessage());
		}
		return;
	}

	/**
	 * 支付宝充值同步回调
	 * 
	 * @param para
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	@RequestMapping("alipayRechargeReturnUrl")
	public void alipayRechargeReturnUrl(HttpServletRequest request, HttpServletResponse response) {
		log.info("支付成功, 进入同步通知接口...");

		// 获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			} // 乱码解决，这段代码在出现乱码时使用
			try {
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
				params.put(name, valueStr);
			} catch (Exception e) {
				log.info(e.getMessage());
			}
		}

		try {
			log.info("进入验签...");
			// 验签
			boolean signVerified = AlipaySignature.rsaCheckV1(params, ALiPayConfig.ALIPAY_PUBLIC_KEY,
					ALiPayConfig.CHARSET, ALiPayConfig.SIGNTYPE);
			if (signVerified) {
				log.info("验签成功...");
				// 商户订单号
				String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
				log.info("out_trade_no..." + out_trade_no);
				// 支付宝交易号
				String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
				log.info("trade_no..." + trade_no);
				// 付款金额
				String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
				log.info("total_amount..." + total_amount);
				// 卖家支付宝用户号
				String seller_id = new String(request.getParameter("seller_id").getBytes("ISO-8859-1"), "UTF-8");
				log.info("seller_id..." + seller_id);

				log.info("********************** 支付成功(支付宝同步通知) **********************");
				log.info("* 订单号: {}", out_trade_no);
				log.info("* 支付宝交易号: {}", trade_no);
				log.info("* 实付金额: {}", total_amount);
				log.info("***************************************************************");
				log.info("-----执行逻辑-----");
				Integer TradeStatus = aLiPayServices.queryTradeStatus(out_trade_no);
				// 判断是否已更改过状态
				if (TradeStatus == 8) {
					PayConsumeDeposit payConsumeDeposit = aLiPayServices.queryPayConsumeDeposit(out_trade_no);
					if (out_trade_no.equals(payConsumeDeposit.getDeposit_trade_no().toString())
							&& total_amount.equals(payConsumeDeposit.getDeposit_total_fee().toString())
							&& seller_id.equals(ALiPayConfig.sellerId)) {
						log.info("-----判断成功-----");
						// 更新pdb_pay_consume_deposit所需参数
						String depositGmtPayment = timeStampUtil.getNowDate();
						String depositGmtClose = timeStampUtil.getNowDate();
						Integer depositTradeStatus = 6;
						String depositReturnTradeNo = trade_no;
						Integer depositIsRechargePackage = 0;
						log.info("-----开始更新-----");
						boolean isUpdateDepositSuccess = aLiPayServices.updateDeposit(depositGmtPayment,
								depositGmtClose, depositTradeStatus, depositReturnTradeNo, depositIsRechargePackage,
								out_trade_no);
						log.info("-----开始更新-----");
						boolean isUpdateRecord = aLiPayServices.updateRecord(depositGmtPayment, out_trade_no);
						// 更新成功，增加金额
						if (isUpdateDepositSuccess && isUpdateRecord) {
							log.info("-----更新成功-----");
							String money = payConsumeDeposit.getDeposit_total_fee();
							Integer userId = payConsumeDeposit.getDeposit_buyer_id();
							aLiPayServices.updateUserResource(money, userId);
						} else {
							log.info("-----更新失败-----");
						}
					} else {
						log.info("订单号或订单金额不符！");
					}
				} else {
					log.info("状态已更改，执行完毕！");
				}
			} else {
				log.info("-----验签失败-----");
			}
			response.sendRedirect("https://www.fkgou.com/wap/pages/wallet.html");
		} catch (Exception e) {
			log.info("错误结果2：" + e.getMessage());
		}
		return;
	}

	/**
	 * 支付宝充值pc同步回调
	 * 
	 * @param para
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	@RequestMapping("alipayRechargeReturnUrlByPc")
	public void alipayRechargeReturnUrlByPc(HttpServletRequest request, HttpServletResponse response) {
		log.info("支付成功, 进入同步通知接口...");

		// 获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			} // 乱码解决，这段代码在出现乱码时使用
			try {
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
				params.put(name, valueStr);
			} catch (Exception e) {
				log.info(e.getMessage());
			}
		}

		try {
			log.info("进入验签...");
			// 验签
			boolean signVerified = AlipaySignature.rsaCheckV1(params, ALiPayConfig.ALIPAY_PUBLIC_KEY,
					ALiPayConfig.CHARSET, ALiPayConfig.SIGNTYPE);
			if (signVerified) {
				log.info("验签成功...");
				// 商户订单号
				String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
				log.info("out_trade_no..." + out_trade_no);
				// 支付宝交易号
				String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
				log.info("trade_no..." + trade_no);
				// 付款金额
				String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
				log.info("total_amount..." + total_amount);
				// 卖家支付宝用户号
				String seller_id = new String(request.getParameter("seller_id").getBytes("ISO-8859-1"), "UTF-8");
				log.info("seller_id..." + seller_id);

				log.info("********************** 支付成功(支付宝同步通知) **********************");
				log.info("* 订单号: {}", out_trade_no);
				log.info("* 支付宝交易号: {}", trade_no);
				log.info("* 实付金额: {}", total_amount);
				log.info("***************************************************************");
				log.info("-----执行逻辑-----");
				Integer TradeStatus = aLiPayServices.queryTradeStatus(out_trade_no);
				// 判断是否已更改过状态
				if (TradeStatus == 8) {
					PayConsumeDeposit payConsumeDeposit = aLiPayServices.queryPayConsumeDeposit(out_trade_no);
					if (out_trade_no.equals(payConsumeDeposit.getDeposit_trade_no().toString())
							&& total_amount.equals(payConsumeDeposit.getDeposit_total_fee().toString())
							&& seller_id.equals(ALiPayConfig.sellerId)) {
						log.info("-----判断成功-----");
						// 更新pdb_pay_consume_deposit所需参数
						String depositGmtPayment = timeStampUtil.getNowDate();
						String depositGmtClose = timeStampUtil.getNowDate();
						Integer depositTradeStatus = 6;
						String depositReturnTradeNo = trade_no;
						Integer depositIsRechargePackage = 0;
						log.info("-----开始更新-----");
						boolean isUpdateDepositSuccess = aLiPayServices.updateDeposit(depositGmtPayment,
								depositGmtClose, depositTradeStatus, depositReturnTradeNo, depositIsRechargePackage,
								out_trade_no);
						log.info("-----开始更新-----");
						boolean isUpdateRecord = aLiPayServices.updateRecord(depositGmtPayment, out_trade_no);
						// 更新成功，增加金额
						if (isUpdateDepositSuccess && isUpdateRecord) {
							log.info("-----更新成功-----");
							String money = payConsumeDeposit.getDeposit_total_fee();
							Integer userId = payConsumeDeposit.getDeposit_buyer_id();
							aLiPayServices.updateUserResource(money, userId);
						} else {
							log.info("-----更新失败-----");
						}
					} else {
						log.info("订单号或订单金额不符！");
					}
				} else {
					log.info("状态已更改，执行完毕！");
				}
			} else {
				log.info("-----验签失败-----");
			}
			response.sendRedirect("https://www.fkgou.com/pc/fkg/pages/fkpay.html?fkg=");
		} catch (Exception e) {
			log.info("错误结果2：" + e.getMessage());
		}
		return;
	}
}