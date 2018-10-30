package com.github.dapeng.mockserver.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 11:32 AM
 */
public class JsonNestedTest {
    public static String getJsonStr1() {
        String jsonStr = "{\"id\":\"1ui2kdic9\",\"j1\":{\"dd\":\"dd\",\"uu\":\"uu\"},\"j2\":{\"33\":\"33\",\"66\":\"66\",\"j22\":{\"00\":0}},\"name\":\"110\"}";
        return jsonStr;
    }

    public static String getJsonStr2() {
        String jsonStr = "{\"id\":\"1ui2kdic9\",\"j1\":{\"dd\":\"dd\",\"uu\":\"uu\"},\"j2\":{\"33\":\"33\",\"66\":\"66\",\"j22\":{\"j0\":0},\"j23\":{\"00\":0}},\"name\":\"110\"}";
        return jsonStr;
    }

    public static void main(String[] args) throws JSONException {
        Map<String, Object> jsonMap = new HashMap<>();

        JSONObject jsonObject = (JSONObject) JSONParser.parseJSON(getJsonStr1());
//        analysisJson(jsonObject, jsonMap);

        jsonMap.values().forEach(System.out::println);
    }
}
