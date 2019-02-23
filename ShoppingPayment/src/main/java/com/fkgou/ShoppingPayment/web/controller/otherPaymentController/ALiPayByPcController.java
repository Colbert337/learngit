
package com.fkgou.ShoppingPayment.web.controller.otherPaymentController;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.services.otherPaymentServices.ALiPayByPCServices;

/**
 * pc端支付宝支付
 * 
 * @author ilove
 *
 */
@CrossOrigin
@RestController
@RequestMapping("ALiPay")
public class ALiPayByPcController {

	@Autowired
	ALiPayByPCServices aLiPayByPCServices;

	/**
	 * 支付宝支付pc
	 * 
	 * @param para
	 * @return
	 */
	@RequestMapping("userAliPayToPc")
	public ResultResp userAliPayToPc(@RequestBody Map<Object, Object> para, HttpServletRequest request,
			HttpServletResponse response) {
		ResultResp rr = aLiPayByPCServices.userAliPayToPc(para, request, response);
		return rr;
	}

	/**
	 * 支付宝充值pc
	 * 
	 * @param para
	 * @return
	 */
	@RequestMapping("userAliPayByRechargeToPc")
	public ResultResp userAliPayByRechargeToPc(@RequestBody Map<Object, Object> para) {
		ResultResp rr = aLiPayByPCServices.userAliPayByRechargeToPc(para);
		return rr;
	}

}
