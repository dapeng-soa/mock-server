package com.github.dapeng.mockserver.repository;

import com.github.dapeng.mockserver.entity.MockServiceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:52 PM
 */
public interface MockServiceRepository extends JpaRepository<MockServiceInfo, Long> {


    MockServiceInfo findByServiceName(String name);

}
