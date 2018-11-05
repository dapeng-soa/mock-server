package com.github.dapeng.dms.mvc.util;

import com.github.dapeng.dms.util.Constants;

import java.util.Optional;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-05 1:49 PM
 */
public class MockUtils {
    public static String combineMockKey(String service, String method, String version) {
        return service + Constants.KEY_SEPARATE + method + Constants.KEY_SEPARATE + version;
    }

    public static <T> T optional(Optional<T> optional, String msg) {
        if (!optional.isPresent()) {
            throw new IllegalArgumentException(msg);
        }
        return optional.get();
    }
}
