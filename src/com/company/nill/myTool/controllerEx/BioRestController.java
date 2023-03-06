package com.company.nill.myTool.controllerEx;

import com.metavegas.admin.controller.BaseRestController;
import com.metavegas.admin.service.AirdropService;
import com.metavegas.admin.service.BioService;
import com.metavegas.admin.service.UserService;
import com.metavegas.admin.utils.DateUtils;
import com.metavegas.admin.utils.TemplateExcelDownload;
import com.metavegas.admin.vo.common.PageVo;
import com.metavegas.admin.vo.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "")
public class BioRestController extends BaseRestController {

    @Autowired
    BioService bioService;

    @Autowired
    UserService userService;

    @Autowired
    AirdropService airdropService;

    @PostMapping(value = "/salesList")
    public ResponseEntity<?> salesList(@ModelAttribute SalesVo vo) {

        //매출내역조회
        vo.setMemberNo("900");
        vo.setFeeType("S");
        List<SalesVo> list = bioService.salesList(vo);

        return success(list);
    }

    /**
     * CoinSwap 내역
     * */
    @GetMapping(value = "/coinSwapList")
    public ResponseEntity<?> coinSwapList(@ModelAttribute ThCoinSwapVo vo) {
        PageVo<ThCoinSwapVo> list = bioService.selectCoinSwapList(vo);
        return paging(list);
    }

    /**
     * CoinSwapList Excel 다운
     * */
    @GetMapping(value = "/coinSwapListExcel")
    public void coinSwapListExcel(@ModelAttribute ThCoinSwapVo vo, HttpServletResponse response) {
        TemplateExcelDownload.createXlsx(bioService.getCoinSwapListExcel(vo), "coin_swap_list_" + DateUtils.LocalDateTimeString(LocalDateTime.now(),"yyyy_MM_dd")+".xlsx",null, response);
    }

    /**
     * TnEventAirdrop 리스트
     *
     * @param tnEventAirdrop
     *
     */
    @PostMapping(value = "/tnEventAirdropList")
    public ResponseEntity<?> tnEventAirdropList(@ModelAttribute TnEventAirdrop tnEventAirdrop) {
        airdropService.updateTnEventAirdrop();
        PageVo<TnEventAirdrop> result = airdropService.tnEventAirdropList(tnEventAirdrop);

        return paging(result);
    }

    /**
     * TnEventAirdropList Excel 다운
     *
     * @param tnEventAirdrop
     *
     * */
    @GetMapping(value = "/tnEventAirdropListExcel")
    public void tnEventAirdropListExcel(@ModelAttribute TnEventAirdrop tnEventAirdrop, HttpServletResponse response) {
        TemplateExcelDownload.createXlsx(airdropService.getTnEventAirdropListExcel(tnEventAirdrop), "airdrop_list_" + DateUtils.LocalDateTimeString(LocalDateTime.now(),"yyyy_MM_dd")+".xlsx",null, response);
    }

    /**
     * TnEventAirdrop 등록 (By:excel file)
     *
     * @param file
     *
     */
    @PostMapping(value = "/insertTnEventAirdropByExcel")
    public ResponseEntity<?> insertTnEventAirdropByExcel(MultipartFile file) {

        airdropService.insertTnEventAirdropByExcel(file);

        return success("OK");
    }

    /**
     * 대기건 Airdrop 진행 (status = R)
     *
     */
    @PostMapping("/airdropWithStatusW")
    public ResponseEntity<?> airdropWithStatusW() {
        TnEventAirdrop tnEventAirdrop = new TnEventAirdrop();
        tnEventAirdrop.setStatusVal("R");

        airdropService.airdrop(tnEventAirdrop);

        return (success("OK"));
    }

    /**
     * 실패건 Airdrop 진행 (status = F)
     *
     */
    @PostMapping("/airdropWithStatusF")
    public ResponseEntity<?> airdropWithStatusF() {
        TnEventAirdrop tnEventAirdrop = new TnEventAirdrop();
        tnEventAirdrop.setStatusVal("F");

        airdropService.airdropWithF(tnEventAirdrop);

        return (success("OK"));
    }

}
