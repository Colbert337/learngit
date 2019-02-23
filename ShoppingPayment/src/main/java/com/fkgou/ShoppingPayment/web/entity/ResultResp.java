package com.fkgou.ShoppingPayment.web.entity;

/**
 * 公共返回对象
 * 
 * @author Poi
 *
 */
public class ResultResp {

	private Integer code;// 1:成功;

	private Object data;// 数据对象

	private String desc;// 描述

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
