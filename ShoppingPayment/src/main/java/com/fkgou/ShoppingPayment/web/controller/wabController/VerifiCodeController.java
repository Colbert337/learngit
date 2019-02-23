package com.fkgou.ShoppingPayment.web.controller.wabController;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.util.FkGouConfig;
import com.fkgou.ShoppingPayment.web.util.PhoneVerifiCode;
import com.fkgou.ShoppingPayment.web.util.RedisUtil;
import com.fkgou.userms.entity.UserInfo;

/**
 * 验证码
 * 
 * @author Poi
 *
 */

@CrossOrigin
@RestController
@RequestMapping("VerifiCode")
public class VerifiCodeController {

	/**
	 * 生成手机验证码
	 * 
	 * @param userMobile
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("verifiPhoneCode")
	public ResultResp verifiPhoneCode(@RequestBody Map<Object, Object> map) {
		/*
		 * 1.生成验证码 2.把验证码保存再session中
		 */
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
		@SuppressWarnings("unused")
		String userMobiles = (String) map.get("userMobile");// 接收不做处理
		try {
			ObjectMapper mapper = new ObjectMapper();
			UserInfo ui = new UserInfo();
			String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
			ui = mapper.readValue(userinfo, UserInfo.class);// String转object
			String userMobile = ui.getUserMobile();
			PhoneVerifiCode pvc = new PhoneVerifiCode();
			Random random = new Random();
			String result = "";// 6位验证码
			for (int i = 0; i < 6; i++) {
				result += random.nextInt(10);
			}
			pvc.sendVerificationCode(userMobile, result);
			System.out.println(result);
			RedisUtil.set(FkGouConfig.FKGOU_VERIFICODE_CLIENT_SESSION_ID + "_" + token, result, 1200);// 存放redis
			rr.setCode(1);
			rr.setDesc("发送成功！！");
		} catch (Exception e) {
			rr.setCode(2);
			rr.setDesc("错误结果:" + e.getMessage());
		}
		return rr;
	}
}