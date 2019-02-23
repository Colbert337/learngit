package com.fkgou.ShoppingPayment.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 随机数与时间戳
 * 
 * @author Poi
 *
 */
public class timeStampUtil {

	/**
	 * 生成单号
	 * 
	 * @return
	 */
	public static String getTimeStamp() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		int random = (int) (Math.random() * 9000 + 1000);
		String timestamp = dateString + random;
		return timestamp;

	}

	/**
	 * 设置微信二维码失效时间，并返回具体失效的时间点
	 * 
	 * @param expire
	 *            二维码的有效时间，单位是毫秒
	 * @return
	 */
	public static String getOrderExpireTime(Long expire) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date now = new Date();
		Date afterDate = new Date(now.getTime() + expire);
		return sdf.format(afterDate);
	}

	/**
	 * 生成支付宝单号
	 * 
	 * @return
	 */
	public static String getALiTimeStamp() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		int random = (int) (Math.random() * 900 + 100);
		String timestamp = dateString + random;
		return timestamp;

	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取年份
	 * 
	 * @return
	 */
	public static String getYear() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String year = formatter.format(currentTime);
		return year;

	}

	/**
	 * 获取月份
	 * 
	 * @return
	 */
	public static String getMonth() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		String month = formatter.format(currentTime);
		return month;

	}

	/**
	 * 获取日期
	 * 
	 * @return
	 */
	public static String getDay() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd");
		String day = formatter.format(currentTime);
		return day;

	}

	/**
	 * 获取当前时间时间戳
	 * 
	 * @return
	 */
	public static Integer timeToStamp() {
		int seconds = (int) (System.currentTimeMillis() / 1000);
		return seconds;
	}

	/**
	 * 获取当前时间时间戳（后九位）
	 * 
	 * @return
	 */
	public static String nowtime() {
		int seconds = (int) (System.currentTimeMillis() / 1000);
		String str = String.valueOf(seconds);
		String time = str.substring(1, 10);
		return time;
	}

	/**
	 * 随机获取两位数
	 * 
	 * @return
	 */
	public static int randomLengthByTwo() {
		int random = (int) (Math.random() * 90 + 10);
		return random;
	}

	/**
	 * 将时间转换为时间戳
	 */
	public static String dateToStamp(String s) throws ParseException {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;

	}

	/**
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}
}
