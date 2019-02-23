package com.fkgou.ShoppingPayment.web.entity;

import java.util.List;

/**
 * 店铺编号查询具体信息
 * 
 * @author Poi
 *
 */
public class StoreInformation {
	private Integer couponId;
	private Integer shopId;
	private String buyerMessage;
	private List<GoodsInformation> goodsBaseList;

	public String getBuyerMessage() {
		return buyerMessage;
	}

	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}

	public List<GoodsInformation> getGoodsBaseList() {
		return goodsBaseList;
	}

	public void setGoodsBaseList(List<GoodsInformation> goodsBaseList) {
		this.goodsBaseList = goodsBaseList;
	}

	public Integer getCouponId() {
		return couponId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

}
