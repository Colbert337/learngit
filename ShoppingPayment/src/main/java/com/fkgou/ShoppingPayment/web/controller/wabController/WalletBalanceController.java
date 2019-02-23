package com.fkgou.ShoppingPayment.web.controller.wabController;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fkgou.ShoppingPayment.web.entity.AccountBalanceDisplay;
import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.services.wabServicesImpl.WalletBalanceDisplayServicesImpl;
import com.fkgou.ShoppingPayment.web.util.FkGouConfig;
import com.fkgou.ShoppingPayment.web.util.RedisUtil;

@CrossOrigin
@RestController
@RequestMapping("Wallet")
public class WalletBalanceController {

	@Autowired
	private WalletBalanceDisplayServicesImpl walletBalanceDisplayServicesImpl;

	/**
	 * 钱包余额展示
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("balanceDisplay")
	public ResultResp balanceDisplay(@RequestBody Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			String userId = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USERID + token);
			AccountBalanceDisplay acd = walletBalanceDisplayServicesImpl.queryAccountBalance(userId);
			if (acd == null) {
				rr.setCode(2);
				rr.setDesc("数据查询为空！");
			} else {
				BigDecimal userbalance = acd.getUser_money();// 用户余额
				BigDecimal userRechargeCard = acd.getUser_recharge_card();// 用户充值卡金额
				BigDecimal totalUserAssets = userbalance.add(userRechargeCard);// 用户总资产
				acd.setTotalUserAssets(totalUserAssets);
				rr.setCode(1);
				rr.setData(acd);
			}
		} else {
			rr.setCode(2);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 最近交易查询
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("recentTransactionOrder")
	public ResultResp recentTransactionOrder(@RequestBody Map<String, Object> map) {
		ResultResp rr = walletBalanceDisplayServicesImpl.queryRecentOrder(map);
		return rr;
	}

	/**
	 * 卖家端最近交易查询
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("SellerRecentTransactionOrder")
	public ResultResp SellerRecentTransactionOrder(@RequestBody Map<String, Object> map) {
		ResultResp rr = walletBalanceDisplayServicesImpl.SellerRecentTransactionOrder(map);
		return rr;
	}
}
