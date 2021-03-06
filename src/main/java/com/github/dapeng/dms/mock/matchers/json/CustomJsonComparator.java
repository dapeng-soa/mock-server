package com.github.dapeng.dms.mock.matchers.json;

import com.github.dapeng.dms.web.entity.MockContext;
import com.github.dapeng.router.pattern.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.AbstractComparator;

import java.util.*;

import static org.skyscreamer.jsonassert.comparator.JSONCompareUtil.*;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 10:37 AM
 */
@Slf4j
public class CustomJsonComparator extends AbstractComparator {
    private final JSONCompareMode mode;
    private final MockContext mockContext;

    public CustomJsonComparator(JSONCompareMode mode, MockContext mockContext) {
        this.mode = mode;
        this.mockContext = mockContext;
    }

    @Override
    public void compareJSON(String prefix, JSONObject expected, JSONObject actual, JSONCompareResult result)
            throws JSONException {
        // Check that actual contains all the expected values
        checkJsonObjectKeysExpectedInActual(prefix, expected, actual, result);

        // If strict, check for vice-versa
        if (!mode.isExtensible()) {
            checkJsonObjectKeysActualInExpected(prefix, expected, actual, result);
        }
    }

    @Override
    public void compareValues(String prefix, Object expectedValue, Object actualValue, JSONCompareResult result)
            throws JSONException {
        Pattern pattern = (Pattern) expectedValue;

        if (areNumbers(expectedValue, actualValue)) {
            if (areNotSameDoubles(expectedValue, actualValue)) {
                result.fail(prefix, expectedValue, actualValue);
            }
        } else if (expectedValue.getClass().isAssignableFrom(actualValue.getClass())) {
            if (expectedValue instanceof JSONArray) {
                compareJSONArray(prefix, (JSONArray) expectedValue, (JSONArray) actualValue, result);
            } else if (expectedValue instanceof JSONObject) {
                compareJSON(prefix, (JSONObject) expectedValue, (JSONObject) actualValue, result);
            } else if (!expectedValue.equals(actualValue)) {
                //Rule Pattern 表达式
                boolean compareResult = JsonMatcherUtils.matcherPattern(pattern, (String) actualValue);
                //匹配失败，报错。
                if (!compareResult) {
                    result.fail(prefix, expectedValue, actualValue);
                }
            }

        } else {
            //Rule 表达式
            boolean compareResult = JsonMatcherUtils.matcherPattern(pattern, actualValue.toString());
            //匹配失败，报错。
            if (!compareResult) {
                result.fail(prefix, expectedValue, actualValue);
            }
        }
    }

    @Override
    public void compareJSONArray(String prefix, JSONArray expected, JSONArray actual, JSONCompareResult result)
            throws JSONException {
        if (expected.length() != actual.length()) {
            result.fail(prefix + "[]: Expected " + expected.length() + " values but got " + actual.length());
            return;
        } else if (expected.length() == 0) {
            return; // Nothing to compare
        }

        if (mode.hasStrictOrder()) {
            compareJSONArrayWithStrictOrder(prefix, expected, actual, result);
        } else if (allSimpleValues(expected)) {
            compareJSONArrayOfSimpleValues(prefix, expected, actual, result);
        } else if (allJSONObjects(expected)) {
            compareJSONArrayOfJsonObjects(prefix, expected, actual, result);
        } else {
            // An expensive last resort
            recursivelyCompareJSONArray(prefix, expected, actual, result);
        }
    }

    private boolean areNumbers(Object expectedValue, Object actualValue) {
        return expectedValue instanceof Number && actualValue instanceof Number;
    }

    private boolean areNotSameDoubles(Object expectedValue, Object actualValue) {
        return ((Number) expectedValue).doubleValue() != ((Number) actualValue).doubleValue();
    }

