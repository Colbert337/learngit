package com.fkgou.ShoppingPayment.web.entity.callBackEntity;

/**
 * 支付宝充值回调参数用于对比
 * 
 * @author ilove
 *
 */
public class PayConsumeDeposit {
	private String deposit_trade_no;
	private String deposit_total_fee;
	private Integer deposit_buyer_id;

	public Integer getDeposit_buyer_id() {
		return deposit_buyer_id;
	}

	public void setDeposit_buyer_id(Integer deposit_buyer_id) {
		this.deposit_buyer_id = deposit_buyer_id;
	}

	public String getDeposit_trade_no() {
		return deposit_trade_no;
	}

	public String getDeposit_total_fee() {
		return deposit_total_fee;
	}

	public void setDeposit_trade_no(String deposit_trade_no) {
		this.deposit_trade_no = deposit_trade_no;
	}

	public void setDeposit_total_fee(String deposit_total_fee) {
		this.deposit_total_fee = deposit_total_fee;
	}

}
