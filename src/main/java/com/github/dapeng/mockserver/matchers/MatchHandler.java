package com.github.dapeng.mockserver.matchers;

import com.github.dapeng.mockserver.matchers.json.JsonMatcherUtils;
import com.github.dapeng.mockserver.matchers.json.JsonStringMatcher;
import com.github.dapeng.mockserver.matchers.json.MatchType;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 11:37 AM
 */
public class MatchHandler {


    public static boolean matcherJson(String matched, String expectJson,
                                      HttpServletRequest request) {
        return new JsonStringMatcher(JsonMatcherUtils.convertJson(expectJson), MatchType.ONLY_MATCHING_FIELDS).matches(request, matched);
    }

}
