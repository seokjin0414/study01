package com.company.nill.myTool.utill;

import com.bind.front.biz.vo.SessionUserVo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

public class SessionUtil {

    private final static String SESSION_USER = "SESSION_USER";

    //세션가지고 오기
    public static HttpSession getSession(HttpServletRequest request) {
        return request.getSession();
    }

    //로그인 사용자
    public static SessionUserVo getSessionUser(HttpServletRequest request) {
        if (getSession(request).getAttribute(SESSION_USER) == null) {
            return null;
        }
        return (SessionUserVo) getSession(request).getAttribute(SESSION_USER);
    }

    public static String getAddressFromHead(HttpServletRequest request) {
        String address = request.getHeader("walletAddress");
        if (StringUtils.isEmpty(address)) {
            return "";
        }
        return address;
    }

    public static boolean isLogin(HttpServletRequest request) {
        if (getSessionUser(request) != null) {
            return true;
        }
        return false;
    }

    public static void setSessionUser(HttpServletRequest request, SessionUserVo sessionUserVo) {
        getSession(request).setAttribute(SESSION_USER, sessionUserVo);
    }

    public static void setSessionUserAnonymous(HttpServletRequest request) {
        SessionUserVo newSession = new SessionUserVo().buildAnonymous(request);
        getSession(request).setAttribute(SESSION_USER, newSession);
    }

    public static void setSession(HttpServletRequest request, String key, Object value) {
        getSession(request).setAttribute(key, value);
    }

    public static Object getSessionValue(HttpServletRequest request, String key) {
        if (getSession(request).getAttribute(key) == null) {
            return null;
        }
        return getSession(request).getAttribute(key);
    }

    /**
     * 쿠키가지고 오기
     * @param request
     * @param name
     * @return
     */
    public static String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return "";
    }

    /**
     * 쿠키값 설정
     * @param response
     * @param name
     * @param value
     * @param expTime
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int expTime) {
        try {
            value = URLEncoder.encode(value, "utf-8");
        } catch (Exception e) {
            value = "";
        }

        Cookie setCookie = new Cookie(name, value);
        setCookie.setMaxAge(expTime);
        setCookie.setPath("/");
        response.addCookie(setCookie);
    }

    /**
     * 쿠키값 삭제
     * @param response
     * @param name
     */
    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie setCookie = new Cookie(name, null);
        setCookie.setMaxAge(0);
        setCookie.setPath("/");
        response.addCookie(setCookie);
    }

    /**
     * 쿠키전체 삭제
     * @param request
     * @param response
     */
    public static void invalidCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }
        for (Cookie c : cookies) {
            c.setMaxAge(0);
            response.addCookie(c);
        }
    }

    public static void logout(HttpServletRequest request) {
        SessionUserVo userVo = SessionUtil.getSessionUser(request);
        getSession(request).invalidate();
        userVo = null;
    }

    public static String getRefCd(HttpServletRequest request) {
        String refCd = request.getParameter("refCd");
        if (!StringUtils.isEmpty(refCd)) {
            return refCd;
        }
        if (refCd == null) {
            refCd = getCookie(request, "refCd");
        }
        return refCd;
    }

    public static void setRefCd(HttpServletResponse response, String refCd) {
        setCookie(response, "refCd", refCd, 60 * 60 * 24);
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
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
