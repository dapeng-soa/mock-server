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
    private List<MetadataVo> metadataVoList;
    private DmsPageResp pageResponse;

    public QueryMetaResp(List<MetadataVo> metadataVoList) {
        this.metadataVoList = metadataVoList;
    }

    public QueryMetaResp(List<MetadataVo> metadataVoList, DmsPageResp pageResponse) {
        this.metadataVoList = metadataVoList;
        this.pageResponse = pageResponse;
    }
}
