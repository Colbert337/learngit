package com.fkgou.ShoppingPayment.web.controller.otherPaymentController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.services.otherPaymentServices.ALiPayByAppServices;

/**
 * app支付宝支付
 * 
 * @author ilove
 *
 */

@CrossOrigin
@RestController
@RequestMapping("ALiPay")
public class ALiPayByAppController {

	@Autowired
	ALiPayByAppServices aLiPayByAppServices;

	/**
	 * 支付宝支付
	 * 
	 * @param para
	 * @return
	 */
	@RequestMapping("userAliPayToApp")
	public ResultResp userAliPayToApp(@RequestBody Map<Object, Object> para) {
		ResultResp rr = aLiPayByAppServices.userAliPayToApp(para);
		return rr;
	}

	/**
	 * 支付宝充值
	 * 
	 * @param para
	 * @return
	 */
	@RequestMapping("userAliPayByRechargeToApp")
	public ResultResp userAliPayByRechargeToApp(@RequestBody Map<Object, Object> para) {
		ResultResp rr = aLiPayByAppServices.userAliPayByRechargeToApp(para);
		return rr;
	}

}
