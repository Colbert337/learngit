package com.fkgou.ShoppingPayment.web.entity.fkPayEntity;

import java.math.BigDecimal;

/**
 * feign调用购物详情
 * 
 * @author ilove
 *
 */
public class ShoppingDetailsPara {
	private String shop_name;
	private Integer pay_code;
	private BigDecimal order_discount_fee;
	private BigDecimal order_shipping_fee;
	private Integer order_discount_type;

	public String getShop_name() {
		return shop_name;
	}

	public Integer getPay_code() {
		return pay_code;
	}

	public BigDecimal getOrder_discount_fee() {
		return order_discount_fee;
	}

	public BigDecimal getOrder_shipping_fee() {
		return order_shipping_fee;
	}

	public Integer getOrder_discount_type() {
		return order_discount_type;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public void setPay_code(Integer pay_code) {
		this.pay_code = pay_code;
	}

	public void setOrder_discount_fee(BigDecimal order_discount_fee) {
		this.order_discount_fee = order_discount_fee;
	}

	public void setOrder_shipping_fee(BigDecimal order_shipping_fee) {
		this.order_shipping_fee = order_shipping_fee;
	}

	public void setOrder_discount_type(Integer order_discount_type) {
		this.order_discount_type = order_discount_type;
	}

}
