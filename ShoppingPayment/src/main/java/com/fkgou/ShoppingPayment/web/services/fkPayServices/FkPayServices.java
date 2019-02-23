package com.fkgou.ShoppingPayment.web.services.fkPayServices;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkgou.ShoppingPayment.web.dao.fkPayDao.FkPayDao;
import com.fkgou.ShoppingPayment.web.entity.PageInfo;
import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.entity.fkPayEntity.ComsumeRecordPara;
import com.fkgou.ShoppingPayment.web.entity.fkPayEntity.OrderSettlementPara;
import com.fkgou.ShoppingPayment.web.entity.fkPayEntity.RecentDealRecordPara;
import com.fkgou.ShoppingPayment.web.entity.fkPayEntity.RecordDetailsPara;
import com.fkgou.ShoppingPayment.web.entity.fkPayEntity.ShoppingDetailsPara;
import com.fkgou.ShoppingPayment.web.entity.fkPayEntity.ShowOrderDetails;
import com.fkgou.ShoppingPayment.web.entity.fkPayEntity.StatusAndTypePara;
import com.fkgou.ShoppingPayment.web.entity.userInfo.RpcUserInfo;
import com.fkgou.ShoppingPayment.web.entity.userInfo.ShopUserMessageResult;
import com.fkgou.ShoppingPayment.web.services.feginServices.MallsServices;
import com.fkgou.ShoppingPayment.web.services.feginServices.OrderServices;
import com.fkgou.ShoppingPayment.web.services.feginServices.UsermsServices;
import com.fkgou.ShoppingPayment.web.util.FkGouConfig;
import com.fkgou.ShoppingPayment.web.util.JsonUtil;
import com.fkgou.ShoppingPayment.web.util.RedisUtil;
import com.fkgou.ShoppingPayment.web.util.timeStampUtil;
import com.fkgou.userms.entity.UserInfo;

@Service
public class FkPayServices {

	@Autowired
	FkPayDao fkPayDao;

	@Autowired
	UsermsServices usermsServices;

	@Autowired
	OrderServices orderServices;

	@Autowired
	MallsServices mallsServices;

	final static Logger log = LoggerFactory.getLogger(FkPayServices.class);

