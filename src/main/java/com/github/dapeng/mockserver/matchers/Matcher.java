package com.github.dapeng.mockserver.matchers;

import com.github.dapeng.mockserver.request.HttpRequestContext;


/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 9:31 AM
 */
public interface Matcher<T> {
    /**
     * @param context 请求上下文
     * @param actual       预期匹配规则
     * @return
     */
    boolean matches(HttpRequestContext context, T actual);
}