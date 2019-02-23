package com.fkgou.ShoppingPayment.web.controller.feginController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fkgou.ShoppingPayment.web.services.consumeServices.CreateUserServices;

/**
 * 创建用户
 * 
 * @author ilove
 *
 */
@CrossOrigin
@RestController
@RequestMapping("ReceiveUserMessage")
public class CreateUserController {

	@Autowired
	CreateUserServices createUserServices;

	/**
	 * 创建用户
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping("CreateUser")
	public boolean createUser(@RequestParam Integer userId) {
		boolean isInsertSuccess = createUserServices.createUser(userId);
		return isInsertSuccess;
	}
}
