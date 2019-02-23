package com.fkgou.ShoppingPayment.web.entity.fkPayEntity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单记录展示
 * 
 * @author ilove
 *
 */
public class ComsumeRecordPara {
	private Integer consume_record_id;
	private String order_id;
	private BigDecimal record_money;
	private Date record_time;
	private Integer record_status;
	private Integer record_delete;

	public Integer getConsume_record_id() {
		return consume_record_id;
	}

	public void setConsume_record_id(Integer consume_record_id) {
		this.consume_record_id = consume_record_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public BigDecimal getRecord_money() {
		return record_money;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRecord_time() {
		return record_time;
	}

	public Integer getRecord_status() {
		return record_status;
	}

	public Integer getRecord_delete() {
		return record_delete;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public void setRecord_money(BigDecimal record_money) {
		this.record_money = record_money;
	}

	public void setRecord_time(Date record_time) {
		this.record_time = record_time;
	}

	public void setRecord_status(Integer record_status) {
		this.record_status = record_status;
	}

	public void setRecord_delete(Integer record_delete) {
		this.record_delete = record_delete;
	}

}
