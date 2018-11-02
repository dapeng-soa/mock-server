package com.github.dapeng.mockserver.json;

import com.github.dapeng.dms.mock.matchers.json.JsonStringMatcher;
import com.github.dapeng.dms.mock.matchers.json.MatchType;
import com.github.dapeng.dms.mock.matchers.json.JsonMatcherUtils;
import com.google.gson.Gson;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 10:42 AM
 */
public class JsonStringMatcherTest {
    public static final String NEW_LINE = System.getProperty("line.separator");

    private Gson gson = new Gson();

    @Test
    public void shouldMatchSomeFields() {
        // given
        String matched = "" +
                "{" + NEW_LINE +
                "    \"menu\": {" + NEW_LINE +
                "        \"id\": \"file\"," + NEW_LINE +
                "        \"value\": \"File\"," + NEW_LINE +
                "        \"popup\": {" + NEW_LINE +
                "            \"menuitem\": [" + NEW_LINE +
                "                {" + NEW_LINE +
                "                    \"value\": \"Close\"," + NEW_LINE +
                "                    \"onclick\": \"CloseDoc()\"" + NEW_LINE +
                "                }" + NEW_LINE +
                "            ]" + NEW_LINE +
                "        }" + NEW_LINE +
                "    }" + NEW_LINE +
                "}";

        // then
        System.out.println(matched);
        assertTrue(new JsonStringMatcher("{" +
                "\"id\": \"file\"" + ",\"value\": \"Close\"" + "}"
                , MatchType.ONLY_MATCHING_FIELDS).matches(null, null, matched));
    }

    @Test
    public void shouldMatchSomeFields2() {
        // given
        String matched = "{\n" +
                "  \"menu\": {\n" +
                "    \"id\": \"file\",\n" +
                "   \"value\": \"File\",\n" +
                "    \"popup\": {\n" +
                "       \"menuitem\": [{\n" +
                "           \"value\": \"Close1\",\n" +
                "           \"onclick\": \"CloseDoc1()\"\n" +
                "},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"value\": \"Close2\",\n" +
                "\t\t\t\t\t\"onclick\": \"CloseDoc2()\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";

        // then
        System.out.println(matched);

        Map<String, String> expectMap = new HashMap<>();
        expectMap.put("menu-value", "File");
        expectMap.put("menu-popup-menuitem-value", "Close2");

        assertTrue(new JsonStringMatcher(gson.toJson(expectMap), MatchType.ONLY_MATCHING_FIELDS).matches(null, null, matched));
    }

    @Test
    public void shouldMatchSomeFields3() {
        // given
        String matched = "{\n" +
                "  \"menu\": {\n" +
                "    \"id\": \"file\",\n" +
                "   \"value\": \"File\",\n" +
                "    \"popup\": {\n" +
                "       \"menuitem\": [{\n" +
                "           \"value\": \"Close1\",\n" +
                "           \"onclick\": \"CloseDoc1()\"\n" +
                "},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"value\": \"Close2\",\n" +
                "\t\t\t\t\t\"onclick\": \"CloseDoc2()\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";

        System.out.println(matched);

        String expectJson = "{\n" +
                "\t\"menu\": {\n" +
                "\t\t\"value\": \"File1\",\n" +
                "\t\t\"popup\": {\n" +
                "\t\t\t\"menuitem\": [{\n" +
                "\t\t\t\t\t\"value\": \"Close1\"\n" +
                "\n" +
                "\t\t\t\t}\n" +
                "\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";


        assertTrue(new JsonStringMatcher(JsonMatcherUtils.convertJson(expectJson), MatchType.ONLY_MATCHING_FIELDS).matches(null, null, matched));
    }
}
