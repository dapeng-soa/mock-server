package com.github.dapeng.dms.mvc.repository;

import com.github.dapeng.dms.mvc.entity.Mock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:52 PM
 */
public interface MockRepository extends JpaRepository<Mock, Long> {

    List<Mock> findByMockKey(String name);

    List<Mock> findMockByServiceName(String name);
}
