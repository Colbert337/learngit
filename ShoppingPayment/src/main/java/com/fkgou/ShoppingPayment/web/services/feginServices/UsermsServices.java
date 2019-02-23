package com.fkgou.ShoppingPayment.web.services.feginServices;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fkgou.ShoppingPayment.web.entity.userInfo.RpcUserInfo;

/**
 * feign调用user信息
 * 
 * @author ilove
 *
 */
// @FeignClient(name = "userms", url = "http://user.fkgou.com/userms/")
@FeignClient(name = "userms")
public interface UsermsServices {

	/**
	 * 获取用户信息
	 * 
	 * @param userMobile
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/userms/feign/userinfo")
	public RpcUserInfo queryUserId(@RequestParam("userMobile") String userMobile);
}
