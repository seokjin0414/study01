package com.company.nill.myTool.serviceEx;

import com.google.gson.Gson;
import com.metavegas.admin.dao.AirdropDao;
import com.metavegas.admin.exception.http.ApiExecutionException;
import com.metavegas.admin.exception.http.BadRequestException;
import com.metavegas.admin.utils.ExcelUpload;
import com.metavegas.admin.utils.LegacyCall;
import com.metavegas.admin.utils.StringUtils;
import com.metavegas.admin.vo.WithdrawCndModel;
import com.metavegas.admin.vo.api.GenerateWalletRes;
import com.metavegas.admin.vo.api.WithdrawWalletReq;
import com.metavegas.admin.vo.common.PageVo;
import com.metavegas.admin.vo.excel.TnEventAirdropListExcelVo;
import com.metavegas.admin.vo.table.TnEventAirdrop;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AirdropService {

    @Value("${wallet.api.domain}")
    private String wallDomain;

    @Value("${wallet.api.wallet.withdraw.url}")
    private String withdrawUrl;

    private String resultCodeKey = "error_code";

    private final ExcelUpload excelUpload;
    private final AirdropDao airdropDao;
    private final ApiService apiService;

    /**
     * TnEventAirdrop 리스트
     *
     * @param tnEventAirdrop
     */
    public PageVo<TnEventAirdrop> tnEventAirdropList(TnEventAirdrop tnEventAirdrop) throws BadRequestException {
        PageVo<TnEventAirdrop> result = new PageVo<>();

        List<TnEventAirdrop> list = airdropDao.tnEventAirdropList(tnEventAirdrop);
        int listCnt = airdropDao.tnEventAirdropListCnt(tnEventAirdrop);

        result.setData(list);
        result.setItemsCount(listCnt);

        return result;
    }

    /**
     * TnEventAirdrop 등록 (By:excel file)
     *
     * @param file
     */
    public void insertTnEventAirdropByExcel(MultipartFile file) throws BadRequestException {

        //확장자 유효성 검사
        String originalFilename = file.getOriginalFilename();
        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1);

        if (!ext.equals("xlsx") && !ext.equals("xls")) {
            throw new BadRequestException("Excel 파일을 선택해주세요.");
        }

        List<Map<String, Object>> listMap = excelUpload.getListData(file, 1, 5);

        for (Map<String, Object> map : listMap) {
            TnEventAirdrop tnEventAirdrop = new TnEventAirdrop();
            tnEventAirdrop.setReceiveAddr(map.get("0").toString());
            tnEventAirdrop.setCrncyCd(map.get("1").toString());
            tnEventAirdrop.setCrncyType(map.get("2").toString());
            tnEventAirdrop.setVolume(map.get("3").toString());
            tnEventAirdrop.setSendAddr(map.get("4").toString());
            tnEventAirdrop.setComment(map.get("5").toString());

            String txId = StringUtils.UUID();
            tnEventAirdrop.setTxId(txId);

            airdropDao.insertTnEventAirdrop(tnEventAirdrop);
        }
    }

    /**
     * TnEventAirdrop 수정 (status = W 인것)
     *
     */
    public void updateTnEventAirdrop() throws BadRequestException {
        TnEventAirdrop tnEventAirdrop = new TnEventAirdrop();
        tnEventAirdrop.setStatusVal("W");

        List<TnEventAirdrop> list = airdropDao.tnEventAirdropList(tnEventAirdrop);

        for (TnEventAirdrop vo : list) {
            String state = vo.getWdState();
            if (state != null) {
                if (state.equals("C") || state.equals("F")) {
                    vo.setStatus(state);
                    airdropDao.updateTnEventAirdrop(vo);
                    log.info("@@@@@@@@@@@@@@@@@@@@@@@" + vo.getAirdropIdx() + "STATUS UPDATE");
                }
            }
        }

        log.info("-----------------UPDATE DONE");
    }

    /**
     * TnEventAirdropList Excel 다운
     *
     * @param tnEventAirdrop
     */
    public List<TnEventAirdropListExcelVo> getTnEventAirdropListExcel(TnEventAirdrop tnEventAirdrop) throws BadRequestException {
        List<TnEventAirdropListExcelVo> result = new ArrayList<>();
        List<TnEventAirdrop> list = airdropDao.tnEventAirdropList(tnEventAirdrop);

        for (TnEventAirdrop vo : list) {
            TnEventAirdropListExcelVo excelVo = new TnEventAirdropListExcelVo();
            excelVo.setAirdropIdx(vo.getAirdropIdx());
            excelVo.setCrncyTypeCd(vo.getCrncyType() + "/" + vo.getCrncyCd());
            excelVo.setVolume(vo.getVolume());
            excelVo.setReceiveUsrIdx(vo.getUsrIdx());
            excelVo.setReceiveUsrId(vo.getUsrId());
            excelVo.setReceiveRefCd(vo.getRefCd());
            excelVo.setReceiveAddr(vo.getReceiveAddr());
            excelVo.setTxId(vo.getTxId());
            excelVo.setExtTxId(vo.getExtTxId());
            excelVo.setSendUsrIdx(vo.getSendUsrIdx());
            excelVo.setSendAddr(vo.getSendAddr());
            excelVo.setComment(vo.getComment());
            excelVo.setStatus(vo.getStatus());
            excelVo.setRegDtm(vo.getRegDtm());
            excelVo.setModDtm(vo.getModDtm());

            String status = vo.getStatus();

            if (status != null) {
                if (status.equals("C")) {
                    excelVo.setStatus("완료");
                } else if (status.equals("W")) {
                    excelVo.setStatus("진행중");
                } else if (status.equals("R")) {
                    excelVo.setStatus("대기");
                } else if (status.equals("F")) {
                    excelVo.setStatus("실패");
                }
            }

            result.add(excelVo);
        }

        return result;
    }

    /**
     * Airdrop 진행
     *
     * @param tnEventAirdrop
     */
    public void airdrop(TnEventAirdrop tnEventAirdrop) throws BadRequestException {
        List<TnEventAirdrop> list = airdropDao.tnEventAirdropList(tnEventAirdrop);

        for (TnEventAirdrop vo : list) {
            vo.setStatus("W");
            airdropDao.updateTnEventAirdrop(vo);
            log.info("-----------------statusUpdate OK");

            WithdrawCndModel cnd = new WithdrawCndModel();
            cnd.setCurrency(vo.getCrncyCd().toLowerCase());
            cnd.setCurrencyType(vo.getCrncyType());
            cnd.setVolume(vo.getVolume());
            cnd.setMemberNo(vo.getSendUsrIdx());
            cnd.setSenderAddr(vo.getSendAddr());
            cnd.setReceiveMemberNo(vo.getUsrIdx());
            cnd.setReceiveAddr(vo.getReceiveAddr());
            cnd.setLocSendAddr(vo.getTxId());
            cnd.setAddrType("E");
            cnd.setCnfgType("wa");
            cnd.setFee("0");
            cnd.setFeeCrncy(vo.getCrncyCd().toLowerCase());
            cnd.setFeeType("SL");
            cnd.setTmpComment("AIRDROP");

            //수량 출금
            widthraw(cnd);
            log.info("-----------------airdrop OK");
        }

        log.info("-----------------airdrop DONE");
    }

    /**
     * 실패건 Airdrop 진행 (F)
     *
     * @param tnEventAirdrop
     */
    public void airdropWithF(TnEventAirdrop tnEventAirdrop) throws BadRequestException {
        List<TnEventAirdrop> list = airdropDao.tnEventAirdropList(tnEventAirdrop);

        for(TnEventAirdrop vo : list) {
            String txId = StringUtils.UUID();
            vo.setTxId(txId);

            airdropDao.updateTnEventAirdrop(vo);
            log.info("-----------------txIdUpdate OK");
        }

        airdrop(tnEventAirdrop);
    }

    /**
     * 출금하기
     *
     * @param cnd
     * @return
     */
    public GenerateWalletRes widthraw(WithdrawCndModel cnd) throws BadRequestException {

        //주소 체크
        String senderAddr = cnd.getSenderAddr();

        if (org.apache.commons.lang3.StringUtils.isEmpty(senderAddr)) {
            throw new BadRequestException("error.sender_addr.empty");
        }

        String receiveAddr = cnd.getReceiveAddr();

        if (org.apache.commons.lang3.StringUtils.isEmpty(receiveAddr)) {
            throw new BadRequestException("error.receiver_addr.empty");
        }

        //출금신청하기 생성
        String url = withdrawUrl.replace("{currency}", cnd.getCurrency());

        LegacyCall<Map<String, Object>> call = new LegacyCall<>(wallDomain + url);
        call.setMethod(HttpMethod.POST);

        Class<Map<String, Object>> clazz = (Class<Map<String, Object>>) (Class) Map.class;

        String locSendAddr = cnd.getLocSendAddr();
        if (locSendAddr == null || locSendAddr.length() == 0) locSendAddr = "";

        WithdrawWalletReq param = new WithdrawWalletReq();
        param.setCurrency(cnd.getCurrency());
        param.setCurrencyType(cnd.getCurrencyType());
        param.setVolume(cnd.getVolume());
        param.setFee(cnd.getFee());
        param.setFeeType(cnd.getFeeType());
        param.setLocSndAddr(locSendAddr);
        param.setMemberNo(cnd.getMemberNo());
        param.setSendAddr(senderAddr);
        param.setAddrType(cnd.getAddrType());
        param.setReceiveMemberNo(cnd.getReceiveMemberNo());
        param.setReceiveAddr(receiveAddr);
        param.setComment(cnd.getTmpComment());
        param.setState("R");
        param.setLocRcvAddr("");
        param.setNodeId("0");
        param.setAdminInfo("");

        Map<String, Object> res = call.send(param, clazz);

        GenerateWalletRes walletRes = null;

        if ("0".equals(res.get(resultCodeKey))) {
            Gson gson = new Gson();
            String resultJSon = gson.toJson(res.get("result"));
            walletRes = gson.fromJson(resultJSon, GenerateWalletRes.class);

        } else {
            log.info("[withdraw error]:" + res);

            String result_code = (String) res.get(resultCodeKey);

            String err_cd = "";

            //API 명세와 다른 요청
            if ("90001".equals(result_code) || "90006".equals(result_code)) {
                err_cd = "error.withraw.data_error";

            } else if ("10003".equals(result_code)) {
                //잔액 부족
                err_cd = "error.show.me.the.money";
            } else if ("20001".equals(result_code)) {
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



}