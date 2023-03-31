package com.company.nill.myTool.serviceEx;

import com.bind.front.biz.dao.Swap2Dao;
import com.bind.front.biz.dao.WalletDao;
import com.bind.front.biz.vo.dto.swap.GetSwap;
import com.bind.front.biz.vo.dto.swap.GetSwapWallet;
import com.bind.front.biz.vo.dto.swap.GetWalletImage;
import com.bind.front.biz.vo.model.*;
import com.bind.front.exception.http.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SwapService2 {
    private Logger L = LoggerFactory.getLogger(this.getClass());

    private final MyInfoSvc myInfoSvc;
    private final WalletSvc walletSvc;
    private final WalletDao walletDao;
    private final Swap2Dao swapDao;


    //개인 출금 대기건 조회
    private void countByCoinSwap(String usrIdx) throws BadRequestException {

        Wallet wallet = new Wallet();
        wallet.setMemberNo(usrIdx);

        int cnt = walletDao.countByCoinSwap2(wallet);

        if(cnt > 0) {
            throw new BadRequestException("error.already.swap.request");
        }
    }

    /**
     * 스왑 생성
     * @param swap
     * @return
     * @throws BadRequestException
     */
    public ThCoinSwap2 getSwap(GetSwap swap) throws BadRequestException {// bad user check

        ThCoinSwap2 thCoinSwap = swapDao.getSwap(swap);


        return thCoinSwap;
    }

    public String getSwapImage(GetWalletImage swap) throws BadRequestException {// bad user check

        String image = swapDao.getWalletImage(swap);


        return image;
    }

    /**
     * 스왑 정보
     * @param getSwapWallet
     */
    public ThCoinSwap2 getSwapWallet(GetSwapWallet getSwapWallet) throws BadRequestException {// bad user check
        ThCoinSwap2 result = swapDao.getSwapWallet(getSwapWallet);
        log.info("@@@@@@@@@@@@@@getSwapWallet");
        Map<String, Object> balance_obj = walletSvc.sch_account_bio_svc(result.getFromAddress(), result.getFromCrypto(), result.getFromAsset());
        log.info("@@@@@@@@@@@@@@sch_account_bio_svc");
        result.setBalance(balance_obj.get("balance").toString());


        return result;
    }

    /**
     * 스왑 ORDER
     */
    public String orderSwap(ThCoinSwap2 vo) throws BadRequestException {// bad user check
        TbUserModel param = new TbUserModel();
        param.setUsrIdx(vo.getUsrIdx());
        int bad_user_cnt = myInfoSvc.getBadUserCnt(param);

        if (bad_user_cnt > 0) {
            L.debug("withdraw bad user");
            throw new BadRequestException("error.bad.user.call.cs");
        }

        @SuppressWarnings("DuplicatedCode")
        TbUserModel tbUserModel = new TbUserModel();
        tbUserModel.setUsrIdx(vo.getUsrIdx());

        @SuppressWarnings("DuplicatedCode")
        TbUserModel myInfo = myInfoSvc.selectMyInfoUsrId(tbUserModel);

        //noinspection DuplicatedCode
        if (myInfo.getWithdrawYn().equals("N")) {
            throw new BadRequestException("error.wallet_checked");
        }

        this.countByCoinSwap(vo.getUsrIdx());

        // 비번 몇번 틀렸는지 확인하기
        String smplError = myInfo.getSmplError();
        int errorCnt = Integer.parseInt(smplError);

        if (errorCnt >= 10) {
            throw new BadRequestException("error.join.notmatch.smpl2_" + Integer.toString(errorCnt) + "_10");
        }

        GetSwapWallet getSwapWallet = new GetSwapWallet();
        getSwapWallet.setUsrIdx(vo.getUsrIdx());
        getSwapWallet.setFromCrncyCd(vo.getFromCrypto());
        getSwapWallet.setToCrncyCd(vo.getToCrypto());


        ThCoinSwap2 tcs2 = getSwapWallet(getSwapWallet);
        tcs2.setId(String.valueOf(System.currentTimeMillis()));
        tcs2.setFromValue(vo.getFromValue());
        tcs2.setFromFee("0");
        tcs2.setToFee("0");
        tcs2.setSwapFee("0");

        BigDecimal fromPriceUsd = new BigDecimal(tcs2.getFromValue()).multiply(new BigDecimal(tcs2.getFromUnitPriceUsd()));
        BigDecimal toValue = fromPriceUsd.divide(new BigDecimal(tcs2.getToUnitPriceUsd()), 8, RoundingMode.HALF_EVEN);

        tcs2.setFromPriceUsd(fromPriceUsd.toString());
        tcs2.setToValue(toValue.toString());
        tcs2.setToPriceUsd(fromPriceUsd.toString());


        swapDao.insertTnCoinSwap2(tcs2);
        String resurt = tcs2.getIdx();

        return resurt;
    }

}
