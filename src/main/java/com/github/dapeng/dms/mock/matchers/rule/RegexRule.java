package com.github.dapeng.dms.mock.matchers.rule;

import lombok.Data;

import java.util.regex.Pattern;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-02 3:59 PM
 */
@Data
public class RegexRule implements Rule {
    private final String regexExpress;
    private final Pattern pattern;

    public RegexRule(String regexExpress) {
        this.regexExpress = regexExpress;
        pattern = Pattern.compile(regexExpress);
    }

    @Override
    public boolean compareValues(Object actualValue) {
        if (actualValue instanceof CharSequence) {
            return pattern.matcher((CharSequence) actualValue).matches();
        }
        return false;
    }
}
