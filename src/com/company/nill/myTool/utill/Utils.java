package com.company.nill.myTool.utill;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

public class Utils {
	public static String LPad(String str, Integer length, char car) {
		// 같을 경우 반환
		if(str.length()==length) {
			return str;
		}
		// 클경우  substirn
		if(str.length()>length) {
			return str.substring(0, length);
		}
		return str + String.format("%" + (length - str.length()) + "s" , "").replace(" ", String.valueOf(car));
	}

	public static String RPad(String str, Integer length, char car) {
		// 같을 경우 반환
		if(str.length()==length) {
			return str;
		}
		// 클경우  substirn
		if(str.length()>length) {
			return str.substring(0, length);
		}
		return String.format("%" + (length - str.length()) + "s" , "").replace(" ", String.valueOf(car)) + str;
	}

	public static String getTxId() throws UnknownHostException {
		String hostName = InetAddress.getLocalHost().getHostName();
		if(hostName.length() > 18) {
			hostName = hostName.substring(0, 18);
		}
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return LPad(hostName, 18, 'X') + uuid;
	}

	/**
	 * 특정 키로 중복제거
	 *
	 * @param keyExtractor
	 * @param <T>
	 * @return
	 */
	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new HashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}



	public static void main(String[] args) {

		try {
			System.out.println(Utils.getTxId().length());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

