package com.company.nill.myTool.utill;

import com.bind.front.exception.http.BadRequestException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordUtils {

    private static MessageDigest getDigetst() {
        try {
//			return MessageDigest.getInstance("SHA-1");
            return MessageDigest.getInstance("SHA-256");
//			return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static byte[] encode(byte[] data) {
        return getDigetst().digest(data);
    }

    public static String encode(String data) {
        return new String(Hex.encode(encode(data.getBytes())));
    }

    /**
     * Creates a random 8letter word, used as temporary password for reseting user password
     * @return String
     */
    public static String getNewPassword() {
        String passStr = "";
        int randomA = 0;
        int randomN = 0;
        String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        String[] numeric = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

        for (int i = 0; i < 6; i++) {
            if (Math.round(2 * Math.random()) % 2 == 0) {
                randomA = (int) (Math.round(26 * Math.random()) % 26);
                passStr += alphabet[randomA];
            } else {
                randomN = (int) (Math.round(10 * Math.random()) % 10);
                passStr += numeric[randomN];
            }
        }
        return passStr;
    }

    /**
     * 패스워드 유효성 검사
     * @param newPwd
     * @return
     */
    public static boolean checkPwReg(String newPwd) {
        if (StringUtils.isEmpty(newPwd)) {
            throw new BadRequestException("scrn.error.input.pw");
        }

        // 영문, 숫자, 특수문자 조합 (8~16 자리)
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%_=+-])[A-Za-z\\d!@#$%_=+-]{8,16}$";
        Matcher match = Pattern.compile(regex).matcher(newPwd);
        newPwd = StringUtils.trimLeadingWhitespace(newPwd);
        newPwd = StringUtils.trimTrailingWhitespace(newPwd);

       /* if (!match.find()) {
            throw new BadRequestException("error.pw.pattern1");
        }*/
        //같은문자 4자리
        if (samePwd(newPwd)) {
            throw new BadRequestException("error.pw.pattern2");
        }
        //연속문자 4자리
        if (continuousPwd(newPwd)) {
            throw new BadRequestException("error.pw.pattern3");
        }
        return true;
    }

    /**
     * 같은 문자, 숫자 4자리 체크
     * @param pwd
     * @return
     */
    public static boolean samePwd(String pwd) {
        String pattern5 = "(\\w)\\1\\1\\1";
        Matcher match = Pattern.compile(pattern5).matcher(pwd);
        return match.find() ? true : false;
    }

    /**
     * 연속 문자, 숫자 4자리 체크
     * @param pwd
     * @return
     */
    public static boolean continuousPwd(String pwd) {
        int o = 0;
        int d = 0;
        int p = 0;
        int n = 0;
        int limit = 4;
        for (int i = 0; i < pwd.length(); i++) {
            char tempVal = pwd.charAt(i);
            if (i > 0 && (p = o - tempVal) > -2 && (n = p == d ? n + 1 : 0) > limit - 3) {
                return true;
            }
            d = p;
            o = tempVal;
        }
        return false;
    }
}
