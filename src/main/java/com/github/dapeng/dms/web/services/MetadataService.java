package com.github.dapeng.dms.web.services;

import com.github.dapeng.core.metadata.Method;
import com.github.dapeng.core.metadata.Service;
import com.github.dapeng.dms.mock.metadata.MetaMemoryCache;
import com.github.dapeng.dms.thrift.MetadataHandler;
import com.github.dapeng.dms.thrift.ThriftGenerator;
import com.github.dapeng.dms.web.entity.MockMetadata;
import com.github.dapeng.dms.web.entity.QMockMetadata;
import com.github.dapeng.dms.web.repository.MetadataRepository;
import com.github.dapeng.dms.web.util.MockException;
import com.github.dapeng.dms.web.vo.response.CreateMetaServiceResp;
import com.github.dapeng.json.OptimizedMetadata;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import scala.Tuple2;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXB;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-14 4:47 PM
 */
@org.springframework.stereotype.Service
@Slf4j
public class MetadataService implements InitializingBean {


    private final MetadataRepository metaRepository;
    //实体管理者
    private final EntityManager entityManager;
    //JPA查询工厂
    private JPAQueryFactory queryDsl;

    public MetadataService(MetadataRepository metaRepository, EntityManager entityManager) {
        this.metaRepository = metaRepository;
        this.entityManager = entityManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        queryDsl = new JPAQueryFactory(entityManager);
        log.info("init JPAQueryFactory successfully");
    }

    public List<Service> parseMetadata(String thriftBaseDir, String resourceBaseDir, String tag) {
        List<Service> serviceList = thriftGenerator(thriftBaseDir, resourceBaseDir, tag);

        serviceList.forEach(this::processService);

        List<OptimizedMetadata.OptimizedService> optimizedServiceList = serviceList.stream()
                .map(OptimizedMetadata.OptimizedService::new).collect(Collectors.toList());
        // process metadata
        serviceList.forEach(this::processMetadataService);

        return null;
    }

    public CreateMetaServiceResp processService(Service service) {
        String simpleName = service.name;
        String serviceName = service.namespace + "." + service.name;
        String version = service.meta.version;
        List<Method> methods = service.methods;
        new CreateMetaServiceResp(simpleName, serviceName, version, methods.size());
        StringWriter metadata = new StringWriter();
        JAXB.marshal(service, metadata);

        QMockMetadata qMeta = QMockMetadata.mockMetadata;
        MockMetadata oldMeta = queryDsl.selectFrom(qMeta).where(qMeta.serviceName.eq(serviceName).and(qMeta.version.eq(version))).fetchFirst();
        if (oldMeta != null) {
            metaRepository.delete(oldMeta);
        }
        //保存元数据信息
        metaRepository.save(new MockMetadata(simpleName, serviceName, version, metadata.toString()));

        return null;

    }


    public List<Service> thriftGenerator(String thriftBaseDir, String resourceBaseDir, String tag) {
        try {
            String thriftDir = thriftBaseDir + tag;
            String resourceDir = resourceBaseDir + tag;
            Tuple2<List<com.github.dapeng.core.metadata.Service>, String[]> generateFiles = ThriftGenerator.generateFiles(thriftDir, resourceDir);
            log.info("解析 thrift 成功, generateFiles: {} ,{}", generateFiles._1, generateFiles._2);
            return generateFiles._1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }


    private Map<String, OptimizedMetadata.OptimizedService> loadServicesMetadata(String metadata) {
        Map<String, OptimizedMetadata.OptimizedService> services = new TreeMap<>();
//        log.info("fetched the  metadataClient, metadata:{}", metadata.substring(0, 50));
        try (StringReader reader = new StringReader(metadata)) {
            Service serviceData = JAXB.unmarshal(reader, Service.class);
            OptimizedMetadata.OptimizedService optimizedService = new OptimizedMetadata.OptimizedService(serviceData);
            services.put(optimizedService.getService().getName(), optimizedService);
            processMetadataService(serviceData);
        } catch (Exception e) {
            log.error("metadata解析出错");
            log.error(e.getMessage(), e);

            log.info(metadata);
        }
        return services;
    }

    public void processMetadataService(Service service) {
        //etc. 服务简名key ==> OrderService:1.0.0
        String simpleKey = String.format("%s:%s", service.name, service.getMeta().version);
        //etc. 服务全名key ==> com.today.api.order.service.OrderService:1.0.0
        String fullKey = String.format("%s.%s:%s", service.namespace, service.name, service.getMeta().version);

        OptimizedMetadata.OptimizedService optimizedService = new OptimizedMetadata.OptimizedService(service);

        MetaMemoryCache.getSimpleServiceMap().put(simpleKey, optimizedService);
        MetaMemoryCache.getFullServiceMap().put(fullKey, optimizedService);
        log.info("存储服务 {} 元信息成功,目前已存在的元数据数量：{}", service.getName(), MetaMemoryCache.getSimpleServiceMap().size());
    }

    /************************************************************************************************************************************
     *                                                                                                                                  *
     * @param targetDir                                                                                                                 *
     * @return                                                                                                                          *
     * @throws IOException                                                                                                              *
     * **********************************************************************************************************************************
     */
    public List<Map<String, OptimizedMetadata.OptimizedService>> loadMetadata(String targetDir) throws IOException {
        List<String> serviceXmlList = new ArrayList<>();
        File folder = new File(targetDir);
        if (!folder.exists()) {
            log.error("file path [{}] was not exists ", targetDir);
            throw new MockException("根据[" + targetDir + "]目标路径未找到元数据信息");
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
            List<Map<String, OptimizedMetadata.OptimizedService>> servicesMapList =
                    serviceXmlList.stream().map(this::loadServicesMetadata).collect(Collectors.toList());
            StringBuilder logBuilder = new StringBuilder();
            MetaMemoryCache.getSimpleServiceMap().forEach((k, v) -> logBuilder.append(k).append(",  "));
            log.info("服务实例列表:\n #### [{}] ####", logBuilder);
            return servicesMapList;
        }
        log.error("{} 路径下没有元数据信息解析", targetDir);
        return null;
    }
}
