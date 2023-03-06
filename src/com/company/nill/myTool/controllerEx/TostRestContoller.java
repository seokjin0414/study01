package com.company.nill.myTool.controllerEx;

import com.bind.front.biz.controller.BaseRestController;
import com.bind.front.biz.dao.TostDao;
import com.bind.front.biz.dao.UserDao;
import com.bind.front.biz.svc.SynchronizedService;
import com.bind.front.biz.svc.TostServise;
import com.bind.front.biz.vo.Authorization;
import com.bind.front.biz.vo.SessionUserVo;
import com.bind.front.biz.vo.model.*;
import com.bind.front.exception.http.BadRequestException;
import com.bind.front.utils.SessionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TostRestContoller extends BaseRestController {

    private final UserDao userDao;

    private final TostDao tostDao;

    private final TostServise tostServise;

    private final SynchronizedService synchronizedService;

    /**
     * TOST 리스트
     *
     */
    @PostMapping(value = "/tostList")
    public ResponseEntity<?> tosting() throws BadRequestException {
        List<Tost> result = tostServise.mainTostList();

        return success(result);
    }

    /**
     * TOSTING 상세
     *
     * @param tost
     */
    @PostMapping(value = "/tostingDetail")
    @Authorization(role = Authorization.Role.LOGIN)
    public ResponseEntity<?> tosting(@RequestBody Tost tost, HttpServletRequest request) throws BadRequestException {
        SessionUserVo sessionUser = SessionUtil.getSessionUser(request);

        tost.setUsrIdx(sessionUser.getUsrIdx());


        Tost result = tostServise.tosting(tost);

        return success(result);
    }

    /**
     * TOSTING 요청
     *
     * @param tost
     */
    @PostMapping(value = "/tostingOrder")
    @Authorization(role = Authorization.Role.LOGIN)
    public ResponseEntity<?> fundOrder(@RequestBody Tost tost, HttpServletRequest request) throws BadRequestException {
        int result = 0;

        SessionUserVo sessionUser = SessionUtil.getSessionUser(request);
        tost.setUsrIdx(sessionUser.getUsrIdx());

        result = synchronizedService.checkTostingOrder(tost);

        if (result < 0) {
            throw new BadRequestException("TOSTING 에 실패했습니다.");
        }

        return success("OK");
    }

    /**
     * Reward Coin 선택
     *
     * @param tost
     */
    @PostMapping(value = "/updateTnUserSelRewardCoin")
    @Authorization(role = Authorization.Role.LOGIN)
    public ResponseEntity<?> updateTnUserSelRewardCoin(@RequestBody Tost tost, HttpServletRequest request) throws BadRequestException {
        SessionUserVo sessionUser = SessionUtil.getSessionUser(request);
        tost.setUsrIdx(sessionUser.getUsrIdx());

        tostServise.updateTnUserSelRewardCoin(tost);

        return success("OK");
    }

    /**
     * Application Details 리스트 (MY PAGE)
     *
     */
    @PostMapping(value = "/applicationDetailsList")
    @Authorization(role = Authorization.Role.LOGIN)
    public ResponseEntity<?> applicationDetailsList(HttpServletRequest request) throws BadRequestException {
        SessionUserVo sessionUser = SessionUtil.getSessionUser(request);

        MyTosting myTosting = new MyTosting();
        myTosting.setUsrIdx(sessionUser.getUsrIdx());

        List<MyTosting> result = tostServise.applicationDetailsList(myTosting);

        return success(result);
    }

    /**
     * tnUserSelRewardCoinList TEST
     *
     */
    @PostMapping(value = "/tnUserSelRewardCoinList")
    @Authorization(role = Authorization.Role.LOGIN)
    public ResponseEntity<?> tnUserSelRewardCoinList(HttpServletRequest request) throws BadRequestException {
        SessionUserVo sessionUser = SessionUtil.getSessionUser(request);

        Wallet wallet = new Wallet();
        wallet.setMemberNo(sessionUser.getUsrIdx());

        Map<String, List<TnUserSelRewardCoin>> result = tostServise.tnUserSelRewardCoinList(wallet);

        return success(result);
    }

    /**
     * TOST GET PROFIT INFO
     *
     * @param
     */
    @PostMapping(value = "/tostGetProfitInfo")
    @Authorization(role = Authorization.Role.LOGIN)
    public ResponseEntity<?> tostGetProfitInfo(@RequestBody TnUserRewardTotal tnUserRewardTotal, HttpServletRequest request) throws BadRequestException {
        SessionUserVo sessionUser = SessionUtil.getSessionUser(request);
        tnUserRewardTotal.setUsrIdx(sessionUser.getUsrIdx());

        List<TnUserRewardTotal> result = tostServise.tostGetProfitInfo(tnUserRewardTotal);

        return success(result);
    }

    /**
     * TOST GET PROFIT 요청
     *
     * @param tnUserRewardTotal
     */
    @PostMapping(value = "/tostGetProfitOrder")
    @Authorization(role = Authorization.Role.LOGIN)
    public ResponseEntity<?> tostGetProfitOrder(@RequestBody TnUserRewardTotal tnUserRewardTotal, HttpServletRequest request) throws BadRequestException {
        int result = 0;

        SessionUserVo sessionUser = SessionUtil.getSessionUser(request);
        tnUserRewardTotal.setUsrIdx(sessionUser.getUsrIdx());

        result = synchronizedService.checkTostGetProfit(tnUserRewardTotal);

        if (result < 0) {
            throw new BadRequestException("이자지급 신청에 실패했습니다.");
        }

        return success("OK");
    }

    /**
     * DAILY PROFIT
     *
     * @param tostReward
     */
    @PostMapping(value = "/dailyProfit")
    @Authorization(role = Authorization.Role.LOGIN)
    public ResponseEntity<?> tostDailyProfit(@RequestBody TostReward tostReward, HttpServletRequest request) throws BadRequestException {
        SessionUserVo sessionUser = SessionUtil.getSessionUser(request);
        tostReward.setUsrIdx(sessionUser.getUsrIdx());

        Map<String, List<TostReward>> result = new HashMap<>();
        result.put("myDaily", tostServise.myDailyRewardList(tostReward));
        result.put("downDaily", tostServise.downDailyRewardList(tostReward));
        result.put("downTotal", tostServise.downTotalRewardList(tostReward));

        return success(result);
    }

    /**
     * 코인별 총 누적 수익률(%)
     *
     * @param thRewardHisDay
     */
    @PostMapping(value = "/getCoinRewardHisTotal")
    @Authorization(role = Authorization.Role.ANONY)
    public ResponseEntity<?> getCoinRewardHisTotal(@RequestBody ThRewardHisDay thRewardHisDay) throws BadRequestException {
        return success(tostServise.getCoinRewardHisTotal(thRewardHisDay));
    }

    /**
     * 개인 총 누적 수익($)
     *
     */
    @PostMapping(value = "/getMyRewardTotal")
    @Authorization(role = Authorization.Role.ANONY)
    public ResponseEntity<?> getMyRewardTotal(HttpServletRequest request) throws BadRequestException {
        TostReward result = new TostReward();
        SessionUserVo sessionUser = SessionUtil.getSessionUser(request);

        if(sessionUser != null) {
            result.setUsrIdx(sessionUser.getUsrIdx());
            result= tostServise.getMyRewardTotal(result);

            if(result != null) {
                return success(result);
            }
        }

        return success(null);
    }





    /**
     * TnUser 등록 (By:excel file)
     *
     * @param file
     *
     */
    @PostMapping(value = "/insertTnUserByExcel")
    public ResponseEntity<?> insertTnUserByExcel(MultipartFile file) {

        tostServise.insertTnUserByExcel(file);

        return success("OK");
    }
//
//    /**
//     * TN_USER_STKINFO 일괄 등록
//     *
//     *
//     */
//    @PostMapping(value = "/insertTnUserStk")
//    public ResponseEntity<?> insertTnUserStk() {
//        List<TbUserModel> list = userDao.getUsersOfTest();
//
//        for (TbUserModel user : list) {
//            TnUserStkinfo vo = new TnUserStkinfo();
//            vo.setStkIdx("1");
//            vo.setUsrIdx(user.getUsrIdx());
//            vo.setCoinPrice("1");
//            vo.setBalance("10000");
//            vo.setStkVlme("10000");
//
//            tostDao.insertTnUserStkinfo(vo);
//        }
//
//        return success("OK");
//    }

    //  기생수
    @PostMapping(value = "/lbioList")
    public ResponseEntity<?> lbioList() throws BadRequestException {

        List<Lbio> result = tostServise.lbioList();

        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + result.size());
        return success(result);
    }


}

