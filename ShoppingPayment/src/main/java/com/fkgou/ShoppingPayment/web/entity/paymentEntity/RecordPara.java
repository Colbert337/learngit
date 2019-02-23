package com.fkgou.ShoppingPayment.web.entity.paymentEntity;

import java.math.BigDecimal;

/**
 * 支付参数
 * 
 * @author ilove
 *
 */
public class RecordPara {
	private String order_id;
	private String record_desc;
	private BigDecimal record_money;

	public BigDecimal getRecord_money() {
		return record_money;
	}

	public void setRecord_money(BigDecimal record_money) {
		this.record_money = record_money;
	}

	public String getRecord_desc() {
		return record_desc;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setRecord_desc(String record_desc) {
		this.record_desc = record_desc;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

}
