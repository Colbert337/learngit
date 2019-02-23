package com.fkgou.ShoppingPayment.web.entity.paymentEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * ConsumeRecord参数
 * 
 * @author ilove
 *
 */
public class ConsumeRecordPara {
	private Integer userId;
	private String orderId;
	private String userName;
	private String paymentNumber;
	private BigDecimal totalPrice;
	private List<String> goodsName;

	public List<String> getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(List<String> goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getPaymentNumber() {
		return paymentNumber;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

}
