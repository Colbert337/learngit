package com.fkgou.ShoppingPayment.web.controller.printController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fkgou.ShoppingPayment.web.services.payPrintServices.PrintPayServiceImpl;

@CrossOrigin
@RestController
@RequestMapping("/payprint")
public class PrintOrderController {

	@Autowired
	PrintPayServiceImpl printPayServiceImpl;

	@RequestMapping(value = "/updateUnionOrderState", method = RequestMethod.GET)
	public int updateUnionOrderState(@RequestParam("orderId") String orderId) {
		int base = printPayServiceImpl.updateUnionOrderState(orderId);
		return base;
	}

	@RequestMapping(value = "/updateConsumeTradeState", method = RequestMethod.GET)
	public int updateConsumeTradeState(@RequestParam("orderId") String orderId) {
		int base = printPayServiceImpl.updateConsumeTradeState(orderId);
		return base;
	}

	@RequestMapping(value = "/updateConsunmRecordState", method = RequestMethod.GET)
	public int updateConsunmRecordState(@RequestParam("orderId") String orderId) {
		int base = printPayServiceImpl.updateConsunmRecordState(orderId);
		return base;
	}

	@RequestMapping(value = "/updateunionorderstatus", method = RequestMethod.GET)
	public int updateunionorderstatus(@RequestParam("orderId") String orderId) {
		int base = printPayServiceImpl.updateunionorderstatus(orderId);
		return base;
	}

	@RequestMapping(value = "/updateconsumetradestatus", method = RequestMethod.GET)
	public int updateconsumetradestatus(@RequestParam("orderId") String orderId) {
		int base = printPayServiceImpl.updateconsumetradestatus(orderId);
		return base;
	}

	@RequestMapping(value = "/updateconsunmrecordstatus", method = RequestMethod.GET)
	public int updateconsunmrecordstatus(@RequestParam("orderId") String orderId) {
		int base = printPayServiceImpl.updateconsunmrecordstatus(orderId);
		return base;
	}
}