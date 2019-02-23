package com.fkgou.ShoppingPayment.web.entity;

import java.math.BigDecimal;

/**
 * 运费集合
 * 
 * @author Poi
 *
 */
public class ShippingList {
	private Integer shopId;
	private BigDecimal shipping;// 每家店铺运费

	public Integer getShopId() {
		return shopId;
	}

	public BigDecimal getShipping() {
		return shipping;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public void setShipping(BigDecimal shipping) {
		this.shipping = shipping;
	}

}
