package com.github.dapeng.dms.mvc.web.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

/**
 * @author maple
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EventVo {
    /**
     * 触发方法列表
     */
    private List<String> touchMethods;

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


    public EventVo(List<String> touchMethods, String event, String shortName, String mark) {
        this.touchMethods = touchMethods;
        this.event = event;
        this.shortName = shortName;
        this.mark = mark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventVo eventVo = (EventVo) o;
        return Objects.equals(touchMethods, eventVo.touchMethods) &&
                Objects.equals(event, eventVo.event) &&
                Objects.equals(shortName, eventVo.shortName) &&
                Objects.equals(mark, eventVo.mark);
    }

    @Override
    public int hashCode() {

        return Objects.hash(touchMethods, event, shortName, mark);
    }
}


