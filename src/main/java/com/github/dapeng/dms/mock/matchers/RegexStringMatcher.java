package com.github.dapeng.dms.mock.matchers;

import com.github.dapeng.dms.mock.request.HttpRequestContext;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.PatternSyntaxException;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 2:59 PM
 */
@Slf4j
public class RegexStringMatcher implements Matcher<String> {
    private final String matcher;

    public RegexStringMatcher(String matcher) {
        this.matcher = matcher;
    }


    @Override
    public boolean matches(HttpRequestContext context, String matched) {
        return false;
    }

    public static boolean matches(String matcher, String matched, boolean ignoreCase) {
        boolean result = false;

        if (Strings.isNullOrEmpty(matcher)) {
            result = true;
        } else if (matched != null) {
            // match as exact string
            if (matched.equals(matcher)) {
                result = true;
            }
            if (!result) {
                // match as regex - matcher -> matched
                try {
                    if (matched.matches(matcher)) {
                        result = true;
                    }
                } catch (PatternSyntaxException pse) {
                    log.error("Error while matching regex [" + matcher + "] for string [" + matched + "] " + pse.getMessage());
                }
                // match as regex - matched -> matcher
                try {
                    if (matcher.matches(matched)) {
                        result = true;
                    }
                } catch (PatternSyntaxException pse) {
                    log.error("Error while matching regex [" + matched + "] for string [" + matcher + "] " + pse.getMessage());
                }
                // case insensitive comparison is mainly to improve matching in web containers like Tomcat that convert header names to lower case
                if (!result && ignoreCase) {
                    // match as exact string lower-case
                    if (matched.equalsIgnoreCase(matcher)) {
                        result = true;
                    }
                    // match as regex - matcher -> matched
                    try {
                        if (matched.toLowerCase().matches(matcher.toLowerCase())) {
                            result = true;
                        }
                    } catch (PatternSyntaxException pse) {
                        log.error("Error while matching regex [" + matcher.toLowerCase() + "] for string [" + matched.toLowerCase() + "] " + pse.getMessage());
                    }
                    // match as regex - matched -> matcher
                    try {
                        if (matcher.toLowerCase().matches(matched.toLowerCase())) {
                            result = true;
                        }
                    } catch (PatternSyntaxException pse) {
                        log.error("Error while matching regex [" + matched.toLowerCase() + "] for string [" + matcher.toLowerCase() + "] " + pse.getMessage());
                    }
                }
            }
        }

        return result;
    }
}
