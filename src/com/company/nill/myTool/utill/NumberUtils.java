package com.company.nill.myTool.utill;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Slf4j
public class NumberUtils implements Formatter<Number> {

    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(locale);
        return format.parse(text);
    }

    @Override
    public String print(Number object, Locale locale) {
        return NumberFormat.getInstance(locale).format(object);
    }

    /**
     * usd format
     * @param str
     * @return
     */
    public String numberFormat(String str) {
        if (str == null || str.equals("0E-36")) {
            return null;
        } else {
            String slc_str = strSplit(str);
            return print(Integer.parseInt(slc_str), Locale.ENGLISH);
        }
    }

    public String strSplit(String str) {
        return str.split("\\.")[0];
    }

}
