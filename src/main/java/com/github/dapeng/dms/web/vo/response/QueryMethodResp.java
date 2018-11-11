package com.github.dapeng.dms.web.vo.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.dapeng.dms.web.vo.MockMethodVo;
import lombok.Data;

import java.util.List;

/**
 * {@link JsonInclude https://blog.csdn.net/youthsong/article/details/78801756}
 *
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:06 PM
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryMethodResp {

    private List<MockMethodVo> methodList;

    private DmsPageResp pageResponse;

    public QueryMethodResp(List<MockMethodVo> methodVoList, DmsPageResp pageResponse) {
        this.methodList = methodVoList;
        this.pageResponse = pageResponse;
    }

    public QueryMethodResp(List<MockMethodVo> methodVoList) {
        this.methodList = methodVoList;
    }
}
