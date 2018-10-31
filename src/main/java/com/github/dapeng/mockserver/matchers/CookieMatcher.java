package com.github.dapeng.mockserver.matchers;


import com.github.dapeng.mockserver.request.HttpRequestContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;


/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 2:31 PM
 */
@Slf4j
public class CookieMatcher implements Matcher<Cookie> {
    private final Cookie[] expectedCookies;

    public CookieMatcher(Cookie[] cookies) {
        this.expectedCookies = cookies;
    }

    @Override
    public boolean matches(HttpRequestContext context, Cookie cookie) {
        Cookie[] actualValues = context.getCookies();
        boolean result = false;
        if (this.expectedCookies == null || this.expectedCookies.length == 0) {
            result = true;
//        } else if () {
//            result = true;
        } else {
            log.trace("Map [{}] is not a subset of {}", this.expectedCookies, cookie);
        }
        return result;
    }
}
