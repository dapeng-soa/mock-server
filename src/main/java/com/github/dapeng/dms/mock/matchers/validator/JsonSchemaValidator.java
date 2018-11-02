package com.github.dapeng.dms.mock.matchers.validator;

import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-31 6:02 PM
 */
@Slf4j
public class JsonSchemaValidator {
    public static void matcher(String actualJson) throws JSONException {
        try {
            JSONObject actualJsonObject = new JSONObject(actualJson);
            SchemaLoader.load(actualJsonObject).validate(actualJsonObject);
        } catch (ValidationException | JSONException e) {
            log.error("Failed to match JSON: {},because: {}", actualJson, e.getMessage());
            throw e;
        }
    }
}
