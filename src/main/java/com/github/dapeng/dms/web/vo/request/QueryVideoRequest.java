package com.github.dapeng.dms.web.vo.request;


import lombok.Data;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:06 PM
 */
@Data
public class QueryVideoRequest {

    private String videoName;

    private DmsPageReq pageRequest;
}