package com.github.dapeng.dms.web.vo.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.dapeng.dms.web.vo.MockServiceVo;
import lombok.Data;

import java.util.List;

/**
 * {@link JsonInclude https://blog.csdn.net/youthsong/article/details/78801756}
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:06 PM
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListServiceResp {

    private List<MockServiceVo> serviceList;

    private DmsPageResp pageResponse;

    public ListServiceResp(List<MockServiceVo> serviceList, DmsPageResp pageResponse) {
        this.serviceList = serviceList;
        this.pageResponse = pageResponse;
    }

    public ListServiceResp(List<MockServiceVo> serviceList) {
        this.serviceList = serviceList;
    }
}
