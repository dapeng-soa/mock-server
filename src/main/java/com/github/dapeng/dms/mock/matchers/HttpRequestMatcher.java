package com.github.dapeng.dms.mock.matchers;

import com.github.dapeng.dms.mock.matchers.json.JsonStringMatcher;
import com.github.dapeng.dms.mvc.entity.MockContext;
import com.github.dapeng.dms.mock.matchers.json.JsonMatcherUtils;
import com.github.dapeng.dms.mock.matchers.json.MatchType;
import com.github.dapeng.dms.mock.request.HttpRequestContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 11:37 AM
 */
@Slf4j
public class HttpRequestMatcher {
    private static final boolean logEnable = true;
    public static final String NEW_LINE = System.getProperty("line.separator");
    private final HttpRequestContext requestContext;

    private final MockContext mockContext;

    private CookieMatcher cookieMatcher = null;
    private JsonStringMatcher parameterMatcer = null;
    private RegexStringMatcher pathMatcher = null;


    public HttpRequestMatcher(HttpRequestContext requestContext,
                              MockContext mockContext) {

        this.requestContext = requestContext;
        this.mockContext = mockContext;
        //
//        withMethod(requestContext.getMethod());
        //
        withPath(requestContext.getPath());
        // expected
        withJsonParameters(mockContext.getExpectedJson());
        //
//        withBody(httpRequest.getBody());
        //
        withCookies(requestContext.getCookies());
    }


    private void withPath(String path) {
        this.pathMatcher = new RegexStringMatcher(path);
    }

    private void withJsonParameters(String expectedParameter) {
        this.parameterMatcer = new JsonStringMatcher(JsonMatcherUtils.convertJson(expectedParameter), MatchType.ONLY_MATCHING_FIELDS);
    }

    private void withCookies(Cookie[] cookies) {
        this.cookieMatcher = new CookieMatcher(cookies);
    }

    public boolean matches() {
        return matches(requestContext, mockContext);
    }

    public boolean matches(final HttpRequestContext context) {
        return matches(context, mockContext);
    }

    public boolean matches(MockContext request) {
        return matches(requestContext, request);
    }

    private boolean matches(HttpRequestContext requestContext, MockContext mockContext) {
        boolean matches = false;
        /*if (requestContext == this.requestContext) {
            matches = true;
        } else*/
        if (this.requestContext == null) {
            matches = true;
        } else {

            if (requestContext != null) {
//                boolean methodMatches = Strings.isNullOrEmpty(request.getMethod()) || matches(requestContext, parameterMatcer, mockContext);
//                boolean pathMatches = Strings.isNullOrEmpty(request.getPath().getValue()) || matches(context, pathMatcher, request.getPath());
                boolean jsonParametersMatches = matches(requestContext, parameterMatcer, requestContext.getParameter());
//                boolean bodyMatches = bodyMatches(context, request);
//                boolean headersMatch = matches(context, headerMatcher, request.getHeaders());
//                boolean cookiesMatch = matches(context, cookieMatcher, request.getCookies());

                boolean totalResult = jsonParametersMatches;
//                boolean totalResultAfterNotOperatorApplied = request.isNot() == (this.httpRequest.isNot() == (not != totalResult));

                if (logEnable) {
                    StringBuilder becauseBuilder = new StringBuilder();
//                    becauseBuilder.append("method ").append((methodMatches ? "matched" : "didn't match"));
//                    becauseBuilder.append(",").append(NEW_LINE).append("path ").append((pathMatches ? "matched" : "didn't match"));
//                    becauseBuilder.append(",").append(NEW_LINE).append("query ").append((queryStringParametersMatches ? "matched" : "didn't match"));
//                    becauseBuilder.append(",").append(NEW_LINE).append("body ").append((bodyMatches ? "matched" : "didn't match"));
//                    becauseBuilder.append(",").append(NEW_LINE).append("headers ").append((headersMatch ? "matched" : "didn't match"));
//                    becauseBuilder.append(",").append(NEW_LINE).append("cookies ").append((cookiesMatch ? "matched" : "didn't match"));
                    /*if (request.isNot()) {
                        becauseBuilder.append(",").append(NEW_LINE).append("request \'not\' operator is enabled");
                    }*/
                   /* if (this.httpRequest.isNot()) {
                        becauseBuilder.append(",").append(NEW_LINE).append("expectation's request \'not\' operator is enabled");
                    }
                    if (not) {
                        becauseBuilder.append(",").append(NEW_LINE).append("expectation's request matcher \'not\' operator is enabled");
                    }*/
//                    log.info(requestContext, "request:{}" + (totalResult ? "matched " : "didn't match ") + (this.expectation == null ? "request" : "expectation") + ":{}because:{}", request, (this.expectation == null ? this : this.expectation.clone()), becauseBuilder.toString());
                }
                matches = totalResult;
            }
        }
        return matches;
    }

    /*private boolean bodyMatches(HttpRequest context, HttpRequest request) {
        boolean bodyMatches = true;
        String bodyAsString = request.getBody() != null ? new String(request.getBody().getRawBytes(), request.getBody().getCharset(StandardCharsets.UTF_8)) : "";
        if (!bodyAsString.isEmpty()) {
            if (bodyMatcher instanceof BinaryMatcher) {
                bodyMatches = matches(context, bodyMatcher, request.getBodyAsRawBytes());
            } else {
                if (bodyMatcher instanceof ExactStringMatcher ||
                        bodyMatcher instanceof SubStringMatcher ||
                        bodyMatcher instanceof RegexStringMatcher ||
                        bodyMatcher instanceof XmlStringMatcher) {
                    bodyMatches = matches(context, bodyMatcher, string(bodyAsString));
                } else {
                    bodyMatches = matches(context, bodyMatcher, bodyAsString);
                }
            }
            if (!bodyMatches) {
                try {
                    bodyMatches = bodyDTOMatcher.equals(objectMapper.readValue(bodyAsString, BodyDTO.class));
                } catch (Throwable e) {
                    // ignore this exception as this exception would typically get thrown for "normal" HTTP requests (i.e. not clear or retrieve)
                }
            }
        }
        return bodyMatches;
    }*/

    /**
     * @param context 每次请求上下文
     * @param matcher 匹配器
     * @param actual  实际数据
     * @return 匹配成功或者失败 {@code true or false}
     */
    private <T> boolean matches(HttpRequestContext context, Matcher<T> matcher, T actual) {
        boolean result = false;

        if (matcher == null) {
            result = true;
        } else if (matcher.matches(context, actual)) {
            result = true;
        }
        return result;
    }
}
