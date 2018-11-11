package com.github.dapeng.dms.web.vo.request;

import lombok.Data;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:18 PM
 */
public class DmsPageReq {
    /**
     * *
     * 查询的开始序号（序号从零开始）
     **/

    private Integer start;

    /**
     * *
     * 返回记录数
     **/

    private Integer limit;

//    private Integer results;

//    private Integer pageIndex;



    /**
     * *
     * 排序的字段
     **/
    private String sortFields;

    public Integer getStart() {
        return start;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getSortFields() {
        return sortFields;
    }

//    public Integer getResults() {
//        return results;
//    }
//
//    public Integer getPageIndex() {
//        return pageIndex;
//    }
}
