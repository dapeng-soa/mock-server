package com.github.dapeng.mockserver.matchers;

import com.github.dapeng.mockserver.request.RequestContext;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 9:31 AM
 */
public interface Matcher<T> {
    //
    boolean matches(RequestContext context, T t);
}