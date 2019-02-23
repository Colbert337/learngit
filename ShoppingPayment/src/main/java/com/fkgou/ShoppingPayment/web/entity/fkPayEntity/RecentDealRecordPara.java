package com.fkgou.ShoppingPayment.web.entity.fkPayEntity;

import java.util.List;

/**
 * 最近交易记录参数
 * 
 * @author ilove
 *
 */
public class RecentDealRecordPara {
	private List<Integer> tradeTypeId;// 查询所有交易类型
	private List<ComsumeRecordPara> comsumeRecord;// 订单记录

	public List<Integer> getTradeTypeId() {
		return tradeTypeId;
	}

	public List<ComsumeRecordPara> getComsumeRecord() {
		return comsumeRecord;
	}

	public void setTradeTypeId(List<Integer> tradeTypeId) {
		this.tradeTypeId = tradeTypeId;
	}

	public void setComsumeRecord(List<ComsumeRecordPara> comsumeRecord) {
		this.comsumeRecord = comsumeRecord;
	}

}
