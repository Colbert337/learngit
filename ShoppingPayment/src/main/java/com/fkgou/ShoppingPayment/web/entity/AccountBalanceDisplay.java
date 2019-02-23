package com.fkgou.ShoppingPayment.web.entity;

import java.math.BigDecimal;

/**
 * 账户余额展示
 * 
 * @author Poi
 *
 */
public class AccountBalanceDisplay {

	private BigDecimal user_money_pending_settlement;// 待结算余额
	private BigDecimal user_money;// 用户资金-预付款充值、转账、结算后的款项等等
	private BigDecimal user_package_money;// 用户充值套餐金额
	private BigDecimal user_money_frozen;// 冻结余额
	private BigDecimal user_recharge_card;// 充值卡余额
	private BigDecimal user_recharge_card_frozen;// 充值卡冻结余额
	private BigDecimal totalUserAssets;// 用户总资产
	private BigDecimal user_points;// 用户积分
	private BigDecimal user_points_frozen;// 冻结积分
	private BigDecimal user_credit_limit;// 信用额度
	private BigDecimal user_credit_availability;// 可用额度
	private BigDecimal user_credit_return;// 已还信用额度
	private Integer user_credit_cycle;// 白条还款周期
	private Integer user_growth;// 成长值

	public BigDecimal getUser_money_pending_settlement() {
		return user_money_pending_settlement;
	}

	public BigDecimal getUser_money() {
		return user_money;
	}

	public BigDecimal getUser_package_money() {
		return user_package_money;
	}

	public BigDecimal getUser_money_frozen() {
		return user_money_frozen;
	}

	public BigDecimal getUser_recharge_card() {
		return user_recharge_card;
	}

	public BigDecimal getUser_recharge_card_frozen() {
		return user_recharge_card_frozen;
	}

	public BigDecimal getTotalUserAssets() {
		return totalUserAssets;
	}

	public BigDecimal getUser_points() {
		return user_points;
	}

	public BigDecimal getUser_points_frozen() {
		return user_points_frozen;
	}

	public BigDecimal getUser_credit_limit() {
		return user_credit_limit;
	}

	public BigDecimal getUser_credit_availability() {
		return user_credit_availability;
	}

	public BigDecimal getUser_credit_return() {
		return user_credit_return;
	}

	public Integer getUser_credit_cycle() {
		return user_credit_cycle;
	}

	public Integer getUser_growth() {
		return user_growth;
	}

	public void setUser_money_pending_settlement(BigDecimal user_money_pending_settlement) {
		this.user_money_pending_settlement = user_money_pending_settlement;
	}

	public void setUser_money(BigDecimal user_money) {
		this.user_money = user_money;
	}

	public void setUser_package_money(BigDecimal user_package_money) {
		this.user_package_money = user_package_money;
	}

	public void setUser_money_frozen(BigDecimal user_money_frozen) {
		this.user_money_frozen = user_money_frozen;
	}

	public void setUser_recharge_card(BigDecimal user_recharge_card) {
		this.user_recharge_card = user_recharge_card;
	}

	public void setUser_recharge_card_frozen(BigDecimal user_recharge_card_frozen) {
		this.user_recharge_card_frozen = user_recharge_card_frozen;
	}

	public void setTotalUserAssets(BigDecimal totalUserAssets) {
		this.totalUserAssets = totalUserAssets;
	}

	public void setUser_points(BigDecimal user_points) {
		this.user_points = user_points;
	}

	public void setUser_points_frozen(BigDecimal user_points_frozen) {
		this.user_points_frozen = user_points_frozen;
	}

	public void setUser_credit_limit(BigDecimal user_credit_limit) {
		this.user_credit_limit = user_credit_limit;
	}

	public void setUser_credit_availability(BigDecimal user_credit_availability) {
		this.user_credit_availability = user_credit_availability;
	}

	public void setUser_credit_return(BigDecimal user_credit_return) {
		this.user_credit_return = user_credit_return;
	}

	public void setUser_credit_cycle(Integer user_credit_cycle) {
		this.user_credit_cycle = user_credit_cycle;
	}

	public void setUser_growth(Integer user_growth) {
		this.user_growth = user_growth;
	}

}
