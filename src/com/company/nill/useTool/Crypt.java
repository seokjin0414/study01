package com.company.nill.useTool;

import kr.onapp.bio.utils.AESUtils;

public class Crypt {

	public static void main(String[] args) throws Exception {
		// 대칭키 암호화
		AESUtils aes_util 		= new AESUtils("복호화");
		// 출금 파라미터 설정
		String privateKey 		= aes_util.decrypt("privateKey");
		
		System.out.println(privateKey);
		String privateKey1 		= aes_util.encrypt(privateKey);
		System.out.println(privateKey1);
	}

}
