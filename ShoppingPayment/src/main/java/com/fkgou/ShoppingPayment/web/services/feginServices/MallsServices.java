package com.fkgou.ShoppingPayment.web.services.feginServices;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.entity.WapGoodsSaleSumUpdate;
import com.fkgou.ShoppingPayment.web.entity.userInfo.ShopUserMessageResult;

/***
 * fegin
 * 
 * @author ilove
 *
 */

// @FeignClient(name = "mallms", url = "http://mall.fkgou.com/mallms")
@FeignClient(name = "mallms")
public interface MallsServices {

	/**
	 * 
	 * @param message
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/mallms/wap/other/get/cart/message")
	public ResultResp getCartMessage(@RequestBody String message);

	/**
	 * 获取商品信息
	 * 
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/mallms/wap/other/get/cart/getGoodsDetails")
	public ResultResp getGoodsDetails(@RequestParam("goodsId") Integer goodsId);

	/**
	 * 更新库存及销量
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/mallms/wap/other/update/salesum/and/stock")
	public ResultResp updateSaleSum(@RequestBody Map<String, List<WapGoodsSaleSumUpdate>> map);

	/**
	 * 更新使用红包,优惠券的状态
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/mallms/salesprm/salesprm/update")
	public Integer updateSalesPrm(@RequestBody Map<String, Object> map);

	/**
	 * 获取卖家id卖家名
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/mallms/wap/other/userId/query")
	public ShopUserMessageResult queryUserId(@RequestParam("shopId") Integer shopId);
}
