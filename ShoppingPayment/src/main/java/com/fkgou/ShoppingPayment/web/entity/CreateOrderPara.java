package com.fkgou.ShoppingPayment.web.entity;

import java.math.BigDecimal;

/**
 * 创建订单
 * 
 * @author ilove
 *
 */
public class CreateOrderPara {
	private String orderId;
	private String token;
	private Integer type;
	private String recordPaytime;
	private BigDecimal amountActuallyPaids;

	public String getRecordPaytime() {
		return recordPaytime;
	}

	public void setRecordPaytime(String recordPaytime) {
		this.recordPaytime = recordPaytime;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getToken() {
		return token;
	}

	public Integer getType() {
		return type;
	}

	public BigDecimal getAmountActuallyPaids() {
		return amountActuallyPaids;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setAmountActuallyPaids(BigDecimal amountActuallyPaids) {
		this.amountActuallyPaids = amountActuallyPaids;
	}

}
