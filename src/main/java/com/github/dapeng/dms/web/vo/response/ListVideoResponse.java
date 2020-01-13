package com.github.dapeng.dms.web.vo.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.dapeng.dms.web.entity.Video;
import lombok.Data;

import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2020-1-04 23:18 PM
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListVideoResponse {

    private List<Video> videoList;

    private DmsPageResp pageResponse;

    public ListVideoResponse(List<Video> videoList, DmsPageResp pageResponse) {
        this.videoList = videoList;
        this.pageResponse = pageResponse;
    }

    public ListVideoResponse(List<Video> videoList) {
        this.videoList = videoList;
    }
}
