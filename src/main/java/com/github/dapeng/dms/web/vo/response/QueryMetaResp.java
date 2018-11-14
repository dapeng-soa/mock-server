package com.github.dapeng.dms.web.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.dapeng.dms.web.vo.MetadataVo;
import com.github.dapeng.dms.web.vo.MockMethodVo;
import lombok.Data;

import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-14 6:38 PM
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryMetaResp {
    private List<MetadataVo> metadataList;
    private DmsPageResp pageResponse;

    public QueryMetaResp(List<MetadataVo> metadataList) {
        this.metadataList = metadataList;
    }

    public QueryMetaResp(List<MetadataVo> metadataList, DmsPageResp pageResponse) {
        this.metadataList = metadataList;
        this.pageResponse = pageResponse;
    }
}
