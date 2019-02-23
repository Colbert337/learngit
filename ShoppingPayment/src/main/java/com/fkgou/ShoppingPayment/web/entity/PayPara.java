package com.fkgou.ShoppingPayment.web.entity;

import java.math.BigDecimal;

/**
 * 支付所需参数
 * 
 * @author ilove
 *
 */
public class PayPara {

	private String goods_name;
	private BigDecimal order_payment_amount;

	public String getGoods_name() {
		return goods_name;
	}

	public BigDecimal getOrder_payment_amount() {
		return order_payment_amount;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public void setOrder_payment_amount(BigDecimal order_payment_amount) {
		this.order_payment_amount = order_payment_amount;
	}

}
