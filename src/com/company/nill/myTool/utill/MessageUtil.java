package com.company.nill.myTool.utill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Component
public class MessageUtil {

	private static final Logger logger = LoggerFactory.getLogger(MessageUtil.class);

	private static MessageSource messageSource;

	@Autowired
	@Qualifier("errorMessgeSource")
	private MessageSource tempMessageSource;

	@PostConstruct
	public void registerInstance() {
		messageSource = tempMessageSource;
	}

	public static String getKeyMessage(String messageId, String localeCd) {
		Locale locale = MessageUtil.getLocale(localeCd);
		return messageSource.getMessage(messageId, null, messageId, locale);
	}

	public static String getKeyMessageWithValue(String messageId, String word, String localeCd) {
		Locale locale = MessageUtil.getLocale(localeCd);

		return messageSource.getMessage(messageId, new String[] {word}, messageId, locale);
	}

	public static String getKeyMessageWithValue(String messageId, Object[] args, String localeCd) {
		Locale locale = MessageUtil.getLocale(localeCd);
		return messageSource.getMessage(messageId, args, messageId, locale);
	}

	private static Locale getLocale(String localeCd) {
		if(localeCd == null || localeCd.equals("")) {
			localeCd = "ko_KR"; // 코드가 없으면 한국어로
		}

		Locale locale = Locale.KOREA;
		try {
			String[] arr = localeCd.split("_");
			locale = new Locale(arr[0], arr[1]);
		} catch (Exception e) {
			logger.error("[New Locale:::]" + e.getMessage());
			locale = Locale.KOREA;
		}

		if(locale == null) {
			locale = Locale.KOREA;
		}
		return locale;
	}
}
