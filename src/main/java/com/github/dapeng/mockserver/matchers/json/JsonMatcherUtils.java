package com.github.dapeng.mockserver.matchers.json;

import com.google.gson.Gson;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 6:29 PM
 */
public class JsonMatcherUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonMatcherUtils.class);
    private static final Gson gson = new Gson();

    //Object o = gson.fromJson(json, JsonObject.class);
    public static String convertJson(String json) {
        try {
            return gson.toJson(CustomJsonComparator.analysisJson(JSONParser.parseJSON(json), false));
        } catch (JSONException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }


}
