package com.github.dapeng.dms.mvc.util;

import com.github.dapeng.dms.mock.matchers.json.CustomJsonComparator;
import com.github.dapeng.dms.mock.matchers.rule.PatternWrapper;
import com.github.dapeng.dms.mock.matchers.rule.RuleLexer;
import com.github.dapeng.dms.util.Constants;
import com.github.dapeng.router.exception.ParsingException;
import com.github.dapeng.router.pattern.*;
import com.github.dapeng.router.token.*;
import com.google.gson.Gson;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.github.dapeng.router.token.Token.STRING;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-05 1:49 PM
 */
public class MockUtils {
    private static final Gson GSON = new Gson();

    public static String combineMockKey(String service, String method, String version) {
        return service + Constants.KEY_SEPARATE + method + Constants.KEY_SEPARATE + version;
    }

    public static <T> T optional(Optional<T> optional, String msg) {
        if (!optional.isPresent()) {
            throw new IllegalArgumentException(msg);
        }
        return optional.get();
    }


    public static String convertJsonValueToPatternJson(String mockExpress) throws JSONException {
        Map<String, PatternWrapper> expressWrapperMap = new HashMap<>();

        Map<String, Object> stringObjectMap = CustomJsonComparator.analysisExpectedJson(JSONParser.parseJSON(mockExpress));

        for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
            String k = entry.getKey();
            String expectedValue = (String) entry.getValue();
            PatternWrapper wrapper = pattern(new RuleLexer(expectedValue));
            expressWrapperMap.put(k, wrapper);
        }
        return GSON.toJson(expressWrapperMap);
    }

    /**
     * 转换 expectedValue 表达式 为 Rule 对象。
     */
    private PatternWrapper convertValueToRules(String expectedValue) {
        return pattern(new RuleLexer(expectedValue));


    }

    public static PatternWrapper pattern(RuleLexer lexer) {
        Token token = lexer.peek();
        switch (token.type()) {
            case Token.NOT:
                lexer.next(Token.NOT);
                PatternWrapper it = pattern(lexer);
                return new PatternWrapper(PatternWrapper.PatternEnum.NotPattern.getId(),
                        new NotPattern(it.pattern));
            case STRING:
                StringToken st = (StringToken) lexer.next(Token.STRING);
                return new PatternWrapper(PatternWrapper.PatternEnum.StringPattern.getId(),
                        new StringPattern(st.content));
            case Token.REGEXP:
                // get.*
                RegexToken regex = (RegexToken) lexer.next(Token.REGEXP);
                return new PatternWrapper(PatternWrapper.PatternEnum.RegexPattern.getId(),
                        new RegexPattern(regex.pattern));
            case Token.RANGE:
                // getFoo
                RangeToken rt = (RangeToken) lexer.next(Token.RANGE);
                return new PatternWrapper(PatternWrapper.PatternEnum.RangePattern.getId(),
                        new RangePattern(rt.from, rt.to));

            case Token.NUMBER:
                NumberToken nt = (NumberToken) lexer.next(Token.NUMBER);
                return new PatternWrapper(PatternWrapper.PatternEnum.NumberPattern.getId(),
                        new NumberPattern(nt.number));

            case Token.IP:
                IpToken ipToken = (IpToken) lexer.next(Token.IP);
                return new PatternWrapper(PatternWrapper.PatternEnum.IpPattern.getId(),
                        new IpPattern(ipToken.ip, ipToken.mask));
            case Token.KV:
                //todo not implemented
                throw new ParsingException("[KV]", "KV pattern not implemented yet");
            case Token.MODE:
                ModeToken modeToken = (ModeToken) lexer.next(Token.MODE);
                return new PatternWrapper(PatternWrapper.PatternEnum.ModePattern.getId(),
                        new ModePattern(modeToken.base, modeToken.from, modeToken.to));
            default:
                throw new ParsingException("[UNKNOWN TOKEN]", "UnKnown token:" + token);
        }
    }
}
