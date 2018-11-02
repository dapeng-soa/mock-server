package com.github.dapeng.dms.mvc.services;

import com.github.dapeng.dms.mvc.entity.Mock;
import com.github.dapeng.dms.mvc.entity.MockServiceInfo;
import com.github.dapeng.dms.mock.matchers.HttpRequestMatcher;
import com.github.dapeng.dms.mvc.repository.MockServiceRepository;
import com.github.dapeng.dms.mvc.entity.MockContext;
import com.github.dapeng.dms.mock.matchers.validator.JsonSchemaValidator;
import com.github.dapeng.dms.mvc.repository.MockRepository;
import com.github.dapeng.dms.mock.request.HttpRequestContext;
import com.github.dapeng.dms.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:57 PM
 */
@Service
@Slf4j
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
        List<Mock> mocks = mockRepository.findByMockKey(mockName);
        mocks.sort((mock, mock2) -> (int) (mock.getId() - mock2.getId()));

        for (Mock mock : mocks) {
            MockContext mockContext = new MockContext(mock.getMockKey(), mock.getMockExpress(),
                    mock.getData(), mock.getMockRuleId());
            boolean isMatch = new HttpRequestMatcher(requestContext, mockContext).matches();
            if (isMatch) {
                return mockContext.getMockData();
            }
        }
        return null;
    }

    public void addMockInfo(String service, String method, String version,
                            String mockExpress, String mockData, int ordered) throws JSONException {
        JsonSchemaValidator.matcher(mockExpress);
        JsonSchemaValidator.matcher(mockData);

        MockServiceInfo serviceInfo = mockServiceRepository.findByServiceName(service);

        if (serviceInfo == null) {
            serviceInfo = mockServiceRepository.save(new MockServiceInfo(service));

        }
        mockRepository.save(new Mock(service, method, version, HttpMethod.POST.name(), mockExpress, mockData, ordered, serviceInfo.getId()));
    }


    public List<MockServiceInfo> findMockServiceList() {
        return mockServiceRepository.findAll();
    }


    public List<Mock> findMockByServiceName(String serviceName) {
        return mockRepository.findMockByServiceName(serviceName);
    }
}
