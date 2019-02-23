package com.fkgou.ShoppingPayment.web.entity.callBackEntity;

import java.math.BigDecimal;

/**
 * 支付宝回调参数用于对比
 * 
 * @author ilove
 *
 */
public class PayConsumeRecord {
	private String order_id;
	private Integer user_id;
	private BigDecimal record_money;

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public BigDecimal getRecord_money() {
		return record_money;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public void setRecord_money(BigDecimal record_money) {
		this.record_money = record_money;
	}

	@Override
	public String toString() {
		return "PayConsumeRecord [order_id=" + order_id + ", record_money=" + record_money + "]";
	}

}
