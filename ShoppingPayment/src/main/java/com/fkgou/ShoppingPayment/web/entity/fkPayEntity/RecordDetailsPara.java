package com.fkgou.ShoppingPayment.web.entity.fkPayEntity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单详情
 * 
 * @author ilove
 *
 */
public class RecordDetailsPara {
	private String order_id;
	private BigDecimal record_money;
	private String record_title;
	private String record_desc;
	private Date record_time;
	private Integer trade_type_id;
	private Integer record_status;
	private Date record_paytime;
	private Integer plus_or_minus;

	public String getRecord_title() {
		return record_title;
	}

	public void setRecord_title(String record_title) {
		this.record_title = record_title;
	}

	public String getOrder_id() {
		return order_id;
	}

	public BigDecimal getRecord_money() {
		return record_money;
	}

	public String getRecord_desc() {
		return record_desc;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRecord_time() {
		return record_time;
	}

	public Integer getTrade_type_id() {
		return trade_type_id;
	}

	public Integer getRecord_status() {
		return record_status;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRecord_paytime() {
		return record_paytime;
	}

	public Integer getPlus_or_minus() {
		return plus_or_minus;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public void setRecord_money(BigDecimal record_money) {
		this.record_money = record_money;
	}

	public void setRecord_desc(String record_desc) {
		this.record_desc = record_desc;
	}

	public void setRecord_time(Date record_time) {
		this.record_time = record_time;
	}

	public void setTrade_type_id(Integer trade_type_id) {
		this.trade_type_id = trade_type_id;
	}

	public void setRecord_status(Integer record_status) {
		this.record_status = record_status;
	}

	public void setRecord_paytime(Date record_paytime) {
		this.record_paytime = record_paytime;
	}

	public void setPlus_or_minus(Integer plus_or_minus) {
		this.plus_or_minus = plus_or_minus;
	}

}
