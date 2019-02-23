package com.fkgou.ShoppingPayment.web.entity;

import java.util.List;

/**
 * 
 * @author Poi
 *
 */
public class PageInfo {
	// private String userAvatar;// 用户头像
	private Integer currPage;// 当前页数
	private Integer pageSize;// 每页显示的记录数
	private Integer totalCount;// 总记录数
	private Integer totalPage;// 总页数
	private String rate;// 好评率
	private List<?> list;// 每页的显示的数据

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public Integer getCurrPage() {
		return currPage;
	}

	public void setCurrPage(Integer currPage) {
		this.currPage = currPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
}
