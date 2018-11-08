package com.github.dapeng.dms.web.vo.request;

import com.github.dapeng.dms.web.vo.MockVo;
import lombok.Data;

import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-08 11:39 AM
 */
@Data
public class MethodAddRequest {
    private String serviceName;

    private String methodName;

    private String httpType;

    private List<MockVo> mockList;
}
