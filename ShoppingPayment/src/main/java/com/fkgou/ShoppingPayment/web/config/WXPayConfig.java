package com.fkgou.ShoppingPayment.web.config;

public class WXPayConfig {

	// 初始化 wap支付
	// public final static String WAP_APP_ID = "wx9b54e512c75d1f0e"; //
	// 公众账号appid（改为自己实际的）
	// public final static String WAP_APP_SECRET =
	// "e44f1eb4ee349de2f85db5ee98b804d3";
	// public final static String WAP_MCH_ID = "1504283971"; // 商户号（改为自己实际的）
	// public final static String WAP_API_KEY =
	// "b20fec2cec06c87413b8554fdbe03dba";
	// （改为自己实际的）key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置

	public final static String WAP_APP_ID = "wxe5f208761db70502"; // 公众账号appid（改为自己实际的）
	public final static String WAP_APP_SECRET = "bc48a3938bc92ddcdaa118765e74bc43";
	public final static String WAP_MCH_ID = "10025054"; // 商户号（改为自己实际的）
	public final static String WAP_API_KEY = "OBc0Zl0U20NIiSuZnBMyNOhlCrVKbPoU";

	// public final static String APP_ID = "wx3262122eca7d3007";
	// public final static String APP_SECRET =
	// "5294ab48538c90e92e7d6ff4d7c54f27";
	// public final static String MCH_ID = "1502392561";
	// public final static String API_KEY = "FAFHQiSFkDkGHBIVC1wzR6BfX2YrNzrj";

	public final static String APP_ID = "wxa23833e56f7897ec"; // 公众账号appid（改为自己实际的）
	public final static String APP_SECRET = "a017d76eb50d33dae339ae7b477dc2d5";
	public final static String MCH_ID = "1498662282"; // 商户号（改为自己实际的）
	public final static String API_KEY = "2PX60JtOHQ1JeQpHpKxMIG9vy0SoJ1DV";

	// 有关url
	public final static String UFDODER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	// http://pay.ahfkg.cn/fkgoupay/WXPay/weixinNotify.do
	public final static String NOTIFY_URL = "http://118.190.144.198:80/ShoppingPayment/WXPay/notifyWeiXinPay"; // 微信支付回调接口，就是微信那边收到（改为自己实际的）
	// wap回调
	public final static String WAP_NOTIFY_URL = "http://118.190.144.198:80/ShoppingPayment/WXPay/notifyWeiXinPayByWAP"; // 微信支付回调接口，就是微信那边收到（改为自己实际的）

	public final static String NOTIFY_URL_By_Recharge = "http://118.190.144.198:80/ShoppingPayment/WXPay/notifyWXPayToRecharge";

	public final static String NOTIFY_URL_By_WAPRecharge = "http://118.190.144.198:80/ShoppingPayment/WXPay/notifyWXPayToRechargeByWap";
	// 企业向个人账号付款的URL
	public final static String SEND_EED_PACK_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

}