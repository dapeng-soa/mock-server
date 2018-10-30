package com.github.dapeng.mockserver.matchers;

import com.github.dapeng.mockserver.matchers.json.CustomJsonComparator;
import com.github.dapeng.mockserver.request.RequestContext;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 10:32 AM
 */
public class JsonStringMatcher implements Matcher<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonStringMatcher.class);
    private final Gson gson = new Gson();

    private final String matcher;
    private final MatchType matchType;


    public JsonStringMatcher(String matcher, MatchType matchType) {
        this.matcher = matcher;
        this.matchType = matchType;
    }


    public enum FormatType {
        JSON,
        KEY
    }

    @Override
    public boolean matches(final RequestContext context, String matched) {
        boolean result = false;
        JSONCompareResult jsonCompareResult;
        try {
            if (Strings.isNullOrEmpty(matcher)) {
                result = true;
            } else {
                JSONCompareMode jsonCompareMode = JSONCompareMode.LENIENT;
                if (matchType == MatchType.STRICT) {
                    jsonCompareMode = JSONCompareMode.STRICT;
                }
                jsonCompareResult = JSONCompare.compareJSON(matcher, matched, new CustomJsonComparator(jsonCompareMode));

                if (jsonCompareResult.passed()) {
                    result = true;
                }

                if (!result) {
                    LOGGER.warn("Failed to perform JSON match \"{}\" with \"{}\" because {}", matched, this.matcher, jsonCompareResult.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to perform JSON match \"{}\" with \"{}\" because {}", matched, this.matcher, e.getMessage());
        }

        return result;
    }
}
