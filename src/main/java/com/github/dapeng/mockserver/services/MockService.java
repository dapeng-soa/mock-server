package com.github.dapeng.mockserver.services;

import com.github.dapeng.mockserver.entity.Mock;
import com.github.dapeng.mockserver.entity.MockContext;
import com.github.dapeng.mockserver.matchers.HttpRequestMatcher;
import com.github.dapeng.mockserver.repository.MockRepository;
import com.github.dapeng.mockserver.request.HttpRequestContext;
import com.github.dapeng.mockserver.util.Constants;
import lombok.extern.slf4j.Slf4j;
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


    public MockService(MockRepository mockRepository) {
        this.mockRepository = mockRepository;
    }

    public List<Mock> findMockByName(String name) {
        return mockRepository.findByName(name);
    }

    public String mock(String serviceName, String version,
                       String methodName, String parameter, HttpServletRequest request) {
        //构造当前请求
        HttpRequestContext requestContext = new HttpRequestContext(serviceName, methodName, version, parameter, request);
        //获取 mock 数据
        String mockName = serviceName + Constants.KEY_SEPARATE + methodName + Constants.KEY_SEPARATE + version;
        List<Mock> mocks = mockRepository.findByName(mockName);
        mocks.sort((mock, mock2) -> (int) (mock.getId() - mock2.getId()));

        for (Mock mock : mocks) {
            MockContext mockContext = new MockContext(mock.getName(), mock.getMockExpress(), mock.getData());
            boolean isMatch = new HttpRequestMatcher(requestContext, mockContext).matches();
            if (isMatch) {
                return mockContext.getMockData();
            }
        }
        return null;
    }

    public void addMockInfo(String service, String method, String version, String mockExpress, String mockData) {
        String mockName = service + Constants.KEY_SEPARATE + method + Constants.KEY_SEPARATE + version;

        mockRepository.save(new Mock(mockName, HttpMethod.PUT.name(), mockExpress, mockData));
    }
}
