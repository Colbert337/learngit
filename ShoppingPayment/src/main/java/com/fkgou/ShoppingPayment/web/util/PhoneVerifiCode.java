package com.fkgou.ShoppingPayment.web.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class PhoneVerifiCode {
	String appid = "EUCP-EMY-SMS1-C8IYS";// 请联系销售，或者在页面中 获取
	// 时间戳
	String timestamp = PhoneVerifiCode.toString(new Date(), "yyyyMMddHHmmss");
	// 密钥
	String secretKey = "41E696DF11BDB95B";// 请联系销售，或者在页面中 获取
	// 接口地址
	String host = "http://shmtn.b2m.cn:80/simpleinter/sendSMS?";// 请联系销售获取
	// 签名
	String sign = PhoneVerifiCode.md5((appid + secretKey + timestamp).getBytes());

	// String mobiles = "18715120649";

	String content = "【蜂狂购】您的验证码是：";
	// 扩展码(选填)最长支持12位，如果最终号码长度超长，会截取扩展码，请根据我司建议位数提交
	String extendedCode = "123";
	// 定时发送时间(选填，定时时间在90天之内)格式：yyyy-MM-dd HH:mm:ss、如果不填，则为即时发送
	String timerTime = "";
	// 自定义消息ID(选填)最长32位
	String customSmsId = "10001";

	// String
	// urlString="http://bjmtn.b2m.cn/simpleinter/sendSMS?"+"appId="+appid+"&timestamp="+timestamp+"&sign="+sign
	// +"&mobiles="+mobiles+"&content=【蜂狂购】"+"XXXXX"+"&extendedCode=123"+"&customSmsId=10001";
	/**
	 * 发送content内容，需要当前时间，所有不能做成static
	 * 
	 * @param content
	 * @return
	 */
	public Integer sendVerificationCode(String user_Mobile, String content) {
		HttpClient httpclient = HttpClientBuilder.create().build();
		String urlString = "http://bjmtn.b2m.cn/simpleinter/sendSMS?" + "appId=" + appid + "&timestamp=" + timestamp
				+ "&sign=" + sign + "&mobiles=" + user_Mobile + "&content=【蜂狂购】验证码：" + content
				+ "，请输入后进行验证，请不要将验证码泄露给他人，谢谢！(验证码有效期10分钟)" + "&extendedCode=8" + "&customSmsId=10001";
		HttpGet httpGet = new HttpGet(urlString);
		HttpResponse httpResponse;
		try {
			httpResponse = httpclient.execute(httpGet);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			return statusCode;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 100;
	}

	public static String toString(Date date, String format) {
		String dateStr = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			dateStr = sdf.format(date);
		} catch (Exception e) {
		}
		return dateStr;
	}

	// 随机验证码
	public static int randomCode() {
		int random = (int) (Math.random() * 9000 + 1000);
		return random;
	}

	public static String md5(byte[] bytes) {
		if (bytes == null || bytes.length == 0)
			return null;
		String s = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bytes);
			byte tmp[] = md.digest();
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return s;
	}
}
