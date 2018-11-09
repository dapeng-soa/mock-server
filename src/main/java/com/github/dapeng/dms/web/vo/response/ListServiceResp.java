package com.github.dapeng.dms.web.vo.response;


import com.github.dapeng.dms.web.vo.MockServiceVo;
import lombok.Data;

import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-09 1:06 PM
 */
@Data
public class ListServiceResp {

    private List<MockServiceVo> serviceList;

    private DmsPageResp pageResponse;

    public ListServiceResp(List<MockServiceVo> serviceList, DmsPageResp pageResponse) {
        this.serviceList = serviceList;
        this.pageResponse = pageResponse;
    }
}
