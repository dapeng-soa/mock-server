package com.github.dapeng.mockserver.services;

import com.github.dapeng.mockserver.entity.Mock;
import com.github.dapeng.mockserver.repository.MockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:57 PM
 */
@Service
public class MockService {

    private final MockRepository mockRepository;

    public MockService(MockRepository mockRepository) {
        this.mockRepository = mockRepository;
    }

    public List<Mock> findMockByName(String name) {
        return mockRepository.findByName(name);
    }
}
