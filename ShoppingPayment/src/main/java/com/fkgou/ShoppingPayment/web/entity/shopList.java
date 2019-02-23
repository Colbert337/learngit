package com.fkgou.ShoppingPayment.web.entity;

import java.util.List;

/**
 * 
 * @author Poi
 *
 */
public class shopList {
	private Integer shopId;
	private String shopName;
	private List<ItemsList> itemsList;

	public List<ItemsList> getItemsList() {
		return itemsList;
	}

	public void setItemsList(List<ItemsList> itemsList) {
		this.itemsList = itemsList;
	}

	public Integer getShopId() {
		return shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	@Override
	public String toString() {
		return "shopList [shopId=" + shopId + ", shopName=" + shopName + ", itemsList=" + itemsList + "]";
	}

}