    /**
     * custom rewrite 重写
     *
     * @throws JSONException ex
     */
    protected void checkJsonObjectKeysExpectedInActual(String prefix, JSONObject expected, JSONObject actual, JSONCompareResult result) throws JSONException {
        Set<String> expectedKeys = getKeys(expected);
        Map<String, Object> allValuesJsonMap = analysisJson(actual);

        for (String key : expectedKeys) {
            Object expectedValue = expected.get(key);
            Pattern pattern = JsonMatcherUtils.convertJsonValueToPattern(expectedValue.toString());

            if (allValuesJsonMap.containsKey(key)) {
                Object actualValue = allValuesJsonMap.get(key);
                if (actualValue instanceof List) {
                    List actualList = (List) actualValue;
                    boolean flag = false;
                    for (Object obj : actualList) {
                        try {
                            JSONCompareResult listResult = new JSONCompareResult();
                            compareValues(qualify(prefix, key), pattern, obj, listResult);
                            if (listResult.passed()) {
                                flag = true;
                                break;
                            }
                        } catch (JSONException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                    if (!flag) {
                        result.fail("数组匹配不通过!");
                    }

                } else {
                    compareValues(qualify(prefix, key), pattern, actualValue, result);
                    log.info("result: {}", result.toString());
                }
            } else {
                result.missing(prefix, key);
            }
        }
        log.info("end result: {}", result.toString());
    }


    private static Map<String, Object> analysisJson(Object objJson) throws JSONException {
        Map<String, Object> jsonMap = new HashMap<>();
        analysisJson(objJson, null, jsonMap, true);
        return jsonMap;
    }

    public static Map<String, Object> analysisExpectedJson(Object objJson) throws JSONException {
        Map<String, Object> jsonMap = new HashMap<>();
        analysisJson(objJson, null, jsonMap, false);
        return jsonMap;
    }

    @SuppressWarnings("rawtypes")
    private static void analysisJson(Object objJson, String parent, Map<String, Object> jsonMap, boolean putJson) throws JSONException {
        //如果obj为json数组
        if (objJson instanceof JSONArray) {
            JSONArray objArray = (JSONArray) objJson;
            analysisJsonArray(objArray, parent, jsonMap, putJson);
        }
        //如果为json对象
        else if (objJson instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) objJson;
            Iterator it = jsonObject.keys();
            String prefixTmp = parent;
            while (it.hasNext()) {
                String key = it.next().toString();
                Object object = jsonObject.get(key);
                //存一次
                parent = parent == null ? "" : parent + "-";

                //如果得到的是数组
                if (object instanceof JSONArray) {
                    if (putJson) {
                        jsonMap.put(parent + key, object);
                    }
                    JSONArray objArray = (JSONArray) object;
                    analysisJson(objArray, parent + key, jsonMap, putJson);
                }
                //如果key中是一个json对象
                else if (object instanceof JSONObject) {
                    if (putJson) {
                        jsonMap.put(parent + key, object);
                    }
                    analysisJson(object, parent + key, jsonMap, putJson);
                }
                //如果key中是其他
                else {
                    if (jsonMap.containsKey(parent + key) && jsonMap.get(parent + key) != null) {
                        Object oldValue = jsonMap.get(parent + key);
                        jsonMap.put(parent + key, Arrays.asList(oldValue, object));
                    } else {
                        jsonMap.put(parent + key, object);
                    }

                }
                parent = prefixTmp;
            }
        }
    }

    /**
     * @param jsonArray Json 数组对象
     * @param parent    当前json key的父 key
     * @param jsonMap   存储 嵌套 Json 键值对的 map
     * @param putJson   形如: {"menu" : {"id":"2"}} 形式，if {@code false}。就不会存 menu为key 后面json串为 value的值
     *                  主要作用是，在对用户输入的 expectJson 表达式时，只会存具体的key value.
     *                  {@code "{"order":{
     *                  "detail":{
     *                  "sales":"20"
     *                  }
     *                  }}"
     *                  }
     *                  如果putJson 为 false =>  ("order-detail-sales",20)
     *                  如果为 true =>  ("order","{"detail":{"sales":"20"}")  ("order-detail",{"sales":"20"}})  ("order-detail-sales",20)
     * @throws JSONException ex
     */
    private static void analysisJsonArray(JSONArray jsonArray, String parent,
                                          Map<String, Object> jsonMap, boolean putJson) throws JSONException {
        String prefix = parent;
        for (int i = 0; i < jsonArray.length(); i++) {
            analysisJson(jsonArray.get(i), parent, jsonMap, putJson);
            parent = prefix;
        }
    }
}
