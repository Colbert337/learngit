package com.fkgou.ShoppingPayment.web.util;

import java.io.UnsupportedEncodingException;

public class Url {

	private final static String ENCODE = "GBK";

	/**
	 * URL 解码
	 *
	 * @return String
	 * @author lifq
	 * @date 2015-3-17 下午04:09:51
	 */
	public static String getURLDecoderString(String str) {
		String result = "";
		if (null == str) {
			return "";
		}
		try {
			result = java.net.URLDecoder.decode(str, ENCODE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * URL 转码
	 *
	 * @return String
	 * @author lifq
	 * @date 2015-3-17 下午04:10:28
	 */
	public static String getURLEncoderString(String str) {
		String result = "";
		if (null == str) {
			return "";
		}
		try {
			result = java.net.URLEncoder.encode(str, ENCODE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @return void
	 * @author lifq
	 * @date 2015-3-17 下午04:09:16
	 */
//	public static void main(String[] args) {
//		String str = "notify_url=http%3A%2F%2Ftest1.fkgou.com%3A8080%2FShoppingPayment%2FALiPay%2FalipayNotifyUrl";
//		System.out.println(getURLEncoderString(str));
//		System.out.println(getURLDecoderString(str));
//
//	}

}
