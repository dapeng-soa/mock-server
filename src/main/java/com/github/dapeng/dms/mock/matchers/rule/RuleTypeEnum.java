package com.github.dapeng.dms.mock.matchers.rule;

import lombok.Getter;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-02 4:31 PM
 */
@Getter
public enum RuleTypeEnum {
    STRING(0, "STRING"),
    REGEX(1, "REGEX");

    private int id;

    private String rule;

    RuleTypeEnum(int id, String rule) {
        this.id = id;
        this.rule = rule;
    }

    public static Rule findRuleById(int id, Object expectedValue) {
        switch (id) {
            case 0:
                return new StringRule((String) expectedValue);
            case 1:
                return new RegexRule((String) expectedValue);
            default:
                return new StringRule((String) expectedValue);
        }
    }
}
