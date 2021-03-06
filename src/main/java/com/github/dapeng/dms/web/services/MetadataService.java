package com.github.dapeng.dms.web.services;

import com.github.dapeng.core.metadata.Method;
import com.github.dapeng.core.metadata.Service;
import com.github.dapeng.dms.mock.metadata.MetaMemoryCache;
import com.github.dapeng.dms.mock.metadata.MetaSearcher;
import com.github.dapeng.dms.thrift.ThriftGenerator;
import com.github.dapeng.dms.util.CommonUtil;
import com.github.dapeng.dms.web.entity.MockMetadata;
import com.github.dapeng.dms.web.entity.MockService;
import com.github.dapeng.dms.web.entity.QMockMetadata;
import com.github.dapeng.dms.web.entity.QMockService;
import com.github.dapeng.dms.web.repository.MetadataRepository;
import com.github.dapeng.dms.web.util.MockException;
import com.github.dapeng.dms.web.vo.MetaMethodVo;
import com.github.dapeng.dms.web.vo.request.DmsPageReq;
import com.github.dapeng.dms.web.vo.request.QueryMetaMethodReq;
import com.github.dapeng.dms.web.vo.response.CreateMetaServiceResp;
import com.github.dapeng.dms.web.vo.response.DmsPageResp;
import com.github.dapeng.dms.web.vo.response.QueryMetaMethodResp;
import com.github.dapeng.json.OptimizedMetadata;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.transaction.annotation.Transactional;
import scala.Tuple2;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXB;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-14 4:47 PM
 */
@org.springframework.stereotype.Service
@Slf4j
@Transactional(rollbackFor = Throwable.class)
public class MetadataService implements InitializingBean, ApplicationRunner {


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

    /**
     * 自动判断解析类型
     */
    public String candidateGenerateType(String tag, String fullPath) {
        File targetFolder = new File(fullPath);
        if (!targetFolder.exists()) {
            log.error("file path [{}] was not exists ", fullPath);
            throw new MockException("根据[" + fullPath + "]目标路径未找到元数据信息");
        }
        File[] files = targetFolder.listFiles();
        if (files != null && files.length > 0) {
            Set<String> suffixType = new HashSet<>();
            for (File file : files) {
                String fileName = file.getName();
                String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
                suffixType.add(suffixName);
            }
            if (suffixType.size() == 0) {
                throw new MockException("上传文件后缀名不能为空.");
            }
            if (suffixType.size() > 1) {
                throw new MockException("单次上传只能选择一种格式，thrift 或者 xml");
            }
            return suffixType.iterator().next();
        }
        throw new MockException("根据服务Tag:" + tag + " 指定目录下没有可解析文件，请重新上传.");
    }


    /**
     * 解析上传的 thrift 文件并
     * 1).生成 xml 存入 数据库。
     * 2).解析 现有服务信息存入 内存。
     * <p>
     * filterServiceName 是否过滤
     *
     * @param tag 服务 tag
     */
    public void processThriftGenerator(String thriftBaseDir, String tag, String serviceFilter) {
        List<Service> serviceList = doThriftGenerator(thriftBaseDir, tag);
        if (serviceList != null) {
            if (serviceFilter != null) {
                serviceList = serviceList.stream()
                        .filter(service -> serviceFilter.equals(CommonUtil.combineMeta(service.namespace, service.name)))
                        .collect(Collectors.toList());
            }
            serviceList.forEach(service -> {
                //保存最新元信息到 DB
                saveMetaService(service);
                //保存数据到内存空间.每次重启会重新加载.
                cachedMetaService(service);
            });
        }
    }

    private List<Service> doThriftGenerator(String thriftBaseDir, String tag) {
        try {
            String thriftDir = thriftBaseDir + tag;
            Tuple2<List<com.github.dapeng.core.metadata.Service>, String[]> generateFiles = ThriftGenerator.generateFiles(thriftDir, thriftDir);
            log.info("解析 thrift 成功, generateFiles: {} ,{}", generateFiles._1, generateFiles._2);
            return generateFiles._1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 解析上传的 xml 文件并
     * 1).将 xml 存入 数据库。
     * 2).解析 现有服务信息存入 内存。
     *
     * @param tag 服务 tag
     */
    public void processXmlGenerator(String resourcesDir, String tag, String serviceFilter) throws IOException {
        String targetDir = resourcesDir + tag;
        List<String> xmlStringList = loadXmlReSourcesFromDist(targetDir);

        xmlStringList.forEach(str -> {
            Service service = unmarshalMetadataFromStream(str);
            if (service != null) {
                if (serviceFilter != null) {
                    if (serviceFilter.equals(CommonUtil.combineMeta(service.namespace, service.name))) {
                        saveMetaService(service);
                        cachedMetaService(service);
                    }
                } else {
                    saveMetaService(service);
                    cachedMetaService(service);
                }
            }
        });
    }


    /**
     * 保存元数据信息
     */
    private void saveMetaService(Service service) {
        String simpleName = service.name;
        String serviceName = service.namespace + "." + service.name;
        String version = service.meta.version;
        List<Method> methods = service.methods;
        new CreateMetaServiceResp(simpleName, serviceName, version, methods.size());
        StringWriter metadata = new StringWriter();
        JAXB.marshal(service, metadata);
        QMockMetadata qMeta = QMockMetadata.mockMetadata;


        MockMetadata storedMeta;
        MockMetadata oldMeta = queryDsl.selectFrom(qMeta).where(qMeta.serviceName.eq(serviceName).and(qMeta.version.eq(version))).fetchFirst();
        if (oldMeta != null) {
            oldMeta.setSimpleName(simpleName);
            oldMeta.setServiceName(serviceName);
            oldMeta.setVersion(version);
            oldMeta.setMetadata(metadata.toString());
            oldMeta.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            oldMeta.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            storedMeta = metaRepository.save(oldMeta);
        } else {
            //保存元数据信息
            storedMeta = metaRepository.save(new MockMetadata(simpleName, serviceName, version, metadata.toString()));
        }
        //关联 mockData
        associateMockService(serviceName, version, storedMeta.getId());
    }

