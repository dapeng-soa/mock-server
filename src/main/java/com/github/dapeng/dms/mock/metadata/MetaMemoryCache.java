package com.github.dapeng.dms.mock.metadata;

import com.github.dapeng.json.OptimizedMetadata;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-14 5:59 PM
 */
public class MetaMemoryCache {

    /**
     * 以服务的SimpleName和version拼接作为key，保存服务元数据的map
     * etc. AdminSkuPriceService:1.0.0 -> 元信息
     */
    private static Map<String, OptimizedMetadata.OptimizedService> simpleServiceMap = Collections.synchronizedMap(new TreeMap<>());
    /**
     * 以服务的全限定名和 version 拼接作为key，保存服务的实例信息
     * etc. com.today.api.skuprice.service.AdminSkuPriceService:1.0.0  -> ServiceInfo 实例信息
     */
    private static Map<String, OptimizedMetadata.OptimizedService> fullServiceMap = Collections.synchronizedMap(new TreeMap<>());

    public static Map<String, OptimizedMetadata.OptimizedService> getSimpleServiceMap() {
        return simpleServiceMap;
    }

    public static Map<String, OptimizedMetadata.OptimizedService> getFullServiceMap() {
        return fullServiceMap;
    }
}
