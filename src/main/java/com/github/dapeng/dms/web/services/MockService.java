package com.github.dapeng.dms.web.services;

import com.github.dapeng.dms.web.entity.Mock;
import com.github.dapeng.dms.web.entity.MockMetadata;
import com.github.dapeng.dms.web.entity.MockServiceInfo;
import com.github.dapeng.dms.mock.matchers.HttpRequestMatcher;
import com.github.dapeng.dms.web.repository.MetadataRepository;
import com.github.dapeng.dms.web.repository.MockServiceRepository;
import com.github.dapeng.dms.web.entity.MockContext;
import com.github.dapeng.dms.mock.matchers.validator.JsonSchemaValidator;
import com.github.dapeng.dms.web.repository.MockRepository;
import com.github.dapeng.dms.mock.request.HttpRequestContext;
import com.github.dapeng.dms.web.util.MockUtils;
import com.github.dapeng.dms.web.vo.MockServiceVo;
import com.github.dapeng.dms.web.vo.MockVo;
import com.github.dapeng.dms.util.Constants;
import com.github.dapeng.dms.web.vo.request.ServiceAddRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.List;

import static com.github.dapeng.dms.util.Constants.DEFAULT_SORT_NUM;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:57 PM
 */
@Service
@Slf4j
@Transactional(rollbackFor = Throwable.class)
public class MockService {

    private final MockRepository mockRepository;
    private final MockServiceRepository mockServiceRepository;
    private final MetadataRepository metadataRepository;


    public MockService(MockRepository mockRepository, MockServiceRepository mockServiceRepository,
                       MetadataRepository metadataRepository) {
        this.mockRepository = mockRepository;
        this.mockServiceRepository = mockServiceRepository;
        this.metadataRepository = metadataRepository;
    }


    public String mock(String serviceName, String version,
                       String methodName, String parameter, HttpServletRequest request) {
        //构造当前请求
        HttpRequestContext requestContext = new HttpRequestContext(serviceName, methodName, version, parameter, request);
        //获取 mock 数据
        String mockName = serviceName + Constants.KEY_SEPARATE + methodName + Constants.KEY_SEPARATE + version;
        List<Mock> mocks = mockRepository.findMockByMockKey(mockName);
        mocks.sort((mock, mock2) -> (int) (mock.getId() - mock2.getId()));

        for (Mock mock : mocks) {
            MockContext mockContext = new MockContext(mock.getMockKey(), mock.getMockCompileJson(),
                    mock.getData());
            boolean isMatch = new HttpRequestMatcher(requestContext, mockContext).matches();
            if (isMatch) {
                return mockContext.getMockData();
            }
        }
        return null;
    }


    /**
     * add service-info
     */
    public MockServiceVo addServiceInfo(ServiceAddRequest request) {
        MockServiceInfo serviceInfo;
        if (request.getMetadata() != null) {
            MockMetadata metadata = metadataRepository.save(new MockMetadata(request.getServiceName(), request.getMetadata(), "1.0", 1));
            serviceInfo = new MockServiceInfo(request.getServiceName(), metadata.getId(), new Timestamp(System.currentTimeMillis()));
        } else {
            serviceInfo = new MockServiceInfo(request.getServiceName(), 0L, new Timestamp(System.currentTimeMillis()));
        }
        mockServiceRepository.save(serviceInfo);
        return new MockServiceVo(serviceInfo.getId(), request.getServiceName());
    }

