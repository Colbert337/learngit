package com.fkgou.ShoppingPayment.web.controller.feginController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fkgou.ShoppingPayment.web.services.feginServices.ReturnServices;

@CrossOrigin
@RestController
@RequestMapping("Return")
public class ReturnController {

	@Autowired
	ReturnServices returnServices;

	/**
	 * 插入record数据表
	 * 
	 * @param returnMap
	 * @return
	 */
	@RequestMapping("/insertrecord")
	public Integer insertRecord(@RequestBody Map<String, Map<String, String>> returnMap) {
		Integer torf = returnServices.insertRecord(returnMap);
		return torf;
	}

	/**
	 * 更新用户&卖家资产
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/updatemoney")
	public Integer updateMoney(@RequestBody Map<String, String> map) {
		Integer torf = returnServices.updateMoney(map);
		return torf;
	}

}
