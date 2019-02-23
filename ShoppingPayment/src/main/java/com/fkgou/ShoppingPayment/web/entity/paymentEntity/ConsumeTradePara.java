package com.fkgou.ShoppingPayment.web.entity.paymentEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * consumeTrade参数
 * 
 * @author ilove
 *
 */
public class ConsumeTradePara {
	private Integer userId;
	private Integer sellerId;
	private String orderId;
	private String paymentNumber;
	private List<String> goodsName;
	private BigDecimal totalPrice;
	private BigDecimal tradeDiscount;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<String> getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(List<String> goodsName) {
		this.goodsName = goodsName;
	}

	public String getPaymentNumber() {
		return paymentNumber;
	}

	public Integer getUserId() {
		return userId;
	}

	public Integer getSellerId() {
		return sellerId;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public BigDecimal getTradeDiscount() {
		return tradeDiscount;
	}

	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void setTradeDiscount(BigDecimal tradeDiscount) {
		this.tradeDiscount = tradeDiscount;
	}

}
