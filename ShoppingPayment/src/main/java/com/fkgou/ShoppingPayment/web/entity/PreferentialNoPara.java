package com.fkgou.ShoppingPayment.web.entity;

import java.util.List;

/**
 * 存放优惠编号
 * 
 * @author ilove
 *
 */
public class PreferentialNoPara {
	private String redpacketId;
	private List<String> voucherIdList;

	public String getRedpacketId() {
		return redpacketId;
	}

	public void setRedpacketId(String redpacketId) {
		this.redpacketId = redpacketId;
	}

	public List<String> getVoucherIdList() {
		return voucherIdList;
	}

	public void setVoucherIdList(List<String> voucherIdList) {
		this.voucherIdList = voucherIdList;
	}

}
