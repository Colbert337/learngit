package com.fkgou.ShoppingPayment.web.entity;

import java.util.List;

/**
 * 
 * @author Poi
 *
 */
public class CommonParameter {
	private String token;
	private String orderReceiverName;
	private String orderReceiverAddress;
	private String orderReceiverContact;
	private Integer orderReceiverProvinceId;
	private Integer orderReceiverCityId;
	private Integer orderReceiverAreaId;
	private Integer redPacketId;
	private Integer areaId;
	private List<StoreInformation> storeInformationList;

	public Integer getOrderReceiverProvinceId() {
		return orderReceiverProvinceId;
	}

	public Integer getOrderReceiverCityId() {
		return orderReceiverCityId;
	}

	public Integer getOrderReceiverAreaId() {
		return orderReceiverAreaId;
	}

	public void setOrderReceiverProvinceId(Integer orderReceiverProvinceId) {
		this.orderReceiverProvinceId = orderReceiverProvinceId;
	}

	public void setOrderReceiverCityId(Integer orderReceiverCityId) {
		this.orderReceiverCityId = orderReceiverCityId;
	}

	public void setOrderReceiverAreaId(Integer orderReceiverAreaId) {
		this.orderReceiverAreaId = orderReceiverAreaId;
	}

	public String getOrderReceiverName() {
		return orderReceiverName;
	}

	public String getOrderReceiverAddress() {
		return orderReceiverAddress;
	}

	public String getOrderReceiverContact() {
		return orderReceiverContact;
	}

	public void setOrderReceiverName(String orderReceiverName) {
		this.orderReceiverName = orderReceiverName;
	}

	public void setOrderReceiverAddress(String orderReceiverAddress) {
		this.orderReceiverAddress = orderReceiverAddress;
	}

	public void setOrderReceiverContact(String orderReceiverContact) {
		this.orderReceiverContact = orderReceiverContact;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getRedPacketId() {
		return redPacketId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public List<StoreInformation> getStoreInformationList() {
		return storeInformationList;
	}

	public void setRedPacketId(Integer redPacketId) {
		this.redPacketId = redPacketId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public void setStoreInformationList(List<StoreInformation> storeInformationList) {
		this.storeInformationList = storeInformationList;
	}

}
