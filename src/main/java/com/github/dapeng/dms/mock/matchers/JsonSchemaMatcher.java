package com.github.dapeng.dms.mock.matchers;

import com.github.dapeng.dms.mock.request.HttpRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 5:55 PM
 */
@Slf4j
public class JsonSchemaMatcher implements Matcher<String> {
    @Override
    public boolean matches(final HttpRequestContext context, String actualJson) {
        boolean result = false;
        try {
            JSONObject actualJsonObject = new JSONObject(actualJson);
            SchemaLoader.load(actualJsonObject).validate(actualJsonObject);
            result = true;
        } catch (ValidationException | JSONException e) {
            log.error("Failed to match JSON: {},because: {},context:{} ", actualJson, e.getMessage(), context);
        }
        return result;
    }
}
