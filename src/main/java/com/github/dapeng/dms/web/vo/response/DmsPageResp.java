package com.github.dapeng.dms.web.vo.response;

import lombok.Data;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:18 PM
 */
@Data
public class DmsPageResp {
    /**
     * *
     * 查询的开始序号（序号从零开始）
     **/
    private long start;

    /**
     * *
     * 返回记录数
     **/
    private long limit;
    /**
     * 结果记录数
     **/
    private long results;

    public DmsPageResp(long start, long limit, long results) {
        this.start = start;
        this.limit = limit;
        this.results = results;
    }
}
