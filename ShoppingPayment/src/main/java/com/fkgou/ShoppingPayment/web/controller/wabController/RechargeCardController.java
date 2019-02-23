package com.fkgou.ShoppingPayment.web.controller.wabController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.services.wabServicesImpl.RechargeServicesImpl;

/**
 * 充值卡充值
 * 
 * @author Poi
 *
 */

@CrossOrigin
@RestController
@RequestMapping("Recharge")
public class RechargeCardController {

	@Autowired
	RechargeServicesImpl rechargeServices;

	/**
	 * 充值卡充值
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("RechargeCard")
	public ResultResp RechargeCard(@RequestBody Map<Object, Object> map) {
		ResultResp rr = rechargeServices.rechargeCard(map);
		return rr;
	}

}
