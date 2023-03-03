package com.company.nill.myTool.utill;

import com.withdraw.batch.comm.err.ApplicationException;
import org.springframework.http.HttpMethod;

public class ApiCall {

	public static <T> T post(String uri, Object param, Class<T> res) throws ApplicationException {
        LegacyCall<T> legacyCall = new LegacyCall<>(uri);
        return legacyCall.send(param, res);
    }

	public static <T> T  get(String uri, Object param, Class<T> res) throws ApplicationException{
        LegacyCall<T> legacyCall = new LegacyCall<>(uri);
        legacyCall.setMethod(HttpMethod.GET);
        return legacyCall.send(param, res);
    }

	public static <T> T  patch(String uri, Object param, Class<T> res) throws ApplicationException{
        LegacyCall<T> legacyCall = new LegacyCall<>(uri);
        legacyCall.setMethod(HttpMethod.PATCH);
        return legacyCall.send(param, res);
    }
	
//	private static String getUri(API api, String currency, String suffix) {
//		String plfmUri = api.getUrl();
//		String rtnUri = plfmUri.replace("{currency}", currency);
//		return rtnUri.replace("{surfix}", suffix);
//	}
}
