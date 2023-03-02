package com.company.nill.myTool.serviceEx;

import com.bind.front.biz.dao.WalletDao;
import com.bind.front.biz.vo.api.WithdrawWalletReq;
import com.bind.front.biz.vo.dto.TradeStateCndModel;
import com.bind.front.biz.vo.dto.WithdrawCndModel;
import com.bind.front.biz.vo.model.*;
import com.bind.front.exception.http.ApiExecutionException;
import com.bind.front.exception.http.BadRequestException;
import com.bind.front.utils.LegacyCall;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class WalletSvc {
	
	private Logger L = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    WalletDao walletDao;
    
    @Autowired
    CodeDtlSvc codeDtlSvc;

	@Autowired
	Environment env;
	
    @Value("${wallet.api.domain}")
    private String wallDomain;

    @Value("${wallet.api.wallet.gen.url}")
    private String genUrl;

    @Value("${wallet.api.wallet.gen.method}")
    private String getMethod;
    
    @Value("${wallet.api.wallet.withdraw.url}")
    private String withdrawUrl;
    
    @Value("${wallet.api.wallet.balance.url}")
    private String balanceUrl;

    @Value("${wallet.api.wallet.withdraw.method}")
    private String withdrawMethod;

	@Value("${wallet.api.wallet.invest.url}")
	private String withdrawInvest;

    private String resultCodeKey = "error_code";
    private String resultMsgKey = "error_msg";
    
    /**
     * @throws BadRequestException
     * 
     */
    public void generateWalletAll(String usrIdx) throws BadRequestException {
        
    	Wallet wallet = new Wallet();
    	wallet.setMemberNo(usrIdx);
    	
    	List<Wallet> list = walletDao.selectAllCrncy();
        System.out.println("generateWalletAll @@@@@@ START");
        for (Wallet w : list) {
        	
        	wallet.setCrncyCd(w.getCrncyCd());
        	wallet.setCrncyType("BIO");
        	
        	List<Wallet> userWalletVo = selectWallet(wallet);
            if (userWalletVo != null && userWalletVo.size() > 0) {
                continue;
            }

            generateWalletRes(usrIdx, w.getCrncyCd());
        }
    }
    
    public GenerateWalletRes generateWalletRes(String usrIdx, String crncyCd) throws BadRequestException {
        // 지갑주소 생성
        String url = genUrl.replace("{currency}", crncyCd);
        url = url.replace("{currencyType}", "BIO");
        url = url.replace("{memberNo}", usrIdx);

        LegacyCall<Map<String,Object>> call = new LegacyCall<>(wallDomain+url);
        call.setMethod(HttpMethod.POST);
        Class<Map<String,Object>> clazz = (Class<Map<String,Object>>)(Class)Map.class;
        Map<String,Object> res = call.send(null, clazz);
        
        if ("0".equals(res.get(resultCodeKey)))
        {
            Gson gson 					= new Gson();
            String resultJSon 			= gson.toJson(res.get("result"));
            GenerateWalletRes walletRes = gson.fromJson(resultJSon, GenerateWalletRes.class);
            return walletRes;
        }
        else
        {
            //한번더 시도
            call.setMethod(HttpMethod.POST);
            if( "0".equals(res.get(resultCodeKey)) || "20003".equals(res.get(resultCodeKey)) )
            {
                Gson gson 					= new Gson();
                String resultJSon 			= gson.toJson(res.get("result"));
                GenerateWalletRes walletRes = gson.fromJson(resultJSon, GenerateWalletRes.class);
                return walletRes;
            }
        }
        return null;
    }
    
    /**
     * 잔액 조회
     */
    public Map<String,Object> sch_account_bio_svc(String address, String crncyCd , String crncy_type ) {
    	//지갑주소 생성
        String url = balanceUrl.replace("{currency}", crncyCd);
        url = url.replace("{address}", address);
        url = url.replace("{currencyType}", crncy_type);

        LegacyCall<Map<String,Object>> call = new LegacyCall<>(wallDomain+url);
        call.setMethod(HttpMethod.GET);
        Class<Map<String,Object>> clazz = (Class<Map<String,Object>>)(Class)Map.class;
        
        Map<String,Object> rtn = null;
        
        try {
	        Map<String,Object> res = call.send(null, clazz);
	    	
	        System.out.println(res);
	        
	        
	        
	        if ("0".equals(res.get(resultCodeKey))) {
	        	Gson gson = new Gson();
	        	String resultJSon = gson.toJson(res.get("result"));
	        	rtn = gson.fromJson(resultJSon, (Class<Map<String,Object>>)(Class)Map.class);
	        	
	        } else {
	        	rtn = new HashMap<>();
	        	
	        	rtn.put("balance", "0");
	        }
        } catch (Exception e) {
        	rtn = new HashMap<>();
        	
        	rtn.put("balance", "0");
        }
        
        return rtn;
    }
    
    /**
	 * 잔액구하기
	 * @param wallet
	 */
	public Map<String,Object> getBalanceToNode(Wallet wallet) throws BadRequestException {
		String usr_idx    = wallet.getMemberNo();
    	String crncy_cd   = wallet.getCrncyCd();
    	String crncy_type = "BIO";
    	
    	List<Wallet> usr_wallet_list = selectWallet(wallet);
    	
    	if(usr_wallet_list.size() == 0) {
    		//throw new BadRequestException("error.wallet_checked");

    		Map<String,Object> rtn_map = new HashMap<>();
    		rtn_map.put("balance", "0");
    		
    		return rtn_map;
    	}
    	
    	Wallet usr_wallet = usr_wallet_list.get(0);
    	
    	String addr = usr_wallet.getAddr();
    	
    	HashMap<String,Object> param = new HashMap<>();
    	param.put("address", addr);
    	param.put("cruncyType", crncy_cd);

    	Map<String,Object> rtn_map = sch_account_bio_svc(addr , crncy_cd , crncy_type);
		
		L.debug("res===================" + rtn_map);
    	
		return rtn_map;
	}
	
	// 출금
    public void sendWithdrawConfirm(TnWidthdrawHis tnWidthdrawHis) {

		Wallet wallet = new Wallet();
		wallet.setCrncyCd(tnWidthdrawHis.getCrncyCd());
		wallet.setMemberNo(tnWidthdrawHis.getUsrIdx());
		wallet.setCrncyType(tnWidthdrawHis.getCrncyType());
		
		List<Wallet>  walletList =  selectWallet(wallet);

		//내 잔액 조회
		Map<String,Object> my_balance = getBalanceToNode(walletList.get(0));
		String balance = (String)my_balance.get("balance");
		
		//가스비 조회
		Wallet wallet_info = selectWalletCrncy(wallet);

        String gas_price 			= wallet_info.getGasPrice();
        String gas_rate 			= wallet_info.getGasRate();
        
        // 가스비
        BigDecimal dec_gas_price 	= new BigDecimal(gas_price);
        // 가스비 배수
        BigDecimal dec_gas_rate 	= new BigDecimal(gas_rate);
        
        // 스왑 수수료(%)
        String withdrawFee	   	= codeDtlSvc.selectTcCodeDtlVal("WITHDRAW_FEE" , "BIO");
        // 수수료 (0.97%)
        BigDecimal withdrawFeeW	= new BigDecimal(100).subtract(new BigDecimal(withdrawFee)).divide(new BigDecimal(100));
        // 수수료 (0.03%)
        BigDecimal withdrawFeeD = new BigDecimal(1).subtract(withdrawFeeW);
        
        System.out.println("withdrawFeeW : " + withdrawFeeW);
        System.out.println("withdrawFeeD : " + withdrawFeeD);
        
        // 출금 수수료 3% 지급 800 번 지갑 정보 조회
        Wallet admin_sch_wallet = new Wallet();
        admin_sch_wallet.setCrncyType("BIO");
        admin_sch_wallet.setCrncyCd("BIO");
        admin_sch_wallet.setMemberNo("800");
        
        // admin 지갑 주소 조회 -> crncy_cd , crncy_type 으로 조회
    	List<Wallet> admin_wallet_list = selectWallet(admin_sch_wallet);

        // BIO 출금 시 수수료 3%, 수수료 제외 97% 총 2건 전송해야 하므로 가스비 * 2 잔액 확인
        if("BIO".equals(wallet_info.getCrncyCd()) )
        {
        	// 내 잔액 조회
        	Map<String,Object> my_bio_balance = getBalanceToNode(walletList.get(0));
        	String bio_balance = (String)my_bio_balance.get("balance");
        	
        	dec_gas_price = dec_gas_price.multiply(dec_gas_rate).multiply(new BigDecimal(2));
        	
        	// 나의 잔액
    		BigDecimal useBalance = new BigDecimal(bio_balance);		// 나의 잔액
    		
    		// 가스비 + 나의 잔의 잔액
    		BigDecimal useableBalanceChk = useBalance.add(dec_gas_price);
    		
    		// 출금할 금액
    		BigDecimal AmountFrom = new BigDecimal(tnWidthdrawHis.getAmountFrom());

    		if(useableBalanceChk.compareTo(AmountFrom) < 0)
    		{
    			throw new BadRequestException("error.coin.lack");
    		}
    		
    		Wallet fee_wallet = new Wallet();
    		fee_wallet.setCrncyCd(tnWidthdrawHis.getCrncyCd());
    		fee_wallet.setAddr(tnWidthdrawHis.getReceiveAddr());
    		
    		/*
    		 * 1. 수수료 3% 800번 지갑에 출금(BIO)
    		 */
    		String locSendAddr = com.bind.front.utils.StringUtils.UUID();
    		
    		WithdrawCndModel fee_cnd = new WithdrawCndModel();
    		
    		// 출금 금액 100%
    		BigDecimal total_amount 	= new BigDecimal(tnWidthdrawHis.getAmountFrom());
    		// 수수료 금액 3%
    		BigDecimal fee_amount 		= total_amount.multiply(withdrawFeeD).setScale(4, BigDecimal.ROUND_HALF_UP);
    		// 출금 금액(100%) - 수수료 금액(3%) = 97%
    		BigDecimal withdraw_amount 	= total_amount.multiply(withdrawFeeW).setScale(4, BigDecimal.ROUND_HALF_UP);

    		fee_cnd.setLocSendAddr(locSendAddr);
    		fee_cnd.setCurrency("BIO");
    		fee_cnd.setVolume(fee_amount.toString());
    		fee_cnd.setReceiveAddr(admin_wallet_list.get(0).getAddr());		// 받는 사람
    		fee_cnd.setCnfgType("MA");                    					// 출금
    		fee_cnd.setFeeCrncy("BIO");
    		fee_cnd.setMemberNo(walletList.get(0).getMemberNo());        	// 보내는 사람
    		fee_cnd.setSenderAddr(walletList.get(0).getAddr());        		// 보내는 사람
    		fee_cnd.setCrncyType(tnWidthdrawHis.getCrncyType());
    		fee_cnd.setCurrencyType(tnWidthdrawHis.getCrncyType());

    		String fee = "0";
    		fee_cnd.setAddrType("E");
    		fee_cnd.setFeeType("IW");
    		fee_cnd.setTmpComment(String.valueOf("Send Fee withdraw"));
    		fee_cnd.setFee(fee);
    		
    		if(withdrawFee.equals("0")) {
    			
    		} else {
    			widthraw(fee_cnd);
    		}
    		
    		/*
    		 * 2. 수수료 3% 제외 97% 출금(BIO)
    		 */
    		
    		WithdrawCndModel cnd = new WithdrawCndModel();
    		cnd.setLocSendAddr(locSendAddr);
    		cnd.setCurrency(tnWidthdrawHis.getCrncyCd());
    		cnd.setVolume(withdraw_amount.toString());
    		cnd.setReceiveAddr(tnWidthdrawHis.getReceiveAddr());		// 받는 사람
    		cnd.setCnfgType("MA");                    					// 출금
    		cnd.setFeeCrncy(tnWidthdrawHis.getCrncyCd());
    		cnd.setMemberNo(walletList.get(0).getMemberNo());        	// 보내는 사람
    		cnd.setSenderAddr(walletList.get(0).getAddr());        		// 보내는 사람
    		cnd.setCrncyType(tnWidthdrawHis.getCrncyType());
    		cnd.setCurrencyType(tnWidthdrawHis.getCrncyType());

    		cnd.setAddrType("E");
    		cnd.setFeeType("I");
    		cnd.setTmpComment(String.valueOf("Send withdraw"));
    		cnd.setFee(fee);

    		widthraw(cnd);
        }
        // DST 출금 시 수수료 3%, 수수료 제외 97% 총 2건 전송해야 하므로 가스비 * 2 잔액 확인
        // BIO 가 아닌 Token 출금 시 gas_rate 가 다르기 때문에 가스비 잔액 조회 별도 필요
        else
        {
        	// 바이오 가격 조회
            TnCoinPrice tnCoinPrice = new TnCoinPrice();
        	tnCoinPrice.setCoinCd("BIO-USDT");
        	tnCoinPrice.setTradeCorp("LBANK");

        	TnCoinPrice bioPriceObj = selectCoinPriceOne(tnCoinPrice);
        	
        	// TOKEN 가격 조회
			tnCoinPrice.setCoinCd(tnWidthdrawHis.getCrncyCd() + "-USDT");
        	TnCoinPrice tokenPriceObj = selectCoinPriceOne(tnCoinPrice);
            
        	// TOKEN 출금 수량
    		BigDecimal total_amount 	= new BigDecimal(tnWidthdrawHis.getAmountFrom());
    		
    		// BIO 가격
        	String bio_price   			= bioPriceObj.getAskPrice();
    		BigDecimal dec_bio_price	= new BigDecimal(bio_price);
    		// TOKEN 가격 
        	String token_price 			= tokenPriceObj.getBidPrice();
    		BigDecimal dec_token_price	= new BigDecimal(token_price);
    		
    		// BIO 수수료 계산식
    		// (TOKEN 가격 * TOKEN 개수) / BIO 가격 * 3%
    		BigDecimal bio_fee_count	= dec_token_price.multiply(total_amount).divide(dec_bio_price, 4 , BigDecimal.ROUND_HALF_UP).multiply(withdrawFeeD).setScale(4, BigDecimal.ROUND_HALF_UP);
        	Wallet bio_wallet = new Wallet();
        	bio_wallet.setMemberNo(walletList.get(0).getMemberNo());
        	bio_wallet.setCrncyCd("BIO");
        	bio_wallet.setCrncyType("BIO");

        	// 내 잔액 조회(BIO)
        	Map<String,Object> my_bio_balance = getBalanceToNode(bio_wallet);
        	String bio_balance = (String)my_bio_balance.get("balance");

        	// 내 잔액 조회(Token)
        	Map<String,Object> my_token_balance = getBalanceToNode(walletList.get(0));
        	String token_balance = (String)my_token_balance.get("balance");

        	// 가스비 * 2 (BIO 수수료 출금 1번 + TOKEN 출금 1번)
        	dec_gas_price = dec_gas_price.multiply(dec_gas_rate).multiply(new BigDecimal(2));

        	// 나의 잔액
    		BigDecimal useBalance = new BigDecimal(token_balance);
    		// BIO
    		BigDecimal dec_bio_balance = new BigDecimal(bio_balance);
    		// 출금할 금액
    		BigDecimal AmountFrom = new BigDecimal(tnWidthdrawHis.getAmountFrom());

    		System.out.println("BIO 잔액 : " + dec_bio_balance);
    		System.out.println("BIO 수수료 : " + bio_fee_count);
    		System.out.println("TOKEN 잔액 : " +useBalance);
    		System.out.println("TOKEN 출금 수량 : " +AmountFrom);
    		
    		// BIO 가스비 잔액 비교
    		if(dec_bio_balance.compareTo(bio_fee_count.add(dec_gas_price)) < 0)
    		{
    			throw new BadRequestException("error.coin.lack");
    		}
    		
    		// Token 잔액 비교
    		if(useBalance.compareTo(AmountFrom) < 0)
    		{
    			throw new BadRequestException("error.coin.lack");
    		}
    		
    		/*
    		 * 1. 수수료 3% 800번 지갑에 출금(BIO)
    		 */
    		String locSendAddr = com.bind.front.utils.StringUtils.UUID();
    		
    		WithdrawCndModel fee_cnd = new WithdrawCndModel();

    		fee_cnd.setLocSendAddr(locSendAddr);
    		fee_cnd.setCurrency("BIO");
    		fee_cnd.setVolume(bio_fee_count.toString());
    		fee_cnd.setReceiveAddr(admin_wallet_list.get(0).getAddr());		// 받는 사람
    		fee_cnd.setCnfgType("MA");                    					// 출금
    		fee_cnd.setFeeCrncy("BIO");
    		fee_cnd.setMemberNo(walletList.get(0).getMemberNo());        	// 보내는 사람
    		fee_cnd.setSenderAddr(walletList.get(0).getAddr());        		// 보내는 사람
    		fee_cnd.setCrncyType(tnWidthdrawHis.getCrncyType());
    		fee_cnd.setCurrencyType(tnWidthdrawHis.getCrncyType());

    		String fee = "0";
    		fee_cnd.setAddrType("E");
    		fee_cnd.setFeeType("IW");
    		fee_cnd.setTmpComment(String.valueOf("Send Fee withdraw"));
    		fee_cnd.setFee(fee);

    		widthraw(fee_cnd);
    		
    		/*
    		 * 2. TOKEN 출금
    		 */
    		WithdrawCndModel cnd = new WithdrawCndModel();
    		cnd.setLocSendAddr(locSendAddr);
    		cnd.setCurrency(tnWidthdrawHis.getCrncyCd());
    		cnd.setVolume(tnWidthdrawHis.getAmountFrom());
    		cnd.setReceiveAddr(tnWidthdrawHis.getReceiveAddr());		// 받는 사람
    		cnd.setCnfgType("MA");                    					// 출금
    		cnd.setFeeCrncy(tnWidthdrawHis.getCrncyCd());
    		cnd.setMemberNo(walletList.get(0).getMemberNo());        	// 보내는 사람
    		cnd.setSenderAddr(walletList.get(0).getAddr());        		// 보내는 사람
    		cnd.setCrncyType(tnWidthdrawHis.getCrncyType());
    		cnd.setCurrencyType(tnWidthdrawHis.getCrncyType());

    		cnd.setAddrType("E");
    		cnd.setFeeType("I");
    		cnd.setTmpComment(String.valueOf("Send withdraw"));
    		cnd.setFee(fee);

    		widthraw(cnd);
        }
	}
    
	/**
	 * 출금하기
	 * @param cnd
	 * @return
	 */
	public GenerateWalletRes widthraw(WithdrawCndModel cnd) throws BadRequestException {

		//주소 체크
		String senderAddr 	= cnd.getSenderAddr();

		if (StringUtils.isEmpty(senderAddr)) {
			throw new BadRequestException("error.sender_addr.empty");
		}

		String receiveAddr 	= cnd.getReceiveAddr();

		if (StringUtils.isEmpty(receiveAddr)) {
			throw new BadRequestException("error.receiver_addr.empty");
		}

		String addr_type 	= cnd.getAddrType();
		String fee_type 	= cnd.getFeeType();
		String comment 		= cnd.getTmpComment();

		String vol_crncy 	= cnd.getCurrency();	// 출금할 코인
		String volume 		= cnd.getVolume();		// 출금 수량

		String fee 			= "0";					// 수수료

		//출금신청하기 생성
		String url = withdrawUrl.replace("{currency}", cnd.getCurrency());

		LegacyCall<Map<String,Object>> call = new LegacyCall<>(wallDomain+url);
		call.setMethod(HttpMethod.POST);

		Class<Map<String,Object>> clazz = (Class<Map<String,Object>>)(Class)Map.class;

		WithdrawWalletReq req = new WithdrawWalletReq();
		req.setAddrType(addr_type);
		req.setFeeType(fee_type);
		req.setComment(comment);
		req.setCurrency(vol_crncy);

		Wallet paramReceiveWallet = new Wallet();
		paramReceiveWallet.setAddr(receiveAddr);
		paramReceiveWallet.setCrncyCd(vol_crncy);
		paramReceiveWallet.setCrncyType(cnd.getCurrencyType());
		
		Wallet receiveWallet = walletDao.selectReceiveMemberNo(paramReceiveWallet);

		String receiveMemberNo = "0";
		
		if(receiveWallet != null)
		{
			receiveMemberNo = receiveWallet.getMemberNo();
		}

		String locSendAddr = cnd.getLocSendAddr();
		if(locSendAddr == null || locSendAddr.length() == 0) locSendAddr = "";

		WithdrawWalletReq param = new WithdrawWalletReq();
		param.setAddrType(req.getAddrType());
		param.setAdminInfo("");
		param.setComment(req.getComment());
		param.setCurrency(req.getCurrency());
		param.setFee(fee);
		param.setFeeType(req.getFeeType());
		param.setLocRcvAddr("");
		param.setLocSndAddr(locSendAddr);
		param.setMemberNo(cnd.getMemberNo());
		param.setNodeId("0");
		param.setReceiveAddr(receiveAddr);
		param.setReceiveMemberNo(Long.parseLong(receiveMemberNo));
		param.setSendAddr(senderAddr);
		param.setState("R");
		param.setVolume(volume);
		param.setCurrencyType(cnd.getCurrencyType());

		Map<String,Object> res = call.send(param, clazz);

		GenerateWalletRes walletRes = null;

		if ("0".equals(res.get(resultCodeKey)))
		{
			Gson gson 			= new Gson();
			String resultJSon 	= gson.toJson(res.get("result"));
			
			walletRes 			= gson.fromJson(resultJSon, GenerateWalletRes.class);
		}
		else
		{
			L.error("[withdraw error]:" + res);

			String result_code 	= (String) res.get(resultCodeKey);
			String err_cd 		= "";

			//API 명세와 다른 요청
			if("90001".equals(result_code) || "90006".equals(result_code))
			{
				err_cd = "error.withraw.data_error";

			}
			else if("10003".equals(result_code))
			{
				//잔액 부족
				err_cd = "error.show.me.the.money";
			}
			else if("20001".equals(result_code))
			{
				//지원하지 않는 코인
				err_cd = "error.withraw.crncy.not_support";
			}

			throw new ApiExecutionException(err_cd);

        	/*
       	 	0		"ok"	ErrorCode.OK	성공
			90001	"bad request"	ErrorCode.BAD_REQUEST	API 명세와 다른 요청
			90002	"no permission"	ErrorCode.UNAUTHORIZED	권한 없음
			90003	"service temporarily unavailable"	ErrorCode.SERVICE_UNAVAILABLE	서비스 불가
			90004	"internal server error"	ErrorCode.INTERNAL_SERVER_ERROR	내부 서비스 오류
			90005	"unknown system error"	ErrorCode.UNKNOWN_ERROR	서비스 논리 오류
			90006	"check required parameters"	ErrorCode.PARAM_ERROR_REQUIRED	파라미터 정보 오류
			10003	"insufficient fund"	ErrorCode.PARAM_LOW_BALANCE	잔액부족
			20001	"the currency does not exist"	ErrorCode.NOT_EXIST_CRNCY	지원되지 않는 화폐타입
			20003	"A wallet address has already been issued"	ErrorCode.ALREADY_EXIST_ADDRESS	이미 해당 화폐타입의 지갑주소가 존재
			20004	"api is processing please wait a moment"	ErrorCode.REGISTERING_ADDRESS	API 처리 진행중
			20005	"Failed to create TX ID."	ErrorCode.NOT_EXIST_TXID	존재하지 않는 주문번호
        	 */
		}

		return walletRes;
	}
	
    /**
     * 사용자 지갑 정보 조회
     * @param wallet
     * @return
     */
    public List<Wallet> selectWallet(Wallet wallet) {
    	return walletDao.selectWallet(wallet);
    }
    
    /**
     * 코인 정보 조회
     * @param wallet
     * @return
     */
 	public Wallet selectWalletCrncy(Wallet wallet) {
 		return walletDao.selectWalletCrncy(wallet);
 	}
 	
 	public int checkAddr(Wallet wallet2) {
		return walletDao.checkAddr(wallet2);
	}
 	
 	//개인 출금 대기건 조회
	public int selectMyWithdrawCnt(String usr_idx) throws BadRequestException {
		
		Wallet my_wallet = new Wallet();
        my_wallet.setMemberNo(usr_idx);
        
		int withdraw_cnt = walletDao.selectMyWithdrawCnt(my_wallet);
		
		if(withdraw_cnt > 0) {
        	throw new BadRequestException("error.wallet.check");
        }
		
		return withdraw_cnt;
	}
	
	/**
	 * 코인 가격 조회
	 * @param 
	 * @return
	 */
    public TnCoinPrice selectCoinPriceOne(TnCoinPrice tnCoinPrice) {
    	return walletDao.selectCoinPriceOne(tnCoinPrice);
    }
    
    /**
	 * 코인 공시 가격 조회
	 * @param 
	 * @return
	 */
    public TnFundCoinPrice selectFundCoinPriceOne(String crncyCd) {
    	return walletDao.selectFundCoinPriceOne(crncyCd);
    }
    
    /**
     * 회원 리스트
     *
     * @param crncyCd
     */
    public TnCoinPrice coinPriceInfo(String crncyCd) {
    	return walletDao.coinPriceInfo(crncyCd);
    }

	/**
	 * 공시 가격 리스트 조회
	 * @return
	 */
	public List<TnFundCoinPrice> coinPriceList() {
		return walletDao.coinPriceList();
	}
	
	/**
	 * 입출금 내역 조회
	 * @return
	 */
	public List<TradeState> withdrawDepoistList(TradeStateCndModel param) {
		return walletDao.selectWidthDepositList(param);
	}
	
	/**
	 * 주소 목록 조회
	 * @return
	 */
	public List<TnExWalletAddr> getTnExWalletAddr(TnExWallet tnExWallet) {
		return walletDao.getTnExWalletAddr(tnExWallet);
	}

	/**
	 * 스크래핑 코인 가격 조회
	 * @return
	 */
	public List<TnCoinPrice> getCoinPrice() {
		return walletDao.getCoinPrice();
	}

	/**
	 * 스크래핑 코인 DAY 등락 비율
	 *
	 * @param tnCoinPrice
	 */
	public TnCoinPrice getCoinRate(TnCoinPrice tnCoinPrice) throws BadRequestException {
		TnCoinPrice result = walletDao.getCoinRate(tnCoinPrice);

		return result;
	}


}