    /**
     * 会按照顺序进行存放mock
     *
     * @throws JSONException 如果 Json 格式不正确会抛此异常
     */
    public void addMockInfo(String service, String method, String version,
                            String mockExpress, String mockData) throws JSONException {
        JsonSchemaValidator.matcher(mockExpress);
        JsonSchemaValidator.matcher(mockData);
        //convert
        String mockCompileJson = MockUtils.convertJsonValueToPatternJson(mockExpress);

        MockServiceInfo serviceInfo = mockServiceRepository.findByServiceName(service);

        if (serviceInfo == null) {
            serviceInfo = mockServiceRepository.save(new MockServiceInfo(service, new Timestamp(System.currentTimeMillis())));
        }
        String mockKey = MockUtils.combineMockKey(service, method, version);
        Mock latestMock = mockRepository.findMockByMockKeyOrderBySortDesc(mockKey);

        if (latestMock != null) {
            Mock newMock = new Mock(service, method, version,
                    HttpMethod.POST.name(), mockExpress, mockCompileJson, mockData, serviceInfo.getId(), latestMock.getSort() + DEFAULT_SORT_NUM);
            mockRepository.save(newMock);
        } else {
            Mock newMock = new Mock(service, method, version,
                    HttpMethod.POST.name(), mockExpress, mockCompileJson, mockData, serviceInfo.getId(), DEFAULT_SORT_NUM);
            mockRepository.save(newMock);
        }
    }


    public List<MockServiceInfo> findMockServiceList() {
        return mockServiceRepository.findAll();
    }


    public List<Mock> findMockByServiceId(long serviceId) {
        return mockRepository.findMockByServiceId(serviceId);
    }

    /**
     * 根据 mock-key 查询已经添加的mock规则，会根据用户排过序的规则显示。
     */
    public List<Mock> findMockDataByMockKey(String service, String method, String version) {
        String mockKey = MockUtils.combineMockKey(service, method, version);
        List<Mock> mocks = mockRepository.findMockByMockKey(mockKey);
        mocks.sort((m1, m2) -> (int) (m1.getSort() - m2.getSort()));
        return mocks;
    }

    /**
     * 修改 mock 优先级顺序
     */
    public void modifyMockPriority(long frontId, long belowId) throws JSONException {
        Mock frontMock = MockUtils.optional(mockRepository.findById(frontId), "根据 frontId 查询不到 Mock data 信息");
        Mock belowMock = MockUtils.optional(mockRepository.findById(belowId), "根据 belowId 查询不到 Mock data 信息");

        if (frontMock.getSort() > belowMock.getSort()) {
            throw new IllegalArgumentException("修改优先级错误,frontId 顺序低于 belowId");
        }

        Mock frontBef = mockRepository.findMockBySortLimit(frontMock.getSort());
        long newSort;
        if (frontBef != null) {
            newSort = (frontMock.getSort() + frontBef.getSort()) / 2;
        } else {
            newSort = frontMock.getSort() / 2;
        }
        belowMock.setSort(newSort);
    }

    public void updateService(MockServiceVo mockServiceVo) {
        MockServiceInfo serviceInfo = mockServiceRepository.findById(mockServiceVo.getServiceId());
        serviceInfo.setServiceName(mockServiceVo.getService());
    }

    public void updateMock(MockVo mockVo) throws InvocationTargetException, IllegalAccessException {
        Mock mock = MockUtils.optional(mockRepository.findById(mockVo.getId()), "根据mock id 没有查询到Mock信息");
        BeanUtils.copyProperties(mock, mockVo);
        log.info("更新 mock 信息完成");
    }


    /**
     * metadata 信息
     *
     * @param serviceName serviceName
     */
    public List<MockMetadata> findMetadataByServiceName(String serviceName) {
        return metadataRepository.findByServiceName(serviceName);

    }










   /* public static List<Mock> mockListSort(List<Mock> data) {
        int next = 0;
        data.sort((o1, o2) -> (int) (o1.getPrevNo() - o2.getPrevNo()));
        List<Long> mockIdList = data.stream().map(Mock::getId).collect(Collectors.toList());

        List<Mock> result = new ArrayList<>();

        int size = data.size();

        while (next != -1) {
            Mock cur = data.get(next);
            result.add(cur);
            next = mockIdList.indexOf(cur.getNextNo());
            if (result.size() > size) {
                break;
            }
        }
        return result;
    }*/

}
