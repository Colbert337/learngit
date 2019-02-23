package com.fkgou.ShoppingPayment.web.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	private static ObjectMapper objectMapper = new ObjectMapper();

	@SuppressWarnings("rawtypes")
	public static List<Object> parseJson(String str) {

		Map maps = (Map) JSON.parse(str);
		List<Object> list = new ArrayList<Object>();
		for (Object map : maps.entrySet()) {
			Object value = ((Map.Entry) map).getValue();
			list.add(value);
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public static List<Object> parseJsonDouble(String str) {
		Map maps = (Map) JSON.parse(str);
		List<Object> list = new ArrayList<Object>();
		for (Object map : maps.entrySet()) {
			Object value = ((Map.Entry) map).getValue();
			Map maps2 = (Map) JSON.parse(JsonUtil.toString(value));
			for (Object map2 : maps2.entrySet()) {
				String value2 = (String) ((Map.Entry) map2).getValue();
				list.add(value2);
			}
		}
		return list;
	}

	/**
	 * Object转json字符串
	 * 
	 * @param obj
	 * @param <T>
	 * @return
	 */
	public static <T> String toString(T obj) {
		if (obj == null) {
			return null;
		}
		try {
			return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			System.out.println("Parse object to String error");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 数组的json对象转化为数组
	 * 
	 * @param str
	 * @return
	 */
	public static List<Integer> StringToList(String str) {

		String[] split = str.substring(1, str.length() - 1).split(",");
		List<Integer> list = new ArrayList<>();
		for (String string : split) {
			list.add(Integer.parseInt(string));
		}
		return list;
	}

	public static <T> T toBean(String str, Class<T> cls) {
		if (null == str) {
			return null;
		}
		try {
			return objectMapper.readValue(str, cls);
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * string转object
	 * 
	 * @param str
	 *            json字符串
	 * @param clazz
	 *            被转对象class
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T string2Obj(String str, Class<T> clazz) {
		if (StringUtils.isEmpty(str) || clazz == null) {
			return null;
		}
		try {
			return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
		} catch (IOException e) {
			System.out.println("Parse String to Object error");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * string转object 用于转为集合对象
	 * 
	 * @param str
	 *            json字符串
	 * @param collectionClass
	 *            被转集合class
	 * @param elementClasses
	 *            被转集合中对象类型class
	 * @param <T>
	 * @return
	 */
	public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses) {
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
		try {
			return objectMapper.readValue(str, javaType);
		} catch (IOException e) {
			System.out.println("Parse String to Object error");
			e.printStackTrace();
			return null;
		}
	}
}
