package com.github.dapeng.dms.web.vo.request;


import java.util.List;

/**
 * list 服务下的所有方法的请求
 *
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:06 PM
 */
public class UpdatePriorityReq {

    private List<Long> priorityList;

    public List<Long> getPriorityList() {
        return priorityList;
    }
}
