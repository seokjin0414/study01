package com.company.nill.myTool.utill;

import com.wallet.kompass.front.biz.vo.code.BaseEnum;

public class EnumUtils extends org.apache.commons.lang3.EnumUtils {

    public static <E extends Enum<E>> String getLangCd(Class<E> enumClass, String name) {
        Enum<E> base = getEnum(enumClass, name);
        if (base == null) {
            return "";
        }
        try {
            BaseEnum b = (BaseEnum) base;
            return b.getLangCd();
        } catch (Exception e) {
            Log.error(e);
            return "";
        }
    }
}
