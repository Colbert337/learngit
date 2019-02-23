package com.fkgou.ShoppingPayment.web.entity.paymentEntity;

/**
 * 微信回调参数对比
 * 
 * @author ilove
 *
 */
public class RechargeCallBackPara {
	private Integer user_id;
	private String order_id;
	private String record_money;

	public Integer getUser_id() {
		return user_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public String getRecord_money() {
		return record_money;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public void setRecord_money(String record_money) {
		this.record_money = record_money;
	}

}
