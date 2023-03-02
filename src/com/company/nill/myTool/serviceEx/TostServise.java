package com.company.nill.myTool.serviceEx;

import com.bind.front.biz.dao.MyInfoDao;
import com.bind.front.biz.dao.TostDao;
import com.bind.front.biz.dao.UserDao;
import com.bind.front.biz.vo.dto.WithdrawCndModel;
import com.bind.front.biz.vo.model.*;
import com.bind.front.exception.http.BadRequestException;
import com.bind.front.utils.ExcelUpload;
import com.bind.front.utils.PasswordUtils;
import com.bind.front.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TostServise {

    private final TostDao tostDao;

    private  final MyInfoDao myInfoDao;

    private final WalletSvc walletSvc;

    private final ExcelUpload excelUpload;
    private final UserDao userDao;
    private final UserService userService;

    /**
     * 메인화면 TOST 리스트
     */
    public List<Tost> mainTostList() {
        return tostDao.mainTostList();
    }

    /**
     * STATUS 체크
     *
     * @param status
     */
    public int statusCheck(String status) {
        int result = 0;

        if (!status.equals("P")) {
            throw new BadRequestException("진헹중인 상태가 아닙니다.");
        }

        log.info("----------checkStstus OK");
        return result;
    }

    /**
     * input check (입력 값 검증)
     *
     * @param vo
     */
    public String checkInput(String vo) throws BadRequestException {
        try {
            if (Double.parseDouble(vo) < 0) {
                throw new BadRequestException("음수는 입력이 불가능합니다.");
            }
        } catch (NullPointerException nu) {
            return "0";
        } catch (NumberFormatException nm) {
            throw new BadRequestException("올바른 입력값을 입력해주세요.");
        }
        return vo;
    }

    /**
     * bad user 확인
     *
     * @param userIdx
     */
    public int checkBadUser(String userIdx) throws BadRequestException {
        TbUserModel vo = new TbUserModel();
        vo.setUsrIdx(userIdx);
        int resut = myInfoDao.getBadUserCnt(vo);
        if (resut > 0) {
            throw new BadRequestException("차단된 사용자입니다.");
        }

        log.info("----------checkBadUser OK");
        return resut;
    }

    /**
     * 잔여 가입금액 체크
     *
     * @param remainingCheck
     */
    public int remainingCheck(String remainingCheck) {
        int result = 0;

        if (!remainingCheck.equals("0")) {
            if (remainingCheck.equals("-1")) {
                throw new BadRequestException("TOSTING 가능한 최대치가 참여 되어있습니다.");
            } else if (remainingCheck.equals("-2")) {
                throw new BadRequestException("참여 가능한 잔여금액이 TOSTING 가능한 최소금액 보다 적습니다.");
            }
        }

        log.info("----------checkRemaining OK");
        return result;
    }

    /**
     * 지갑 리스트 (TOST 용)
     *
     * @param tost
     */
    public List<Wallet> walletList(Tost tost) {
        return tostDao.walletList(tost);
    }

    /**
     * 잔고 구하기 (TOST & GAS)
     *
     * @param tost
     */
    public Tost setBalance(Tost tost) {
        String cd = tost.getCrncyCd();
        String gasCd = "BIO";

        List<Wallet> list = walletList(tost);

        for (Wallet w : list) {
            String crncyCd = w.getCrncyCd();
            String crncyType = w.getCrncyType();

            if (crncyCd.equals(cd)) {
                Map<String, Object> balanceObj = walletSvc.sch_account_bio_svc(w.getAddr(), crncyCd, crncyType);
                tost.setMyBalance(balanceObj.get("balance").toString());
                tost.setMyAddr(w.getAddr());
                tost.setTostCoinStnd(w.getBidPrice());
                tost.setGasValue(w.getGasValue());
            }

            if (crncyCd.equals(gasCd)) {
                Map<String, Object> balanceObj = walletSvc.sch_account_bio_svc(w.getAddr(), crncyCd, crncyType);
                tost.setMyGasBalance(balanceObj.get("balance").toString());
                tost.setGasCoinStnd(w.getBidPrice());
            }

        }

        // USD 환산
        BigDecimal myUsd = new BigDecimal(tost.getMyBalance()).multiply(new BigDecimal(tost.getTostCoinStnd()));
        tost.setMyUsd(myUsd.toString());

        log.info("----------setBalance OK");
        return tost;
    }

    /**
     * 잔고 체크 (TOST 용)
     *
     * @param my
     * @param cost
     */
    public int balanceCheck(String my, String cost) {
        int result = 0;

        BigDecimal bigMy = new BigDecimal(my);
        BigDecimal bigCost = new BigDecimal(cost);

        if (bigCost.compareTo(bigMy) > 0) {
            result = -1;
        }
        
        log.info("----------checkBalance OK");
        return result;
    }

    /**
     * input 유효성 체크 (TOSTING)
     *
     * @param tost
     */
    public int inpuitAvailableCheck(Tost tost) {
        int result = 0;
        BigDecimal inputUsd = new BigDecimal(tost.getInputUsd());
        BigDecimal remainingAmount = new BigDecimal(tost.getRemainingAmount());
        BigDecimal min = new BigDecimal(tost.getPerMinVal());

        //  input min 체크
        if (min.compareTo(inputUsd) > 0) {
            throw new BadRequestException("요청금액이 TOSTING 가능한 최소금액 보다 작습니다.");
        }

        //  input 잔여금액 체크
        if (inputUsd.compareTo(remainingAmount) > 0) {
            throw new BadRequestException("요청금액이 TOSTING 가입가능한 잔여금액을 초과하였습니다.");
        }

        //  input 잔고 체크
        if (0 > balanceCheck(tost.getMyBalance(), tost.getInput())) {
            throw new BadRequestException(tost.getCrncyCd() + " 잔고가 부족합니다");
        }

        log.info("----------checkInpuiAvailable OK");
        return result;
    }

    /**
     * TN_USER_STKINFO 등록
     *
     * @param tost
     */
    public int insertTnUserStkinfo(Tost tost) throws BadRequestException {
        int result = 0;

        TnUserStkinfo vo = new TnUserStkinfo();
        vo.setStkIdx(tost.getStkIdx());
        vo.setUsrIdx(tost.getUsrIdx());
        vo.setCoinPrice(tost.getTostCoinStnd());
        vo.setBalance(tost.getInput());
        vo.setStkVlme(tost.getInputUsd());

        // TN_USER_STKINFO 등록
        tostDao.insertTnUserStkinfo(vo);

        log.info("----------insertTnUserStkinfo OK");
        return result;
    }

    /**
     * TH_USER_STKINFO 등록
     *
     * @param tost
     */
    public int insertThUserStkinfo(Tost tost) throws BadRequestException {
        int result = 0;

        String usrStkIdx = tostDao.getUsrStkIdx(tost);

        ThUserStkinfo vo = new ThUserStkinfo();
        vo.setStkIdx(tost.getStkIdx());
        vo.setUsrStkIdx(usrStkIdx);
        vo.setCrncyCd(tost.getCrncyCd());
        vo.setCrncyType(tost.getCrncyType());
        vo.setStkVlme(tost.getInput());
        vo.setProcStatus("W");
        vo.setCorpAddr(tost.getCorpAddr());
        vo.setCorpMemberNo(tost.getCorpUsrIdx());
        vo.setTxId(tost.getLocSendAddr());

        // TN_USER_STKINFO 등록
        tostDao.insertThUserStkinfo(vo);

        log.info("----------insertThUserStkinfo OK");
        return result;
    }

    /**
     * tost 출금
     *
     * @param tost
     */
    public int tostWidthraw(Tost tost) throws BadRequestException {
        int result = 0;

        if (tost.getInput().equals("0") || tost.getInput().equals("")) {
            throw new BadRequestException("잘못된 입금 요청입니다.");
        } else {
            WithdrawCndModel cnd = new WithdrawCndModel();
            cnd.setLocSendAddr(tost.getLocSendAddr());
            cnd.setCurrency(tost.getCrncyCd().toLowerCase());
            cnd.setVolume(tost.getInput());
            cnd.setReceiveAddr(tost.getCorpAddr());        //받는 사람
            cnd.setCnfgType("MA");                    //출금
            cnd.setFeeCrncy(tost.getCrncyCd().toLowerCase());
            cnd.setMemberNo(tost.getUsrIdx());        //보내는 사람
            cnd.setSenderAddr(tost.getMyAddr());        //보내는 사람
            cnd.setCrncyType(tost.getCrncyType());
            cnd.setCurrencyType(tost.getCrncyType());
            cnd.setAddrType("E");
            cnd.setFeeType("TS");
            cnd.setTmpComment(String.valueOf("tost input"));

            String fee = "0";
            cnd.setFee(fee);

            walletSvc.widthraw(cnd);
        }

        log.info("----------tostWidthraw OK");
        return result;
    }

    /**
     * TN_USER_SEL_REWARD_COIN 등록
     *
     * @param tost
     */
    public int insertTnUserSelRewardCoin(Tost tost) throws BadRequestException {
        int result = 0;

        TnCorpRewardCoin tn = new TnCorpRewardCoin();
        tn.setCorpIdx(tost.getCorpIdx());

        TnCorpRewardCoin rc = tostDao.selectTnCorpRewardCoin(tn);

        TnUserSelRewardCoin vo = new TnUserSelRewardCoin();
        vo.setUsrIdx(tost.getUsrIdx());
        vo.setStkIdx(tost.getStkIdx());
        vo.setCrncyCd(rc.getCrncyCd());
        vo.setCrncyType(rc.getCrncyType());
        vo.setChangeYn("Y");

        //  TN_USER_SEL_REWARD_COIN 삭제
        tostDao.deleteTnUserSelRewardCoin(vo);
        log.info("----------deleteTnUserSelRewardCoin OK");

        //  TN_USER_SEL_REWARD_COIN 등록
        tostDao.insertTnUserSelRewardCoin(vo);

        log.info("----------insertTnUserSelRewardCoin OK");
        return result;
    }

    /**
     * TN_USER_SEL_REWARD_COIN 수정
     *
     * @param tost
     */
    public int updateTnUserSelRewardCoin(Tost tost) throws BadRequestException {
        int result = 0;

        Tost vo = tostDao.tostInfo(tost);

        TnCorpRewardCoin tn = new TnCorpRewardCoin();
        tn.setCorpIdx(vo.getCorpIdx());
        tn.setCrncyCd(tost.getSelectRewardCoin());

        TnCorpRewardCoin rc = tostDao.selectTnCorpRewardCoin(tn);

        TnUserSelRewardCoin uc = new TnUserSelRewardCoin();
        uc.setUsrIdx(tost.getUsrIdx());
        uc.setStkIdx(vo.getStkIdx());
        uc.setCrncyCd(rc.getCrncyCd());
        uc.setCrncyType(rc.getCrncyType());
        uc.setChangeYn("Y");

        //  checkTnUserSelRewardCoin (STATUS)
        checkTnUserSelRewardCoin(uc);

        //  TN_USER_SEL_REWARD_COIN 수정
        tostDao.updateTnUserSelRewardCoin(uc);

        log.info("----------updateTnUserSelRewardCoin OK");
        return result;
    }

    /**
     * TN_USER_SEL_REWARD_COIN 체크 (STATUS)
     *
     * @param tnUserSelRewardCoin
     */
    public int checkTnUserSelRewardCoin(TnUserSelRewardCoin tnUserSelRewardCoin) throws BadRequestException {
        int result = 0;

        TnUserSelRewardCoin vo = tostDao.selectTnUserSelRewardCoin(tnUserSelRewardCoin);

        if (vo.getChangeYn().equals("N")) {
            throw new BadRequestException("오늘은 이자 코인을 선택할수 없습니다.");
        }

        log.info("----------checkTnUserSelRewardCoin OK");
        return result;
    }

    /**
     * TN_USER_SEL_REWARD_COIN 리스트
     *
     * @param wallet
     */
    public Map<String, List<TnUserSelRewardCoin>> tnUserSelRewardCoinList(Wallet wallet) throws BadRequestException {
        Map<String, List<TnUserSelRewardCoin>> result = new HashMap<>();

        List<Wallet> walletList = walletSvc.selectWallet(wallet);

        for(Wallet w : walletList) {
            result.put(w.getCrncyCd(), tostDao.tnUserSelRewardCoinList(w));
        }

        return result;
    }

    /**
     * TOSTING 상세
     *
     * @param tost
     */
    public Tost tosting(Tost tost) {
        Tost result = tostDao.tosting(tost);
        result.setUsrIdx(tost.getUsrIdx());

        // 상태 체크
        statusCheck(result.getProcStatus());

        // 잔여 가입금액 체크
        remainingCheck(result.getRemainingCheck());

        // SET 잔고 & COIN INFO
        result = setBalance(result);

        // MY 잔고 체크 (perMinVal)
        if(0 > balanceCheck(result.getMyUsd(), result.getPerMinVal())) {
            throw new BadRequestException("보유중인 잔고가 TOSTING 가능한 최소금액($" + result.getPerMinVal() + ") 보다 적습니다.");
        }

        return result;
    }

    /**
     * TOSTING 요청 체크
     *
     * @param tost
     */
    public int checkTostingOrder(Tost tost) throws BadRequestException {
        log.info("@@@@@@@@@@@@@@@@@@@@ Transaction START");
        int result = 0;

        String stkIdx = tost.getStkIdx();
        String usrIdx = tost.getUsrIdx();

        //  배드 유저 확인
        checkBadUser(usrIdx);

        //  출금 준비중인건이 있는 확인 리턴
        walletSvc.selectMyWithdrawCnt(usrIdx);

        Tost vo = tosting(tost);
        vo.setInput(checkInput(tost.getInput()));

        // GAS 잔고 체크
        if(0 > balanceCheck(vo.getMyGasBalance(), vo.getGasValue())) {
            throw new BadRequestException("보유중인 BIO가 예상 가스비보다 적습니다.");
        }

        //  input USD 환산
        BigDecimal inputUsd = new BigDecimal(vo.getInput()).multiply(new BigDecimal(vo.getTostCoinStnd()));
        vo.setInputUsd(inputUsd.toString());

        //  input 유효성 체크
        inpuitAvailableCheck(vo);

        //  TN_USER_STKINFO 등록
        insertTnUserStkinfo(vo);

        //  TH_USER_STKINFO 등록
        vo.setLocSendAddr(StringUtils.UUID());
        insertThUserStkinfo(vo);

        //  TN_USER_SEL_REWARD_COIN 등록
        insertTnUserSelRewardCoin(vo);

        //  출금요청
        tostWidthraw(vo);

        log.info("@@@@@@@@@@@@@@@@@@@@ Transaction END");
        return result;
    }

    /**
     * Earnings Available 상세 (MY PAGE)
     *
     * @param myTosting
     */
    public MyTosting earningsAvailable(MyTosting myTosting) throws BadRequestException {
        MyTosting result = tostDao.earningsAvailable(myTosting);

        log.info("----------selectEarningsAvailable OK");
        return result;
    }

    /**
     *  Tosting Amount (MY PAGE)
     *
     * @param myTosting
     */
    public String tostingAmount(MyTosting myTosting) throws BadRequestException {
        String result = tostDao.tostingAmount(myTosting);

        log.info("----------selectTostingAmounte OK");
        return result;
    }

    /**
     * Referral Profit (MY PAGE)
     *
     * @param myTosting
     */
    public String referralProfit(MyTosting myTosting) throws BadRequestException {
        String result = tostDao.referralProfit(myTosting);

        log.info("----------selectReferralProfit OK");
        return  result;
    }

    /**
     * Referral Benefit 리스트 (MY PAGE)
     *
     * @param myTosting
     */
    public List<MyTosting> referralBenefitList(MyTosting myTosting) throws BadRequestException {
        List<MyTosting> result = tostDao.referralBenefitList(myTosting);

        log.info("----------selectReferralBenefitList OK");
        return  result;
    }

    /**
     * Application Details 리스트 (MY PAGE)
     *
     * @param myTosting
     */
    public List<MyTosting> applicationDetailsList(MyTosting myTosting) throws BadRequestException {
        List<MyTosting> result = tostDao.applicationDetailsList(myTosting);

        log.info("----------selectApplicationDetailsList OK");
        return result;
     }

    /**
     * USE TOSTING INFO (MY PAGE)
     *
     */
    public List<MyTosting> useTostingList() throws BadRequestException {
        List<MyTosting> result = tostDao.useTostingList();

        log.info("----------selectUseTostingList OK");
        return result;
    }

    /**
     * MY TOSTING INFO (MY PAGE)
     *
     * @param myTosting
     */
    public MyTosting myTostingInfo(MyTosting myTosting) throws BadRequestException {
        // Earnings Available 상세 (MY PAGE)
        MyTosting result = earningsAvailable(myTosting);

        if (result == null) {
            return new MyTosting();
        }

        // Tosting Amount (MY PAGE)
        result.setTostingAmount(tostingAmount(myTosting));

        // Referral Profit (MY PAGE)
        result.setReferralProfit(referralProfit(myTosting));

        log.info("----------setmyTostingInfo OK");
        return result;
    }

    /**
     * TOST GET PROFIT INFO
     *
     * @param tnUserRewardTotal
     */
    public List<TnUserRewardTotal> tostGetProfitInfo(TnUserRewardTotal tnUserRewardTotal) throws BadRequestException {
        List<TnUserRewardTotal> result = tostDao.tnUserRewardTotalList(tnUserRewardTotal);

        log.info("----------getTostGetProfitInfo OK");
        return result;
    }

    /**
     * checkTnUserRewardReq
     *
     * @param tnUserRewardTotal
     */
    public int checkTnUserRewardReq(TnUserRewardTotal tnUserRewardTotal) throws BadRequestException {
        int result = tostDao.checkTnUserRewardReq(tnUserRewardTotal);

        if (result > 0) {
            throw new BadRequestException("진행중인 이자 출금 신청건이 있습니다. 잠시후 다시 요청해주세요");
        }

        log.info("----------checkTnUserRewardReq OK");
        return result;
    }

    /**
     * CHECK TOST GET PROFIT
     *
     * @param tnUserRewardTotal
     */
    public int checkTostGetProfit(TnUserRewardTotal tnUserRewardTotal) throws BadRequestException {
        int result = 0;
        String usrIdx = tnUserRewardTotal.getUsrIdx();

        //  배드 유저 확인
        checkBadUser(usrIdx);

        // 출금대기건 체크
        checkTnUserRewardReq(tnUserRewardTotal);

        TnUserRewardTotal vo = tostGetProfitInfo(tnUserRewardTotal).get(0);
        vo.setInput(tnUserRewardTotal.getInput());
        vo.setUsrReceiveAddr(tnUserRewardTotal.getUsrReceiveAddr());

        // 이자 잔고 체크
        if (0 > balanceCheck(vo.getRemainingVlme(), vo.getInput())) {
        	throw new BadRequestException("요청한 금액이 보유 이자 금액을 초과하였습니다.");
        }

      /*  //  수수료 체크
        Map<String, Object> balanceObj = walletSvc.sch_account_bio_svc(vo.getAddr(), vo.getRateCrncyCd(), vo.getRateCrncyType());
        String my = balanceObj.get("balance").toString();*/

        //  수수료
        BigDecimal input = new BigDecimal(vo.getInput());
        BigDecimal swapRate = new BigDecimal(vo.getSwapRate());
        BigDecimal rateValue = input.multiply(swapRate).divide(new BigDecimal("100"));
        BigDecimal reqVlme = input.subtract(rateValue);
        log.info("----------rateValue = " + rateValue.toPlainString());

     /*   // 수수료 잔고 체크
        if (0 > balanceCheck(my, rateValue.toString())) {
        	throw new BadRequestException("예상 수수료가 보유" + vo.getRateCrncyCd() + "보다 큼니다.");
        }*/

        //  이자신청 AUTO 여부에 따라
        String procStatus = "R";
        if (vo.getAutoApproval().equals("Y")) {
            procStatus = "W";
        }

        TnUserRewardReq tn = new TnUserRewardReq();
        tn.setUsrIdx(vo.getUsrIdx());
        tn.setReqRate(vo.getSwapRate());
        tn.setSendCrncyCd(vo.getCrncyCd());
        tn.setSendCrncyType(vo.getCrncyType());
        tn.setSendCoinPrice(vo.getBidPrice());
        tn.setSendVlme(rateValue.toString());
        tn.setCorpIdx(vo.getCorpIdx());
        tn.setReqCrncyCd(vo.getCrncyCd());
        tn.setReqCrncyType(vo.getCrncyType());
        tn.setReqVlme(reqVlme.toString());
        tn.setProcStatus(procStatus);
        tn.setCorpUsrIdx(vo.getCorpUsrIdx());
        tn.setCorpAddr(vo.getCorpRewardAddr());
        tn.setUsrReceiveAddr(vo.getUsrReceiveAddr());
        tn.setTotalVlme(vo.getInput());
        //  TN_USER_REWARD_REQ 등록
        tostDao.insertTnUserRewardReq(tn);
        log.info("----------insertTnUserRewardReq OK");

        ThUserRewardReq th = new ThUserRewardReq();
        th.setReqIdx(tn.getReqIdx());
        th.setProcStatus(procStatus);

        //  TH_USER_REWARD_REQ 등록
        tostDao.insertThUserRewardReq(th);
        log.info("----------insertThUserRewardReq OK");

        vo.setLockVlme(vo.getInput());
        tostDao.updateTnUserRewardTotal(vo);
        log.info("----------updateTnUserRewardTotal OK");

        log.info("----------checkTostGetProfit OK");
        return result;
    }

    /**
     * 나의 일별 수익 리스트
     *
     * @param tostReward
     */
    public List<TostReward> myDailyRewardList(TostReward tostReward) throws BadRequestException {
        List<TostReward> result = tostDao.myDailyRewardList(tostReward);

        return result;
    }

    /**
     * 하위자 일별 수익 리스트
     *
     * @param tostReward
     */
    public List<TostReward> downDailyRewardList(TostReward tostReward) throws BadRequestException {
        List<TostReward> result = tostDao.downDailyRewardList(tostReward);

        return result;
    }

    /**
     * 하위자 총 수익 리스트
     *
     * @param tostReward
     */
    public List<TostReward> downTotalRewardList(TostReward tostReward) throws BadRequestException {
        List<TostReward> result = tostDao.downTotalRewardList(tostReward);

        return result;
    }

    /**
     * 코인별 총 누적 수익률(%)
     *
     * @param thRewardHisDay
     */
    public ThRewardHisDay getCoinRewardHisTotal(ThRewardHisDay thRewardHisDay) throws BadRequestException {
        ThRewardHisDay result = tostDao.getCoinRewardHisTotal(thRewardHisDay);

        return result;
    }

    /**
     * 개인 총 누적 수익($)
     *
     * @param tostReward
     */
    public TostReward getMyRewardTotal(TostReward tostReward) throws BadRequestException {
        TostReward result = tostDao.getMyRewardTotal(tostReward);

        return result;
    }






    /**
     * TnUser 등록 (By:excel file)
     *
     * @param file
     */
    public int insertTnUserByExcel(MultipartFile file) throws BadRequestException {

        //확장자 유효성 검사
        String originalFilename = file.getOriginalFilename();
        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1);

        if (!ext.equals("xlsx") && !ext.equals("xls")) {
            throw new BadRequestException("Excel 파일을 선택해주세요.");
        }

        List<Map<String, Object>> listMap = excelUpload.getListData(file, 1, 5);

        for (Map<String, Object> map : listMap) {
            try {
                //상위코드 확인
                TbUserModel tbUserModel = new TbUserModel();

                String encPw = PasswordUtils.encode("1");
                String upUsrId = (map.get("1").toString());
                String upRefCd = userDao.findRefCdByUsrId(upUsrId).getRefCd();
                TbUserModel upUser = userService.getRefCdChk(upRefCd);

                tbUserModel.setUsrId(map.get("0").toString());
                tbUserModel.setUsrPw(encPw);
                tbUserModel.setUsrNick(map.get("0").toString());
                tbUserModel.setUpRefCd(upRefCd);
                tbUserModel.setCorpIdx("1");

                //10자리 문자열 난수 생성
                String refCd = "";
                int refCdCheck = 1;
                do {
                    try {
                        refCd = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
                        refCdCheck = userDao.checkRecommender(refCd);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (refCdCheck != 0);
                tbUserModel.setRefCd(refCd);

                userDao.insertUser(tbUserModel);

                // get usrIdx
                String usrIdx = tbUserModel.getUsrIdx();

                //추천인 테이블 등록
                if (upUser != null)
                {
                    TnUserRefModel tnUserRefModel = new TnUserRefModel();
                    tnUserRefModel.setUsrIdx(upUser.getUsrIdx());
                    tnUserRefModel.setDnUsrIdx(usrIdx);
                    tnUserRefModel.setRegId(tbUserModel.getUsrId());
                    tnUserRefModel.setModId(tbUserModel.getUsrId());

                    userDao.insertUserRef(tnUserRefModel);
                }

                //상위 추천인 - 하위 추천인 카운트 업데이트
                upUser.setSponsorNum(upUser.getSponsorNum() + 1);
                userDao.updateUserSponsorNum(upUser);

            } catch (BadRequestException e) {
                log.error(e.getLocalizedMessage());
                throw e;
            } catch (Exception e){
                log.error(e.getLocalizedMessage());
                throw new BadRequestException("error.bad.request");
            }

        }


        return 1;
    }

    //  기생수
    public List<Lbio> lbioList() {
        String[] addr = {

        };

        String[] refCd = {

        };

        List<Lbio> result = new ArrayList<>();
        Double all = 0D;

        if (addr.length == addr.length) {

            for (int i = 0; i < addr.length; i++) {
                Lbio l = new Lbio();
                l.setCrncyCd("BIO");
                l.setCrncyType("BIO");

                l.setAddr(addr[i]);



                Map<String, Object> balanceObj = walletSvc.sch_account_bio_svc(l.getAddr(), l.getCrncyCd(), l.getCrncyType());
                String my = balanceObj.get("balance").toString();
                l.setBalance(my);

                if (!my.equals("0")) {
                    result.add(l);
                    all = all + Double.parseDouble(my);
                }
            }

        }

        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + all);
        for (Lbio l : result) {
            System.out.println(l);
        }

        return result;
    }

}
