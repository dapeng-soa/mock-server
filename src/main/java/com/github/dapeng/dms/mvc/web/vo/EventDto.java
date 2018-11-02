package com.github.dapeng.dms.mvc.web.vo;

import lombok.Data;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-01 1:22 PM
 */
@Data
public class EventDto {
    /**
     * 触发方法名
     */
    private String touchMethod;

    /**
     * 事件结构体名称
     */
    private String event;
    /**
     * 事件简称
     */
    private String shortName;

    /**
     * 事件简介
     */
    private String mark;

    public EventDto(String touchMethod, String event, String shortName, String mark) {
        this.touchMethod = touchMethod;
        this.event = event;
        this.shortName = shortName;
        this.mark = mark;
    }
}
