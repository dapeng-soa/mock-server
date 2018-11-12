package com.github.dapeng.dms.web.util;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-12 5:41 PM
 */
public class MockException extends RuntimeException {

    public MockException() {
    }

    public MockException(String message) {
        super(message);
    }

    public MockException(String message, Throwable cause) {
        super(message, cause);
    }

    public MockException(Throwable cause) {
        super(cause);
    }

    public MockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
