package com.fkgou.ShoppingPayment.web.services.payPrintServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fkgou.ShoppingPayment.web.dao.payPrintDao.PrintPayMapper;

@Service
public class PrintPayServiceImpl {

	@Autowired
	PrintPayMapper printPayMapper;

	public int updateUnionOrderState(String orderId) {
		int update = printPayMapper.updateUnionOrderState(orderId);
		return update;
	}

	public int updateConsumeTradeState(String orderId) {
		int update = printPayMapper.updateConsumeTradeState(orderId);
		return update;
	}

	public int updateConsunmRecordState(String orderId) {
		int update = printPayMapper.updateConsunmRecordState(orderId);
		return update;
	}

	public int updateunionorderstatus(String orderId) {
		int update = printPayMapper.updateunionorderstatus(orderId);
		return update;
	}

	public int updateconsumetradestatus(String orderId) {
		int update = printPayMapper.updateconsumetradestatus(orderId);
		return update;
	}

	public int updateconsunmrecordstatus(String orderId) {
		int update = printPayMapper.updateconsunmrecordstatus(orderId);
		return update;
	}

}