    /**
     * 将 metadata 信息放入内存缓存.
     */
    private void cachedMetaService(Service service) {
        //etc. 服务简名key ==> OrderService:1.0.0
        String simpleKey = String.format("%s:%s", service.name, service.getMeta().version);
        //etc. 服务全名key ==> com.today.api.order.service.OrderService:1.0.0
        String fullKey = String.format("%s.%s:%s", service.namespace, service.name, service.getMeta().version);

        OptimizedMetadata.OptimizedService optimizedService = new OptimizedMetadata.OptimizedService(service);

        MetaMemoryCache.getSimpleServiceMap().put(simpleKey, optimizedService);
        MetaMemoryCache.getFullServiceMap().put(fullKey, optimizedService);
        log.info("存储服务 {} 元信息缓存成功,目前已存在的元数据数量：{}", service.getName(), MetaMemoryCache.getSimpleServiceMap().size());
    }

    private void associateMockService(String serviceName, String version, long id) {
        QMockService qService = QMockService.mockService;
        MockService mockService = queryDsl.selectFrom(qService).where(qService.serviceName.eq(serviceName).and(qService.version.eq(version))).fetchFirst();
        if (mockService != null) {
            queryDsl.update(qService).set(qService.metadataId, id).where(qService.id.eq(mockService.getId())).execute();
        }
    }


    /************************************************************************************************************************************
     *                                                                                                                                  *
     * @param targetDir                                                                                                                 *
     * @return                                                                                                                          *
     * @throws IOException                                                                                                              *
     * **********************************************************************************************************************************
     */
    public List<String> loadXmlReSourcesFromDist(String targetDir) throws IOException {
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
            log.info("========= load metadata xml resources count: {} from dir {}   =========", serviceXmlList.size(), targetDir);
        } else {
            log.error("{} 路径下没有元数据xml文件进行解析", targetDir);
        }
        return serviceXmlList;
    }

    private Service unmarshalMetadataFromStream(String metadata) {
        try (StringReader reader = new StringReader(metadata)) {
            return JAXB.unmarshal(reader, Service.class);
        } catch (Exception e) {
            log.error("metadata解析出错");
            log.error(e.getMessage(), e);
            log.info(metadata);
        }
        return null;
    }

    /************************************************************************************************************************************
     *      增删该查
     * **********************************************************************************************************************************
     */
    public QueryMetaMethodResp queryMetaDetailMethod(QueryMetaMethodReq request) {
        DmsPageReq dmsPage = request.getPageRequest();
        QMockMetadata qMeta = QMockMetadata.mockMetadata;
        MockMetadata metadata = queryDsl.selectFrom(qMeta).where(qMeta.id.eq(request.getMetadataId())).fetchFirst();


        MetaSearcher.Query metaQuery = new MetaSearcher.Query();
        metaQuery.serviceAndVersion(metadata.getServiceName(), metadata.getVersion());

        if (request.getMethodName() != null) {
            metaQuery.method(request.getMethodName());
        }
        if (dmsPage != null) {
            metaQuery.offset(dmsPage.getStart()).limit(dmsPage.getLimit());
        }
        MetaSearcher.Result queryResults = metaQuery.executeMethod();
        List<MetaMethodVo> methodVoList = queryResults.methodList.stream().map(m -> new MetaMethodVo(metadata.getServiceName(), m.getName(), m.getDoc())).collect(Collectors.toList());


        log.info("Metadata Detail Method results size: {}", methodVoList.size());

        if (dmsPage != null) {
            DmsPageResp dmsPageResp = new DmsPageResp(dmsPage.getStart(), dmsPage.getLimit(), queryResults.records);
            return new QueryMetaMethodResp(methodVoList, dmsPageResp);
        }
        return new QueryMetaMethodResp(methodVoList);
    }


    /*********************************************************************************************
     *                                                                                           *
     *  等待服务启动完成..                                                                         *
     *                                                                                           *
     * *******************************************************************************************
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<MockMetadata> metadataList = metaRepository.findAll();
        metadataList.forEach(metadata -> {
            String metadataXml = metadata.getMetadata();
            Service serviceData = unmarshalMetadataFromStream(metadataXml);
            if (serviceData != null) {
                cachedMetaService(serviceData);
            } else {
                log.warn("元数据文件名为 {} 解析 service为空，跳过 cache 数据到内存.", metadata);
            }
        });
        log.info("====================== load existing in db  metadata service end ====================== ");
    }


    public boolean deleteTargetFiles(String fullPath) {
        boolean isDelete = false;
        File targetFile = new File(fullPath);
        if (!targetFile.exists()) {
            log.error("file path [{}] was not exists ", fullPath);
            throw new MockException("根据[" + fullPath + "]目标路径未找到元数据信息");
        }
        if (targetFile.isFile()) {
            File parentFile = targetFile.getParentFile();
            return deleteDirFiles(parentFile);
        }
        return deleteDirFiles(targetFile);
    }

    private boolean deleteDirFiles(File parentFile) {
        boolean isDelete = false;
        File[] files = parentFile.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                isDelete = file.delete();
            }
        }
        isDelete = parentFile.delete();
        return isDelete;
    }
}
