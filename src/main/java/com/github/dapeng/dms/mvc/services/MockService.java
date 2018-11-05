package com.github.dapeng.dms.mvc.services;

import com.github.dapeng.dms.mvc.entity.Mock;
import com.github.dapeng.dms.mvc.entity.MockServiceInfo;
import com.github.dapeng.dms.mock.matchers.HttpRequestMatcher;
import com.github.dapeng.dms.mvc.repository.MockServiceRepository;
import com.github.dapeng.dms.mvc.entity.MockContext;
import com.github.dapeng.dms.mock.matchers.validator.JsonSchemaValidator;
import com.github.dapeng.dms.mvc.repository.MockRepository;
import com.github.dapeng.dms.mock.request.HttpRequestContext;
import com.github.dapeng.dms.mvc.util.MockUtils;
import com.github.dapeng.dms.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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


    public MockService(MockRepository mockRepository, MockServiceRepository mockServiceRepository) {
        this.mockRepository = mockRepository;
        this.mockServiceRepository = mockServiceRepository;
    }

    public List<Mock> findMockByName(String name) {
        return mockRepository.findMockByServiceName(name);
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
            MockContext mockContext = new MockContext(mock.getMockKey(), mock.getMockExpress(),
                    mock.getData(), mock.getMockRule());
            boolean isMatch = new HttpRequestMatcher(requestContext, mockContext).matches();
            if (isMatch) {
                return mockContext.getMockData();
            }
        }
        return null;
    }

    /**
     * 会按照顺序进行存放mock
     *
     * @throws JSONException 如果 Json 格式不正确会抛此异常
     */
    public void addMockInfo(String service, String method, String version,
                            String mockExpress, String mockData) throws JSONException {
        //判断request Json 是否合法。
        JsonSchemaValidator.matcher(mockExpress);
        JsonSchemaValidator.matcher(mockData);

        MockServiceInfo serviceInfo = mockServiceRepository.findByServiceName(service);

        if (serviceInfo == null) {
            serviceInfo = mockServiceRepository.save(new MockServiceInfo(service, new Timestamp(System.currentTimeMillis())));
        }
        String mockKey = MockUtils.combineMockKey(service, method, version);
        Mock latestMock = mockRepository.findMockByMockKeyAndGroupNextNo(mockKey, 0L);

        if (latestMock != null) {
            Mock newMock = new Mock(service, method, version,
                    HttpMethod.POST.name(), mockExpress, mockData, serviceInfo.getId(), latestMock.getId());
            mockRepository.save(newMock);
            // update latestMock nextNo when transaction committed.
            latestMock.setNextNo(newMock.getId());
        } else {
            Mock newMock = new Mock(service, method, version,
                    HttpMethod.POST.name(), mockExpress, mockData, serviceInfo.getId());
            mockRepository.save(newMock);
        }
    }


    public List<MockServiceInfo> findMockServiceList() {
        return mockServiceRepository.findAll();
    }


    public List<Mock> findMockByServiceName(String serviceName) {
        return mockRepository.findMockByServiceName(serviceName);
    }

    /**
     * 根据 mock-key 查询已经添加的mock规则，会根据用户排过序的规则显示。
     */
    public List<Mock> findMockDataByMockKey(String service, String method, String version) {
        String mockKey = MockUtils.combineMockKey(service, method, version);
        List<Mock> mocks = mockRepository.findMockByMockKey(mockKey);
        mockListSort(mocks);
        return mocks;
    }

    /**
     * 修改 mock 优先级顺序
     */
    public void modifyMockPriority(long frontId, long belowId) throws JSONException {
        Mock frontMock = MockUtils.optional(mockRepository.findById(frontId), "根据 frontId 查询不到 Mock data 信息");
        Mock belowMock = MockUtils.optional(mockRepository.findById(belowId), "根据 belowId 查询不到 Mock data 信息");
        //todo 校验 front 再 below 上面
        List<Mock> mocks = mockRepository.findMockByMockKey(frontMock.getMockKey());
        mockListSort(mocks);
        System.out.println(mocks);

    }

    public static void mockListSort(List<Mock> data) {
        int next = 0;
        data.sort((o1, o2) -> (int) (o1.getPrevNo() - o2.getPrevNo()));
        List<Long> mockIdList = data.stream().map(Mock::getId).collect(Collectors.toList());

        List<Mock> result = new ArrayList<>();

        while (next != -1) {
            Mock cur = data.get(next);
            result.add(cur);
            next = mockIdList.indexOf(cur.getNextNo());
        }
    }

}