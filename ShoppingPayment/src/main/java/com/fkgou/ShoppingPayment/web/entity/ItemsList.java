package com.fkgou.ShoppingPayment.web.entity;

/**
 * 
 * @author Poi
 *
 */
public class ItemsList {

	private Integer goodsId;
	private Integer commonId;
	private String goodsName;
	private String orderSpecChineseValue;
	private Integer orderGoodsNum;
	private String goodsImage;
	private double orderGoodsPrice;
	private double CommodityPrice;// 总价
	private double theAmountActuallyPaid;// 实付金额
	private String amountActuallyPaid;
	private String goodsNo;
	private String goodsNumber;
	private String commonID;

	public String getAmountActuallyPaid() {
		return amountActuallyPaid;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public String getGoodsNumber() {
		return goodsNumber;
	}

	public String getCommonID() {
		return commonID;
	}

	public void setAmountActuallyPaid(String amountActuallyPaid) {
		this.amountActuallyPaid = amountActuallyPaid;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public void setCommonID(String commonID) {
		this.commonID = commonID;
	}

	public double getCommodityPrice() {
		return CommodityPrice;
	}

	public double getTheAmountActuallyPaid() {
		return theAmountActuallyPaid;
	}

	public void setCommodityPrice(double commodityPrice) {
		CommodityPrice = commodityPrice;
	}

	public void setTheAmountActuallyPaid(double theAmountActuallyPaid) {
		this.theAmountActuallyPaid = theAmountActuallyPaid;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public Integer getCommonId() {
		return commonId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public String getOrderSpecChineseValue() {
		return orderSpecChineseValue;
	}

	public Integer getOrderGoodsNum() {
		return orderGoodsNum;
	}

	public String getGoodsImage() {
		return goodsImage;
	}

	public double getOrderGoodsPrice() {
		return orderGoodsPrice;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public void setCommonId(Integer commonId) {
		this.commonId = commonId;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public void setOrderSpecChineseValue(String orderSpecChineseValue) {
		this.orderSpecChineseValue = orderSpecChineseValue;
	}

	public void setOrderGoodsNum(Integer orderGoodsNum) {
		this.orderGoodsNum = orderGoodsNum;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public void setOrderGoodsPrice(double orderGoodsPrice) {
		this.orderGoodsPrice = orderGoodsPrice;
	}

	@Override
	public String toString() {
		return "itemsList [goodsId=" + goodsId + ", commonId=" + commonId + ", goodsName=" + goodsName
				+ ", orderSpecChineseValue=" + orderSpecChineseValue + ", orderGoodsNum=" + orderGoodsNum
				+ ", goodsImage=" + goodsImage + ", orderGoodsPrice=" + orderGoodsPrice + "]";
	}

}
