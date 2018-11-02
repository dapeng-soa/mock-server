package com.github.dapeng.dms.mock.matchers.rule;

import lombok.Data;

import java.util.regex.Pattern;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-02 3:59 PM
 */
@Data
public class StringRule implements Rule {
    private final String regexExpress;

    public StringRule(String regexExpress) {
        this.regexExpress = regexExpress;
    }

    @Override
    public boolean compareValues(Object actualValue) {
        return regexExpress.equals(actualValue);
    }
}
