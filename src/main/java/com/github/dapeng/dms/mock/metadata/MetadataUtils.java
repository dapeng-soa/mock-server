package com.github.dapeng.dms.mock.metadata;

import com.github.dapeng.core.metadata.*;
import com.github.dapeng.json.OptimizedMetadata;
import com.github.dapeng.openapi.cache.ServiceCache;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-02 11:40 AM
 */
public class MetadataUtils {

    public final static Gson gsonFormat = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getName().equals("attachment") || f.getName().equals("__isset_bitfield");
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return clazz == ByteBuffer.class;
        }
    }).setPrettyPrinting().create();

    public static String getServiceResponse(String serviceName, String methodName, String version) {
        OptimizedMetadata.OptimizedService service = ServiceCache.getService(serviceName, version);
        if (service == null) {
            return null;
        }
        return MetadataUtils.getMethodResponseJson(service, serviceName, version, methodName);
    }


    public static String getMethodResponseJson(OptimizedMetadata.OptimizedService service, String serviceName, String version, String methodName) {
        Struct struct = getMethodResponse(service, methodName);
        if (struct == null) {
            return methodName + " of " + serviceName + ":" + version + " not found..........";
        } else {
            List<Field> parameters = struct.getFields();
            Map<String, Object> jsonObjectMap = new HashMap<>();
            jsonObjectMap.put("body", getSample(service, parameters));
            return gsonFormat.toJson(jsonObjectMap);
        }
    }

    private static Struct getMethodResponse(OptimizedMetadata.OptimizedService service, String methodName) {
        Map<String, Method> methodMap = service.getMethodMap();
        Method method = methodMap.get(methodName);
        return method != null ? method.getResponse() : null;
    }

    private static Map<String, Object> getSample(OptimizedMetadata.OptimizedService service, List<Field> parameters) {
        String fieldName;
        DataType fieldType;
        Map<String, Object> mapTemp = new HashMap<>();
        for (int i = 0; i < parameters.size(); i++) {
            Field parameter = parameters.get(i);
            fieldName = parameter.getName();
            fieldType = parameter.getDataType();
            mapTemp.put(fieldName, assignValue(service, fieldType));
        }
        return mapTemp;
    }

    private static Object assignValue(OptimizedMetadata.OptimizedService service, DataType fieldType) {
        Object randomValue = null;
        switch (fieldType.getKind()) {
            case VOID:
                break;
            case BOOLEAN:
                randomValue = Math.round(Math.random()) == 1 ? "true" : "false";
                break;
            case BYTE:
                randomValue = (byte) (Math.random() * 256 - 128);
                break;
            case SHORT:
                randomValue = Math.round(Math.random() * 100);
                break;
            case INTEGER:
                randomValue = Math.round(Math.random() * 1000);
                break;
            case LONG:
                randomValue = Math.round(Math.random() * 1000);
                break;
            case DOUBLE:
                randomValue = Math.random() * 100;
                break;
            case STRING:
                randomValue = "sampleDataString";
                break;
            case BINARY:
                randomValue = "546869732049732041205465737420427974652041727261792E";
                break;
            case MAP:
                DataType keyType = fieldType.getKeyType();
                DataType valueType = fieldType.getValueType();
                Map<Object, Object> mapTemp = new HashMap<Object, Object>();
                Object key = assignValue(service, keyType);
                Object value = assignValue(service, valueType);
                mapTemp.put(key, value);

                randomValue = mapTemp;
                break;
            case LIST:
                List<Object> list = new ArrayList<>();
                DataType listValueType = fieldType.getValueType();
                list.add(assignValue(service, listValueType));
                list.add(assignValue(service, listValueType));

                randomValue = list;
                break;
            case SET:
                Set<Object> set = new HashSet<>();
                DataType setValueType = fieldType.getValueType();
                set.add(assignValue(service, setValueType));
                set.add(assignValue(service, setValueType));
                randomValue = set;
                break;
            case ENUM:
                Map<String, TEnum> structEnumMap = service.getEnumMap();
                TEnum tEnum = structEnumMap.get(fieldType.qualifiedName);
                if (tEnum != null) {

                    int size = tEnum.enumItems.size();
                    int index = (int) (Math.random() * size);
                    return tEnum.enumItems.get(index).label;
                }
                return "";
            case STRUCT:
                Map<String, OptimizedMetadata.OptimizedStruct> optimizedStructMap = service.getOptimizedStructs();
                OptimizedMetadata.OptimizedStruct struct = optimizedStructMap.get(fieldType.getQualifiedName());
                randomValue = getSample(service, struct.getStruct().getFields());
                break;

            case DATE:
                randomValue = "2018/11/11 11:11";
                break;
            case BIGDECIMAL:
                randomValue = "1234567.123456789123456";
                break;
            default:
                randomValue = "";
        }
        return randomValue;
    }

}
