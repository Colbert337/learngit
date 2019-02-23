package com.fkgou.ShoppingPayment.web.dao.payPrintDao;

import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface PrintPayMapper {

	@Update("update pdb_pay_union_order\r\n" + "set \r\n" + "order_state_id =4\r\n"
			+ "where inorder = #{orderId,jdbcType=VARCHAR}")
	int updateUnionOrderState(String orderId);

	@Update("update pdb_pay_consume_trade\r\n" + "set \r\n" + " order_state_id =4\r\n"
			+ "where order_id = #{orderId,jdbcType=VARCHAR}")
	int updateConsumeTradeState(String orderId);

	@Update("update pdb_pay_consume_record\r\n" + "set \r\n" + "record_status =4\r\n"
			+ "where order_id = #{orderId,jdbcType=VARCHAR}")
	int updateConsunmRecordState(String orderId);

	@Update("update pdb_pay_union_order\r\n" + "set \r\n" + "order_state_id =2\r\n"
			+ "where inorder = #{orderId,jdbcType=VARCHAR}")
	int updateunionorderstatus(String orderId);

	@Update("update pdb_pay_consume_record\r\n" + "set \r\n" + "record_status =4\r\n"
			+ "where order_id = #{orderId,jdbcType=VARCHAR}")
	int updateconsumetradestatus(String orderId);

	@Update("update pdb_pay_consume_trade\r\n" + "set \r\n" + " order_state_id =2\r\n"
			+ "where order_id = #{orderId,jdbcType=VARCHAR}")
	int updateconsunmrecordstatus(String orderId);

}