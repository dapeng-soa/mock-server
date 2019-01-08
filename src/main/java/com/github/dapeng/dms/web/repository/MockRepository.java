package com.github.dapeng.dms.web.repository;

import com.github.dapeng.dms.web.entity.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-10-30 1:52 PM
 */
public interface MockRepository extends BaseRepository<Mock> {

    List<Mock> findMockByMockKey(String name);
}
