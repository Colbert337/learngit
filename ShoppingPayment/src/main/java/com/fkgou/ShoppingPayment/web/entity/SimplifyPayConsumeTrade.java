package com.fkgou.ShoppingPayment.web.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author ilove
 *
 */
public class SimplifyPayConsumeTrade {
	private String order_id;
	private Integer record_status;
	private Integer trade_type_id;
	private BigDecimal record_money;
	private String money;
	private Date record_time;
	private Integer plus_or_minus;

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public Integer getPlus_or_minus() {
		return plus_or_minus;
	}

	public void setPlus_or_minus(Integer plus_or_minus) {
		this.plus_or_minus = plus_or_minus;
	}

	public String getOrder_id() {
		return order_id;
	}

	public Integer getRecord_status() {
		return record_status;
	}

	public Integer getTrade_type_id() {
		return trade_type_id;
	}

	public BigDecimal getRecord_money() {
		return record_money;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRecord_time() {
		return record_time;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public void setRecord_status(Integer record_status) {
		this.record_status = record_status;
	}

	public void setTrade_type_id(Integer trade_type_id) {
		this.trade_type_id = trade_type_id;
	}

	public void setRecord_money(BigDecimal record_money) {
		this.record_money = record_money;
	}

	public void setRecord_time(Date record_time) {
		this.record_time = record_time;
	}

}
