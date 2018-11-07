package com.github.dapeng.dms.mvc.repository;

import com.github.dapeng.dms.mvc.entity.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:52 PM
 */
public interface MockRepository extends JpaRepository<Mock, Long> {

    List<Mock> findMockByMockKey(String name);

    List<Mock> findMockByServiceId(long serviceId);


    @Query(nativeQuery = true, value = "select * from mock_data where mock_key =?1  order by sort  desc limit 1")
    Mock findMockByMockKeyOrderBySortDesc(String mockKey);


    @Query(nativeQuery = true, value = "select * from mock_data where sort <?1  order by sort  desc limit 1")
    Mock findMockBySortLimit(long sort);
}
