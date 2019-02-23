package com.fkgou.ShoppingPayment.web.entity.paymentEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 卖家信息
 * 
 * @author ilove
 *
 */
public class ConsumeRecordByBusiness {
	private String orderId;
	private Integer sellId;
	private String userName;
	private String paymentNumber;
	private BigDecimal totalPrice;
	private List<String> goodsName;

	public String getOrderId() {
		return orderId;
	}

	public Integer getSellId() {
		return sellId;
	}

	public String getUserName() {
		return userName;
	}

	public String getPaymentNumber() {
		return paymentNumber;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public List<String> getGoodsName() {
		return goodsName;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setSellId(Integer sellId) {
		this.sellId = sellId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void setGoodsName(List<String> goodsName) {
		this.goodsName = goodsName;
	}

}
