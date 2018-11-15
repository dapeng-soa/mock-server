package com.github.dapeng.dms.web.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.dapeng.dms.web.vo.MetaMethodVo;
import com.github.dapeng.dms.web.vo.MetadataVo;
import lombok.Data;

import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-14 6:38 PM
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryMetaMethodResp {
    private List<MetaMethodVo> metaMethodList;
    private DmsPageResp pageResponse;

    public QueryMetaMethodResp(List<MetaMethodVo> metaMethodList) {
        this.metaMethodList = metaMethodList;
    }

    public QueryMetaMethodResp(List<MetaMethodVo> metaMethodList, DmsPageResp pageResponse) {
        this.metaMethodList = metaMethodList;
        this.pageResponse = pageResponse;
    }
}
