package com.fkgou.ShoppingPayment.web.entity;

import java.math.BigDecimal;

public class WapShippingQueryResult {

	private BigDecimal goodsCubage;// 商品重量

	private Integer transportTemplate;// 运费模板

	private Integer goodsId;

	private String goodsName;

	private Integer commonId;

	private Integer catId;// 分类id

	private String goodsSpec;// 规格名

	private BigDecimal goodsPrice;// 商品价格

	private BigDecimal goodsMarketPrice;// 商品市场价格

	private String goodsImage;

	private BigDecimal goodsShippingFee;// 运费

	private Integer goodsNum;

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	public BigDecimal getGoodsCubage() {
		return goodsCubage;
	}

	public void setGoodsCubage(BigDecimal goodsCubage) {
		this.goodsCubage = goodsCubage;
	}

	public Integer getTransportTemplate() {
		return transportTemplate;
	}

	public void setTransportTemplate(Integer transportTemplate) {
		this.transportTemplate = transportTemplate;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getCommonId() {
		return commonId;
	}

	public void setCommonId(Integer commonId) {
		this.commonId = commonId;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public String getGoodsSpec() {
		return goodsSpec;
	}

	public void setGoodsSpec(String goodsSpec) {
		this.goodsSpec = goodsSpec;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public BigDecimal getGoodsMarketPrice() {
		return goodsMarketPrice;
	}

	public void setGoodsMarketPrice(BigDecimal goodsMarketPrice) {
		this.goodsMarketPrice = goodsMarketPrice;
	}

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public BigDecimal getGoodsShippingFee() {
		return goodsShippingFee;
	}

	public void setGoodsShippingFee(BigDecimal goodsShippingFee) {
		this.goodsShippingFee = goodsShippingFee;
	}

}
