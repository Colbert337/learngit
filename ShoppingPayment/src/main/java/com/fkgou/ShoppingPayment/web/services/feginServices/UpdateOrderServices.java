package com.fkgou.ShoppingPayment.web.services.feginServices;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fkgou.ShoppingPayment.web.entity.ResultResp;

/**
 * fegin调用
 * 
 * @author ilove
 *
 */

// @FeignClient(name = "ShoppingOrder", url =
// "http://shop.fkgou.com/ShoppingOrder")
@FeignClient(name = "ShoppingOrder")
public interface UpdateOrderServices {

	/**
	 * 更新orderBase中支付方式等字段
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/ShoppingOrder/UpdateOrderBase/UpdateOrderBaseByPayment")
	public boolean UpdateOrderBaseByPayment(@RequestBody Map<Object, Object> map);

	/**
	 * 支付宝下单
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/ShoppingOrder/Orderms/ALiPayCreateOrders")
	public ResultResp ALiPayCreateOrders(@RequestBody String para);

	/**
	 * H5支付宝支付完成，更新orderBase中支付方式等字段
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/ShoppingOrder/UpdateOrderBase/HFiveUpdateOrderBaseByPayment")
	public boolean HFiveUpdateOrderBaseByPayment(@RequestBody Map<Object, Object> map);

	// /**
	// * 根据支付号查询实际支付单号
	// *
	// * @param payNumber
	// * @return
	// */
	// @RequestMapping(method = RequestMethod.POST, value =
	// "/UpdateOrderBase/queryTrueOrderId")
	// public List<String> queryTrueOrderId(@RequestBody String payNumber);

	/**
	 * 发送下单通知
	 * 
	 * @param payNumber
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/ShoppingOrder/UpdateOrderBase/orderToInform")
	public boolean orderToInform(@RequestBody Map<Object, Object> map);

}
