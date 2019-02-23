package com.fkgou.ShoppingPayment.web.entity.userInfo;

public class RpcUserInfo {
	private String userId;
	private String userMobile;
	private String userName;
	private String userAvatar;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	@Override
	public String toString() {
		return "RpcUserInfo [userId=" + userId + ", userMobile=" + userMobile + ", userName=" + userName
				+ ", userAvatar=" + userAvatar + "]";
	}

}
