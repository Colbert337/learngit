package com.fkgou.ShoppingPayment.web.controller.fkPayController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.services.fkPayServices.FkPayServices;

/**
 * 蜂乐Pay(fkpay)
 * 
 * @author ilove
 *
 */

@CrossOrigin
@RestController
@RequestMapping("FkPay")
public class FKPayController {

	@Autowired
	FkPayServices fkPayServices;

	/**
	 * 商家后台冻结金额
	 * 
	 * @return
	 */
	@RequestMapping("businessFreezesFunds")
	public ResultResp businessFreezesFunds(@RequestBody Map<Object, Object> map) {
		ResultResp rr = fkPayServices.businessFreezesFunds(map);
		return rr;
	}

	/**
	 * 蜂乐pay最近交易记录
	 * 
	 * @return
	 */
	@RequestMapping("fkPayForRecentDealRecord")
	public ResultResp fkPayForRecentDealRecord(@RequestBody Map<Object, Object> map) {
		ResultResp rr = fkPayServices.fkPayForRecentDealRecord(map);
		return rr;
	}

	/**
	 * 蜂乐pay最近交易记录之订单详情
	 * 
	 * @return
	 */
	@RequestMapping("fkPayForRecentDealRecordDetails")
	public ResultResp fkPayForRecentDealRecordDetails(@RequestBody Map<Object, Object> map) {
		ResultResp rr = fkPayServices.fkPayForRecentDealRecordDetails(map);
		return rr;
	}

	/**
	 * 蜂乐pay最近交易记录之删除订单
	 * 
	 * @return
	 */
	@RequestMapping("fkPayForRecentDealRecordDel")
	public ResultResp fkPayForRecentDealRecordDel(@RequestBody Map<Object, Object> map) {
		ResultResp rr = fkPayServices.fkPayForRecentDealRecordDel(map);
		return rr;
	}

	/**
	 * 蜂乐pay交易类型与状态
	 * 
	 * @return
	 */
	@RequestMapping("fkPayForStatus")
	public ResultResp fkPayForStatus() {
		ResultResp rr = fkPayServices.fkPayForStatus();
		return rr;
	}

	/**
	 * 蜂乐pay交易查询
	 * 
	 * @return
	 */
	@RequestMapping("fkPayForTradingQuery")
	public ResultResp fkPayForTradingQuery(@RequestBody Map<Object, Object> map) {
		ResultResp rr = fkPayServices.fkPayForTradingQuery(map);
		return rr;
	}

	/**
	 * 蜂乐pay转账
	 * 
	 * @return
	 */
	@RequestMapping("fkPayForTransfer")
	public ResultResp fkPayForTransfer(@RequestBody Map<Object, Object> map) {
		ResultResp rr = fkPayServices.fkPayForTransfer(map);
		return rr;
	}

	/**
	 * 转账确认转账方信息
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("fkPayForTransferCheckPayeeInfo")
	public ResultResp fkPayForTransferCheckPayeeInfo(@RequestBody Map<Object, Object> map) {
		ResultResp rr = fkPayServices.fkPayForTransferCheckPayeeInfo(map);
		return rr;
	}

	/**
	 * 蜂乐pay提现之获取商家结算单号
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("fkPayForBusinessWithdrawal")
	public ResultResp fkPayForBusinessWithdrawal(@RequestBody Map<Object, Object> map) {
		ResultResp rr = fkPayServices.fkPayForBusinessWithdrawal(map);
		return rr;
	}

	/**
	 * 蜂乐pay提现之计算结算金额
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("fkPayForBusinessWithdrawalMoney")
	public ResultResp fkPayForBusinessWithdrawalMoney(@RequestBody Map<Object, Object> map) {
		ResultResp rr = fkPayServices.fkPayForBusinessWithdrawalMoney(map);
		return rr;
	}

	/**
	 * 蜂乐pay提现
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("fkPayForWithdrawal")
	public ResultResp fkPayForWithdrawal(@RequestBody Map<Object, Object> map) {
		ResultResp rr = fkPayServices.fkPayForWithdrawal(map);
		return rr;
	}

	/**
	 * 蜂乐pay买家端提现app
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("fkPayForWithdrawalByApp")
	public ResultResp fkPayForWithdrawalByApp(@RequestBody Map<Object, Object> map) {
		ResultResp rr = fkPayServices.fkPayForWithdrawalByApp(map);
		return rr;
	}

	/**
	 * 蜂乐pay商家端提现app
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("fkPayForMerchantsWithdrawal")
	public ResultResp fkPayForMerchantsWithdrawal(@RequestBody Map<Object, Object> map) {
		ResultResp rr = fkPayServices.fkPayForMerchantsWithdrawal(map);
		return rr;
	}
}
