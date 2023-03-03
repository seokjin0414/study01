package com.company.nill.myTool.utill;

import com.lazy.admin.vo.common.CodeVo;

import java.util.List;

public class CodeUtils {

    private final static String PACKAGE = "com.lazy.admin.vo.code";

    public static String getCodeName(String cdGrp, String cd) {
        List<CodeVo> codeVos = getCodeList(cdGrp);
        for (CodeVo vo : codeVos) {
            if (vo.getCode().equals(cd)) {
                return vo.getCodeNm();
            }
        }
        return "";
    }

    public static List<CodeVo> getCodeList(String grp) {
        return ClassUtils.getEnumCode(PACKAGE + "." + grp);

    }

}
