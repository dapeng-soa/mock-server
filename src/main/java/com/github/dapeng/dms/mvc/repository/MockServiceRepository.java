package com.github.dapeng.dms.mvc.repository;

import com.github.dapeng.dms.mvc.entity.MockServiceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:52 PM
 */
public interface MockServiceRepository extends JpaRepository<MockServiceInfo, Long> {


    MockServiceInfo findByServiceName(String name);

}
