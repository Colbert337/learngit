package com.fkgou.ShoppingPayment.web.controller.feginController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fkgou.ShoppingPayment.web.entity.paymentEntity.AddMoney;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.ConsumeRecordByBusiness;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.ConsumeRecordPara;
import com.fkgou.ShoppingPayment.web.entity.paymentEntity.ConsumeTradePara;
import com.fkgou.ShoppingPayment.web.services.consumeServices.ConsumeServices;

@CrossOrigin
@RestController
@RequestMapping("InsertOrder")
public class ConsumeTradeController {

	@Autowired
	ConsumeServices consumeServices;

	/**
	 * 下单成功，添加数据
	 * 
	 * @param ctp
	 * @return
	 */
	@RequestMapping("insertConsumeTrade")
	public boolean insertConsumeTrade(@RequestBody ConsumeTradePara ctp) {
		boolean isInsertSuccess = consumeServices.insertConsumeTrade(ctp);
		return isInsertSuccess;
	}

	/**
	 * 下单成功，添加数据
	 * 
	 * @param crp
	 * @return
	 */
	@RequestMapping("insertConsumeRecord")
	public boolean insertConsumeRecord(@RequestBody ConsumeRecordPara crp) {
		boolean isInsertSuccess = consumeServices.insertConsumeRecord(crp);
		return isInsertSuccess;
	}

	/**
	 * 下单成功，添加数据
	 * 
	 * @param crp
	 * @return
	 */
	@RequestMapping("insertConsumeRecordByBusiness")
	public boolean insertConsumeRecordByBusiness(@RequestBody ConsumeRecordByBusiness crb) {
		boolean isInsertSuccess = consumeServices.insertConsumeRecordByBusiness(crb);
		return isInsertSuccess;
	}

	/**
	 * 发货成功，改变状态
	 * 
	 * @param crp
	 * @return
	 */
	@RequestMapping("changeConsumeRecord")
	public boolean changeConsumeRecord(@RequestBody String orderId) {
		boolean isInsertSuccess = consumeServices.changeConsumeRecord(orderId);
		return isInsertSuccess;
	}

	/**
	 * 添加结算单订单
	 * 
	 * @param crp
	 * @return
	 */
	@RequestMapping("insertSettlementRecord")
	public boolean insertSettlementRecord(@RequestBody Map<String, Object> map) {
		boolean isInsertSuccess = consumeServices.insertSettlementRecord(map);
		return isInsertSuccess;
	}

	/**
	 * 增加卖家金额
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("addMoney")
	public boolean addMoney(@RequestBody AddMoney addMoney) {
		boolean isUpdateSuccess = consumeServices.addMoney(addMoney);
		return isUpdateSuccess;
	}
}
