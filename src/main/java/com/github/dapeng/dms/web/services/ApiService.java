package com.github.dapeng.dms.web.services;

import com.github.dapeng.dms.mock.matchers.HttpRequestMatcher;
import com.github.dapeng.dms.mock.request.HttpRequestContext;
import com.github.dapeng.dms.util.Constants;
import com.github.dapeng.dms.web.entity.Mock;
import com.github.dapeng.dms.web.entity.MockContext;
import com.github.dapeng.dms.web.entity.MockMetadata;
import com.github.dapeng.dms.web.repository.MetadataRepository;
import com.github.dapeng.dms.web.repository.MockRepository;
import com.github.dapeng.dms.web.repository.MockServiceRepository;
import com.github.dapeng.dms.web.util.MockUtils;
import com.github.dapeng.dms.web.vo.MockServiceVo;
import com.github.dapeng.dms.web.vo.MockVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-15 11:55 PM
 */
@Service
@Slf4j
@Transactional(rollbackFor = Throwable.class)
public class ApiService {
    private final MockRepository mockRepository;

    public ApiService(MockRepository mockRepository) {
        this.mockRepository = mockRepository;
    }

    public String doMock(String serviceName, String version,
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
}
