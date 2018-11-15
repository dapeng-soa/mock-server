package com.github.dapeng.dms.util;

import com.github.dapeng.dms.web.util.MockException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-12 4:06 PM
 */
public class CommonUtil {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YY-MM-dd HH:mm");

    public static <T> void notNull(T req) {
        if (req == null) {
            throw new IllegalArgumentException("parameter could not be null");
        }
    }

    public static <T> void notNull(T req, String msg) {
        if (req == null) {
            throw new MockException(msg);
        }
    }

    public static <T> T notNullRet(T req, String msg) {
        if (req == null) {
            throw new MockException(msg);
        }
        return req;
    }

    public static String combine(String service, String version) {
        return service + ":" + version;
    }

    public static String combineMeta(String namespace, String name) {
        return namespace + "." + name;
    }


    public static String convertName(String service) {
        return service.substring(service.lastIndexOf(".") + 1);
    }


    public static String longToStringDate(long timeStamp) {
        Instant instant = Instant.ofEpochMilli(timeStamp);
        ZoneId zone = ZoneId.of("Asia/Shanghai");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.format(formatter);
    }

}
