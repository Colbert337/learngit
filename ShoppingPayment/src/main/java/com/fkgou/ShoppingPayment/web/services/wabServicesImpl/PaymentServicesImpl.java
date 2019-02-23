package com.fkgou.ShoppingPayment.web.services.wabServicesImpl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkgou.ShoppingPayment.web.dao.wabDao.PaymentDAO;
import com.fkgou.ShoppingPayment.web.entity.QueryGoodsPara;
import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.util.FkGouConfig;
import com.fkgou.ShoppingPayment.web.util.RedisUtil;
import com.fkgou.userms.entity.UserInfo;

@Service
public class PaymentServicesImpl {

	@Autowired
	private PaymentDAO paymentDAO;

	/**
	 * 验证密码是否存在
	 * 
	 * @param userId
	 * @param passwd
	 * @return
	 */
	public String isExistPasswd(String userId) {
		return paymentDAO.isExistPasswd(userId);
	}

	/**
	 * 设置支付密码与找回
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp setPaymentPassword(Map<String, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
		boolean isExist = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExist) {
			Integer verify = Integer.parseInt(map.get("type") + "");// 获取类型
			String verifyCode = (String) map.get("verifyCode");// 获取验证码
			// verify:1 设置支付密码 2：找回支付密码
			Integer userId = null;
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				userId = ui.getUserId();
			} catch (Exception e) {
				rr.setCode(2);
				rr.setDesc(e.getMessage());
			}
			String passwd = (String) map.get("passwd");
			String code = RedisUtil.get(FkGouConfig.FKGOU_VERIFICODE_CLIENT_SESSION_ID + "_" + token);
			if (verify == 1) {
				if (verifyCode.equals(code)) {
					boolean isChangePasswd = paymentDAO.modifyPasswd(userId, passwd);
					if (isChangePasswd) {
						rr.setCode(1);
						rr.setDesc("设置成功！");
					} else {
						rr.setCode(2);
						rr.setDesc("设置失败！");
					}
				} else {
					rr.setCode(2);
					rr.setDesc("验证码错误！");
				}
			} else if (verify == 2) {
				if (verifyCode.equals(code)) {
					boolean isChangePasswd = paymentDAO.modifyPasswd(userId, passwd);
					if (isChangePasswd) {
						rr.setCode(1);
						rr.setDesc("设置成功！");
					} else {
						rr.setCode(2);
						rr.setDesc("设置失败！");
					}
				} else {
					rr.setCode(2);
					rr.setDesc("验证码错误！");
				}
			}
		} else {
			rr.setCode(2);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 验证支付密码是否正确
	 * 
	 * @param userId
	 * @param passwd
	 * @return
	 */
	public Integer isPasswd(Integer userId, String passwd) {
		return paymentDAO.isPasswd(userId, passwd);
	}

	/**
	 * 修改支付密码
	 * 
	 * @param map
	 * @return
	 */
	@Transactional
	public ResultResp changePaymentPasswd(Map<String, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
		boolean isExist = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExist) {
			String userId = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USERID + token);
			String oldPasswd = (String) map.get("oldPasswd");
			String newPasswd = (String) map.get("newPasswd");
			boolean verifyOldPasswd = paymentDAO.verifyOldPasswd(userId, oldPasswd);
			if (verifyOldPasswd) {
				boolean isSuccess = paymentDAO.changePaymentPasswd(userId, newPasswd);
				if (isSuccess) {
					rr.setCode(1);
					rr.setDesc("更改成功！");
				} else {
					rr.setCode(2);
					rr.setDesc("更改失败！");
				}
			} else {
				rr.setCode(2);
				rr.setDesc("旧密码错误！");
			}
		} else {
			rr.setCode(2);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 去除购物金额
	 * 
	 * @param userId
	 * @param amountactuallypaid
	 * @return
	 */
	@Transactional
	public boolean updateMoney(Integer userId, BigDecimal amountActuallyPaids) {
		return paymentDAO.updateMoney(userId, amountActuallyPaids);
	}

	/**
	 * 查询用户余额
	 * 
	 * @param userId
	 * @return
	 */
	public BigDecimal queryBalanceMoney(Integer userId) {
		return paymentDAO.queryBalanceMoney(userId);
	}

	/**
	 * 查询购物卡余额
	 * 
	 * @param userId
	 * @return
	 */
	public BigDecimal queryGiftCardMoney(Integer userId) {
		return paymentDAO.queryGiftCardMoney(userId);
	}

	/**
	 * 扣除购物卡金额
	 * 
	 * @param userId
	 * @param amountActuallyPaid
	 * @return
	 */
	public boolean updateGiftCardMoney(Integer userId, BigDecimal amountActuallyPaids) {
		return paymentDAO.updateGiftCardMoney(userId, amountActuallyPaids);
	}

	/**
	 * trade更新
	 * 
	 * @param orderId
	 * @param paymentTime
	 * @param userId
	 */
	public boolean updateTradeStatus(String orderId, String paymentTime, String finishTime, Integer userId) {
		return paymentDAO.updateTradeStatus(orderId, paymentTime, finishTime, userId);
	}

	/**
	 * record更新
	 * 
	 * @param orderId
	 * @param paymentTime
	 */
	public boolean updateRecord(String orderId, String paymentTime) {
		return paymentDAO.updateRecord(orderId, paymentTime);
	}

	/**
	 * record卖家更新
	 * 
	 * @param orderId
	 * @param paymentTime
	 */
	public boolean updateRecordBySeller(String orderId, String paymentTime) {
		return paymentDAO.updateRecordBySeller(orderId, paymentTime);
	}

	/**
	 * 查询订单金额及订单名称
	 * 
	 * @param orderId
	 * @return
	 */
	public QueryGoodsPara queryGoodsPriceAndName(String orderId, Integer userId) {
		return paymentDAO.queryGoodsPriceAndName(orderId, userId);
	}
}
