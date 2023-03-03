package com.company.nill.myTool.utill;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class TemplateExcelDownload {

    public static <T> void createXlsx(List<T> pojoObjectList, String filename, String voType, HttpServletResponse response) {

        Workbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = (SXSSFSheet ) workbook.createSheet();

        try {
        	if(pojoObjectList.size()>0){
        		Row row = sheet.createRow(0);
            	RowFilledWithPojoHeader(pojoObjectList.get(0), row , voType);

            	for (int i = 0; i < pojoObjectList.size(); i++) {
                	row = sheet.createRow(i+1);
                	RowFilledWithPojoData(pojoObjectList.get(i), row , workbook);
            	}
            }else{
            	Row row = sheet.createRow(0);
            	row.createCell(0).setCellValue("데이터가 존재 하지 않습니다.");
            }
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.setCharacterEncoding("UTF-8");
            workbook.write(response.getOutputStream());

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | IOException e) {
            throw new RuntimeException(e);
        } finally {
        	 try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }

    private static Row RowFilledWithPojoHeader(Object pojoObject, Row row, String voType) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Field[] fields = pojoObject.getClass().getDeclaredFields();
        int fieldLength = fields.length;

        int j = 0;
        for (int i = 0; i < fieldLength; i++) {
            if(!"serialVersionUID".equals(fields[i].getName())) {

                String cellValue = "";
                cellValue = replaceName(fields[i].getName());


               	row.createCell(j).setCellValue(cellValue);
               	j++;
            }
        }

        return row;
    }

    private static Row RowFilledWithPojoData(Object pojoObject, Row row, Workbook workbook) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Field[] fields = pojoObject.getClass().getDeclaredFields();
        int fieldLength = fields.length;
        int j = 0;
        for (int i = 0; i < fieldLength; i++) {
            if(!"serialVersionUID".equals(fields[i].getName())) {
                Method method = pojoObject.getClass().getMethod("get" + fields[i].getName().substring(0, 1).toUpperCase() + fields[i].getName().substring(1));
                String cellValue = method.invoke(pojoObject) == null ? "" : method.invoke(pojoObject).toString();

                if("sbscrptnNum".equals(fields[i].getName())){
//                    CellStyle style = workbook.createCellStyle();
//                    Font font = workbook.createFont();
//                    font.setColor(IndexedColors.RED.getIndex());
//                    style.setFont(font);
//                    RichTextString rich = workbook.getCreationHelper().createRichTextString(cellValue);
//                    rich.applyFont(0,1, font);
//                    Cell cell =row.createCell(i);
//                    cell.setCellStyle(style);
//                    cell.setCellValue(rich);

                    //CellStyle style = workbook.createCellStyle();
                    Font font = workbook.createFont();
                    font.setColor(IndexedColors.RED.getIndex());
                    XSSFRichTextString rich =new XSSFRichTextString(cellValue);
                    if(cellValue != null && cellValue != ""){
                        rich.applyFont(0,5, font);
                    }

                    Cell cell =row.createCell(i);
                    //cell.setCellStyle(style);
                    cell.setCellValue(rich);

                }else{
                    row.createCell(j).setCellValue(replaceCell(cellValue));
                }

                j++;
            }
        }
        return row;
    }

    private static String replaceName(String column){
        String retval ="";
        if("usrNm".equals(column)) {
            retval = "이름";
        } else if("no".equals(column)){
            retval = "번호";
        } else if("usrId".equals(column)){
            retval = "회원ID";
        } else if("pntBal".equals(column) || "point".equals(column)){
            retval = "포인트";
        } else if("regDtm".equals(column)){
            retval = "등록일";
        } else if("hisTypeNm".equals(column)){
            retval = "구분";
        } else if("admMemo".equals(column)){
            retval = "메모";
        } else if("locCode".equals(column)){
            retval = "멘토 코드";
        } else if("locUsrCnt".equals(column)){
            retval = "멘토 회원";
        } else if("tradeUsdt".equals(column)){
            retval = "거래금액(USDT)";
        } else if("feeUsdt".equals(column)){
            retval = "수수료(USDT)";
        } else if("aplctnCnt".equals(column)){
            retval = "구매신청(USDT)";
        } else if("aplctnDt".equals(column)){
            retval = "날짜";
        } else if("usrIdx".equals(column)){
            retval = "회원 번호";
        } else if("hpCntryCd".equals(column)){
            retval = "국가번호";
        } else if("txId".equals(column)){
            retval = "TXID";
        } else if("gbn".equals(column)){
            retval = "거래";
        } else if("addrType".equals(column)){
            retval = "구분";
        } else if("crncyCd".equals(column)){
            retval = "코인";
        } else if("volume".equals(column)){
            retval = "거래량";
        } else if("sender".equals(column)){
            retval = "보낸사람";
        } else if("receiver".equals(column)){
            retval = "받는사람";
        } else if("stateNm".equals(column)){
            retval = "상태";
        } else if("mtchTxid".equals(column)){
            retval = "매칭ID";
        } else if("lvl".equals(column)){
            retval = "아이템";
        } else if("hotelIssueCd".equals(column)){
            retval = "발행코드";
        } else if("buyUsrId".equals(column)){
            retval = "구매회원";
        } else if("saleUsrId".equals(column)){
            retval = "판매회원";
        } else if("buyStusCd".equals(column)){
            retval = "상태";
        } else if("dealPrice".equals(column)){
            retval = "거래가격";
        } else if("dealStrtDt".equals(column)){
            retval = "거래시작일";
        } else if("dealEndDtm".equals(column)){
            retval = "거래종료일시";
        } else if("dealDt".equals(column)){
            retval = "구매날짜";
        } else if("buyPrice".equals(column)){
            retval = "구매(USDT)";
        } else if("salePrice".equals(column)){
            retval = "판매(USDT)";
        } else if("prftMoney".equals(column)){
            retval = "순수익(USDT)";
        } else if("prftRt".equals(column)){
            retval = "수익률(%)";
        } else if("hpNum".equals(column)){
            retval = "휴대전화";
        } else if("rnum".equals(column)){
            retval = "순위";
        } else if("feeIkomp".equals(column)){
            retval = "수수료(IKOMP)";
        } else if("feeCnv".equals(column)){
            retval = "수수료(CNV)";
        } else if("usrGrdNm".equals(column)){
            retval = "등급";
        } else if("hotelk01".equals(column)){
            retval = "빌딩카드";
        } else if("hotelApplyTot".equals(column)){
            retval = "합계";
        } else if("aplctnTime".equals(column)){
            retval = "시간";
        } else if("pointIdx".equals(column)){
            retval = "거래번호";
        } else if("admConfm".equals(column)){
            retval = "관리";
        } else if("buyHpCntryCd".equals(column)){
            retval = "국가번호";
        } else if("buyHpNum".equals(column)){
            retval = "휴대전화";
        } else if("payCnt".equals(column)){
            retval = "구매 개수";
        } else if("payPrice".equals(column)){
            retval = "구매 금액(USDT)";
        } else if("unPayCnt".equals(column)){
            retval = "미구매 개수";
        } else if("unPayPrice".equals(column)){
            retval = "미구매 금액(USDT)";
        } else if("usrNIck".equals(column)){
            retval = "닉네임";
        } else if("twitterIdx".equals(column)){
            retval = "twitterIdx";
        } else if("twitterNm".equals(column)){
            retval = "twitter 이름";
        } else if("plNm".equals(column)){
            retval = "POOL";
        } else if("stkVlme".equals(column)){
            retval = "스테이킹 수량";
        } else if("totMnrVlme".equals(column)){
            retval = "총누적스테이킹";
        } else if("nftVlme".equals(column)){
            retval = "NFT 수량";
        } else if("regDt".equals(column)){
            retval = "가입일자";
        } else if("unlockDt".equals(column)){
            retval = "언락일자";
        } else if("withdrawVlme".equals(column)){
            retval = "출금가능금액";
        } else if("mnrVlme".equals(column)){
            retval = "이자금액";
        } else if("timeStartDt".equals(column)){
            retval = "스테이킹시작일";
        } else if("unstkAvblVlme".equals(column)){
            retval = "언스테이킹 가능 수량";
        } else if("stkDt".equals(column)){
            retval = "스테이킹일자";
        } else if("stkStatCd".equals(column)){
            retval = "스테이킹상태";
        } else if("mnrTpCd".equals(column)){
            retval = "지급방식";
        } else if("hvstStatCd".equals(column)){
            retval = "채굴상태";
        } else if("mnrDt".equals(column)){
            retval = "채굴일자";
        } else if("hvstAvlDt".equals(column)){
            retval = "수확가능일자";
        } else if("addrMetamask".equals(column)){
            retval = "메타마스크주소";
        } else if("moveDtm".equals(column)){
            retval = "이관신청일자";
        } else if("nftIdx".equals(column)){
            retval = "NFT IDX";
        } else if("email".equals(column)){
        	retval = "이메일 주소";
        } else if("reqPoint".equals(column)){
        	retval = "지급수량";
        } else if("taxRate".equals(column)){
        	retval = "적용 세율";
        } else if("procStatusNm".equals(column)){
        	retval = "신청상태";
        } else if("reqDt".equals(column)){
        	retval = "신청일";
        } else if("sendDtm".equals(column)){
        	retval = "지급일";
        } else if("modId".equals(column)){
        	retval = "처리자ID";
        } else if("korNationNm".equals(column)){
        	retval = "국가";
        } else if("dividend".equals(column)){
        	retval = "종류";
        } else if("orgUsrId".equals(column)){
        	retval = "유저아이디";
        } else if("batchReqPoint".equals(column)){
        	retval = "지급수량";
        } else if("crncyTypeCd".equals(column)){
        	retval = "통화구분/코드";
        } else if("idx".equals(column)){
        	retval = "IDX";
        } else if("pointDeductionPoint".equals(column)){
        	retval = "신청금액/공제후금액";
        } else if("tempWithdrawStatus".equals(column)){
        	retval = "출금상태";
        } else {
            retval = column;
        }

        return retval;
    }

    private static String replaceCell(String column) {

        return column;
    }

    public static <T> void createMapXlsx(String filename, Map<String, List> cmap, HttpServletResponse response) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        try {
            filename = URLEncoder.encode(filename,"UTF-8");

            List<String> title_list = (List<String>) cmap.get("title_list"); // 제목
            List<String> title_list_coll = (List<String>) cmap.get("title_list_coll"); // key
            List<Map> list = (List<Map>) cmap.get("list"); // 내용

            // create a new Excel sheet
            SXSSFSheet sheet = workbook.createSheet("sheet1");
            sheet.setDefaultColumnWidth(30);

            // create header row
            Row headerRow = sheet.createRow(0);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

            // 쎌 생성
            SXSSFCell cell;
            // 헤더 정보 구성
            for (int i=0; i < title_list.size(); i++) {
                cell = (SXSSFCell) headerRow.createCell(i);
                cell.setCellStyle(headerStyle);
                cell.setCellValue(title_list.get(i));
            }

            // 내용
            String item;
            Map hm;
            for(int rowIdx=0; rowIdx < list.size(); rowIdx++) {
                hm = list.get(rowIdx);
                // 행 생성
                headerRow = sheet.createRow(rowIdx+1);

                //사이즈 조정
                for (int i=0; i < title_list.size(); i++) {
                    item = title_list_coll.get(i);
                    cell = (SXSSFCell) headerRow.createCell(i);
                    cell.setCellValue((hm.get(item)==null? "": String.valueOf(hm.get(item))));

                    int tmpsizecol = sheet.getColumnWidth(i);
                    sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 400);
                    if (tmpsizecol > sheet.getColumnWidth(i)) {
                        sheet.setColumnWidth(i, tmpsizecol);
                    }
                }
            }

            response.setContentType("application/ms-excel; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);

            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
