package com.fkgou.ShoppingPayment.web.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 生成订单
 * 
 * @author Poi
 *
 */
public class PayConsumeTrade {
	private String consume_trade_id;
	private String order_id;
	private Integer buyer_id;
	private Integer seller_id;
	private Integer pay_user_id;
	private Integer order_state_id;
	private Integer trade_type_id;
	private Integer payment_channel_id;
	private Integer trade_type;
	private double order_payment_amount;
	private double trade_payment_amount;
	private double trade_payment_money;
	private double trade_payment_recharge_card;
	private double trade_payment_points;
	private double trade_discount;
	private double trade_commis_amount;
	private double trade_commis_refund;
	private double trade_refund_amount;
	private double trade_amount;
	private String trade_date;
	private Integer trade_year;
	private Integer trade_month;
	private Integer trade_day;
	private String trade_title;
	private String trade_desc;
	private String trade_remark;
	private Date trade_create_time;
	private Date trade_pay_time;
	private Date trade_finish_time;
	private Integer trade_delete;
	private double trade_shop_bear;
	private double trade_platform_bear;

	public String getConsume_trade_id() {
		return consume_trade_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public Integer getBuyer_id() {
		return buyer_id;
	}

	public Integer getSeller_id() {
		return seller_id;
	}

	public Integer getPay_user_id() {
		return pay_user_id;
	}

	public Integer getOrder_state_id() {
		return order_state_id;
	}

	public Integer getTrade_type_id() {
		return trade_type_id;
	}

	public Integer getPayment_channel_id() {
		return payment_channel_id;
	}

	public Integer getTrade_type() {
		return trade_type;
	}

	public double getOrder_payment_amount() {
		return order_payment_amount;
	}

	public double getTrade_payment_amount() {
		return trade_payment_amount;
	}

	public double getTrade_payment_money() {
		return trade_payment_money;
	}

	public double getTrade_payment_recharge_card() {
		return trade_payment_recharge_card;
	}

	public double getTrade_payment_points() {
		return trade_payment_points;
	}

	public double getTrade_discount() {
		return trade_discount;
	}

	public double getTrade_commis_amount() {
		return trade_commis_amount;
	}

	public double getTrade_commis_refund() {
		return trade_commis_refund;
	}

	public double getTrade_refund_amount() {
		return trade_refund_amount;
	}

	public double getTrade_amount() {
		return trade_amount;
	}

	public String getTrade_date() {
		return trade_date;
	}

	public Integer getTrade_year() {
		return trade_year;
	}

	public Integer getTrade_month() {
		return trade_month;
	}

	public Integer getTrade_day() {
		return trade_day;
	}

	public String getTrade_title() {
		return trade_title;
	}

	public String getTrade_desc() {
		return trade_desc;
	}

	public String getTrade_remark() {
		return trade_remark;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTrade_create_time() {
		return trade_create_time;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTrade_pay_time() {
		return trade_pay_time;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTrade_finish_time() {
		return trade_finish_time;
	}

	public Integer getTrade_delete() {
		return trade_delete;
	}

	public double getTrade_shop_bear() {
		return trade_shop_bear;
	}

	public double getTrade_platform_bear() {
		return trade_platform_bear;
	}

	public void setConsume_trade_id(String consume_trade_id) {
		this.consume_trade_id = consume_trade_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public void setBuyer_id(Integer buyer_id) {
		this.buyer_id = buyer_id;
	}

	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}

	public void setPay_user_id(Integer pay_user_id) {
		this.pay_user_id = pay_user_id;
	}

	public void setOrder_state_id(Integer order_state_id) {
		this.order_state_id = order_state_id;
	}

	public void setTrade_type_id(Integer trade_type_id) {
		this.trade_type_id = trade_type_id;
	}

	public void setPayment_channel_id(Integer payment_channel_id) {
		this.payment_channel_id = payment_channel_id;
	}

	public void setTrade_type(Integer trade_type) {
		this.trade_type = trade_type;
	}

	public void setOrder_payment_amount(double order_payment_amount) {
		this.order_payment_amount = order_payment_amount;
	}

	public void setTrade_payment_amount(double trade_payment_amount) {
		this.trade_payment_amount = trade_payment_amount;
	}

	public void setTrade_payment_money(double trade_payment_money) {
		this.trade_payment_money = trade_payment_money;
	}

	public void setTrade_payment_recharge_card(double trade_payment_recharge_card) {
		this.trade_payment_recharge_card = trade_payment_recharge_card;
	}

	public void setTrade_payment_points(double trade_payment_points) {
		this.trade_payment_points = trade_payment_points;
	}

	public void setTrade_discount(double trade_discount) {
		this.trade_discount = trade_discount;
	}

	public void setTrade_commis_amount(double trade_commis_amount) {
		this.trade_commis_amount = trade_commis_amount;
	}

	public void setTrade_commis_refund(double trade_commis_refund) {
		this.trade_commis_refund = trade_commis_refund;
	}

	public void setTrade_refund_amount(double trade_refund_amount) {
		this.trade_refund_amount = trade_refund_amount;
	}

	public void setTrade_amount(double trade_amount) {
		this.trade_amount = trade_amount;
	}

	public void setTrade_date(String trade_date) {
		this.trade_date = trade_date;
	}

	public void setTrade_year(Integer trade_year) {
		this.trade_year = trade_year;
	}

	public void setTrade_month(Integer trade_month) {
		this.trade_month = trade_month;
	}

	public void setTrade_day(Integer trade_day) {
		this.trade_day = trade_day;
	}

	public void setTrade_title(String trade_title) {
		this.trade_title = trade_title;
	}

	public void setTrade_desc(String trade_desc) {
		this.trade_desc = trade_desc;
	}

	public void setTrade_remark(String trade_remark) {
		this.trade_remark = trade_remark;
	}

	public void setTrade_create_time(Date trade_create_time) {
		this.trade_create_time = trade_create_time;
	}

	public void setTrade_pay_time(Date trade_pay_time) {
		this.trade_pay_time = trade_pay_time;
	}

	public void setTrade_finish_time(Date trade_finish_time) {
		this.trade_finish_time = trade_finish_time;
	}

	public void setTrade_delete(Integer trade_delete) {
		this.trade_delete = trade_delete;
	}

	public void setTrade_shop_bear(double trade_shop_bear) {
		this.trade_shop_bear = trade_shop_bear;
	}

	public void setTrade_platform_bear(double trade_platform_bear) {
		this.trade_platform_bear = trade_platform_bear;
	}

}