	/**
	 * 商家冻结金额
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp businessFreezesFunds(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				Integer userId = ui.getUserId();
				BigDecimal moneyFrozen = fkPayDao.queryMoneyFrozen(userId);
				if (moneyFrozen != null) {
					rr.setCode(1);
					rr.setData(moneyFrozen);
				} else {
					rr.setCode(1);
					rr.setDesc("数据查询为空！");
				}
			} catch (Exception e) {
				log.info("错误结果：" + e.getMessage());
				rr.setCode(2);
				rr.setDesc("错误结果：" + e.getMessage());
			}
		} else {

		}
		return rr;
	}

	/**
	 * 最近交易记录查询
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp fkPayForRecentDealRecord(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		RecentDealRecordPara rdrp = new RecentDealRecordPara();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				Integer userId = ui.getUserId();
				List<Integer> tradeId = fkPayDao.queryTradeId();// 查询交易类型
				rdrp.setTradeTypeId(tradeId);
				Integer tradeTypeId = (Integer) map.get("tradeTypeId");
				List<ComsumeRecordPara> comsumeRecordParaList = fkPayDao.queryRecentDealRecord(userId, tradeTypeId);
				rdrp.setComsumeRecord(comsumeRecordParaList);
				if (tradeId.isEmpty() && comsumeRecordParaList.isEmpty()) {
					rr.setCode(2);
					rr.setDesc("查询数据为空！");
				} else {
					rr.setCode(1);
					rr.setData(rdrp);
				}
			} catch (Exception e) {
				log.info("错误结果：" + e.getMessage());
				rr.setCode(2);
				rr.setDesc("错误结果：" + e.getMessage());
			}
		} else {
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 蜂乐pay最近交易记录之订单详情
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp fkPayForRecentDealRecordDetails(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		ShowOrderDetails sd = new ShowOrderDetails();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				Integer userId = ui.getUserId();
				String orderId = (String) map.get("orderId");
				Map<String, Object> detailsMap = new HashMap<String, Object>();
				detailsMap.put("userId", userId);
				detailsMap.put("orderId", orderId);
				rr = orderServices.queryShoppingDetails(detailsMap);
				if (rr.getCode() != 1) {
					rr.setCode(2);
					rr.setDesc("未获取到信息，请稍后！");
				} else {
					List<ShoppingDetailsPara> detailsList = JsonUtil.string2Obj(rr.getData().toString(), List.class,
							ShoppingDetailsPara.class);
					for (int i = 0; i < detailsList.size(); i++) {
						List<RecordDetailsPara> recordDetailsParaList = fkPayDao.queryRecordDetails(userId, orderId);
						if (recordDetailsParaList.isEmpty()) {
							rr.setCode(2);
							rr.setDesc("查询数据为空！");
						} else {
							sd.setOrder_discount_fee(detailsList.get(i).getOrder_discount_fee());
							sd.setOrder_discount_type(detailsList.get(i).getOrder_discount_type());
							sd.setOrder_shipping_fee(detailsList.get(i).getOrder_shipping_fee());
							sd.setPay_code(detailsList.get(i).getPay_code());
							sd.setShop_name(detailsList.get(i).getShop_name());
							sd.setRecordDetailsPara(recordDetailsParaList);
						}
					}
					rr.setCode(1);
					rr.setData(sd);
				}
			} catch (Exception e) {
				log.info("错误结果：" + e.getMessage());
				rr.setCode(2);
				rr.setDesc("错误结果：" + e.getMessage());
			}
		} else {
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 蜂乐pay最近交易记录之删除订单
	 * 
	 * @param map
	 * @return
	 */
	@Transactional
	public ResultResp fkPayForRecentDealRecordDel(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				Integer userId = ui.getUserId();
				Integer recordId = (Integer) map.get("recordId");
				boolean isUpdateSuccess = fkPayDao.updateRecordDelete(userId, recordId);
				if (isUpdateSuccess) {
					rr.setCode(1);
					rr.setDesc("删除成功！");
				} else {
					rr.setCode(2);
					rr.setDesc("删除失败！");
				}
			} catch (Exception e) {
				log.info("错误结果：" + e.getMessage());
				rr.setCode(2);
				rr.setDesc("错误结果：" + e.getMessage());
			}
		} else {
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 类型与状态查询
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp fkPayForStatus() {
		ResultResp rr = new ResultResp();
		StatusAndTypePara ast = new StatusAndTypePara();
		List<Integer> tradeId = fkPayDao.queryTradeId();
		List<Integer> orderStatusId = fkPayDao.queryOrderStatus();
		if (tradeId.isEmpty() && orderStatusId.isEmpty()) {
			rr.setCode(2);
			rr.setDesc("查询数据为空！");
		} else {
			ast.setTradeId(tradeId);
			ast.setOrderStatusId(orderStatusId);
			rr.setCode(1);
			rr.setData(ast);
		}
		return rr;
	}

	/**
	 * 交易查询
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp fkPayForTradingQuery(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		PageInfo pi = new PageInfo();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				Integer userId = ui.getUserId();
				String startTime = (String) map.get("startTime");
				String endTime = (String) map.get("endTime");
				Integer trade_Type_Id = (Integer) map.get("tradeTypeId");
				String AllType = null;// 全部
				Integer tradeTypeId = null;
				if (trade_Type_Id == 0) {
					AllType = "' '";
				} else {
					tradeTypeId = trade_Type_Id;
				}
				Integer order_Status = (Integer) map.get("orderStatus");
				String AllStatus = null;// 全部
				Integer orderStatus = null;
				if (order_Status == 0) {
					AllStatus = "' '";
				} else {
					orderStatus = order_Status;
				}
				Integer currentPage = (Integer) map.get("currentPage");
				Integer pageSize = (Integer) map.get("pageSize");
				Integer cp = (currentPage - 1) * pageSize;
				List<ComsumeRecordPara> comsumeRecordParaList = fkPayDao.queryOrderRecord(userId, startTime, endTime,
						tradeTypeId, AllType, orderStatus, AllStatus, cp, pageSize);
				Integer total = fkPayDao.getRecordCount(userId, startTime, endTime, tradeTypeId, AllType, orderStatus,
						AllStatus);
				Integer totalPage = (total % pageSize) == 0 ? total / pageSize : total / pageSize + 1;
				if (comsumeRecordParaList.isEmpty()) {
					rr.setCode(2);
					rr.setDesc("查询数据为空！");
				} else {
					pi.setPageSize(pageSize);
					pi.setTotalPage(totalPage);
					pi.setTotalCount(total);
					pi.setCurrPage(currentPage);
					pi.setList(comsumeRecordParaList);
					rr.setCode(1);
					rr.setData(pi);
				}
			} catch (Exception e) {
				log.info("错误结果：" + e.getMessage());
				rr.setCode(2);
				rr.setDesc("错误结果：" + e.getMessage());
			}
		} else {
			log.info("token失效！");
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 确认转账方信息
	 * 
	 * @param payeeInfo
	 * @return
	 */
	public ResultResp fkPayForTransferCheckPayeeInfo(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				Integer userId = ui.getUserId();
				String payeeAccount = (String) map.get("payeeAccount");// 收款人账号
				RpcUserInfo rpcUserInfo = usermsServices.queryUserId(payeeAccount);
				if (rpcUserInfo == null) {
					rr.setCode(2);
					rr.setDesc("账号不存在，请输入正确账号或确认账号是否正确！");
				} else if (userId.equals(rpcUserInfo.getUserId())) {
					rr.setCode(2);
					rr.setDesc("请勿给本账号转账！");
				} else {
					rr.setCode(1);
					rr.setData(rpcUserInfo);
				}
			} catch (Exception e) {
				log.info("错误结果：" + e.getMessage());
				rr.setCode(2);
				rr.setDesc("错误结果：" + e.getMessage());
			}
		} else {
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 蜂乐pay转账
	 * 
	 * @param map
	 * @return
	 */
	@Transactional
	public ResultResp fkPayForTransfer(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				Integer fromUserId = ui.getUserId();// 发送人id
				String fromUserName = ui.getUserName();// 发起人姓名
				String payeeAccount = (String) map.get("payeeAccount");// 收款人账号
				RpcUserInfo rpcUserInfo = usermsServices.queryUserId(payeeAccount);
				String toUserId = rpcUserInfo.getUserId();// 接收人id
				String toUserName = rpcUserInfo.getUserName();// 接收人姓名
				String money = (String) map.get("money");// 转账金额
				String paymentExplain = (String) map.get("paymentExplain");// 付款说明
				String picValidation = (String) map.get("picValidation");// 图形验证码
				String phoneValidation = (String) map.get("phoneValidation");// 手机验证码
				String passwd = (String) map.get("passwd");// 支付密码
				String picCode = RedisUtil.get("verification_code_" + token);// 获取存储的图形验证码
				String phoneCode = RedisUtil.get(FkGouConfig.FKGOU_VERIFICODE_CLIENT_SESSION_ID + "_" + token);// 获取存储手机验证码
				if (picCode.equals(picValidation)) {
					if (phoneCode.equals(phoneValidation)) {
						Integer isExist = fkPayDao.isPasswd(fromUserId, passwd);// 验证支付密码
						if (isExist == 1) {
							String sendTime = timeStampUtil.getNowDate();
							Integer transferType = 1;
							String transactionNumber = "T" + timeStampUtil.getTimeStamp()
									+ timeStampUtil.randomLengthByTwo();// 转账订单号
							// 插值存库
							boolean isInsertSuccess = fkPayDao.insertTransfer(fromUserName, toUserName, sendTime, money,
									paymentExplain, transferType, transactionNumber);
							// 向record表插值
							String recordDate = timeStampUtil.getYear() + "-" + timeStampUtil.getMonth() + "-"
									+ timeStampUtil.getDay();
							String recordYear = timeStampUtil.getYear();
							String recordMonth = timeStampUtil.getMonth();
							String recordDay = timeStampUtil.getDay();
							String recordTitle = "转账";
							String recordDesc = "转账";
							Integer tradeTypeId = 2;// 交易类型
							Integer fromUserType = 2;// 2-付款方
							Integer toUserType = 1;// 1-收款方
							Integer recordStatus = 8;// 付款状态
							String recordPayorder = transactionNumber;// 实际支付单号
							String recordPaytime = timeStampUtil.getNowDate();
							Integer fromPlusOrMinus = 0;
							Integer toPlusOrMinus = 1;
							if (isInsertSuccess) {
								// 转账人记录
								boolean isFromRecord = fkPayDao.insertFromRecord(transactionNumber, fromUserId,
										fromUserName, money, recordDate, recordYear, recordMonth, recordDay,
										recordTitle, recordDesc, sendTime, tradeTypeId, fromUserType, recordStatus,
										recordPayorder, recordPaytime, fromPlusOrMinus);
								// 收款人
								boolean isToRecord = fkPayDao.insertToRecord(transactionNumber, toUserId, toUserName,
										money, recordDate, recordYear, recordMonth, recordDay, recordTitle, recordDesc,
										sendTime, tradeTypeId, toUserType, recordStatus, recordPayorder, recordPaytime,
										toPlusOrMinus);
								if (isFromRecord && isToRecord) {
									// 更新转账双方资产信息
									boolean isUpdateFrom = fkPayDao.updateFromMoney(fromUserId, money);
									boolean isUpdateTo = fkPayDao.updateToMoney(toUserId, money);
									if (isUpdateFrom && isUpdateTo) {
										// 更新各种状态
										fkPayDao.updateTransfer(transactionNumber);
										fkPayDao.updateFromRecord(fromUserId, transactionNumber);
										fkPayDao.updateToRecord(toUserId, transactionNumber);
										rr.setCode(1);
										rr.setDesc("转账成功！");
									} else {
										rr.setCode(2);
										rr.setDesc("资产更新失败！");
									}
								} else {
									rr.setCode(2);
									rr.setDesc("记录输入失败！");
								}
							} else {
								rr.setCode(2);
								rr.setDesc("转账失败！");
							}
						} else {
							rr.setCode(2);
							rr.setDesc("密码错误！");
						}
					} else {
						rr.setCode(2);
						rr.setDesc("验证码错误，请重新输入！");
					}
				} else {
					rr.setCode(2);
					rr.setDesc("验证错误，请重新输入！");
				}
			} catch (Exception e) {
				log.info("错误结果：" + e.getMessage());
				rr.setCode(2);
				rr.setDesc("错误结果：" + e.getMessage());
			}
		} else {
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 商家提现，获取商家结算单号
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp fkPayForBusinessWithdrawal(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		PageInfo pi = new PageInfo();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				// 获取商家店铺id
				Integer userId = ui.getUserId();
				Integer judge = ui.getSellerIsAdmin();// 判断是否是商家 1：是，2：不是
				Integer currentPage = (Integer) map.get("currentPage");
				Integer pageSize = (Integer) map.get("pageSize");
				Integer cp = (currentPage - 1) * pageSize;
				if (judge == 1) {
					List<OrderSettlementPara> settlementList = fkPayDao.querySettlementOrder(userId, cp, pageSize);
					Integer total = fkPayDao.getSettlementOrderCount(userId);
					Integer totalPage = (total % pageSize) == 0 ? total / pageSize : total / pageSize + 1;
					pi.setTotalCount(total);
					pi.setPageSize(pageSize);
					pi.setList(settlementList);
					pi.setTotalPage(totalPage);
					pi.setCurrPage(currentPage);
					rr.setCode(1);
					rr.setData(pi);
				} else {
					rr.setCode(1);
					rr.setDesc("该用户没有商家权限，请继续操作！");
				}
			} catch (Exception e) {
				rr.setCode(2);
				rr.setDesc("错误结果：" + e.getMessage());
			}
		} else {
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 蜂乐pay提现之计算结算金额
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp fkPayForBusinessWithdrawalMoney(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				Integer userId = ui.getUserId();
				@SuppressWarnings("unchecked")
				List<Integer> recordIdList = (List<Integer>) map.get("recordId");
				BigDecimal money = new BigDecimal(0);
				if (recordIdList.isEmpty()) {
					// 判断为空，处理所有金额
					BigDecimal totalMoney = fkPayDao.queryAllMoney(userId);
					rr.setCode(1);
					rr.setData(totalMoney);
				} else {
					for (int i = 0; i < recordIdList.size(); i++) {
						Integer recordId = recordIdList.get(i);
						BigDecimal recordMoney = fkPayDao.queryRecordMoney(recordId, userId);
						money = money.add(recordMoney);
					}
					rr.setCode(1);
					rr.setData(money);
				}
			} catch (Exception e) {
				rr.setCode(2);
				rr.setDesc("fkPayForBusinessWithdrawalMoneyErrorResults");
				log.info("fkPayForBusinessWithdrawalMoneyErrorResults:" + e.getMessage());
			}
		} else {
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 提现
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp fkPayForWithdrawal(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				Integer userId = ui.getUserId();// 发送人id
				String userName = ui.getUserName();
				Integer shopId = ui.getShopId();
				String payee = (String) map.get("payee");// 收款方
				String bankName = (String) map.get("bankName");// 银行名称
				String bankNum = (String) map.get("bankNum");// 银行卡号
				String money = (String) map.get("money");// 提现金额
				String withdrawalInstructions = (String) map.get("withdrawalInstructions");// 提现说明
				Integer type = (Integer) map.get("type");// 判断是否是全部提现
				String statementNo = null;// 结算单号
				if (type == 1) {
					statementNo = (String) map.get("statementNo");
				} else {
					statementNo = fkPayDao.queryAllStatementNo(userId);
				}
				String picValidation = (String) map.get("picValidation");// 图形验证码
				String phoneValidation = (String) map.get("phoneValidation");// 手机验证码
				String passwd = (String) map.get("passwd");// 支付密码
				String picCode = RedisUtil.get("verification_code_" + token);// 获取存储的图形验证码
				String phoneCode = RedisUtil.get(FkGouConfig.FKGOU_VERIFICODE_CLIENT_SESSION_ID + "_" + token);// 获取存储手机验证码
				Integer judge = ui.getSellerIsAdmin();// 判断是否是商家 1：是，2：不是
				if (judge == 1) {
					// 商家提现
					if (picValidation.equals(picCode)) {
						if (phoneValidation.equals(phoneCode)) {
							Integer isExist = fkPayDao.isPasswd(userId, passwd);// 验证支付密码
							if (isExist == 1) {
								String orderId = "W" + timeStampUtil.getTimeStamp() + timeStampUtil.randomLengthByTwo();// 提现订单号
								String createTime = timeStampUtil.getNowDate();
								ShopUserMessageResult shopUserMessageResult = mallsServices.queryUserId(shopId);
								String shopName = null;
								if (shopUserMessageResult != null) {
									shopName = shopUserMessageResult.getShopName();
								}
								boolean isInsertSuccess = fkPayDao.insertConsumeWithdrawBySeller(userId, userName,
										orderId, money, createTime, withdrawalInstructions, bankName, bankNum, payee,
										statementNo, shopName);
								if (isInsertSuccess) {
									rr.setCode(1);
									rr.setDesc("提现申请已提交，等待审核中！");
								} else {
									rr.setCode(2);
									rr.setDesc("提现申请提交失败！");
								}
							} else {
								rr.setCode(2);
								rr.setDesc("密码错误！");
							}
						} else {
							rr.setCode(2);
							rr.setDesc("手机验证码错误，请重新输入！");
						}
					} else {
						rr.setCode(2);
						rr.setDesc("图形验证码错误，请重新输入！");
					}
				} else {
					// 普通用户提现
					if (picValidation.equals(picCode)) {
						if (phoneValidation.equals(phoneCode)) {
							Integer isExist = fkPayDao.isPasswd(userId, passwd);// 验证支付密码
							if (isExist == 1) {
								BigDecimal accountOfBalance = fkPayDao.queryAccountOfBalance(userId);// 查询账户余额
								BigDecimal userMoney = new BigDecimal(money);// 类型转换，用于比较
								if (userMoney.compareTo(accountOfBalance) == 1) {
									rr.setCode(2);
									rr.setDesc("账户余额不足！");
								} else {
									String orderId = "W" + timeStampUtil.getTimeStamp()
											+ timeStampUtil.randomLengthByTwo();// 提现订单号
									String createTime = timeStampUtil.getNowDate();
									boolean isInsertSuccess = fkPayDao.insertConsumeWithdraw(userId, userName, orderId,
											money, createTime, withdrawalInstructions, bankName, bankNum, payee);
									if (isInsertSuccess) {
										rr.setCode(1);
										rr.setDesc("提现申请已提交，等待审核中！");
									} else {
										rr.setCode(2);
										rr.setDesc("提现申请提交失败！");
									}
								}
							} else {
								rr.setCode(2);
								rr.setDesc("密码错误！");
							}
						} else {
							rr.setCode(2);
							rr.setDesc("手机验证码错误，请重新输入！");
						}
					} else {
						rr.setCode(2);
						rr.setDesc("图形验证码错误，请重新输入！");
					}
				}
			} catch (Exception e) {
				log.info("错误结果：" + e.getMessage());
				rr.setCode(2);
				rr.setDesc("错误结果：" + e.getMessage());
			}
		} else {
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * app买家端提现
	 * 
	 * @param map
	 * @return
	 */
	@Transactional
	public ResultResp fkPayForWithdrawalByApp(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				Integer userId = ui.getUserId();// 发送人id
				String userName = ui.getUserName();
				String payee = (String) map.get("payee");// 收款方
				String bankName = (String) map.get("bankName");// 银行名称
				String bankNum = (String) map.get("bankNum");// 银行卡号
				String money = (String) map.get("money");// 提现金额
				String withdrawalInstructions = (String) map.get("withdrawalInstructions");// 提现说明
				String passwd = (String) map.get("passwd");// 支付密码
				// 用户提现，判断验证是否正确
				Integer isExist = fkPayDao.isPasswd(userId, passwd);// 验证支付密码
				if (isExist == 1) {
					BigDecimal accountOfBalance = fkPayDao.queryAccountOfBalance(userId);// 查询账户余额
					BigDecimal userMoney = new BigDecimal(money);// 类型转换，用于比较(提现金额)
					if (userMoney.compareTo(accountOfBalance) == 1) {
						rr.setCode(2);
						rr.setDesc("账户余额不足！");
					} else {
						String orderId = "W" + timeStampUtil.getTimeStamp() + timeStampUtil.randomLengthByTwo();// 提现订单号
						String createTime = timeStampUtil.getNowDate();
						boolean isInsertSuccess = fkPayDao.insertConsumeWithdraw(userId, userName, orderId, money,
								createTime, withdrawalInstructions, bankName, bankNum, payee);
						if (isInsertSuccess) {
							rr.setCode(1);
							rr.setDesc("提现申请已提交，等待审核中！");
						} else {
							rr.setCode(2);
							rr.setDesc("提现申请提交失败！");
						}
					}
				} else {
					rr.setCode(2);
					rr.setDesc("密码错误！");
				}
			} catch (Exception e) {
				rr.setCode(2);
				log.info("错误结果：" + e.getMessage());
			}
		} else {
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 商家提现
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp fkPayForMerchantsWithdrawal(Map<Object, Object> map) {
		ResultResp rr = new ResultResp();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				UserInfo ui = new UserInfo();
				String userinfo = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USER + token);// 获取user信息
				ui = mapper.readValue(userinfo, UserInfo.class);// String转object
				Integer userId = ui.getUserId();// 发送人id
				String userName = ui.getUserName();
				Integer shopId = ui.getShopId();
				String payee = (String) map.get("payee");// 收款方
				String bankName = (String) map.get("bankName");// 银行名称
				String bankNum = (String) map.get("bankNum");// 银行卡号
				String money = (String) map.get("money");// 提现金额
				Integer type = (Integer) map.get("type");// 判断是否是全部提现
				String statementNo = null;// 结算单号
				if (type == 1) {
					statementNo = (String) map.get("statementNo");
				} else {
					statementNo = fkPayDao.queryAllStatementNo(userId);
				}
				String withdrawalInstructions = (String) map.get("withdrawalInstructions");// 提现说明
				String phoneValidation = (String) map.get("phoneValidation");// 手机验证码
				String phoneCode = RedisUtil.get(FkGouConfig.FKGOU_VERIFICODE_CLIENT_SESSION_ID + "_" + token);// 获取存储手机验证码
				// 商家用户提现，判断验证是否正确
				if (phoneValidation.equals(phoneCode)) {
					String orderId = "W" + timeStampUtil.getTimeStamp() + timeStampUtil.randomLengthByTwo();// 提现订单号
					String createTime = timeStampUtil.getNowDate();
					ShopUserMessageResult shopUserMessageResult = mallsServices.queryUserId(shopId);
					String shopName = null;
					if (shopUserMessageResult != null) {
						shopName = shopUserMessageResult.getShopName();
					}
					boolean isInsertSuccess = fkPayDao.insertConsumeWithdrawBySeller(userId, userName, orderId, money,
							createTime, withdrawalInstructions, bankName, bankNum, payee, statementNo, shopName);
					if (isInsertSuccess) {
						rr.setCode(1);
						rr.setDesc("提现申请已提交，等待审核中！");
					} else {
						rr.setCode(2);
						rr.setDesc("提现申请提交失败！");
					}
				} else {
					rr.setCode(2);
					rr.setDesc("验证码错误！");
				}
			} catch (Exception e) {
				rr.setCode(2);
				log.info("错误结果：" + e.getMessage());
			}
		} else {
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}
}