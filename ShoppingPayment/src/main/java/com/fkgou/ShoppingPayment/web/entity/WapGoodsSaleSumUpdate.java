package com.fkgou.ShoppingPayment.web.entity;

/**
 * @author sunny
 *
 */
public class WapGoodsSaleSumUpdate {

	private Integer commonId;
	private Integer goodsId;
	private Integer goodsNum;// 商品数据

	public Integer getCommonId() {
		return commonId;
	}

	public void setCommonId(Integer commonId) {
		this.commonId = commonId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

}
