package com.github.dapeng.dms.web.vo.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.dapeng.dms.web.vo.MockMethodVo;
import com.github.dapeng.dms.web.vo.MockVo;
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
public class QueryMockResp {

    private List<MockVo> mockList;

    private DmsPageResp pageResponse;

    public QueryMockResp(List<MockVo> mockList, DmsPageResp pageResponse) {
        this.mockList = mockList;
        this.pageResponse = pageResponse;
    }

    public QueryMockResp(List<MockVo> mockList) {
        this.mockList = mockList;
    }
}
