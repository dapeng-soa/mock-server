package com.github.dapeng.dms.mock.matchers.json;

import com.github.dapeng.dms.mock.matchers.Matcher;
import com.github.dapeng.dms.mock.request.HttpRequestContext;
import com.github.dapeng.dms.mvc.entity.MockContext;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 10:32 AM
 */
@Slf4j
public class JsonStringMatcher implements Matcher<String> {
    /**
     * 预期 Json 格式
     */
    private final String expectedJson;
    private final MatchType matchType;

    public JsonStringMatcher(String expectedJson, MatchType matchType) {
        this.expectedJson = expectedJson;
        this.matchType = matchType;
    }

    @Override
    public boolean matches(final HttpRequestContext context, MockContext mockContext, String actualJson) {
        boolean result = false;
        JSONCompareResult jsonCompareResult;
        try {
            if (Strings.isNullOrEmpty(expectedJson)) {
                result = true;
            } else {
                JSONCompareMode jsonCompareMode = JSONCompareMode.LENIENT;
                if (matchType == MatchType.STRICT) {
                    jsonCompareMode = JSONCompareMode.STRICT;
                }
                //                                       expectedStr, actualStr
                jsonCompareResult = JSONCompare.compareJSON(expectedJson, actualJson,
                        new CustomJsonComparator(jsonCompareMode, mockContext));

                if (jsonCompareResult.passed()) {
                    result = true;
                }

                if (!result) {
                    log.warn("Failed to perform JSON match actual \"{}\" with expected \"{}\" because {}",
                            actualJson, this.expectedJson, jsonCompareResult.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Failed to perform JSON match actual \"{}\" with expected \"{}\" because {}",
                    actualJson, this.expectedJson, e.getMessage());
        }
        return result;
    }
}
