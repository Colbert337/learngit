package com.fkgou.ShoppingPayment.web.MybatisSQL;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

public class FkPaySql {

	/**
	 * 查询交易订单
	 * 
	 * @param para
	 * @return
	 */
	public String queryOrderRecord(Map<String, Object> para) {
		String sql = new SQL() {
			{
				SELECT("consume_record_id,order_id,record_money,record_time,record_status,record_delete");
				FROM("pdb_pay_consume_record");
				if (para.get("AllType") == "' '") {
					WHERE(" 1 = 1 ");
				}
				if (para.get("tradeTypeId") != "" && para.get("tradeTypeId") != null) {
					WHERE("trade_type_id = " + para.get("tradeTypeId"));
				}
				if (para.get("AllStatus") == "' '") {
					WHERE(" 1 = 1 ");
				}
				if (para.get("orderStatus") != "" && para.get("orderStatus") != null) {
					WHERE("record_status = " + para.get("orderStatus"));
				}
				if (para.get("startTime") != "" && para.get("startTime") != null) {
					WHERE("record_time >= " + "'" + para.get("startTime") + "'");
				}
				if (para.get("endTime") != "" && para.get("endTime") != null) {
					WHERE("record_time <=" + "'" + para.get("endTime") + "'");
				}
				WHERE("user_id = " + para.get("userId"));
			}
		}.toString();
		StringBuffer sb = new StringBuffer(sql);
		sb.append(" and record_delete = 0 order by record_time desc limit #{cp},#{pageSize}");
		return sb.toString();
	}

	/**
	 * 查询订单总数
	 * 
	 * @param para
	 * @return
	 */
	public String getRecordCount(Map<String, Object> para) {
		String sql = new SQL() {
			{
				SELECT("count(*)");
				FROM("pdb_pay_consume_record");
				if (para.get("AllType") == "' '") {
					WHERE(" 1 = 1 ");
				}
				if (para.get("tradeTypeId") != "" && para.get("tradeTypeId") != null) {
					WHERE("trade_type_id = " + para.get("tradeTypeId"));
				}
				if (para.get("AllStatus") == "' '") {
					WHERE(" 1 = 1 ");
				}
				if (para.get("orderStatus") != "" && para.get("orderStatus") != null) {
					WHERE("record_status = " + para.get("orderStatus"));
				}
				if (para.get("startTime") != "" && para.get("startTime") != null) {
					WHERE("record_time >= " + "'" + para.get("startTime") + "'");
				}
				if (para.get("endTime") != "" && para.get("endTime") != null) {
					WHERE("record_time <=" + "'" + para.get("endTime") + "'");
				}
				WHERE("user_id = " + para.get("userId"));
			}
		}.toString();
		return sql.toString();
	}
}
