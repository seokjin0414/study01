package com.company.nill.myTool.utill;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static long diffDays(String format, String from, String to) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        Date firstDate = sdf.parse(from);
        Date secondDate = sdf.parse(to);

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }


    public static long diffHours(String format, String from, String to) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        Date firstDate = sdf.parse(from);
        Date secondDate = sdf.parse(to);

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        return TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static long diffMinutes(String format, String from, String to) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        Date firstDate = sdf.parse(from);
        Date secondDate = sdf.parse(to);

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        return TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
    public static long diffSecs(String format, String from, String to) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        Date firstDate = sdf.parse(from);
        Date secondDate = sdf.parse(to);

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        return TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.SECONDS);
    }

    public static String LocalDateTimeString(LocalDateTime dateTime,String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }
    
    public static String getNowDateString(String format) {
    	LocalDateTime currentDateTime = LocalDateTime.now();
        return getNowDateString(currentDateTime , format);
    }
    
    public static String getNowDateString(LocalDateTime currentDateTime , String format) {
        return DateUtils.LocalDateTimeString(currentDateTime , format);
    }
    
    public static LocalDateTime getLocalDate() {
    	return LocalDateTime.now();
    }

    public static String now(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = Calendar.getInstance().getTime();
        return sdf.format(date);
    }

    public static String MonthAdd(int amount , String format ) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Date date = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(Calendar.MONTH , amount) ;
        return sdf.format(cal.getTime());
    }
}
