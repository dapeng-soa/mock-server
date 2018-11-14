package com.github.dapeng.dms.util;

import com.github.dapeng.dms.web.util.MockException;

import java.util.List;

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

    public static <T> void notNull(T req, String msg) {
        if (req == null) {
            throw new MockException(msg);
        }
    }


}
