package com.company.nill.myTool.utill;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HotelUtils {

    public static String addDate(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, day);
        return sdf.format(c.getTime());
    }

    public static String addDate(String day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, NumberUtils.toInt(day));
        return sdf.format(c.getTime());
    }

    public static String price(String price, String rt) {
        float fprice = NumberUtils.toFloat(price);
        float fRt = NumberUtils.toFloat(rt);
        float rst = fprice + (fprice * (fRt / 100));
        return String.format("%.2f", rst);
    }

}
