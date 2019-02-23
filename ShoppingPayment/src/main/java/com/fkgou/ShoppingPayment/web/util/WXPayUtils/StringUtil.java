package com.fkgou.ShoppingPayment.web.util.WXPayUtils;

import java.util.Map;

public class StringUtil {
	/**
	 * 
	 * @param param
	 * @return
	 */
	public static String GetMapToXML(Map<String, String> param) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		for (Map.Entry<String, String> entry : param.entrySet()) {
			sb.append("<" + entry.getKey() + ">");
			sb.append(entry.getValue());
			sb.append("</" + entry.getKey() + ">");
		}
		sb.append("</xml>");
		return sb.toString();
	}
}
