package com.github.dapeng.dms.web.entity.dto;

import lombok.Data;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-13 2:00 PM
 */
@Data
public class MockDto {
    private String service;
    private String method;
    private String version;
    private String requestType;

    private Long serviceId;

}
