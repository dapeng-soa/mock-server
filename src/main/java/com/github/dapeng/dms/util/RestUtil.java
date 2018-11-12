package com.github.dapeng.dms.util;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-12 4:06 PM
 */
public class RestUtil {
    public static <T> void notNull(T req) {
        if (req == null) {
            throw new IllegalArgumentException("parameter could not be null");
        }
    }
}
