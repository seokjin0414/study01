package com.company.nill.myTool.utill;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CryptoUtils {
    public static String makeSHA256(String input){

        String result = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(input.getBytes("utf8"));
            result = String.format("%064x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String encMD5(String str) {
        String MD5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            MD5 = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            MD5 = null;
        }
        return MD5;
    }

    public static String encSha1(String str) {
        String sha1 = "";

        // With the java libraries
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(str.getBytes("utf8"));
            sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (Exception e){
            e.printStackTrace();
            sha1 = null;
        }

        return sha1;
    }

    public static String encBase64(String str) {
        String encStr = "";
        try {
            byte[] targetBytes = str.getBytes("UTF-8");

            Base64.Encoder encoder = Base64.getEncoder();

            encStr = encoder.encodeToString(targetBytes);
        } catch (Exception e){
            e.printStackTrace();
            encStr = null;
        }
        return  encStr;

    }

    public static String decBase64(String str) throws UnsupportedEncodingException{
        Base64.Decoder decoder = Base64.getDecoder();

        // Decoder#decode(String src)
        byte[] decodedBytes = decoder.decode(str);

        // 디코딩한 문자열을 표시
        return new String(decodedBytes, "UTF-8");

    }
}
