package com.fkgou.ShoppingPayment.web.services.feginServices;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fkgou.ShoppingPayment.web.entity.ResultResp;

/**
 * 订单相关调用
 * 
 * @author ilove
 *
 */
// @FeignClient(name = "ShoppingOrder", url =
// "http://shop.fkgou.com/ShoppingOrder")
@FeignClient(name = "ShoppingOrder")
public interface OrderServices {

	/**
	 * 查询购物信息
	 * 
	 * @param message
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/ShoppingOrder/Order/queryShoppingDetails")
	public ResultResp queryShoppingDetails(@RequestBody Map<String, Object> map);

	/**
	 * 查询红包优惠券id
	 * 
	 * @param message
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/ShoppingOrder/Order/queryPreferentialId")
	public ResultResp queryPreferentialId(@RequestBody Map<String, Object> map);

}
