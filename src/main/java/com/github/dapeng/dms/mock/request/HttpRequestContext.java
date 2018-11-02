package com.github.dapeng.dms.mock.request;

import lombok.Data;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 2:07 PM
 */
@Data
public class HttpRequestContext {
    /**
     * serviceName
     */
    private final String service;
    /**
     * methodName
     */
    private final String method;
    /**
     * service version number
     */
    private final String version;
    /**
     * request path
     */
    private final String path;
    /**
     * request parameter
     */
    private final String parameter;
    /**
     * httpMethod GET、POST、PUT etc.
     */
    private final String httpMethod;
    /**
     * request cookie array
     */
    private final Cookie[] cookies;
    /**
     * servlet request
     */
    private HttpServletRequest request;

    public HttpRequestContext(String service, String method, String version,
                              String parameter, HttpServletRequest request) {
        this.service = service;
        this.method = method;
        this.version = version;
        this.parameter = parameter;
        this.request = request;
        // calc
        this.httpMethod = request.getMethod();
        this.path = request.getServletPath();
        this.cookies = request.getCookies();
    }
}






