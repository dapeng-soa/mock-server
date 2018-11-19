package com.github.dapeng.dms.web.vo.response;


import lombok.Data;

/**
 * list 服务下的所有方法的请求
 *
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:06 PM
 */
@Data
public class ApiSiteResponse {

    private String responseType;


    private String result;


    public ApiSiteResponse(String responseType, String result) {
        this.responseType = responseType;
        this.result = result;
    }
}
