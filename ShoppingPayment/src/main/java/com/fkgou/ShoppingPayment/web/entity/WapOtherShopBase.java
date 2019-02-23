package com.fkgou.ShoppingPayment.web.entity;

import java.math.BigDecimal;
import java.util.List;

public class WapOtherShopBase {

	private Integer shopId;

	private String shopName;

	private BigDecimal shippingFee;

	private Integer shopFreeShipping;// 店铺免额度

	private List<WapShippingQueryResult> goodsBaseList;

	public Integer getShopFreeShipping() {
		return shopFreeShipping;
	}

	public void setShopFreeShipping(Integer shopFreeShipping) {
		this.shopFreeShipping = shopFreeShipping;
	}

	public BigDecimal getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(BigDecimal shippingFee) {
		this.shippingFee = shippingFee;
	}

	public List<WapShippingQueryResult> getGoodsBaseList() {
		return goodsBaseList;
	}

	public void setGoodsBaseList(List<WapShippingQueryResult> goodsBaseList) {
		this.goodsBaseList = goodsBaseList;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

}
