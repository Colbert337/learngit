package com.fkgou.ShoppingPayment.web.entity.fkPayEntity;

import java.util.List;

public class FkPayPageInfo {
	private Integer currentPage;// 当前页数
	private Integer pageSize;// 每页显示的记录数
	private Integer totalCount;// 总记录数
	private Integer totalPage;// 总页数
	private List<?> orderRecordList;// 每页的显示的数据

	public Integer getCurrentPage() {
		return currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public List<?> getOrderRecordList() {
		return orderRecordList;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public void setOrderRecordList(List<?> orderRecordList) {
		this.orderRecordList = orderRecordList;
	}

}
