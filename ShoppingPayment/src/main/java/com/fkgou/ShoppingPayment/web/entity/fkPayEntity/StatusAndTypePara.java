package com.fkgou.ShoppingPayment.web.entity.fkPayEntity;

import java.util.List;

public class StatusAndTypePara {
	private List<Integer> tradeId;
	private List<Integer> orderStatusId;

	public List<Integer> getTradeId() {
		return tradeId;
	}

	public List<Integer> getOrderStatusId() {
		return orderStatusId;
	}

	public void setTradeId(List<Integer> tradeId) {
		this.tradeId = tradeId;
	}

	public void setOrderStatusId(List<Integer> orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

}
