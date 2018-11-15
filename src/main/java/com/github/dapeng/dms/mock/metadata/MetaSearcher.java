package com.github.dapeng.dms.mock.metadata;

import com.github.dapeng.core.metadata.Method;
import com.github.dapeng.dms.util.CommonUtil;
import com.github.dapeng.json.OptimizedMetadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-14 5:59 PM
 */
public class MetaSearcher {
    private String service;

    private String version;

    private String methodCondition;

    private long offset;

    private long limit;

    private String sort;

    public void setService(String service) {
        this.service = service;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setMethodCondition(String methodCondition) {
        this.methodCondition = methodCondition;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    private Result executeMethod() {
        long records;
        Map<String, OptimizedMetadata.OptimizedService> serviceMap = MetaMemoryCache.getFullServiceMap();
        String serviceKey = CommonUtil.combine(service, version);
        OptimizedMetadata.OptimizedService service = serviceMap.get(serviceKey);
        if (service != null) {
            Collection<Method> methods = service.getMethodMap().values();
            List<Method> resultList = new ArrayList<>(methods);
            records = resultList.size();
            if (methodCondition != null) {
                resultList = methods.stream().filter(method -> method.getName().contains(methodCondition)).collect(Collectors.toList());
                records = resultList.size();
            }
            if (limit != 0L) {
                if (offset + limit > resultList.size()) {
                    limit = resultList.size() - offset;
                }
                resultList = resultList.subList((int) offset, (int) (offset + limit));
            }
            return new Result(records, resultList);
        }
        return null;
    }

    public static class Result {
        public long records;
        public List<Method> methodList;

        public Result(long records, List<Method> methodList) {
            this.records = records;
            this.methodList = methodList;
        }
    }

    public static class Query {
        private MetaSearcher searcher = new MetaSearcher();

        public Query serviceAndVersion(String service, String version) {
            searcher.setService(service);
            searcher.setVersion(version);
            return this;
        }

        public Query method(String methodName) {
            searcher.setMethodCondition(methodName);
            return this;
        }

        public Query offset(long offset) {
            searcher.setOffset(offset);
            return this;
        }

        public Query limit(long limit) {
            searcher.setLimit(limit);
            return this;
        }

        public Query orderBy(String sort) {
            searcher.setSort(sort);
            return this;
        }

        public Result executeMethod() {
            return searcher.executeMethod();
        }
    }

}
