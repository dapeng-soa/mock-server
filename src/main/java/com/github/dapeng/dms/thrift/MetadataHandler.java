package com.github.dapeng.dms.thrift;

import com.github.dapeng.core.metadata.Service;
import com.github.dapeng.json.OptimizedMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXB;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-08 3:55 PM
 */
@Slf4j
@Component
public class MetadataHandler {
    @Value("${dms.metadata.base.dir}")
    private String baseDir;

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

    public void loadMetadata(String dir, String bizName) throws IOException {
        List<String> serviceXmlList = new ArrayList<>();
        String folderPath = baseDir + bizName;
        File folder = new File(folderPath);

        if (!folder.exists()) {
            log.error("file path [{}] was not exists ", folderPath);
            return;
        }
        File[] files = folder.listFiles();
        //files exists.
        if (files != null && files.length > 0) {
            for (File file : files) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                    StringBuilder builder = new StringBuilder();
                    int len = 0;
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (len != 0) {
                            builder.append("\r\n").append(line);
                        } else {
                            builder.append(line);
                        }
                        len++;
                    }
                    String xml = builder.toString();
                    serviceXmlList.add(xml);
                }

            }
            List<Map<String, OptimizedMetadata.OptimizedService>> services =
                    serviceXmlList.stream().map(this::loadServicesMetadata).collect(Collectors.toList());
        }
    }


    public Map<String, OptimizedMetadata.OptimizedService> loadServicesMetadata(String metadata) {
        Map<String, OptimizedMetadata.OptimizedService> services = Collections.synchronizedMap(new TreeMap<>());
        log.info("fetched the  metadataClient, metadata:{}", metadata.substring(0, 50));
        try (StringReader reader = new StringReader(metadata)) {
            Service serviceData = JAXB.unmarshal(reader, Service.class);
            OptimizedMetadata.OptimizedService optimizedService = new OptimizedMetadata.OptimizedService(serviceData);
            services.put(optimizedService.getService().getName(), optimizedService);
        } catch (Exception e) {
            log.error("metadata解析出错");
            log.error(e.getMessage(), e);

            log.info(metadata);
        }
        log.info("metadata获取成功");

        return services;
    }


    public void processMetadataService(Service service) {
        //etc. 服务简名key ==> OrderService:1.0.0
        String simpleKey = String.format("%s:%s", service.name, service.getMeta().version);
        //etc. 服务全名key ==> com.today.api.order.service.OrderService:1.0.0
        String fullKey = String.format("%s.%s:%s", service.namespace, service.name, service.getMeta().version);

        OptimizedMetadata.OptimizedService optimizedService = new OptimizedMetadata.OptimizedService(service);


        simpleServiceMap.put(simpleKey, optimizedService);

        fullServiceMap.put(fullKey, optimizedService);

        log.info("目前已存在的元数据 service size :  " + simpleServiceMap.size());

        StringBuilder logBuilder = new StringBuilder();
        simpleServiceMap.forEach((k, v) -> logBuilder.append(k + ",  "));
        log.info("zk 服务实例列表: {}", logBuilder);
    }


    public static void main(String[] args) throws IOException {

    }

}



