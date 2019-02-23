package com.fkgou.ShoppingPayment.web.entity.fkPayEntity;

import java.util.List;

/**
 * 交易查询
 * 
 * @author ilove
 *
 */
public class TransactionRecordsPara {
	private List<Integer> tradeTypeId;// 交易类型
	private List<Integer> orderStatus;// 订单状态
	private List<ComsumeRecordPara> comsumeRecord;

	public List<Integer> getTradeTypeId() {
		return tradeTypeId;
	}

	public List<Integer> getOrderStatus() {
		return orderStatus;
	}

	public List<ComsumeRecordPara> getComsumeRecord() {
		return comsumeRecord;
	}

	public void setTradeTypeId(List<Integer> tradeTypeId) {
		this.tradeTypeId = tradeTypeId;
	}

	public void setOrderStatus(List<Integer> orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setComsumeRecord(List<ComsumeRecordPara> comsumeRecord) {
		this.comsumeRecord = comsumeRecord;
	}

}
