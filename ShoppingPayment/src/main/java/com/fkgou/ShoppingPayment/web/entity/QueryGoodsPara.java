package com.fkgou.ShoppingPayment.web.entity;

import java.math.BigDecimal;

/**
 * 查询商品价格商品名
 * 
 * @author ilove
 *
 */
public class QueryGoodsPara {
	private String trade_desc;
	private BigDecimal trade_payment_amount;


	public BigDecimal getTrade_payment_amount() {
		return trade_payment_amount;
	}

	public String getTrade_desc() {
		return trade_desc;
	}

	public void setTrade_payment_amount(BigDecimal trade_payment_amount) {
		this.trade_payment_amount = trade_payment_amount;
	}

	public void setTrade_desc(String trade_desc) {
		this.trade_desc = trade_desc;
	}

}
