package com.fkgou.ShoppingPayment.web.entity.paymentEntity;

import java.math.BigDecimal;

public class QueryRecordPara {
	private String order_id;
	private Integer buyer_id;
	private String trade_desc;
	private BigDecimal trade_payment_amount;

	public Integer getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(Integer buyer_id) {
		this.buyer_id = buyer_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public String getTrade_desc() {
		return trade_desc;
	}

	public BigDecimal getTrade_payment_amount() {
		return trade_payment_amount;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public void setTrade_desc(String trade_desc) {
		this.trade_desc = trade_desc;
	}

	public void setTrade_payment_amount(BigDecimal trade_payment_amount) {
		this.trade_payment_amount = trade_payment_amount;
	}

}
