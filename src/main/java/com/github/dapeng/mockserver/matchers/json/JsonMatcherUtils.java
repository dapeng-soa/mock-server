package com.github.dapeng.mockserver.matchers.json;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONParser;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 6:29 PM
 */
@Slf4j
public class JsonMatcherUtils {
    private static final Gson gson = new Gson();

    //Object o = gson.fromJson(json, JsonObject.class);
    public static String convertJson(String json) {
        try {
            return gson.toJson(CustomJsonComparator.analysisJson(JSONParser.parseJSON(json), false));
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


}
