package com.github.dapeng.dms.mock.matchers.json;

import com.github.dapeng.dms.mock.matchers.rule.PatternWrapper;
import com.github.dapeng.router.pattern.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONParser;

import java.util.Optional;

import static com.github.dapeng.core.helper.IPUtils.matchIpWithMask;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 6:29 PM
 */
@Slf4j
public class JsonMatcherUtils {
    private static final Gson GSON = new Gson();

    //Object o = gson.fromJson(json, JsonObject.class);
    public static String convertJson(String json) {
        try {
            return GSON.toJson(CustomJsonComparator.analysisExpectedJson(JSONParser.parseJSON(json)));
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    //LinkedTreeMap
    public static Pattern convertJsonValueToPattern(String valueJson) {

        Custom obj = GSON.fromJson(valueJson, Custom.class);

        Class<? extends Pattern> patternClass = PatternWrapper.findPatternClass(obj.type);

        Pattern pattern = GSON.fromJson(obj.pattern, patternClass);

        return pattern;
    }

    @Getter
    private static class Custom {
        private int type;
        private JsonObject pattern;

        public Custom(int type, JsonObject pattern) {
            this.type = type;
            this.pattern = pattern;
        }
    }

    public static boolean matcherPattern(Pattern pattern, String actualValue) {
        if (actualValue == null || actualValue.trim().equals("")) {
            return false;
        }
        if (pattern instanceof StringPattern) {
            String content = ((StringPattern) pattern).content;
            return content.equals(actualValue);
        } else if (pattern instanceof NotPattern) {
            Pattern pattern1 = ((NotPattern) pattern).pattern;
            return !matcherPattern(pattern1, actualValue);
        } else if (pattern instanceof IpPattern) {
            IpPattern ipPattern = ((IpPattern) pattern);
            return matchIpWithMask(ipPattern.ip, Integer.parseInt(actualValue), ipPattern.mask);
        } else if (pattern instanceof RegexPattern) {
            /**
             * 使用缓存好的 pattern 进行 正则 匹配
             */
            java.util.regex.Pattern regex = ((RegexPattern) pattern).pattern;
            return regex.matcher(actualValue).matches();

        } else if (pattern instanceof RangePattern) {
            RangePattern range = ((RangePattern) pattern);
            long from = range.from;
            long to = range.to;

            long valueAsLong = Long.parseLong(actualValue);
            return valueAsLong <= to && valueAsLong >= from;

        } else if (pattern instanceof ModePattern) {
            ModePattern mode = ((ModePattern) pattern);
            try {
                long valueAsLong = Long.valueOf(actualValue);
                long result = valueAsLong % mode.base;
                Optional<Long> from = mode.from;
                long to = mode.to;

                if (from.isPresent()) {
                    return result >= from.get() && result <= to;
                } else {
                    return result == to;
                }
            } catch (NumberFormatException e) {
                log.error("[ModePattern]::输入参数 value 应为数字类型的id ，but get {}", actualValue);
            } catch (Exception e) {
                log.error("[ModePattern]::throw exception:" + e.getMessage(), e);
            }
            return false;
        } else if (pattern instanceof NumberPattern) {
            try {
                NumberPattern number = ((NumberPattern) pattern);
                long valueAsLong = Long.parseLong(actualValue);
                long numberLong = number.number;
                return valueAsLong == numberLong;
            } catch (Exception e) {
                log.error("[NumberPattern]::throw exception:" + e.getMessage(), e);
            }
            return false;
        }
        return false;
    }


}
