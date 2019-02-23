package com.fkgou.ShoppingPayment.web.services.wabServicesImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fkgou.ShoppingPayment.web.dao.wabDao.WalletBalanceDisplayDAO;
import com.fkgou.ShoppingPayment.web.entity.AccountBalanceDisplay;
import com.fkgou.ShoppingPayment.web.entity.PageInfo;
import com.fkgou.ShoppingPayment.web.entity.ResultResp;
import com.fkgou.ShoppingPayment.web.entity.SimplifyPayConsumeTrade;
import com.fkgou.ShoppingPayment.web.util.FkGouConfig;
import com.fkgou.ShoppingPayment.web.util.RedisUtil;

@Service
public class WalletBalanceDisplayServicesImpl {

	@Autowired
	WalletBalanceDisplayDAO walletBalanceDisplayDAO;

	/**
	 * 查询账户余额
	 * 
	 * @param userId
	 * @return
	 */
	public AccountBalanceDisplay queryAccountBalance(String userId) {
		return walletBalanceDisplayDAO.queryAccountBalance(userId);
	}

	/**
	 * 最近交易信息
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp queryRecentOrder(Map<String, Object> map) {
		ResultResp rr = new ResultResp();
		PageInfo pi = new PageInfo();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			String userId = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USERID + token);
			Integer currentPage = Integer.parseInt(map.get("currentPage") + "");
			Integer pageSize = Integer.parseInt(map.get("pageSize") + "");
			int cp = (currentPage - 1) * pageSize;
			try {
				List<SimplifyPayConsumeTrade> spct = walletBalanceDisplayDAO.queryRecentOrder(userId, cp, pageSize);
				int total = walletBalanceDisplayDAO.getShipmentsOrderConut(userId);
				int totalPage = (total % pageSize) == 0 ? total / pageSize : total / pageSize + 1;
				if (spct != null) {
					for (int i = 0; i < spct.size(); i++) {
						BigDecimal recordMoney = spct.get(i).getRecord_money().setScale(2, BigDecimal.ROUND_HALF_UP);
						String money = recordMoney.toString();
						spct.get(i).setMoney(money);
					}
					pi.setCurrPage(currentPage);
					pi.setPageSize(pageSize);
					pi.setTotalCount(total);
					pi.setTotalPage(totalPage);
					pi.setList(spct);
					rr.setCode(1);
					rr.setData(pi);
				} else {
					rr.setCode(2);
					rr.setDesc("订单为空！");
				}
			} catch (Exception e) {
				rr.setDesc(e.getMessage());
			}
		} else {
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}

	/**
	 * 卖家交易信息
	 * 
	 * @param map
	 * @return
	 */
	public ResultResp SellerRecentTransactionOrder(Map<String, Object> map) {
		ResultResp rr = new ResultResp();
		PageInfo pi = new PageInfo();
		String token = (String) map.get("token");
		boolean isExistToken = RedisUtil.exists(FkGouConfig.FKGOU_USER_TOKEN_USER + token);
		if (isExistToken) {
			String userId = RedisUtil.get(FkGouConfig.FKGOU_USER_TOKEN_USERID + token);
			Integer currentPage = Integer.parseInt(map.get("currentPage") + "");
			Integer pageSize = Integer.parseInt(map.get("pageSize") + "");
			Integer cp = (currentPage - 1) * pageSize;
			try {
				List<SimplifyPayConsumeTrade> spct = walletBalanceDisplayDAO.queryBusinessRecentOrder(userId, cp, pageSize);
				Integer total = walletBalanceDisplayDAO.getShipmentsOrderConut(userId);
				Integer totalPage = (total % pageSize) == 0 ? total / pageSize : total / pageSize + 1;
				if (spct != null) {
					for (int i = 0; i < spct.size(); i++) {
						BigDecimal recordMoney = spct.get(i).getRecord_money().setScale(2, BigDecimal.ROUND_HALF_UP);
						String money = recordMoney.toString();
						spct.get(i).setMoney(money);
					}
					pi.setCurrPage(currentPage);
					pi.setPageSize(pageSize);
					pi.setTotalCount(total);
					pi.setTotalPage(totalPage);
					pi.setList(spct);
					rr.setCode(1);
					rr.setData(pi);
				} else {
					rr.setCode(2);
					rr.setDesc("订单为空！");
				}
			} catch (Exception e) {
				rr.setDesc(e.getMessage());
			}
		} else {
			rr.setCode(10086);
			rr.setDesc("token失效，请重新获取！");
		}
		return rr;
	}
}
