package com.company.nill.myTool.utill;

import com.withdraw.batch.comm.err.ApplicationException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;

import static com.withdraw.batch.comm.StringUtils.compare;

public class OrderUtil {


    public static void throwApplicationException(Logger logger, String errMsg, String code, HttpStatus statusCode) throws ApplicationException {
        throwApplicationException(logger, errMsg, code, "", statusCode);
    }

    public static void throwApplicationException(Logger logger, String errMsg, String code, String exceptionMsg, HttpStatus statusCode) throws ApplicationException {
        logger.error(errMsg);
        if (exceptionMsg.isEmpty()) {
            throw new ApplicationException(code, statusCode);
        } else {
            throw new ApplicationException(code, exceptionMsg, statusCode);
        }
    }



    public static boolean isNagativeBigDecimal(String param) {
        boolean result = false;
        String value = param;

        if (null == value || value.isEmpty()) value = "0";

        if (compare("0",value) >= 0) {
            result = true;
        }
        return result;
    }
}
