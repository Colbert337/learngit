package com.fkgou.ShoppingPayment.web.entity.paymentEntity;

import java.math.BigDecimal;

public class AddMoney {

	private Integer sellerId;
	private BigDecimal money;

	public Integer getSellerId() {
		return sellerId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

}
