package com.fkgou.ShoppingPayment.web.controller.otherPaymentController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fkgou.ShoppingPayment.web.config.WXPayConfig;
import com.fkgou.ShoppingPayment.web.entity.QueryGoodsPara;
import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.RecordPara;
import com.fkgou.ShoppingPayment.web.services.otherPaymentServices.WXPayServices;
import com.fkgou.ShoppingPayment.web.util.WXPayUtils.PayToolUtil;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.PaymentApi;
import com.jfinal.weixin.sdk.kit.PaymentKit;

/**
 * 微信支付
 * 
 * @author ilove
 *
 */
@CrossOrigin
@RestController
@RequestMapping("WXPay")
public class WXPayController {

	@Autowired
	WXPayServices WXPayServices;

	/**
	 * 微信购物支付
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("userWXPayToPay")
	public ResultResp userWXPayToPay(@RequestBody Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String out_trade_no = (String) map.get("orderId");
		BigDecimal totalPrice = new BigDecimal(0);
		String goodsName = null;
		// 查询商品名与商品价格
		if (PayToolUtil.isInteger(out_trade_no)) {
			QueryGoodsPara queryGoodsPara = WXPayServices.queryGoodsPriceAndName(out_trade_no);
			totalPrice = queryGoodsPara.getTrade_payment_amount();// 总价
			goodsName = queryGoodsPara.getTrade_desc();
		} else {
			RecordPara rp = WXPayServices.queryRecordPara(out_trade_no);
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
		return rr;
	}

	/**
	 * 微信支付回调
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("notifyWeiXinPay")
	public String notifyWeiXinPay(HttpServletRequest request, HttpServletResponse response) {
		String msg = WXPayServices.notifyWeiXinPay(request, response);
		return msg;
	}

	/**
	 * 微信支付回调wap
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("notifyWeiXinPayByWAP")
	public String notifyWeiXinPayByWAP(HttpServletRequest request, HttpServletResponse response) {
		String msg = WXPayServices.notifyWeiXinPayByWAP(request, response);
		return msg;
	}

	/**
	 * 微信充值回调wap
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("notifyWXPayToRechargeByWap")
	public String notifyWXPayToRechargeByWap(HttpServletRequest request, HttpServletResponse response) {
		String msg = WXPayServices.notifyWXPayToRechargeByWap(request, response);
		return msg;
	}

	/**
	 * 微信充值
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("userWXPayToRecharge")
	public ResultResp userWXPayToRecharge(@RequestBody Map<Object, Object> map) {
		ResultResp rr = WXPayServices.userWXPayToRecharge(map);
		return rr;
	}

	/**
	 * 微信充值回调
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("notifyWXPayToRecharge")
	public String notifyWXPayToRecharge(HttpServletRequest request, HttpServletResponse response) {
		String msg = WXPayServices.notifyWXPayToRecharge(request, response);
		return msg;
	}

	/**
	 * 微信购物支付pc
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("userWXPayToPayByPc")
	public ResultResp userWXPayToPayByPc(@RequestBody Map<Object, Object> map) {
		ResultResp rr = WXPayServices.userWXPayToPayByPc(map);
		return rr;
	}

	/**
	 * 轮询微信支付后状态
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("queryWxPayStatus")
	public ResultResp queryWxPayStatus(@RequestBody Map<Object, Object> map) {
		ResultResp rr = WXPayServices.queryWxPayStatus(map);
		return rr;
	}

	/**
	 * 微信充值pc
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("userWXPayToRechargeByPc")
	public ResultResp userWXPayToRechargeByPc(@RequestBody Map<Object, Object> map) {
		ResultResp rr = WXPayServices.userWXPayToRechargeByPc(map);
		return rr;
	}

	/**
	 * 微信购物支付(h5)
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("userWXPayToPayByHFive")
	public ResultResp userWXPayToPayByHFive(@RequestBody Map<Object, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		ResultResp rr = WXPayServices.userWXPayToPayByHFive(map, request, response);
		return rr;
	}

	/**
	 * 微信充值h5
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("userWXPayToRechargeByHFive")
	public ResultResp userWXPayToRechargeByHFive(@RequestBody Map<Object, Object> map) {
		ResultResp rr = WXPayServices.userWXPayToRechargeByHFive(map);
		return rr;
	}

	/**
	 * 微信购物支付(jsapi)
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("userWXPayToPayByOfficialAccounts")
	public ResultResp userWXPayToPayByOfficialAccounts(@RequestBody Map<Object, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		ResultResp rr = WXPayServices.userWXPayToPayByOfficialAccounts(map, request, response);
		return rr;
	}

	/**
	 * 微信充值(jsapi)
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("userWXPayToRechargeByOfficialAccounts")
	public ResultResp userWXPayToRechargeByOfficialAccounts(@RequestBody Map<Object, Object> map) {
		ResultResp rr = WXPayServices.userWXPayToRechargeByOfficialAccounts(map);
		return rr;
	}
}
