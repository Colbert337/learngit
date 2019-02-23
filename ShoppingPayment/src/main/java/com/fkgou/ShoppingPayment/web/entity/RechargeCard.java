package com.fkgou.ShoppingPayment.web.entity;

import java.math.BigDecimal;

/**
 * 充值卡信息
 * 
 * @author ilove
 *
 */
public class RechargeCard {
	private Integer user_id;
	private BigDecimal card_money;

	public Integer getUser_id() {
		return user_id;
	}

	public BigDecimal getCard_money() {
		return card_money;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public void setCard_money(BigDecimal card_money) {
		this.card_money = card_money;
	}

}
