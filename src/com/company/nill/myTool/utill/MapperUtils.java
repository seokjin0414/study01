package com.company.nill.myTool.utill;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class MapperUtils {

	private static Random random = new Random();

	private MapperUtils(){
		throw new IllegalStateException("MapperUtils class");
	}

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	static {
		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		OBJECT_MAPPER.registerModule(new JavaTimeModule());
	}

	public static Random getRandom() {
		return random;
	}

	/**
	 * 모델의 필드를 추출하여 Set<String>으로 리턴한다.
	 * @param classType : 필드 추출할 모델클래스
	 * @return : 필드셋
	 */
	public static Set<String> getModelFieldNames(Class<?> classType){

		Set<String> fields = new HashSet<>();
		Arrays.stream(classType.getDeclaredFields()).forEach(field -> fields.add(field.getName()));

		//log.info("### fields = {}", fields);
		return fields;
	}

	/**
	 * 오브젝트를 json 문자열로 리턴한다.
	 * @param o : 오브젝트
	 * @return : json 문자열
	 */
	public static String toJson(Object o) {
		String json = null;
		try {
			json = OBJECT_MAPPER.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			//log.warn(e.getMessage());
		}
		return json;
	}

	/**
	 * json 문자열을 T 오브젝트로 리턴한다.
	 * @param json : json 문자열
	 * @param type : T 클래스
	 * @return : T 오브젝트
	 */
	public static <T> T fromJson(String json, Class<T> type) {
		if(json == null || type == null) {
			return null;
		}
		T result = null;
		try {
			result = OBJECT_MAPPER.readValue(json, type);
		} catch (IOException e) {
			//log.warn(e.getMessage());
		}
		return result;
	}

	/**
	 * json 문자열을 T 오브젝트로 리턴한다.
	 * @param json : json 문자열
	 * @param type : T 타입레퍼런스(Generic class 용도)
	 * @return : T 오브젝트
	 */
	public static <T> T fromJson(String json, TypeReference<T> type) {
		if(json == null || type == null) {
			return null;
		}
		T result = null;
		try {
			result = OBJECT_MAPPER.readValue(json, type);
		} catch (IOException e) {
			//log.warn(e.getMessage());
		}
		return result;
	}

	/**
	 * 문자열을 URL 인코딩된 문자열로 리턴한다.
	 * @param plainText : 문자열데이터
	 * @return : URL 인코딩된 문자열
	 */
	public static String urlEncode(String plainText) {
		String encodedText = null;
		try {
			encodedText = URLEncoder.encode(plainText, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			//log.error(e.getMessage());
		}
		return encodedText;
	}

	/**
	 * URL 인코딩된 문자열을 디코딩된 문자열로 리턴한다.
	 * @param encodedText : URL 인코딩된 문자열
	 * @return : 디코딩된 문자열
	 */
	public static String urlDecode(String encodedText) {
		String decodedText = null;
		try {
			decodedText = URLDecoder.decode(encodedText, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			//log.error(e.getMessage());
		}
		return decodedText;
	}


	/**
	 * 추천인코드 생성
	 * @return
	 */
	public static String getReferralCode(Long userIdx) {
		StringBuffer referralCode = new StringBuffer();
		referralCode
				.append(getRandomStr(1))
				.append("0")
				.append(userIdx);
		return referralCode.toString();
	}

	/**
	 * 랜덤 문자열 생성
	 * @return
	 */
	public static String getRandomStr(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			String randomStr = String.valueOf((char)(random.nextInt(26) + 65));
			sb.append(randomStr);
		}
		return sb.toString();
	}

	/**
	 * 인증코드 생성
	 * @return
	 */
	public static String generateAuthCode() {
		return generateAuthCode(6);
	}
	public static String generateAuthCode(int length) {
		// 기본 자리수는 6자리
		int bound = 1;
		int minBound = 1;
		for (int i = 0; i < length; i++) {
			bound *= 10;
			if (i != length - 1) {
				minBound *= 10;
			}
		}
		int result = random.nextInt(bound);
		// 최소 자리수 보정
		if(result < minBound) {
			result += minBound;
		}
		return String.valueOf(result);
	}


	/**
	 * 데이터 없거나 빈문자열일 경우 널 처리, 필수값이 아닌 항목 저장 시 활용
	 * @return String
	 */
	public static String getNullIfEmpty(String content) {
		String result = getNvlIfEmpty(content);
		if(StringUtils.isEmpty(result)) {
			result = null;
		}
		return result;
	}

	/**
	 * 데이터 없거나 공백만 있는 경우 빈문자열 처리
	 * @return String
	 */
	public static String getNvlIfEmpty(String content) {
		String result = "";
		if(!StringUtils.isEmpty(content)) {
			result = content.trim();
		}
		return result;
	}

	/**
	 * 날짜 문자열을 ZonedDateTime 으로 변환(시간단위 포멧까지 포함해야 동작)
	 * @return ZonedDateTime
	 */
	public static ZonedDateTime convertDateToZonedDateTime(String date, String pattern) {
		if(StringUtils.isEmpty(date)) {
			return null;
		}
		DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().appendPattern(pattern).toFormatter().withZone(ZoneId.systemDefault());
		return ZonedDateTime.parse(date, dateTimeFormatter);
	}

	/**
	 * TimeMilliseconds 를 ZonedDateTime 으로 변환
	 * @return ZonedDateTime
	 */
	public static ZonedDateTime convertLongToZonedDateTime(Long timeMilliseconds) {
		return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timeMilliseconds), ZoneId.systemDefault());
	}

	/**
	 * 접속 IP 추출
	 * @return IP
	 */
	public static String getRemoteIP(HttpServletRequest request) {
		String ip = request.getHeader("X-FORWARDED-FOR");

		//proxy 환경일 경우
		if (ip == null || ip.length() == 0) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		//웹로직 서버일 경우
		if (ip == null || ip.length() == 0) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0) {
			ip = request.getRemoteAddr() ;
		}

		return ip;
	}

	/**
	 * 패스워드 패턴 검증
	 * @param pwd
	 * @return
	 */
	public static boolean validatPwd(String pwd) {

		//정규식 (영문, 숫자, 6~15\ 자리)
		String pwPattern = "^(?=.*\\d)(?=.*[a-z]).{6,15}$";
		Matcher matcher = Pattern.compile(pwPattern).matcher(pwd);

		//정규식 (같은 문자 4개 이상 사용 불가)
		pwPattern = "(.)\\1\\1\\1";
		Matcher matcher2 = Pattern.compile(pwPattern).matcher(pwd);

		if(!matcher.matches()){
			return false;
		}

		if(matcher2.find()){
			return false;
		}

		return true;
	}

	public static Locale convertStringToLocale(String language) {
		Locale locale = null;
		if (StringUtils.isEmpty(language)) {
			locale = locale.getDefault();
		} else {
			if (language.indexOf(",")>-1) {
				language = language.substring(0, 2);
			}
			locale = new Locale(language);
		}
		return locale;
	}


}
