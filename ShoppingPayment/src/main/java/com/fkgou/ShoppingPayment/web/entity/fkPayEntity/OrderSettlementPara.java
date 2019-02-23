package com.fkgou.ShoppingPayment.web.entity.fkPayEntity;

import java.math.BigDecimal;

/**
 * 结算单参数
 * 
 * @author ilove
 *
 */
public class OrderSettlementPara {
	private Integer consume_record_id;
	private String order_id;
	private BigDecimal record_money;

	public Integer getConsume_record_id() {
		return consume_record_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public BigDecimal getRecord_money() {
		return record_money;
	}

	public void setConsume_record_id(Integer consume_record_id) {
		this.consume_record_id = consume_record_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public void setRecord_money(BigDecimal record_money) {
		this.record_money = record_money;
	}

}
