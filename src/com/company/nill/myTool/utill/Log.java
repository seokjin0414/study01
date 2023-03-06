package com.company.nill.myTool.utill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

    static Logger L = LoggerFactory.getLogger(Log.class);

    private static final String format = "[%s, %s]:[%s] %s";
    public static void info(Exception e) {info(e,e.getLocalizedMessage());}
    public static void info(Exception e,String msg) {
        String logStr = logstr(e, msg);
        L.info(logStr);
    }


    public static void error(Exception e) {error(e,e.getLocalizedMessage());}
    public static void error(Exception e,String msg) {
        String logStr = logstr(e, msg);
        L.error(logStr);
    }

    public static void warn(Exception e) {warn(e,e.getLocalizedMessage());}
    public static void warn(Exception e,String msg) {
        String logStr = logstr(e, msg);
        L.warn(logStr);
    }

    public static void debug(Exception e) {debug(e,e.getLocalizedMessage());}
    public static void debug(Exception e,String msg) {
        String logStr = logstr(e, msg);
        L.debug(logStr);
    }

    private static String logstr(Exception e,String msg) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        String callerClass = ste.getClassName();
        String callerMethod = ste.getMethodName();
        if (e != null) {
            return String.format(format, callerClass, callerMethod, msg, e.getLocalizedMessage());
        } else {
            return String.format(format, callerClass, callerMethod, msg, "");
        }
    }
}
