package com.fkgou.ShoppingPayment.web.config;

public class ALiPayConfig {
	// 商户appid
	public static String APPID = "2015121700992149";
	// sellerId
	public static String sellerId = "2088412724244655";
	// 私钥 pkcs8格式的
	public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCmkd0vhuBLDS+GGIlywNWC3QVG1qvPoxr8u++B57f4SyzB3y7kSglV6A/VO3wO5pVvjg7Ul0ScuPvllUeC3CTXRDWQMqf0kq4JhCwmXcRpwR13BHohwutn4TM9eEbAAh7obLgZiK1mp2rIa/9RVJzToIRFttoh8Rib4M1bka1js0NvGDp+3kB4OCk8yQJEJ2fCMhMbTlMenANLQqGzxR07w1lFbzyrntPqZ4E/hVmHQgfPdhxn/EYr2yzR009hi2oJO+X06SOj7dmu/KQI8pYhW+ZVUCdUWnR5IR72fKpCvwib2ptImBnba3zTR+TEKtISFyXtwsoZoks4o3FZFj5BAgMBAAECggEAYH9aY2FA6VnfMVFft2Itj12Z4/sTl/vG86mwzFJAixCKAlrYoJqfo+wH2CFY8oz0/6aNwu1La3vjOpLr1czRbwBuA3bFRHQBbTPsG+Hwms+s0Tl/w7VXiVu2wqK+YhgYbdAmK2y6ACF3RFsEk8pv/E4eswCGiGlTN2LZtXXEvF+6LLn9Wxu5NYKbVaGV6IF/Fl7HDT7wv/yLYZna3xMomQE/Xm3Ee3eMPdsKuIalhswEapnb48vj474ImrgmcJMqn/LZyAxLi6C2HmblG9Ev+BGPfzY4DeO8Xe4eknXq7p00+XGaEPvEuCLeqHqCxsCPVm4M9gV4APkESH0Bwshb0QKBgQDm5jTlKgtaM1KllVAxK/ZBzeuhU4K4FZ9OSDQ7R2rgS5rh+MiOhZ2jTZjewQ3K4Q448AtUZvsfgs1nx6R6/Brzk8ggSjpkNnXyWh9o5LUbfJqAlxtBVkM0iVBoDUijG+kXNTaZgq02+LlCfeTx1ut6UShFoGBLA2Gt1GA2LVuzrQKBgQC4rWfF2JiIVcScZ6/CZsuuHfyFe4ItevGILcNNsQbTirqM/+XmgJltsSCz9eQl5EWgquczccOczvLBj+0Nt8AEupvfbmcCGZSrcS06zLRXPiDGnDEPXXDTI4x5mjkDz3uuZq8YYJ+C/Br/Tjz1d4ttQZTHEhRvoTLzzzTjQs0nZQKBgF5PCKvRUHG3JLqdEfQDkMGv6d4qzemCa179/td3hB+4wboqc0BDMu/QcfAJyihjY/u9j+MZbNiiBGOvF29kKV3zrnoB13e3BtXGGa4uPArOCg0bqvY01Z5Pt5fIVTLJ45qnEXYB+d3KGWEfKrPcpLGBFIHbz3jUGMF3TRTT0fDBAoGAAIqBg1LfH4cgzFSBb1BdyGTOGJYEC54QlxWLPO8H1hbR9RfK1KIL0QZz0hMmV/YTPiVCCCSNLihtDaQeTav8JAdn7X+lTZxFkzsBpupynM7RycMrYaAkdHnmL3UENb6l3hzu5mNiFEAo1BamlxGClw9aIDstVCZkv9IblY7ciwECgYEAyx7ScpCVYOU4CHvd1ZTr6LoFcJEc9ON3UGh++v1sO5RvlTr4Wc8DCSORUsMfCLax0ejaeRTOa1A8JrB4n17Jm+qZ6Ug3TsDDR+8JaqiOYkW6BFFyHYMOtxi+TU26+3a7N3UCEVl7QYmanASoMGqnFmGiAxbbUGktb+h+aGsQYF8=";
	// 服务器异步通知页面路径
	// 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问(购物回调)
	public static String shopping_notify_url = "http://118.190.144.198:80/ShoppingPayment/ALiPay/alipayNotifyUrl";
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	// 商户可以自定义同步跳转地址(购物回调)118.190.151.246:8080/ShoppingPayment
	public static String shopping_return_url = "http://118.190.144.198:80/ShoppingPayment/ALiPay/alipayReturnUrl";
	// pc购物回调
	public static String shopping_return_url_pc = "http://118.190.144.198:80/ShoppingPayment/ALiPay/alipayReturnUrlByPc";
	// 服务器异步通知页面路径
	// 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问(充值回调)
	public static String recharge_notify_url = "http://118.190.144.198:80/ShoppingPayment/ALiPay/alipayRechargeNotifyUrl";
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	// 商户可以自定义同步跳转地址(充值回调)
	public static String recharge_return_url = "http://118.190.144.198:80/ShoppingPayment/ALiPay/alipayRechargeReturnUrl";
	// pc充值回调
	public static String recharge_return_url_pc = "http://118.190.144.198:80/ShoppingPayment/ALiPay/alipayRechargeReturnUrlByPc";
	// 请求网关地址//https://openapi.alipaydev.com/gateway.do
	public static String URL = "https://openapi.alipay.com/gateway.do";
	// 编码
	public static String CHARSET = "UTF-8";
	// 返回格式
	public static String FORMAT = "json";
	// 支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkj6dw+cvChJS0IDyAStTDatNB30MNEEN16lNaxNVrCXCG9AblsRJn8MPeV3KBXXm4C//jIStRheXi9YLPX8tlT/T8a0G009rllNRKMwQ18Kwbyrduxt+L295gCKgM1Fh9gnONFUBzDQ3dejyJOcL5GWf46km1Vx2crtYsa4VBtFJ5RztWR0gdPoswQyBmBOvmW2sm1dbnB4Nh/2aig8KX73AX0Id/3g5mD8jHTWTG6MaNYNFwGhdPHPtze9XSrXOcAHHYnvixf5upvBJIhPEFA2W626Thivx15GC03SmtlU+SX8zBArIMt01uf5c6fWtGqoa4itqJgDX1cFFECQPfwIDAQAB";
	// 日志记录目录
	public static String log_path = "/log";
	// RSA2
	public static String SIGNTYPE = "RSA2";
}
